package objects;

import java.io.*;
import java.awt.Color;

public class ObjectLoader
{
	public static GameObject loadOBJ(String path)
	{
		GameObject object = new GameObject();

		FileInputStream fileIn = null;
		BufferedReader reader = null;

		int vertices = 0;
		int triangles = 0;

		try
		{
			fileIn = new FileInputStream(path);
			reader = new BufferedReader(new InputStreamReader(fileIn));

			String line;

			do
			{
				line = reader.readLine();

				String type;

				if (line != null && !line.equals(" ") && !line.equals("#") && !line.equals(""))
				{
					type = line.substring(0, 2).trim();
				}
				else
				{
					type = "";
				}

				switch (type)
				{
					case "v":
						String parsed = line.substring(1);
						String[] vertexData = parsed.trim().split("\\s");
						object.addVertex(Double.parseDouble(vertexData[0]),
										 Double.parseDouble(vertexData[1]),
										 Double.parseDouble(vertexData[2]));
						vertices++;
						break;

					case "f":
						parsed = line.substring(1);
						String[] surfaceData = parsed.trim().split("\\s");

						int v1 = Integer.parseInt(surfaceData[0].split("/")[0]) - 1;
						int v2 = Integer.parseInt(surfaceData[1].split("/")[0]) - 1;
						int v3 = Integer.parseInt(surfaceData[2].split("/")[0]) - 1;

						object.addSurface(Color.WHITE, v1, v2, v3);

						triangles++;
						break;

					default:
						break;
				}
			}
			while (line != null);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			System.out.println("Could not load model at " + path);
		}
		finally
		{
			try
			{
				reader.close();
				fileIn.close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}

		System.out.println("Model at " + path + " loaded.");
		System.out.println(vertices + " vertices, " + triangles + " triangles.");

		return object;
	}
}