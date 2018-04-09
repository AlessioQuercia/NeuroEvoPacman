package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class SampleDetailsPanel extends JPanel
{
	private JFrame frame;
	
	private JTextPane infoLancio;
	

	public SampleDetailsPanel(JFrame frame) 
	{
		this.frame = frame;
		
		init();
	}

	private void init()
	{
    	setLayout(new GridBagLayout());
    	
    	GridBagConstraints gc = new GridBagConstraints();
    	
		setBorder(BorderFactory.createTitledBorder("Sample details"));
		infoLancio = new JTextPane();
		infoLancio.setFont(getFont());
		infoLancio.setEditable(false);
		infoLancio.setOpaque(false);
		infoLancio.setVisible(true);
		infoLancio.setText("");
		
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		
		gc.gridx = 0;
		gc.gridy = 0;	
		add(infoLancio, gc);
	}
	
	public JTextPane getInfoLancio() 
	{
		return infoLancio;
	}
}
