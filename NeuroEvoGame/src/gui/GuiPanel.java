package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class GuiPanel extends JPanel
{
	private JFrame frame;
	
	public GuiPanel(JFrame frame) 
	{
		this.frame = frame;
	}
	
	public JFrame getFrame() 
	{
		return frame;
	}
	
	public abstract void init();
}
