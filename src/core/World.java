package core;

import objects.GameObject;
import objects.Box;

import java.util.ArrayList;
import java.awt.event.*;

import java.awt.Color;

public class World
{
	private ArrayList<GameObject> m_objects;
	private ArrayList<GameObject> m_networkObjects;
	private ArrayList<Light> m_lights;
	private Camera m_camera;

	public World()
	{
		m_objects = new ArrayList<GameObject>();
		m_networkObjects = new ArrayList<GameObject>();
		m_lights = new ArrayList<Light>();
		m_camera = new Camera();
	}

	public void setCamera(Camera camera)
	{
		m_camera = camera;
	}

	public Camera getCamera()
	{
		return m_camera;
	}

	public void update()
	{
		for (int i = 0; i < m_objects.size(); i++)
		{
			m_objects.get(i).update();
		}

		for (int i = 0; i < m_networkObjects.size(); i++)
		{
			m_networkObjects.get(i).update();
		}

		for (int i = 0; i < m_lights.size(); i++)
		{
			Light light = m_lights.get(i);

			int object = light.getObjectIndex();
			m_objects.get(object).setPosition(light.getPosition());
		}
		
		m_camera.update();
	}

	// public ArrayList<Surface> getSurfaces()
	// {

	// }

	public void addObject(GameObject object)
	{
		m_objects.add(object);
	}

	public void addNetworkObject(GameObject object)
	{
		m_networkObjects.add(object); 
	}

	public void refreshNetwork()
	{
		m_networkObjects.clear();
	}

	public void removeObject(int index)
	{
		m_objects.remove(index);
	}

	public void addLight(Light light)
	{
		m_lights.add(light);

		Box lightBox = new Box(new Vec3(0.15, 0.15, 0.15));
		lightBox.setPosition(light.getPosition());
		lightBox.setLighting(false);
		lightBox.setSurfaceColors(light.getColor());
		addObject(lightBox);

		light.setObjectIndex(m_objects.size() - 1);
	}

	public ArrayList<GameObject> getObjects()
	{
		ArrayList<GameObject> objects = 
					new ArrayList<>(m_objects.size() + m_networkObjects.size());
		objects.addAll(m_objects);
		objects.addAll(m_networkObjects);
		return objects;
	}

	public ArrayList<Light> getLights()
	{
		return m_lights;
	}

	public void input(InputHandler input)
	{
		Point2 mousePos = input.mousePosition;
		double ratio = Math.PI / 200;
		m_camera.setRotation(new Vec3((mousePos.y - 200) * ratio, 
									  (mousePos.x - 200) * ratio, 0));

		Vec3 camRot = m_camera.getRotation();
		if (camRot.x < Math.PI / -2)
		{
			camRot.x = Math.PI / -2;
		}
		else if (camRot.x > Math.PI / 2)
		{
			camRot.x = Math.PI / 2;
		}

		boolean[] keys = input.keys;

		if (keys[KeyEvent.VK_W])
		{
			m_camera.move(Camera.Movement.FOWARD);
		}

		if (keys[KeyEvent.VK_S])
		{
			m_camera.move(Camera.Movement.BACKWARD);
		}

		if (keys[KeyEvent.VK_A])
		{
			m_camera.move(Camera.Movement.LEFT);
		}

		if (keys[KeyEvent.VK_D])
		{
			m_camera.move(Camera.Movement.RIGHT);
		}

		if (keys[KeyEvent.VK_Q])
		{
			m_camera.move(Camera.Movement.UP);
		}

		if (keys[KeyEvent.VK_E])
		{
			m_camera.move(Camera.Movement.DOWN);
		}
	}
}