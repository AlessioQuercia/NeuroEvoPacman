package common;

public enum Direction 
{
	UP, DOWN, LEFT, RIGHT;
	
	public static Direction getDirection(double val)
	{
		Direction result = null;
		if (0 <= val && val < 0.25)
		{
			result = UP;
		}
		else if (0.25 <= val && val < 0.5)
		{
			result = DOWN;
		}
		else if (0.5 <= val && val < 0.75)
		{
			result = DOWN;
		}
		else if (0.75 <= val && val <= 1)
		{
			result = DOWN;
		}
		
		return result;
	}
}
