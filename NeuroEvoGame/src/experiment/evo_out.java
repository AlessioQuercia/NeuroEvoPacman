package experiment;

public class evo_out 
{
	public static String[] outputNames = {"LEFT", "RIGHT", "UP", "DOWN", "NOACTION"};
	
	   public static int getNumUnit() { return 5; } 
	   
	   public static double getTarget(double y_obj) 
	   { 
		   //double g = 2.8;				//costante gravitazionale (accelerazione di gravità)
		   //double a_tgt = 45 + Math.atan(y_obj/x_obj)/2; 	//angolo di tiro più efficiente (che permette di utilizzare la velicità minima)
		   //double v_tgt = x_obj*Math.cos(a_tgt)*Math.sqrt(g/2*(Math.tan(a_tgt)*x_obj-y_obj)); 	//velocità di lancio
		   //double [] target = {a_tgt,v_tgt};
		   //double maxY = 100;
		   double target = y_obj;//  *maxY;
		   return target;
	   } 
}
