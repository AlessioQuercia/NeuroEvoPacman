package gui;

import javax.swing.JFrame;

public class NewMainGui 
{
	public static void main(String[] args) 
	{
		JFrame frame = new MainFrame();
		MainPanel mainPanel = new MainPanel(frame); 
		frame.setVisible(true);
//		String filename = MyConstants.DATA_DIR + "genome_prova";
//		int ID = 1;
//		int in_nodes = 10; //compreso un bias
//		int out_nodes = 3;
//		int hidden_nodes = 0;
//		int max_nodes = in_nodes+out_nodes+hidden_nodes;
//		boolean recur = false;
//		double link_prob = 1;
//		boolean between_layer_nodes = false;
//		boolean input_to_output_links = true;
		
//		MyMethods.generateGenomeToFile(filename, ID, in_nodes, out_nodes, hidden_nodes, hidden_nodes, 
//				recur, link_prob, between_layer_nodes, input_to_output_links);
	}
}
