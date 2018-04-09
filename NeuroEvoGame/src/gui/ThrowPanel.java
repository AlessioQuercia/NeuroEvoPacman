package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.joml.Vector2d;

import common.MyConstants;

public class ThrowPanel extends JPanel
{
	private JFrame frame;
	
	private Graphics2D g;
	
	private Line2D line;
	
//	private Peso peso = new Peso(60, 60, 0);
	
	private Ellipse2D target;
	private Ellipse2D peso;
	private Line2D tailLine;
	private Line2D targetTailLine;

	private LinkedList<Vector2d> tail;
	private LinkedList<Vector2d> targetTail;
	private LinkedList<Line2D> lines;
	
	private double a;
	private double v;
	
	private boolean showBest;
	
	public Ellipse2D getBestTarget() {
		return bestTarget;
	}


	public Ellipse2D getBestShot() {
		return bestShot;
	}

	private Ellipse2D bestTarget;
	private Ellipse2D bestShot;

	public double getA() {
		return a;
	}


	public void setA(double a) {
		this.a = a;
	}


	public double getV() {
		return v;
	}


	public void setV(double v) {
		this.v = v;
	}

	boolean draw;

	public ThrowPanel(JFrame frame) 
	{
		this.frame = frame;
		
		g = (Graphics2D) getGraphics();
		
		init();
	}
	
	
	private void init() 
	{
		setBorder(BorderFactory.createTitledBorder("Simulation"));
		
		setLayout(new GridBagLayout());	
		
		target = new Ellipse2D.Double(0, 0, 5, 5);
		
		peso = new Ellipse2D.Double(0, 0, 3, 3);
		
		tailLine = new Line2D.Double(0, 0, 0, 0);
		
		targetTailLine = new Line2D.Double(0, 0, 0, 0);
		
		tail = new LinkedList<Vector2d> ();
		
		targetTail = new LinkedList<Vector2d> ();
		
		lines = new LinkedList<Line2D> ();
		
		draw = false;
		
		showBest = false;
		
		bestTarget = new Ellipse2D.Double(0, 0, 5, 5);
		
		bestShot = new Ellipse2D.Double(0, 0, 3, 3);
	}

	public void clearPanel()
	{
        repaint();
	}
	
	public void drawAxis()
	{
		g = (Graphics2D) getGraphics();
        g.drawLine(MyConstants.BORDER_X, getHeight()-MyConstants.BORDER_Y, getWidth()-MyConstants.BORDER_X, getHeight()-MyConstants.BORDER_Y); // x-axis
        g.drawLine(MyConstants.BORDER_X, getHeight()-MyConstants.BORDER_Y, MyConstants.BORDER_X, MyConstants.BORDER_Y); // y-axis
	}
	
	public void drawTarget(double x_tgt, double y_tgt)
	{
		double X = proportionX(x_tgt);
		double Y = proportionY(y_tgt);
        g.setColor(Color.RED);
        Ellipse2D circle = new Ellipse2D.Double(MyConstants.BORDER_X + (int)X, (getHeight()-MyConstants.BORDER_Y) - (int)Y, 5, 5);
        g.fill(circle);
//        g.fillOval(MyConstants.BORDER_X + (int)X, getHeight()-MyConstants.BORDER_Y-(int)Y, 3, 3);
	}
	
	public void drawTarget(Graphics2D g2d, double x_tgt, double y_tgt)
	{
		double X = proportionX(x_tgt);
		double Y = proportionY(y_tgt);
        g2d.setColor(Color.RED);
        Ellipse2D circle = new Ellipse2D.Double(MyConstants.BORDER_X + (int)X, (getHeight()-MyConstants.BORDER_Y) - (int)Y, 5, 5);
        g2d.fill(circle);
//        g.fillOval(MyConstants.BORDER_X + (int)X, getHeight()-MyConstants.BORDER_Y-(int)Y, 3, 3);
	}
	
	public void clearTarget(double x_tgt, double y_tgt)
	{
		double X = proportionX(x_tgt);
		double Y = proportionY(y_tgt);
		
		g.clearRect(MyConstants.BORDER_X + (int)X, getHeight()-MyConstants.BORDER_Y-(int)Y, 5, 5);
	}
	
	public void drawParabola(double a, double v)
	{
        g = (Graphics2D) getGraphics();
//        g.drawLine(MyConstants.BORDER_X, getHeight()-MyConstants.BORDER_Y, getWidth()-MyConstants.BORDER_X, getHeight()-MyConstants.BORDER_Y); // x-axis
//        g.drawLine(MyConstants.BORDER_X, getHeight()-MyConstants.BORDER_Y, MyConstants.BORDER_X, MyConstants.BORDER_Y); // y-axis
//        getGraphics().drawLine((int)x_tgt, getHeight()-MyConstants.BORDER_Y-(int)y_tgt, (int)x_tgt, getHeight()-MyConstants.BORDER_Y-(int)y_tgt);
//        g.setColor(Color.RED);
//        g.fillOval(MyConstants.BORDER_X + (int)x_tgt, getHeight()-MyConstants.BORDER_Y-(int)y_tgt, 3, 3);
        g.setColor(Color.BLACK);
        
        for(int x=0; x<=getWidth()-MyConstants.BORDER_X; x++)
        {
            double y = Math.tan(a)*x - ((MyConstants.GRAVITY/(2*Math.pow(v, 2)*Math.pow(Math.cos(a), 2)))*Math.pow(x, 2));
            double Y = proportionY(y);	///PROPORZIONI Y
            double X = proportionX(x);		///PROPORZIONI X
            if (y < 0 || x > getWidth()-MyConstants.BORDER_X) break;
            line = new Line2D.Double(MyConstants.BORDER_X+X,(getHeight()-MyConstants.BORDER_Y)-Y,MyConstants.BORDER_X+X,(getHeight()-MyConstants.BORDER_Y)-Y);
            g.draw(line);
//            g.drawLine(MyConstants.BORDER_X+x,(getHeight()-MyConstants.BORDER_Y)-Y,MyConstants.BORDER_X+x,(getHeight()-MyConstants.BORDER_Y)-Y);
        }
	}
	
	public void drawParabola(Graphics2D g2d, double a, double v)
	{
        g2d.setColor(Color.BLACK);
        for(int x=0; x<=getWidth()-MyConstants.BORDER_X; x++)
        {
            double y = Math.tan(a)*x - ((MyConstants.GRAVITY/(2*Math.pow(v, 2)*Math.pow(Math.cos(a), 2)))*Math.pow(x, 2));
            double Y = proportionY(y);	///PROPORZIONI Y
            double X = proportionX(x);		///PROPORZIONI X
            if (y < 0 || x > getWidth()-MyConstants.BORDER_X) break;
            line = new Line2D.Double(MyConstants.BORDER_X+X,(getHeight()-MyConstants.BORDER_Y)-Y,MyConstants.BORDER_X+X,(getHeight()-MyConstants.BORDER_Y)-Y);
            g2d.draw(line);
//            g.drawLine(MyConstants.BORDER_X+x,(getHeight()-MyConstants.BORDER_Y)-Y,MyConstants.BORDER_X+x,(getHeight()-MyConstants.BORDER_Y)-Y);
        }
	}
	
	public double proportionX(double x)
	{
        double X = x*(getWidth()-MyConstants.BORDER_X)/MyConstants.ASSE_X;	///PROPORZIONI X
        return X;
	}
	
	public double proportionY(double y)
	{
        double Y = y*(getHeight()-MyConstants.BORDER_Y)/MyConstants.ASSE_Y;	///PROPORZIONI Y
        return Y;
	}
	
	public Graphics2D getGraphics2D()
	{
		return g;
	}
	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		if (draw)
		{
			drawAxis(g2d);
			
			drawTarget(g2d);
			
			drawWeight(g2d);
			
			drawTail(g2d);
			
			drawTargetTail(g2d);
			
			if(MyConstants.SETTINGS_VALUES[MyConstants.SIM_SHOW_BEST_INDEX]) 
			{
				drawBestThrow(g2d);
				drawDebug(g2d);
			}

			
//			drawParabola(g2d, a, v);
		}
	}

	private void drawDebug(Graphics2D g2d)
	{
        g2d.setColor(Color.BLACK);
        Line2D distance = new Line2D.Double(bestTarget.getCenterX(), bestTarget.getCenterY(), bestShot.getCenterX(), bestShot.getCenterY());
        g2d.draw(distance);
        g2d.setColor(Color.BLUE);
		g2d.fill(bestTarget);	
        g2d.setColor(Color.GREEN);
		g2d.fill(bestShot);
	}


	private void drawBestThrow(Graphics2D g2d) 
	{
		drawParabola(g2d, target.getCenterX(), target.getCenterY(), a, v);
	}


	private void drawParabola(Graphics2D g2d, double centerX, double centerY, double a2, double v2) 
	{
        g2d.setColor(Color.BLACK);
        for(int x=0; x<=getWidth()-MyConstants.BORDER_X; x++)
        {
            double y = Math.tan(a)*x - ((MyConstants.GRAVITY/(2*Math.pow(v, 2)*Math.pow(Math.cos(a), 2)))*Math.pow(x, 2));
            double Y = proportionY(y);	///PROPORZIONI Y
            double X = proportionX(x);		///PROPORZIONI X
            if (y < 0 || x > getWidth()-MyConstants.BORDER_X) break;
            line = new Line2D.Double(MyConstants.BORDER_X+X,(getHeight()-MyConstants.BORDER_Y)-Y,MyConstants.BORDER_X+X,(getHeight()-MyConstants.BORDER_Y)-Y);
            g2d.draw(line);
        }
	}


	public void drawAxis(Graphics2D g2d)
	{
        g2d.drawLine(MyConstants.BORDER_X, getHeight()-MyConstants.BORDER_Y, getWidth()-MyConstants.BORDER_X, getHeight()-MyConstants.BORDER_Y); // x-axis
        g2d.drawLine(MyConstants.BORDER_X, getHeight()-MyConstants.BORDER_Y, MyConstants.BORDER_X, MyConstants.BORDER_Y); // y-axis
	}
	
	private void drawTarget(Graphics2D g2d) 
	{
		g2d.setColor(Color.RED);
		g2d.fill(target);	
	}
	
	private void drawWeight(Graphics2D g2d) 
	{
		g2d.setColor(Color.BLACK);
		g2d.fill(peso);
	}
	
	public void drawTail(Graphics2D g2d) 
	{
		g2d.setColor(Color.BLACK);
		for (int i=0; i<tail.size(); i++)
		{
			try 
			{
				tailLine.setLine(tail.get(i).x, tail.get(i).y, tail.get(i).x, tail.get(i).y);
				g2d.draw(tailLine);
			} 
			catch (Exception e) 
			{
				System.out.println("Errore nel disegnare la coda");
			}

		}
	}
	
	private void drawTargetTail(Graphics2D g2d)
	{
		g2d.setColor(Color.RED);
		for (int i=0; i<targetTail.size(); i++)
		{
			try 
			{
				targetTailLine.setLine(targetTail.get(i).x, targetTail.get(i).y, targetTail.get(i).x, targetTail.get(i).y);
				g2d.draw(targetTailLine);
			} 
			catch (Exception e) 
			{
				System.out.println("Errore nel disegnare la coda");
			}

		}
	}
	
//	public void drawTail(Graphics2D g2d) 
//	{
//		g2d.setColor(Color.BLACK);
//		for (Line2D line : lines)
//			g2d.draw(line);
//	}

	public Ellipse2D getPeso()
	{
		return peso;
	}
	
	public Ellipse2D getTarget()
	{
		return target;
	}
	
	public boolean getDraw()
	{
		return draw;
	}
	
	public void setDraw(boolean bool)
	{
		draw = bool;
	}
	
	public Line2D getTailLine()
	{
		return tailLine;
	}
	
	public LinkedList<Vector2d> getTail() 
	{
		return tail;
	}
	
	public LinkedList<Line2D> getLines()
	{
		return lines;
	}

	public void resetTail() 
	{
		tail.removeAll(tail);
	}

	public boolean getShowBest()
	{
		return showBest;
	}

	public void setShowBest(boolean b) 
	{
		showBest = b;
	}
	
	public Line2D getTargetTailLine()
	{
		return targetTailLine;
	}


	public LinkedList<Vector2d> getTargetTail()
	{
		return targetTail;
	}


	public void resetTargetTail()
	{
		targetTail.removeAll(targetTail);
	}
}
