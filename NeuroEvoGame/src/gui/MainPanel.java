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

import common.MyConstants;
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

public class MainPanel extends JPanel implements Runnable
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

	  
	public MainPanel(JFrame f) 
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
		
		double x = 0;
		double a = 0;
		double v = 0;
		double x_tgt = 0;
		double y_tgt = 0;
		
		int simGen = 0;
		int prevNetGen = -1;
		int currNetGen = 0;
		String prevSelectedChart = graphs.getLeftPanel().getOptionsPanel().getChartList().getSelectedItem().toString();
		
		int prevForzaGen = -1;
		int currForzaGen = 0;
		
		int prevSelectedThrow = -1;
		int currSelectedThrow = graphs.getLeftPanel().getForzaOptionsPanel().getThrowList().getSelectedIndex();
		
		double x_rimbalzo = 0; // per traslare la parabola
		double X_rimbalzo = 0;
		
		double y_rimbalzo = 0;
		double Y_rimbalzo = 0;
		
		double t_sim = 0;
		double x_sim = 0;
		double y_sim = 0;
		double x_rim_sim = 0;
		double y_rim_sim = 0;
		double t_rim_sim = 0;
		double v_rim_sim = -1;
		double h_max = 0;
		double gittata = 0;
		double diametro = 0.05;	// Il diametro del corpo lanciato è di 50 mm (5 cm)
		
		int targetPos = 0;
		double t_charge = 0;
		
		// Mappa rappresentante il vettore velocità: ad ogni istante t è associata una coppia di valori ( v_x(t) = v0x , v_y(t) )
		Map<Double, Vector2d> vel_vector = new HashMap<Double, Vector2d> ();
		
		double prevSelectedOrgIndex = -1;
		
		boolean change_inputs = false;
		boolean updated = false;
		
		double currentHeight = MainFrame.HEIGHT;
		double currentWidth = MainFrame.WIDTH;
		
		double currentDisplayWidth = evolution.getRightPanel().getWidth();
		double currentDisplayHeight = evolution.getRightPanel().getHeight();
		
		
        long desiredFrameRateTime = 1000 / 60;
        long currentTime = System.currentTimeMillis();
        long lastTime = currentTime - desiredFrameRateTime;
        long unprocessedTime = 0;
        boolean needsRender = false;
        
        double scaleX = 2;
        double scaleY = 2;
		
		while (isRunning)
		{
			evolution.getRightPanel().requestFocus();
//			evolution.game.setScreenSize(evolution.getRightPanel().getSize());
//			evolution.game.setScreenScale(new Point2D.Double(5, 5));
//			evolution.getRightPanel().setPreferredSize(evolution.getRightPanel().getSize());
//			System.out.println(evolution.getRightPanel().getGame().screenSize);
//			System.out.println(evolution.getRightPanel().size());
			
			scaleX = evolution.getRightPanel().getWidth()/evolution.getRightPanel().getGame().screenSize.getWidth();
			scaleY = evolution.getRightPanel().getHeight()/evolution.getRightPanel().getGame().screenSize.getHeight();
			
//			System.out.println(scaleX + "   " + scaleY);
			
			currentDisplayWidth = evolution.getRightPanel().getWidth();
			currentDisplayHeight = evolution.getRightPanel().getHeight();
			
			long startTime = System.currentTimeMillis();
			
			currentTime = System.currentTimeMillis();
            unprocessedTime += currentTime - lastTime;
            lastTime = currentTime;
            
            while (unprocessedTime >= desiredFrameRateTime) {
                unprocessedTime -= desiredFrameRateTime;
                evolution.getRightPanel().update();
                needsRender = true;
            }
            
            if (needsRender) {
                Graphics2D g = (Graphics2D) evolution.getRightPanel().getGraphics2D();
    			evolution.getRightPanel().getGame().setScreenScale(new Point2D.Double(scaleX, scaleY));
                evolution.getRightPanel().repaint();
                needsRender = false;
            }
            else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                }
            }
			
			if (MyConstants.LOADED_INPUTS)
			{
				t_sim = 0;
				x_sim = 0;
				y_sim = 0;
				x_rim_sim = 0;
				y_rim_sim = 0;
				t_rim_sim = 0;
				v_rim_sim = -1;
				h_max = 0;
				gittata = 0;
				targetPos = 0;
				t_charge = 0;
//				evolution.getRightPanel().resetTail();
//				evolution.getRightPanel().resetTargetTail();
				
				Organism organism = winners.get(
						evolution.getLeftPanel().getOptionsPanel().
						getGenerationList().getSelectedIndex());
				
				String lancio = evolution.getLeftPanel().getOptionsPanel().getThrowList().getSelectedItem().toString();
				if (lancio.equals("Best"))
					lancio = ""+(organism.getMap().get(EnvConstant.NUMBER_OF_SAMPLES).get(MyConstants.LANCIO_MIGLIORE_INDEX).intValue()+1);
				
				int selectedThrow = Integer.parseInt(lancio)-1;
				
				fixedPool = Executors.newFixedThreadPool(1);	//// VERSIONE PARALLELA
				EnvConstant.TYPE_OF_SIMULATION = EnvConstant.SIMULATION_FROM_CLASS;
				fixedPool.submit(new OrganismRunnableMovementLoaded(organism, MyConstants.LOADED_X, MyConstants.LOADED_Y, selectedThrow));
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
					
					t_sim = 0;
					x_sim = 0;
					y_sim = 0;
					vel_vector.clear();
					x_rim_sim = 0;
					y_rim_sim = 0;
					t_rim_sim = 0;
					v_rim_sim = -1;
					h_max = 0;
					gittata = 0;
					targetPos = 0;
					t_charge = 0;
//					evolution.getRightPanel().resetTail();
//					evolution.getRightPanel().resetTargetTail();
					
					evolution.getLeftPanel().updateInfoRete(selectedOrg.getMap().get(EnvConstant.NUMBER_OF_SAMPLES));
					evolution.getLeftPanel().updateInfoLancio(infoLancio);
					
					x_tgt = infoLancio.get(MyConstants.X_TARGET_INDEX);
					y_tgt = infoLancio.get(MyConstants.Y_TARGET_INDEX);

					evolution.repaint();
				}
				
				if (targetPos >= selectedOrg.getTargetMap().get(selectedThrow).size())
					targetPos--;

				double prova_x = x_tgt + infoLancio.get(MyConstants.VEL_RET_X_INDEX)*(t_charge + t_sim);
				double prova_y = y_tgt + infoLancio.get(MyConstants.VEL_RET_Y_INDEX)*(t_charge + t_sim);
				
//	            double X_tgt = evolution.getRightPanel().proportionX(prova_x);
//	            double Y_tgt = evolution.getRightPanel().proportionY(prova_y);
//
//				evolution.getRightPanel().getTarget().setFrame(MyConstants.BORDER_X + X_tgt - 2.5, 
//						(evolution.getRightPanel().getHeight()-MyConstants.BORDER_Y) - Y_tgt - 2.5, 5, 5);
				
//				if (MyConstants.SETTINGS_VALUES[MyConstants.SIM_SHOW_BEST_INDEX])
//				{
//		            double bestTgt_x = evolution.getRightPanel().proportionX(infoLancio.get(MyConstants.BEST_TARGET_X_INDEX));
//		            double bestTgt_y = evolution.getRightPanel().proportionY(infoLancio.get(MyConstants.BEST_TARGET_Y_INDEX));
//		            double bestShot_x = evolution.getRightPanel().proportionX(infoLancio.get(MyConstants.X_MIGLIORE_INDEX));
//		            double bestShot_y = evolution.getRightPanel().proportionY(infoLancio.get(MyConstants.Y_MIGLIORE_INDEX));
//					
//					evolution.getRightPanel().getBestTarget().setFrame(MyConstants.BORDER_X + bestTgt_x - 2.5, 
//							(evolution.getRightPanel().getHeight()-MyConstants.BORDER_Y) - bestTgt_y - 2.5, 5, 5);
//					
//					evolution.getRightPanel().getBestShot().setFrame(MyConstants.BORDER_X + bestShot_x - 1.5, 
//							(evolution.getRightPanel().getHeight()-MyConstants.BORDER_Y) - bestShot_y - 1.5, 3, 3);
//				
//				}
				
				
				simGen = evolution.getLeftPanel().getOptionsPanel().getGenerationList().getSelectedIndex();
				Organism o = winners.get(simGen);

				ArrayList<Double> bestThrow = evo_fit.computeMinVel(prova_x, prova_y);

				
//				evolution.getRightPanel().setA(bestThrow.get(0));
//				evolution.getRightPanel().setV(bestThrow.get(1));
				
				a = infoLancio.get(MyConstants.ANGOLO_INDEX);
				v = infoLancio.get(MyConstants.VELOCITA_INDEX);
				
				// INIZIO SIMULAZIONE MOTO PARABOLICO DEL CORPO
				
				// Aggiorna le componenti del vettore velocità per l'istante t
				double v_x = v*Math.cos(a);
				
				double v_y = v*Math.sin(a) - MyConstants.GRAVITY*t_sim;
				
				vel_vector.put(t_sim, new Vector2d(v_x, v_y));
				
				
				// Aggiorna la posizione del corpo utilizzando le equazioni del moto per l'istante t
				x_sim = v*Math.cos(a)*t_sim;
				
				y_sim = v*Math.sin(a)*t_sim - 0.5*MyConstants.GRAVITY*Math.pow(t_sim, 2);
				
				// Calcola le proporzioni per la rappresentazione grafica
//				double X_sim = evolution.getRightPanel().proportionX(x_sim);
//				double Y_sim = evolution.getRightPanel().proportionY(y_sim);

				// Aggiorna le posizioni nella rappresentazione grafica (corpo + coda)
//    			evolution.getRightPanel().getPeso().setFrame(
//    					MyConstants.BORDER_X+X_sim -1.5,(evolution.getRightPanel().getHeight()-MyConstants.BORDER_Y) - Y_sim - 1.5, 3, 3);
    			
//    			if (y_sim >= 0) 
//    				evolution.getRightPanel().getTail().add(
//    						new Vector2d(MyConstants.BORDER_X+X_sim, (evolution.getRightPanel().getHeight()-MyConstants.BORDER_Y) - Y_sim));
    			
//    			if (evolution.getRightPanel().getTail().size() > MyConstants.TAIL_LENGTH)
//    			{
//    				evolution.getRightPanel().getTail().removeFirst();
//    			}
    			
//    			if (prova_x >= 0 && prova_y >= 0)
//    			{
//    				evolution.getRightPanel().getTargetTail().add(
//    						new Vector2d(MyConstants.BORDER_X+X_tgt, (evolution.getRightPanel().getHeight()-MyConstants.BORDER_Y) - Y_tgt));
//    			}
//    			
//    			if (evolution.getRightPanel().getTargetTail().size() > MyConstants.TARGET_TAIL_LENGTH)
//    			{
//    				evolution.getRightPanel().getTargetTail().removeFirst();
//    			}
				
//				if (y_sim > 0)
//				{
//					gittata = 2*v*Math.cos(a)*v*Math.sin(a)/MyConstants.GRAVITY;	// gittata
//					h_max = ( Math.pow(v, 2)*Math.pow(Math.sin(a), 2) ) / (2*MyConstants.GRAVITY);	// altezza massima
//					v_rim_sim = Math.sqrt( (2*MyConstants.GRAVITY*(h_max-diametro/2)) );	// velocità di rimbalzo (nel caso in cui il corpo sia elastico, es: palla da tennis)
//					
////					System.out.println(v_rim_sim);
//					
//					double m1 = infoLancio.get(MyConstants.MASSA_INDEX);	// massa del proiettile
//					double m2 = MyConstants.MASSA_TERRA;	// massa della terra
//					
//					double v1 = infoLancio.get(MyConstants.VELOCITA_INDEX);	// VELOCITA' INIZIALE DEL PROIETTILE
//					double v2 = 0;//MyConstants.VELOCITA_TERRA;	// VELOCITA' DI ROTAZIONE DELLA TERRA
//					
//					v_rim_sim = - ((((m1 - m2)*v1 + 2*m2*v2)/(m1 + m2)) * MyConstants.ATTRITO);
//					
////					System.out.println(v_rim_sim + " vs " + v);
//					
//					if (x_sim > MyConstants.ASSE_X)	// RESETTA IL LANCIO
//					{
//						x_sim = 0;
//						y_sim = 0;
//						t_sim = 0;
//						vel_vector.clear();
//						x_rim_sim = 0;
//						y_rim_sim = 0;
//						t_rim_sim = 0;
//						gittata = 0;
//						h_max = 0;
//						v_rim_sim = -1;
////						simulation.getRightPanel().resetTail();
//						targetPos = 0;
//						t_charge = 0;
//						evolution.getRightPanel().resetTargetTail();
//					}
//				}
//				
//				// Aggiorna il tempo t e la posizione del target(l'indice dell'array delle posizioni del target)
//				if (t_charge == o.getMap().get(selectedThrow).get(MyConstants.TEMPO_INDEX))
//				{
//					t_sim += 0.04;
//					targetPos++;
//				}
//				if (t_charge < o.getMap().get(selectedThrow).get(MyConstants.TEMPO_INDEX)) 
//				{
//					t_charge += 0.04;
//					targetPos++;
//				}
//				
////				System.out.println(o.getTargetMap().get(selectedThrow).get(0).x + " " + o.getTargetMap().get(selectedThrow).get(o.getTargetMap().get(selectedThrow).size()-1).x);
//				
//				// Controllo x e y per resettare il lancio (x > asse_X) o per effettuare il rimbalzo (y = 0)
//				if (MyConstants.SETTINGS_VALUES[MyConstants.SIM_PHYSICS])
//				{
//					if (y_sim < 0)	// RIMBALZA
//					{
//	//					System.out.println("VELOCITA_RIMBALZO = " + v_rim + " vs V_0 = " + v);
//						x_rim_sim = v_rim_sim*Math.cos(a)*t_rim_sim + gittata;
//						y_rim_sim = v_rim_sim*Math.sin(a)*t_rim_sim - 0.5*MyConstants.GRAVITY*Math.pow(t_rim_sim, 2);
//						double X_rim_sim = evolution.getRightPanel().proportionX(x_rim_sim);
//						double Y_rim_sim = evolution.getRightPanel().proportionY(y_rim_sim);
//	
//		    			evolution.getRightPanel().getPeso().setFrame(
//		    					MyConstants.BORDER_X+X_rim_sim -1.5,(evolution.getRightPanel().getHeight()-MyConstants.BORDER_Y) - Y_rim_sim - 1.5, 3, 3);
//		    			
//		    			if (y_rim_sim >= 0)
//		    				evolution.getRightPanel().getTail().add(
//		    					new Vector2d(MyConstants.BORDER_X+X_rim_sim, (evolution.getRightPanel().getHeight()-MyConstants.BORDER_Y) - Y_rim_sim));
//		    			
//		    			if (evolution.getRightPanel().getTail().size() > MyConstants.TAIL_LENGTH)
//		    			{
//		    				evolution.getRightPanel().getTail().removeFirst();
//		    			}
//		    			
//						// Aggiorna il tempo t della parabola di rimbalzo
//		    			t_rim_sim += 0.04;
//	
//	//					System.out.println("X = " + x_sim + ", Y = " + y_rim_sim);
//					}
//					if (y_rim_sim < 0)
//					{
//						x_rim_sim = 0;
//						y_rim_sim = 0;
//						t_rim_sim = 0;
//						
//						// aggiorna gittata, altezza massima e velocità di rimbalzo in base all'ultimo rimbalzo
//						gittata += 2*v_rim_sim*Math.cos(a)*v_rim_sim*Math.sin(a)/MyConstants.GRAVITY;	// nuova gittata
//						h_max = ( Math.pow(v_rim_sim, 2)*Math.pow(Math.sin(a), 2) ) / (2*MyConstants.GRAVITY);	// nuova altezza massima
////						v_rim_sim = Math.sqrt( (2*MyConstants.GRAVITY*(h_max-diametro/2)) );	// nuova velocità di rimbalzo
//						
//						double m1 = infoLancio.get(MyConstants.MASSA_INDEX);	// massa del proiettile
//						double m2 = MyConstants.MASSA_TERRA;	// massa della terra
//						
//						double v1 = v_rim_sim;	// VELOCITA' INIZIALE DEL PROIETTILE
//						double v2 = 0;//MyConstants.VELOCITA_TERRA;	// VELOCITA' DI ROTAZIONE DELLA TERRA
//						
//						v_rim_sim = - ((((m1 - m2)*v1 + 2*m2*v2)/(m1 + m2)) * MyConstants.ATTRITO);
//						
////						System.out.println(v_rim_sim + " vs " + v1);
//					}
//					if (x_rim_sim > MyConstants.ASSE_X || Double.isNaN(v_rim_sim) || (v_rim_sim < 0.5 && v_rim_sim >= 0)
//							|| prova_x < 0 || prova_y < 0)	// RESETTA IL LANCIO
//					{
//	//					System.out.println("Lancia ancora");
//						x_sim = 0;
//						y_sim = 0;
//						t_sim = 0;
//						vel_vector.clear();
//						x_rim_sim = 0;
//						y_rim_sim = 0;
//						t_rim_sim = 0;
//						gittata = 0;
//						h_max = 0;
//						v_rim_sim = -1;
//						evolution.getRightPanel().resetTail();
//						evolution.getRightPanel().resetTargetTail();
//						targetPos = 0;
//						t_charge = 0;
//					}
//				}
//				else	// Se il rimbalzo (collisione con il terreno) è disabilitato, allora quando tocca l'asse delle x (y = 0) resetta il lancio
//				{
//					if (y_sim<0)
//					{
////						t_sim = 0;
//						x_sim = 0;
//						y_sim = 0;
//						t_sim = 0;
//						vel_vector.clear();
//						x_rim_sim = 0;
//						y_rim_sim = 0;
//						t_rim_sim = 0;
//						gittata = 0;
//						h_max = 0;
//						v_rim_sim = -1;
////						simulation.getRightPanel().resetTail();
//						targetPos = 0;
//						t_charge = 0;
//						evolution.getRightPanel().resetTargetTail();
//					}
//				}
//				// FINE SIMULAZIONE MOTO PARABOLICO DEL CORPO
				
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
			
//			if (tabbedPanel.getSelectedIndex() == 1 && (!simulation.getStart() && !simulation.getLoad()) && !graphs.getLeftPanel().getOptionsPanel().getChartList().getSelectedItem().toString().equals(prevSelectedChart))
//			{
//				prevSelectedChart = graphs.getLeftPanel().getOptionsPanel().getChartList().getSelectedItem().toString();
//				
//				graphs.repaint();
//			}
			
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
			}

		}
	}
	
	/// SIMULAZIONE MOTO PROIETTILE ///
	
	
//	public void simulate()
//	{
//		double t_sim = 0;
//		double x_sim = 0;
//		double y_sim = 0;
//		
//		// Mappa rappresentante il vettore velocità: ad ogni istante t è associata una coppia di valori ( v_x(t) = v0x , v_y(t) )
//		Map<Double, Vector2d> vel_vector = new HashMap<Double, Vector2d> ();
//		
//		
//		// Aggiorna le componenti del vettore velocità per l'istante t
//		double v_x = v0*Math.cos(a);
//		
//		double v_y = v0*Math.sin(a) - MyConstants.GRAVITY*t_sim;
//		
//		vel_vector.put(t_sim, new Vector2d(v_x, v_y));
//		
//		
//		// Aggiorna la posizione del corpo utilizzando le equazioni del moto per l'istante t
//		x_sim = v0*Math.cos(a)*t_sim;
//		
//		y_sim = v0*Math.sin(a)*t_sim - 0.5*MyConstants.GRAVITY*Math.pow(t_sim, 2);
//		
//		
//		// Aggiorna il tempo t
//		t_sim += 0.04;
//	}
	
	
	
	
	
	
	
	
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
			   
				fixedPool.submit(new OrganismRunnableMovement(organism));	//// VERSIONE PARALLELA
				
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
	
	   public boolean evaluate(Organism organism) 
		  {
			 double fit_dyn = 0.0;
			 double err_dyn = 0.0;
			 double win_dyn = 0.0;
			 double angle = 0.0;
			 double velocity = 0.0;
			 double y_target = 0.0;
			 double x_target = 0.0;
			 double total_err = 0.0;
			 Map<Integer,ArrayList<Double>> map = null;
		  // per evitare errori il numero di ingressi e uscite viene calcolato in base
		  // ai dati ;
		  // per le unit di input a tale numero viene aggiunto una unit bias
		  // di tipo neuron
		  // le classi di copdifica input e output quindi dovranno fornire due
		  // metodi : uno per restituire l'input j-esimo e uno per restituire
		  // il numero di ingressi/uscite
		  // se I/O è da file allora è il metodo di acesso ai files che avrà lo
		  // stesso nome e che farà la stessa cosa.
		  
			 Network _net = null;
			 boolean success = false;
		  
			 double errorsum = 0.0;
			 int net_depth = 0; //The max depth of the network to be activated
			 int count = 0;
		  
		  
		  
		  //	  			   System.out.print("\n evaluate.step 1 ");
		  
			 
			 
			 double in[] = null;
			 in = new double[EnvConstant.NR_UNIT_INPUT + 1];
//			 in = new double[EnvConstant.NR_UNIT_INPUT];
		  
		  // setting bias
		  
			 in[EnvConstant.NR_UNIT_INPUT] = 1.0;
		  
			 double out[][] = null;
			 out = new double[EnvConstant.NUMBER_OF_SAMPLES][EnvConstant.NR_UNIT_OUTPUT];
		  
			 //double tgt[][] = null;
			 //tgt = new double[EnvConstant.NUMBER_OF_SAMPLES][EnvConstant.NR_UNIT_OUTPUT];
			 double tgt[][] = null;
			 tgt = new double[EnvConstant.NUMBER_OF_SAMPLES][EnvConstant.NR_UNIT_INPUT+1];
			 
		  
			 Integer ns = new Integer(EnvConstant.NUMBER_OF_SAMPLES);
		  
		  
			 _net = organism.net;
			 net_depth = _net.max_depth();
		  
		   // pass the number of node in genome for add a new 
		   // parameter of evaluate the fitness
		   //
			 int xnn = _net.getAllnodes().size();
			 Integer nn = new Integer(xnn);
		  
		  
			 //Class[] params = {int.class, int.class , double[][].class, double[][].class};
			 //Object paramsObj[] = new Object[] {ns, nn, out, tgt};
			 Class[] params = {int.class, double[][].class, double[][].class};
			 Object paramsObj[] = new Object[] {ns, out, tgt};
			 
//			 double minX = 40;
//			 double maxX = 60;	//X massima 100 [40+60]
//			 double minY = 20;
//			 double maxY = 80;	//Y massima 100 [20+80]
			 
//			 Random rx = new Random();
//			 long seedX = 10;
//			 rx.setSeed(seedX);
//			 Random ry = new Random();
//			 long seedY = 1000;
//			 ry.setSeed(seedY);
			 
//			 double[] inputX = new double[EnvConstant.NUMBER_OF_SAMPLES];
//			 double[] inputY = new double[EnvConstant.NUMBER_OF_SAMPLES];
//			 double[] inputX = {rx.nextDouble(), rx.nextDouble(), rx.nextDouble(), rx.nextDouble(), rx.nextDouble(),
//					 rx.nextDouble(), rx.nextDouble(), rx.nextDouble(), rx.nextDouble(), rx.nextDouble()};
//			 double[] inputY = {ry.nextDouble(), ry.nextDouble(), ry.nextDouble(), ry.nextDouble(), ry.nextDouble(),
//					 ry.nextDouble(), ry.nextDouble(), ry.nextDouble(), ry.nextDouble(), ry.nextDouble()};
//			 double[] inputX = {40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0, 45.0, 55.0, 65.0};
//			 double[] inputY = {70.0, 30.0, 20.0, 50.0, 60.0, 35.0, 45.0, 100.0, 75.0, 95.0};
			 
//			 for (int i=0; i<EnvConstant.NUMBER_OF_SAMPLES; i++)
//				 input[i]=Math.random();
			 


//			 for (count = 0; count < EnvConstant.NUMBER_OF_SAMPLES; count++) 
//			 {
////				 x = minX + Math.random()*maxX;
////				 y = minY + Math.random()*maxY;
////				 x = rx.nextFloat();
////				 y = ry.nextFloat();
//				 inputX[count] = x;
//				 inputY[count] = y;
//			 }
		  
			 if (EnvConstant.TYPE_OF_SIMULATION == EnvConstant.SIMULATION_FROM_CLASS)
			 {
			 
			 
			 // case of input from class java
			 
				try 
				{
				   //int plist_in[] = new int[2];
				   //Class[] params_inp = {int[].class};
				   //Object[] paramsObj_inp = new Object[] {plist_in};

//				   Class[] params_inp = {double.class};
//				   Object[] paramsObj_inp = new Object[] {y};
					 String mask6d = "  0.00000";
					 DecimalFormat fmt6d = new DecimalFormat(mask6d);
					 
					 Random rx = new Random();

					 Random ry = new Random();

					 Random rm = new Random();
					 
//					 //***** INPUT RIPETUTI *****//
//					 rx.setSeed(100);
//					 ry.setSeed(10000);
//					 rm.setSeed(1000);
					 
					 //***** INPUT RANDOM *****//
					 long seedX = (long)(Math.random()*100);	
					 rx.setSeed(seedX);
					 long seedY = (long)(Math.random()*10000);
					 ry.setSeed(seedY);
					 long seedM = (long)(Math.random()*1000);
					 rm.setSeed(seedM);
					 
					 
				   for (count = 0; count < EnvConstant.NUMBER_OF_SAMPLES; count++) 
				   {
//					   y = Math.random()*maxY;
//					   input[count] = y;
					  //plist_in[0] = count;
				   // first activation from sensor to first next level of neurons
//					  for (int j = 0; j < EnvConstant.NR_UNIT_INPUT; j++) 
//					  {
//						 //plist_in[1] = j;
//						 Method_inp = Class_inp.getMethod("getInput", params_inp);
//						 ObjRet_inp = Method_inp.invoke(ObjClass_inp, paramsObj_inp);
//						 double v1 = Double.parseDouble(ObjRet_inp.toString());
//						 in[j] = v1;
//					  }
					   

					   ///IMPLEMENTAZIONE DECISIONE DI LANCIO   
					   
					   double delta_t = 0.04;
					   double current_time = 0;
					   double minX = 20;
					   double maxX = 80;
					   double minY = 20;
					   double maxY = 80;
					   double minM = 1;
					   double maxM = 2;
					   double massa = minM + rm.nextDouble()*maxM;	// 2kg
					   double v = 0;
					   double minF = 15;	// forza minima
					   double maxF = 60;	// forza massima
					   double maxA = 1.5708;
					   double minV = 0;
					   double maxV = 75;
//					   double d_minA = -0.031416;
					   double d_minA = 0;
					   double d_maxA = 0.031416;
//					   double d_minF = -5;
					   double d_minF = 0;
					   double d_maxF = 5;
					   
					   double a = 0;
					   double F = 15;
					   
					   in[0] = rx.nextDouble();
					   in[1] = ry.nextDouble();
					   in[2] = v;
					   in[3] = a;
					   in[4] = F;
					   
					   tgt[count][0] = in[0];
					   tgt[count][1] = in[1];
					   tgt[count][2] = in[2];
					   tgt[count][3] = in[3];
					   tgt[count][4] = in[4];
					   tgt[count][5] = massa;
					   
					   for (int i = 0; i<50; i++)
					   {
						   current_time += delta_t;
						   
						   // load sensor   
						   _net.load_sensors(in);
						   
						   if (EnvConstant.ACTIVATION_PERIOD == EnvConstant.MANUAL)
						   {
							   for (int relax = 0; relax < EnvConstant.ACTIVATION_TIMES; relax++)
							   {
								   success = _net.activate();
							   }
						   }
						   else
						   {   	            
							   //first activation from sensor to next layer....
							 success = _net.activate();
							 
						  // next activation while last level is reached !
						  // use depth to ensure relaxation
							 for (int relax = 0; relax <= net_depth; relax++)
							 {
								 success = _net.activate();
							 }
						   }
						   
						   //output
						   for( int j=0; j < EnvConstant.NR_UNIT_OUTPUT; j++)
						   {
							   out[count][j] = ((NNode) _net.getOutputs().elementAt(j)).getActivation();
						   }
						   
						   // clear net		 
						   _net.flush();
						   
						   double delta_a = d_minA + out[count][0]*d_maxA;
						   double delta_F = d_minF + out[count][1]*d_maxF;
						   double lascia = out[count][2];
						   
						   a += delta_a;
						   F += delta_F;
						   
						   double acc = F/massa;
						   double delta_v = acc*delta_t;
						   
						   v += delta_v;
						   
						   double V = (v - minV)/maxV;
						   double A = (a)/maxA;
						   double Freal = (F -minF)/maxF;
						   
						   in[2] = V;
						   in[3] = A;
						   in[4] = Freal;
						   tgt[count][2] = v;
						   tgt[count][3] = a;
						   tgt[count][4] = F;
						   
//						   double x_tgt = minX + tgt[count][0]*maxX;
//						   double y_tgt = minY + tgt[count][1]*maxY;
//						   
//						   x_tgt++;
//						   
//						   double X = (x_tgt - minX)/maxX;
//						   
//						   in[0] = X;
//						   tgt[count][0] = in[0];
						   
						   if (lascia >= 0.5) 
						   {
							   break;
						   }
					   }
					   
					   
			 ///IMPLEMENTAZIONE VECCHIA  				   
//					   
////				   in[0] = inputX[count];
////				   in[1] = inputY[count];
//				   in[0] = rx.nextDouble();
//				   in[1] = ry.nextDouble();
//				   in[2] = rm.nextDouble();
//				   //in[2] = rm.nextDouble();
////				   in[0] = NeatRoutine.randfloat();
////				   in[1] = NeatRoutine.randfloat();
//				   tgt[count][0] = in[0];
//				   tgt[count][1] = in[1];
//				   tgt[count][2] = in[2];
//				   //tgt[count][2] = in[2];
//
////				   System.out.println("------ LANCIO: "+count+" ------");
////				   System.out.println("INPUT 0:"+in[0]);
////				   System.out.println("INPUT 1:"+in[1]);
//				   // load sensor   
//					  _net.load_sensors(in);
//				   /*
//				   // activate net	  
//				   success = _net.activate();
//				   
//				   // next activation while last level is reached !
//				   // use depth to ensure relaxation
//				   
//				   for (int relax = 0; relax <= net_depth; relax++)
//					success = _net.activate();
//				   */
//				   
//					  if (EnvConstant.ACTIVATION_PERIOD == EnvConstant.MANUAL)
//					  {
//						 for (int relax = 0; relax < EnvConstant.ACTIVATION_TIMES; relax++)
//						 {
//							success = _net.activate();
//						 }
//					  }
//					  else
//					  {   	            
//					  // first activation from sensor to next layer....
////						  System.out.println("LANCIO "+count);
//						 success = _net.activate();
//						 
//					  // next activation while last level is reached !
//					  // use depth to ensure relaxation
//						 for (int relax = 0; relax <= net_depth; relax++)
//						 {
//							success = _net.activate();
//						 }
//					  }
//				   
//				   
//				   
//				   // for each sample save each output	
//					   
////					   System.out.println("INPUT X: " +inputX[count]);
////					   System.out.println("INPUT Y: "+inputY[count]);
////					  for( int j=0; j < EnvConstant.NR_UNIT_OUTPUT; j++){
////						  out[count][j] = ((NNode) _net.getOutputs().elementAt(j)).getActivation();
//////						  System.out.println(fmt6d.format(out[count][j]));
//////						  System.out.println();
////					  }
//		
//				   // for each sample save each output	
////					  System.out.println("ESEMPIO NUMERO: "+count);
//					  for( int j=0; j < EnvConstant.NR_UNIT_OUTPUT; j++)
//					  {
//						 out[count][j] = ((NNode) _net.getOutputs().elementAt(j)).getActivation();
////						 System.out.println(out[count][j]);
//					  }
////					  double o1 = ((NNode) _net.getOutputs().elementAt(0)).getActivation();
////					  double o2 = ((NNode) _net.getOutputs().elementAt(1)).getActivation();
////					  out[count][0] = o1;
////					  out[count][1] = o2;
////				  System.out.println(fmt6d.format(o1));
////				  System.out.println(fmt6d.format(o2));
////				  System.out.println();
//						 
//					  
//				   
//				   // clear net		 
//					  _net.flush();
				   }
				} 
				
					catch (Exception e2) 
				   {
					  System.out.print("\n Error generic in Generation.input signal : err-code = \n" + e2); 
					  System.out.print("\n re-run this application when the class is ready\n\t\t thank! "); 
					  System.exit(8);
				   
				   }
			 
			 }  
		  
		  //success = true;
		  
		  
		  // control the result 
			 if (success) 
			 {
				try 
				{
				   if (EnvConstant.TYPE_OF_SIMULATION == EnvConstant.SIMULATION_FROM_CLASS)
				   {
				   
				   
				   // prima di passare a calcolare il fitness legge il tgt da ripassare
				   // al chiamante;
					  //int plist_tgt[] = new int[2];
					  //Class [] params_tgt = {int[].class};
					  //Object[] paramsObj_tgt = new Object[] {plist_tgt};
//					   Class [] params_tgt = {double.class};
//					   Object[] paramsObj_tgt = new Object[] {y};
					   
			//	   System.out.println(EnvConstant.NUMBER_OF_SAMPLES);
					  for (count = 0; count < EnvConstant.NUMBER_OF_SAMPLES; count++) 
					  {
					  //					 System.out.print("\n sample : "+count);
					  
						 //plist_tgt[0] = count;
//						  y=inputY[count];
//						 for (int j = 0; j < EnvConstant.NR_UNIT_OUTPUT; j++)
//						 {
//							//plist_tgt[1] = j;
//							Method_tgt = Class_tgt.getMethod("getTarget", params_tgt);
//							ObjRet_tgt = Method_tgt.invoke(ObjClass_tgt, paramsObj_tgt);
//							double v1 = Double.parseDouble(ObjRet_tgt.toString());
//						 //						System.out.print(" ,  o["+j+"] = "+v1);
//							//tgt[count][j] = v1;
//							tgt[count] = v1;
//						 }
//						 tgt[count][0] = inputX[count];
//						 tgt[count][1] = inputY[count];
	 					 
					  }

				   }
				//System.out.println(Class_fit);
				   Method_fit = Class_fit.getMethod("computeFitness", params);
				   ObjRet_fit = Method_fit.invoke(ObjClass_fit, paramsObj);
				   //System.out.println(ObjRet_fit);
//				   fit_dyn = Array.getDouble(ObjRet_fit, 0);
//				   err_dyn = Array.getDouble(ObjRet_fit, 1);
//				   win_dyn = Array.getDouble(ObjRet_fit, 2);
//				   angle = Array.getDouble(ObjRet_fit, 3);
//				   velocity = Array.getDouble(ObjRet_fit, 4);
//				   y_target = Array.getDouble(ObjRet_fit, 5);
//				   x_target = Array.getDouble(ObjRet_fit, 6);
				   HashMap<Integer,ArrayList<Double>> mappa = (HashMap<Integer, ArrayList<Double>>) ObjRet_fit;
				   ArrayList<Double> arrayBest = mappa.get(EnvConstant.NUMBER_OF_SAMPLES);
				   fit_dyn = arrayBest.get(MyConstants.FITNESS_TOTALE_INDEX);
				   err_dyn = arrayBest.get(MyConstants.ERRORE_INDEX);
				   win_dyn = arrayBest.get(MyConstants.WIN_INDEX);
				   angle = arrayBest.get(MyConstants.ANGOLO_INDEX);
				   velocity = arrayBest.get(MyConstants.VELOCITA_INDEX);
				   y_target = arrayBest.get(MyConstants.Y_TARGET_INDEX);
				   x_target = arrayBest.get(MyConstants.X_TARGET_INDEX);
				   total_err = arrayBest.get(MyConstants.ERRORE_TOTALE_INDEX);
				   map = mappa;
				//			   System.out.print("\n ce so passo!");
				

				} 
				
					catch (Exception e3) 
				   {
					  System.out.print("\n Error generic in Generation.success : err-code = \n" + e3); 
					  System.out.print("\n re-run this application when the class is ready\n\t\t thank! "); 
					  System.exit(8);
				   }
			 
				organism.setFitness(fit_dyn);
				organism.setError(err_dyn);
				organism.setTotalError(total_err);
				organism.setAngle(angle);
				organism.setVelocity(velocity);
				organism.setYTarget(y_target);
				organism.setXTarget(x_target);
				organism.setMap(map);
			 } 
			 
			 
			 else 
			 {
				errorsum = 999.0;
				organism.setFitness(0.001);
				organism.setError(errorsum);
				organism.setAngle(0.0);
				organism.setVelocity(0.0);
				organism.setYTarget(0.0);
			 }

		  
			 if (win_dyn == 1.0) 
			 {
				organism.setWinner(true);
				return true;
			 } 
		  
			 if (win_dyn == 2.0) 
			 {
				organism.setWinner(true);
				EnvConstant.SUPER_WINNER_ = true;
				return true;
			 } 
		  
			 organism.setWinner(false);
			 return false;
		  }
	   
	   public boolean[] getOtherSettingsValues()
	   {
		   return settings.getOtherSettings().getUpperPanel().getValues();
	   }
}
