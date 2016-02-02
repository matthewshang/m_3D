package client;

import java.net.*;
import java.io.IOException;

public class NetworkHandler extends Thread
{
	private static InetAddress SERVER_ADDRESS = null;
	static
	{
		try
		{
			SERVER_ADDRESS = InetAddress.getByName("172.17.217.28");
		}
		catch (UnknownHostException ex)
		{
			System.out.println("Error: Could not reach server");
			ex.printStackTrace();
		}
	}
			
	private static final int SERVER_PORT = 7777;

	private DatagramSocket m_socket;
	private String m_data;
	private String m_toSend;
	private boolean m_newData;
	private boolean m_isRunning;

	public NetworkHandler(int port)
	{
		try
		{
			m_socket = new DatagramSocket(port);
			m_socket.setSoTimeout(5000);
		}
		catch (SocketException ex)
		{
			System.out.println("Error: Could not make socket");
			ex.printStackTrace();
		}

		m_data = "";
		m_toSend = "";
		m_isRunning = false;
		m_newData = false;
	}

	public void run()
	{
		connectToServer();

		int timeouts = 0;
		m_isRunning = true;

		while (m_isRunning)
		{
			try
			{
				DatagramPacket packet = receivePacket();

				if ((char) packet.getData()[0] == '?')
				{
					sendPacket(m_toSend.getBytes(), SERVER_ADDRESS, SERVER_PORT);

					packet = receivePacket();

					String raw = new String(packet.getData());
					int index = raw.lastIndexOf("@@");

					if (index != -1)
					{
						String data = raw.substring(0, index);
						m_data = data;
						m_newData = true;
					}
				}
			}
			catch (SocketTimeoutException ex)
			{
				timeouts++;
				System.out.println("Socket timeout");
				
				if (timeouts > 4)
				{
					connectToServer();
					timeouts = 0;
				}
			}
		}
	}

	public boolean isDataNew()
	{
		return m_newData;
	}

	public String getData()
	{
		m_newData = false;
		return m_data;
	}

	public void setToSend(String data)
	{
		m_toSend = data;
	}

	private void connectToServer()
	{
		sendPacket("addme".getBytes(), SERVER_ADDRESS, 4444);
	}

	private void sendPacket(byte[] bytes, InetAddress address, int port)
	{
		DatagramPacket packet = 
			new DatagramPacket(bytes, bytes.length, address, port);

		try
		{
			m_socket.send(packet);
		}
		catch (IOException ex)
		{
			System.out.println("Error: Could not send data to server.");
			ex.printStackTrace();
		}
	}

	private DatagramPacket receivePacket() throws SocketTimeoutException
	{
		byte[] buffer = new byte[2048];
		DatagramPacket packet = 
			new DatagramPacket(buffer, buffer.length);

		try
		{
			m_socket.receive(packet);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}

		return packet;
	}
}