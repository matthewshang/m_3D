package objects;

import core.Vec3;

import java.awt.Color;

public class Surface
{
	private Vec3 m_center;
	private Vec3[] m_vertices;
	private Vec3 m_normal;
	private Color m_color;

	private boolean m_canBeLit;

	public Surface(Vec3[] vertices, Color color, boolean canBeLit)
	{
		m_vertices = new Vec3[vertices.length];
		System.arraycopy(vertices, 0, m_vertices, 0, vertices.length);

		m_center = calculateCenter(m_vertices);
		m_normal = calculateNormal(m_vertices);

		m_color = color;

		m_canBeLit = canBeLit;
	}

	private Vec3 calculateCenter(Vec3[] vertices)
	{
		Vec3 center = new Vec3();

		for (int i = 0; i < vertices.length; i++)
		{
			center.add(m_vertices[i]);
		}

		center.multiply(1.0 / vertices.length);
		return center;
	}

	private Vec3 calculateNormal(Vec3[] vertices)
	{
		Vec3 normal = new Vec3();

		for (int i = 0; i < vertices.length; i++)
		{
			Vec3 current = vertices[i];
			Vec3 next = vertices[(i + 1) % vertices.length];
			normal.add((current.y - next.y) * (current.z + next.z),
					   (current.z - next.z) * (current.x + next.x),
					   (current.x - next.x) * (current.y + next.y));
		}

		normal.normalize();
		return normal;
	}

	public Vec3 getNormal()
	{
		return m_normal;
	}

	public boolean canBeLit()
	{
		return m_canBeLit;
	}

	public Vec3[] getVertices()
	{
		return m_vertices;
	}

	public Vec3 getCenter()
	{
		return m_center;
	}

	public void setColor(double r, double g, double b)
	{
		m_color = new Color((int) Math.min(255, r), (int) Math.min(255, g), (int) Math.min(255, b));
	}

	public Color getColor()
	{
		return m_color;
	}
}