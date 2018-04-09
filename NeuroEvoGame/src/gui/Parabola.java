package gui;
import java.util.ArrayList;

public class Parabola 
{
	public static void main(String[] args) 
	{
		// ****** PARABOLA V1 ****** //
//		double y = 0.0;
//		double velocità = 10000;
//		double x = 100;
//		for (int a = 0; a<=90; a++)
//		{
//			double radianti = (a*Math.PI)/180;
//			System.out.println("angolo: "+a);
//			for(int v = 50; v<51; v++)
//			{
//				y = Math.tan(radianti)*x - ((9.81/(2*Math.pow(v, 2)*Math.pow(Math.cos(radianti), 2)))*Math.pow(x, 2));
//				if (y>20) System.out.println(y);
//			}
//			if (y>0) System.out.println("OK");
//			double val = (9.81*10000)/((Math.tan(radianti)-100)*2*Math.cos(radianti));
//			double velocità = Math.sqrt(val);
////			System.out.println(val);
//			System.out.println(velocità);
//		}
//		double rad = (15.5*Math.PI)/180;
//		double val = 2.8/(2*(Math.tan(rad)*100-100));
//		double min_vel = 100*Math.cos(rad)*Math.sqrt(val);
//		System.out.println(min_vel);
		
		
		// ****** PARABOLA V2 ****** //
//	 	double x_obj = 100;
//	 	double y_obj = 100;
//	 	double g = 9.81;
//	 	double m = 1;
//	 	double t = 0.5;
//	 	double y_tiro = 0;
//		for (int a = 0; a<=90; a++)
//		{
//			double radianti = (a*Math.PI)/180;
//			System.out.println("angolo: "+a);
//			for(int F =100; F<101; F++)
//			{
//			 	double acc = F/m;
//			 	double v = acc*t;
//				y_tiro = Math.tan(radianti)*x_obj - ((g/(2*Math.pow(v, 2)*Math.pow(Math.cos(radianti), 2)))*Math.pow(x_obj, 2));
//				if (y_tiro>=y_obj) 
//				{
//					System.out.println(y_tiro);
//				}
//			}
//		}
		
		
		prova();
		
	}
	
	public ArrayList<Double> calculateMinVel(double x, double y)
	{
		ArrayList<Double> array = new ArrayList<Double> ();
		double g = 9.81;
		double val = y/x;
		double beta = Math.atan(Math.toRadians(val));
		
		double a = 45 + beta/2;
		
		double ang = Math.toRadians(a);
		
//		double numeratore = g*Math.pow(x, 2);
//		double denominatore = 2*(Math.tan(ang)*x - y)*Math.pow(Math.cos(ang), 2);
		double numeratore = g;
		double denominatore = 2*(Math.tan(ang)*x - y);
		
		double vel = Math.sqrt((numeratore/denominatore)*Math.cos(ang)*x);
		
		array.add(ang);
		array.add(vel);
		
		return array;
	}
	
	public static void prova()
	{
		double delta_t = 0.04;
		double delta_F = 300;
		
		double F = 0;
		double m = 1;
		
		double acc = 0;
		double v = 0;
		
		for(int i=0; i<50; i++)
		{
			F += delta_F;
			if (F < -300) F = -300;
			if (F > 300) F = 300;
			acc = F/m;
			double delta_v = acc*delta_t;
			v += delta_v;
		}
		System.out.println("Forza: " + F);
		System.out.println("Velocità: " + v);
	}
				
}
