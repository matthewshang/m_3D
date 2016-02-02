package core;

import javax.swing.JFrame;

public class Display
{
	private JFrame m_frame;

	public Display(int width, int height, Renderer renderer, InputHandler input)
	{
		m_frame = new JFrame("m_3D");
		m_frame.setSize(width, height + 20);
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.setLocationRelativeTo(null);

		m_frame.add(renderer);
		m_frame.addKeyListener(input);
		m_frame.addMouseListener(input);
		m_frame.addMouseMotionListener(input);
	}

	public void setTitle(String title)
	{
		m_frame.setTitle(title);
	}

	public void show()
	{
		m_frame.setVisible(true);
	}
}