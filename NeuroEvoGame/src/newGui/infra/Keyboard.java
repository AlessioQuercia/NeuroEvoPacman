package newGui.infra;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;

/**
 * Keyboard class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Keyboard extends KeyAdapter implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static boolean[] keyPressed = new boolean[256];

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressed[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressed[e.getKeyCode()] = false;
    }
    
}
