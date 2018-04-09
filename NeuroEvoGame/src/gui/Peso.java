package gui;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Peso extends Sprite
{
	private final static Image image = new ImageIcon("res\\image.jpg").getImage();
	public Peso(double x, double y, double speed)
	{
		super(x, y, speed, image);
	}

	@Override
	protected void draw(Graphics2D g) 
	{
		g.drawImage(image, (int)getX(), (int)getY(), null);
	}

}
