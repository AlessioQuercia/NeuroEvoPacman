package gui;

import java.awt.Graphics2D;
import java.awt.Image;

public abstract class Sprite 
{
	private double x, y, speed;
	private Image image;

	public Sprite(double x, double y, double speed, Image image) 
	{
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.image = image;
	}

	public Image getImage() 
	{
		return image;
	}

	public void setImage(Image image) 
	{
		this.image = image;
	}

	public double getX()
	{
		return x;
	}

	public void setX(double x) 
	{
		this.x = x;
	}

	public double getY()
	{
		return y;
	}

	public void setY(double y) 
	{
		this.y = y;
	}

	public double getSpeed() 
	{
		return speed;
	}

	public void setSpeed(double speed) 
	{
		this.speed = speed;
	}
	
	protected abstract void draw(Graphics2D g);
}
