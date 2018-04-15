package common;

import java.util.ArrayList;
import java.util.Random;

public class MyConstants 
{
	public static String RES_PATH = "C:/Users/Alessio/Documents/GitHub/NeuroEvoGame/NeuroEvoGame/res/";
	public static String TITLE = "NEAT Simulation GUI";
	public static int WIDTH = 650;
	public static int HEIGHT = 550;
	public static int OPTIONS_WIDTH = 220;
	public static int LANCIO_WIDTH = 520;
	
	/////COSTANTI PER IL CALCOLO DELLA FITNESS
	public static int NUMBER_OF_SAMPLES = 10;
	public static int FITNESS_CONSTANT = 200;
	public static int MAX_FITNESS_PER_ORGANISM = (int) Math.pow(FITNESS_CONSTANT, 2);
	public static int MAX_FITNESS = MAX_FITNESS_PER_ORGANISM*NUMBER_OF_SAMPLES;

	/////INDICI DELLE INFORMAZIONI SUL LANCIO
	public static int X_TARGET_INDEX = 0;
	public static int Y_TARGET_INDEX = 1;
	public static int Y_LANCIO_INDEX = 2;
	public static int ANGOLO_INDEX = 3;
	public static int VELOCITA_INDEX = 4;
	public static int ERRORE_INDEX = 5;
	public static int FITNESS_INDEX = 6;
	public static int FORZA_INDEX = 7;
	public static int TEMPO_INDEX = 8;
	public static int ACCELERAZIONE_INDEX = 9;
	public static int MASSA_INDEX = 10;
	public static int X_MIGLIORE_INDEX = 11;
	public static int Y_MIGLIORE_INDEX = 12;
	public static int BEST_TARGET_X_INDEX = 13;
	public static int BEST_TARGET_Y_INDEX = 14;
	public static int VEL_RET_X_INDEX = 15;
	public static int VEL_RET_Y_INDEX = 16;
	
	/////INDICI DELLE INFORMAZIONI SULLA RETE
	public static int ERRORE_TOTALE_INDEX = 17;
	public static int FITNESS_TOTALE_INDEX = 18;
	public static int FITNESS_VECCHIA_INDEX = 19;
	public static int LANCIO_MIGLIORE_INDEX = 20;
	public static int WIN_INDEX = 21;
	
	/////NUMERO INDICI
	public static int INFO_LANCIO_SIZE = 17;
	public static int INFO_RETE_SIZE = 22;
	
	
	/////INFORMAZIONI PER START
//	public static String COMPUTER_DIR = "C:\\Users\\Alessio\\Documents\\GitHub\\NeuroEvoGame\\NeuroEvoGame\\src\\gui\\";
	public static String DATA_DIR = "C:\\Users\\Alessio\\Documents\\GitHub\\NeuroEvoGame\\NeuroEvoGame\\src\\gui\\data\\";
	public static String RESULTS_DIR = "C:/Users/Alessio/Desktop/results/";
	public static String POPULATIONS_DIR = "C:/Users/Alessio/Desktop/populations/";
	public static String PARAMETRI_NOMEFILE = "C:\\Users\\Alessio\\Documents\\GitHub\\NeuroEvoGame\\NeuroEvoGame\\src\\gui\\data\\parameters";
	public static String GENOMA_NOMEFILE = DATA_DIR + "pacman_12x0x1";
	public static String POP_NOMEFILE = DATA_DIR + "primitive";
	public static String POPULATION_FILENAME = "C:\\Users\\Alessio\\Documents\\GitHub\\NeuroEvoGame\\NeuroEvoGame\\src\\gui\\data\\population_0";
	
	////GESTIONE UPDATE E RENDER
	public static long MILLISECOND = 1000L;
	public static long FPS = 60;
	
	////COSTANTI PARABOLA
	public static double GRAVITY = 9.81;
	public static int BORDER_X = 20;
	public static int BORDER_Y = 20;
	public static int ASSE_X = 150;
	public static int ASSE_Y = 150;	
	public static int TAIL_LENGTH = 15;
	public static int TARGET_TAIL_LENGTH = 50;
	
	////COSTANTI LOAD INPUT
	public static double LOADED_X;
	public static double LOADED_Y;
	public static boolean LOADED_INPUTS = false;
	
	////COSTANTI SETTINGS
	public static final String OTHER_SETTINGS = "otherSettings";
	public static final String DEFAULT_OTHER_SETTINGS = "defaultOtherSettings";
	public static final String DEFAULT_PARAMETERS = "defaultParameters";
	public static boolean[] SETTINGS_VALUES = null;
	public static final int SIM_AUTO_DRAW_INDEX = 0;
	public static final int SIM_SHOW_BEST_INDEX = 1;
	public static final int SIM_PHYSICS = 2;
	public static final int GRAPHS_AUTO_DRAW_INDEX = 3;
	public static final int GRAPHS_GRID_INDEX = 4;
	public static final int NET_AUTO_DRAW_INDEX = 5;
	
	////COSTANTI ID NODI
	public static ArrayList<Integer> INPUT_NODES_ID = new ArrayList<Integer>();
	public static ArrayList<Integer> BIAS_NODES_ID = new ArrayList<Integer>();
	public static ArrayList<Integer> OUTPUT_NODES_ID = new ArrayList<Integer>();
	
	////INDICI PER LA SIMULAZIONE DI LANCIO DELLA RETE (da salvare nell'array tgt[][])
	public static int SIM_X_TGT_INDEX = 0;
	public static int SIM_Y_TGT_INDEX = 1;
	public static int SIM_VEL_INDEX = 2;
	public static int SIM_MASSA_INDEX = 3;
	public static int SIM_ANGOLO_INDEX = 4;
	public static int SIM_FORZA_INDEX = 5;
	public static int SIM_ACCELERAZIONE_INDEX = 6;
	public static int SIM_TEMPO_INDEX = 7;
	public static int SIM_DISTANZA_MINIMA_INDEX = 8;
	public static int SIM_X_MIGLIORE_INDEX = 9;
	public static int SIM_Y_MIGLIORE_INDEX = 10;
	public static int SIM_VEL_RET_X_INDEX = 11;
	public static int SIM_VEL_RET_Y_INDEX = 12;
	public static int SIM_X0_TARGET_INDEX = 13;
	public static int SIM_Y0_TARGET_INDEX = 14;
	public static int SIM_T0_TARGET_INDEX = 15;
	public static int SIM_BEST_TARGET_X_INDEX = 16;
	public static int SIM_BEST_TARGET_Y_INDEX = 17;
	public static int SIM_FIRST_X_TGT_INDEX = 18;
	public static int SIM_FIRST_Y_TGT_INDEX = 19;
	
	//// NUMERO INDICI PER LA SIMULAZIONE (OLTRE AGLI INPUT)
	public static int SIM_TGT_OTHER_INFO_SIZE = 15;
	
	//// RANDOM PER I COEFFICIENTI ANGOLARI DEL MOTO RETTILINEO DEL TARGET
	public static Random RANDOM_VX = new Random((long) (Math.random()*900));
	public static Random RANDOM_VY = new Random((long) (Math.random()*4000));
	
	//// COSTANTI CALCOLO URTI
	public static double MASSA_TERRA = 5.972*Math.pow(10, 24);	// MASSA DELLA TERRA
	public static double VELOCITA_TERRA = 465.11;	//VELOCITA' DI ROTAZIONE DELLA TERRA
	public static double ATTRITO = 0.8;
}
