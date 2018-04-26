package experiment;

import java.util.ArrayList;
import java.util.HashMap;

import common.MyConstants;


// POSSIBLE PACMAN FITNESS:
// 1) FITNESS = SCORE
// 2) FITNESS = SCORE, LIVES
// 3) FITNESS = SCORE, TOTAL_TIME
// 4) FITNESS = SCORE, LIVES, TOTAL_TIME


public class evo_fit 
{
	  public static double getMaxFitness() { return 1000; }  // ovvero quando l'errore è 0,1
	  
	  public static HashMap<Integer, ArrayList<Double>>  computeFitness(int sample, double out[][], double tgt[][]) 
	  {
		  HashMap<Integer,ArrayList<Double>> mappa= new HashMap<Integer,ArrayList<Double>>();
		  
		  ArrayList<Double> arrayBest = new ArrayList<Double> ();
		  for (int i = 0; i<MyConstants.INFO_RETE_SIZE; i++)
		  {
			  arrayBest.add(0.0);
		  }
		  
		  double direction;
		  
		  for ( int j = 0; j < sample; j++)
		  {
			  ArrayList<Double> array = new ArrayList<Double> ();
			  for (int i=0; i<MyConstants.INFO_LANCIO_SIZE; i++)
				  array.add(0.0);
			  
			  mappa.put(j, array);
		  }
		  
		  mappa.put(sample, arrayBest);
	 	 	 
		  return mappa; 
	  }
	  
}