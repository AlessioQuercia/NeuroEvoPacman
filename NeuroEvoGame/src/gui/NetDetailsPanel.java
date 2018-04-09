package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class NetDetailsPanel extends JPanel
{
	private JFrame frame;
	
	private JTextPane infoRete;
	

	public NetDetailsPanel(JFrame frame) 
	{
		this.frame = frame;
		
		init();
	}

	private void init()
	{
    	setLayout(new GridBagLayout());
    	
    	GridBagConstraints gc = new GridBagConstraints();
    	
		setBorder(BorderFactory.createTitledBorder("Net details"));
		infoRete = new JTextPane();
		infoRete.setFont(getFont());
		infoRete.setEditable(false);
		infoRete.setOpaque(false);
		infoRete.setVisible(true);
		infoRete.setText("");
		
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		
		gc.gridx = 0;
		gc.gridy = 0;	
		add(infoRete, gc);
	}
	
	public JTextPane getInfoRete() 
	{
		return infoRete;
	}
}
