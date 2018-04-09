package gui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import common.MyConstants;

public class InfoPanel extends JPanel
{
	public InfoPanel() 
	{
		Dimension size = getPreferredSize();
		
		size.width = MyConstants.OPTIONS_WIDTH;
		setPreferredSize(size);
		
		setBorder(BorderFactory.createTitledBorder("Options"));
	}
}
