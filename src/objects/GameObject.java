package objects;

import core.Camera;
import core.Vec3;
import core.Point2;

import java.util.ArrayList;
import java.awt.Color;

public class GameObject
{
	protected Vec3 m_position;
	protected Vec3 m_rotation;
	protected double m_scale;

	protected boolean m_canBeLit;
	protected boolean m_isVisible;

	protected Vec3 m_rotVelo;
	protected ArrayList<Vec3> m_vertices;
	protected ArrayList<SurfaceReference> m_surfaces;

	protected ArrayList<GameObject> m_childObjects;

	public GameObject()
	{
		m_vertices = new ArrayList<Vec3>();
		m_surfaces = new ArrayList<SurfaceReference>();
		
		m_position = new Vec3();
		m_rotation = new Vec3();
		m_scale = 1.0;
		
		m_rotVelo = new Vec3();

		m_childObjects = new ArrayList<GameObject>();

		m_canBeLit = true;
		m_isVisible = true;
	}

	public GameObject copy()
	{
		GameObject copy = new GameObject();
		copy.setPosition(m_position);
		copy.setRotation(m_rotation);
		copy.setScale(m_scale);
		copy.setLighting(m_canBeLit);
		copy.setVisible(m_isVisible);
		copy.setRotVelo(m_rotVelo);
		copy.m_vertices = new ArrayList<Vec3>(m_vertices);
		copy.m_surfaces.ensureCapacity(m_surfaces.size());

		for (int i = 0; i < m_surfaces.size(); i++)
		{
			copy.m_surfaces.add(m_surfaces.get(i).copy());
		}


		copy.m_childObjects.ensureCapacity(m_childObjects.size());
		for (int i = 0; i < m_childObjects.size(); i++)
		{
			copy.m_childObjects.add(m_childObjects.get(i).copy());
		}

		return copy;
	}

	public void setSurfaceColors(Color color)
	{
		for (int i = 0; i < m_surfaces.size(); i++)
		{
			m_surfaces.get(i).setColor(color);
		}

		for (int i = 0; i < m_childObjects.size(); i++)
		{
			m_childObjects.get(i).setSurfaceColors(color);
		}
	}

	public void setLighting(boolean canBeLit)
	{
		m_canBeLit = canBeLit;
	}

	public void setVisible(boolean isVisible)
	{
		m_isVisible = isVisible;
	}

	public void setPosition(Vec3 position)
	{
		m_position.set(position);
	}

	public void changePosition(Vec3 dp)
	{
		m_position.add(dp);
	}

	public Vec3 getPosition()
	{
		return m_position;
	}

	public void setRotation(Vec3 rotation)
	{
		m_rotation.set(rotation);
	}

	public Vec3 getRotation()
	{
		return m_rotation;
	}

	public void setScale(double scale)
	{
		m_scale = scale;
	}

	public void setRotVelo(Vec3 rotVelo)
	{
		m_rotVelo.set(rotVelo);
	}

	public void update()
	{
		m_rotation.add(m_rotVelo);
		updateChildren();
	}

	private void updateChildren()
	{
		for (int i = 0; i < m_childObjects.size(); i++)
		{
			m_childObjects.get(i).update();
		}
	}

	public boolean inAim(Camera camera)
	{
		Vec3 camPos = camera.getPosition();
		Vec3 camRot = camera.getRotation();

		ArrayList<Surface> surfaces = getSurfaces();

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

			Point2[] projected = new Point2[vertices.length];

			for (int j = 0; j < projected.length; j++)
			{
				double sx = vertices[j].x * 400 / vertices[j].z;
				double sy = -1 * vertices[j].y * 400 / vertices[j].z;

				sx += 400;
				sy += 400;

				projected[j] = new Point2(sx, sy);
			}

			Point2 p0 = projected[0];
			for (int j = 0; j < projected.length - 2; j++)
			{
				if (!(vertices[j].z > 0 && vertices[j + 1].z > 0 && vertices[j + 2].z > 0))
				{
					continue;
				}

				if (pointInTriangle(new Point2(400, 400), p0, projected[j + 1], projected[j + 2]))
				{
					return true;
				}
			}
		}

		return false;
	}

	private boolean pointInTriangle(Point2 p, Point2 a, Point2 b, Point2 c)
	{
		double triAreaTimesTwo = triAreaTimesTwo(a, b, c);
		Point2 center = new Point2(a);
		center.add(b);
		center.add(c);
		center.multiply(1.0 / 3.0);
		
		double a01 = triAreaTimesTwo(p, a, b);
		double a12 = triAreaTimesTwo(p, b, c);
		double a20 = triAreaTimesTwo(p, c, a);

		return (a01 + a12 + a20) - triAreaTimesTwo < 1e-6;
	}

	private double triAreaTimesTwo(Point2 a, Point2 b, Point2 c)
	{
		return Math.abs((a.x * b.y + b.x * c.y + c.x * a.y) -
						(a.y * b.x + b.y * c.x + c.y * a.x));
	}
	
	public ArrayList<Surface> getSurfaces()
	{
		ArrayList<Surface> surfaces = new ArrayList<Surface>(m_surfaces.size());

		if (m_isVisible)
		{
			Vec3 sinRot = m_rotation.getSin();
			Vec3 cosRot = m_rotation.getCos();

			for (int i = 0; i < m_surfaces.size(); i++)
			{
				SurfaceReference surfaceRef = m_surfaces.get(i);

				int[] indices = surfaceRef.getVertexIndices();
				Vec3[] vertices = new Vec3[indices.length];

				for (int j = 0; j < indices.length; j++)
				{
					vertices[j] = new Vec3(m_vertices.get(indices[j]));
				}

				surfaces.add(new Surface(
								getTransformedVertices(vertices, m_scale, 
													   sinRot, cosRot, m_position), 
								surfaceRef.getColor(), 
								m_canBeLit));
			}

			for (int i = 0; i < m_childObjects.size(); i++)
			{
				ArrayList<Surface> childSurfaces = m_childObjects.get(i).getSurfaces();

				for (int j = 0; j < childSurfaces.size(); j++)
				{
					Surface surface = childSurfaces.get(j);

					surfaces.add(new Surface(
									getTransformedVertices(surface.getVertices(), 
														   m_scale, sinRot, cosRot, 
														   m_position),
									surface.getColor(),
									m_canBeLit));
				}
			}
		}

		return surfaces;
	}

	private Vec3[] getTransformedVertices(Vec3[] vertices, double scale, 
									 Vec3 sinRot, Vec3 cosRot, Vec3 position)
	{
		Vec3[] transformed = new Vec3[vertices.length];

		for (int i = 0; i < vertices.length; i++)
		{
			Vec3 vertex = new Vec3(vertices[i]);

			vertex.multiply(scale);
			vertex.rotate(sinRot, cosRot);
			vertex.add(position);

			transformed[i] = new Vec3(vertex);
		}

		return transformed;
	}

	public void addVertex(double x, double y, double z)
	{
		m_vertices.add(new Vec3(x, y, z));
	}

	public void addSurface(Color color, int... vertexIndices)
	{
		m_surfaces.add(new SurfaceReference(vertexIndices, color));
	}

	public void addChild(GameObject object)
	{
		m_childObjects.add(object);
	}

	protected ArrayList<SurfaceReference> getSurfaceRefs()
	{
		return m_surfaces;
	}

	private class SurfaceReference
	{
		private int[] m_vertexIndices;
		private Color m_color;

		public SurfaceReference(int[] vertexIndices, Color color)
		{
			m_vertexIndices = vertexIndices;

			m_color = color;
		}

		public SurfaceReference copy()
		{
			SurfaceReference copy = new SurfaceReference(m_vertexIndices, m_color);
			return copy;
		}

		public void setColor(Color color)
		{
			m_color = color;
		}

		public int[] getVertexIndices()
		{
			return m_vertexIndices;
		}

		public Color getColor()
		{
			return m_color;
		}
	}
}