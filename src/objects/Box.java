package objects;

import core.Vec3;

import java.awt.Color;

public class Box extends GameObject
{
	public Box(Vec3 lengths)
	{
		super();

		lengths.multiply(0.5);

		addVertex(lengths.x,      lengths.y,      lengths.z);       // 0
		addVertex(lengths.x,      lengths.y * -1, lengths.z);       // 1
		addVertex(lengths.x * -1, lengths.y * -1, lengths.z);       // 2
		addVertex(lengths.x * -1, lengths.y,      lengths.z);       // 3

		addVertex(lengths.x,      lengths.y,      lengths.z * -1);  // 4
		addVertex(lengths.x,      lengths.y * -1, lengths.z * -1);  // 5
		addVertex(lengths.x * -1, lengths.y * -1, lengths.z * -1);  // 6
		addVertex(lengths.x * -1, lengths.y,      lengths.z * -1);  // 7

		// Front
		addSurface(Color.WHITE, 3, 2, 1, 0);

		// Back
		addSurface(Color.WHITE, 4, 5, 6, 7);

		// Top
		addSurface(Color.WHITE, 3, 0, 4, 7);

		// Bottom
		addSurface(Color.WHITE, 6, 5, 1, 2);

		// Right
		addSurface(Color.WHITE, 1, 5, 4, 0);

		// Left
		addSurface(Color.WHITE, 6, 2, 3, 7);
	}
}