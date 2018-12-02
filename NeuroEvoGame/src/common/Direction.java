package common;

import java.util.HashSet;
import java.util.LinkedList;

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
	
	public static Direction getDirection(double left, double right, double up, double down, double noAction, Direction previousDirection, LinkedList<Direction> lastDirections)
	{
		Direction result = null;
		
//		int leftCount = 0;
//		int rightCount = 0;
//		int upCount = 0;
//		int downCount = 0;
//		
//		for (Direction dire : lastDirections)
//		{
//			if (dire == Direction.LEFT)
//				leftCount++;
//			else if (dire == Direction.RIGHT)
//				rightCount++;
//			else if (dire == Direction.UP)
//				upCount++;
//			else if (dire == Direction.DOWN)
//				downCount++;
//		}
//		if (leftCount >= 8)
//			left = -1;
//		else if (rightCount >= 8)
//			right = -1;
//		else if (upCount >= 8)
//			up = -1;
//		else if (downCount >= 8)
//			down = -1;
		
		double dir = Math.max(Math.max(Math.max(left, right), Math.max(up, down)), noAction);
		
		if (dir == left) result = Direction.LEFT;
		else if (dir == right) result = Direction.RIGHT;
		else if (dir == up) result = Direction.UP;
		else if (dir == down) result = Direction.DOWN;
		else if (dir == noAction) result = previousDirection;
		
		
		return result;
	}
}
