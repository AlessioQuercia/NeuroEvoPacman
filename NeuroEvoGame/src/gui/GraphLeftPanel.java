package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import common.MyConstants;

public class GraphLeftPanel extends JPanel
{
	private JFrame frame;

	private GraphOptionsPanel optionsPanel;
	private LegendPanel legendPanel;
	private JLabel generationLabel;
	private ForzaOptionsPanel forzaOptionsPanel;

	private GridBagConstraints gc;
	
	SimpleAttributeSet attributes;
    SimpleAttributeSet attr;

	private boolean forzaPanel;

	public GraphLeftPanel(JFrame frame)
	{
		this.frame = frame;
		
		forzaPanel = false;
		
		init();
	}
	
	public void init()
	{
		Dimension size = getPreferredSize();
		size.width = MyConstants.OPTIONS_WIDTH;
		setPreferredSize(size);	
		
    	setLayout(new GridBagLayout());
//		setBorder(BorderFactory.createTitledBorder("LEFT PANEL"));
		
    	gc = new GridBagConstraints();
		
		optionsPanel = new GraphOptionsPanel(frame);
		legendPanel = new LegendPanel(frame);
		generationLabel = new JLabel();
		forzaOptionsPanel = new ForzaOptionsPanel(frame);
		
		///  STILE SCRITTURA
		attributes = new SimpleAttributeSet();
	    StyleConstants.setBold(attributes, true);
	    StyleConstants.setItalic(attributes, true);
	    
		attr = new SimpleAttributeSet();
	    StyleConstants.setBold(attr, true);
	    StyleConstants.setItalic(attr, true);

		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 0.5;
		gc.weighty = 1;
		
		gc.gridx = 0;
		gc.gridy = 0;	
		add(optionsPanel, gc);	
		
		gc.weighty = 20;
		
		gc.gridx = 0;
		gc.gridy = 1;	
		add(legendPanel, gc);	
		
		gc.weighty = 0.01;
		
		gc.gridx = 0;
		gc.gridy = 2;		
		add(generationLabel, gc);
	}
	
	public void setNormalPanel()
	{
		forzaPanel = false;
//		remove(optionsPanel);
//		remove(forzaOptionsPanel);
//		remove(legendPanel);
//		remove(generationLabel);
		removeAll();
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 0.5;
		gc.weighty = 1;
		
		gc.gridx = 0;
		gc.gridy = 0;	
		add(optionsPanel, gc);	
		
		gc.weighty = 20;
		
		gc.gridx = 0;
		gc.gridy = 1;	
		add(legendPanel, gc);	
		
		gc.weighty = 0.01;
		
		gc.gridx = 0;
		gc.gridy = 2;		
		add(generationLabel, gc);
		
		repaint();
	}
	
	public void setForzaPanel()
	{
		forzaPanel = true;
		remove(optionsPanel);
		remove(legendPanel);
		remove(generationLabel);
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 0.5;
		gc.weighty = 1;
		
		gc.gridx = 0;
		gc.gridy = 0;	
		add(optionsPanel, gc);	
		
		gc.gridx = 0;
		gc.gridy = 1;	
		add(forzaOptionsPanel, gc);
		
		gc.weighty = 20;
		
		gc.gridx = 0;
		gc.gridy = 2;	
		add(legendPanel, gc);	
		
		gc.weighty = 0.01;
		
		gc.gridx = 0;
		gc.gridy = 3;		
		add(generationLabel, gc);
		
		repaint();
	}

	
	public GraphOptionsPanel getOptionsPanel() 
	{
		return optionsPanel;
	}

	public LegendPanel getLegendPanel() 
	{
		return legendPanel;
	}
	
	public JLabel getGenerationLabel() 
	{
		return generationLabel;
	}
	
    public ForzaOptionsPanel getForzaOptionsPanel()
    {
		return forzaOptionsPanel;
	}

	public boolean getForzaPanel()
	{
		return forzaPanel;
	}

	public void setForzaPanel(boolean forzaPanel)
	{
		this.forzaPanel = forzaPanel;
	}
}
