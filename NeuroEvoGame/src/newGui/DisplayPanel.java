package newGui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.joml.Vector2d;

import common.MyConstants;
import newGui.infra.Display;
import newGui.infra.Game;
import newGui.infra.Keyboard;

public class DisplayPanel extends JPanel
{
	private JFrame frame;
	
	private Graphics2D g;
	
	private Game game;
	
	private GridBagConstraints gc;

	public DisplayPanel(JFrame frame) 
	{
		this.frame = frame;
		
		g = (Graphics2D) getGraphics();
		
		init();
	}
	
	
	private void init() 
	{
		setBorder(BorderFactory.createTitledBorder("Simulation"));
		
		setLayout(new GridBagLayout());	
		
		gc = new GridBagConstraints();
		
		game = new PacmanGame();
		
        int sx = (int) (game.screenSize.width * game.screenScale.getX());
        int sy = (int) (game.screenSize.height * game.screenScale.getY());
        setMinimumSize(new Dimension(sx, sy));
        setPreferredSize(new Dimension(sx, sy));
        addKeyListener(new Keyboard());
        
		game.init();
        
//		////// First column ////////
//		gc.anchor = GridBagConstraints.LINE_START;
//		gc.fill = GridBagConstraints.BOTH;
//		gc.weightx = 0.5;
//		gc.weighty = 0.5;
//		
//		gc.gridx = 0;
//		gc.gridy = 0;	
//		add(game, gc);
		
        requestFocus();
		
	}

	public void clearPanel()
	{
        repaint();
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
        g2d.setBackground(Color.BLACK);
        g2d.clearRect(0, 0, getWidth(), getHeight());
        g2d.scale(game.screenScale.getX(), game.screenScale.getY());
		game.draw(g2d);
        g2d.dispose();
	}

    public void update() {
        game.update();
    }
    
    public Game getGame()
    {
    	return game;
    }
	
}