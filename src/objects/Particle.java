package objects;

import core.Vec3;

import java.awt.Color;

public class Particle extends Box
{
	private int m_frameLife;

	private Vec3 m_velocity;
	private Vec3 m_gravity;

	public Particle(Vec3 position, Vec3 velocity, Vec3 gravity, 
					double size, Color color, int life)
	{
		super(new Vec3(1, 1, 1));

		setScale(size);
		setSurfaceColors(color);
		setPosition(position);

		m_frameLife = life;
		m_velocity = new Vec3(velocity);
		m_gravity = new Vec3(gravity);
	}

	public void update()
	{
		if (m_frameLife > 0)
		{
			changePosition(m_velocity);
			m_velocity.add(m_gravity);
			m_frameLife--;
		}
		else
		{
			setVisible(false);
		}
	}
}

