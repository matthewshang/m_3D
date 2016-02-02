package server;

import java.net.InetAddress;

public class Client
{
	private InetAddress m_address;
	private int m_port;
	private String m_data;
	private int m_timeouts;

	public Client(InetAddress address, int port)
	{
		m_address = address;
		m_port = port;
		m_data = "";
		m_timeouts = 0;
	}

	public InetAddress getAddress()
	{
		return m_address;
	}

	public int getPort()
	{
		return m_port;
	}

	public String getData()
	{
		return m_data;
	}

	public void setData(String data)
	{
		m_data = data;
	}

	public int getTimeouts()
	{
		return m_timeouts;
	}

	public void increaseTimeouts()
	{
		m_timeouts++;
	}

	public void resetTimeouts()
	{
		m_timeouts = 0;
	}
}