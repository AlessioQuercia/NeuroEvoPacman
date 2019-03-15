package pacmanGui;

import java.io.Serializable;
import java.util.Map;

import org.joml.Vector2d;

import newGui.infra.Actor;

/**
 * PacmanActor class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class PacmanActor extends Actor<PacmanGame> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PacmanActor(PacmanGame game) {
        super(game);
    }

    @Override
    public void update() {
        switch (game.getState()) {
            case INITIALIZING: updateInitializing(); break;
//            case OL_PRESENTS: updateOLPresents(); break;
            case TITLE: updateTitle(); break;
            case READY: updateReady(); break;
            case READY2: updateReady2(); break;
            case PLAYING: updatePlaying(); break;
            case PACMAN_DIED: updatePacmanDied(); break;
            case GHOST_CATCHED: updateGhostCatched(); break;
            case LEVEL_CLEARED: updateLevelCleared(); break;
            case GAME_OVER: updateGameOver(); break;
        }
    }

    public void updateInitializing() {
    }

    public void updateOLPresents() {
    }

    public void updateTitle() {
    }

    public void updateReady() {
    }

    public void updateReady2() {
    }

    public void updatePlaying() {
    }

    public void updatePacmanDied() {
    }

    public void updateGhostCatched() {
    }

    public void updateLevelCleared() {
    }

    public void updateGameOver() {
    }

    // broadcast messages
    
    public void stateChanged() {
    }

	public void updateGhostCatched(Vector2d position) {
		// TODO Auto-generated method stub
		
	}
    
}
