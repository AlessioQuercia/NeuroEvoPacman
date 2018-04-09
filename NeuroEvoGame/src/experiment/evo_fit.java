package experiment;

import java.util.ArrayList;
import java.util.HashMap;

import common.MyConstants;

public class evo_fit 
{
	  public static double getMaxFitness() { return 1000; }  //ovvero quando l'errore è 0,1
	  
	  public static HashMap<Integer, ArrayList<Double>>  computeFitness(int sample, double out[][], double tgt[][]) 
	  {     
		 //double d[] = new double[7];
		 HashMap<Integer,ArrayList<Double>> mappa= new HashMap<Integer,ArrayList<Double>>();
		 double minX = 20;	// x minima
		 double maxX = 80;	// x massima
		 double minY = 20;	// y minima
		 double maxY = 80;	// y massima
		 double minM = 1;	// massa minima
		 double maxM = 2;	// massa massima
		 double maxA = 1.5708;	// angolo massimo in radianti (corrisponde a 90 gradi)
//		 double minV = 40;	// velocità minima 20
//		 double maxV = 40;	// velocità massima 100 [50 + 50]
		 double minF = 15;	// forza minima
		 double maxF = 60;	// forza massima
		 double minT = 0.5;	// tempo minimo
		 double maxT = 1.5;	//tempo massimo
//		 double x_obj = minX + 0.4*maxX;
		 double g = 9.81;
		 int iter = -1;
		 double min_error = 100000.0;
		 double error = 0.0;
	     double errorsum = 0.0; 
	     double errorsumquad = 0.0;
 	 	 double errsum = 0;
 	 	 double vel_error = 0;
 	 	 double velsum = 0;
 	 	 double asum = 0;
 	 	 double a_error = 0;
	     double fitness = 0.0;
	     double fitness2 = 0.0;
	     double win = 0.0;
	     double k = 0.01;	//costante per poter calcolare la fitness anche quando l'errore è 0
	     
	     double prova = 0;
	     double throwFit = 0;
	     
	     double en_cinetica = 0;
	     
	     //d[2] = 0.0;
	     ArrayList<Double> arrayBest = new ArrayList<Double> ();
	     for (int i = 0; i<MyConstants.INFO_RETE_SIZE; i++)
	     {
	    	 arrayBest.add(0.0);
	     }
//	     System.out.println(array.size());
//	     System.out.println(arrayBest.size());
	     //mappa.put(10, 0.0);
	     //System.out.println("SAMPLE :   "+_sample);
	     
	     int bestThrowIndex = 0;
	     
	     double minV = 0;
	     double maxV = 75;
	     
	     for ( int j = 0; j < sample; j++) 
	        { 
//	    	 System.out.println("X_TARGET:    "+_tgt[j][0]);
//	    	 System.out.println("Y_TARGET:    "+_tgt[j][1]);
		     ArrayList<Double> array = new ArrayList<Double> ();
		     for (int i=0; i<MyConstants.INFO_LANCIO_SIZE; i++)
		    	 array.add(0.0);
		     
			    ///IMPLEMENTAZIONE DECISIONE DI LANCIO   
		     	double m = tgt[j][3];
				double x_obj = minX + tgt[j][0]*maxX;
				double y_obj = minY + tgt[j][1]*maxY;
				double v = tgt[j][2];
				
//	    	 	double t = tgt[j][4];
//	    	 	double acc = tgt[j][5];

	    	 	double a = tgt[j][4];
	    	 	double F = tgt[j][5];
	    	 	double lascia = out[j][2];
	    	 	
	    	 	double acc = tgt[j][6];
	    	 	double t = tgt[j][7];
	    	 	
	    	 	
	    	 	double x2 = tgt[j][MyConstants.SIM_X0_TARGET_INDEX] + tgt[j][MyConstants.SIM_VEL_RET_X_INDEX]*0.04;
	    	 	double y2 = tgt[j][MyConstants.SIM_Y0_TARGET_INDEX] + tgt[j][MyConstants.SIM_VEL_RET_Y_INDEX]*0.04;
	    	 	
//	    	 	System.out.println(F);
	    	 	
	    	 	// CONSIDERA IL BERSAGLIO IN POSIZIONE INIZIALE (ovvero nella prima posizione in cui esso compare)
//	    	 	mappa.put(sample+1, computeMinVel(x_obj, y_obj));
	    	 	
	    	 	// CONSIDERA IL BERSAGLIO NELLA POSIZIONE DELL'ISTANTE DI LANCIO (ovvero la posizione del target nel momento in cui il proiettile si stacca da terra)
	    	 	mappa.put(sample+1, computeMinVel(tgt[j][MyConstants.SIM_X0_TARGET_INDEX], tgt[j][MyConstants.SIM_Y0_TARGET_INDEX]));
	    	 	
	    	 	// CONSIDERA IL BERSAGLIO IN POSIZIONE MENO DISTANTE DAL PROIETTILE (ovvero quando il proiettile è già stato lanciato - sbagliato??)
//	    	 	double prova_x = tgt[j][MyConstants.SIM_BEST_TARGET_X_INDEX];
//	    	 	double prova_y = tgt[j][MyConstants.SIM_BEST_TARGET_Y_INDEX];
//	    	 	if (prova_x < 0) prova_x = 0;
//	    	 	if (prova_y < 0) prova_y = 0;
//	    	 	mappa.put(sample+1, computeMinVel(prova_x, prova_y));
	    	 	
//IMPLEMENTAZIONE VECCHIA		     
////		     array.add(0.0);
////		     for (int i = 0; i<7; i++)
////		     {
////		    	 array.add(0.0);
////		    	 arrayBest.add(0.0);
////		     }
////			     arrayBest.add(0.0);
//	    	 	double x_obj = minX + _tgt[j][0]*maxX;
//	    	 	double y_obj = minY + _tgt[j][1]*maxY;
//	    	 	double m = minM + _tgt[j][2]*maxM;
////	    	 	double x_obj = _tgt[j][0];
////	    	 	double y_obj = _tgt[j][1];
//	    	 	double a = _out[j][0]*maxA;
////	    	 	double v = minV + _out[j][1]*maxV;
//	    	 	double F = minF + _out[j][1]*maxF;
//	    	 	double t = minT + _out[j][2]*maxT;
////	    	 	System.out.println(x_obj);
////	    	 	System.out.println(y_obj);
////	    	 	System.out.println(a);
////	    	 	System.out.println(v);
////	    	 	d[3] = a;
////	    	 	d[4] = v;
////	    	 	d[5] = _tgt[j][1];	//y_obj
////	    	 	d[6] = _tgt[j][0];	//x_obj
//	    	 	//if (v>100) System.out.println("V");
//	    	 	//if (a>90) System.out.println("A");
//	    	 	//System.out.println("ANGOLO:  "+a);
//	    	 	//System.out.println("VELOCITA':   " + v);
//	    	 	double acc = F/m;
//	    	 	double v = acc*t;
	    	 	
	    	 	double y_tiro = Math.tan(a)*x_obj - ((g/(2*Math.pow(v, 2)*Math.pow(Math.cos(a), 2)))*Math.pow(x_obj, 2));
//	    	 	errorsum  += ( double ) (Math.abs(_tgt[j] - y_tiro));		//FITNESS VECCHIA
//	    	 	error = Math.abs(y_obj - y_tiro);	// DISTANZA Y
	    	 	error = Math.abs(tgt[j][8]);	//SIMULAZIONE TIRO (DISTANZA TRA DUE PUNTI)
//	    	 	if (error != 0)
//	    	 	{
//	    	 		System.out.println("MinDist = " + error + " vs PrevError = " + Math.abs(y_obj - y_tiro));
//	    	 		System.out.println(error <= Math.abs(y_obj-y_tiro));
//	    	 	}
//	    	 	System.out.println(Math.abs(y_obj-y_tiro));
	    	 	
	    	 	vel_error = Math.abs(v - mappa.get(sample + 1).get(1));
	    	 	
	    	 	double vel_2_error = Math.abs(Math.pow(v, 2) - mappa.get(sample + 1).get(1));
	    	 	
	    	 	a_error = Math.toDegrees(Math.abs(a - mappa.get(sample + 1).get(0)));
	    	 	
//	    	 	errorsum += Math.pow(error, 2);		//fitness_somma_quadrati
	    	 	errorsum += error;			//fitness_quadrato_somma
//	    	 	errorsumquad += Math.pow(error, 2);		//fitness_somma_quadrati
	    	 	
	    	 	velsum += vel_error;
	    	 	
	    	 	asum += a_error;

	    	 	errsum += error;
	    	 	
//	    	 	en_cinetica = 0.5*m*Math.pow(v, 2);
	    	 	
//	    	 	double best_throw_en_cinetica = 0.5*m*Math.pow(mappa.get(sample+1).get(1), 2);
	    	 	
//	    	 	double en_cinetica_error = Math.abs(en_cinetica - best_throw_en_cinetica);
	    	 	
//	    	 	System.out.println(Math.pow(vel_error, 2));
	    	 	
	    	 	throwFit = (MyConstants.FITNESS_CONSTANT - error) * (MyConstants.FITNESS_CONSTANT - vel_error);
	    	 	
//	    	 	throwFit = (MyConstants.FITNESS_CONSTANT - error) + (MyConstants.FITNESS_CONSTANT - vel_error);
	    	 	
//	    	 	throwFit = (20000 - error) * (20000 - en_cinetica_error);
	    	 	
//	    	 	throwFit = (20000 - error) * (20000 - en_cinetica);
	    	 	
//	    	 	throwFit = (MyConstants.FITNESS_CONSTANT - error) * (MyConstants.FITNESS_CONSTANT - v);
	    	 	
//	    	 	System.out.println(throwFit);
	    	 	
	    	 	prova += throwFit;
	    	 	//TIRO MIGLIORE
	    	 	if (error<min_error)
	    	 	{
	    	 		iter = j;
	    	 		bestThrowIndex = j;
	    	 		min_error = error;
	    	 		arrayBest.set(MyConstants.X_TARGET_INDEX, x_obj);
	    	 		arrayBest.set(MyConstants.Y_TARGET_INDEX, y_obj);
	    	 		arrayBest.set(MyConstants.Y_LANCIO_INDEX, y_tiro);
	    	 		arrayBest.set(MyConstants.ANGOLO_INDEX, a);
	    	 		arrayBest.set(MyConstants.VELOCITA_INDEX, v);
	    	 		arrayBest.set(MyConstants.ERRORE_INDEX, min_error);
		    	 	arrayBest.set(MyConstants.FITNESS_INDEX, throwFit);
		    	 	arrayBest.set(MyConstants.FORZA_INDEX, F);
		    	 	arrayBest.set(MyConstants.TEMPO_INDEX, t);
		    	 	arrayBest.set(MyConstants.ACCELERAZIONE_INDEX, acc);
		    	 	arrayBest.set(MyConstants.MASSA_INDEX, m);
		    	 	arrayBest.set(MyConstants.X_MIGLIORE_INDEX, tgt[j][MyConstants.SIM_X_MIGLIORE_INDEX]);
		    	 	arrayBest.set(MyConstants.Y_MIGLIORE_INDEX, tgt[j][MyConstants.SIM_Y_MIGLIORE_INDEX]);
		    	 	arrayBest.set(MyConstants.BEST_TARGET_X_INDEX, tgt[j][MyConstants.SIM_BEST_TARGET_X_INDEX]);
		    	 	arrayBest.set(MyConstants.BEST_TARGET_Y_INDEX, tgt[j][MyConstants.SIM_BEST_TARGET_Y_INDEX]);
		    	 	arrayBest.set(MyConstants.VEL_RET_X_INDEX, tgt[j][MyConstants.SIM_VEL_RET_X_INDEX]);
		    	 	arrayBest.set(MyConstants.VEL_RET_Y_INDEX, tgt[j][MyConstants.SIM_VEL_RET_Y_INDEX]);
	    	 	}
	    	 	fitness2 += 1/(error+k);
	    	 	//fitness += 1/(errorsum+k);		//FITNESS VECCHIA
//	    	 	System.out.println("LANCIO:  "+j);
//	    	 	System.out.println("ERRORE:  "+errorsum);
//	    	 	System.out.println("FITNESS:  "+fitness);
//	    	 	if (errorsum==0) {
//	    	 		System.out.println("SUPERWINNER");
//	    	 		d[2] = 2.00;
//	    	 	}
//	    	 	fitness = 1/errorsum;
//	    	 	System.out.println("ERRORE "+j+":      "+errorsum);
//	    	 	System.out.println("ANGOLO: "+a);
//	    	 	System.out.println("VELOCITA': "+v);
//	    	 	System.out.println("Y_TIRO:  "+y_tiro);
//	    	 	System.out.println("Y_OBJ:   "+_tgt[j]);
//	    	 	System.out.println("FITNESS:   "+fitness);
    	 		array.set(MyConstants.X_TARGET_INDEX, x_obj);
    	 		array.set(MyConstants.Y_TARGET_INDEX, y_obj);
    	 		array.set(MyConstants.Y_LANCIO_INDEX, y_tiro);
    	 		array.set(MyConstants.ANGOLO_INDEX, a);
    	 		array.set(MyConstants.VELOCITA_INDEX, v);
    	 		array.set(MyConstants.ERRORE_INDEX, error);
	    	 	array.set(MyConstants.FITNESS_INDEX, throwFit);
	    	 	array.set(MyConstants.FORZA_INDEX, F);
	    	 	array.set(MyConstants.TEMPO_INDEX, t);
	    	 	array.set(MyConstants.ACCELERAZIONE_INDEX, acc);
	    	 	array.set(MyConstants.MASSA_INDEX, m);
	    	 	array.set(MyConstants.X_MIGLIORE_INDEX, tgt[j][MyConstants.SIM_X_MIGLIORE_INDEX]);
	    	 	array.set(MyConstants.Y_MIGLIORE_INDEX, tgt[j][MyConstants.SIM_Y_MIGLIORE_INDEX]);
	    	 	array.set(MyConstants.BEST_TARGET_X_INDEX, tgt[j][MyConstants.SIM_BEST_TARGET_X_INDEX]);
	    	 	array.set(MyConstants.BEST_TARGET_Y_INDEX, tgt[j][MyConstants.SIM_BEST_TARGET_Y_INDEX]);
	    	 	array.set(MyConstants.VEL_RET_X_INDEX, tgt[j][MyConstants.SIM_VEL_RET_X_INDEX]);
	    	 	array.set(MyConstants.VEL_RET_Y_INDEX, tgt[j][MyConstants.SIM_VEL_RET_Y_INDEX]);
	    	 	mappa.put(j, array);
	        } 
//	     System.out.println("VEL: " + velsum + "    " + "ERR: " + errorsum);
	     
//	     fitness = 1000000 - Math.pow(errorsum, 3);		//fitness_cubo_somma
//	     fitness = 1000000 - Math.pow(errorsum, 2);		//fitness_quadrato_somma
//	     fitness = 100000 - errorsumquad;		//fitness_somma_quadrati

//	     fitness = 1000000000 - Math.pow((errorsum * velsum), 2);
	     
	     fitness = prova;
	     
//	     fitness = Math.pow(prova, 2);
	     
//	     d[0] = fitness; 
//	     d[1] = errorsum;
//	     if (iter>=0)
//	     {
//	    	 d[1] = min_error;
//	 		 d[3] = _out[iter][0]*maxA;
//	 	 	 d[4] = minV + _out[iter][1]*maxV;
//	 	 	 d[5] = _tgt[iter][1];	//y_obj
//	 	 	 d[6] = _tgt[iter][0];	//x_obj
//	     }
	     
	    //if (fitness>=500) win = 1.0;
 	 	 arrayBest.set(MyConstants.FITNESS_TOTALE_INDEX, fitness);
// 	 	 arrayBest.set(7, win);
 	 	 arrayBest.set(MyConstants.ERRORE_TOTALE_INDEX, errsum);
 	 	 arrayBest.set(MyConstants.FITNESS_VECCHIA_INDEX, fitness2);
 	 	 arrayBest.set(MyConstants.LANCIO_MIGLIORE_INDEX, (double)bestThrowIndex);
 	 	 arrayBest.set(MyConstants.WIN_INDEX, win);
 	 	 mappa.put(sample, arrayBest);
	     //d[2] = 0.0;
	     //if (fitness > 20) d[2] = 1;

	     return mappa; 
	  }
	  
		public static ArrayList<Double> computeMinVel(double x, double y)
		{
			ArrayList<Double> array = new ArrayList<Double> ();
			double g = 9.81;
			double coeff_ang_tgt = y/x;

			double beta = Math.atan(coeff_ang_tgt);
			
//			System.out.println(beta);
			
			double a = 45 + Math.toDegrees(beta)/2;
			
			double ang = Math.toRadians(a);
			
			double numeratore = g*Math.pow(x, 2);
			double denominatore = 2*(Math.tan(ang)*x - y)*Math.pow(Math.cos(ang), 2);
			
//			System.out.println(denominatore);
			
			double vel = Math.sqrt(numeratore/denominatore);
			
			array.add(ang);
			array.add(vel);
			
			return array;
		}
		
		public static ArrayList<Double> computeMinVelTargetMov(double x1, double y1, double x2, double y2)
		{
			ArrayList<Double> array = new ArrayList<Double> ();
			double g = 9.81;
			double coeff_ang_tgt = (y2-y1)/(x2-x1);

			double beta = Math.atan(coeff_ang_tgt);
			
//			System.out.println(beta);
			
			double a = 45 + Math.toDegrees(beta)/2;
			
			double ang = Math.toRadians(a);
			
			double numeratore = g*Math.pow(x1, 2);
			double denominatore = 2*(Math.tan(ang)*x1 - y1)*Math.pow(Math.cos(ang), 2);
			
//			System.out.println(denominatore);
			
			double vel = Math.sqrt(numeratore/denominatore);
			
			array.add(ang);
			array.add(vel);
			
			return array;
		}
}
