package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.joml.Vector2d;

import common.MyConstants;

public class Chart extends JPanel
{
	private JFrame frame;
	
//	private JLabel descX;
//	private JLabel descY;
	
	private int x_axis;
	private int y_axis;
	
	private double x_axis_length;
	private double y_axis_length;

	private String descX;
	private String descY;
	
	private double minX;
	private double minY;
	private double maxX;
	private double maxY;
	
	private int factor_x;
	private int factor_y;
	
	private double tran_x;
	private double tran_y;
	
	private boolean shifted_x;
	private boolean shifted_y;
	
//	private double prevX;
//	private double prevY;
//	private double currX;
//	private double currY;
	
	private int hyphens_x;
	private int hyphens_y;
	
	private boolean grid;
	
	private ArrayList<ArrayList<Vector2d>> lines = new ArrayList<ArrayList<Vector2d>> ();;
	private ArrayList<String> names = new ArrayList<String> ();
	private ArrayList<Color> colors = new ArrayList<Color> ();

	private boolean startFromMax;

	private boolean startFromFirst;
	
	private int larghezzaNumeri;
	
	public Chart(JFrame frame) 
	{
		this.frame = frame;
		
//		lines 
		
//		prevX = 0;
//		prevY = 0;
//		currX = 0;
//		currY = 0;
		
		grid = false;
		startFromMax = false;
		startFromFirst = false;
    	
		setMinX(0);
		setMinY(0);
		setMaxX(1000);
		setMaxY(100000);
		
		setHyphens_x(ChartConstants.NUMBERS_HYPHENS_X);
		setHyphens_y(ChartConstants.NUMBERS_HYPHENS_Y);
		
//		descX = new JLabel("Prova");
//		descY = new JLabel("Prova");
		descX = "Generation";
		descY = "Fitness";
		
		init();
	}
	
	public Chart(JFrame frame, int maxX, int maxY, String descX, String descY, int hyphensX, int hyphensY) 
	{
		this.frame = frame;
		
//		prevX = 0;
//		prevY = 0;
//		currX = 0;
//		currY = 0;
    	
		setMinX(0);
		setMinY(0);
		setMaxX(maxX);
		setMaxY(maxY);
		
		setHyphens_x(hyphensX);
		setHyphens_y(hyphensY);
		
//		descX = new JLabel("Prova");
//		descY = new JLabel("Prova");
		this.descX = descX;
		this.descY = descY;
		
		init();
	}
	
	public Chart(JFrame frame, int minX, int minY, int maxX, int maxY, String descX, String descY, int hyphensX, int hyphensY) 
	{
		this.frame = frame;
		
//		prevX = 0;
//		prevY = 0;
//		currX = 0;
//		currY = 0;
    	
		setMinX(minX);
		setMinY(minY);
		setMaxX(maxX);
		setMaxY(maxY);
		
		setHyphens_x(hyphensX);
		setHyphens_y(hyphensY);
		
//		descX = new JLabel("Prova");
//		descY = new JLabel("Prova");
		this.descX = descX;
		this.descY = descY;
		
		init();
	}

	public void init()
	{
		setLayout(new GridBagLayout());
		
		setX_axis(55);
		setY_axis(30);
		
		factor_x = 1;
		factor_y = 1;
		tran_x = 0;
		tran_y = 0;
		
		shifted_x = false;
		shifted_y = false;
		
//    	GridBagConstraints gc = new GridBagConstraints();
    	
//		gc.anchor = GridBagConstraints.FIRST_LINE_START;
////		gc.fill = GridBagConstraints.BOTH;
//		
//		gc.weightx = 0.5;
//		gc.weighty = 0.5;
//		
//		gc.gridx = 0;
//		gc.gridy = 0;
//    	
//    	add(descX, gc);
//    	
//		gc.anchor = GridBagConstraints.LAST_LINE_END;
//    	
//		gc.gridx = 1;
//		gc.gridy = 1;
//    	add(descY, gc);
	}

	public void drawAxis(Graphics2D g2d)
	{
		g2d.drawLine(x_axis + ChartConstants.BORDER_X, getHeight()-ChartConstants.BORDER_Y - y_axis, getWidth()-ChartConstants.BORDER_X, getHeight()-ChartConstants.BORDER_Y - y_axis); // x-axis
        g2d.drawLine(x_axis + ChartConstants.BORDER_X, getHeight()-ChartConstants.BORDER_Y - y_axis, x_axis + ChartConstants.BORDER_X, ChartConstants.BORDER_Y); // y-axis
	}
	
	public void drawNumbers(Graphics2D g2d)
	{
		//ASSE X
		double larghezza = (getWidth()-ChartConstants.BORDER_X) - (x_axis + ChartConstants.BORDER_X);
		double stepX = larghezza/hyphens_x;
		double numeroX = (Math.abs(getMinX()) + Math.abs(getMaxX()))/hyphens_x;
		
		//ASSE Y
		double altezza = (getHeight()-ChartConstants.BORDER_Y - y_axis) - (ChartConstants.BORDER_Y);
		double stepY = altezza/hyphens_y;
		double numeroY = (Math.abs(getMinY()) + Math.abs(getMaxY()))/hyphens_y;
		
		// DISEGNA LUNGO L'ASSE X
		double i = x_axis;
		for (int j = (int)getMinX(); j<=getMaxX(); i+=stepX, j+=numeroX)
		{
			Line2D line = new Line2D.Double(ChartConstants.BORDER_X + i, getHeight() - y_axis - ChartConstants.BORDER_Y,
					ChartConstants.BORDER_X + i, getHeight() - y_axis - ChartConstants.BORDER_Y + ChartConstants.HYPHEN_WIDTH);
			g2d.draw(line);	// DISEGNA I TRATTINI
			if (MyConstants.SETTINGS_VALUES[MyConstants.GRAPHS_GRID_INDEX])	// DISEGNA LA GRIGLIA
			{
				Line2D horizontalGrid = new Line2D.Double(ChartConstants.BORDER_X + stepX + i, getHeight() - y_axis - ChartConstants.BORDER_Y,
						ChartConstants.BORDER_X + stepX + i, getHeight() - y_axis - ChartConstants.BORDER_Y - altezza);
				Color c = g2d.getColor();
				g2d.setColor(Color.gray);
				g2d.draw(horizontalGrid);
				g2d.setColor(c);
			}
			String s = "" + j;
			
			int sWidth = g2d.getFontMetrics().stringWidth(s);
			
			g2d.drawString(s, ChartConstants.BORDER_X + (int)i - sWidth/2, getHeight() - y_axis);	// DISEGNA I NUMERI
		}
		
		// DISEGNA LUNGO L'ASSE Y
		i = y_axis;
		for (int j = (int)getMinY(); j<=getMaxY(); i+=stepY, j+=numeroY)
		{
			Line2D line = new Line2D.Double(x_axis + ChartConstants.BORDER_X, getHeight() - ChartConstants.BORDER_Y - i,
					 x_axis + ChartConstants.BORDER_X - ChartConstants.HYPHEN_WIDTH, getHeight() - ChartConstants.BORDER_Y - i);
			g2d.draw(line);	// DISEGNA I TRATTINI
			if (MyConstants.SETTINGS_VALUES[MyConstants.GRAPHS_GRID_INDEX])	// DISEGNA LA GRIGLIA
			{
				Line2D verticalGrid = new Line2D.Double(x_axis + ChartConstants.BORDER_X, getHeight() - ChartConstants.BORDER_Y - i - stepY,
						 x_axis + ChartConstants.BORDER_X + larghezza, getHeight() - ChartConstants.BORDER_Y - i - stepY);
				Color c = g2d.getColor();
				g2d.setColor(Color.gray);
				g2d.draw(verticalGrid);
				g2d.setColor(c);
			}
			
			String s = "" + j;
			
			int sWidth = g2d.getFontMetrics().stringWidth(s);
			
			larghezzaNumeri = sWidth;
			
			g2d.drawString(s, ChartConstants.BORDER_X + x_axis - sWidth - 15, getHeight() - ChartConstants.BORDER_Y - (int)i + 5);	// DISEGNA I NUMERI
		}
	}
	
	public void drawDesc(Graphics2D g2d)
	{
		int descXWidth = g2d.getFontMetrics().stringWidth(descX);
		
        g2d.drawString(descX, getWidth() - MyConstants.BORDER_X -  descXWidth, getHeight() - y_axis + MyConstants.BORDER_Y);
        
        AffineTransform orig = g2d.getTransform();
        g2d.rotate(Math.toRadians(270));
        
        int descYWidth = g2d.getFontMetrics().stringWidth(descY);
        
    	// Per scrivere la descrizione subito a sinistra dei numeri usare: (x_axis - larghezzaNumeri) come componente y (al posto di MyConstants.BORDER_X)
        g2d.drawString(descY, - MyConstants.BORDER_Y - descYWidth, MyConstants.BORDER_X);
        
        g2d.setTransform(orig);
	}
	
	public void drawCurve(Graphics2D g2d, double x, double y)
	{
		double prevX = 0;
		double prevY = 0;
		double currX = proportionX(x);
		double currY = proportionY(y);
		
//		g2d.fillOval(x_axis + ChartConstants.BORDER_X + currX, getHeight() - y_axis - ChartConstants.BORDER_Y - currY, 3, 3);
//		g2d.drawLine(x_axis + ChartConstants.BORDER_X + prevX, getHeight() - y_axis - ChartConstants.BORDER_Y - prevY
//				, x_axis + ChartConstants.BORDER_X + currX, getHeight() - y_axis - ChartConstants.BORDER_Y - currY);
		Line2D line = new Line2D.Double(x_axis + ChartConstants.BORDER_X + prevX + 1, getHeight() - y_axis - ChartConstants.BORDER_Y - prevY - 1
				, x_axis + ChartConstants.BORDER_X + currX + 1, getHeight() - y_axis - ChartConstants.BORDER_Y - currY - 1);
		Stroke s = g2d.getStroke();
		g2d.setStroke(new BasicStroke(2));
		g2d.draw(line);
		g2d.setStroke(s);
	}
	
	public void drawCurve(Graphics2D g2d, ArrayList<Vector2d> line)
	{
		double prevX = 0;
		double prevY = 0;
		double currX = 0;
		double currY = 0;
		
		if (shifted_x) tran_x = ((getWidth()-ChartConstants.BORDER_X) - (x_axis + ChartConstants.BORDER_X))/factor_x; 
		if (shifted_y) tran_y = ((getHeight()-ChartConstants.BORDER_Y - y_axis) - (ChartConstants.BORDER_Y))/factor_y;
		
		if (startFromMax)
		{
			prevY = proportionY(getMaxY());
			currY = proportionY(getMaxY());
		}
		else if (startFromFirst && line.size() > 0)
		{
			prevY = proportionY(line.get(0).y);
			currY = proportionY(line.get(0).y);
		}
		
		for (Vector2d punto : line)
		{
			prevX = currX;
			prevY = currY;
			currX = proportionX(punto.x);
			currY = proportionY(punto.y);
			
			Line2D segment = new Line2D.Double(tran_x + x_axis + ChartConstants.BORDER_X + prevX + 1, getHeight() - tran_y - y_axis - ChartConstants.BORDER_Y - prevY - 1
					, tran_x + x_axis + ChartConstants.BORDER_X + currX + 1, getHeight() - tran_y - y_axis - ChartConstants.BORDER_Y - currY - 1);
//			Stroke s = g2d.getStroke();
			Color c = g2d.getColor();
//			g2d.setStroke(new BasicStroke(2));
			g2d.setColor(colors.get(lines.indexOf(line)));
			g2d.draw(segment);
//			g2d.setStroke(s);
			g2d.setColor(c);
		}
	}
	
	public void addDescription()
	{
		//TODO
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		drawAxis(g2d);
		drawNumbers(g2d);
		drawDesc(g2d);
		
//		if (grid) 
//			drawGrid(g2d);
		
		//TODO DISEGNA TUTTI I PUNTI COLLEGATI
		if (lines.size() > 0)
		{
			for (ArrayList<Vector2d> line : lines)
			{
				drawCurve(g2d, line);
			}
				
		}
		
	}
	
	public double proportionX(double x)
	{
		int larghezza = (getWidth()-ChartConstants.BORDER_X) - (x_axis + ChartConstants.BORDER_X);
		if (shifted_x) larghezza = larghezza/factor_x;
        double X = x*larghezza/maxX;	///PROPORZIONI X
        return X;
	}
	
	public double proportionY(double y)
	{
		int altezza = (getHeight()-ChartConstants.BORDER_Y - y_axis) - (ChartConstants.BORDER_Y);
		if (shifted_y) altezza = altezza/factor_y;
        double Y = y*altezza/maxY;	///PROPORZIONI Y
        return Y;
	}
	
	public double getMaxX() 
	{
		return maxX;
	}

	public void setMaxX(double maxX) 
	{
		this.maxX = maxX;
	}

	public double getMaxY()
	{
		return maxY;
	}

	public void setMaxY(double maxY) 
	{
		this.maxY = maxY;
	}
	
	public int getX_axis() 
	{
		return x_axis;
	}

	public void setX_axis(int x_axis) 
	{
		this.x_axis = x_axis;
	}

	public int getY_axis() 
	{
		return y_axis;
	}

	public void setY_axis(int y_axis) 
	{
		this.y_axis = y_axis;
	}
	
	public int getHyphens_x() 
	{
		return hyphens_x;
	}

	public void setHyphens_x(int hyphens_x) 
	{
		this.hyphens_x = hyphens_x;
	}

	public int getHyphens_y() 
	{
		return hyphens_y;
	}

	public void setHyphens_y(int hyphens_y)
	{
		this.hyphens_y = hyphens_y;
	}

	public ArrayList<ArrayList<Vector2d>> getLines() 
	{
		return lines;
	}

	public void addVector(int array_index, Vector2d vector) 
	{
		lines.get(array_index).add(vector);
	}

	public void addLine(String lineName, Color lineColor) 
	{
		lines.add(new ArrayList<Vector2d> ());
		names.add(lineName);
		colors.add(lineColor);
	}
	
	public void setGrid(boolean grid)
	{
		this.grid = grid;
	}
	
	public boolean getGrid()
	{
		return grid;
	}
	
	public ArrayList<String> getNames() 
	{
		return names;
	}

	public ArrayList<Color> getColors()
	{
		return colors;
	}

	public void startFromMax() 
	{
		startFromFirst = false;
		startFromMax = true;
	}
	
	public void startFromFirst()
	{
		startFromMax = false;
		startFromFirst = true;
	}
	
	public void reset()
	{
		for (ArrayList<Vector2d> arr: lines)
			arr.clear();
	}

	public void setLines(ArrayList<ArrayList<Vector2d>> lines)
	{
		this.lines = lines;
	}
	
	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}
	
	public void setNegativeMinX()
	{
		this.minX = -maxX;
		this.factor_x = 2;
		this.tran_x = x_axis_length/2;
		shifted_x = true;
	}
	
	public void setNegativeMinY()
	{
		this.minY = -maxY;
		this.factor_y = 2;
		this.tran_y = y_axis_length/2;
		shifted_y = true;
	}
	
	
}
