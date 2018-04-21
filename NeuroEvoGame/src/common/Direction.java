package common;

import java.util.HashSet;

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
	
	public static Direction getDirection(double left, double right, double up, double down)
	{
		Direction result = null;
		
		double dir = Math.max(Math.max(left, right), Math.max(up, down));
		
		if (dir == left) result = Direction.LEFT;
		else if (dir == right) result = Direction.RIGHT;
		else if (dir == up) result = Direction.UP;
		else if (dir == down) result = Direction.DOWN;
		
		return result;
	}
}
