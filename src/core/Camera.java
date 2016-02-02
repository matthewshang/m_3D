package core;

public class Camera
{
	public enum Movement
	{
		FOWARD, BACKWARD, LEFT, RIGHT, UP, DOWN
	}

	private Vec3 m_position;
	private Vec3 m_moveVelo;
	private Vec3 m_rotation;
	private Vec3 m_rotVelo;
	private double m_moveSpeed;
	private double m_rotSpeed;

	public Camera()
	{
		m_position = new Vec3();
		m_moveVelo = new Vec3();
		m_rotation = new Vec3();
		m_rotVelo = new Vec3();
		m_moveSpeed = 0.05;
		m_rotSpeed = Math.PI / 360;
	}

	public void setPosition(Vec3 position)
	{
		m_position.set(position);
	}

	public void setRotation(Vec3 rotation)
	{
		m_rotation.set(rotation);
	}

	public void setSpeed(double moveSpeed, double rotSpeed)
	{
		m_moveSpeed = moveSpeed;
		m_rotSpeed = rotSpeed;
	}

	public Vec3 getPosition()
	{
		return m_position;
	}

	public Vec3 getRotation()
	{
		return m_rotation;
	}

	public void update()
	{
		m_position.add(m_moveVelo);
		m_moveVelo.multiply(0.7);

		m_rotation.add(m_rotVelo);
		m_rotVelo.multiply(0.7);
	}

	public void rotate(Movement rotation)
	{
		switch (rotation)
		{
			case RIGHT:
				m_rotVelo.add(0.0, m_rotSpeed, 0.0);
				break;

			case LEFT:
				m_rotVelo.add(0.0, m_rotSpeed * -1, 0.0);
				break;

			case UP:
				if (m_rotation.x - m_rotSpeed > Math.PI / -2)
				{
					m_rotVelo.add(m_rotSpeed * -1, 0.0, 0.0);
				}
				else
				{
					m_rotation.set(Math.PI / -2, m_rotation.y, m_rotation.z);
				}

				break;

			case DOWN:
				if (m_rotation.x + m_rotSpeed < Math.PI / 2)
				{
					m_rotVelo.add(m_rotSpeed, 0.0, 0.0);
				}
				else
				{
					m_rotation.set(Math.PI / 2, m_rotation.y, m_rotation.z);
				}

				break;

			default:
				break;
		}
	}

	public void move(Movement movement)
	{
		switch (movement)
		{
			case FOWARD:
				m_moveVelo.add(Math.sin(m_rotation.y) * m_moveSpeed, 
							   0.0, 
							   Math.cos(m_rotation.y) * m_moveSpeed);
				break;

			case BACKWARD:
				m_moveVelo.add(Math.sin(m_rotation.y - Math.PI) * m_moveSpeed,
							   0.0,
							   Math.cos(m_rotation.y - Math.PI) * m_moveSpeed);
				break;

			case LEFT:
				m_moveVelo.add(Math.sin(m_rotation.y - Math.PI / 2) * m_moveSpeed,
							   0.0,
							   Math.cos(m_rotation.y - Math.PI / 2) * m_moveSpeed);
				break;

			case RIGHT:
				m_moveVelo.add(Math.sin(m_rotation.y + Math.PI / 2) * m_moveSpeed,
							   0.0, 
							   Math.cos(m_rotation.y + Math.PI / 2) * m_moveSpeed);
				break;

			case UP:
				m_moveVelo.add(0.0, m_moveSpeed, 0.0);
				break;

			case DOWN:
				m_moveVelo.add(0.0, -1 * m_moveSpeed, 0.0);
				break;

			default:
				break;
		}
	}
}