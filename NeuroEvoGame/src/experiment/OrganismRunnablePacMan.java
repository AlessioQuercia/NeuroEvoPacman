package experiment;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.joml.Vector2d;

import common.Direction;
import common.MyConstants;
import experiment.evo_fit;
import jNeatCommon.EnvConstant;
import jneat.NNode;
import jneat.Network;
import jneat.Organism;
import newGui.infra.Game;
import pacmanGui.PacmanGame;
import pacmanGui.PacmanGame.State;

public class OrganismRunnablePacMan implements Runnable
{
	private Organism o;
	private PacmanGame game;
	
	private Map<Integer, Vector2d> pacmanPositions;
	private ArrayList<HashMap<Integer, Vector2d>> ghostsPositions;
	private Map<Integer, Direction> pacmanDirections;
	private ArrayList<HashMap<Integer, Integer>> ghostsDirections;
	private ArrayList<HashMap<Integer, Integer>> ghostsDesiredDirections;
	
	// dynamic definition for fitness
		  Class  Class_fit;
		  Object ObjClass_fit;
		  Method Method_fit;
		  Object ObjRet_fit;
	
	public OrganismRunnablePacMan(Organism o, PacmanGame game) 
	{
		this.o = o;
		this.game = game;
		
		this.pacmanPositions = new HashMap<Integer, Vector2d> ();
		this.ghostsPositions = new ArrayList<HashMap<Integer, Vector2d>>();
		
		this.pacmanDirections = new HashMap<Integer, Direction> ();
		this.ghostsDirections = new ArrayList<HashMap<Integer, Integer>> ();
		this.ghostsDesiredDirections = new ArrayList<HashMap<Integer, Integer>> ();
		
		
		Class_fit = evo_fit.class; //Class.forName(EnvConstant.CLASS_FITNESS);
		try {
			ObjClass_fit = Class_fit.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() 
	{
		evaluate(o, game);
	}
	
	   public boolean evaluate(Organism organism, PacmanGame game) 
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
			 tgt = new double[EnvConstant.NUMBER_OF_SAMPLES][EnvConstant.NR_UNIT_INPUT + MyConstants.SIM_TGT_OTHER_INFO_SIZE];
			 
		  
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
		  
			 if (EnvConstant.TYPE_OF_SIMULATION == EnvConstant.SIMULATION_FROM_CLASS)
			 {
			 // case of input from class java
				try 
				{
					 String mask6d = "  0.00000";
					 DecimalFormat fmt6d = new DecimalFormat(mask6d);
					 
					 Random rx = new Random();

					 Random ry = new Random();

					 Random rm = new Random();
					 
					 Random rvx = new Random();
					 
					 Random rvy = new Random();
					 
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
					 
					 long seedV_x = (long)(Math.random()*900);
					 rvx.setSeed(seedV_x);
					 long seedV_y = (long)(Math.random()*4000);
					 rvy.setSeed(seedV_y);
					 
					 HashMap<Integer,ArrayList<Double>> mappa= new HashMap<Integer,ArrayList<Double>>();
					 HashMap<Integer,ArrayList<Vector2d>> targetMap= new HashMap<Integer,ArrayList<Vector2d>>();
					 Map<Integer, Vector2d> bestPoints = new HashMap<Integer, Vector2d>();
					 
					 int maxRows = 35;
					 int maxCols = 30;
					 int minRows = 0;
					 int minCols = 0;
					   
				   for (count = 0; count < EnvConstant.NUMBER_OF_SAMPLES; count++) 
				   {   
					   ///IMPLEMENTAZIONE DECISIONE DIREZIONE   
					   
					   HashMap<Integer, Vector2d> ghost1 = new HashMap<Integer, Vector2d>();
					   HashMap<Integer, Vector2d> ghost2 = new HashMap<Integer, Vector2d>();
					   HashMap<Integer, Vector2d> ghost3 = new HashMap<Integer, Vector2d>();
					   HashMap<Integer, Vector2d> ghost4 = new HashMap<Integer, Vector2d>();
					   ghostsPositions.add(ghost1);
					   ghostsPositions.add(ghost2);
					   ghostsPositions.add(ghost3);
					   ghostsPositions.add(ghost4);
					   
					   HashMap<Integer, Integer> ghost1d = new HashMap<Integer, Integer>();
					   HashMap<Integer, Integer> ghost2d = new HashMap<Integer, Integer>();
					   HashMap<Integer, Integer> ghost3d = new HashMap<Integer, Integer>();
					   HashMap<Integer, Integer> ghost4d = new HashMap<Integer, Integer>();
					   ghostsDirections.add(ghost1d);
					   ghostsDirections.add(ghost2d);
					   ghostsDirections.add(ghost3d);
					   ghostsDirections.add(ghost4d);
					   
					   HashMap<Integer, Integer> ghost1dd = new HashMap<Integer, Integer>();
					   HashMap<Integer, Integer> ghost2dd = new HashMap<Integer, Integer>();
					   HashMap<Integer, Integer> ghost3dd = new HashMap<Integer, Integer>();
					   HashMap<Integer, Integer> ghost4dd = new HashMap<Integer, Integer>();
					   ghostsDesiredDirections.add(ghost1dd);
					   ghostsDesiredDirections.add(ghost2dd);
					   ghostsDesiredDirections.add(ghost3dd);
					   ghostsDesiredDirections.add(ghost4dd);
					   
					   int total_time = 0;
					   int perLive_time = 0;
					   
					   game.startGame();
					   
					   while (game.getLives() > 0)
					   {
						   while (game.state != State.PLAYING)
							   game.update();
						   
						   perLive_time = 0;
						   // PARAMETRI IN INPUT DA NORMALIZZARE!!!
						   
						   
//						   int newCol = minCols + in[0]*maxCOls; // CONVERSIONE DA VALORE TRA 0 e 1 A VALORE TRA MIN E MAX
						   
//						   in[5] = (game.getPacMan().getRow() - minRows)/maxRows; // CONVERSIONE DA VALORE TRA MIN E MAX A VALORE TRA 0 E 1
						   
						   
						   in[0] = (game.getPacMan().getRow() - minRows)/maxRows;	//PACMAN_X = PACMAN_ROW
						   in[1] = (game.getPacMan().getCol() - minCols)/maxCols;	//PACMAN_Y = PACMAN_COL
						   in[2] = (game.getPacMan().getRow() - minRows)/maxRows;	//PACMAN_X = PACMAN_ROW (AL TEMPO PRECEDENTE)
						   in[3] = (game.getPacMan().getCol() - minCols)/maxCols;	//PACMAN_Y = PACMAN_COL (AL TEMPO PRECEDENTE)
						   in[4] = (game.getGhosts().get(0).getRow() - minRows)/maxRows;	//GHOST1_X = GHOST1_ROW
						   in[5] = (game.getGhosts().get(0).getCol() - minCols)/maxCols;	//GHOST1_Y = GHOST1_COL
						   in[6] = (game.getGhosts().get(1).getRow() - minRows)/maxRows;	//GHOST2_X = GHOST2_ROW
						   in[7] = (game.getGhosts().get(1).getCol() - minCols)/maxCols;	//GHOST2_Y = GHOST2_COL
						   in[8] = (game.getGhosts().get(2).getRow() - minRows)/maxRows;	//GHOST3_X = GHOST3_ROW
						   in[9] = (game.getGhosts().get(2).getCol() - minCols)/maxCols;	//GHOST3_Y = GHOST3_COL
						   in[10] = (game.getGhosts().get(3).getRow() - minRows)/maxRows;	//GHOST4_X = GHOST4_ROW
						   in[11] = (game.getGhosts().get(3).getCol() - minCols)/maxCols;	//GHOST4_Y = GHOST4_COL
						   
						   tgt[count][0] = in[0];
						   tgt[count][1] = in[1];
						   tgt[count][2] = in[2];
						   tgt[count][3] = in[3];
						   tgt[count][4] = in[4];
						   tgt[count][5] = in[5];
						   tgt[count][6] = in[6];
						   tgt[count][7] = in[7];
						   tgt[count][8] = in[8];
						   tgt[count][9] = in[9];
						   tgt[count][10] = in[10];
						   tgt[count][11] = in[11];
						   
						   pacmanPositions.put(total_time, new Vector2d(game.getPacMan().row, game.getPacMan().col));
						   ghost1.put(total_time, new Vector2d(game.getGhosts().get(0).row, game.getGhosts().get(0).col));
						   ghost2.put(total_time, new Vector2d(game.getGhosts().get(1).row, game.getGhosts().get(1).col));
						   ghost3.put(total_time, new Vector2d(game.getGhosts().get(2).row, game.getGhosts().get(2).col));
						   ghost4.put(total_time, new Vector2d(game.getGhosts().get(3).row, game.getGhosts().get(3).col));
						   
//						   System.out.println("PACMAN_LIVES: " + game.getLives());

						   while (game.getState() != State.PACMAN_DIED)
						   {
//							   System.out.println("PACMAN_COL: " + game.getPacMan().col + " PACMAN_ROW: " + game.getPacMan().row);
//							   System.out.println("GHOST1_COL: " + game.getGhosts().get(0).col + " GHOST1_ROW: " + game.getGhosts().get(0).row);
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
							   
							   double dir = out[count][0];
							   
							   Direction direction = Direction.getDirection(dir);
							   
//							   System.out.println(direction);
							   
							   // EFFETTUARE LA MOSSA SCELTA DALLA RETE E UNA MOSSA PER OGNI GHOST (UN PASSO T)
							   
							   game.update(direction);
							   
							   // NON BASTA AGGIORNARE SOLO LE MOSSE!!!!!!!!!!!!!
							   // AGGIORNARE TUTTO IL GIOCO!!!!!!
							   
							   
//							   game.getPacMan().updatePlaying(direction);
							   
//							   for (int i = 0; i<game.getGhosts().size(); i++)
//							   {
//								   game.getGhosts().get(i).updatePlaying();
//							   }
							   
							   // AGGIORNAMENTO DELLE POSIZIONI
							   in[0] = (game.getPacMan().getRow() - minRows)/maxRows;	//PACMAN_X = PACMAN_ROW
							   in[1] = (game.getPacMan().getCol() - minCols)/maxCols;	//PACMAN_Y = PACMAN_COL
							   in[2] = (game.getPacMan().getRow() - minRows)/maxRows;	//PACMAN_X = PACMAN_ROW (AL TEMPO PRECEDENTE)
							   in[3] = (game.getPacMan().getCol() - minCols)/maxCols;	//PACMAN_Y = PACMAN_COL (AL TEMPO PRECEDENTE)
							   in[4] = (game.getGhosts().get(0).getRow() - minRows)/maxRows;	//GHOST1_X = GHOST1_ROW
							   in[5] = (game.getGhosts().get(0).getCol() - minCols)/maxCols;	//GHOST1_Y = GHOST1_COL
							   in[6] = (game.getGhosts().get(1).getRow() - minRows)/maxRows;	//GHOST2_X = GHOST2_ROW
							   in[7] = (game.getGhosts().get(1).getCol() - minCols)/maxCols;	//GHOST2_Y = GHOST2_COL
							   in[8] = (game.getGhosts().get(2).getRow() - minRows)/maxRows;	//GHOST3_X = GHOST3_ROW
							   in[9] = (game.getGhosts().get(2).getCol() - minCols)/maxCols;	//GHOST3_Y = GHOST3_COL
							   in[10] = (game.getGhosts().get(3).getRow() - minRows)/maxRows;	//GHOST4_X = GHOST4_ROW
							   in[11] = (game.getGhosts().get(3).getCol() - minCols)/maxCols;	//GHOST4_Y = GHOST4_COL
							   
							   tgt[count][0] = in[0];
							   tgt[count][1] = in[1];
							   tgt[count][2] = in[2];
							   tgt[count][3] = in[3];
							   tgt[count][4] = in[4];
							   tgt[count][5] = in[5];
							   tgt[count][6] = in[6];
							   tgt[count][7] = in[7];
							   tgt[count][8] = in[8];
							   tgt[count][9] = in[9];
							   tgt[count][10] = in[10];
							   tgt[count][11] = in[11];
							   
							   // SALVATAGGIO POSIZIONI RISPETTO AL TEMPO TOTALE
							   pacmanPositions.put(total_time, new Vector2d(game.getPacMan().row, game.getPacMan().col));
							   ghost1.put(total_time, new Vector2d(game.getGhosts().get(0).row, game.getGhosts().get(0).col));
							   ghost2.put(total_time, new Vector2d(game.getGhosts().get(1).row, game.getGhosts().get(1).col));
							   ghost3.put(total_time, new Vector2d(game.getGhosts().get(2).row, game.getGhosts().get(2).col));
							   ghost4.put(total_time, new Vector2d(game.getGhosts().get(3).row, game.getGhosts().get(3).col));
							   
							   pacmanDirections.put(total_time, direction);
							   
							   ghostsDirections.get(0).put(total_time, game.getGhosts().get(0).direction);
							   ghostsDirections.get(1).put(total_time, game.getGhosts().get(1).direction);
							   ghostsDirections.get(2).put(total_time, game.getGhosts().get(2).direction);
							   ghostsDirections.get(3).put(total_time, game.getGhosts().get(3).direction);
							   
							   ghostsDesiredDirections.get(0).put(total_time, game.getGhosts().get(0).desiredDirection);
							   ghostsDesiredDirections.get(1).put(total_time, game.getGhosts().get(1).desiredDirection);
							   ghostsDesiredDirections.get(2).put(total_time, game.getGhosts().get(2).desiredDirection);
							   ghostsDesiredDirections.get(3).put(total_time, game.getGhosts().get(3).desiredDirection);
							   
//						       System.out.println("COL: " + game.getGhosts().get(0).col + " ROW: " + game.getGhosts().get(0).row + " VALORE: " + game.maze[game.getGhosts().get(0).row][game.getGhosts().get(0).col]);
							   
							   // AGGIORNAMENTO DEL TEMPO PER_VITA (TEMPO INTESO COME PASSI)
							   perLive_time++; 
							   
							   // AGGIORNAMENTO DEL TEMPO TOTALE (TEMPO INTESO COME PASSI)
							   total_time++;
						   }
						   
						   game.init();
						   game.nextLife();
						   game.state = State.PLAYING;
						   
						   tgt[count][12] = Double.parseDouble(game.getScore());
					   }
				   }
				   
				} 
				
					catch (Exception e2) 
				   {
					  System.out.print("\n Error generic in Generation.input signal : err-code = \n" + e2); 
					  System.out.print("\n re-run this application when the class is ready\n\t\t thank! "); 
					  e2.printStackTrace();
					  System.exit(8);
				   }
			 
			 }  
		  
		  //success = true;
		  
		  
		  // control the result 
			 if (success) 
			 {
				 Map<Integer, Vector2d> bestPoints = new HashMap<Integer, Vector2d>();
				try 
				{
					//SIMULAZIONE DI LANCIO PER CALCOLO DISTANZA MINIMA TRAIETTORIA-BERSAGLIO
//					double minX = 20;
//					double maxX = 100;
//					double minY = 20;
//					double maxY = 100;
					
//					for (int i=0; i<EnvConstant.NUMBER_OF_SAMPLES; i++)
//					{
////						System.out.println("Prima: " + tgt[i][0] + " " + i);
//						bestPoints.put(i, computeMinDistanceMov(organism, i, tgt, bestTargetPreThrow.get(i), bestDistancePreThrow[i]));
////						System.out.println("Dopo: " + tgt[i][0] + " " + i);
//					}
////					
//					o.setBestPoints(bestPoints);
					
					
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
				   fit_dyn = Integer.parseInt(game.getScore());//arrayBest.get(MyConstants.FITNESS_TOTALE_INDEX);
				   
//				   System.out.println(fit_dyn);
				   
				   err_dyn = arrayBest.get(MyConstants.ERRORE_INDEX);
				   win_dyn = arrayBest.get(MyConstants.WIN_INDEX);
//				   angle = arrayBest.get(MyConstants.ANGOLO_INDEX);
//				   velocity = arrayBest.get(MyConstants.VELOCITA_INDEX);
//				   y_target = arrayBest.get(MyConstants.Y_TARGET_INDEX);
//				   x_target = arrayBest.get(MyConstants.X_TARGET_INDEX);
//				   total_err = arrayBest.get(MyConstants.ERRORE_TOTALE_INDEX);
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
//				organism.setOrig_fitness(fit_dyn);
				organism.setError(err_dyn);
				organism.setTotalError(total_err);
				organism.setAngle(angle);
				organism.setVelocity(velocity);
				organism.setYTarget(y_target);
				organism.setXTarget(x_target);
				organism.setMap(map);
				organism.setPacmanPositions(pacmanPositions);
				organism.setGhostsPositions(ghostsPositions);
				organism.setPacmanDirections(pacmanDirections);
				organism.setGhostsDirections(ghostsDirections);
				organism.setGhostsDesiredDirections(ghostsDesiredDirections);
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
		
}


