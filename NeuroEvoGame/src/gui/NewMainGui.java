package gui;

import javax.swing.JFrame;

import common.MyConstants;

public class NewMainGui 
{
	public static void main(String[] args) 
	{
		JFrame frame = new MainFrame();
		MainPanel mainPanel = new MainPanel(frame); 
		frame.setVisible(true);
		
		
//		// GENERAZIONE GENOMA SU FILE
//		String filename = MyConstants.DATA_DIR + "pacman_17x0x4";
//		int ID = 1;
//		int in_nodes = 17; //compreso un bias
//		int out_nodes = 4;
//		int hidden_nodes = 0;
//		int max_nodes = in_nodes+out_nodes+hidden_nodes;
//		boolean recur = false;
//		double link_prob = 1;
//		boolean between_layer_nodes = false;
//		boolean input_to_output_links = true;
//		
//		MyMethods.generateGenomeToFile(filename, ID, in_nodes, out_nodes, hidden_nodes, hidden_nodes, 
//				recur, link_prob, between_layer_nodes, input_to_output_links);
	}
}
