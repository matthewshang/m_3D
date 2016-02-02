package objects;

import java.awt.Color;

public class Pyramid extends GameObject
{
	public Pyramid(double baseLength, double height)
	{
		super();

		addVertex(0.0, height, 0.0);                              // 0

		addVertex(baseLength, height * -1, baseLength);           // 1
		addVertex(baseLength, height * -1, baseLength * -1);      // 2
		addVertex(baseLength * -1, height * -1, baseLength * -1); // 3
		addVertex(baseLength * -1, height * -1, baseLength);      // 4

		// Sides
		addSurface(Color.WHITE, 0, 2, 3);
		addSurface(Color.WHITE, 0, 3, 4);
		addSurface(Color.WHITE, 0, 4, 1);
		addSurface(Color.WHITE, 0, 1, 2);

		// Bottom
		addSurface(Color.WHITE, 2, 1, 4, 3);
	}
}