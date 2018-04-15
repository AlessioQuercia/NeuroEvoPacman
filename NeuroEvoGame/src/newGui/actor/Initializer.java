package newGui.actor;

import pacmanGui.PacmanActor;
import pacmanGui.PacmanGame;
import pacmanGui.PacmanGame.State;

/**
 * Initializer class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Initializer extends PacmanActor {

    public Initializer(PacmanGame game) {
        super(game);
    }

    @Override
    public void updateInitializing() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 1000) {
                        break yield;
                    }
                    instructionPointer = 2;
                case 2:
//                    game.setState(State.OL_PRESENTS);
                    game.setState(State.TITLE);
                    break yield;
            }
        }
    }
    
}
