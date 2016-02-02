package client;

import core.*;
import objects.*;

import java.awt.Color;
import java.io.*;

public class GameClient
{
	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 800;

	private Renderer m_renderer;
	private World m_world;
	private Display m_window;
	private InputHandler m_input;

	private NetworkHandler m_networkConnection;
	private BufferedReader m_consoleIn;

	private int m_targetFrameTime;
	private boolean m_isRunning;

	private Player m_player;
	private Camera m_camera;

	public GameClient(int targetFPS)
	{
		m_world = new World();
		m_renderer = new Renderer(m_world, WINDOW_WIDTH, WINDOW_HEIGHT);
		m_input = new InputHandler();
		m_window = new Display(WINDOW_WIDTH, WINDOW_HEIGHT, m_renderer, m_input);
		m_networkConnection = new NetworkHandler(getRandomPort());

		m_consoleIn = new BufferedReader(new InputStreamReader(System.in));

		m_targetFrameTime = 1000 / targetFPS;
		m_isRunning = false;
	}

	private int getRandomPort()
	{
		int port;

		do
		{
			port = 2000 + (int) (Math.random() * 7000.0);
		}
		while (port == 4444 && port == 7777);

		return port;
	}

	private void init()
	{
		System.out.println("Welcome to Matthew's FPS! Please enter a username.");
		String username = "player";
		try
		{
			username = m_consoleIn.readLine();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		m_player = 
			new Player(username, new Color((int) (Math.random() * 255.0), 
										   (int) (Math.random() * 255.0), 
										   (int) (Math.random() * 255.0)), true);
		m_world.addObject(m_player);

		m_camera = new Camera();
		m_camera.setSpeed(0.2, Math.PI / 90);
		m_camera.setPosition(new Vec3(0, 5, -5));
		m_world.setCamera(m_camera);

		Light light = new Light(new Vec3(0, 32, 0), 64, Color.WHITE);
		m_world.addLight(light);

		GameMap map = new GameMap(16, 4, 16);
		m_world.addObject(map);
		map.setScale(8);
	}

	private void start()
	{
		Player testPlayer = new Player("Player", Color.RED, false);
		// Box testPlayer = new Box(new Vec3(5, 5, 5));
		m_world.addObject(testPlayer);

		m_window.show();
		m_isRunning = true;
		m_networkConnection.start();

		while (m_isRunning)
		{
			long startTime = System.nanoTime();

			m_world.input(m_input);
			m_world.update();

			m_player.setFromCamera(m_camera);
			updateNetwork(m_player);

			if (testPlayer.inAim(m_camera))
			{
				testPlayer.setSurfaceColors(Color.GREEN);
			}
			else
			{
				testPlayer.setSurfaceColors(Color.RED);
			}

			m_renderer.repaint();

			double frameTime = (System.nanoTime() - startTime) / 1e6;
			double sleepTime = ((double) m_targetFrameTime - frameTime);

			if (sleepTime > 0)
			{
				try 
				{
					Thread.sleep((long) sleepTime);

				}
				catch (InterruptedException ex)
				{
					m_isRunning = false;
				}
			}
		}
	}

	private void updateNetwork(Player player)
	{
		m_networkConnection.setToSend(player.toString());

		if (m_networkConnection.isDataNew())
		{
			String[] players = m_networkConnection.getData().split("%%");

			m_world.refreshNetwork();

			if (players.length > 1)
			{
				for (int i = 0; i < players.length - 1; i++)
				{
					m_world.addNetworkObject(Player.fromString(players[i]));
				}
			}
		}
	}

	public static void main(String[] args)
	{
		GameClient client = new GameClient(30);
		client.init();
		client.start();
	}
}