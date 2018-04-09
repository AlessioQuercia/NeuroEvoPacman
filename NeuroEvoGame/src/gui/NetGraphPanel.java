package gui;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class NetGraphPanel extends JPanel
{
	private JFrame frame;
	
	public NetGraphPanel(JFrame frame) 
	{
		this.frame = frame;
		
		init();
	}

	private void init() 
	{
		setLayout(new GridBagLayout());
		
		setBorder(BorderFactory.createTitledBorder("Selected net"));
	}
}
