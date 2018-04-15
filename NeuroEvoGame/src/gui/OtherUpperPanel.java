package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import common.MyConstants;
import jGraph.graph;

public class OtherUpperPanel extends JPanel
{
	private JFrame frame;
	
	private JLabel simulation;
	private JLabel graphs;
	private JLabel net;
	private JLabel[] options;
	private JButton defaultSettings;
	
	private SettingsComboBox[] comboBoxes;
	private boolean[] values;
	private String[] descriptions = 
		{ 
			"If enabled, Evolution Panel will be automatically updated when a better net is found.",
			"If on, best throw's trajectory will be shown in the simulation in the Evolution Panel. \n"
			+ "The best throw is the one that has the angle which permits to use the minimum velocity.",
			"If enabled, collisions with the x axis will be shown in the simulation in the Evolution Panel.", 
			"If enabled, force chart will be automatically updated when a better net is found.", 
			"If on, charts' grid will be shown in the Graphs Panel.", 
			"If enabled, Net Panel will be automatically updated when a better net is found."
		};

	private String filename;
	
	public OtherUpperPanel(JFrame frame) 
	{
		this.frame = frame;
		
		init();
	}
	
	public void init()
	{
		setBorder(BorderFactory.createTitledBorder("Other settings"));
		
		setLayout(new GridBagLayout());	
		
		GridBagConstraints gc = new GridBagConstraints();
		
		String[][] sim_opt = { {"Disabled", "Enabled"}, {"OFF", "ON"}, {"Disabled", "Enabled"} };
		String[] sim_boxes = {"Auto-update net: ", "Draw best throw: ", "Collisions: "};
		String[][] graphs_opt = { {"Disabled", "Enabled"}, {"OFF", "ON"} };
		String[] graphs_boxes = {"Auto-update net: ", "Charts grid: "};
		String[][] net_opt = { {"Disabled", "Enabled"} };
		String[] net_boxes = {"Auto-update net"};
		
		simulation = new JLabel("Evolution settings");
		graphs = new JLabel("Graphs settings");
		net = new JLabel("Net settings");
		
		int length = sim_boxes.length + graphs_boxes.length + net_boxes.length;
		
		values = new boolean[length];
		
		filename = MyConstants.DATA_DIR + MyConstants.OTHER_SETTINGS;
		
		loadSettings(filename);
		
		comboBoxes = new SettingsComboBox[length];
		options = new JLabel[length];
		defaultSettings = new JButton("Restore default settings");
		
		Dimension size = getSize();
		size.width = 200;
		size.height = 25;
		
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		
		gc.gridx = 0;
		gc.gridy = 0;
    	add(simulation, gc);
		
		int pos = 0;
		
		for (int i=0; i<sim_boxes.length; i++)
		{
			comboBoxes[i] = new SettingsComboBox(descriptions[i]);
			comboBoxes[i].setPreferredSize(size);
			for (int k=0; k<sim_opt[i].length; k++)
				comboBoxes[i].addItem(sim_opt[i][k]);
			options[i] = new JLabel(sim_boxes[i]);
			gc.anchor = GridBagConstraints.WEST;
//			gc.fill = GridBagConstraints.VERTICAL;
			gc.weightx = 0.5;
			gc.weighty = 0.5;
			
			gc.gridx = 0;
			gc.gridy = i+1;
	    	add(options[i], gc);
	    	
	    	gc.weightx = 10;
	    	
	    	gc.gridx = 1;
	    	gc.gridy = i+1;
	    	add(comboBoxes[i], gc);
		}
		pos+=sim_boxes.length+1;
		
		gc.anchor = GridBagConstraints.WEST;
		gc.weightx = 0.5;
		gc.weighty = 1;
		
		gc.gridx = 0;
		gc.gridy = pos++;
    	add(graphs, gc);
    	
    	int index = sim_boxes.length;
    	
		for (int i=0; i<graphs_boxes.length; i++)
		{
			comboBoxes[index + i] = new SettingsComboBox(descriptions[i]);
			comboBoxes[index + i].setPreferredSize(size);
			for (int k=0; k<graphs_opt[i].length; k++)
				comboBoxes[index + i].addItem(graphs_opt[i][k]);
			options[index + i] = new JLabel(graphs_boxes[i]);
			gc.anchor = GridBagConstraints.WEST;
//			gc.fill = GridBagConstraints.VERTICAL;
			gc.weightx = 0.5;
			gc.weighty = 0.5;
			
			gc.gridx = 0;
			gc.gridy = pos;
	    	add(options[index + i], gc);
	    	
	    	gc.weightx = 10;
	    	
	    	gc.gridx = 1;
	    	gc.gridy = pos++;
	    	add(comboBoxes[index + i], gc);
		}
    	
    	
		gc.anchor = GridBagConstraints.WEST;
		gc.weightx = 0.5;
		gc.weighty = 1;
		
		gc.gridx = 0;
		gc.gridy = pos++;
    	add(net, gc);
    	
    	index += graphs_boxes.length;
    	
		for (int i=0; i<net_boxes.length; i++)
		{
			comboBoxes[index + i] = new SettingsComboBox(descriptions[i]);
			comboBoxes[index + i].setPreferredSize(size);
			for (int k=0; k<net_opt[i].length; k++)
				comboBoxes[index + i].addItem(net_opt[i][k]);
			options[index + i] = new JLabel(net_boxes[i]);
			gc.anchor = GridBagConstraints.WEST;
//			gc.fill = GridBagConstraints.VERTICAL;
			gc.weightx = 0.5;
			gc.weighty = 0.5;
			
			gc.gridx = 0;
			gc.gridy = pos;
	    	add(options[index + i], gc);
	    	
	    	gc.weightx = 10;
	    	
	    	gc.gridx = 1;
	    	gc.gridy = pos++;
	    	add(comboBoxes[index + i], gc);
		}
		
		defaultSettings.setPreferredSize(size);
		
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.weightx = 0.5;
		gc.weighty = 10;
		
		gc.gridx = 1;
		gc.gridy = pos;
    	add(defaultSettings, gc);
    	
    	updateComboBoxes();
	}
	
	public boolean loadSettings(String filename)
	{
		boolean success = false;
		
		try 
		{
			File file = new File(filename);
			FileReader fr = new FileReader(file);
	        BufferedReader br = new BufferedReader(fr);
	        
	        int count = 0;
	        String currentLine;
	        while ((currentLine = br.readLine()) != null)
	        {
	        	int readNumber = Integer.parseInt(currentLine);
	        	
	        	if (readNumber == 0)
	        	{
	        		values[count] = false;
	        		count++;
	        	}
	        	else if (readNumber == 1)
	        	{
	        		values[count] = true;
	        		count++;
	        	}
	        }
	        MyConstants.SETTINGS_VALUES = values;
			success = true;
		} 
		catch (FileNotFoundException e) 
		{
			success = false;
			e.printStackTrace();
			return success;
		} 
		catch (IOException e) 
		{
			success = false;
			e.printStackTrace();
			return success;
		}

		return success;
	}
	
	public boolean saveSettings(String filename)
	{
		boolean success = false;
		try 
		{
			File file = new File(filename);

	        FileWriter fw = new FileWriter(file);
	        BufferedWriter bw = new BufferedWriter(fw);
	        
	        String info = "";
	        
	        for (int i=0; i<values.length; i++)
	        {
	        	if (!values[i])	
	        		info += 0;
	        	else
	        		info += 1;
	        	if (i<values.length-1)
	        		info += "\n";
	        }
	        
	        bw.write(info);
	        
	        bw.flush();
	        bw.close();	
	        
	        success = true;
		} 
		catch (IOException e) 
        {
			e.printStackTrace();
		}
		return success;
	}
	
	public void updateComboBoxes()
	{
    	for (int i = 0; i<values.length; i++)
    	{
    		if(!values[i])
    			comboBoxes[i].setSelectedIndex(0);
    		else
    			comboBoxes[i].setSelectedIndex(1);
    	}
	}
	
	public JButton getDefaultSettings() {
		return defaultSettings;
	}

	public JComboBox[] getComboBoxes() {
		return comboBoxes;
	}

	public boolean[] getValues() {
		return values;
	}

	public String[] getDescriptions() {
		return descriptions;
	}

	public String getFilename() 
	{
		return filename;
	}

	public void updateValue(int index, int value)
	{
		if (value == 0)
			values[index] = false;
		else if (value == 1)
			values[index] = true;
		MyConstants.SETTINGS_VALUES = values;
	}

}
