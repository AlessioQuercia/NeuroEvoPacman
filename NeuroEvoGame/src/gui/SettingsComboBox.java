package gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JComboBox;

public class SettingsComboBox extends JComboBox implements FocusListener
{
	
	private String description;
	
	public SettingsComboBox(String description) 
	{
		this.description = description;
		addFocusListener(this);
	}

	@Override
	public void focusGained(FocusEvent e) 
	{
		OtherSettings.updateDescription(description);
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		OtherSettings.updateDescription("");
	}

}
