package gui;

import jneat.Genome;

public class MyMethods 
{
	public static void generateGenomeToFile(String filename, int id, int i, int o, int n, int nmax, boolean r, double linkprob, boolean between_layer_links, boolean input_to_output_links)
	{
		new Genome(id, i, o, n, nmax, r, linkprob, between_layer_links, input_to_output_links).print_to_filename(filename);
	}
}
