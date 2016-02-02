package core;

import java.awt.event.*;

import java.util.Arrays;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener
{
	public boolean[] keys;
	public Point2 mousePosition;
	public boolean mouseDown;

	public InputHandler()
	{
		keys = new boolean[200];
		Arrays.fill(keys, false);

		mousePosition = new Point2(0, 0);
		mouseDown = false; 
	}

	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();

		keys[key] = true;
	}

	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();

		keys[key] = false;
	}

	public void keyTyped(KeyEvent e)
	{
		
	}

	public void mouseMoved(MouseEvent e)
	{
		mousePosition.set(e.getX(), e.getY());
	}

	public void mouseDragged(MouseEvent e)
	{

	}

	public void mousePressed(MouseEvent e)
	{
		mouseDown = true;
	}

	public void mouseReleased(MouseEvent e)
	{
		mouseDown = false;
	}

	public void mouseEntered(MouseEvent e)
	{

	}

	public void mouseExited(MouseEvent e)
	{
		Arrays.fill(keys, false);
		mouseDown = false;
	}

	public void mouseClicked(MouseEvent e)
	{
		
	}
}