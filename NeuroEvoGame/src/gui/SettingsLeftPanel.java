package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import common.MyConstants;

public class SettingsLeftPanel extends JPanel
{
	private JFrame frame;
	
	private JList list;
	
	private boolean session, parameter, other;

	public SettingsLeftPanel(JFrame frame) 
	{
		this.frame = frame;
		
		init();
	}
	
	public void init()
	{
		setBorder(BorderFactory.createTitledBorder("Settings selection"));
		
		setLayout(new GridBagLayout());	
		
		Dimension size = getPreferredSize();
		
		size.width = MyConstants.OPTIONS_WIDTH;
		setPreferredSize(size);
		
    	GridBagConstraints gc = new GridBagConstraints();
    	
    	String[] data = {"Parameters", "Other settings"};
    	
    	list = new JList(data); //data has type Object[]
    	list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	list.setLayoutOrientation(JList.VERTICAL);
//    	list.setVisibleRowCount(-1); 
    	list.setBackground(null);
    	list.setSelectedIndex(0);
    	
    	session = true;
    	parameter = false;
    	other = false;
    	
//		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.fill = GridBagConstraints.BOTH;
		
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		
		gc.gridx = 0;
		gc.gridy = 0;
    	add(list, gc);
    	setMinimumSize(size);
	}
	
	public JList getList() 
	{
		return list;
	}
	
	public boolean getSession() {
		return session;
	}

	public void setSession(boolean session) {
		this.session = session;
	}

	public boolean getParameter() {
		return parameter;
	}

	public void setParameter(boolean parameter) {
		this.parameter = parameter;
	}

	public boolean getOther() {
		return other;
	}

	public void setOther(boolean other) {
		this.other = other;
	}

}
