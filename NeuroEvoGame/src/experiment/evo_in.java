package experiment;

import java.util.ArrayList;

public class evo_in 
{
//	public static String[] inputNames = {"pacman_x", "pacman_y", "pacman_x (t-1)", "pacman_y(t-1)",
//										"ghost1_x", "ghost1_y", "ghost2_x", "ghost2_y", "ghost3_x",
//										"ghost3_y", "ghost4_x", "ghost4_y", "pacman_left", "pacman_right",
//										"pacman_up", "pacman_down"
//	};
	
//	public static String[] inputNames = {"pacman_left", "pacman_right",
//			"pacman_up", "pacman_down", "pacman_left2", "pacman_right2",
//			"pacman_up2", "pacman_down2", "coin_row", "coin_col"
//};
	
	public static String[] inputNames = {"pacman_row", "pacman_col", "pacman_left", "pacman_right", "pacman_up", "pacman_down", 
										"ghost1_row", "ghost1_col", "ghost1_mode", "ghost2_row", "ghost2_col", "ghost2_mode",
										"ghost3_row", "ghost3_col", "ghost3_mode", "ghost4_row", "ghost4_col", "ghost4_mode",
									    "food_row", "food_col", "powerup_row", "powerup_col"
};
	
	   public static int getNumSamples() { return 1; } 
	   
	   public static int getNumUnit()    { return 22; } 
	 
	   public static double getInput(double x_obj, double y_obj)
	   {
		   //double minY = 20;
		   //double maxY = 100;
		   double input = y_obj;  // /maxY;
		   return input; 
	   } 
	   
}
