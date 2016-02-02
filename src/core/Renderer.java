package core;

import objects.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.geom.*;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Arrays;

public class Renderer extends JPanel
{
	private int m_width;
	private int m_height;

	private World m_world;

	private boolean m_backfaceCulling;
	private boolean m_fillSurfaces;

	public Renderer(World world, int width, int height)
	{
		m_width = width;
		m_height = height;

		m_world = world;

		m_backfaceCulling = true;
		m_fillSurfaces = true;
	}

	public void setBackfaceCulling(boolean culling)
	{
		m_backfaceCulling = culling;
	}

	public void setSurfaceFill(boolean fill)
	{
		m_fillSurfaces = fill;
	}

	public void paintComponent(Graphics g)
	{
		long startTime = System.nanoTime();

		super.paintComponent(g);
		renderWorld(g);

		g.drawString("Render time: " + 
				Math.round((System.nanoTime() - startTime) / 1000000.0), 20, 100);
	}

	private void renderWorld(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							 RenderingHints.VALUE_ANTIALIAS_ON);

		ArrayList<Surface> surfaces = new ArrayList<Surface>();

		ArrayList<GameObject> objects = m_world.getObjects();
		for (int i = 0; i < objects.size(); i++)
		{
			surfaces.addAll(objects.get(i).getSurfaces());
		}

		if (surfaces.size() != 0)
		{
			sortSurfaces(surfaces);
			lightSurfaces(surfaces, m_world.getLights());
			transformSurfaces(surfaces);
			renderHorizon(g2d);
			renderSurfaces(surfaces, g2d);
		}

		g.setColor(Color.BLACK);

		g.drawLine(m_width / 2, (m_height / 2) - 10, m_width / 2, (m_height / 2) + 10);
		g.drawLine((m_width / 2) - 10, m_height / 2, (m_width / 2) + 10, m_height / 2);

		Vec3 camPos = m_world.getCamera().getPosition();
		Vec3 camRot = m_world.getCamera().getRotation();

		g.drawString("Camera Position: " + 
							Math.round(camPos.x) + 
					 ", " + Math.round(camPos.y) + 
					 ", " + Math.round(camPos.z), 20, 20);
		g.drawString("Camera Rotation: " + 
							Math.round(180 * camRot.x / Math.PI) + 
					 ", " + Math.round(180 * camRot.y / Math.PI) + 
					 ", " + Math.round(180 * camRot.z / Math.PI), 20, 40);

	}

	private void sortSurfaces(ArrayList<Surface> surfaces)
	{
		int length = surfaces.size();
		double[] distances = new double[length];
		int[] indices = new int[length];

		Vec3 camPos = m_world.getCamera().getPosition();

		for (int i = 0; i < length; i++)
		{
			distances[i] = Vec3.distance2(surfaces.get(i).getCenter(), camPos);
			indices[i] = i;
		}

		quicksort(distances, indices, 0, distances.length - 1);

		Surface[] orderedSurfaces = new Surface[length];

		for (int i = 0; i < length; i++)
		{
			orderedSurfaces[i] = surfaces.get(indices[i]);
		}

		surfaces.clear();
		surfaces.addAll(Arrays.asList(orderedSurfaces));
	}

	private void quicksort(double[] distances, int[] indices, int left, int right)
	{
		double pivot = distances[(int) Math.floor((left + right) / 2)];
		int i = left;
		int j = right;

		while (i <= j)
		{
			while (distances[i] > pivot && i < right)
			{
				i++;
			}

			while (distances[j] < pivot && j > left)
			{
				j--;
			}

			if (i <= j)
			{
				double tmp1 = distances[i];
				distances[i] = distances[j];
				distances[j] = tmp1;
				int tmp2 = indices[i];
				indices[i] = indices[j];
				indices[j] = tmp2;
				i++;
				j--;
			}
		}

		if (left < j)
		{
			quicksort(distances, indices, left, j);
		}

		if (i < right)
		{
			quicksort(distances, indices, i, right);
		}
	}

	private void transformSurfaces(ArrayList<Surface> surfaces)
	{
		Camera camera = m_world.getCamera();

		Vec3 camPos = camera.getPosition();
		Vec3 camRot = camera.getRotation();

		Vec3 sinCam = camRot.getSin();
		Vec3 cosCam = camRot.getCos();

		for (int i = 0; i < surfaces.size(); i++)
		{
			Surface face = surfaces.get(i);

			Vec3[] vertices = face.getVertices();

			for (int j = 0; j < vertices.length; j++)
			{
				Vec3 v = vertices[j];

				v.subtract(camPos);
				v.rotate(sinCam, cosCam);
			}
		}
	}

	private void lightSurfaces(ArrayList<Surface> surfaces, ArrayList<Light> lights)
	{
		if (lights.size() > 0)
		{
			Vec3[] normals = getNormals(surfaces);

			for (int i = 0; i < normals.length; i++)
			{
				Surface surface = surfaces.get(i);

				if (surface.canBeLit())
				{
					Vec3 surfaceCenter = surface.getCenter();

					double totalR = 0;
					double totalG = 0;
					double totalB = 0;

					for (int j = 0; j < lights.size(); j++)
					{
						Light light = lights.get(j);
						double radius = light.getRadius();

						Vec3 lightToSurface = new Vec3(light.getPosition());
						lightToSurface.subtract(surfaceCenter);

						double distance = lightToSurface.length();
						double cosA = Vec3.dot(lightToSurface, normals[i]) / distance;
						cosA = Math.max(0.5, cosA);
						double intensity = Math.min(1 / cosA, radius / distance);

						Color lightColor = light.getColor();

						intensity *= cosA;

						totalR += (lightColor.getRed() / 255) * intensity;
						totalG += (lightColor.getGreen() / 255) * intensity;
						totalB += (lightColor.getBlue() / 255) * intensity;
					}

					Color surfaceColor = surface.getColor();
					double r = surfaceColor.getRed();
					double g = surfaceColor.getGreen();
					double b = surfaceColor.getBlue();

					surface.setColor(r * totalR, g * totalG, b * totalB);
				}
			}
		}
	}

	private Vec3[] getNormals(ArrayList<Surface> surfaces)
	{
		Vec3[] normals = new Vec3[surfaces.size()];

		for (int i = 0; i < normals.length; i++)
		{
			// Newell's Method
			normals[i] = surfaces.get(i).getNormal();
		}

		return normals;
	}

	private void renderHorizon(Graphics2D g)
	{
		double camRotX = m_world.getCamera().getRotation().x;
		Vec3 horizon = new Vec3(0, 0, 1);
		horizon.rotate(new Vec3(Math.sin(camRotX), 0, 0), new Vec3(Math.cos(camRotX), 1, 1));
		int y = (int) pointToScreen(horizon).y;

		if (y > 800)
		{
			g.setColor(new Color(102, 178, 255));
			g.fillRect(0, 0, m_width, m_height);
		}
		else if (y < 0)
		{
			g.setColor(new Color(76, 153, 0));
			g.fillRect(0, 0, m_width, m_height);
		}
		else
		{
			g.setColor(new Color(76, 153, 0));
			g.fillRect(0, y, m_width, m_height - y);
			g.setColor(new Color(102, 178, 255));
			g.fillRect(0, 0, m_width, y);
		}
	}

	private void renderSurfaces(ArrayList<Surface> surfaces, Graphics2D g)
	{
		int totalPolygons = surfaces.size();
		int drawnPolygons = 0;

		for (int i = 0; i < totalPolygons; i++)
		{
			Surface face = surfaces.get(i);

			Vec3[] vertices = face.getVertices();

			Point2[] projected = new Point2[vertices.length];

			for (int j = 0; j < projected.length; j++)
			{
				projected[j] = pointToScreen(vertices[j]);
			}

			if (m_backfaceCulling && getSignedArea(projected) < 0 
				|| !isPolygonInScreen(projected))
			{
				continue;
			}

			drawnPolygons++;

			g.setColor(face.getColor());

			if (m_fillSurfaces)
			{
				fillSurface(vertices, projected, g);
			}
			else
			{
				outlineSurface(vertices, projected, g);
			}
		}

		g.setColor(Color.BLACK);
		g.drawString("Total Polygons: " + Integer.toString(totalPolygons), 20, 60);
		g.drawString("Drawn Polygons: " + Integer.toString(drawnPolygons), 20, 80);
	}

	private void fillSurface(Vec3[] vertices, Point2[] projected, Graphics2D g)
	{
		Point2 p1 = projected[0];
		double z1 = vertices[0].z;

		for (int i = 1; i < vertices.length - 1; i++)
		{
			double z2 = vertices[i].z;
			double z3 = vertices[i + 1].z;

			if (z1 > 0 && z2 > 0 && z3 > 0)
			{
				Point2 p2 = projected[i];
				Point2 p3 = projected[i + 1];

				fill2DTriangle(p1, p2, p3, g);

				g.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
				g.draw(new Line2D.Double(p2.x, p2.y, p3.x, p3.y));
				g.draw(new Line2D.Double(p3.x, p3.y, p1.x, p1.y));
			}
		}
	}

	private void outlineSurface(Vec3[] vertices, Point2[] projected, Graphics2D g)
	{
		for (int i = 0; i < vertices.length; i++)
		{
			Point2 p1;
			Point2 p2;
			double z1;
			double z2;

			if (i == vertices.length - 1)
			{
				p1 = projected[i];
				p2 = projected[0];
				z1 = vertices[i].z;
				z2 = vertices[0].z;
			}
			else
			{
				p1 = projected[i];
				p2 = projected[i + 1];
				z1 = vertices[i].z;
				z2 = vertices[i + 1].z;
			}

			if (z1 > 0 && z2 > 0)
			{
				g.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
			}
		}
	}

	private double getSignedArea(Point2[] polygon)
	{
		double area = 0;

		for (int i = 0; i < polygon.length; i++)
		{
			double x1 = polygon[i].x;
			double y1 = polygon[i].y;
			double x2;
			double y2;

			if (i == polygon.length - 1)
			{
				x2 = polygon[0].x;
				y2 = polygon[0].y;
			}
			else
			{
				x2 = polygon[i + 1].x;
				y2 = polygon[i + 1].y;
			}

			area += (x1 * y2 - x2 * y1);
		}

		return area;
	}

	private Point2 pointToScreen(Vec3 point)
	{
		double sx = point.x * (m_width / 2) / point.z;
		double sy = -1 * point.y * (m_height / 2) / point.z;

		sx += (m_width / 2);
		sy += (m_height / 2);

		return new Point2(sx, sy);
	}

	private boolean isPolygonInScreen(Point2[] polygon)
	{
		for (int i = 0; i < polygon.length; i++)
		{
			if (polygon[i].x > 0 && polygon[i].y > 0 && 
				polygon[i].x < m_width && polygon[i].y < m_height)
			{
				return true;
			}
		}

		return false;
	}

	private void fill2DTriangle(Point2 p1, Point2 p2, Point2 p3, Graphics2D g)
	{
		Path2D p = new Path2D.Double();

		p.moveTo(p1.x, p1.y);
		p.lineTo(p2.x, p2.y);
		p.lineTo(p3.x, p3.y);
		p.closePath();

		g.fill(p);
	}
}