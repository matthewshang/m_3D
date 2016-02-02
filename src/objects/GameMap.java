package objects;

import java.awt.Color;

public class GameMap extends GameObject
{
	public GameMap(int width, int height, int length)
	{
		super();

		int verticesWidth = width + 1;
		double xMin = width * -0.5;
		double zMin = length * -0.5;
		
		for (int z = 0; z < 2; z++)
		{
			for (int y = 0; y < 2; y++)
			{
				for (int x = 0; x < verticesWidth; x++)
				{
					addVertex(xMin + x, y * height, zMin + (z * length));
				}
			}

			int offset = z * verticesWidth * 2;

			for (int x = 0; x < width; x++)
			{
				int pos = offset + x;
				if (offset == 0)
				{
					addSurface(Color.WHITE, pos + 1, pos + 1 + verticesWidth, pos + verticesWidth, pos);
				}
				else
				{
					addSurface(Color.WHITE, pos, pos + verticesWidth, pos + 1 + verticesWidth, pos + 1);
				}
			}
		}

		int vertex = m_vertices.size();

		int verticesLength = length + 1;
		for (int x = 0; x < 2; x++)
		{
			for (int y = 0; y < 2; y++)
			{
				for (int z = 0; z < verticesLength; z++)
				{
					addVertex(xMin + (x * width), y * height, zMin + z);
				}
			}

			int offset = x * verticesWidth * 2 + vertex;

			for (int z = 0; z < width; z++)
			{
				int pos = offset + z;
				if (offset == vertex)
				{
					addSurface(Color.WHITE, pos, pos + verticesWidth, pos + 1 + verticesWidth, pos + 1);
				}
				else
				{
					addSurface(Color.WHITE, pos + 1, pos + 1 + verticesWidth, pos + verticesWidth, pos);
				}
			}
		}
	}
}