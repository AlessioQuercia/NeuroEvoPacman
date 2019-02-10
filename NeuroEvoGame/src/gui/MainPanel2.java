package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.text.Document;

import org.joml.Vector2d;

import common.Direction;
import common.MyConstants;
import experiment.OrganismRunnablePacMan;
import experiment.OrganismRunnablePacMan2;
import experiment.evo_fit;
import experiment.evo_in;
import experiment.evo_out;
import jGraph.chartXY;
import jNeatCommon.EnvConstant;
import jNeatCommon.EnvRoutine;
import jNeatCommon.IOseq;
import jNeatCommon.NeatConstant;
import jneat.Genome;
import jneat.NNode;
import jneat.Neat;
import jneat.Network;
import jneat.Organism;
import jneat.Population;
import jneat.Species;
import log.HistoryLog;
import newGui.actor.Ghost;
import newGui.actor.Pacman;
import newGui.actor.PowerBall;
import newGui.actor.Ghost.Mode;
import newGui.infra.Actor;
import pacmanGui.PacmanGame;
import pacmanGui.PacmanGame.State;

public class MainPanel2 extends JPanel implements Runnable
{
	private JFrame frame;
	
	private Thread mainThread;
	private boolean isRunning;
	
	private EvolutionPanel evolution;
	private Graphs graphs;
	private Net net;
	private SettingsPanel settings;
	private JTabbedPane tabbedPanel;
	
	
// dynamic definition for fitness
	  Class  Class_fit;
	  Object ObjClass_fit;
	  Method Method_fit;
	  Object ObjRet_fit;


// dynamic definition for input class
	  Class  Class_inp;
	  Object ObjClass_inp;
	  Method Method_inp;
	  Object ObjRet_inp;

// dynamic definition for target class
	  Class  Class_tgt;
	  Object ObjClass_tgt;
	  Method Method_tgt;
	  Object ObjRet_tgt;

private   volatile Thread  lookupThread;

private boolean debug;

private chartXY mappa;

static ArrayList<Organism> winners;

private ExecutorService fixedPool;

private boolean done;

	  
	public MainPanel2(JFrame f) 
	{
		frame = f;	       
		
		debug = false;
		
		done = false;
		
		winners = new ArrayList<Organism> ();
		
		init();
		
		start();	// LANCIA IL THREAD CHE GESITSCE LA GRAFICA
	}

	public EvolutionPanel getSimulationPanel()
	{
		return evolution;
	}
	
	public Graphs getGraphsPanel()
	{
		return graphs;
	}

	private void init() 
	{
        // Set layout manager
        
        setLayout(new BorderLayout());
        
        // Create Swing component
        
        evolution = new EvolutionPanel(frame, this);
        
        graphs = new Graphs(frame);
        
        net = new Net(frame);
        
        settings = new SettingsPanel(frame);
        
        mappa = new chartXY();
        
		tabbedPanel = new JTabbedPane();
		tabbedPanel.addTab("Evolution", evolution);
		tabbedPanel.addTab("Graphs", graphs);
		tabbedPanel.addTab("Net", net);
		tabbedPanel.addTab("Settings", settings);
		tabbedPanel.setSelectedIndex(0);
		
        // Add Swing components to content pane
        
		Container contentPane = frame.getContentPane(); 
		contentPane.setLayout(new BorderLayout());

		tabbedPanel.setMinimumSize(new Dimension(400, 50));

		contentPane.add(tabbedPanel,BorderLayout.CENTER);
        
		// Add behaviour
		//TODO
		try
		{
			// data input
			   Class_inp = evo_in.class; //Class.forName(EnvConstant.DATA_INP);
			   ObjClass_inp = Class_inp.newInstance();
			   Method_inp = Class_inp.getMethod("getNumUnit", null);
			   ObjRet_inp = Method_inp.invoke(ObjClass_inp, null);
			   EnvConstant.NR_UNIT_INPUT = Integer.parseInt(ObjRet_inp.toString());
			
			// number of samples
			   Method_inp = Class_inp.getMethod("getNumSamples", null);
			   ObjRet_inp = Method_inp.invoke(ObjClass_inp, null);
			   EnvConstant.NUMBER_OF_SAMPLES = Integer.parseInt(ObjRet_inp.toString());
			   
			// data output
			   Class_tgt = evo_out.class; //Class.forName(EnvConstant.DATA_OUT);
			   ObjClass_tgt = Class_tgt.newInstance();
			   Method_tgt = Class_tgt.getMethod("getNumUnit", null);
			   ObjRet_tgt = Method_tgt.invoke(ObjClass_tgt, null);
			   EnvConstant.NR_UNIT_OUTPUT = Integer.parseInt(ObjRet_tgt.toString());
		} 
		catch (Exception e) 
		
		{
			e.printStackTrace();
		}
	}
	
	private void start()
	{
		isRunning = true;
		if(mainThread == null)
		{
			mainThread = new Thread(this, "mainThread");
			mainThread.setPriority(Thread.MAX_PRIORITY);
			mainThread.start();
		}
	}

	@Override
	public void run() 
	{	
		JComboBox[] boxess = settings.getOtherSettings().getUpperPanel().getComboBoxes();
		
		int[] selectedOptions = new int[boxess.length];
		
		for (int i = 0; i<boxess.length; i++)
		{
			selectedOptions[i] = boxess[i].getSelectedIndex();
		}
		
		int prevSelectedSettingsIndex = -1;
		
		int prevSelectedGraph = -1;
		int currSelectedGraph = 0;
		
		int prevSelectedSettings = -1;
		int currSelectedSettings = 0;
		
		int prevNetGen = -1;
		int currNetGen = 0;
		
		int prevForzaGen = -1;
		int currForzaGen = 0;
		
		int prevSelectedThrow = -1;
		int currSelectedThrow = graphs.getLeftPanel().getForzaOptionsPanel().getThrowList().getSelectedIndex();
		
		double prevSelectedOrgIndex = -1;
		
		boolean change_inputs = false;
		boolean updated = false;
		
		double currentHeight = MainFrame.HEIGHT;
		double currentWidth = MainFrame.WIDTH;
		
        long desiredFrameRateTime = 1000 / 60;
        long currentTime = System.currentTimeMillis();
        long lastTime = currentTime - desiredFrameRateTime;
        long unprocessedTime = 0;
        boolean needsRender = false;
        
        double scaleX = 2;
        double scaleY = 2;
        
        int timestep = 0;
        Map<Integer, Vector2d> pacmanPositions = null;
        Map<Integer, Vector2d> pacmanCoordinates = null;
        ArrayList<HashMap<Integer, Vector2d>> ghostsPositions = null;
        
        Map<Integer, Integer> pacmanDirections = null;
        Map<Integer, Direction> pacmanDesiredDirections = null;
        ArrayList<HashMap<Integer, Integer>> ghostsDirections = null;
        ArrayList<HashMap<Integer, Integer>> ghostsDesiredDirections = null;
        
        boolean gameStarted = false;
        int startStepsAsVulnerable = 0;
        
        long vulnerableStartTime = 0;
        
        boolean startVulnerableMode = false;
        int stepsAsVulnerable = 0;
        int currentPowerPills = 4;
        
        

        
		while (isRunning)
		{	
			long startTime = System.currentTimeMillis();
		
			if (evolution.getStart() && winners.size() > 0 && evolution.getLeftPanel().getOptionsPanel().getGenerationList().getSelectedItem() != null)
			{
				// REPRODUCING PACMAN SIMULATION
//				evolution.getRightPanel().requestFocus();
				
				scaleX = evolution.getRightPanel().getWidth()/evolution.getRightPanel().getGame().screenSize.getWidth();
				scaleY = evolution.getRightPanel().getHeight()/evolution.getRightPanel().getGame().screenSize.getHeight();
				
				currentTime = System.currentTimeMillis();
	            unprocessedTime += currentTime - lastTime;
	            lastTime = currentTime;
	            
				int gen = evolution.getLeftPanel().getOptionsPanel().getGenerationList().getSelectedIndex();
				String lancio = evolution.getLeftPanel().getOptionsPanel().getThrowList().getSelectedItem().toString();

				Organism selectedOrg = winners.get(gen);
				
				int selectedOrgIndex = winners.indexOf(selectedOrg);
				
//				System.out.println(selectedOrg.getOrig_fitness());
				
				if (lancio.equals("Best"))
					lancio = ""+ (selectedOrg.getMap().get(EnvConstant.NUMBER_OF_SAMPLES).get(MyConstants.LANCIO_MIGLIORE_INDEX).intValue()+1);
				
				int selectedThrow = Integer.parseInt(lancio)-1;
				ArrayList<Double> infoLancio = selectedOrg.getMap().get(selectedThrow);
				
				if (prevSelectedThrow != selectedThrow || prevSelectedOrgIndex != selectedOrgIndex || change_inputs)
				{
					change_inputs = false;
					
					updated = false;
					
					prevSelectedOrgIndex = selectedOrgIndex;
					prevSelectedThrow = selectedThrow;
					
					timestep = 0;
                	startStepsAsVulnerable = 0;
                	currentPowerPills = 4;
					evolution.getRightPanel().restartGame();
					gameStarted = true;
					
					evolution.getLeftPanel().updateInfoRete(selectedOrg);
					evolution.getLeftPanel().updateInfoLancio(selectedOrg, selectedThrow, timestep);
                	
					evolution.repaint();
				}
				
				if (evolution.getRightPanel().getGame().state == State.PLAYING)
					gameStarted = false;
					
				if (evolution.getRightPanel().getGame().state == State.GAME_OVER && !gameStarted)
				{
					timestep = 0;
					evolution.getRightPanel().restartGame();
					gameStarted = true;
                	startStepsAsVulnerable = 0;
                	vulnerableStartTime = 0;
                	currentPowerPills = 4;
				}
				
		        double start = System.currentTimeMillis();
				
//	            while (unprocessedTime >= desiredFrameRateTime) 
//	            {
//	                unprocessedTime -= desiredFrameRateTime;
	                
	                // REPRODUCE THE POSITIONS CHOSEN BY THE SIMULATION MADE WITH NEURAL NETWORKS AND THE GENETIC ALGORITHM (NEAT)
	                // (reproduce the whole simulation??)
	                // (make methods to reproduce the game just by giving the position used in the simulation??)
		        
		        	pacmanCoordinates = selectedOrg.getPacmanCoordinates();
	                
	                pacmanPositions = selectedOrg.getPacmanPositions();
	                
	                ghostsPositions = selectedOrg.getGhostsPositions();
	                
	                pacmanDirections = selectedOrg.getPacmanDirections();
	                
	                pacmanDesiredDirections = selectedOrg.getPacmanDesiredDirections();
	                
	                ghostsDirections = selectedOrg.getGhostsDirections();
	                
	                ghostsDesiredDirections = selectedOrg.getGhostsDesiredDirections();
	                
	                if (timestep >= selectedOrg.getGhostsPositions().get(0).size())
	                {
	                	timestep = 0;
//	                	while (evolution.getRightPanel().getGame().state != State.GAME_OVER)
//	                	{
//	                		evolution.getRightPanel().getGame().update();
//	                	}
						evolution.getRightPanel().restartGame();
						gameStarted = true;
	                	startStepsAsVulnerable = 0;
	                	vulnerableStartTime = 0;
	                	currentPowerPills = 4;
	                }
	                
	                if (evolution.getRightPanel().getGame().state == State.PLAYING || evolution.getRightPanel().getGame().state == State.GHOST_CATCHED)
	                {                	            	
	                	// REPRODUCE THE SIMULATED GAME
		                
	                	evolution.getRightPanel().reproduceSimulatedGame(timestep, pacmanDirections, pacmanDesiredDirections, pacmanPositions, pacmanCoordinates,
	                			ghostsDirections, ghostsDesiredDirections, ghostsPositions);
		                
	                	
	                	// CHECK IF GHOSTS ARE VULNERABLE
	                	
					   if (evolution.getRightPanel().getGame().powerUpList.size() < currentPowerPills)
					   {
						   startVulnerableMode = true;
						   stepsAsVulnerable = 0;
						   currentPowerPills = evolution.getRightPanel().getGame().powerUpList.size();
						   for (Ghost g : evolution.getRightPanel().getGame().getGhosts())
							   g.setMode(Mode.VULNERABLE);
					   }
					   
					   if (startVulnerableMode)
					   {
						   stepsAsVulnerable++;
					   }
					   
					   if (stepsAsVulnerable > 480)
					   {
						   for (Ghost g : evolution.getRightPanel().getGame().getGhosts())
							   g.setMode(Mode.NORMAL);
						  
						   stepsAsVulnerable = 0;
						   startVulnerableMode = false;
						   evolution.getRightPanel().getGame().getPacMan().canEatGhosts = false;
					   }
	                	
//		                if (vulnerableStartTime == 0)
////	                	if (startStepsAsVulnerable == 0)
//		                {
//			                for (Ghost g : evolution.getRightPanel().getGame().getGhosts())
//				                if (g.mode == Mode.VULNERABLE) //&& vulnerableStartTime == 0/*&& startStepsAsVulnerable == 0*/)
//				                {
//				                	startStepsAsVulnerable = timestep;
//				                	vulnerableStartTime = System.currentTimeMillis();
//				                	break;
//				                }
//		                }
//		                
//		                if (System.currentTimeMillis() - vulnerableStartTime > 8500 /*&& timestep == startStepsAsVulnerable + 480*/)
////		                if (timestep - startStepsAsVulnerable == 480)
//		                {
//		                	for (Ghost g : evolution.getRightPanel().getGame().getGhosts())
//		                		g.mode = Mode.NORMAL;
//		                	
//		                	startStepsAsVulnerable = 0;
//		                	vulnerableStartTime = 0;
//		                	evolution.getRightPanel().getGame().getPacMan().canEatGhosts = false;
//		                }
		                
						evolution.getLeftPanel().updateInfoRete(selectedOrg);
						evolution.getLeftPanel().updateInfoLancio(selectedOrg, selectedThrow, timestep);
						
						
						// UPDATE TIMESTEP
		                
		                timestep++;
	                }
	                else
	                {
		                evolution.getRightPanel().getGame().update();
	                }
	                
	                needsRender = true;
	                Graphics2D g = (Graphics2D) evolution.getRightPanel().getGraphics2D();
	    			evolution.getRightPanel().getGame().setScreenScale(new Point2D.Double(scaleX, scaleY));
	                evolution.getRightPanel().repaint();
//	            }
	            
//	            if (needsRender) {
//	                Graphics2D g = (Graphics2D) evolution.getRightPanel().getGraphics2D();
//	    			evolution.getRightPanel().getGame().setScreenScale(new Point2D.Double(scaleX, scaleY));
//	                evolution.getRightPanel().repaint();
//	                needsRender = false;
//	            }
//	            else {
//	                try {
//	                    Thread.sleep(1);
//	                } catch (InterruptedException ex) {
//	                }
//	            }
			}
			if (MyConstants.LOADED_INPUTS)
			{				
				Organism organism = winners.get(
						evolution.getLeftPanel().getOptionsPanel().
						getGenerationList().getSelectedIndex());
				
				String lancio = evolution.getLeftPanel().getOptionsPanel().getThrowList().getSelectedItem().toString();
				if (lancio.equals("Best"))
					lancio = ""+(organism.getMap().get(EnvConstant.NUMBER_OF_SAMPLES).get(MyConstants.LANCIO_MIGLIORE_INDEX).intValue()+1);
				
				int selectedThrow = Integer.parseInt(lancio)-1;
				
				fixedPool = Executors.newFixedThreadPool(1);	//// VERSIONE PARALLELA
				EnvConstant.TYPE_OF_SIMULATION = EnvConstant.SIMULATION_FROM_CLASS;
//				fixedPool.submit(new OrganismRunnableMovementLoaded(organism, MyConstants.LOADED_X, MyConstants.LOADED_Y, selectedThrow));
				fixedPool.shutdown();							//// VERSIONE PARALLELA
				
				try {
					fixedPool.awaitTermination(1, TimeUnit.DAYS);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	//// VERSIONE PARALLELA
				
				evolution.getLeftPanel().updateInfoRete(organism.getMap().get(EnvConstant.NUMBER_OF_SAMPLES));
				evolution.getLeftPanel().updateInfoLancio(organism.getMap().get(selectedThrow));
				
				MyConstants.LOADED_INPUTS = false;
				
				change_inputs = true;
			}
			
			if (evolution.getLoad() && !done)
			{
				evolution.getLeftPanel().getOptionsPanel().getThrowList().removeAllItems();
				graphs.getLeftPanel().getForzaOptionsPanel().getThrowList().removeAllItems();
				
				for (int i=1; i<=EnvConstant.NUMBER_OF_SAMPLES; i++)
					evolution.getLeftPanel().getOptionsPanel().getThrowList().addItem(i);
				evolution.getLeftPanel().getOptionsPanel().getThrowList().addItem("Best");
				evolution.getLeftPanel().getOptionsPanel().getThrowList().setSelectedItem("Best");
				
				for (int i=1; i<=EnvConstant.NUMBER_OF_SAMPLES; i++)
					graphs.getLeftPanel().getForzaOptionsPanel().getThrowList().addItem(i);
				graphs.getLeftPanel().getForzaOptionsPanel().getThrowList().addItem("Best");
				graphs.getLeftPanel().getForzaOptionsPanel().getThrowList().setSelectedItem("Best");
				
				evolution.getLeftPanel().setLoadLayout();
				
				done = true;
			}
			
			if (evolution.isLoading())
			{
//				evolution.getRightPanel().setDraw(true);
				graphs.updateLoadedOrganismChart(winners.get(winners.size()-1));
				net.updateNetPanel(winners.get(winners.size()-1));
				
				graphs.getLeftPanel().getForzaOptionsPanel().getGenerationList().addItem(winners.get(winners.size()-1).getGeneration());
				graphs.getLeftPanel().getForzaOptionsPanel().getGenerationList().setSelectedItem(winners.get(winners.size()-1).getGeneration());
				
				int size = graphs.getFitnessChart().getLines().get(0).size();
				
				double lastGen = graphs.getFitnessChart().getLines().get(0).get(size-1).x;
				
				String val = ""+lastGen;
				int first = Integer.parseInt(""+val.charAt(0));
				first++;
				String newVal = ""+first;
				String interi = val.substring(0, val.indexOf('.'));
				for (int i = 1; i<interi.length(); i++)
				{
					newVal+= 0;
				}
				int gen = Integer.parseInt(newVal);
				
				graphs.getFitnessChart().setMaxX(gen);
				graphs.getErrorChart().setMaxX(gen);
				graphs.getClonedChart().setMaxX(gen);
				
				double loaded_fitness = winners.get(winners.size()-1).getMap().get(MyConstants.NUMBER_OF_SAMPLES).get(MyConstants.FITNESS_TOTALE_INDEX);
				double max_fit = MyConstants.MAX_FITNESS;
				
				if (loaded_fitness > MyConstants.MAX_FITNESS)
					max_fit = 16000000;
				
				graphs.getFitnessChart().setMaxY(max_fit);
				
				updated = false;
				
				evolution.setLoading(false);
			}
			
			if ((evolution.getStart() || evolution.getLoad()) && evolution.getLeftPanel().getOptionsPanel().getGenerationList().getSelectedItem() != null)
			{				
				int gen = evolution.getLeftPanel().getOptionsPanel().getGenerationList().getSelectedIndex();
				String lancio = evolution.getLeftPanel().getOptionsPanel().getThrowList().getSelectedItem().toString();

				Organism selectedOrg = winners.get(gen);
				
				//AGGIORNA GRAFICO FITNESS, PER QUANDO SI CARICANO ORGANISMI CON FITNESS DIVERSA
				if (!updated && evolution.getLoad())
				{
					double lastGen = selectedOrg.getGeneration();
					
					String val = ""+lastGen;
					int first = Integer.parseInt(""+val.charAt(0));
					first++;
					String newVal = ""+first;
					String interi = val.substring(0, val.indexOf('.'));
					for (int i = 1; i<interi.length(); i++)
					{
						newVal+= 0;
					}
					int gener = Integer.parseInt(newVal);
					
					graphs.getFitnessChart().setMaxX(gener);
					graphs.getErrorChart().setMaxX(gener);
					graphs.getClonedChart().setMaxX(gener);
					
					
					double loaded_fitness = selectedOrg.getMap().get(MyConstants.NUMBER_OF_SAMPLES).get(MyConstants.FITNESS_TOTALE_INDEX);
					double max_fit = MyConstants.MAX_FITNESS;
					if (loaded_fitness > MyConstants.MAX_FITNESS)
						max_fit = 16000000;
					
					graphs.getFitnessChart().setMaxY(max_fit);
					
					updated = true;
				}
				
				int selectedOrgIndex = winners.indexOf(selectedOrg);
				
				if (lancio.equals("Best"))
					lancio = ""+ (selectedOrg.getMap().get(EnvConstant.NUMBER_OF_SAMPLES).get(MyConstants.LANCIO_MIGLIORE_INDEX).intValue()+1);
				
				int selectedThrow = Integer.parseInt(lancio)-1;
				ArrayList<Double> infoLancio = selectedOrg.getMap().get(selectedThrow);
				
				if (evolution.getLoad())
				{
					graphs.updateLoadedOrganismChart(winners.get(evolution.getLeftPanel().getOptionsPanel().getGenerationList().getSelectedIndex()));
				}
				
				if (prevSelectedThrow != selectedThrow || prevSelectedOrgIndex != selectedOrgIndex || change_inputs)
				{
					change_inputs = false;
					
					updated = false;
					
					prevSelectedOrgIndex = selectedOrgIndex;
					prevSelectedThrow = selectedThrow;
					

					
					evolution.getLeftPanel().updateInfoRete(selectedOrg.getMap().get(EnvConstant.NUMBER_OF_SAMPLES));
					evolution.getLeftPanel().updateInfoLancio(infoLancio);
					
					

					evolution.repaint();
				}
				
				evolution.getRightPanel().repaint();
			}
			
			if (tabbedPanel.getSelectedIndex() == 1)
			{	
				
				currSelectedGraph = graphs.getLeftPanel().getOptionsPanel().getChartList().getSelectedIndex();
				if (prevSelectedGraph != currSelectedGraph)
				{
					graphs.getFitnessChart().revalidate();
					graphs.getFitnessChart().repaint();
					graphs.getErrorChart().revalidate();
					graphs.getErrorChart().repaint();
					graphs.getForzaChart().revalidate();
					graphs.getForzaChart().repaint();
					graphs.getClonedChart().revalidate();
					graphs.getClonedChart().repaint();
				}
				
				if ((evolution.getStart() || evolution.getLoad()) && winners.size()>0
						&& graphs.getLeftPanel().getForzaOptionsPanel().getGenerationList().getItemCount()>0)
				{
					currForzaGen = graphs.getLeftPanel().getForzaOptionsPanel().getGenerationList().getSelectedIndex();
					Organism selectedOrg = winners.get(currForzaGen);
					currSelectedThrow = graphs.getLeftPanel().getForzaOptionsPanel().getThrowList().getSelectedIndex();
					if (currSelectedThrow == EnvConstant.NUMBER_OF_SAMPLES)
						currSelectedThrow = selectedOrg.getMap().get(EnvConstant.NUMBER_OF_SAMPLES).get(MyConstants.LANCIO_MIGLIORE_INDEX).intValue();
					if (graphs.getLeftPanel().getForzaPanel())
						graphs.updateForzaChart(selectedOrg, currSelectedThrow);
					
					if (prevForzaGen != currForzaGen)
					{
						prevForzaGen = currForzaGen;
						
	//					graphs.repaint();
					}
					
					if (prevSelectedThrow != currSelectedThrow)
					{
						prevSelectedThrow = currSelectedThrow;
						
	//					graphs.repaint();
					}
				}
				
				graphs.repaint();
			}
			
			if (tabbedPanel.getSelectedIndex() == 2 && (evolution.getStart() || evolution.getLoad()) && winners.size() > 0
					&& net.getLeftPanel().getOptionsPanel().getGenerationList().getItemCount()>0)
			{
				currNetGen = net.getLeftPanel().getOptionsPanel().getGenerationList().getSelectedIndex();
				Organism selectedOrg = winners.get(currNetGen);
				net.drawGraph(selectedOrg, mappa);
				net.getLeftPanel().updateInfoRete(selectedOrg);
				
				if (prevNetGen != currNetGen)
				{
					prevNetGen = currNetGen;
					net.repaint();
				}
			}
			
			if (tabbedPanel.getSelectedIndex() == 3)
			{
				currSelectedSettings = settings.getLeftPanel().getList().getSelectedIndex();
				if (prevSelectedSettings != currSelectedSettings)
				{
					settings.getParameterSettings().revalidate();
					settings.getParameterSettings().repaint();
					settings.getOtherSettings().revalidate();
					settings.getOtherSettings().repaint();
				}
				
				JComboBox[] boxes = settings.getOtherSettings().getUpperPanel().getComboBoxes();
				boolean selected = false;
				
				for (int i=0; i<boxes.length; i++)
				{
					if (boxes[i].getSelectedIndex() != selectedOptions[i])
					{
//						System.out.println("CAMBIATO");
						selectedOptions[i] = boxes[i].getSelectedIndex();
						settings.getOtherSettings().getUpperPanel().updateValue(i, selectedOptions[i]);
						settings.getOtherSettings().getUpperPanel().saveSettings(settings.getOtherSettings().getUpperPanel().getFilename());
					}
				}
				
				if(settings.getLeftPanel().getList().getSelectedIndex() != prevSelectedSettingsIndex)
				{
					prevSelectedSettingsIndex = settings.getLeftPanel().getList().getSelectedIndex();
					settings.repaint();
				}
				
				if (MainFrame.HEIGHT != currentHeight || MainFrame.WIDTH != currentWidth)
				{
					currentHeight = MainFrame.HEIGHT;
					currentWidth = MainFrame.WIDTH;
					
					settings.repaint();
				}
			}
			
			long endTime = System.currentTimeMillis() - startTime;
			long waitTime = (MyConstants.MILLISECOND/MyConstants.FPS) - endTime/MyConstants.MILLISECOND;

			try 
			{
				mainThread.sleep(waitTime);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				System.out.println("ERRORE NEL MAIN LOOP");
			}

		}
	}
	
	/// START DA INTERFACCIA NUOVA ///
	
	   public void startProcessAsync()
	  {
		 Runnable lookupRun = 
			 new Runnable() 
			{
				public void run() 
				{		
					
					 evolution.getLeftPanel().getOptionsPanel().getGenerationList().removeAllItems();
					 evolution.getLeftPanel().getOptionsPanel().getThrowList().removeAllItems();
					 graphs.getErrorChart().reset();
					 graphs.getFitnessChart().reset();
					 graphs.getForzaChart().reset();
					 graphs.getClonedChart().reset();
					 graphs.getFitnessChart().setMaxX(Neat.p_epoch_number);
					 graphs.getFitnessChart().setMaxY(MyConstants.MAX_FITNESS);
					 graphs.getErrorChart().setMaxX(Neat.p_epoch_number);
					 graphs.getClonedChart().setMaxX(Neat.p_epoch_number);
					 graphs.getLeftPanel().getForzaOptionsPanel().getGenerationList().removeAllItems();
					 graphs.getLeftPanel().getForzaOptionsPanel().getThrowList().removeAllItems();
					 net.getLeftPanel().getOptionsPanel().getGenerationList().removeAllItems();
					 winners.clear();
					 debug = false;
					 evolution.setLoad(false);
					 done = false;
					
					 if (EnvConstant.TYPE_OF_START == EnvConstant.START_FROM_EXISTING_POPULATION)
					 {
						 graphs.getFitnessChart().startFromFirst();
					 }
				  EnvRoutine.getSession();
			   
				  startProcess();
			   }
			};
		 lookupThread = new Thread(lookupRun," looktest" );
		 lookupThread.start();  
	  } 
	   
	   public void stopProcessAsync()
	  {
//		   lookupThread.interrupt();
		   EnvConstant.STOP_EPOCH = true;
		   
			 evolution.getLeftPanel().getOptionsPanel().getGC().anchor = GridBagConstraints.LINE_END;
			 evolution.getLeftPanel().getOptionsPanel().getGC().gridx = 1;
			 evolution.getLeftPanel().getOptionsPanel().getGC().gridy = 2;
			 evolution.getLeftPanel().getOptionsPanel().add(evolution.getLeftPanel().getOptionsPanel().getLoadBtn(), evolution.getLeftPanel().getOptionsPanel().getGC());
			 
			 evolution.getLeftPanel().getOptionsPanel().getGC().anchor = GridBagConstraints.LINE_START;
			 evolution.getLeftPanel().getOptionsPanel().getGC().fill = GridBagConstraints.HORIZONTAL;
			 evolution.getLeftPanel().getOptionsPanel().getGC().gridx = 0;
			 evolution.getLeftPanel().getOptionsPanel().getGC().gridy = 3;
			 evolution.getLeftPanel().getOptionsPanel().add(evolution.getLeftPanel().getOptionsPanel().getStartFromBtn(), evolution.getLeftPanel().getOptionsPanel().getGC());
			 
			 evolution.getLeftPanel().getOptionsPanel().getStartBtn().setText("Start");
			 
			 evolution.getLeftPanel().getGenerationLabel().setText("");
			 graphs.getLeftPanel().getGenerationLabel().setText("");
			 net.getLeftPanel().getGenerationLabel().setText("");
	  } 
	   
	   public void startProcess() 
	  {	  
		 try 
		 {
			 boolean  rc1  = startNeat();
		 } 
		 
		 catch (Throwable e1) 
		{
		   System.out.println((" generation: error during generation.startProcess() :"+e1));
		}
	  }

	   public boolean startNeat()
	  {
		 boolean rc = false;
		 String curr_name_pop_specie = null;
	  
		 String mask5 = "00000";
		 DecimalFormat fmt5 = new DecimalFormat(mask5);
	  
		 Population u_pop = null;
		 Genome u_genome = null;
		 StringTokenizer st;
		 String curword;
		 String xline;
		 String fnamebuf;
		 IOseq xFile;
		 int id;
		 int expcount = 0;
		 int gen = 0;

	  // imposta classe dinamica per fitness 
		 try
		 {
			Class_fit = evo_fit.class; //Class.forName(EnvConstant.CLASS_FITNESS);
			ObjClass_fit = Class_fit.newInstance();
		 
		 // read max Fitness possible		
			Method_fit = Class_fit.getMethod("getMaxFitness", null);
			ObjRet_fit = Method_fit.invoke(ObjClass_fit, null);
			EnvConstant.MAX_FITNESS =  Double.parseDouble(ObjRet_fit.toString());
	 
			if ( EnvConstant.TYPE_OF_SIMULATION == EnvConstant.SIMULATION_FROM_CLASS )
			{
			
			// data input
			   Class_inp = evo_in.class; //Class.forName(EnvConstant.DATA_INP);
			   ObjClass_inp = Class_inp.newInstance();
			   Method_inp = Class_inp.getMethod("getNumUnit", null);
			   ObjRet_inp = Method_inp.invoke(ObjClass_inp, null);
			   EnvConstant.NR_UNIT_INPUT = Integer.parseInt(ObjRet_inp.toString());
			
			// number of samples
			   Method_inp = Class_inp.getMethod("getNumSamples", null);
			   ObjRet_inp = Method_inp.invoke(ObjClass_inp, null);
			   EnvConstant.NUMBER_OF_SAMPLES = Integer.parseInt(ObjRet_inp.toString());
			
			   
			   ///RIEMPE LISTA LANCI IN SIMULATION
				for (int i = 1; i<=EnvConstant.NUMBER_OF_SAMPLES; i++)
					evolution.getLeftPanel().getOptionsPanel().getThrowList().addItem(i);
				evolution.getLeftPanel().getOptionsPanel().getThrowList().addItem("Best");
				evolution.getLeftPanel().getOptionsPanel().getThrowList().setSelectedItem("Best");
				
			   ///RIEMPE LISTA LANCI NEL GRAFICO DELLA FORZA
				for (int i = 1; i<=EnvConstant.NUMBER_OF_SAMPLES; i++)
					graphs.getLeftPanel().getForzaOptionsPanel().getThrowList().addItem(i);
				graphs.getLeftPanel().getForzaOptionsPanel().getThrowList().addItem("Best");
				graphs.getLeftPanel().getForzaOptionsPanel().getThrowList().setSelectedItem("Best");
			   
			   
			   
			// data output
			   Class_tgt = evo_out.class; //Class.forName(EnvConstant.DATA_OUT);
			   ObjClass_tgt = Class_tgt.newInstance();
			   Method_tgt = Class_tgt.getMethod("getNumUnit", null);
			   ObjRet_tgt = Method_tgt.invoke(ObjClass_tgt, null);
			   EnvConstant.NR_UNIT_OUTPUT = Integer.parseInt(ObjRet_tgt.toString());
			} 
		 }
			 catch(Exception ed) 
			{
			   System.out.println((" generation: error in startNeat() "+ed));
			}
	  
		 try 
		 {
			Neat u_neat = new Neat();
			u_neat.initbase();
			rc = u_neat.readParam(MyConstants.PARAMETRI_NOMEFILE);

		 
			if (!rc)
			{
			   System.out.println(" generation: error in read "+EnvRoutine.getJneatParameter());
			   System.out.println();
			   return false;
			}   

		 // gestisce start da genoma unico
			if (!EnvConstant.FORCE_RESTART )
			{
			   xFile = new IOseq(MyConstants.GENOMA_NOMEFILE);
			   rc = xFile.IOseqOpenR();
			   if (!rc)
			   {
				  System.out.println(" generation:   error open "+MyConstants.GENOMA_NOMEFILE);
				  return false;
			   }
			   xline = xFile.IOseqRead();
			   st = new StringTokenizer(xline);
			//skip 
			   curword = st.nextToken();
			//id of genome can be readed
			   curword = st.nextToken();
			   id = Integer.parseInt(curword);
			   u_genome = new Genome(id, xFile);
			}
			EnvConstant.SERIAL_SUPER_WINNER = 0;
			EnvConstant.MAX_WINNER_FITNESS = 0;
		 
		 // 1.6.2002 : reset pointer to first champion
			EnvConstant.CURR_ORGANISM_CHAMPION = null;
			EnvConstant.FIRST_ORGANISM_WINNER = null;
		 
			EnvConstant.RUNNING = true;
			
			for (expcount = 0; expcount < u_neat.p_num_runs; expcount++) 
			{
//			   if (!EnvConstant.FORCE_RESTART )
//				  u_pop = new Population(u_genome, u_neat.p_pop_size);
			   
			   if ((EnvConstant.TYPE_OF_START  == EnvConstant.START_FROM_GENOME) &&  (!EnvConstant.FORCE_RESTART ))
				  u_pop = new Population(u_genome, u_neat.p_pop_size);
			
//			   if ((EnvConstant.TYPE_OF_START  == EnvConstant.START_FROM_NEW_RANDOM_POPULATION) && (!EnvConstant.FORCE_RESTART ))
//				  u_pop = new Population(u_neat.p_pop_size, (u_inp_unit+1), u_out_unit, u_max_unit, u_recurrent, u_prb_link);
			
			   if (( EnvConstant.TYPE_OF_START  == EnvConstant.START_FROM_EXISTING_POPULATION))
				  u_pop = new Population(MyConstants.POPULATIONS_DIR + MyConstants.POPULATION_FILENAME);   		
			
//			   u_pop.verify();
			   
			   EnvConstant.NUMBER_OF_EPOCH = u_neat.p_epoch_number;
			   
			// start ............
			   for (gen = 1; gen <= EnvConstant.NUMBER_OF_EPOCH; gen++) 
			   {
				  curr_name_pop_specie =  EnvConstant.PREFIX_SPECIES_FILE;
				  EnvConstant.SUPER_WINNER_ = false;
				  boolean esito = epoch(u_neat, u_pop, gen, curr_name_pop_specie);
				  evolution.getLeftPanel().getGenerationLabel().setText("Running generation -> " + gen);
				  graphs.getLeftPanel().getGenerationLabel().setText("Running generation -> " + gen);
				  net.getLeftPanel().getGenerationLabel().setText("Running generation -> " + gen);
//				  System.out.println(" running generation ->"+gen);
				  if (EnvConstant.STOP_EPOCH)
					 break;
			   }
			   
			   // Una volta finite le epoche (tutte le generazioni) chiama lo stop per poter effettuare un'altra simulazione
			   stopProcessAsync();
			   
			   if (EnvConstant.STOP_EPOCH)
				  break;
			}

//		 // before exit save last population
//			u_pop.print_to_file_by_species(EnvRoutine.getJneatFileData(EnvConstant.NAME_CURR_POPULATION));
//			String filename = "Population_" + u_pop.getFinal_gen();
//			u_pop.print_to_file_by_species(MyConstants.POPULATIONS_DIR + filename);
			
			// salva il migliore organismo dell'ultima popolazione
//			Organism bestPopOrg = u_pop.getCurrentPop_bestOrganism();
//			String nomefile = MyConstants.POPULATIONS_DIR + "infoLastNet_" + bestPopOrg.getGeneration();
//			boolean prova = simulation.serializeOnFile(nomefile, bestPopOrg);
			
		 
		 }
		 
			 catch (Throwable e1) 
			{
			   System.out.println(" error in generation.startNeat() "+e1);
			}

			EnvConstant.RUNNING = false;
			
		 return true;
	  }
	   
	public boolean epoch
	   (
	   Neat _neat, 
	   Population pop, 
	   int generation, 
	   String filename) {
		 String winner_prefix = EnvConstant.PREFIX_WINNER_FILE;
		 String riga1 = null;
		 boolean esito = false;
		 boolean win = false;
		 Genome _genome_win = null;
	  
//		 Document doc2 = textPane2.getDocument();
//		 String ckx = ck_group.getSelection().getActionCommand();
	  

//		 if (generation == 1) 
//		 {
//			v1_fitness_win = new Vector(1, 0);
//			v1_fitness = new Vector(1, 0);
//			v1_species = new Vector(1, 0);
//		 }
	  
	  
		 try 
		 {
		 // Evaluate each organism if exist the winner.........
		 // flag and store only the first winner
			Iterator itr_organism;
			itr_organism = pop.organisms.iterator();
			double max_fitness_of_winner = 0.0;
		 
			
			
			//TODO DA PARALLELIZZARE
			
			int nThreads = Runtime.getRuntime().availableProcessors();			//// VERSIONE PARALLELA
//			System.out.println(nThreads);
			
			fixedPool = Executors.newFixedThreadPool(nThreads);	//// VERSIONE PARALLELA
			
			while (itr_organism.hasNext()) 
			{
			//point to organism
			   Organism organism = ((Organism) itr_organism.next());
			   
			   PacmanGame pacmanGame = new PacmanGame();
			   pacmanGame.init();
			   pacmanGame.startGame();
			   
				fixedPool.submit(new OrganismRunnablePacMan2(organism, pacmanGame/*evolution.getRightPanel().getGame()*/));	//// VERSIONE PARALLELA
				
//			//// VERSIONE SERIALE
//			//evaluate 
//			   esito = evaluate(organism);
//
//			// if is a winner , store a flag
//			   if (esito) 
//			   {
//				  win = true;
//			   
//				  if (organism.getFitness() > max_fitness_of_winner) 
//				  {
//					 max_fitness_of_winner = organism.getFitness();
//					 EnvConstant.MAX_WINNER_FITNESS = max_fitness_of_winner;
//				  }
//			   //
//			   // 01.06.2002 : store only first organism 
//				  if (EnvConstant.FIRST_ORGANISM_WINNER == null) 
//				  {
//					 EnvConstant.FIRST_ORGANISM_WINNER = organism;
//				  // System.out.print("\n okay flagged first *****");
//				  }
//			   }
//			///// FINE VERSIONE SERIALE
			}
			
			fixedPool.shutdown();							//// VERSIONE PARALLELA
			fixedPool.awaitTermination(1, TimeUnit.DAYS);	//// VERSIONE PARALLELA
			
			//TODO FINE DA PARALLELIZZARE
		 
		 //compute average and max fitness for each species
			Iterator itr_specie;
			itr_specie = pop.species.iterator();
			while (itr_specie.hasNext()) 
			{
			   Species _specie = ((Species) itr_specie.next());
			   _specie.compute_average_fitness();
			   _specie.compute_max_fitness();
			}
			
		 // Only print to file every print_every generations /// WRITE TO FILE ///
			String cause1 = " ";
			String cause2 = " ";
			if (_neat.p_print_every != 0 && (((generation % _neat.p_print_every) == 0) || (win))) 
			{
			   if ((generation % _neat.p_print_every) == 0)
				  cause1 = " request";
			   if (win)
				  cause2 = " winner";
			
			   // Salva su file l'intera popolazione per poter ricominciare da lì (implementazione originale)
//			   String name_of_specie = EnvRoutine.getJneatFileData(filename) + generation;
//			   pop.print_to_file_by_species(name_of_specie);
				String nome = "Population_" + pop.getFinal_gen();
				pop.print_to_file_by_species(MyConstants.POPULATIONS_DIR + nome);
			   
			   // Salva su file il migliore della popolazione ogni print_every generazioni
			   Organism bestPopOrg = pop.getCurrentPop_bestOrganism();
			   if (bestPopOrg != null)
			   {
				   evolution.storeBestNet(bestPopOrg);
				   String name = MyConstants.RESULTS_DIR + "prova_" + bestPopOrg.getGeneration();
				   evolution.serializeOnFile(name, bestPopOrg);
			   }
			}
			
			if (EnvConstant.STOP_EPOCH)
			{
//				 // before exit save last population
//				u_pop.print_to_file_by_species(EnvRoutine.getJneatFileData(EnvConstant.NAME_CURR_POPULATION));
				String nome = "Population_" + pop.getFinal_gen();
				pop.print_to_file_by_species(MyConstants.POPULATIONS_DIR + nome);
			}
		 
		 // if exist a winner write to file   /// WRITE TO FILE ///
			if (win) 
			{
			   String name_of_winner;
			   int conta = 0;
			   itr_organism = pop.getOrganisms().iterator();
			   while (itr_organism.hasNext()) 
			   {
				  Organism _organism = ((Organism) itr_organism.next());
				  if (_organism.winner) 
				  {
					 name_of_winner = EnvRoutine.getJneatFileData(winner_prefix) + generation + "_" + _organism.getGenome().genome_id; 
					 _organism.getGenome().print_to_filename(name_of_winner);
				  // EnvConstant.SERIAL_WINNER++;
					 conta++;
				  }
				  if (EnvConstant.SUPER_WINNER_) 
				  {
					 name_of_winner = EnvRoutine.getJneatFileData(winner_prefix)+ "_SUPER_" + generation + "_" + _organism.getGenome().genome_id; 
					 _organism.getGenome().print_to_filename(name_of_winner);
					 EnvConstant.SUPER_WINNER_ = false;
				  }
			   }
			}
			
		 // wait an epoch and make a reproduction of the best species
			pop.epoch(generation);
			
//			System.out.println("FITNESS PIU' ALTA: " + pop.getHighest_fitness());
//		    System.out.println("FITNESS MEDIA: " + pop.getMean_fitness());
		    
			graphs.updateGraphPanel((Organism)EnvConstant.CURR_ORGANISM_CHAMPION, pop);
			
//			graphs.updateGraphPanel(pop);
			
//			System.out.println(pop.getHighest_fitness());
//			System.out.println(pop.getFinal_gen());
			
			if (!EnvConstant.REPORT_SPECIES_TESTA.equalsIgnoreCase("")) 
			{
				
//			   doc2.insertString(doc2.getLength(),  "\n\n GENERATION : " + generation,  textPane2.getStyle(My_styles[2])); 
//			   doc2.insertString(doc2.getLength(), EnvConstant.REPORT_SPECIES_TESTA, textPane2.getStyle(My_styles[1])); 
//			   doc2.insertString(doc2.getLength(), EnvConstant.REPORT_SPECIES_CORPO, textPane2.getStyle(My_styles[3])); 
//			   doc2.insertString(doc2.getLength(), EnvConstant.REPORT_SPECIES_CODA,  textPane2.getStyle(My_styles[1])); 	
//			
//			   textPane2.setCaretPosition(doc2.getLength());
			
			
//			   if (!(EnvConstant.FIRST_ORGANISM_WINNER == null)) 
//			   {
//				  int idx = ((Organism) EnvConstant.FIRST_ORGANISM_WINNER).genome.genome_id;
//			   
//				  if (win)
//					 riga1 = "Time : " + generation + " genome (id=" + idx + ") is Current CHAMPION - WINNER "; 
//				  else
//					 riga1 = "Time : " + generation + " genome (id=" + idx + ") is Current CHAMPION "; 
//			   
//			
////				  drawGraph((Organism) EnvConstant.FIRST_ORGANISM_WINNER, riga1, mappa_graph);
//				  storeBestNet((Organism) EnvConstant.FIRST_ORGANISM_WINNER);
//				  updateNewGui((Organism) EnvConstant.FIRST_ORGANISM_WINNER);
//				  
//			
//			   
//			   }
			   if (!(EnvConstant.CURR_ORGANISM_CHAMPION == null)) 
			   {
			
//				  drawGraph((Organism) EnvConstant.CURR_ORGANISM_CHAMPION, " ", mappa_graph_curr); 
				   Organism o = (Organism) EnvConstant.CURR_ORGANISM_CHAMPION;
				   
//				   System.out.println(o.getGeneration());

				   if (!debug && o.getGeneration() == 1) 
				   {
				   		o.setGeneration(0);
				   		debug = true;
				   }
				   
				   winners.add(o);
				   evolution.storeBestNet(o);
				   String nomefile = MyConstants.RESULTS_DIR + "prova_" + o.getGeneration();
				   evolution.serializeOnFile(nomefile, o);
				   evolution.updateSimulationPanel(o);
				   graphs.updateForzaOptionsPanel(o);
				   net.updateNetPanel(o);
//				   net.drawGraph(o, mappa);
				   

			
			   }
			
			
			}
//			v1_species.add(new Double(generation));
//			v1_species.add(new Double(pop.getSpecies().size()));
//		 
//			v1_fitness.add(new Double(generation));
//			v1_fitness.add(new Double(pop.getHighest_fitness()));
//		 
//			v1_fitness_win.add(new Double(generation));
//			v1_fitness_win.add(new Double(EnvConstant.MAX_WINNER_FITNESS));
//
//
//		    drawCurve(riga1, pop.getHighest_fitness(), generation,  pop.getSpecies().size(),  _neat.p_pop_size);

			 
			if (win)
			   riga1 = "Time : " + generation + " found WINNER ! ";
			else
			   riga1 = "Time : " + generation + " ";

//			drawCurve(riga1, pop.getHighest_fitness(), generation,  pop.getSpecies().size(),  _neat.p_pop_size);
			    
			if (win) 
			{
			   return true;
			} 
			else
			   return false;
		 	} 
			 catch (Exception e) 
			{
			   System.out.print("\n exception in generation.epoch ->" + e);
			   System.exit(12);
			   return false;
			}
	  }
	   
	   public boolean[] getOtherSettingsValues()
	   {
		   return settings.getOtherSettings().getUpperPanel().getValues();
	   }
}
