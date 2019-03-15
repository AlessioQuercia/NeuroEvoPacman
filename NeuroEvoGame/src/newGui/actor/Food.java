package newGui.actor;

import pacmanGui.PacmanActor;
import pacmanGui.PacmanGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Food class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Food extends PacmanActor {
    
	public int col;
    public int row;
    
    public Food(PacmanGame game) { super(game); }
    
    public Food(PacmanGame game, int col, int row) {
        super(game);
        this.col = col;
        this.row = row;
    }

    @Override
    public void init() {
        loadFrames("/res/food.png");
        x = col * 8 + 3 - 32;
        y = (row + 3) * 8 + 3;
        collider = new Rectangle(0, 0, 2, 2);
    }

    @Override
    public void updatePlaying() {
//        // for debug purpose A key clear level
//        if (Keyboard.keyPressed[KeyEvent.VK_A]) {
//            game.currentFoodCount = 0;
//        }
        
        if (game.checkCollision(this, Pacman.class) != null) {
            visible = false;
            game.currentFoodCount--;
//            System.out.println("Prima: " + game.foodList.size());
            game.foodList.remove(this);
//            System.out.println("Dopo: " + game.foodList.size());
            game.addScore(10);
            //System.out.println("current food count: " + game.currentFoodCount);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (!visible) {
            return;
        }
        g.setColor(Color.WHITE);
        g.fillRect((int) (x), (int) (y), 2, 2);
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
        if (game.getState() == PacmanGame.State.TITLE) {
            visible = false;
        }
        else if (game.getState() == PacmanGame.State.READY) {
            visible = true;
        }
    }

    public void hideAll() {
        visible = false;
    }
    
}
