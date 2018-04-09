package gui;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SessionSettings extends JPanel
{
	private JFrame frame;
	
	public SessionSettings(JFrame frame) 
	{
		this.frame = frame;
		
		init();
	}
	
	public void init()
	{
		setBorder(BorderFactory.createTitledBorder("Session"));
		
		setLayout(new GridBagLayout());	
	}
}
