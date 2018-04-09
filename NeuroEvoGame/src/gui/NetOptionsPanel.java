package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NetOptionsPanel extends JPanel
{
	private JFrame frame;
	
	private JComboBox generationList;
//	private JButton autodrawBtn;

	public NetOptionsPanel(JFrame frame) 
	{
		this.frame = frame;
		
		init();
	}

	private void init()
	{
		setLayout(new GridBagLayout());
		
		setBorder(BorderFactory.createTitledBorder("Net selection"));
		
		JLabel genLabel = new JLabel("Generation: ");
		
		generationList = new JComboBox();
		Dimension size = generationList.getSize();
		size.width = 120;
		size.height = 25;
		generationList.setMinimumSize(size);
		generationList.setPreferredSize(size);
		generationList.setMaximumSize(size);
		
//		autodrawBtn = new JButton("Auto-draw: OFF");
		
		GridBagConstraints gc = new GridBagConstraints();
		
		////// First column ////////
		gc.anchor = GridBagConstraints.LINE_START;
//		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.weighty = 0.5;
		
		gc.gridx = 0;
		gc.gridy = 0;	
		add(genLabel, gc);
		
		////// Second column ////////
		gc.anchor = GridBagConstraints.LINE_END;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.gridx = 1;
		gc.gridy = 0;	
		add(generationList, gc);
		
		
//		gc.weightx = 0.5;
////		gc.weighty = 20;
//		gc.anchor = GridBagConstraints.FIRST_LINE_START;
//		gc.gridx = 1;
//		gc.gridy = 1;
//		add(autodrawBtn, gc);
	}
	
	public JComboBox getGenerationList() 
	{
		return generationList;
	}

//	public JButton getAutodrawBtn() 
//	{
//		return autodrawBtn;
//	}
}
