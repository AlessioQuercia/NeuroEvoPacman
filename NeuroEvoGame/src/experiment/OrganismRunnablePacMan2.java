package experiment;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
import newGui.actor.Food;
import newGui.actor.Ghost;
import newGui.actor.Ghost.Mode;
import newGui.actor.PowerBall;
import newGui.infra.Game;
import pacmanGui.PacmanGame;
import pacmanGui.PacmanGame.State;

public class OrganismRunnablePacMan2 implements Runnable
{
	private Organism o;
	private PacmanGame game;
	
	private Map<Integer, Vector2d> pacmanPositions;
	private Map<Integer, Vector2d> pacmanCoordinates;
	private ArrayList<HashMap<Integer, Vector2d>> ghostsPositions;
	private Map<Integer, Integer> pacmanDirections;
	private Map<Integer, Direction> pacmanDesiredDirections;
	private ArrayList<HashMap<Integer, Integer>> ghostsDirections;
	private ArrayList<HashMap<Integer, Integer>> ghostsDesiredDirections;
	private Map<Integer, Integer> pacmanLefts;
	private Map<Integer, Integer> pacmanRights;
	private Map<Integer, Integer> pacmanUps;
	private Map<Integer, Integer> pacmanDowns;
	private Map<Integer, Integer> pacmanLefts2;
	private Map<Integer, Integer> pacmanRights2;
	private Map<Integer, Integer> pacmanUps2;
	private Map<Integer, Integer> pacmanDowns2;
	private Map<Integer, Food> pacmanNearestFoods;
	private Map<Integer, Double> pacmanLeftOutputs;
	private Map<Integer, Double> pacmanRightOutputs;
	private Map<Integer, Double> pacmanUpOutputs;
	private Map<Integer, Double> pacmanDownOutputs;
	private Map<Integer, Double> pacmanNoActionsOutputs;
	
	private Direction previousDirection;
	
	private LinkedList<Direction> lastDirections;
	
	// dynamic definition for fitness
		  Class  Class_fit;
		  Object ObjClass_fit;
		  Method Method_fit;
		  Object ObjRet_fit;
	
	public OrganismRunnablePacMan2(Organism o, PacmanGame game) 
	{
		this.o = o;
		this.game = game;
		
		this.pacmanPositions = new HashMap<Integer, Vector2d> ();
		this.pacmanCoordinates = new HashMap<Integer, Vector2d>();
		this.ghostsPositions = new ArrayList<HashMap<Integer, Vector2d>>();
		
		this.pacmanDirections = new HashMap<Integer, Integer> ();
		this.pacmanDesiredDirections = new HashMap<Integer, Direction> ();
		this.ghostsDirections = new ArrayList<HashMap<Integer, Integer>> ();
		this.ghostsDesiredDirections = new ArrayList<HashMap<Integer, Integer>> ();
		
		this.pacmanLefts = new HashMap<>();
		this.pacmanRights = new HashMap<>();
		this.pacmanUps = new HashMap<>();
		this.pacmanDowns = new HashMap<>();
		
		this.pacmanLefts2 = new HashMap<>();
		this.pacmanRights2 = new HashMap<>();
		this.pacmanUps2 = new HashMap<>();
		this.pacmanDowns2 = new HashMap<>();
		
		this.pacmanNearestFoods = new HashMap<>();
		
		this.pacmanLeftOutputs = new HashMap<>();
		this.pacmanRightOutputs = new HashMap<>();
		this.pacmanUpOutputs = new HashMap<>();
		this.pacmanDownOutputs = new HashMap<>();
		this.pacmanNoActionsOutputs = new HashMap<>();
		
		this.previousDirection = Direction.RIGHT;
		
		this.lastDirections = new LinkedList<Direction>();
		
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
					 
					 int maxRows = 35;
					 int maxCols = 30;
					 int minRows = 0;
					 int minCols = 0;
					 int minVal = 0;
					 int maxVal = 5;
					 int minMode = 0;
					 int maxMode = 3;
					 
					 int pacmanLeft = game.mazeCopy[game.getPacMan().row][game.getPacMan().col - 1];
					 int pacmanRight = game.mazeCopy[game.getPacMan().row][game.getPacMan().col + 1];
					 int pacmanUp = game.mazeCopy[game.getPacMan().row - 1][game.getPacMan().col];
					 int pacmanDown = game.mazeCopy[game.getPacMan().row + 1][game.getPacMan().col];
					 
					 double left = 0;
					 double right = 1;
					 double up = 0;
					 double down = 0;
					 double noAction = 0;
					 
					 int currentPowerPills = 4;
					 
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
					   
					   int stepsAsVulnerable = 0;
					   boolean startVulnerableMode = false;
					   
					   // START THE GAME
					   
					   game.startGame();
					   

					   // START A LIFE
					   
					   while (game.getLives() > 0)
					   {
						   
						   while (game.state != State.PLAYING)
						   {
							   game.update();
							   if (game.getLives() == 0)
								   break;
						   }
						   
						   if (game.lives == 0)
							   break;
						   
						   Food nearestFood = getNearestFood();
						   PowerBall nearestPowerUp = getNearesPowerUp();
						   
						   int previousCol[] = new int[4];
						   int previousRow[] = new int[4];
						   
						   Direction desiredDirection = Direction.RIGHT;
						   
						   
						   
						   // STORE EVERYTHING FOR THE REPLAY
						   
						   pacmanLefts.put(total_time, pacmanLeft);
						   pacmanRights.put(total_time, pacmanRight);
						   pacmanUps.put(total_time, pacmanUp);
						   pacmanDowns.put(total_time, pacmanDown);
						   
						   pacmanNearestFoods.put(total_time, nearestFood);
						   
//						   pacmanNearestPowerUps.put(total_time, nearestPowerUp);
						   
						   pacmanPositions.put(total_time, new Vector2d(game.getPacMan().row, game.getPacMan().col));
						   
						   pacmanCoordinates.put(total_time, new Vector2d(game.getPacMan().x, game.getPacMan().y));
						   
						   ghostsPositions.get(0).put(total_time, new Vector2d(game.getGhosts().get(0).x, game.getGhosts().get(0).y));
						   ghostsPositions.get(1).put(total_time, new Vector2d(game.getGhosts().get(1).x, game.getGhosts().get(1).y));
						   ghostsPositions.get(2).put(total_time, new Vector2d(game.getGhosts().get(2).x, game.getGhosts().get(2).y));
						   ghostsPositions.get(3).put(total_time, new Vector2d(game.getGhosts().get(3).x, game.getGhosts().get(3).y));
						   
						   pacmanDirections.put(total_time, game.getPacMan().direction);
						   pacmanDesiredDirections.put(total_time, desiredDirection);
						   
						   ghostsDirections.get(0).put(total_time, game.getGhosts().get(0).direction);
						   ghostsDirections.get(1).put(total_time, game.getGhosts().get(1).direction);
						   ghostsDirections.get(2).put(total_time, game.getGhosts().get(2).direction);
						   ghostsDirections.get(3).put(total_time, game.getGhosts().get(3).direction);
						   
						   ghostsDesiredDirections.get(0).put(total_time, game.getGhosts().get(0).desiredDirection);
						   ghostsDesiredDirections.get(1).put(total_time, game.getGhosts().get(1).desiredDirection);
						   ghostsDesiredDirections.get(2).put(total_time, game.getGhosts().get(2).desiredDirection);
						   ghostsDesiredDirections.get(3).put(total_time, game.getGhosts().get(3).desiredDirection);

						   
						   // STORE OUTPUTS
						   
						   pacmanLeftOutputs.put(total_time, left);
						   pacmanRightOutputs.put(total_time, right);
						   pacmanUpOutputs.put(total_time, up);
						   pacmanDownOutputs.put(total_time, down);
						   pacmanNoActionsOutputs.put(total_time, noAction);
						   
						   // UPDATE THE TOTAL TIME
						   total_time++;

						   
						   // START THE TRAINING
						   while (game.getState() != State.PACMAN_DIED) //== State.PLAYING)
						   {
							   previousCol[0] = game.getGhosts().get(0).col;
							   previousRow[0] = game.getGhosts().get(0).row;
							   previousCol[1] = game.getGhosts().get(1).col;
							   previousRow[1] = game.getGhosts().get(1).row;
							   previousCol[2] = game.getGhosts().get(2).col;
							   previousRow[2] = game.getGhosts().get(2).row;
							   previousCol[3] = game.getGhosts().get(3).col;
							   previousRow[3] = game.getGhosts().get(3).row;
							   
							   
							   pacmanLeft = game.mazeCopy[game.getPacMan().row][game.getPacMan().col - 1];
							   pacmanRight = game.mazeCopy[game.getPacMan().row][game.getPacMan().col + 1];
							   pacmanUp = game.mazeCopy[game.getPacMan().row - 1][game.getPacMan().col];
							   pacmanDown = game.mazeCopy[game.getPacMan().row + 1][game.getPacMan().col];
							   
							   // CHECK WHETHER AROUND PACMAN THERE ARE GHOSTS OR NOT
							   
							   for (Ghost g : game.getGhosts())
							   {
								   if (g.row == game.getPacMan().row && g.col == game.getPacMan().col - 1 && g.mode == Mode.NORMAL)
									   pacmanLeft = 4;
								   else if (g.row == game.getPacMan().row && g.col == game.getPacMan().col - 1 && g.mode == Mode.VULNERABLE)
									   pacmanLeft = 5;
								   if (g.row == game.getPacMan().row && g.col == game.getPacMan().col + 1 && g.mode == Mode.NORMAL)
									   pacmanRight = 4;
								   else if (g.row == game.getPacMan().row && g.col == game.getPacMan().col + 1 && g.mode == Mode.VULNERABLE)
									   pacmanRight = 5;
								   if (g.row == game.getPacMan().row - 1 && g.col == game.getPacMan().col && g.mode == Mode.NORMAL)
									   pacmanUp = 4;
								   else if (g.row == game.getPacMan().row - 1 && g.col == game.getPacMan().col && g.mode == Mode.VULNERABLE)
									   pacmanUp = 5;
								   if (g.row == game.getPacMan().row + 1 && g.col == game.getPacMan().col && g.mode == Mode.NORMAL)
									   pacmanDown = 4;
								   else if (g.row == game.getPacMan().row + 1 && g.col == game.getPacMan().col && g.mode == Mode.VULNERABLE)
									   pacmanDown = 5;
							   }
							   
							   nearestFood = getNearestFood();
							   nearestPowerUp = getNearesPowerUp(); 
							   
							   
							   
							   // SET THE INPUTS FOR THE NEURAL NETWORK
							   
							   in[0] = (game.getPacMan().row - minRows)/maxRows;	//PACMAN's ROW
							   in[1] = (game.getPacMan().col - minCols)/maxCols;	//PACMAN'S COL
							   in[2] = (pacmanLeft - minVal)/maxVal;	//PACMAN'S LEFT POSITION CONTENT
							   in[3] = (pacmanRight - minVal)/maxVal;	//PACMAN'S RIGHT POSITION CONTENT
							   in[4] = (pacmanUp - minVal)/maxVal;		//PACMAN'S UP POSITION CONTENT
							   in[5] = (pacmanDown - minVal)/maxVal;	//PACMAN'S DOWN POSITION CONTENT
							   in[6] = (nearestFood.row - minRows)/maxRows;		//NEAREST FOOD ROW
							   in[7] = (nearestFood.col - minCols)/maxCols;		//NEAREST FOOD COL
							   in[8] = (nearestPowerUp.row - minRows)/maxRows;		//NEAREST POWER UP ROW
							   in[9] = (nearestPowerUp.col - minCols)/maxCols;		//NEAREST POWER UP COL
							   in[10] = (game.getGhosts().get(0).getRow() - minRows)/maxRows;	//GHOST1_ROW
							   in[11] = (game.getGhosts().get(0).getCol() - minCols)/maxCols;	//GHOST1_COl
							   in[12] = (getMode(game.getGhosts().get(0).mode) - minMode)/maxMode;	//GHOST1_MODE
							   in[13] = (game.getGhosts().get(1).getRow() - minRows)/maxRows;	//GHOST2_ROW
							   in[14] = (game.getGhosts().get(1).getCol() - minCols)/maxCols;	//GHOST2_COl
							   in[15] = (getMode(game.getGhosts().get(1).mode) - minMode)/maxMode;	//GHOST2_MODE
							   in[16] = (game.getGhosts().get(2).getRow() - minRows)/maxRows;	//GHOST3_ROW
							   in[17] = (game.getGhosts().get(2).getCol() - minCols)/maxCols;	//GHOST3_COl
							   in[18] = (getMode(game.getGhosts().get(2).mode) - minMode)/maxMode;	//GHOST3_MODE
							   in[19] = (game.getGhosts().get(3).getRow() - minRows)/maxRows;	//GHOST4_ROW
							   in[20] = (game.getGhosts().get(3).getCol() - minCols)/maxCols;	//GHOST4_COl
							   in[21] = (getMode(game.getGhosts().get(3).mode) - minMode)/maxMode;	//GHOST4_MODE
							   
							   
							   
							   // load sensor   
							   _net.load_sensors(in);
							   
							   // UPDATE CURRENT POSITION
							   game.mazeCopy[game.getPacMan().row][game.getPacMan().col] = 0;
							   
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
							   
							   left = out[count][0];
							   right = out[count][1];
							   up = out[count][2];
							   down = out[count][3];
							   noAction = out[count][4];

							   
							   
							   // **** VINCOLI ****
							   
							   // SE L'AZIONE PORTA AD UN MURO, ALLORA LA AZZERO E NON LA CONSIDERO
							   if (pacmanLeft == 1)
								   left = -1;
							   if (pacmanRight == 1)
								   right = -1;
							   if (pacmanUp == 1)
								   up = -1;
							   if (pacmanDown == 1)
								   down = -1;
							   
							   // SE L'AZIONE PORTA AD UNA CELLA VUOTA E UNA DELLE ALTRE AZIONI POSSIBILI PORTEREBBE AD UN PUNTO, DIMEZZO IL SUO VALORE
							   if (pacmanLeft == 0 && ( (pacmanRight == 2 || pacmanRight == 3) || (pacmanUp == 2 || pacmanUp == 3) ||
									   (pacmanDown == 2 || pacmanDown == 3)))
								   left = left/2;
							   if (pacmanRight == 0 && ( (pacmanLeft == 2 || pacmanLeft == 3) || (pacmanUp == 2 || pacmanUp == 3) ||
									   (pacmanDown == 2 || pacmanDown == 3)))
								   right = right/2;
							   if (pacmanUp == 0 && ( (pacmanRight == 2 || pacmanRight == 3) || (pacmanLeft == 2 || pacmanLeft == 3) ||
									   (pacmanDown == 2 || pacmanDown == 3)))
								   up = up/2;
							   if (pacmanDown == 0 && ( (pacmanRight == 2 || pacmanRight == 3) || (pacmanUp == 2 || pacmanUp == 3) ||
									   (pacmanLeft == 2 || pacmanLeft == 3)))
								   down = down/2;
							   
							   // SE L'AZIONE PORTA AD UNA CELLA CON UN GHOST E PACMAN E' POTENZIATO, ALLORA METTO IL VALORE AD 1, ALTRIMENTI -1
//							   if ((pacmanLeft == 4) && game.getPacMan().canEatGhosts)
//								   left = 1;
//							   else if ((pacmanLeft == 4) && !game.getPacMan().canEatGhosts)
//								   left = -1;
//							   if ((pacmanRight == 4) && game.getPacMan().canEatGhosts)
//								   right = 1;
//							   else if ((pacmanRight == 4) && !game.getPacMan().canEatGhosts)
//								   right = -1;
//							   if ((pacmanUp == 4) && game.getPacMan().canEatGhosts)
//								   up = 1;
//							   else if ((pacmanUp == 4) && !game.getPacMan().canEatGhosts)
//								   up = -1;
//							   if ((pacmanDown == 4) && game.getPacMan().canEatGhosts)
//								   down = 1;
//							   else if ((pacmanDown == 4) && !game.getPacMan().canEatGhosts)
//								   down = -1;
							   
							   if (pacmanLeft == 5)
								   left = 1;
							   else if (pacmanLeft == 4)
								   left = -1;
							   if (pacmanRight == 5)
								   right = 1;
							   else if (pacmanRight == 4)
								   right = -1;
							   if (pacmanUp == 5)
								   up = 1;
							   else if (pacmanUp == 4)
								   up = -1;
							   if (pacmanDown == 5)
								   down = 1;
							   else if (pacmanDown == 4)
								   down = -1;
							   
							   // **** FINE VINCOLI ****
							   
							   
							   
							   // GET DIRECTION
							   
							   desiredDirection = Direction.getDirection(left, right, up, down, noAction, previousDirection, lastDirections);
							   
							   previousDirection = desiredDirection;
							   
							   if (lastDirections.size() == 15)
								   lastDirections.removeFirst();
							   
							   lastDirections.add(desiredDirection);

							   double previousX = game.getPacMan().x;
							   double previousY = game.getPacMan().y;	
							   
							   
							   
							   // STORE EVERYTHING FOR THE REPLAY
							   
							   pacmanLefts.put(total_time, pacmanLeft);
							   pacmanRights.put(total_time, pacmanRight);
							   pacmanUps.put(total_time, pacmanUp);
							   pacmanDowns.put(total_time, pacmanDown);
							   
							   pacmanNearestFoods.put(total_time, nearestFood);
							   
//							   pacmanNearestPowerUps.put(total_time, nearestPowerUp);
							   
							   pacmanPositions.put(total_time, new Vector2d(game.getPacMan().row, game.getPacMan().col));
							   
							   pacmanCoordinates.put(total_time, new Vector2d(game.getPacMan().x, game.getPacMan().y));
							   
							   ghostsPositions.get(0).put(total_time, new Vector2d(game.getGhosts().get(0).x, game.getGhosts().get(0).y));
							   ghostsPositions.get(1).put(total_time, new Vector2d(game.getGhosts().get(1).x, game.getGhosts().get(1).y));
							   ghostsPositions.get(2).put(total_time, new Vector2d(game.getGhosts().get(2).x, game.getGhosts().get(2).y));
							   ghostsPositions.get(3).put(total_time, new Vector2d(game.getGhosts().get(3).x, game.getGhosts().get(3).y));
							   
							   pacmanDirections.put(total_time, game.getPacMan().direction);
							   pacmanDesiredDirections.put(total_time, desiredDirection);
							   
							   ghostsDirections.get(0).put(total_time, game.getGhosts().get(0).direction);
							   ghostsDirections.get(1).put(total_time, game.getGhosts().get(1).direction);
							   ghostsDirections.get(2).put(total_time, game.getGhosts().get(2).direction);
							   ghostsDirections.get(3).put(total_time, game.getGhosts().get(3).direction);
							   
							   ghostsDesiredDirections.get(0).put(total_time, game.getGhosts().get(0).desiredDirection);
							   ghostsDesiredDirections.get(1).put(total_time, game.getGhosts().get(1).desiredDirection);
							   ghostsDesiredDirections.get(2).put(total_time, game.getGhosts().get(2).desiredDirection);
							   ghostsDesiredDirections.get(3).put(total_time, game.getGhosts().get(3).desiredDirection);

							   
							   // STORE OUTPUTS
							   
							   pacmanLeftOutputs.put(total_time, left);
							   pacmanRightOutputs.put(total_time, right);
							   pacmanUpOutputs.put(total_time, up);
							   pacmanDownOutputs.put(total_time, down);
							   pacmanNoActionsOutputs.put(total_time, noAction);
							   
							   

							   // GAME UPDATE
							   
							   game.update(desiredDirection, "NORMAL");
							   
							   
							   
							   // CHECK VULNERABLE MODE
							   
							   if (game.powerUpList.size() < currentPowerPills)
							   {
								   startVulnerableMode = true;
								   stepsAsVulnerable = 0;
								   currentPowerPills = game.powerUpList.size();
//								   for (Ghost g : game.getGhosts())
//									   g.setMode(Mode.VULNERABLE);
							   }
							   
							   if (startVulnerableMode)
							   {
								   stepsAsVulnerable++;
							   }
//							   else
//							   {
//								   for (Ghost g : game.getGhosts())
//									   if (g.mode == Mode.VULNERABLE)
//									   {
//										   startVulnerableMode = true;
//										   break;
//									   }
//							   }
							   
							   if (stepsAsVulnerable == 480)
							   {
								   for (Ghost g : game.getGhosts())
									   g.setMode(Mode.NORMAL);
								  
								   stepsAsVulnerable = 0;
								   startVulnerableMode = false;
								   game.getPacMan().canEatGhosts = false;
							   }
							   
//							   while (game.getState() == State.GHOST_CATCHED)
//								   game.update();
							   
							   
							   while (game.getState() == State.GHOST_CATCHED)
							   {
								   
								   total_time++;
								   
								   // GET DIRECTION
								   
								   desiredDirection = Direction.getDirection(left, right, up, down, noAction, previousDirection, lastDirections);
								   
								   previousDirection = desiredDirection;
								   
								   if (lastDirections.size() == 15)
									   lastDirections.removeFirst();
								   
								   lastDirections.add(desiredDirection);
								   
								   

								   // STORE EVERYTHING FOR THE REPLAY
								   
								   pacmanLefts.put(total_time, pacmanLeft);
								   pacmanRights.put(total_time, pacmanRight);
								   pacmanUps.put(total_time, pacmanUp);
								   pacmanDowns.put(total_time, pacmanDown);
								   
								   pacmanNearestFoods.put(total_time, nearestFood);
								   
//								   pacmanNearestPowerUps.put(total_time, nearestPowerUp);
								   
								   pacmanCoordinates.put(total_time, new Vector2d(game.getPacMan().x, game.getPacMan().y));
								   
								   pacmanPositions.put(total_time, new Vector2d(game.getPacMan().row, game.getPacMan().col));
								   
								   ghostsPositions.get(0).put(total_time, new Vector2d(game.getGhosts().get(0).x, game.getGhosts().get(0).y));
								   ghostsPositions.get(1).put(total_time, new Vector2d(game.getGhosts().get(1).x, game.getGhosts().get(1).y));
								   ghostsPositions.get(2).put(total_time, new Vector2d(game.getGhosts().get(2).x, game.getGhosts().get(2).y));
								   ghostsPositions.get(3).put(total_time, new Vector2d(game.getGhosts().get(3).x, game.getGhosts().get(3).y));
								   
								   pacmanDirections.put(total_time,  game.getPacMan().direction);
								   pacmanDesiredDirections.put(total_time, desiredDirection);
								   
								   ghostsDirections.get(0).put(total_time, game.getGhosts().get(0).direction);
								   ghostsDirections.get(1).put(total_time, game.getGhosts().get(1).direction);
								   ghostsDirections.get(2).put(total_time, game.getGhosts().get(2).direction);
								   ghostsDirections.get(3).put(total_time, game.getGhosts().get(3).direction);
								   
								   ghostsDesiredDirections.get(0).put(total_time, game.getGhosts().get(0).desiredDirection);
								   ghostsDesiredDirections.get(1).put(total_time, game.getGhosts().get(1).desiredDirection);
								   ghostsDesiredDirections.get(2).put(total_time, game.getGhosts().get(2).desiredDirection);
								   ghostsDesiredDirections.get(3).put(total_time, game.getGhosts().get(3).desiredDirection);

								   
								   // STORE OUTPUTS
								   
								   pacmanLeftOutputs.put(total_time, left);
								   pacmanRightOutputs.put(total_time, right);
								   pacmanUpOutputs.put(total_time, up);
								   pacmanDownOutputs.put(total_time, down);
								   pacmanNoActionsOutputs.put(total_time, noAction);
								   
								   
								   
								   // GAME UPDATE WHEN GHOST IS CATCHED
								   
								   game.update(desiredDirection, "GHOST_CATCHED");
								   
								// CHECK VULNERABLE MODE
								   
								   if (game.powerUpList.size() < currentPowerPills)
								   {
									   startVulnerableMode = true;
									   stepsAsVulnerable = 0;
									   currentPowerPills = game.powerUpList.size();
//									   for (Ghost g : game.getGhosts())
//										   g.setMode(Mode.VULNERABLE);
								   }

								   if (startVulnerableMode)
								   {
									   stepsAsVulnerable++;
								   }
//								   else
//								   {
//									   for (Ghost g : game.getGhosts())
//										   if (g.mode == Mode.VULNERABLE)
//										   {
//											   startVulnerableMode = true;
//											   break;
//										   }
//								   }
								   
								   if (stepsAsVulnerable == 480)
								   {
									   for (Ghost g : game.getGhosts())
										   g.setMode(Mode.NORMAL);
									  
									   stepsAsVulnerable = 0;
									   startVulnerableMode = false;
									   game.getPacMan().canEatGhosts = false;
								   }
							   }
							   
							   
							   // UPDATE TIMESTEP
							   
							   total_time++;
						   }
							   
						   
						   tgt[count][12] = Double.parseDouble(game.getHiscore());
					   }
					   
					   game.timestep = total_time;
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
				   
				   
				   // FITNESS UGUALE ALLO SCORE
				   fit_dyn = Integer.parseInt(game.getHiscore());//arrayBest.get(MyConstants.FITNESS_TOTALE_INDEX);
				   
//				   System.out.println(fit_dyn);
				   
				   
//				   // ERRORE IN BASE AL PUNTEGGIO MASSIMO CHE POTREBBE FARE IN UNA PARTITA DI PACMAN NORMALE
//				   err_dyn = Math.pow((3333360 - Integer.parseInt(game.getHiscore())), 2);	//arrayBest.get(MyConstants.ERRORE_INDEX);
				   
//				   // ERRORE IN BASE AL PUNTEGGIO MASSIMO SU UN SINGOLO LIVELLO AL QUADRATO
//				   err_dyn = Math.pow((12454 - Integer.parseInt(game.getHiscore())), 2);
				   
//				   // ERRORE IN BASE AL PUNTEGGIO MASSIMO SU UN SINGOLO LIVELLO
				   err_dyn = 12454 - Integer.parseInt(game.getHiscore());
				   
				   
				   // ERRORE IN BASE A QUANTI COLLEZIONABILI HA PRESO SUL TOTALE NEL LIVELLO
//				   double errore = (double)game.collectable_current_number/game.collectable_total_number;
//				   
//				   err_dyn = Math.pow(errore, 2);
				   
				   
				   
				   // FITNESS IN FUNZIONE DELL'ERRORE, NORMALIZZATA DA 
				   double k = 0.00001;
				   
				   double fitness2 = 1/(err_dyn + k);
				   
				   fit_dyn = fitness2;
				   
				   
				   
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
				organism.setPacmanCoordinates(pacmanCoordinates);
				organism.setGhostsPositions(ghostsPositions);
				organism.setPacmanDirections(pacmanDirections);
				organism.setPacmanDesiredDirections(pacmanDesiredDirections);
				organism.setGhostsDirections(ghostsDirections);
				organism.setGhostsDesiredDirections(ghostsDesiredDirections);
				
				organism.setPacmanLefts(pacmanLefts);
				organism.setPacmanRights(pacmanRights);
				organism.setPacmanUps(pacmanUps);
				organism.setPacmanDowns(pacmanDowns);
				
				organism.setPacmanLefts2(pacmanLefts2);
				organism.setPacmanRights2(pacmanRights2);
				organism.setPacmanUps2(pacmanUps2);
				organism.setPacmanDowns2(pacmanDowns2);
				
				organism.setPacmanNoActions(pacmanNoActionsOutputs);
				
				organism.setPacmanLeftOutputs(pacmanLeftOutputs);
				organism.setPacmanRightOutputs(pacmanRightOutputs);
				organism.setPacmanUpOutputs(pacmanUpOutputs);
				organism.setPacmanDownOutputs(pacmanDownOutputs);
				
				organism.setPacmanNearestFoods(pacmanNearestFoods);
				
				organism.setHighScore(Integer.parseInt(game.getHiscore()));
				

				
//				System.out.println(organism.getFitness());
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

	private int getMode(Mode mode) 
	{
		int modeInt = 0;
		
		if (mode == Mode.CAGE)
			modeInt = 0;
		else if (mode == Mode.NORMAL)
			modeInt = 1;
		else if (mode == Mode.VULNERABLE)
			modeInt = 2;
		else
			modeInt = 3;
		
		return modeInt;		
	}
	
	private Food getNearestFood() 
	{
		Food nearestFood = null;
		double minDistance = Double.POSITIVE_INFINITY;
		
		for (Food f : game.foodList)
		{
			double distance = Math.sqrt( Math.pow((f.x - game.getPacMan().x), 2) + Math.pow((f.y - game.getPacMan().y), 2) );
			if (distance < minDistance)
			{
				minDistance = distance;
				nearestFood = f;
			}
		}
		
		return nearestFood;
	}
	
	private PowerBall getNearesPowerUp() {
		PowerBall nearestPowerUp = null;
		double minDistance = Double.POSITIVE_INFINITY;
		
		for (PowerBall p: game.powerUpList)
		{
			double distance = Math.sqrt( Math.pow((p.x - game.getPacMan().x), 2) + Math.pow((p.y - game.getPacMan().y), 2) );
			if (distance < minDistance)
			{
				minDistance = distance;
				nearestPowerUp = p;
			}
		}
		
		return nearestPowerUp;
	}
		
}


