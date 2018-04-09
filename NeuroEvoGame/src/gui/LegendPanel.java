package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LegendPanel extends JPanel
{
	private JFrame frame;
	
	ArrayList<String> names;
	ArrayList<Color> colors;

	public LegendPanel(JFrame frame) 
	{
		this.frame = frame;
		
		init();
	}

	private void init() 
	{
		setLayout(new GridBagLayout());
		
		setBorder(BorderFactory.createTitledBorder("Chart legend"));	
	}
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		drawLegend(g2d);
	}

	private void drawLegend(Graphics2D g2d) 
	{
		int x = 10;
		int y = 50;
		for (String s : names)
		{
			g2d.drawString(s, x, y);
			Color c = g2d.getColor();
			g2d.setColor(colors.get(names.indexOf(s)));
			int sWidht = g2d.getFontMetrics().stringWidth(s);
			g2d.drawLine(sWidht + 15, y - 5, sWidht + 100, y - 5);
			g2d.setColor(c);
			y+=20;
		}
	}
	
	public void setLegend(ArrayList<String> names, ArrayList<Color> colors)
	{
		this.names = names;
		this.colors = colors;
	}
}
