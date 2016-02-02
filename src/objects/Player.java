package objects;

import core.Vec3;
import core.Camera;

import java.io.*;
import java.awt.Color;

public class Player extends GameObject
{
	private static GameObject PISTOL_MODEL = ObjectLoader.loadOBJ("./res/pistol.obj");

	private String m_username;
	private Color m_color;
	private boolean m_isLocal;

	private Vec3 m_lastPosition;
	private Vec3 m_lastRotation;

	private GameObject m_weapon;
	private Box m_rightArmBottom;

	public Player(String username, Color color, boolean isLocal)
	{
		super();

		m_color = color;
		m_username = username;
		m_isLocal = isLocal;

		initMesh(isLocal);

		m_lastPosition = new Vec3();
		m_lastRotation = new Vec3();
	}

	private void initMesh(boolean isLocal)
	{
		if (!isLocal)
		{
			Box body = new Box(new Vec3(4, 7, 3));
			body.setSurfaceColors(m_color);
			addChild(body);

			Box head = new Box(new Vec3(2, 2, 2));
			body.addChild(head);
			head.setSurfaceColors(m_color);
			head.setPosition(new Vec3(0, 4.5, 0));

			Box leftArm = new Box(new Vec3(1.5, 7, 1.5));
			body.addChild(leftArm);
			leftArm.setSurfaceColors(m_color);
			leftArm.setPosition(new Vec3(-3, 0, 0));

			Box leftLeg = new Box(new Vec3(1.5, 8, 1.5));
			body.addChild(leftLeg);
			leftLeg.setSurfaceColors(m_color);
			leftLeg.setPosition(new Vec3(1.25, -7.5, 0));

			Box rightLeg = new Box(new Vec3(1.5, 8, 1.5));
			body.addChild(rightLeg);
			rightLeg.setSurfaceColors(m_color);
			rightLeg.setPosition(new Vec3(-1.25, -7.5, 0));
		}

		Box rightArmTop = new Box(new Vec3(1.5, 5, 1.5));
		addChild(rightArmTop);
		rightArmTop.setSurfaceColors(m_color);
		rightArmTop.setPosition(new Vec3(3, 1, 0));

		m_rightArmBottom = new Box(new Vec3(1.5, 1.5, 5));
		addChild(m_rightArmBottom);
		m_rightArmBottom.setSurfaceColors(m_color);
		m_rightArmBottom.setPosition(new Vec3(3, -2, 2));
		m_rightArmBottom.setRotation(new Vec3(0, Math.PI / 6, 0));

		m_weapon = PISTOL_MODEL.copy();
		m_rightArmBottom.addChild(m_weapon);
		m_weapon.setSurfaceColors(new Color(128, 128, 128));
		m_weapon.setPosition(new Vec3(0, 0, 3));
		m_weapon.setRotation(new Vec3(0, Math.PI / -2, 0));
		m_weapon.setScale(0.5);	
	}

	public void setFromCamera(Camera camera)
	{
		Vec3 camPos = camera.getPosition();
		Vec3 camRot = camera.getRotation();
		
		m_rotation.set(new Vec3(0, -1 * camRot.y, camRot.z));
		double angle = -1 * camRot.x;
		m_rightArmBottom.setRotation(new Vec3(angle, 0, 0));
		m_rightArmBottom.setPosition(new Vec3(3, -1.5 + (Math.sin(angle) * 2.5), 
												 0.75 + (Math.cos(angle) * 2.5)));
		m_position.set(camPos);
		m_position.subtract(0, 4.5, 0);
	}

	protected void setArm(Vec3 position, Vec3 rotation)
	{
		m_rightArmBottom.setPosition(position);
		m_rightArmBottom.setRotation(rotation);
	}

	public void update()
	{
		if (m_isLocal)
		{
			setLast(m_position, m_rotation);
			super.update();
		}
		else
		{
			Vec3 deltaPos = new Vec3(m_lastPosition);
			deltaPos.multiply(0.1);
			m_position.add(deltaPos);
			Vec3 deltaRot = new Vec3(m_lastRotation);
			deltaRot.multiply(0.1);
			m_rotation.add(deltaRot);
		}
	}

	public String toString()
	{
		return 	   	m_username 					+ "<>" + 			  // 0
					m_position.toString() 		+ "<>" + 			  // 1
					m_lastPosition.toString()   + "<>" + 			  // 2
					m_rotation.toString() 		+ "<>" + 			  // 3
					m_lastRotation.toString()   + "<>" + 			  // 4
					m_color.getRed() 	 		+ ", " + 			  // 5
					m_color.getGreen() 			+ ", " + 
					m_color.getBlue() 			+ "<>" +
					m_rightArmBottom.getPosition().toString() + "<>" +// 6
					m_rightArmBottom.getRotation().toString() + "%%"; // 7

	}

	public static Player fromString(String data)
	{
		String[] parsedData = data.split("<>");

		String[] color = parsedData[5].split(", ");
		Color playerColor = new Color(Integer.parseInt(color[0]),
									  Integer.parseInt(color[1]),
									  Integer.parseInt(color[2]));
		Player player = new Player(parsedData[0], playerColor, false);

		player.setPosition(Vec3.fromString(parsedData[1]));
		player.setRotation(Vec3.fromString(parsedData[3]));
		player.setArm(Vec3.fromString(parsedData[6]), Vec3.fromString(parsedData[7]));

		Vec3 posDifference = new Vec3(player.getPosition());
		posDifference.subtract(Vec3.fromString(parsedData[2]));
		Vec3 rotDifference = new Vec3(player.getRotation());
		rotDifference.subtract(Vec3.fromString(parsedData[4]));

		player.setLast(posDifference, rotDifference);

		return player;
	}

	private void setLast(Vec3 lastPosition, Vec3 lastRotation)
	{
		m_lastPosition.set(lastPosition);
		m_lastRotation.set(lastRotation);
	}
}