package gui;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.joml.Vector2d;

import common.MyConstants;
import experiment.evo_fit;
import jNeatCommon.EnvConstant;
import jneat.NNode;
import jneat.Network;
import jneat.Organism;

public class OrganismRunnableMovement implements Runnable
{
	private Organism o;
	private Map<Integer, Vector2d> bestTargetPreThrow;
	private double[] bestDistancePreThrow;
	
	// dynamic definition for fitness
		  Class  Class_fit;
		  Object ObjClass_fit;
		  Method Method_fit;
		  Object ObjRet_fit;
	
	public OrganismRunnableMovement(Organism o) 
	{
		this.o = o;
		
		this.bestDistancePreThrow = new double[EnvConstant.NUMBER_OF_SAMPLES];
		
		this.bestTargetPreThrow = new HashMap<Integer, Vector2d> ();
		
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
		evaluate(o);
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
			 
				
			double x0_sim_tgt = 0;
			double y0_sim_tgt = 0;
			double t0_sim_tgt = 0;
		  
			 if (EnvConstant.TYPE_OF_SIMULATION == EnvConstant.SIMULATION_FROM_CLASS)
			 {
			 
			 
			 // case of input from class java
			 
				try 
				{
					double minX = 20;
					double maxX = 80;
					double minY = 20;
					double maxY = 80;
					
					double minV_x = -5;
					double maxV_x = 10;
					double minV_y = -5;
					double maxV_y = 10;
					
					
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
					 
				   for (count = 0; count < EnvConstant.NUMBER_OF_SAMPLES; count++) 
				   {
					   ArrayList<Double> arrayForza = new ArrayList<Double> ();
					   ArrayList<Vector2d> arrayTarget = new ArrayList<Vector2d> ();
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
//					   double minX = 20;
//					   double maxX = 80;
//					   double minY = 20;
//					   double maxY = 80;
					   double minM = 1;
					   double maxM = 2;
					   double minF = -300;	// forza minima
					   double maxF = 600;	// forza massima
					   double maxA = 1.5708;
					   double minV = 0;
					   double maxV = 516;
					   
					   double d_minA = -0.031416;	//-1.8 gradi
					   double d_maxA = 0.062832;	//1.8 gradi (per 50 passi fa esattamente 90 gradi)
//					   double d_minA = -0.1036728;	//-5.94 gradi
//					   double d_maxA = 0.2073456;	//5.94 gradi (per 50 passi fa circa 297 gradi, permette quindi di alzare e abbassare durante la simulazione)
//					   double d_minA = 0;
//					   double d_maxA = 0.031416;	   
					   double d_minF = -20;
					   double d_maxF = 40;
					   
					   double massa = minM + rm.nextDouble()*maxM;	// 2kg
					   double v = 0;
					   double a = 0;
					   double F = 0;
					   
					   in[0] = rx.nextDouble();
					   in[1] = ry.nextDouble();
					   in[2] = v;
					   in[3] = a;
					   in[4] = F;
					   
					   tgt[count][0] = in[0];
					   tgt[count][1] = in[1];
					   tgt[count][2] = in[2];
//					   tgt[count][3] = in[3];
//					   tgt[count][4] = in[4];
					   tgt[count][3] = massa;
					   
					   double x_tgt = minX + in[0]*maxX;
					   double y_tgt = minY + in[1]*maxY;
					   arrayTarget.add(new Vector2d(x_tgt, y_tgt));
					   double d_x_tgt = 0.4;
					   double d_y_tgt = 0.4;
					   double x0_tgt = x_tgt;
					   double y0_tgt = y_tgt;
					   
					   double v_ret_x = minV_x + rvx.nextDouble()*maxV_x;
					   double v_ret_y = minV_y + rvy.nextDouble()*maxV_y;
					   
					   boolean done = false;
					   double sim_time = 15;
					   
					   while(!done)
					   {
						   double x_t = x0_tgt + v_ret_x*sim_time;
						   double y_t = y0_tgt + v_ret_y*sim_time;
						   
						   if (x_t >= 0 && y_t >= 0)
							   done = true;
						   
						   if (x_t < 0)
							   v_ret_x = minV_x + rvx.nextDouble()*maxV_x;
						   
						   if (y_t < 0) 
							   v_ret_y = minV_y + rvy.nextDouble()*maxV_y;
					   }
					   
					   tgt[count][MyConstants.SIM_VEL_RET_X_INDEX] = v_ret_x;
					   tgt[count][MyConstants.SIM_VEL_RET_Y_INDEX] = v_ret_y;
					   
					   
					   double delta_x_tgt = v_ret_x*delta_t;
					   double delta_y_tgt = v_ret_y*delta_t;
					   
					   double prev_x_tgt = x_tgt - delta_x_tgt;
					   double prev_y_tgt = y_tgt - delta_y_tgt;
					   double prev_prev_x_tgt = prev_x_tgt - delta_x_tgt;
					   double prev_prev_y_tgt = prev_y_tgt - delta_y_tgt;
							   
					   in[5] = (prev_x_tgt - minX)/maxX;
					   in[6] = (prev_y_tgt - minY)/maxY;
					   in[7] = (prev_prev_x_tgt - minX)/maxX;
					   in[8] = (prev_prev_y_tgt - minY)/maxY;
					   
					   
					   Vector2d origine = new Vector2d(0.0, 0.0);
					   
					   bestTargetPreThrow.put(count, new Vector2d(x_tgt, y_tgt));
					   bestDistancePreThrow[count] = origine.distance(bestTargetPreThrow.get(count));
					   
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
						   
						   if (F<-300) F = -300;
						   else if (F>300) F = 300;
						   if (a<0) a = 0;
						   else if (a>1.5708) a = 1.5708;
						   
						   
						   double acc = F/massa;
						   double delta_v = acc*delta_t;
						   
						   v += delta_v;
						   
						   if (v < 0) v = 0; 
						   
//						   x_tgt += d_x_tgt;
//						   y_tgt += d_y_tgt;
						   
						   //MOTO RETTILINEO UNIFORME
						   x_tgt = x0_tgt + v_ret_x*current_time;
						   y_tgt = y0_tgt + v_ret_y*current_time;
						   
						   Vector2d temp = new Vector2d(x_tgt, y_tgt);
						   
						   double dist = origine.distance(temp);
						   
						   if(dist < bestDistancePreThrow[count])
						   {
							   bestTargetPreThrow.put(count, new Vector2d(x_tgt, y_tgt));
							   bestDistancePreThrow[count] = dist;
						   }
						   
						   double V = (v - minV)/maxV;
						   double A = (a)/maxA;
						   double Freal = (F - minF)/maxF;
						   double X_tgt = (x_tgt - minX)/maxX;
						   double Y_tgt = (y_tgt - minY)/maxY;
						   
						   // AGGIORNAMENTO DELLE COORDINATE PRECEDENTI DEL TARGET
						   prev_x_tgt = x_tgt - delta_x_tgt;
						   prev_y_tgt = y_tgt - delta_y_tgt;
						   prev_prev_x_tgt = prev_x_tgt - delta_x_tgt;
						   prev_prev_y_tgt = prev_y_tgt - delta_y_tgt;

						   
						   in[0] = X_tgt;
						   in[1] = Y_tgt;
						   in[2] = V;
						   in[3] = A;
						   in[4] = Freal;
						   in[5] = (prev_x_tgt - minX)/maxX;
						   in[6] = (prev_y_tgt - minY)/maxY;
						   in[7] = (prev_prev_x_tgt - minX)/maxX;
						   in[8] = (prev_prev_y_tgt - minY)/maxY;
						   
						   
						   tgt[count][MyConstants.SIM_VEL_INDEX] = v;
						   tgt[count][MyConstants.SIM_ANGOLO_INDEX] = a;
						   tgt[count][MyConstants.SIM_FORZA_INDEX] = F;
						   tgt[count][MyConstants.SIM_TEMPO_INDEX] = current_time;
						   tgt[count][MyConstants.SIM_ACCELERAZIONE_INDEX] = acc;
						   
//						   double x_tgt = minX + tgt[count][0]*maxX;
//						   double y_tgt = minY + tgt[count][1]*maxY;
//						   
//						   x_tgt++;
//						   
//						   double X = (x_tgt - minX)/maxX;
//						   
//						   in[0] = X;
//						   tgt[count][0] = in[0];
						   
						   arrayForza.add(F);
						   arrayTarget.add(new Vector2d(x_tgt, y_tgt));
						   
						   if (lascia >= 0.5) 
						   {
							   break;
						   }
					   }
					   
					   double delta_x_sim = v*Math.cos(a)*delta_t;
					   
					   x0_sim_tgt = x0_tgt;
					   y0_sim_tgt = y0_tgt;
					   t0_sim_tgt = current_time;
					   
					   tgt[count][MyConstants.SIM_X0_TARGET_INDEX] = x_tgt;
					   tgt[count][MyConstants.SIM_Y0_TARGET_INDEX] = y_tgt;
					   tgt[count][MyConstants.SIM_T0_TARGET_INDEX] = current_time;
					   tgt[count][MyConstants.SIM_FIRST_X_TGT_INDEX] = x0_tgt;
					   tgt[count][MyConstants.SIM_FIRST_Y_TGT_INDEX] = y0_tgt;
					   
//					   bestPoints.put(count, computeMinDistanceMov(organism, count, tgt, x0_sim_tgt, y0_sim_tgt, t0_sim_tgt));
						
					   
					   // SIMULAZIONE DEL MOTO PARABOLICO DEL PROIETTILE E DEL MOTO RETTILINEO DEL BERSAGLIO (UNA VOLTA LANCIATO IL PROIETTILE)
//
//					   double x_sim = 0;
//					   double y_sim = 0;
//					   double t_sim = 0;
//					   Vector2d target = new Vector2d(x_tgt, y_tgt);
//					   Vector2d proiettile = new Vector2d(x_sim, y_sim);
//					   Vector2d bestPoint = proiettile;
//					   Vector2d bestTarget = target;
//					   double distanza = -1;
//					   double distanzaMinima = target.distance(proiettile);
//					   
////					   for (int i = (int)arrayTarget.get(arrayTarget.size()-1).x; i<150; i++) // i++ è sbagliato, dovrebbe essere i+=delta_X
////					   for (double i = arrayTarget.get(arrayTarget.size()-1).x; i<150; i+=delta_x_tgt) // i++ è sbagliato, dovrebbe essere i+=delta_X
////					   for (double i = x_sim; i<100; i+=delta_x_sim) // i++ è sbagliato, dovrebbe essere i+=delta_X
////					   for (int i = arrayTarget.size(); i<200; i++)
//					   for (double i = 0; i<10; i+=0.04)
//					   {
////						   x_tgt += d_x_tgt;
////						   y_tgt += d_y_tgt;
//						   
//						   //aggiorna tempi
//						   current_time += 0.04;
//						   t_sim += 0.04;
//						   
//						   //MOTO RETTILINEO UNIFORME TARGET
//						   if (x_tgt >= 0 && y_tgt >= 0)
//						   {
//							   x_tgt = x0_tgt + v_ret_x*current_time;
//							   y_tgt = y0_tgt + v_ret_y*current_time;
//						   }
//						   
////						   System.out.println(x_tgt + " " + y_tgt);
//						   arrayTarget.add(new Vector2d(x_tgt, y_tgt));
//						   
//						   //MOTO PARABOLICO PROIETTILE
//							// Aggiorna la posizione del corpo utilizzando le equazioni del moto per l'istante t
//							x_sim = v*Math.cos(a)*t_sim;
//							
//							y_sim = v*Math.sin(a)*t_sim - 0.5*MyConstants.GRAVITY*Math.pow(t_sim, 2);
//						   
//						 //CALCOLA DISTANZA MINIMA TRA DUE PUNTI (TARGET AL TEMPO T e PARABOLA AL TEMPO T)
//							target = new Vector2d(x_tgt, y_tgt);
//							proiettile = new Vector2d(x_sim, y_sim);
//							distanza = target.distance(proiettile);
//							
//							if(distanza < distanzaMinima)
//							{
//								distanzaMinima = distanza;
//								bestPoint = proiettile;
//								bestTarget = target;
////								bestPoints.put(count, bestPoint);
////								tgt[count][0] = bestTarget.x;
////								tgt[count][1] = bestTarget.y;
////								tgt[count][MyConstants.SIM_DISTANZA_MINIMA] = distanzaMinima;
////								tgt[count][MyConstants.SIM_X_MIGLIORE] = bestPoint.x;
////								tgt[count][MyConstants.SIM_Y_MIGLIORE] = bestPoint.y;
//							}
//					   }
					   
					   mappa.put(count, arrayForza);
					   targetMap.put(count, arrayTarget);
					   o.setForzaMap(mappa);
					   o.setTargetMap(targetMap);
					   
//					   bestPoints.put(count, bestPoint);
//					   tgt[count][MyConstants.SIM_BEST_TARGET_X_INDEX] = bestTarget.x;
//					   tgt[count][MyConstants.SIM_BEST_TARGET_Y_INDEX] = bestTarget.y;
//					   tgt[count][MyConstants.SIM_DISTANZA_MINIMA_INDEX] = distanzaMinima;
//					   tgt[count][MyConstants.SIM_X_MIGLIORE_INDEX] = bestPoint.x;
//					   tgt[count][MyConstants.SIM_Y_MIGLIORE_INDEX] = bestPoint.y;
//					   
//					   o.setBestPoints(bestPoints);
					   
					   
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
				   
//				   o.setBestPoints(bestPoints);
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
					
					for (int i=0; i<EnvConstant.NUMBER_OF_SAMPLES; i++)
					{
//						System.out.println("Prima: " + tgt[i][0] + " " + i);
						bestPoints.put(i, computeMinDistanceMov(organism, i, tgt, bestTargetPreThrow.get(i), bestDistancePreThrow[i]));
//						System.out.println("Dopo: " + tgt[i][0] + " " + i);
					}
//					
					o.setBestPoints(bestPoints);
					
					
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
//				organism.setOrig_fitness(fit_dyn);
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

	   
		private Vector2d computeMinDistance(Organism o, int i, double[][] tgt) 
		{
			double minX = 20;
			double maxX = 80;
			double minY = 20;
			double maxY = 80;
			double a = tgt[i][4];
			double v = tgt[i][2];
//			double x_tgt = minX + tgt[i][0]*maxX;
//			double y_tgt = minY + tgt[i][1]*maxY;
			double x_tgt = minX + tgt[i][0]*maxX;
			double y_tgt = minY + tgt[i][1]*maxY;
			
			// Fisso un punto P = (x, f(x)) sulla parabola,
			// creo la funzione distanza d(x) = |P - T|, dove T = (x_tgt, y_tgt) è il target,
			// calcolo i minimi di d(x) (che è equivalente a calcolare i minimi di d^2(x)
			
			double[] coeff_parabola = {-(MyConstants.GRAVITY/(2*Math.pow(v, 2)*Math.pow(Math.cos(a), 2))), Math.tan(a), 0};
			Funzione parabola = new Funzione(coeff_parabola);	// funzione f della parabola
			
//			System.out.println("f(x) = " + parabola);
			
			Vector2d target = new Vector2d(x_tgt, y_tgt);	// punto T (target)
//			Vector2d punto = new Vector2d(x, parabola.getValue(x));	// punto P fissato sulla parabola
			
			//Distanza tra due punti al quadrato (quindi scompare la radice)
//			double distance = Math.pow((punto.x-target.x), 2) + 
//					Math.pow((coeff_parabola[0]*Math.pow(x_tgt, 2) + coeff_parabola[1]*punto.x -target.y), 2);
			
			double c0 = coeff_parabola[0];	// (-coeff???)
			double c1 = coeff_parabola[1];
			
			double[] coeff_distanza = {};
			
			Funzione distanza = new Funzione(coeff_distanza);		// funzione d(x)
			
			double[] coeff_distanzaQuad = {
					Math.pow(c0, 2),								// a0 (coeff x^4)
					(2*c0*c1),										// a1 (coeff x^3)
					1 + Math.pow(c1, 2) - 2*c0*target.y,			// a2 (coeff x^2)
					- (2*target.x) - 2*c1*target.y,					// a3 (coeff x^1)
					Math.pow(target.x, 2) + Math.pow(target.y, 2)	// a4 (coeff x^0)
					};
			
			Funzione distanzaQuad = new Funzione(coeff_distanzaQuad);	// funzione d^2(x)
			
//			System.out.println("d^2(x) = " + distanzaQuad);
			
			Funzione derivata = distanzaQuad.getDerivativeFunction();
			
//			System.out.println("d^2(x)' = " + derivata);
			
//			double sol = derivata.computeThirdDegreeEquationFormaDepressa();
			
//			double distQuad = Math.abs(derivata.getValue(sol));
			
//			double dist = Math.sqrt(distQuad);
			
			String mask6d;
			DecimalFormat fmt6d;
			mask6d = "  0.000000000000";
			fmt6d = new DecimalFormat(mask6d);
			
//			System.out.println(Math.sqrt(distanzaQuad.getValue(sol)));	// Questa è la distanza minima corretta!
			
//			System.out.println("distQUad: " + fmt6d.format(distQuad));
			
//			System.out.println("dist: " + fmt6d.format(dist));
			
			
			//SBAGLIATA!! Quella corretta è quella successiva!
//			double minDist = Math.sqrt(derivata.computeEquationFormaDepressa());
			
			//il valore ottenuto da computeEquation è il valore della x della funzione derivata della distanza al quadrato
			// perciò di derivata. Per ottenere la distanza minima al quadrato devo calcolare d^2(x) con il valore della x ottenuto e poi fare
			// la radice quadrata per ottenere la distanza minima d(x)
			
//			double sol = derivata.computeThirdDegreeEquationFormaDepressa();
			
			double soluzioni_x[] = derivata.computeThirdDegreeEquationFormaDepressa();
			
			double distanzaMinima = Double.POSITIVE_INFINITY;
			
			Vector2d bestPoint = new Vector2d();
			
			for (int j = 0; j<soluzioni_x.length; j++)
			{
				Vector2d temp = new Vector2d(soluzioni_x[j], parabola.getValue(soluzioni_x[j]));
				
				double dist = temp.distance(target);
				if (dist < distanzaMinima)
				{
					distanzaMinima = dist;	
					bestPoint = temp;
				}
			}
			
//			double minDist = Math.sqrt(distanzaQuad.getValue(sol));		// d(x) minima
			
//			System.out.println("minDist = " + minDist);
			
			
//			System.out.println("a = "+ a + " v = "+ v);
//			double x = sol;
//			double y = parabola.getValue(x);
//			y = parabola.getValue(x);
			
//			System.out.println("distanza^2 = " + distanza.getValue(x));
//			System.out.println("distanza = " + Math.sqrt(distanza.getValue(x)));
//			
//			System.out.println(derivata.getValue(x));
//			System.out.println(Math.sqrt(derivata.getValue(x)));
//			System.out.println(parabola.getValue(x));
			
//			Vector2d bestPoint = new Vector2d(x, y);
			
//			System.out.println("x :" + bestPoint.x + " y: " + bestPoint.y);
			
			double bestDistance = target.distance(bestPoint);
			
//			System.out.println(bestDistance == minDist);
			
			double y_tiro = Math.tan(a)*x_tgt - ((MyConstants.GRAVITY/(2*Math.pow(v, 2)*Math.pow(Math.cos(a), 2)))*Math.pow(x_tgt, 2));
			
//			System.out.println(bestDistance <= Math.abs(y_tgt-y_tiro));
//			System.out.println("bestDistance = " + bestDistance + " minDist = " + minDist + " prevErr = " + Math.abs(y_tgt-y_tiro));
			
//			if (distanzaMinima > Math.abs(y_tgt-y_tiro))
//			{
//				System.out.println("##### INIZIO #####" + "\n" + 
//									"DELTA = " + derivata.getDelta() + "\n" + 
//									"bestPoint = (" + bestPoint.x + " ," + bestPoint.y + ")" + "\n" +
//									"bestDistance = " + bestDistance + "\n" +
//									"target = (" + x_tgt + " ," + y_tgt + ")" + "\n" +
//									"prevPoint = (" + x_tgt + " ," + y_tiro + ")" + "\n" +
//									"prevDistance = " + Math.abs(y_tgt-y_tiro) + "\n"
//									);
//			}
			
//			if (distanzaMinima != bestDistance)
//			{
//				System.out.println("##### INIZIO #####" + "\n" + 
//						"DistanzaMinima: " + distanzaMinima + "\n" +
//									"BestDistance: " + bestDistance + "\n");
//			}
			
//			tgt[i][8] = minDist;
			
			
			tgt[i][MyConstants.SIM_DISTANZA_MINIMA_INDEX] = distanzaMinima;
			tgt[i][MyConstants.SIM_X_MIGLIORE_INDEX] = bestPoint.x;
			tgt[i][MyConstants.SIM_Y_MIGLIORE_INDEX] = bestPoint.y;
			
			return bestPoint;
		}
		
		private Vector2d computeMinDistanceMovement(Organism o, int i, double[][] tgt) 
		{
			double minX = 20;
			double maxX = 80;
			double minY = 20;
			double maxY = 80;
			double a = tgt[i][4];
			double v = tgt[i][2];
			
			Vector2d bestPoint = new Vector2d(-1, -1);
			double distanzaMinima = Double.POSITIVE_INFINITY;
			double bestX_tgt = 0;
			double bestY_tgt = 0;
			
			for (int j=0; j<o.getTargetMap().get(i).size(); j++)
			{
					
	//			double x_tgt = minX + tgt[i][0]*maxX;
	//			double y_tgt = minY + tgt[i][1]*maxY;
				double x_tgt = o.getTargetMap().get(i).get(j).x;
				double y_tgt = o.getTargetMap().get(i).get(j).y;
				
				// Fisso un punto P = (x, f(x)) sulla parabola,
				// creo la funzione distanza d(x) = |P - T|, dove T = (x_tgt, y_tgt) è il target,
				// calcolo i minimi di d(x) (che è equivalente a calcolare i minimi di d^2(x)
				
				double[] coeff_parabola = {-(MyConstants.GRAVITY/(2*Math.pow(v, 2)*Math.pow(Math.cos(a), 2))), Math.tan(a), 0};
				Funzione parabola = new Funzione(coeff_parabola);	// funzione f della parabola
				
	//			System.out.println("f(x) = " + parabola);
				
				Vector2d target = new Vector2d(x_tgt, y_tgt);	// punto T (target)
	//			Vector2d punto = new Vector2d(x, parabola.getValue(x));	// punto P fissato sulla parabola
				
				//Distanza tra due punti al quadrato (quindi scompare la radice)
	//			double distance = Math.pow((punto.x-target.x), 2) + 
	//					Math.pow((coeff_parabola[0]*Math.pow(x_tgt, 2) + coeff_parabola[1]*punto.x -target.y), 2);
				
				double c0 = coeff_parabola[0];	// (-coeff???)
				double c1 = coeff_parabola[1];
				
				double[] coeff_distanza = {};
				
				Funzione distanza = new Funzione(coeff_distanza);		// funzione d(x)
				
				double[] coeff_distanzaQuad = {
						Math.pow(c0, 2),								// a0 (coeff x^4)
						(2*c0*c1),										// a1 (coeff x^3)
						1 + Math.pow(c1, 2) - 2*c0*target.y,			// a2 (coeff x^2)
						- (2*target.x) - 2*c1*target.y,					// a3 (coeff x^1)
						Math.pow(target.x, 2) + Math.pow(target.y, 2)	// a4 (coeff x^0)
						};
				
				Funzione distanzaQuad = new Funzione(coeff_distanzaQuad);	// funzione d^2(x)
				
	//			System.out.println("d^2(x) = " + distanzaQuad);
				
				Funzione derivata = distanzaQuad.getDerivativeFunction();
				
	//			System.out.println("d^2(x)' = " + derivata);
				
	//			double sol = derivata.computeThirdDegreeEquationFormaDepressa();
				
	//			double distQuad = Math.abs(derivata.getValue(sol));
				
	//			double dist = Math.sqrt(distQuad);
				
				String mask6d;
				DecimalFormat fmt6d;
				mask6d = "  0.000000000000";
				fmt6d = new DecimalFormat(mask6d);
				
	//			System.out.println(Math.sqrt(distanzaQuad.getValue(sol)));	// Questa è la distanza minima corretta!
				
	//			System.out.println("distQUad: " + fmt6d.format(distQuad));
				
	//			System.out.println("dist: " + fmt6d.format(dist));
				
				
				//SBAGLIATA!! Quella corretta è quella successiva!
	//			double minDist = Math.sqrt(derivata.computeEquationFormaDepressa());
				
				//il valore ottenuto da computeEquation è il valore della x della funzione derivata della distanza al quadrato
				// perciò di derivata. Per ottenere la distanza minima al quadrato devo calcolare d^2(x) con il valore della x ottenuto e poi fare
				// la radice quadrata per ottenere la distanza minima d(x)
				
	//			double sol = derivata.computeThirdDegreeEquationFormaDepressa();
				
				double soluzioni_x[] = derivata.computeThirdDegreeEquationFormaDepressa();
				
				double distanzaMin = Double.POSITIVE_INFINITY;
				
				Vector2d point = new Vector2d();
				
				for (int k = 0; k<soluzioni_x.length; k++)
				{
					Vector2d temp = new Vector2d(soluzioni_x[k], parabola.getValue(soluzioni_x[k]));
					
					double dist = temp.distance(target);
					if (dist < distanzaMin)
					{
						distanzaMin = dist;	
						point = temp;
					}
				}
				
				if (distanzaMin < distanzaMinima)
				{
					distanzaMinima = distanzaMin;
					bestPoint = point;
					bestX_tgt = o.getTargetMap().get(i).get(j).x;
					bestY_tgt = o.getTargetMap().get(i).get(j).y;
					bestX_tgt = (bestX_tgt - minX)/maxX;
					bestY_tgt = (bestY_tgt - minY)/maxY;
				}
				
	//			double minDist = Math.sqrt(distanzaQuad.getValue(sol));		// d(x) minima
				
	//			System.out.println("minDist = " + minDist);
				
				
	//			System.out.println("a = "+ a + " v = "+ v);
	//			double x = sol;
	//			double y = parabola.getValue(x);
	//			y = parabola.getValue(x);
				
	//			System.out.println("distanza^2 = " + distanza.getValue(x));
	//			System.out.println("distanza = " + Math.sqrt(distanza.getValue(x)));
	//			
	//			System.out.println(derivata.getValue(x));
	//			System.out.println(Math.sqrt(derivata.getValue(x)));
	//			System.out.println(parabola.getValue(x));
				
	//			Vector2d bestPoint = new Vector2d(x, y);
				
	//			System.out.println("x :" + bestPoint.x + " y: " + bestPoint.y);
				
				double bestDistance = target.distance(bestPoint);
				
	//			System.out.println(bestDistance == minDist);
				
				double y_tiro = Math.tan(a)*x_tgt - ((MyConstants.GRAVITY/(2*Math.pow(v, 2)*Math.pow(Math.cos(a), 2)))*Math.pow(x_tgt, 2));
				
			}

			
			
//			System.out.println(bestDistance <= Math.abs(y_tgt-y_tiro));
//			System.out.println("bestDistance = " + bestDistance + " minDist = " + minDist + " prevErr = " + Math.abs(y_tgt-y_tiro));
			
//			if (distanzaMinima > Math.abs(y_tgt-y_tiro))
//			{
//				System.out.println("##### INIZIO #####" + "\n" + 
//									"DELTA = " + derivata.getDelta() + "\n" + 
//									"bestPoint = (" + bestPoint.x + " ," + bestPoint.y + ")" + "\n" +
//									"bestDistance = " + bestDistance + "\n" +
//									"target = (" + x_tgt + " ," + y_tgt + ")" + "\n" +
//									"prevPoint = (" + x_tgt + " ," + y_tiro + ")" + "\n" +
//									"prevDistance = " + Math.abs(y_tgt-y_tiro) + "\n"
//									);
//			}
			
//			if (distanzaMinima != bestDistance)
//			{
//				System.out.println("##### INIZIO #####" + "\n" + 
//						"DistanzaMinima: " + distanzaMinima + "\n" +
//									"BestDistance: " + bestDistance + "\n");
//			}
			
//			tgt[i][8] = minDist;
			
			tgt[i][0] = bestX_tgt;
			tgt[i][1] = bestY_tgt;
			tgt[i][MyConstants.SIM_DISTANZA_MINIMA_INDEX] = distanzaMinima;
			tgt[i][MyConstants.SIM_X_MIGLIORE_INDEX] = bestPoint.x;
			tgt[i][MyConstants.SIM_Y_MIGLIORE_INDEX] = bestPoint.y;
			
			return bestPoint;
		}
		
		private Vector2d computeMinDistanceMov(Organism o, int i, double[][] tgt, Vector2d bestTargetPreThrow, double bestDistancePreThrow) 
		{
			double minX = 20;
			double maxX = 80;
			double minY = 20;
			double maxY = 80;
			double angle = tgt[i][4];
			double v = tgt[i][2];
			
			double v_ret_x = tgt[i][MyConstants.SIM_VEL_RET_X_INDEX];
			double v_ret_y = tgt[i][MyConstants.SIM_VEL_RET_Y_INDEX];
			
			double t_ret = tgt[i][MyConstants.SIM_T0_TARGET_INDEX];
			
			
//			double x_tgt = minX + tgt[i][0]*maxX;
//			double y_tgt = minY + tgt[i][1]*maxY;
//			double x_tgt = minX + tgt[i][0]*maxX;
//			double y_tgt = minY + tgt[i][1]*maxY;
			
			// Fisso un punto P = (x, f(x)) sulla parabola,
			// creo la funzione distanza d(x) = |P - T|, dove T = (x_tgt, y_tgt) è il target,
			// calcolo i minimi di d(x) (che è equivalente a calcolare i minimi di d^2(x)
			
			double[] coeff_parabola = {-(MyConstants.GRAVITY/(2*Math.pow(v, 2)*Math.pow(Math.cos(angle), 2))), Math.tan(angle), 0};
			Funzione parabola = new Funzione(coeff_parabola);	// funzione f della parabola
			
//			System.out.println("f(x) = " + parabola);
			
//			Vector2d target = new Vector2d(x_tgt, y_tgt);	// punto T (target)
//			Vector2d punto = new Vector2d(x, parabola.getValue(x));	// punto P fissato sulla parabola
			
			//Distanza tra due punti al quadrato (quindi scompare la radice)
//			double distance = Math.pow((punto.x-target.x), 2) + 
//					Math.pow((coeff_parabola[0]*Math.pow(x_tgt, 2) + coeff_parabola[1]*punto.x -target.y), 2);
			
			double c0 = coeff_parabola[0];	// (-coeff???)
			double c1 = coeff_parabola[1];
			
			double[] coeff_distanza = {};
			
			Funzione distanza = new Funzione(coeff_distanza);		// funzione d(x)
			
//			double t = 0;
			
			double a = tgt[i][MyConstants.SIM_X0_TARGET_INDEX];
			double b = v_ret_x;
			double c = tgt[i][MyConstants.SIM_Y0_TARGET_INDEX];
			double d = v_ret_y;
			double e = v*Math.cos(angle);
			double f = v*Math.sin(angle);
			double h = 0.5*MyConstants.GRAVITY;
			
//			double x_target = a + b*t;	//non serve, è solo per ricordare l'equazione per calcolare la x del target (in moto rettilineo uniforme)
			
//			double y_target = c + d*t;	//non serve, è solo per ricordare l'equazione per calcolare la y del target (in moto rettilineo uniforme)
			
//			double x_sim = e*t;	//non serve, è solo per ricordare l'equazione per calcolare la x del proiettile (in moto parabolico)

//			double y_sim = f*t + h*Math.pow(t, 2);	//non serve, è solo per ricordare l'equazione per calcolare la y del proiettile (in moto parabolico)
			
			double[] coeff_distanzaQuad = {
					Math.pow(h, 2),																				// a0 (coeff t^4)
					(-2*f*h + 2*d*h),																			// a1 (coeff t^3)
					Math.pow(b, 2) + Math.pow(e, 2) - 2*b*e + Math.pow(d, 2) + Math.pow(f, 2) + 2*c*h - 2*d*f,	// a2 (coeff t^2)
					2*a*b - 2*a*e + 2*c*d - 2*c*f,																// a3 (coeff t^1)
					Math.pow(a, 2) + Math.pow(c, 2)																// a4 (coeff t^0)
					};
			
			Funzione distanzaQuad = new Funzione(coeff_distanzaQuad);	// funzione d^2(x)
			
//			System.out.println("d^2(x) = " + distanzaQuad);
			
			Funzione derivata = distanzaQuad.getDerivativeFunction();
			
//			System.out.println("d^2(x)' = " + derivata);
			
//			double sol = derivata.computeThirdDegreeEquationFormaDepressa();
			
//			double distQuad = Math.abs(derivata.getValue(sol));
			
//			double dist = Math.sqrt(distQuad);
			
			String mask6d;
			DecimalFormat fmt6d;
			mask6d = "  0.000000000000";
			fmt6d = new DecimalFormat(mask6d);
			
//			System.out.println(Math.sqrt(distanzaQuad.getValue(sol)));	// Questa è la distanza minima corretta!
			
//			System.out.println("distQUad: " + fmt6d.format(distQuad));
			
//			System.out.println("dist: " + fmt6d.format(dist));
			
			
			//SBAGLIATA!! Quella corretta è quella successiva!
//			double minDist = Math.sqrt(derivata.computeEquationFormaDepressa());
			
			//il valore ottenuto da computeEquation è il valore della x della funzione derivata della distanza al quadrato
			// perciò di derivata. Per ottenere la distanza minima al quadrato devo calcolare d^2(x) con il valore della x ottenuto e poi fare
			// la radice quadrata per ottenere la distanza minima d(x)
			
//			double sol = derivata.computeThirdDegreeEquationFormaDepressa();
			
			double soluzioni_x[] = derivata.computeThirdDegreeEquationFormaDepressa();
			
			double distanzaMinima = Double.POSITIVE_INFINITY;
			
			Vector2d bestPoint = new Vector2d();
			
			Vector2d bestTarget = new Vector2d();
			
			double tempo = 0;
			
			for (int j = 0; j<soluzioni_x.length; j++)
			{
				double t = soluzioni_x[j];
				
				double x_target = a + b*(t);	//equazione per calcolare la x del target (in moto rettilineo uniforme)
				
				double y_target = c + d*(t);	//equazione per calcolare la y del target (in moto rettilineo uniforme)
				
				Vector2d target = new Vector2d(x_target, y_target);
						
				double x_sim = v*Math.cos(angle)*t;	//equazione per calcolare la x del proiettile (in moto parabolico)

				double y_sim = v*Math.sin(angle)*t - 0.5*MyConstants.GRAVITY*Math.pow(t, 2);	//equazione per calcolare la y del proiettile (in moto parabolico)
				
				Vector2d proiettile = new Vector2d(x_sim, y_sim);
				
//				System.out.println("TARGET: (" + target.x + ", " + target.y + ")" + "\n" +
//									"PROIETTILE: (" + proiettile.x + ", " + proiettile.y + ")");
				
				double dist = proiettile.distance(target);
				
				if (dist < distanzaMinima)
				{
					distanzaMinima = dist;	
					bestPoint = proiettile;
					bestTarget = target;
					tempo = t;
//					System.out.println(distanzaMinima);
				}
			}
			
//			System.out.println(tempo + " " + i);
			
			double dista = Math.sqrt(derivata.getValue(tempo));
			
//			System.out.println("****************************" + "\n" + 
//								"TEMPO: " + tempo + "\n" + 
//								"DISTANZA: " + distanzaMinima + "\n" +
//								"DIST: " + dista + "\n" + 
//								"TARGET: (" + bestTarget.x + ", " + bestTarget.y + ")" + "\n" +
//								"PROIETTILE: (" + bestPoint.x + ", " + bestPoint.y + ")" + "\n" +
//								"vel = " + v + "\n" + 
//								"angle = " + angle + "\n" + 
//								"e = " + e + "\n" + 
//								"f = " + f + "\n" +
//								"h = " + h + "\n" +
//								"****************************");
			
//			double minDist = Math.sqrt(distanzaQuad.getValue(sol));		// d(x) minima
			
//			System.out.println("minDist = " + minDist);
			
			
//			System.out.println("a = "+ a + " v = "+ v);
//			double x = sol;
//			double y = parabola.getValue(x);
//			y = parabola.getValue(x);
			
//			System.out.println("distanza^2 = " + distanza.getValue(x));
//			System.out.println("distanza = " + Math.sqrt(distanza.getValue(x)));
//			
//			System.out.println(derivata.getValue(x));
//			System.out.println(Math.sqrt(derivata.getValue(x)));
//			System.out.println(parabola.getValue(x));
			
//			Vector2d bestPoint = new Vector2d(x, y);
			
//			System.out.println("x :" + bestPoint.x + " y: " + bestPoint.y);
			
//			double bestDistance = bestTarget.distance(bestPoint);
			
//			System.out.println(bestDistance == minDist);
			
//			double y_tiro = Math.tan(angle)*x_tgt - ((MyConstants.GRAVITY/(2*Math.pow(v, 2)*Math.pow(Math.cos(angle), 2)))*Math.pow(x_tgt, 2));
			
//			System.out.println(bestDistance <= Math.abs(y_tgt-y_tiro));
//			System.out.println("bestDistance = " + bestDistance + " minDist = " + minDist + " prevErr = " + Math.abs(y_tgt-y_tiro));
			
//			if (distanzaMinima > Math.abs(y_tgt-y_tiro))
//			{
//				System.out.println("##### INIZIO #####" + "\n" + 
//									"DELTA = " + derivata.getDelta() + "\n" + 
//									"bestPoint = (" + bestPoint.x + " ," + bestPoint.y + ")" + "\n" +
//									"bestDistance = " + bestDistance + "\n" +
//									"target = (" + x_tgt + " ," + y_tgt + ")" + "\n" +
//									"prevPoint = (" + x_tgt + " ," + y_tiro + ")" + "\n" +
//									"prevDistance = " + Math.abs(y_tgt-y_tiro) + "\n"
//									);
//			}
			
//			if (distanzaMinima != bestDistance)
//			{
//				System.out.println("##### INIZIO #####" + "\n" + 
//						"DistanzaMinima: " + distanzaMinima + "\n" +
//									"BestDistance: " + bestDistance + "\n");
//			}
			
//			tgt[i][8] = minDist;
			
			if (bestPoint.x <= 0 && bestPoint.y <= 0)
			{
				bestPoint.x = 0;
				bestPoint.y = 0;
//				bestTarget.x = tgt[i][MyConstants.SIM_FIRST_X_TGT_INDEX];
//				bestTarget.y = tgt[i][MyConstants.SIM_FIRST_Y_TGT_INDEX];
//				distanzaMinima = bestPoint.distance(bestTarget);
			}
			
			if (bestDistancePreThrow < distanzaMinima)
			{
				bestPoint.x = 0;
				bestPoint.y = 0;
				bestTarget.x = bestTargetPreThrow.x;
				bestTarget.y = bestTargetPreThrow.y;
				distanzaMinima = bestDistancePreThrow;
			}
			
			tgt[i][MyConstants.SIM_BEST_TARGET_X_INDEX] = bestTarget.x;
			tgt[i][MyConstants.SIM_BEST_TARGET_Y_INDEX] = bestTarget.y;
			tgt[i][MyConstants.SIM_DISTANZA_MINIMA_INDEX] = distanzaMinima;
			tgt[i][MyConstants.SIM_X_MIGLIORE_INDEX] = bestPoint.x;
			tgt[i][MyConstants.SIM_Y_MIGLIORE_INDEX] = bestPoint.y;
			
			return bestPoint;
		}


		
}

