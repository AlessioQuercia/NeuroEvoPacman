package newGui.infra;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.joml.Vector2d;

import common.Direction;
import common.MyConstants;
import newGui.actor.Ghost;
import newGui.actor.Pacman;

/**
 * Game class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Game implements Serializable
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Dimension screenSize;
    public Point2D screenScale;

	public List<Actor> actors = new ArrayList<Actor>();
    public BitmapFontRenderer bitmapFontRenderer = new BitmapFontRenderer("/res/font8x8.png", 16, 16);
    
    public int timestep;
    
    public int collectable_total_number;
    public int collectable_current_number;

    public void init() {
    }
    
    public void update() {
        for (Actor actor : actors) {
            actor.update();
        }
    }
    
    public void draw(Graphics2D g) {
        for (Actor actor : actors) {
            actor.draw(g);
        }
    }

    public <T> T checkCollision(Actor a1, Class<T> type) {
        a1.updateCollider();
        for(Actor a2 : actors) {
            a2.updateCollider();
            if (a1 != a2 
                && type.isInstance(a2)
                && a1.collider != null && a2.collider != null
                && a1.visible && a2.visible
                && a2.collider.intersects(a1.collider)) {
                    return type.cast(a2);
            }
        }
        return null;
    }

    public void broadcastMessage(String message) {
        for (Actor obj : actors) {
            try {
                Method method = obj.getClass().getMethod(message);
                if (method != null) {
                    method.invoke(obj);
                }
            } catch (Exception ex) {
            }
        }
    }

    public void drawText(Graphics2D g, String text, int x, int y) {
        bitmapFontRenderer.drawText(g, text, x, y);
    }
    
    public Dimension getScreenSize() {
		return screenSize;
	}

	public void setScreenSize(Dimension screenSize) {
		this.screenSize = screenSize;
	}

	public Point2D getScreenScale() {
		return screenScale;
	}

	public void setScreenScale(Point2D screenScale) {
		this.screenScale = screenScale;
	}

	public void reproduceSimulatedGame(int timestep, Map<Integer, Integer> pacmanDirections, Map<Integer, Direction> pacmanDesiredDirections, Map<Integer, Vector2d> pacmanPositions, 
			Map<Integer, Vector2d> pacmanCoordinates, ArrayList<HashMap<Integer, Integer>> ghostsDirections, 
			ArrayList<HashMap<Integer, Integer>> ghostsDesiredDirections, ArrayList<HashMap<Integer, Vector2d>> ghostsPositions)
	{
        for (Actor actor : actors) 
        {
        	if (actor instanceof Pacman)
        	{
        		Pacman p = (Pacman) actor;
        		p.reproduceSimulatedMove(timestep, pacmanDirections, pacmanDesiredDirections, pacmanPositions, pacmanCoordinates);
        	}
        	else if (actor instanceof Ghost)
        	{
        		Ghost g = (Ghost) actor;
        		g.reproduceSimulatedMove(timestep, ghostsDirections.get(g.type), ghostsDesiredDirections.get(g.type), ghostsPositions.get(g.type));
        	}
        	else
        		actor.update();
        }
	}
	
	public void reproduceSimulatedGameGhostCatched(Vector2d position, int timestep, Map<Integer, Vector2d> pacmanPositions, ArrayList<HashMap<Integer, Integer>> ghostsDirections, 
			ArrayList<HashMap<Integer, Integer>> ghostsDesiredDirections, ArrayList<HashMap<Integer, Vector2d>> ghostsPositions)
	{
        for (Actor actor : actors) 
        {
        	if (actor instanceof Pacman)
        	{
        		Pacman p = (Pacman) actor;
        		p.reproduceSimulatedMoveGhostCatched(position, timestep, pacmanPositions);
        	}
        	else if (actor instanceof Ghost)
        	{
        		Ghost g = (Ghost) actor;
        		g.reproduceSimulatedMove(timestep, ghostsDirections.get(g.type), ghostsDesiredDirections.get(g.type), ghostsPositions.get(g.type));
        	}
        	else
        		actor.update();
        }
	}
	
	public void update(Direction direction, String GhostCatched)
	{
        for (Actor actor : actors) 
        {
        	if (actor instanceof Pacman && GhostCatched.equals("NORMAL"))
        	{
        		Pacman p = (Pacman) actor;
        		p.visible = true;
        		p.updatePlaying(direction);
        	}
        	else if (actor instanceof Pacman && GhostCatched.equals("GHOST_CATCHED"))
        	{
        		Pacman p = (Pacman) actor;
        		p.visible = false;
        		p.updatePlaying(direction);
//        		p.updatePosition();
//        		p.updateCollider();
//        		p.updateAnimation();
        	}
        	else
        	{
        		actor.update();
        	}
        }
	}
    
}
