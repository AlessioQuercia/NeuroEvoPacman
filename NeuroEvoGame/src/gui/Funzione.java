package gui;

import org.joml.Vector2d;

public class Funzione 
{
	private int grado;
	private double[] coeff;
	private int deltaValue;
	private double angle;
	
	public Funzione(double [] coeff) 
	{
		this.grado = coeff.length-1;
		this.coeff = coeff;
	}
	
	public int getGrado() 
	{
		return grado;
	}

	public double[] getCoeff()
	{
		return coeff;
	}

	public double getValue(double x)
	{
		double value = 0;
		double grado = getGrado();
		
		for (int i=0; i<coeff.length; i++)
		{
			value += coeff[i]*Math.pow(x, grado--);
		}
		
		return value;
	}
	
	public double[] getDerivativeValue()
	{
		double[] coeff_der = new double[coeff.length-1];
		double grado = getGrado();
		
		for (int i=0; i<coeff.length-1; i++)
		{
			coeff_der[i] = coeff[i]*grado--;
		}
		
		return coeff_der;
	}
	
	public Funzione getDerivativeFunction()
	{
		return new Funzione(getDerivativeValue());
	}
	
	public double[] computeThirdDegreeEquationFormaDepressa()
	{
		Vector2d point = new Vector2d(0,0);
		
		double[] soluzioni_x = new double[3];
		
		double x = 0;
		double y = 0;
		
		double y1 = 0;
		double y2 = 0;
		double y3 = 0;
		
		double x1 = 0;
		double x2 = 0;
		double x3 = 0;
		
		//risoluzione funzione di terzo grado
		if (getGrado() == 3)
		{
			double a = coeff[0];
			double b = coeff[1];
			double c = coeff[2];
			double d = coeff[3];
			
			double p = (c/a) - ((Math.pow(b, 2))/(3*Math.pow(a, 2)));
			
			double q = (d/a) - ((b*c)/(3*Math.pow(a, 2))) + ((2*Math.pow(b, 3))/(27*Math.pow(a, 3)));
			
			double delta = (Math.pow(q, 2)/4) + (Math.pow(p, 3)/27);
			
//			double u = Math.pow(( (-q/2) + Math.sqrt( delta )), 1.0/3.0);
			
//			double v = Math.pow(( (-q/2) - Math.sqrt( delta )), 1.0/3.0);
			
			double u = Math.cbrt((-q/2) + Math.sqrt( delta ));
			
			double v = Math.cbrt((-q/2) - Math.sqrt( delta ));
			
//			System.out.println("u: " + u + " v: " + v);
//			System.out.println("p: " + p + " q: " + q);
			
			double[] coeff_temp = {1, 0, p, q};
			
			Funzione temp = new Funzione(coeff_temp);
			
//			System.out.println("temp = " + temp);
			
			if (delta > 0)
			{
//				System.out.println("DELTA > 0");
				
				deltaValue = +1;
				
				y1 = u + v;		// Soluzione reale
			}
			else if (delta == 0)
			{
//				System.out.println("DELTA = 0");
				
				deltaValue = 0;
				
				y1 = -2*Math.cbrt(q/2);	
				y2 = y3 = Math.cbrt(q/2);
			}
			else
			{
//				System.out.println("DELTA < 0");
				
				deltaValue = -1;
				
				double val = ( (Math.sqrt(-delta))/(-q/2) );
				
				double angolo = 0;
				
				if (-q/2 > 0)
				{
//					System.out.println("primo/quarto quadrante");
					angolo = Math.atan(val);
				}
				else if (-q/2 < 0)	
				{
//					System.out.println("secondo/terzo quadrante");
					angolo = Math.PI + Math.atan(val);
				}
				
				angle = angolo;
				
				// ( Math.sqrt(-p/3) ) o ( Math.sqrt(Math.abs(p)/3) ) ??? (dovrebbe essere la stessa cosa perché p è negativo
				y1 = 2*Math.sqrt(-p/3)*(Math.cos(angolo/3));				// soluzione reale positiva
				y2 = 2*Math.sqrt(-p/3)*(Math.cos((angolo + 2*Math.PI)/3));	// soluzione reale negativa
				y3 = 2*Math.sqrt(-p/3)*(Math.cos((angolo + 4*Math.PI)/3));	// soluzione reale negativa
				
//				System.out.println("y1 = " + y1 + " y2 = " + y2 + " y3 = " + y3);
			}
			
//			System.out.println("y1 = " + y1 + " y2 = " + y2 + " y3 = " + y3);
			
//			y = Math.max(Math.max(y1, y2), y3);
			
//			y = y1;
//			
//			x = y - b/(3*a);
			
			x1 = y1 - b/(3*a);
			x2 = y2 - b/(3*a);
			x3 = y3 - b/(3*a);
			
//			System.out.println("x1 = " + x1 + " x2 = " + x2 + " x3 = " + x3);
			
		}
		else
		{
			System.out.println("L'equazione non è di terzo grado!");
		}
		
		point.set(x, y);
		
//		System.out.println("x: " + x +" y: "+y);
		
		soluzioni_x[0] = x1;
		soluzioni_x[1] = x2;
		soluzioni_x[2] = x3;
		
		return soluzioni_x;
	}
	
	public double computeThirdDegreeEquation()
	{
		Vector2d point = new Vector2d(0,0);
		
		double x = 0;
		double y = 0;
		
		//risoluzione funzione di terzo grado
		if (getGrado() == 3)
		{
			double a = coeff[0];
			double b = coeff[1];
			double c = coeff[2];
			double d = coeff[3];
			
			double q = ((3*a*c) - Math.pow(b, 2))/(3*Math.pow(a, 2));
			
			double r = ((9*a*b*c) - (27*Math.pow(a, 2)*d) - (2*Math.pow(b, 3)))/(27*Math.pow(a, 3));
			
			double delta = (Math.pow(q, 3)/27) + (Math.pow(r, 2)/4);
			
//			double s = Math.pow(( (r/2) + Math.sqrt( delta )), 1.0/3.0);
//			
//			double t = Math.pow(( (r/2) - Math.sqrt( delta )), 1.0/3.0);
			
			double s = Math.cbrt((r/2) + Math.sqrt( delta ));
			
			double t = Math.cbrt((r/2) - Math.sqrt( delta ));
			
			if (delta > 0)
			{
				System.out.println("DELTA > 0");
				
			}
			else if (delta == 0)
			{
				System.out.println("DELTA = 0");
				
			}
			else
			{
				System.out.println("DELTA < 0");
			}
			
			x = s + t - b/(3*a);
		}
		return x;
	}
	
	public double computeFourthdegreeEquationFormaDepressa()
	{
		return computeFourthdegreeEquationFormaDepressa(0);
	}
	
	public double computeFourthdegreeEquationFormaDepressa(double valore)
	{
		double x = 0;
		double y = 0;
		
		double x1 = 0;
		double x2 = 0;
		double x3 = 0;
		double x4 = 0;
		
		if (getGrado() == 4)
		{
			double a = coeff[0];
			double b = coeff[1];
			double c = coeff[2];
			double d = coeff[3];
			double e = coeff[4] - valore;
			
//			System.out.println("a = " + a);
//			System.out.println("b = " + b);
//			System.out.println("c = " + c);
//			System.out.println("d = " + d);
//			System.out.println("e = " + e);
			
			double p = ( (8*a*c) - (3*Math.pow(b, 2)) )/( (8*Math.pow(a, 2)) );
			double q = ( (Math.pow(b, 3)) - (4*a*b*c) + (8*Math.pow(a, 2)*d) )/( (8*Math.pow(a, 3)) );
			
//			System.out.println("p = " + p + " q = " + q);
			
			double delta_0 = (Math.pow(c, 2)) - (3*b*d) + (12*a*e);
			double delta_1 = (2*Math.pow(c, 3)) - (9*b*c*d) + (27*Math.pow(b, 2)*e) + (27*a*Math.pow(d, 2)) - (72*a*c*e);
			
//			System.out.println("delta_0 = " + delta_0 + " delta_1 = " + delta_1);
			
			double num = delta_1 + ( Math.sqrt( (Math.pow(delta_1, 2) - (4*Math.pow(delta_0, 3))) ) );
			double Q = Math.cbrt( (num/2) );
			
			System.out.println((Math.pow(delta_1, 2) - (4*Math.pow(delta_0, 3))));
			
			System.out.println("num = " + num + " Q = " + Q);
			
			double val = (-2/3*p) + (1/(3*a))*(Q + (delta_0/Q));
			double S = Math.sqrt(val)/2;
			
			System.out.println("val = " + val + " S = " + S);
			
			x1 = -(b/(4*a)) - S + Math.sqrt(-4*Math.pow(S, 2) - 2*p + q/S)/2;
			x2 = -(b/(4*a)) - S - Math.sqrt(-4*Math.pow(S, 2) - 2*p + q/S)/2;
			x3 = -(b/(4*a)) - S + Math.sqrt(-4*Math.pow(S, 2) - 2*p - q/S)/2;
			x4 = -(b/(4*a)) - S - Math.sqrt(-4*Math.pow(S, 2) - 2*p - q/S)/2;
		}
		else
		{
			System.out.println("L'equazione non è di quarto grado!");
		}
		
		System.out.println("x1 = " + x1 + " x2 = " + x2);
		System.out.println("x3 = " + x3 + " x4 = " + x4);
		
		x = Math.max(Math.max(x1, x2), Math.max(x3, x4));
		
		return x;
	}
	
	public boolean checkIdentity(Vector2d vec)
	{
		double y = vec.y;
		double x = getValue(vec.x);
		
		System.out.println(vec.y + " = " + getValue(vec.x));
		
		return y == x;
	}
	
	@Override
	public String toString() 
	{
		String function = "";
		int grado = getGrado();
		
		for (int i=0; i<coeff.length; i++)
		{
			if(grado>0) function += "(" +coeff[i]+"*x^"+grado-- + ") + ";
			else function += coeff[i];
		}
		
		return function;
	}

	public int getDelta() 
	{
		return deltaValue;
	}
	
	public double getAngle()
	{
		return angle;
	}
}
