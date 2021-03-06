package newGui.actor;

import newGui.infra.ShortestPathFinder;
import pacmanGui.PacmanActor;
import pacmanGui.PacmanGame;
import pacmanGui.PacmanGame.State;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector2d;

import common.Direction;

/**
 * Ghost class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Ghost extends PacmanActor {
    
    public Pacman pacman;
    public int type;
    public Point[] initialPositions = { 
        new Point(18, 11), new Point(16, 14), 
        new Point(18, 14), new Point(20, 14)};
    public int cageUpDownCount;

    public static enum Mode { CAGE, NORMAL, VULNERABLE, DIED }
    public Mode mode = Mode.CAGE;
    
    public int dx;
    public int dy;
    public int col;
    public int row;

	public int direction = 0;
    public int lastDirection;
    
    public List<Integer> desiredDirections = new ArrayList<Integer>();
    public int desiredDirection;
    public static final int[] backwardDirections = { 2, 3, 0, 1 };
    
    public long vulnerableModeStartTime;
    public boolean markAsVulnerable;
    
    private boolean startVuln;
    private int vulnCount;
    
    // in this version, i'm using path finder just to return the ghost to the center (cage)
    public ShortestPathFinder pathFinder; 
    
    public Ghost(PacmanGame game, Pacman pacman, int type) {
        super(game);
        this.pacman = pacman;
        this.type = type;
        this.pathFinder = new ShortestPathFinder(game.maze);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        modeChanged();
    }
    
    @Override
    public void init() {
        String[] ghostFrameNames = new String[8 + 4 + 4];
        for (int i=0; i<8; i++) {
            ghostFrameNames[i] = "/res/ghost_" + type + "_" + i + ".png";
        }
        for (int i=0; i<4; i++) {
            ghostFrameNames[8 + i] = "/res/ghost_vulnerable_" + i + ".png";
        }
        for (int i=0; i<4; i++) {
            ghostFrameNames[12 + i] = "/res/ghost_died_" + i + ".png";
        }
        loadFrames(ghostFrameNames);
        collider = new Rectangle(0, 0, 8, 8);
        setMode(Mode.CAGE);
    }
    
    private int getTargetX(int col) {
        return col * 8 - 3 - 32;
    }

    private int getTargetY(int row) {
        return (row + 3) * 8 - 2;
    }
    
    public int getTargetCol(int x)
    {
    	return (3 + 32 + x)/8;
    }
    
    public int getTargetRow(int y)
    {
    	return (2 + y - 24)/8;
    }

    public void updatePosition() {
        x = getTargetX(col);
        y = getTargetY(row);
    }
    
    private void updatePosition(int col, int row) {
        this.col = col;
        this.row = row;
        updatePosition();
    }
    
    private boolean moveToTargetPosition(int targetX, int targetY, int velocity) {
        int sx = (int) (targetX - x);
        int sy = (int) (targetY - y);
        int vx = Math.abs(sx) < velocity ? Math.abs(sx) : velocity;
        int vy = Math.abs(sy) < velocity ? Math.abs(sy) : velocity;
        int idx = vx * (sx == 0 ? 0 : sx > 0 ? 1 : -1);
        int idy = vy * (sy == 0 ? 0 : sy > 0 ? 1 : -1);
        x += idx;
        y += idy;
        return sx != 0 || sy != 0;
    }
    
    private boolean moveToTargetPosition1(int targetX, int targetY, int velocity) {
        int sx = (int) (targetX - x);
        int sy = (int) (targetY - y);
        int vx = velocity;
        int vy = velocity;
        int idx = vx * sx;
        int idy = vy * sy;
        x += idx;
        y += idy;
        return sx != 0 || sy != 0;
    }

    private boolean moveToGridPosition(int col, int row, int velocity) {
        int targetX = getTargetX(col);
        int targetY = getTargetY(row);
        return moveToTargetPosition(targetX, targetY, velocity);
    }
    
    private void adjustHorizontalOutsideMovement() {
        if (col == 1) {
            col = 34;
            x = getTargetX(col);
        }
        else if (col == 34) {
            col = 1;
            x = getTargetX(col);
        }
    }
    
    @Override
    public void updateTitle() {
        int frameIndex = 0;
        x = pacman.x + 17 + 17 * type;
        y = 200;
        if (pacman.direction == 0) {
            frameIndex = 8 + (int) (System.nanoTime() * 0.00000001) % 2;
        }
        else if (pacman.direction == 2) {
            frameIndex = 2 * pacman.direction + (int) (System.nanoTime() * 0.00000001) % 2;
        }
        frame = frames[frameIndex];
    }
    
    @Override
    public void updatePlaying() {
        switch (mode) {
            case CAGE: updateGhostCage(); break;
            case NORMAL: updateGhostNormal(); break;
            case VULNERABLE: updateGhostVulnerable(); break;
            case DIED: updateGhostDied(); break;
        }
        updateAnimation();
    }

    public void updateAnimation() {
        int frameIndex = 0;
        switch (mode) {
            case CAGE: 
            case NORMAL:
            	startVuln = false;
                frameIndex = 2 * direction + (int) (System.nanoTime() * 0.00000001) % 2;
                if (!markAsVulnerable) {
                    break;
                }
            case VULNERABLE:
            	if (!startVuln)
            	{
            		startVuln = true;
            		vulnCount = 0;
            	}
                if (startVuln && vulnCount > 85/*System.currentTimeMillis() - vulnerableModeStartTime > 5000*/) {
                    frameIndex = 8 + (int) (System.nanoTime() * 0.00000002) % 4;
                }
//                else
                else if (startVuln && vulnCount <= 85)
                {
                    frameIndex = 8 + (int) (System.nanoTime() * 0.00000001) % 2;
                }
                
                vulnCount++;
                
                break;
            case DIED:
            	startVuln = false;
            	vulnCount = 0;
                frameIndex = 12 + direction;
                break;
        }
        frame = frames[frameIndex];
    }
    
    public void updateAnimation(int dir, int desiredDir, Vector2d position) {
        int frameIndex = 0;
        switch (mode) {
            case CAGE: 
            case NORMAL:
            	startVuln = false;
                frameIndex = 2 * dir + (int) (System.nanoTime() * 0.00000001) % 2;
                if (!markAsVulnerable) {
                    break;
                }
            case VULNERABLE:
            	if (!startVuln)
            	{
            		startVuln = true;
            		vulnCount = 0;
            	}
                if (startVuln && vulnCount > 85/*System.currentTimeMillis() - vulnerableModeStartTime > 5000*/) {
                    frameIndex = 8 + (int) (System.nanoTime() * 0.00000002) % 4;
                }
                else if (startVuln && vulnCount <= 85)
                {
                    frameIndex = 8 + (int) (System.nanoTime() * 0.00000001) % 2;
                }
                
                vulnCount++;
                
                break;
            case DIED:
            	startVuln = false;
            	vulnCount = 0;
                frameIndex = 12 + dir;
                break;
        }
        frame = frames[frameIndex];
    }

    private void updateGhostCage() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    Point initialPosition = initialPositions[type];
                    updatePosition(initialPosition.x, initialPosition.y);
                    x -= 4;
                    cageUpDownCount = 0;
                    if (type == 0) {
                        instructionPointer = 6;
                        break;
                    }
                    else if (type == 2) {
                        instructionPointer = 2;
                        break;
                    }
                    instructionPointer = 1;
                case 1:
                    if (moveToTargetPosition((int) x, 134 + 4, 1)) {
                        break yield;
                    }
                    instructionPointer = 2;
                case 2:
                    if (moveToTargetPosition((int) x, 134 - 4, 1)) {
                        break yield;
                    }
                    cageUpDownCount++;
                    if (cageUpDownCount <= type * 2) {
                        instructionPointer = 1;
                        break yield;
                    }
                    instructionPointer = 3;
                case 3:
                    if (moveToTargetPosition((int) x, 134, 1)) {
                        break yield;
                    }
                    instructionPointer = 4;
                case 4:
                    if (moveToTargetPosition((int) 105, 134, 1)) {
                        break yield;
                    }
                    instructionPointer = 5;
                case 5:
                    if (moveToTargetPosition((int) 105, 110, 1)) {
                        break yield;
                    }
                    if ((int) (2 * Math.random()) == 0) {
                        instructionPointer = 7;
                        continue yield;
                    }
                    instructionPointer = 6;
                case 6:
                    if (moveToTargetPosition((int) 109, 110, 1)) {
                        break yield;
                    }
                    desiredDirection = 0;
                    lastDirection = 0;
                    updatePosition(18, 11);
                    instructionPointer = 8;
                    continue yield;
                case 7:
                    if (moveToTargetPosition((int) 101, 110, 1)) {
                        break yield;
                    }
                    desiredDirection = 2;
                    lastDirection = 2;
                    updatePosition(17, 11);
                    instructionPointer = 8;
                case 8:
                    setMode(Mode.NORMAL);
                    break yield;
            }
        }
    }
    
//    private PacmanCatchedAction pacmanCatchedAction = new PacmanCatchedAction();
//    
//    private class PacmanCatchedAction implements Runnable {
//        @Override
//        public void run() {
//            game.setState(State.PACMAN_DIED);
//        }
//    }
    
    private void updateGhostNormal() {
        if (/*checkVulnerableModeTime() && */markAsVulnerable) {
            setMode(Mode.VULNERABLE);
            markAsVulnerable = false;
        }
        
        // for debbuging purposes
//        if (Keyboard.keyPressed[KeyEvent.VK_Q] && type == 0) {
//            game.currentCatchedGhostScoreTableIndex = 0;
//            game.ghostCatched(Ghost.this);
//        }
//        else if (Keyboard.keyPressed[KeyEvent.VK_W] && type == 1) {
//            game.currentCatchedGhostScoreTableIndex = 0;
//            game.ghostCatched(Ghost.this);
//        }
//        else if (Keyboard.keyPressed[KeyEvent.VK_E] && type == 2) {
//            game.currentCatchedGhostScoreTableIndex = 0;
//            game.ghostCatched(Ghost.this);
//        }
//        else if (Keyboard.keyPressed[KeyEvent.VK_R] && type == 3) {
//            game.currentCatchedGhostScoreTableIndex = 0;
//            game.ghostCatched(Ghost.this);
//        }
        
        if (type == 0 || type == 1) {
            updateGhostMovement(true, pacman.col, pacman.row, 1, true, 0, 1, 2, 3); // chase movement
        }
        else {
            updateGhostMovement(false, 0, 0, 1, true, 0, 1, 2, 3); // random movement
        }
    }
    
//    private GhostCatchedAction ghostCatchedAction = new GhostCatchedAction();
//    
//    private class GhostCatchedAction implements Runnable {
//        @Override
//        public void run() {
//            game.ghostCatched(Ghost.this);
//        }
//    }
    
    private void updateGhostVulnerable() {
        if (markAsVulnerable) {
            markAsVulnerable = false;
        }
        
        updateGhostMovement(true, pacman.col, pacman.row, 1, false, 2, 3, 0, 1); // run away movement
        
//        // PROVA ----> DA RIMETTERE LA LINEA SOPRA AL POSTO DI QUESTA SOTTO
//        updateGhostMovement(true, pacman.col, pacman.row, 1, false, 0, 1, 2, 3); // chase movement
        
//        // return to normal mode after 8 seconds
//        if (!checkVulnerableModeTime()) {
//            setMode(Mode.NORMAL);
//        }
    }
    
    private boolean checkVulnerableModeTime() {
        return System.currentTimeMillis() - vulnerableModeStartTime <= 8000;
    }
    
    private void updateGhostDied() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    pathFinder.find(col, row, 18, 11);
                    instructionPointer = 1;
                case 1:
                    if (!pathFinder.hasNext()) {
                        instructionPointer = 3;
                        continue yield;
                    }
                    Point nextPosition = pathFinder.getNext();
                    col = nextPosition.x;
                    row = nextPosition.y;
                    instructionPointer = 2;
                case 2:
                    if (!moveToGridPosition(col, row, 4)) {
                        if (row == 11 && (col == 17 || col == 18)) {
                            instructionPointer = 3;
                            continue yield;
                        }
                        instructionPointer = 1;
                        continue yield;
                    }
                    break yield;
                case 3:
                    if (!moveToTargetPosition(105, 110, 4)){
                        instructionPointer = 4;
                        continue yield;
                    }
                    break yield;
                case 4:
                    if (!moveToTargetPosition(105, 134, 4)){
                        instructionPointer = 5;
                        continue yield;
                    }
                    break yield;
                case 5:
                    setMode(Mode.CAGE);
                    game.setState(State.PLAYING);
                    updateAnimation();
                    instructionPointer = 4;
                    break yield;
            }
        }
    }    
    
    private void updateGhostMovement(boolean useTarget, int targetCol, int targetRow
            , int velocity, boolean pacmanDied, int ... desiredDirectionsMap) {
        desiredDirections.clear();
        if (useTarget) {
            if (targetCol - col > 0) {
                desiredDirections.add(desiredDirectionsMap[0]);
            }
            else if (targetCol - col < 0) {
                desiredDirections.add(desiredDirectionsMap[2]);
            }
            if (targetRow - row > 0) {
                desiredDirections.add(desiredDirectionsMap[1]);
            }
            else if (targetRow - row < 0) {
                desiredDirections.add(desiredDirectionsMap[3]);
            }
        }
        if (desiredDirections.size() > 0) {
            int selectedChaseDirection = (int) (desiredDirections.size() * Math.random());
            desiredDirection = desiredDirections.get(selectedChaseDirection);
        }
        
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    if ((row == 14 && col == 1 && lastDirection == 2) 
                            || (row == 14 && col == 34 && lastDirection == 0)) {
                        adjustHorizontalOutsideMovement();
                    }
                    
                    double angle = Math.toRadians(desiredDirection * 90);
                    dx = (int) Math.cos(angle);
                    dy = (int) Math.sin(angle);
                    if (useTarget && game.maze[row + dy][col + dx] == 0
                            && desiredDirection != backwardDirections[lastDirection]) {
                        
                        direction = desiredDirection;
                    }
                    else {
                        do {
                            direction = (int) (4 * Math.random());
                            angle = Math.toRadians(direction * 90);
                            dx = (int) Math.cos(angle);
                            dy = (int) Math.sin(angle);
                        }
                        while (game.maze[row + dy][col + dx] == -1               
                            || direction == backwardDirections[lastDirection]);
                    }
                    
                    col += dx;
                    row += dy;
                    instructionPointer = 1;
                case 1:
                    if (!moveToGridPosition(col, row, velocity)) {
                        lastDirection = direction;
                        instructionPointer = 0;
                        // adjustHorizontalOutsideMovement();
                    }
                    if (checkCollisionWithPacman() && !pacmanDied && game.getPacMan().canEatGhosts) {
                    	game.ghostCatched(Ghost.this);
                    	this.died();
//                    	System.out.println("GHOST DIED");
                    }
                    else if (checkCollisionWithPacman() && pacmanDied && !game.getPacMan().canEatGhosts)
                    {
                    	game.setState(State.PACMAN_DIED);
//                    	System.out.println("PACMAN DIED");
                    }
                    break yield;
            }
        }        
    }

    @Override
    public void updateGhostCatched() {
        if (mode == Mode.DIED) {
            updateGhostDied();
            updateAnimation();
        }
    }

    @Override
    public void updatePacmanDied() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 1500) {
                        break yield;
                    }
                    visible = false;
                    setMode(Mode.CAGE);
                    updateAnimation();
                    break yield;
            }
        }
        updateAnimation();
    }

    @Override
    public void updateLevelCleared() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 1500) {
                        break yield;
                    }
                    visible = false;
                    setMode(Mode.CAGE);
                    updateAnimation();
                    instructionPointer = 2;
                case 2:
                    break yield;
            }
        }
    }
   
    private boolean checkCollisionWithPacman() {
        pacman.updateCollider();
        updateCollider();
        return pacman.collider.intersects(collider);
    }

    @Override
    public void updateCollider() {
        collider.setLocation((int) (x + 4), (int) (y + 4));
    }
    
    private void modeChanged() {
        instructionPointer = 0;
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
        if (game.getState() == PacmanGame.State.TITLE) {
            updateTitle();
            visible = true;
        }
        else if (game.getState() == PacmanGame.State.READY) {
            visible = false;
        }
        else if (game.getState() == PacmanGame.State.READY2) {
            setMode(Mode.CAGE);
            updateAnimation();
            Point initialPosition = initialPositions[type];
            updatePosition(initialPosition.x, initialPosition.y); // col, row
            x -= 4;
        }
        else if (game.getState() == PacmanGame.State.PLAYING && mode != Mode.CAGE) {
            instructionPointer = 0;
        }
        else if (game.getState() == PacmanGame.State.PACMAN_DIED) {
            instructionPointer = 0;
        }
        else if (game.getState() == PacmanGame.State.LEVEL_CLEARED) {
            instructionPointer = 0;
        }
    }
    
    public void showAll() {
        visible = true;
    }

    public void hideAll() {
        visible = false;
    }
    
    public void startGhostVulnerableMode() {
        vulnerableModeStartTime = System.currentTimeMillis();
        markAsVulnerable = true;
    }
    
    public void died() {
        setMode(Mode.DIED);
    }
    
    public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}
	
	public void updatePlayingReproduced(int timestep, HashMap<Integer, Vector2d> positions)
	{
		this.row = (int) positions.get(timestep).x;
		this.col = (int) positions.get(timestep).y;
		 updateAnimation();
	}
	
    public void reproduceSimulatedMove(int timestep, HashMap<Integer, Integer> directions, HashMap<Integer, Integer> desiredDirs, HashMap<Integer, Vector2d> ghostsPositions)
    {
        switch (game.getState()) 
        {
	        case INITIALIZING: updateInitializing(); break;
	//        case OL_PRESENTS: updateOLPresents(); break;
//	        case TITLE: updateTitle(); break;
	        case READY: updateReady(); break;
	        case READY2: updateReady2(); break;
	        case PLAYING: updatePlaying(directions.get(timestep), desiredDirs.get(timestep), ghostsPositions.get(timestep)); break;
	        case PACMAN_DIED: updatePacmanDied(); break;
	        case GHOST_CATCHED: updateGhostCatched(directions.get(timestep), desiredDirs.get(timestep), ghostsPositions.get(timestep)); break;
	        case LEVEL_CLEARED: updateLevelCleared(); break;
	        case GAME_OVER: updateGameOver(); break;
        }
	}
    
    private void updateGhostCatched(int dir, int desiredDir, Vector2d position) {
        if (mode == Mode.DIED) {
            updateGhostDied(dir, desiredDir, position);
            updateAnimation(dir, desiredDir, position);
        }	
	}

	public void updatePlaying(int dir, int desiredDir, Vector2d position) {
        switch (mode) {
            case CAGE: updateGhostCage(dir, desiredDir, position); break;
            case NORMAL: updateGhostNormal(dir, desiredDir, position); break;
            case VULNERABLE: updateGhostVulnerable(dir, desiredDir, position); break;
            case DIED: updateGhostDied(dir, desiredDir, position); break;
        }
        updateAnimation(dir, desiredDir, position);
    }
    
    private void updateGhostMovement(int dir, int desiredDir, Vector2d position, boolean useTarget, int targetCol, int targetRow
            , int velocity, boolean pacmanDied, int ... desiredDirectionsMap)
    {
    	
//        if ((row == 14 && col == 1 && lastDirection == 2) 
//                || (row == 14 && col == 34 && lastDirection == 0)) {
//            adjustHorizontalOutsideMovement();
//        }
        
//        if (Math.abs(this.x - position.x) > 1 || Math.abs(this.y - position.y) > 1)
//        	System.out.println("WHAT THE FUCK");
        
        moveToTargetPosition1((int)position.x, (int)position.y, velocity);
        
        if (checkCollisionWithPacman() && !pacmanDied && game.getPacMan().canEatGhosts && mode == Mode.VULNERABLE) 
        {
        	game.ghostCatched(Ghost.this);
        	this.died();
//        	System.out.println("COLLISION: GHOST DIED");
        }
//        else if (checkCollisionWithPacman() && pacmanDied && !game.getPacMan().canEatGhosts)
        else if (checkCollisionWithPacman() && pacmanDied)
        {
        	game.setState(State.PACMAN_DIED);
//        	System.out.println("COLLISION: PACMAN DIED");
        }
        
//        desiredDirections.clear();
//        if (useTarget) {
//            if (targetCol - col > 0) {
//                desiredDirections.add(desiredDirectionsMap[0]);
//            }
//            else if (targetCol - col < 0) {
//                desiredDirections.add(desiredDirectionsMap[2]);
//            }
//            if (targetRow - row > 0) {
//                desiredDirections.add(desiredDirectionsMap[1]);
//            }
//            else if (targetRow - row < 0) {
//                desiredDirections.add(desiredDirectionsMap[3]);
//            }
//        }
//        if (desiredDirections.size() > 0) {
//            desiredDirection = desiredDir;
//        }
//        
//        yield:
//        while (true) {
//            switch (instructionPointer) {
//                case 0:
//                    if ((row == 14 && col == 1 && lastDirection == 2) 
//                            || (row == 14 && col == 34 && lastDirection == 0)) {
//                        adjustHorizontalOutsideMovement();
//                    }
//                    
//                    double angle = Math.toRadians(desiredDirection * 90);
//                    dx = (int) Math.cos(angle);
//                    dy = (int) Math.sin(angle);
//                    if (useTarget && game.maze[row + dy][col + dx] == 0
//                            && desiredDirection != backwardDirections[lastDirection]) {
//                        
//                        direction = desiredDirection;
//                    }
//                    else {
//                    	
//                        direction = dir;
//                        angle = Math.toRadians(direction * 90);
//                        dx = (int) Math.cos(angle);
//                        dy = (int) Math.sin(angle);
//                    }
//                    
//                    col += position.x;
//                    row += position.y;
//                    instructionPointer = 1;
//                case 1:
//                    if (!moveToGridPosition(col, row, velocity)) {
//                        lastDirection = direction;
//                        instructionPointer = 0;
//                        // adjustHorizontalOutsideMovement();
//                    }
//                    if (collisionWithPacmanAction != null && checkCollisionWithPacman()) {
//                        collisionWithPacmanAction.run();
//                    }
//                    break yield;
//            }
//        }        
    }
    
    private void updateGhostNormal(int dir, int desiredDir, Vector2d position) {
        if (/*checkVulnerableModeTime() && */markAsVulnerable) {
            setMode(Mode.VULNERABLE);
            markAsVulnerable = false;
        }
        
        // for debbuging purposes
//        if (Keyboard.keyPressed[KeyEvent.VK_Q] && type == 0) {
//            game.currentCatchedGhostScoreTableIndex = 0;
//            game.ghostCatched(Ghost.this);
//        }
//        else if (Keyboard.keyPressed[KeyEvent.VK_W] && type == 1) {
//            game.currentCatchedGhostScoreTableIndex = 0;
//            game.ghostCatched(Ghost.this);
//        }
//        else if (Keyboard.keyPressed[KeyEvent.VK_E] && type == 2) {
//            game.currentCatchedGhostScoreTableIndex = 0;
//            game.ghostCatched(Ghost.this);
//        }
//        else if (Keyboard.keyPressed[KeyEvent.VK_R] && type == 3) {
//            game.currentCatchedGhostScoreTableIndex = 0;
//            game.ghostCatched(Ghost.this);
//        }
        
        if (type == 0 || type == 1) {
            updateGhostMovement(dir, desiredDir, position, true, pacman.col, pacman.row, 1, true, 0, 1, 2, 3); // chase movement
        }
        else {
            updateGhostMovement(dir, desiredDir, position, false, 0, 0, 1, true, 0, 1, 2, 3); // random movement
        }
//        System.out.println("COL: " + col + " ROW: " + row + " VALORE: " + game.maze[row][col]);
    }
    
    private void updateGhostVulnerable(int dir, int desiredDir, Vector2d position) {
        if (markAsVulnerable) {
            markAsVulnerable = false;
        }
        
        updateGhostMovement(dir, desiredDir, position, true, pacman.col, pacman.row, 1, false, 2, 3, 0, 1); // run away movement
//        // return to normal mode after 8 seconds
//        if (!checkVulnerableModeTime()) {
//            setMode(Mode.NORMAL);
//        }
    }        
    
    private void updateGhostDied(int dir, int desiredDir, Vector2d position) 
    {
        moveToTargetPosition1((int)position.x, (int)position.y, 4);
        updateAnimation(dir, desiredDir, position);
        
        if (position.x == 105 && position.y == 134)
        {
        	System.out.println("GHOST ARRIVED AT DESTINATION!");
        	setMode(Mode.CAGE);
//        	modeChanged();
        	game.setState(State.PLAYING);
        	System.out.println(game.getState());
//        	stateChanged();
            updateAnimation(dir, desiredDir, position);
        }
        
//        yield:
//        while (true) {
//            switch (instructionPointer) {
//                case 0:
//                    pathFinder.find(col, row, 18, 11);
//                    instructionPointer = 1;
//                case 1:
//                    if (!pathFinder.hasNext()) {
//                        instructionPointer = 3;
//                        continue yield;
//                    }
//                    Point nextPosition = pathFinder.getNext();
//                    col = nextPosition.x;
//                    row = nextPosition.y;
//                    instructionPointer = 2;
//                case 2:
//                    if (!moveToGridPosition(col, row, 4)) {
//                        if (row == 11 && (col == 17 || col == 18)) {
//                            instructionPointer = 3;
//                            continue yield;
//                        }
//                        instructionPointer = 1;
//                        continue yield;
//                    }
//                    break yield;
//                case 3:
//                    if (!moveToTargetPosition(105, 110, 4)){
//                        instructionPointer = 4;
//                        continue yield;
//                    }
//                    break yield;
//                case 4:
//                    if (!moveToTargetPosition(105, 134, 4)){
//                        instructionPointer = 5;
//                        continue yield;
//                    }
//                    break yield;
//                case 5:
//                    setMode(Mode.CAGE);
//                    instructionPointer = 4;
//                    break yield;
//            }
//        }
    }
    
    private void updateGhostCage(int dir, int desiredDir, Vector2d position)
    {
    	moveToTargetPosition1((int)position.x, (int)position.y, 1);
    	updateAnimation(dir, desiredDir, position);
    	
    	if (this.x == 109 && this.y == 110)
    	{
    		desiredDirection = 0;
    		lastDirection = 0;
    		updatePosition(18, 11);
    		setMode(Mode.NORMAL);
    	}
    	
    	if (this.x == 101 && this.y == 110)
    	{
            desiredDirection = 2;
            lastDirection = 2;
            updatePosition(17, 11);
    		setMode(Mode.NORMAL);
    	}
    	
//        yield:
//        while (true) {
//            switch (instructionPointer) {
//                case 0:
//                    Point initialPosition = initialPositions[type];
//                    updatePosition(initialPosition.x, initialPosition.y);
//                    x -= 4;
//                    cageUpDownCount = 0;
//                    if (type == 0) {
//                        instructionPointer = 6;
//                        break;
//                    }
//                    else if (type == 2) {
//                        instructionPointer = 2;
//                        break;
//                    }
//                    instructionPointer = 1;
//                case 1:
//                    if (moveToTargetPosition((int) x, 134 + 4, 1)) {
//                        break yield;
//                    }
//                    instructionPointer = 2;
//                case 2:
//                    if (moveToTargetPosition((int) x, 134 - 4, 1)) {
//                        break yield;
//                    }
//                    cageUpDownCount++;
//                    if (cageUpDownCount <= type * 2) {
//                        instructionPointer = 1;
//                        break yield;
//                    }
//                    instructionPointer = 3;
//                case 3:
//                    if (moveToTargetPosition((int) x, 134, 1)) {
//                        break yield;
//                    }
//                    instructionPointer = 4;
//                case 4:
//                    if (moveToTargetPosition((int) 105, 134, 1)) {
//                        break yield;
//                    }
//                    instructionPointer = 5;
//                case 5:
//                    if (moveToTargetPosition((int) 105, 110, 1)) {
//                        break yield;
//                    }
//                    if ((int) (2 * Math.random()) == 0) {
//                        instructionPointer = 7;
//                        continue yield;
//                    }
//                    instructionPointer = 6;
//                case 6:
//                    if (moveToTargetPosition((int) 109, 110, 1)) {
//                        break yield;
//                    }
//                    desiredDirection = 0;
//                    lastDirection = 0;
//                    updatePosition(18, 11);
//                    instructionPointer = 8;
//                    continue yield;
//                case 7:
//                    if (moveToTargetPosition((int) 101, 110, 1)) {
//                        break yield;
//                    }
//                    desiredDirection = 2;
//                    lastDirection = 2;
//                    updatePosition(17, 11);
//                    instructionPointer = 8;
//                case 8:
//                    setMode(Mode.NORMAL);
//                    break yield;
//            }
//        }
    }
    
    public static Direction getDirection(int direction)
    {
    	Direction result = null;
    	
        switch (direction) 
        {
	        case 0: result = Direction.RIGHT; break;
	        case 1: result = Direction.DOWN; break;
	        case 2: result = Direction.LEFT; break;
	        case 3: result = Direction.UP; break;
        }
        
        return result;
    }
    
}
