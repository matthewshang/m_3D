package core;

import java.awt.Color;

public class Light
{
	private Vec3 m_position;
	private double m_radius;
	private int m_objectIndex;
	private Color m_color;

	public Light(Vec3 position, double radius, Color color)
	{
		m_position = new Vec3(position);
		m_radius = radius;
		m_color = color;
	}

	public void setObjectIndex(int index)
	{
		m_objectIndex = index;
	}

	public Color getColor()
	{
		return m_color;
	}

	public int getObjectIndex()
	{
		return m_objectIndex;
	}

	public Vec3 getPosition()
	{
		return m_position;
	}

	public double getRadius()
	{
		return m_radius;
	}

	public void setPosition(Vec3 position)
	{
		m_position.set(position);
	}
}