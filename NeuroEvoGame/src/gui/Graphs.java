package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.joml.Vector2d;

import common.MyConstants;
import jGraph.chartXY;
import jGraph.code;
import jNeatCommon.CodeConstant;
import jNeatCommon.EnvConstant;
import jneat.Organism;
import jneat.Population;
import log.HistoryLog;

public class Graphs extends JPanel implements ActionListener
{
	private JFrame f;
	
	private GraphLeftPanel leftPanel;
	private Chart fitnessChart;
	private Chart errorChart;
	private Chart forzaChart;
	private Chart clonedChart;
	
	private GridBagConstraints gc;
	private boolean fitness, error, forza, cloned;
	
	private boolean autodraw;

	public Graphs(JFrame f) 
	{
		this.f = f;
		
		fitness = true;
		error = false;
		forza = false;
		cloned = false;
		
		autodraw = true;
		
		init();
	}
	
	public void init()
	{
		setLayout(new GridBagLayout());
    	
//		setBorder(BorderFactory.createTitledBorder("Grafico fitness"));
		
    	gc = new GridBagConstraints();
    	
    	leftPanel = new GraphLeftPanel(f);
//    	leftPanel.getOptionsPanel().getGridButton().addActionListener(this);
//    	leftPanel.getForzaOptionsPanel().getAutodrawBtn().addActionListener(this);
    	
		fitnessChart = new Chart(f, MyConstants.EPOCHS_NUMBER, MyConstants.MAX_FITNESS, "Generation", "Fitness", 10, 5);
		fitnessChart.addLine("Mean fitness", Color.BLUE);	// AGGIUNTA LINEA PER RAPPRESENTARE FITNESS MEDIA
		fitnessChart.addLine("Mean cloned fitness", Color.GREEN);	// AGGIUNTA LINEA PER RAPPRESENTARE FITNESS MEDIA DEI CLONATI
		fitnessChart.addLine("Highest fitness", Color.RED);	// AGGIUNTA LINEA PER RAPPRESENTARE FITNESS PIU' ALTA
		fitnessChart.setGrid(true);
		fitnessChart.setBorder(BorderFactory.createTitledBorder("Fitness chart"));
		
		errorChart = new Chart(f, MyConstants.EPOCHS_NUMBER, MyConstants.MAX_ERROR, "Generation", "Error", 10, 10);
//		errorChart.addLine("Mean error", Color.BLUE);	// AGGIUNTA LINEA PER RAPPRESENTARE ERRORE MEDIO
		errorChart.addLine("Lowest error", Color.RED);	// AGGIUNTA LINEA PER RAPPRESENTARE ERRORE PIU' BASSO
		errorChart.setGrid(true);
		errorChart.setBorder(BorderFactory.createTitledBorder("Error chart"));
		errorChart.startFromFirst();
		
		forzaChart = new Chart(f, 50, 300, "Step", "Force", 10, 6);
//		forzaChart.addLine("Mean error", Color.BLUE);	// AGGIUNTA LINEA PER RAPPRESENTARE ERRORE MEDIO
		forzaChart.addLine("Force", Color.RED);	// AGGIUNTA LINEA PER RAPPRESENTARE ERRORE PIU' BASSO
		forzaChart.setGrid(true);
		forzaChart.setBorder(BorderFactory.createTitledBorder("Force chart"));
		forzaChart.setNegativeMinY();
//		forzaChart.startFromFirst();
		
		clonedChart = new Chart(f, 500, 1000, "Generation", "Cloned Organisms", 10, 5);
//		clonedChart.addLine("Mean error", Color.BLUE);	// AGGIUNTA LINEA PER RAPPRESENTARE ERRORE MEDIO
		clonedChart.addLine("Cloned organisms", Color.RED);	// AGGIUNTA LINEA PER RAPPRESENTARE ERRORE PIU' BASSO
		clonedChart.setGrid(true);
		clonedChart.setBorder(BorderFactory.createTitledBorder("Cloned organisms chart"));
//		clonedChart.startFromFirst();

		leftPanel.getLegendPanel().setLegend(fitnessChart.getNames(), fitnessChart.getColors());
		leftPanel.getOptionsPanel().getChartList().addItem("Fitness");
		leftPanel.getOptionsPanel().getChartList().addItem("Error");
		leftPanel.getOptionsPanel().getChartList().addItem("Force");
		leftPanel.getOptionsPanel().getChartList().addItem("Cloned organisms");
		
		
    	
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
    	add(fitnessChart, gc);
    	add(errorChart, gc);
    	add(forzaChart, gc);
    	add(clonedChart, gc);
//    	setLayout(new GridBagLayout());
//    	
//    	GridBagConstraints gc = new GridBagConstraints();
//    	
////    	OptionsPanel options = new OptionsPanel();
//    	
//    	leftPanel = new SimulationLeftPanel(frame);
//    	leftPanel.getOptionsPanel().getStartBtn().addActionListener(this);
//    	leftPanel.getOptionsPanel().getAutodrawBtn().addActionListener(this);
//    	
//    	rightPanel = new ThrowPanel(frame);
//    	
////    	JTextArea info = new JTextArea();
//
////		gc.weighty = 10;
////		gc.weightx = 10;
////		gc.anchor = GridBagConstraints.FIRST_LINE_START;
////		gc.fill = GridBagConstraints.BOTH;
//    	
////		gc.weightx = 0.5;
////		gc.weighty = 0.5;
//		gc.anchor = GridBagConstraints.FIRST_LINE_START;
//		gc.fill = GridBagConstraints.VERTICAL;
//		
//		gc.gridx = 0;
//		gc.gridy = 0;
//    	add(leftPanel, gc);
//    	
//		gc.weightx = 0.5;
//		gc.weighty = 0.5;
//    	gc.fill = GridBagConstraints.BOTH;
//    	
//		gc.gridx = 1;
//		gc.gridy = 0;
//    	add(rightPanel, gc);
    	
	}
	
	
	public Chart getFitnessChart() 
	{
		return fitnessChart;
	}
	
	public Chart getErrorChart() 
	{
		return errorChart;
	}
	
	public Chart getClonedChart()
	{
		return clonedChart;
	}

	public void updateGraphPanel(Organism o, Population pop)
	{
		double highest_fitness = pop.getHighest_fitness();
		double mean_fitness = pop.getMean_fitness();
		double mean_cloned_fitness = pop.getMean_cloned_fitness();
		double lowest_error = pop.getLowest_error();
		double mean_error = pop.getMean_error();
		int generation = pop.getFinal_gen();
		int cloned = pop.getCloned();
		
//		System.out.println("Clonati: " + cloned);
		
//		System.out.println(mean_cloned_fitness);
//		System.out.println(pop.getCloned());

		fitnessChart.addVector(0, new Vector2d(generation, mean_fitness));
		fitnessChart.addVector(1, new Vector2d(generation, mean_cloned_fitness));
		fitnessChart.addVector(2, new Vector2d(generation, highest_fitness));
		
//		errorChart.addVector(0, new Vector2d(generation, mean_error));	//TROPPO ALTO
		errorChart.addVector(0, new Vector2d(generation, lowest_error));
		
		if (lowest_error > errorChart.getMaxY())
		{
			String val = ""+lowest_error;
			int first = Integer.parseInt(""+val.charAt(0));
			first++;
			String newVal = ""+first;
			String interi = val.substring(0, val.indexOf('.'));
			for (int i = 1; i<interi.length(); i++)
			{
				newVal+= 0;
			}
			int newMaxError = Integer.parseInt(newVal);
			errorChart.setMaxY(newMaxError);
		}
		
		clonedChart.addVector(0, new Vector2d(generation, cloned));
		
		o.setFitnessLinesChart(fitnessChart.getLines());
		o.setErrorLinesChart(errorChart.getLines());
		//TODO Mettere la riga qui sotto
		o.setClonedLinesChart(clonedChart.getLines());	// DA METTERE
	}

	public void updateForzaChart(Organism o, int currSelectedThrow)
	{
		if (forzaChart.getLines().size() > 0) forzaChart.reset();
		forzaChart.addVector(0, new Vector2d(0, 0.0));
		for (int i=0; i<o.getForzaMap().get(currSelectedThrow).size(); i++)
			forzaChart.addVector(0, new Vector2d(i+1, o.getForzaMap().get(currSelectedThrow).get(i)));
		
//		o.setForzaLinesChart(forzaChart.getLines());
	}
	
	public void updateLoadedOrganismChart(Organism o)
	{
		fitnessChart.setLines(o.getFitnessLinesChart());
		errorChart.setLines(o.getErrorLinesChart());
		
		double maxError = errorChart.getLines().get(0).get(0).y;
		
		for (int i = 0; i<errorChart.getLines().get(0).size(); i++)
		{
			if (errorChart.getLines().get(0).get(i).y > maxError)
			{
				maxError = errorChart.getLines().get(0).get(i).y;
			}
		}
		
		if (maxError> errorChart.getMaxY())
		{
			String val = ""+maxError;
			int first = Integer.parseInt(""+val.charAt(0));
			first++;
			String newVal = ""+first;
			String interi = val.substring(0, val.indexOf('.'));
			for (int i = 1; i<interi.length(); i++)
			{
				newVal+= 0;
			}
			int newMaxError = Integer.parseInt(newVal);
			errorChart.setMaxY(newMaxError);
		}
		
//		forzaChart.setLines(o.getForzaLinesChart());
		//TODO Mettere la riga qui sotto
		clonedChart.setLines(o.getClonedLinesChart());	// DA METTERE
	}
	
	public Chart getForzaChart() {
		return forzaChart;
	}

	public GraphLeftPanel getLeftPanel() 
	{
		return leftPanel;
	}


	@Override
	public void actionPerformed(ActionEvent e) 
	{
		 JButton p = (JButton) e.getSource();
		 
//		 if (p.getActionCommand().equals("Grid: OFF")) 
//		 {
//			 fitnessChart.setGrid(false);
//			 errorChart.setGrid(false);
//			 forzaChart.setGrid(false);
//			 clonedChart.setGrid(false);
//			 
//			 leftPanel.getOptionsPanel().getGridButton().setText("Grid: ON");
//			 repaint();
//		 } 
//		 
//		 else if (p.getActionCommand().equals("Grid: ON")) 
//		 {
//			 fitnessChart.setGrid(true);
//			 errorChart.setGrid(true);
//			 forzaChart.setGrid(true);
//			 clonedChart.setGrid(true);
//			 
//			 leftPanel.getOptionsPanel().getGridButton().setText("Grid: OFF");
//			 repaint();
//		 } 
		 
//		 else if (p.getActionCommand().equals("Auto-draw: OFF"))
//		 {
//			 autodraw = false;
//			 
//			 getLeftPanel().getForzaOptionsPanel().getAutodrawBtn().setText("Auto-draw: ON");
//		 }
//		 
//		 else if (p.getActionCommand().equals("Auto-draw: ON"))
//		 {
//			 autodraw = true;
//			 
//			 getLeftPanel().getForzaOptionsPanel().getAutodrawBtn().setText("Auto-draw: OFF");
//		 }
	}
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		 if (leftPanel.getOptionsPanel().getChartList().getSelectedItem().equals("Error") && !error)
		 {
			 if (leftPanel.getForzaPanel())
			 {
				 leftPanel.setNormalPanel();
			 }
			 
			 leftPanel.getLegendPanel().setLegend(errorChart.getNames(), errorChart.getColors());
			 gc.fill = GridBagConstraints.BOTH;
			 gc.weightx = 0.5;
			 gc.weighty = 0.5;
			 gc.gridx = 1;
			 gc.gridy = 0;
			 remove(errorChart);
			 remove(fitnessChart);
			 remove(forzaChart);
			 remove(clonedChart);
	    	 add(errorChart, gc);
//	    	 add(fitnessChart, gc);
//	       	 add(forzaChart, gc);
			 error = true;
			 fitness = false;
			 forza = false;
			 cloned = false;
		 } 
		 else if (leftPanel.getOptionsPanel().getChartList().getSelectedItem().equals("Fitness") && !fitness)
		 {
			 if (leftPanel.getForzaPanel())
			 {
				 leftPanel.setNormalPanel();
			 }
			 
			 leftPanel.getLegendPanel().setLegend(fitnessChart.getNames(), fitnessChart.getColors());
			 gc.fill = GridBagConstraints.BOTH;
			 gc.weightx = 0.5;
			 gc.weighty = 0.5;
			 gc.gridx = 1;
			 gc.gridy = 0;
			 remove(errorChart);
			 remove(fitnessChart);
			 remove(forzaChart);
			 remove(clonedChart);
			 add(fitnessChart, gc);
//		     add(errorChart, gc);
//		     add(forzaChart, gc);
			 fitness = true;
			 error = false;
			 forza = false;
			 cloned = false;
		 }
		 
		 else if (leftPanel.getOptionsPanel().getChartList().getSelectedItem().equals("Force") && !forza)
		 {
			 leftPanel.getLegendPanel().setLegend(forzaChart.getNames(), forzaChart.getColors());
			 gc.fill = GridBagConstraints.BOTH;
			 
			 leftPanel.setForzaPanel();
			 
			 gc.weightx = 0.5;
			 gc.weighty = 0.5;
			 gc.gridx = 1;
			 gc.gridy = 0;
			 remove(errorChart);
			 remove(fitnessChart);
			 remove(clonedChart);
			 remove(forzaChart);
	    	 add(forzaChart, gc);
//	    	 add(errorChart, gc);
//	    	 add(fitnessChart, gc);
			 fitness = false;
			 error = false;
			 cloned = false;
			 forza = true;
		 }
		 
		 else if (leftPanel.getOptionsPanel().getChartList().getSelectedItem().equals("Cloned organisms") && !cloned)
		 {
			 if (leftPanel.getForzaPanel())
			 {
				 leftPanel.setNormalPanel();
			 }
			 
			 leftPanel.getLegendPanel().setLegend(clonedChart.getNames(), clonedChart.getColors());
			 gc.fill = GridBagConstraints.BOTH;
			 gc.weightx = 0.5;
			 gc.weighty = 0.5;
			 gc.gridx = 1;
			 gc.gridy = 0;
			 remove(errorChart);
			 remove(fitnessChart);
			 remove(forzaChart);
			 remove(clonedChart);
			 add(clonedChart, gc);
//		     add(errorChart, gc);
//		     add(forzaChart, gc);
			 fitness = false;
			 error = false;
			 forza = false;
			 cloned = true;
		 }
	}

	public void updateForzaOptionsPanel(Organism o) 
	{
		leftPanel.getForzaOptionsPanel().getGenerationList().addItem(o.getGeneration());
		if (MyConstants.SETTINGS_VALUES[MyConstants.GRAPHS_AUTO_DRAW_INDEX]) 
			leftPanel.getForzaOptionsPanel().getGenerationList().setSelectedItem(o.getGeneration());
	}
	
}
