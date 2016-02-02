package core;

public class Vec3
{
	public double x;
	public double y;
	public double z;

	public Vec3()
	{
		x = 0;
		y = 0;
		z = 0;
	}

	public Vec3(double px, double py, double pz)
	{
		x = px;
		y = py;
		z = pz;
	}

	public Vec3(Vec3 v)
	{
		x = v.x;
		y = v.y;
		z = v.z;
	}

	public void set(Vec3 v)
	{
		x = v.x;
		y = v.y;
		z = v.z;
	}

	public void set(double px, double py, double pz)
	{
		x = px;
		y = py;
		z = pz;
	}

	public void add(Vec3 v)
	{
		x += v.x;
		y += v.y;
		z += v.z;
	}

	public void add(double dx, double dy, double dz)
	{
		x += dx;
		y += dy;
		z += dz;
	}

	public void subtract(Vec3 v)
	{
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}

	public void subtract(double dx, double dy, double dz)
	{
		x -= dx;
		y -= dy;
		z -= dz;
	}

	public void multiply(double s)
	{
		x *= s;
		y *= s;
		z *= s;
	}

	public void multiply(double sx, double sy, double sz)
	{
		x *= sx;
		y *= sy;
		z *= sz;
	}

	public void rotate(Vec3 sin, Vec3 cos)
	{
		double ox = x;
		double oy = y;
		x = (cos.z * ox) - (sin.z * oy);
		y = (sin.z * ox) + (cos.z * oy);

		// Y Axis
		ox = x;
		double oz = z;
		x = (cos.y * ox) - (sin.y * oz);
		z = (sin.y * ox) + (cos.y * oz);

		// X Axis
		oz = z;
		oy = y;
		z = (cos.x * oz) - (sin.x * oy);
		y = (sin.x * oz) + (cos.x * oy);
	}

	public Vec3 getSin()
	{
		return new Vec3(Math.sin(x), Math.sin(y), Math.sin(z));
	}

	public Vec3 getCos()
	{
		return new Vec3(Math.cos(x), Math.cos(y), Math.cos(z));
	}

	public void normalize()
	{
		double length = length();
		x /= length;
		y /= length;
		z /= length;
	}

	public double length()
	{
		return Math.sqrt(length2());
	}

	public double length2()
	{
		return (x * x) + (y * y) + (z * z);
	}

	public static double dot(Vec3 v1, Vec3 v2)
	{
		return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
	}

	public static double distance2(Vec3 v1, Vec3 v2)
	{
		double dx = v2.x - v1.x;
		double dy = v2.y - v1.y;
		double dz = v2.z - v1.z;

		return (dx * dx) + (dy * dy) + (dz * dz);
	}

	public static Vec3 fromString(String data)
	{
		String[] parsed = data.split(", ");
		return new Vec3(Double.parseDouble(parsed[0]),
						Double.parseDouble(parsed[1]),
						Double.parseDouble(parsed[2]));
	}

	public String toString()
	{
		return x + ", " + y + ", " + z;
	}
}