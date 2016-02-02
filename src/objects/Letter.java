package objects;

import java.awt.Color;

public class Letter extends GameObject
{
	public Letter(char letter)
	{
		super();

		switch (letter)
		{
			case 'E':
				initE();
				break;

			default:
				initM();
				break;
		}
	}

	private void initE()
	{
		Color purple = new Color(139, 71, 137);

		// Front
		addVertex( 1.0,   1.0,  -0.25);   // 0
		addVertex( 1.0,   0.75, -0.25);   // 1
		addVertex(-0.75,  0.75, -0.25);   // 2
		addVertex(-0.75,  0.125, -0.25);  // 3
		addVertex( 0.75,   0.125, -0.25); // 4
		addVertex( 0.75,  -0.125, -0.25); // 5
		addVertex(-0.75, -0.125, -0.25);  // 6
		addVertex(-0.75, -0.75, -0.25);   // 7
		addVertex( 1.0,  -0.75, -0.25);   // 8
		addVertex( 1.0,  -1.0,  -0.25);   // 9
		addVertex(-1.0,  -1.0,  -0.25);   // 10
		addVertex(-1.0,   1.0,  -0.25);   // 11

		addSurface(purple, 0, 1, 2, 11);
		addSurface(purple, 11, 2, 3, 10);
		addSurface(purple, 4, 5, 6, 3);
		addSurface(purple, 3, 6, 10);
		addSurface(purple, 6, 7, 10);
		addSurface(purple, 8, 9, 10, 7);

		// Back
		addVertex( 1.0,   1.0,  0.25);    // 12
		addVertex( 1.0,   0.75, 0.25);    // 13
		addVertex(-0.75,  0.75, 0.25);    // 14
		addVertex(-0.75,  0.125, 0.25);   // 15
		addVertex( 0.75,   0.125, 0.25);  // 16
		addVertex( 0.75,  -0.125, 0.25);  // 17
		addVertex(-0.75, -0.125, 0.25);   // 18
		addVertex(-0.75, -0.75, 0.25);    // 19
		addVertex( 1.0,  -0.75, 0.25);    // 20
		addVertex( 1.0,  -1.0,  0.25);    // 21
		addVertex(-1.0,  -1.0,  0.25);    // 22
		addVertex(-1.0,   1.0,  0.25);    // 23

		addSurface(purple, 23, 14, 13, 12);
		addSurface(purple, 23, 22, 15, 14);
		addSurface(purple, 15, 18, 17, 16);
		addSurface(purple, 15, 22, 18);
		addSurface(purple, 18, 22, 19);
		addSurface(purple, 19, 22, 21, 20);

		// Sides
		addSurface(purple, 12, 13, 1, 0);
		addSurface(purple, 13, 14, 2, 1);
		addSurface(purple, 14, 15, 3, 2);
		addSurface(purple, 15, 16, 4, 3);
		addSurface(purple, 16, 17, 5, 4);
		addSurface(purple, 17, 18, 6, 5);
		addSurface(purple, 18, 19, 7, 6);
		addSurface(purple, 19, 20, 8, 7);
		addSurface(purple, 20, 21, 9, 8);
		addSurface(purple, 21, 22, 10, 9);
		addSurface(purple, 11, 10, 22, 23);
		addSurface(purple, 23, 12, 0, 11);
	}

	private void initM()
	{
		Color orange = new Color(255, 165, 0);

		// Front
		addVertex( 1.0,   1.0,  -0.25);   // 0
		addVertex( 1.0,  -1.0,  -0.25);   // 1
		addVertex( 0.75, -1.0,  -0.25);   // 2
		addVertex( 0.75,  0.5,  -0.25);   // 3

		addVertex( 0.0,   0.0,  -0.25);   // 4

		addVertex(-0.75,  0.5,  -0.25);   // 5
		addVertex(-0.75, -1.0,  -0.25);   // 6
		addVertex(-1.0,  -1.0,  -0.25);   // 7
		addVertex(-1.0,   1.0,  -0.25);   // 8

		addVertex( 0.0,   0.25, -0.25);   // 9

		addSurface(orange, 0, 1, 2, 3);
		addSurface(orange, 0, 3, 4, 9);
		addSurface(orange, 9, 4, 5, 8);
		addSurface(orange, 5, 6, 7, 8);

		// Back
		addVertex( 1.0,   1.0,  0.25);    // 10
		addVertex( 1.0,  -1.0,  0.25);    // 11
		addVertex( 0.75, -1.0,  0.25);    // 12
		addVertex( 0.75,  0.5,  0.25);    // 13

		addVertex( 0.0,   0.0,  0.25);    // 14

		addVertex(-0.75,  0.5,  0.25);    // 15
		addVertex(-0.75, -1.0,  0.25);    // 16
		addVertex(-1.0,  -1.0,  0.25);    // 17
		addVertex(-1.0,   1.0,  0.25);    // 18

		addVertex( 0.0,   0.25, 0.25);    // 19

		addSurface(orange, 18, 17, 16, 15);
		addSurface(orange, 18, 15, 14, 19);
		addSurface(orange, 19, 14, 13, 10);
		addSurface(orange, 13, 12, 11, 10);

		// Sides

		addSurface(orange, 10, 11, 1, 0);
		addSurface(orange, 11, 12, 2, 1);
		addSurface(orange, 3, 2, 12, 13);
		addSurface(orange, 13, 14, 4, 3);
		addSurface(orange, 14, 15, 5, 4);
		addSurface(orange, 15, 16, 6, 5);
		addSurface(orange, 16, 17, 7, 6);
		addSurface(orange, 8, 7, 17, 18);
		addSurface(orange, 18, 19, 9, 8);
		addSurface(orange, 0, 9, 19, 10);

	}
}