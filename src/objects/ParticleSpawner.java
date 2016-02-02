package objects;

import core.Vec3;

import java.awt.Color;

public class ParticleSpawner extends GameObject
{
	private double m_rate;
	private Color m_color;
	private double m_particleSize;
	private double m_radius;

	private Vec3 m_gravity;

	private int m_minLife;
	private int m_maxLife;

	public ParticleSpawner(double rate, Color color, double particleSize, 
						   double radius, Vec3 gravity, int minLife, int maxLife)
	{
		super();

		m_rate = rate;
		m_color = color;
		m_particleSize = particleSize;
		m_radius = radius;

		m_gravity = new Vec3(gravity);

		m_minLife = minLife;
		m_maxLife = maxLife; 
	}

	public void update()
	{
		super.update();

		if (Math.random() < m_rate)
		{
			double theta = randomDouble(0.0, Math.PI * 2);
			double phi = randomDouble(0.0, Math.PI * 2);
			Vec3 velocity = new Vec3(m_radius * Math.cos(theta) * Math.sin(phi),
									 m_radius * Math.sin(theta) * Math.sin(phi),
									 m_radius * Math.cos(phi));

			double size = (randomDouble(-0.5, 0.5) + 1) * m_particleSize;
			int life = (int) randomDouble((double) m_minLife, (double) m_maxLife);
			
			addChild(new Particle(m_position, velocity, m_gravity, size, m_color, life));
		}
	}

	private static double randomDouble(double min, double max)
	{
		return min + (Math.random() * (max - min));
	}
}