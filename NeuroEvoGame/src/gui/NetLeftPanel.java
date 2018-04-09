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

import common.MyConstants;
import experiment.evo_in;
import experiment.evo_out;
import jNeatCommon.EnvConstant;
import jneat.Genome;
import jneat.Organism;

public class NetLeftPanel extends JPanel
{
	private JFrame frame;
	
	private NetOptionsPanel optionsPanel;
	private NetDetailsPanel detailsPanel;

	private JLabel generationLabel;
	
	public NetLeftPanel(JFrame frame)
	{
		this.frame = frame;
		
		init();
	}

	private void init() 
	{	
		Dimension size = getPreferredSize();
		size.width = MyConstants.OPTIONS_WIDTH;
		setPreferredSize(size);	
		
		setLayout(new GridBagLayout());
		
		optionsPanel = new NetOptionsPanel(frame);
		
		detailsPanel = new NetDetailsPanel(frame);
		
		generationLabel = new JLabel();
		
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 0.5;
		gc.weighty = 1;
		
		gc.gridx = 0;
		gc.gridy = 0;		
		add(optionsPanel, gc);
		
		gc.weighty = 20;
		gc.gridx = 0;
		gc.gridy = 1;		
		add(detailsPanel, gc);
		
		gc.weighty = 0.01;
		gc.gridx = 0;
		gc.gridy = 2;		
		add(generationLabel, gc);
	}

	public JLabel getGenerationLabel()
	{
		return generationLabel;
	}
	
	public NetOptionsPanel getOptionsPanel() 
	{
		return optionsPanel;
	}
	
	public void updateInfoRete(Organism o)
	{
//		System.out.println(MyConstants.INPUT_NODES_ID.get(0) + " " + evo_in.inputNames[0]);
//		System.out.println(MyConstants.INPUT_NODES_ID.get(1) + " " + evo_in.inputNames[1]);
//		System.out.println(MyConstants.INPUT_NODES_ID.get(2) + " " + evo_in.inputNames[2]);
		
		
		int numNodes = o.getGenome().getNodes().size();
		int inputNodes = EnvConstant.NR_UNIT_INPUT;
		int outputNodes = EnvConstant.NR_UNIT_OUTPUT;
		int biasNodes = EnvConstant.NR_UNIT_BIAS;
		int hiddenNodes = numNodes - inputNodes - outputNodes - biasNodes;
		String info_rete =
				"Total nodes:  " + numNodes + "\n" +
				"Bias nodes:  " + biasNodes + "\n" +
				"Input nodes:  " + inputNodes + "\n" +
				"Hidden nodes:  " + hiddenNodes + "\n" +
				"Output nodes:  " + outputNodes + "\n" + 
				"\n" + "Input nodes list:  " + "\n";
		
		for (int i=0; i<o.getGenome().getInputNodesID().size(); i++)
		{
			info_rete += "- ID_" + o.getGenome().getInputNodesID().get(i) + " = " + evo_in.inputNames[i] + "\n";
		}

//				"- id_1 = " + evo_in.inputNames[0] + "\n" +
//				"- id_2 = " + evo_in.inputNames[1] + "\n" +
//				"- id_3 = " + evo_in.inputNames[2] + "\n" +
		
		info_rete += "\n" + "Output nodes list:  " + "\n";
		
		for (int i=0; i<o.getGenome().getOutputNodesID().size(); i++)
		{
			info_rete += "- ID_" + o.getGenome().getInputNodesID().get(i) + " = " + evo_out.outputNames[i] + "\n";
		}
		
//				"- id_9 = " + evo_out.outputNames[0] + "\n" +
//				"- id_10 = " + evo_out.outputNames[1] + "\n" +
//				"- id_11 = " + evo_out.outputNames[2] + "\n";

			
			detailsPanel.getInfoRete().setText(info_rete);
	}
}
