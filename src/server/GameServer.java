package server;

import java.net.*;
import java.io.IOException;

import java.util.Date;
import java.text.SimpleDateFormat;

import java.util.ArrayList;

public class GameServer
{
	private DatagramSocket m_socket;
	private ConnectionHandler m_handler;

	private ArrayList<Client> m_clients;

	private boolean m_isRunning;

	public GameServer()
	{
		try
		{
			m_socket = new DatagramSocket(7777);
			m_socket.setSoTimeout(500);
		}
		catch (SocketException ex)
		{
			ex.printStackTrace();
		}

		m_handler = new ConnectionHandler(4444);

		m_clients = new ArrayList<Client>();

		m_isRunning = false;
	}

	public void start()
	{
		log("Server started");
		m_isRunning = true;

		m_handler.start();

		while (m_isRunning)
		{
			try
			{
				Thread.sleep(20);
			}
			catch (InterruptedException ex)
			{
				log(ex.getMessage());
			}

			for (int i = 0; i < m_clients.size(); i++)
			{
				Client client = m_clients.get(i);

				sendPacket("?".getBytes(), client.getAddress(), client.getPort());

				byte[] buffer = new byte[1024];

				DatagramPacket packet =
					new DatagramPacket(buffer, buffer.length);
					
				try
				{
					m_socket.receive(packet);

					if (packet.getAddress().equals(client.getAddress()))
					{
						client.setData(new String(packet.getData()));
						client.resetTimeouts();
					}
				}
				catch (SocketTimeoutException ex)
				{
					String address = client.getAddress().toString();
					log(address + ":" + client.getPort() + " timeout");

					if (client.getTimeouts() > 4)
					{
						log(address + " disconnected");
						m_clients.remove(i);

						if (i != m_clients.size())
						{
							i--;
						}
					}
					else
					{
						client.increaseTimeouts();
					}
				}
				catch (IOException ex)
				{
					log(ex.getMessage());
				}
			}

			for (int i = 0; i < m_clients.size(); i++)
			{
				Client client = m_clients.get(i);

				String toSend = "";
				for (int j = 0; j < m_clients.size(); j++)
				{
					if (j != i)
					{
						toSend += m_clients.get(j).getData();
					}
				}
				toSend += "@@";

				sendPacket(toSend.getBytes(), client.getAddress(), client.getPort());
			}
		}
	}

	private void sendPacket(byte[] bytes, InetAddress address, int port)
	{
		DatagramPacket packet = 
			new DatagramPacket(bytes, bytes.length, 
							   address, port);

		try
		{
			m_socket.send(packet);
		}
		catch (IOException ex)
		{
			System.out.println("Error: Could not send data to client.");
			ex.printStackTrace();
		}
	}

	private boolean isNewClient(InetAddress address, int port)
	{
		for (int i = 0; i < m_clients.size(); i++)
		{
			Client client = m_clients.get(i);

			if (client.getAddress().equals(address) && client.getPort() == port)
			{
				return false;
			}
		}

		return true;
	}

	private void log(String message)
	{
		System.out.println(getTime() + ": " + message);
	}

	private String getTime()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		return dateFormat.format(new Date());
	}

	public static void main(String[] args)
	{
		GameServer server = new GameServer();
		server.start();
	}

	public class ConnectionHandler extends Thread
	{
		private DatagramSocket m_listenerSocket;

		public ConnectionHandler(int port)
		{
			try
			{
				m_listenerSocket = new DatagramSocket(port);
			}
			catch (SocketException ex)
			{
				System.out.println("Could not create ConnectionHandler");
				ex.printStackTrace();
			}
		}

		public void run()
		{
			while (m_isRunning)
			{
				byte[] buffer = new byte[1024];
				DatagramPacket packet = 
					new DatagramPacket(buffer, buffer.length);

				try
				{
					m_listenerSocket.receive(packet);
				}
				catch (IOException ex)
				{
					log(ex.getMessage());
					continue;
				}

				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				if (isNewClient(address, port))
				{
					m_clients.add(new Client(address, port));
					log("Connection from " + address.toString() + ":" + port);
				}
			}
		}
	}
}