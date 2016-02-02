package core;

public class Point2
{
	public double x;
	public double y;

	public Point2(double px, double py)
	{
		x = px;
		y = py;
	}

	public Point2(Point2 p)
	{
		x = p.x;
		y = p.y;
	}

	public void set(double px, double py)
	{
		x = px;
		y = py;
	}

	public void set(Point2 p)
	{
		x = p.x;
		y = p.y;
	}

	public void add(Point2 p)
	{
		x += p.x;
		y += p.y;
	}

	public void multiply(double s)
	{
		x *= s;
		y *= s;
	}

	public void print()
	{
		System.out.println(x + ", " + y);
	}
}