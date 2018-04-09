package experiment;

import java.util.ArrayList;

public class evo_in 
{
	public static String[] inputNames = {"x_target", "y_target", "velocity", "angle", "force", "x_target(t-1)", "y_target(t-1)", "x_target(t-2)", "y_target(t-2)"};
	
	   public static int getNumSamples() { return 10; } 
	   
	   public static int getNumUnit()    { return 9; } 
	 
	   public static double getInput(double x_obj, double y_obj)
	   {
		   //double minY = 20;
		   //double maxY = 100;
		   double input = y_obj;  // /maxY;
		   return input; 
	   } 
	   
}
