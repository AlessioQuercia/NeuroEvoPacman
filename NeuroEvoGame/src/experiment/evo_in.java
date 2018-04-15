package experiment;

import java.util.ArrayList;

public class evo_in 
{
	public static String[] inputNames = {"pacman_x", "pacman_y", "pacman_x (t-1)", "pacman_y(t-1)",
										"ghost1_x", "ghost1_y", "ghost2_x", "ghost2_y", "ghost3_x",
										"ghost3_y", "ghost4_x", "ghost4_y"
	};
	
	   public static int getNumSamples() { return 1; } 
	   
	   public static int getNumUnit()    { return 12; } 
	 
	   public static double getInput(double x_obj, double y_obj)
	   {
		   //double minY = 20;
		   //double maxY = 100;
		   double input = y_obj;  // /maxY;
		   return input; 
	   } 
	   
}
