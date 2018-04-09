package gui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import common.MyConstants;
import gui.vectTableModel;
import jNeatCommon.EnvConstant;
import jNeatCommon.EnvRoutine;
import jneat.Neat;

public class SettingsPanel extends JPanel implements ActionListener
{
	private JFrame frame;
	
	private SettingsLeftPanel leftPanel;
	private SessionSettings sessionSettings;
	private ParameterSettings parameterSettings;
	private OtherSettings otherSettings;
	
	private GridBagConstraints gc;
	
	public SettingsPanel(JFrame frame) 
	{
		this.frame = frame;
		
		init();
	}
	
	public void init()
	{
		setLayout(new GridBagLayout());	
		
		leftPanel = new SettingsLeftPanel(frame);
//		sessionSettings = new SessionSettings(frame);
		parameterSettings = new ParameterSettings(frame);
		otherSettings = new OtherSettings(frame);
		
		parameterSettings.getUpperPanel().getDefaultParameters().addActionListener(this);
		otherSettings.getUpperPanel().getDefaultSettings().addActionListener(this);
		
    	gc = new GridBagConstraints();
		
    	
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.fill = GridBagConstraints.VERTICAL;
		
		gc.gridx = 0;
		gc.gridy = 0;
    	add(leftPanel, gc);
    	
    	gc.fill = GridBagConstraints.BOTH;
		
		gc.weightx = 0.5;
		gc.weighty = 0.5;
    	
		gc.gridx = 1;
		gc.gridy = 0;
//    	add(sessionSettings, gc);
    	add(parameterSettings, gc);
    	add(otherSettings, gc);
	}
	
//	public void setSessionSection()
//	{
//		gc.weightx = 0.5;
//		gc.weighty = 0.5;
//		gc.gridx = 1;
//		gc.gridy = 0;
//		remove(sessionSettings);
//		remove(parameterSettings);
//		remove(otherSettings);
//    	add(sessionSettings, gc);
//	}
	
	public void setParameterSection()
	{
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.gridx = 1;
		gc.gridy = 0;
//		remove(sessionSettings);
		remove(parameterSettings);
		remove(otherSettings);
    	add(parameterSettings, gc);
    	repaint();
	}
	
	public void setOtherSection()
	{
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.gridx = 1;
		gc.gridy = 0;
//		remove(sessionSettings);
		remove(parameterSettings);
		remove(otherSettings);
    	add(otherSettings, gc);
    	repaint();
	}
	
	public SettingsLeftPanel getLeftPanel() 
	{
		return leftPanel;
	}
	
//	public SessionSettings getSessionSettings() 
//	{
//		return sessionSettings;
//	}

	public ParameterSettings getParameterSettings() 
	{
		return parameterSettings;
	}

	public OtherSettings getOtherSettings()
	{
		return otherSettings;
	}

	public GridBagConstraints getGc() 
	{
		return gc;
	}
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		
//		if (getLeftPanel().getList().getSelectedIndex() == 0 && !getLeftPanel().getSession())
//		{
//			setSessionSection();
//			getLeftPanel().setSession(true);
//			getLeftPanel().setParameter(false);
//			getLeftPanel().setOther(false);
//		}
		
		if (getLeftPanel().getList().getSelectedIndex() == 0 && !getLeftPanel().getParameter())
		{
			setParameterSection();
//			getLeftPanel().setSession(false);
			getLeftPanel().setParameter(true);
			getLeftPanel().setOther(false);
		}
		
		else if (getLeftPanel().getList().getSelectedIndex() == 1 && !getLeftPanel().getOther())
		{
			setOtherSection();
//			getLeftPanel().setSession(false);
			getLeftPanel().setParameter(false);
			getLeftPanel().setOther(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		 JButton p = (JButton) e.getSource();
		 
		 if (p.getActionCommand().equals("Restore default parameters")) 
		 {
			 parameterSettings.getUpperPanel().setNetx(new Neat());
			 parameterSettings.getUpperPanel().getNetx().initbase();
			 boolean rc = parameterSettings.getUpperPanel().getNetx().readParam(MyConstants.COMPUTER_DIR + MyConstants.DEFAULT_PARAMETERS);
			 
			 if (!rc)
				 System.out.println("Error restoring default parameters!");
			 parameterSettings.getUpperPanel().getModel().data.clear();
			 parameterSettings.getUpperPanel().getModel().rows = -1;
			 parameterSettings.getUpperPanel().getNetx().getParam(parameterSettings.getUpperPanel().getModel());
			 parameterSettings.getUpperPanel().getModel().fireTableDataChanged();
			 
		 }
		 
		 if (p.getActionCommand().equals("Restore default settings"))
		 {
			 otherSettings.getUpperPanel().loadSettings(MyConstants.COMPUTER_DIR + MyConstants.DEFAULT_OTHER_SETTINGS);
			 otherSettings.getUpperPanel().updateComboBoxes();
		 }
	}
}
