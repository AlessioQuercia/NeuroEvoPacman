package experiment;

import java.util.ArrayList;

public class evo_in 
{
	public static String[] inputNames = {"pacman_x", "pacman_y", "pacman_x (t-1)", "pacman_y(t-1)",
										"ghost1_x", "ghost1_y", "ghost2_x", "ghost2_y", "ghost3_x",
										"ghost3_y", "ghost4_x", "ghost4_y", "pacman_left", "pacman_right",
										"pacman_up", "pacman_down"
	};
	
	   public static int getNumSamples() { return 1; } 
	   
	   public static int getNumUnit()    { return 16; } 
	 
	   public static double getInput(double x_obj, double y_obj)
	   {
		   //double minY = 20;
		   //double maxY = 100;
		   double input = y_obj;  // /maxY;
		   return input; 
	   } 
	   
}
