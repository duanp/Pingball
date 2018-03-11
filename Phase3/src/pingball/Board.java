package pingball;

import java.awt.event.KeyEvent;
import java.util.*;

import physics.*;
import physics.Geometry.VectPair;
import pingball.Gadgets.Absorber;
import pingball.Gadgets.CircleBumper;
import pingball.Gadgets.Flipper;
import pingball.Gadgets.Gadget;
import pingball.Gadgets.Portal;
import pingball.Gadgets.SquareBumper;
import pingball.Gadgets.TriangleBumper;
import pingball.Messages.GoodbyePortalBall;
import pingball.Messages.GoodbyeWallBall;

/**
 * A mutable board for the Pingball game. The board both holds the 
 * gadgets and the balls and accounts for the interactions
 * between them in any given time step.
 *
 */
public class Board {
    //
    // Abstraction Function: 
    //      Represents a 20L x 20L playing area board with containing gadgets.  
    // Rep invariant: 
    //      Board cannot have a playing area larger than 20L x 20L. 
    //      All balls, gadgets, must stay within Board area, must not 
    //      overlap on placement.
    //
    
    
    
    private final int width = 20;
    private final int height = 20;
    //board corners and walls in cartesian
    private final List<Circle> boardCorners = Collections.unmodifiableList(new ArrayList<Circle>(
            Arrays.asList(new Circle(0,0,0), new Circle(0,-20,0), new Circle(20,-20,0), new Circle(20,0,0))));
    private List<Ball> balls = new ArrayList<Ball>();
    private List<Gadget> gadgets = new ArrayList<Gadget>(); 
    private String[][] displayArray = new String[20][20];
    
    // board configuration values to be used
    private final Vect gravity;
    private final double friction1;
    private final double friction2;
    
    // default board configuration values
    private static final double DEFAULT_GRAVITY = 25;
    
    // default friction values
    private static final double DEFAULT_FRICTION1 = .025;
    private static final double DEFAULT_FRICTION2 = .025;
    
    private List<GoodbyePortalBall> goodbyePortalBallMessages = new ArrayList<GoodbyePortalBall>(); 
    private List<GoodbyeWallBall> goodbyeWallBallMessages = new ArrayList<GoodbyeWallBall>(); 
    
    private String name;
    private Map<WallType, Wall> walls = new HashMap<WallType, Wall>();
    private Map<WallType, String> connectedWalls = new HashMap<WallType, String>();
    
    private Map<String, Portal> portalMap = new HashMap<String, Portal>();
    
    // structures to map key strokes to triggered gadgets
    private KeyNames keyNames = new KeyNames();
    private Map<String, Set<Gadget>> keyUpTriggers = new HashMap<String, Set<Gadget>>();
    private Map<String, Set<Gadget>> keyDownTriggers = new HashMap<String, Set<Gadget>>();
    
    /**
     * Creator for Board. No arguments, creates a 20Lx20L board with no balls or gadgets.
     */
    public Board(){
        this(DEFAULT_GRAVITY, DEFAULT_FRICTION1, DEFAULT_FRICTION2);
        checkRep();
    }
    
    /**
     * Constructor for Board
     * @param gravity the specified gravity at the board
     */
    public Board(double gravity) {
        this(gravity, DEFAULT_FRICTION1, DEFAULT_FRICTION2);
    }
    
    /**
     * Creator for Board with a name. No arguments besides name, creates a 20Lx20L board with no balls or gadgets.
     * @param name the name of the baord
     */
    public Board(String name){
        this(name, DEFAULT_GRAVITY, DEFAULT_FRICTION1, DEFAULT_FRICTION2);
        checkRep();
    }
    
    /**
     * Constructor for Board
     * @param name the name of the board
     * @param gravity the specified gravity at the board
     */
    public Board(String name, double gravity) {
        this(name, gravity, DEFAULT_FRICTION1, DEFAULT_FRICTION2);
        checkRep();
    }
    
    /**
     * Constructor for Board
     * @param name the name of the board
     * @param gravity the specified gravity at the board
     * @param friction1 friction constant mu1 of the board
     * @param friction2 friction constant mu2 of the board
     */
    public Board(String name, double gravity, double friction1, double friction2) {
        this(gravity, friction1, friction2);
        this.name = name;
        checkRep();
    }
    
    /**
     * Constructor for Board
     * @param name the name of the board
     * @param gravity the specified gravity at the board
     * @param friction1 friction constant mu1 of the board
     * @param friction2 friction constant mu2 of the board
     */
    public Board(double gravity, double friction1, double friction2) {
        this.gravity = new Vect(0, -gravity);
        
        // fill empty displayArray with spaces
        for(int i = 0; i < this.displayArray.length; i++){
            for(int j = 0; j < this.displayArray[i].length; j++){
                this.displayArray[i][j] = " ";
            }
        }
        this.friction1 = friction1;
        this.friction2 = friction2;
        
        walls.put(WallType.TOP, new Wall(WallType.TOP));
        walls.put(WallType.BOTTOM, new Wall(WallType.BOTTOM));
        walls.put(WallType.LEFT, new Wall(WallType.LEFT));
        walls.put(WallType.RIGHT, new Wall(WallType.RIGHT));
        checkRep();

    }

    /**
     * Gets the WallType from a string. Precondition:
     * the string must be "top|bottom|left|right"
     * @param type
     * @return the corresponding WallType
     */
    private WallType getWallType(String type) {
        WallType wallType = WallType.TOP;
        switch(type) {
        case "top":
            wallType = WallType.TOP;
            break;
        case "bottom":
            wallType = WallType.BOTTOM;
            break;
        case "left":
            wallType = WallType.LEFT;
            break;
        case "right":
            wallType = WallType.RIGHT;
            break;
        }
        return wallType;
    }
    

    /**Mutator
     * Add a new ball to the Board. Cannot place a ball on top of anything else.
     * @param newBall ball to be added
     */
    public void addBall(Ball newBall){
        balls.add(newBall);
        checkRep();
    }
    
    /**Mutator
     * Add a new gadget to the Board. Cannot place a gadget on top of anything else.
     * @param newGadget gadget to be added
     */
    public void addGadget(Gadget newGadget){
        gadgets.add(newGadget);
        if (newGadget.isPortal()) {
            Portal newPortal = (Portal) newGadget;
            portalMap.put(newPortal.getName(), newPortal);
        }
        checkRep();
    }
    
    /**
     * Adds a key-gadget trigger relationship
     * @param key the key that triggers the gadget
     * @param gadgetToTrigger the gadget to trigger
     */
    public void addKeyTrigger(String key, Gadget gadgetToTrigger, boolean keyUp) {
        Map<String, Set<Gadget>> keyTriggers = keyDownTriggers;
        if (keyUp) {
            keyTriggers = keyUpTriggers;
        }
        if (!keyTriggers.containsKey(key)) {
            keyTriggers.put(key, new HashSet<Gadget>());
        }
        keyTriggers.get(key).add(gadgetToTrigger);
    }
    

    /**
     * Triggers a key as specified by its keycode i.
     * @param i the keycode
     * @param down whether or not the key was pressed or released
     */
    public void keyTrigger(int i, boolean down) {
        String key = keyNames.getKey(i);
        Set<Gadget> gadgetsToTrigger = keyUpTriggers.get(key);
        if (down) {
            gadgetsToTrigger = keyDownTriggers.get(key);
        }
        if (gadgetsToTrigger != null) {
            for (Gadget gadget : gadgetsToTrigger) {
                gadget.activateGadget();
            }
        }
    }

    
    // rep invariant: all balls, gadgets, must stay within Board area, must not overlap on placement.
    // ignore anything happening inside of an absorber, that thing is black magic.
    /**
     * Check rep invariant is being preserved.
     */
    private void checkRep() throws IndexOutOfBoundsException{
        // get all the absorbers and make a list of them.
        List<Absorber> absorbers = new ArrayList<Absorber>();
        for(Gadget absorbCheck : gadgets){
            if(absorbCheck.isAbsorber()){
                absorbers.add((Absorber) absorbCheck);
            }
        }
        balls:
        for(Ball i : balls){
            if (i.getPosition().x() >=this.width){
                i.setPosition(new Vect(this.width-.25,i.getPosition().y()));
            }
            if(i.getPosition().x() < 0 || i.getPosition().x() > this.width
                    || i.getPosition().y() < 0 || i.getPosition().y() > this.height){
                throw new IndexOutOfBoundsException("A ball (" + i.getPosition() + ") is out of bounds.");
            }
            // check ball-gadget collisions, if absorber, continue to next iteration, because absorbers are black magic.
            for(Gadget inGadget : gadgets){
                if(inGadget.isAbsorber() || inGadget.isFlipper()){ 
                    continue balls;
                }
            }
            for(Ball j: balls){
                if (Math.sqrt((i.getPosition().x() - j.getPosition().x())*(i.getPosition().x() - j.getPosition().x()) + 
                       (i.getPosition().y() - j.getPosition().y())*(i.getPosition().y() - j.getPosition().y())) < i.getRadius() + j.getRadius()
                       && (!i.equals(j))){
                    throw new IndexOutOfBoundsException("Balls overlapping." + j + i);
                }
            }
        }
        //check only gadget-gadget collisions
        for(Gadget k : gadgets){
            if(k.getX() < 0 || (k.getX() + k.getWidth()) > this.width
                    || k.getY() < 0 || (k.getY() + k.getHeight()) > this.height){
                throw new IndexOutOfBoundsException("A gadget (" + gadgets.indexOf(k) + ") is out of bounds.");
            }
            for(Gadget l : gadgets){
                double deltaXkl = k.getX() - l.getX();
                double deltaXlk = l.getX() - k.getX();
                double deltaYkl = k.getY() - l.getY();
                double deltaYlk = l.getY() - k.getY();
                if(
                        ((deltaXkl < l.getWidth() && deltaXkl > 0)
                                || (deltaXlk < k.getWidth()  && deltaXlk > 0))
                        && ((deltaYkl < l.getHeight() && deltaYkl > 0)
                                || (deltaYlk < k.getHeight() && deltaYlk > 0))
                        && !(k.equals(l))
                        ){
                    throw new IndexOutOfBoundsException("Gadgets overlapping. "
                            + "(" + gadgets.indexOf(k) + ", "+ gadgets.indexOf(l) + ")"
                                    + k.getX() + " " + k.getY() + " " + l.getX() + " " + l.getY());
                }
            }
        }
    }
    
    /**
     * Convert Cartesian vector to display coordinates.
     * @return vector in display coordinates (y inverse)
     */
    public Vect cartesianToDisplay(Vect cartesianVect){
        return new Vect(cartesianVect.x(),-cartesianVect.y());
    }
    
    /**
     * Convert display vector to Cartesian coordinates.
     * @return vector in display coordinates, y inverse
     */
    public Vect displayToCartesian(Vect displayVect){
        return new Vect(displayVect.x(),-1*displayVect.y());
    }
    
    /**
     * Draws the board as a piece of text art to the console. A description of symbols used may be found on
     * the 6.005 Pingball Phase 1 specifications page.
     * @return string representation of board, text art.
     */
    public String drawBoard(){
        for(int i = 0; i < displayArray.length; i++){
            for(int j = 0; j < displayArray[i].length; j++){
                displayArray[i][j] = " ";
            }
        }
        for(Gadget l : gadgets){
            l.drawGadgetOnBoard(this);
        }
        for(Ball k : balls){
            k.drawBallOnBoard(this);
        }
        
        StringBuilder boardBuilder = new StringBuilder();
        boardBuilder.append(walls.get(WallType.TOP).getDisplayString() + "\n");
        int rowNum = 0;
        for(String[] row : displayArray){
            boardBuilder.append(Character.toString(walls.get(WallType.LEFT).getDisplayString().charAt(rowNum)));
            for(String cell : row){
                boardBuilder.append(cell);
            }
            boardBuilder.append(Character.toString(walls.get(WallType.RIGHT).getDisplayString().charAt(rowNum))+"\n");
            rowNum++;
        }
        boardBuilder.append(walls.get(WallType.BOTTOM).getDisplayString());
        return boardBuilder.toString();
    }

    /**
     * Get the display array that represents the board.
     * @return the display array for this board (2D string array)
     */
    public String[][] getDisplayArray() {
        return this.displayArray;
    }
    
    /**
     * Returns a list of references to all the balls on the board, as Balls.
     * @return a List of references to all the Balls on this board
     */
    public List<Ball> getBalls(){
        return balls;
    }
    
    /**
     * Returns a list of references to all the gadgets on the board, as Gadgets.
     * @return a List of references to all the Gadgets on this board
     */
    public List<Gadget> getGadgets(){
        return gadgets;
    }
    
    /**
     * Returns the number of balls in the Board.
     * @return an int as the number of balls in the board
     */
    public int getNumberOfBalls(){
        return balls.size();
    }
    
    /**
     * Returns the number of gadgets in the Board.
     * @return an int as the number of gadgets in the board
     */
    public int getNumberOfGadgets(){
        return gadgets.size();
    }
    
    /**
     * Gets the keys that trigger what gadgets when released
     * @return a map of key --> set of gadgets it triggers
     */
    public Map<String, Set<Gadget>> getKeyUpTriggers() {
        return keyUpTriggers;
    }
    
    /**
     * Gets the keys that trigger what gadgets when pressed down
     * @return a map of key --> set of gadgets it triggers
     */
    public Map<String, Set<Gadget>> getKeyDownTriggers() {
        return keyDownTriggers;
    }
    
    /**Mutator
     * removes the ball from the board
     * @param ball the ball you wish to remove.
     */
    public void removeBall(Ball ball){
        balls.remove(ball);
    }
    
    /**Mutator
     * removes the gadget from the board
     * @param gadget the gadget you wish to remove.
     */
    public void removeGadget(Gadget gadget){
        gadgets.remove(gadget);
    }
    
    /**
     * Updates the board by one timestep (whatever that timestep may be)
     * @param timestep the length of the desired timestep
     */
    public void update(long timestep){
        //check collisions for each ball
        long timeRemaining = timestep;
        ballCheck:
        for(Ball currentBall : this.balls){
            //first: if the ball is in any way in an absorber, ignore everything happening to it.
            //Because absorbers are black magic.
            for(Gadget checkAbsorber : gadgets){
                if(checkAbsorber.isAbsorber() && checkAbsorber.getX() <= currentBall.getPosition().x()-currentBall.getRadius()
                        && currentBall.getPosition().x()+currentBall.getRadius() <= checkAbsorber.getX()+checkAbsorber.getWidth()
                        && checkAbsorber.getY() <= currentBall.getPosition().y()-currentBall.getRadius()
                        && currentBall.getPosition().y()+currentBall.getRadius() <= checkAbsorber.getY()+checkAbsorber.getHeight()){
                    //currentBall.update(timeRemaining, gravity, friction1, friction2); no gravity for balls in an absorber!
                    continue ballCheck;
                }
            }
            //check corner collisions
            timeRemaining = updateCornerCollisions(currentBall, timeRemaining);
            
            //check board-wall collisions
            timeRemaining = updateWallCollisions(currentBall, timeRemaining);
            
            // check ball-ball collisions
            timeRemaining = updateBallBallCollisions(currentBall, timeRemaining);

            // check ball-gadget collisions
            timeRemaining = updateBallGadgetCollisions(currentBall, timeRemaining);
        }
        for ( GoodbyePortalBall message : goodbyePortalBallMessages){
        this.removeBall(message.getBall());
        }
        for ( GoodbyeWallBall message : goodbyeWallBallMessages){
        this.removeBall(message.getBall());
        }
        for(Ball currentBall : this.balls){
            // check ball-gadget collisions
            timeRemaining = updateBallGadgetCollisions(currentBall, timeRemaining);

            // check ball-wall collisions
            timeRemaining = updateWallCollisions(currentBall, timeRemaining);
            
            // check ball-ball collisions
            timeRemaining = updateBallBallCollisions(currentBall, timeRemaining);
            
            // do the gravity thing (and friction??)
            currentBall.update(timeRemaining, gravity, friction1, friction2);

        }
        for (Gadget gadget: gadgets){
            if (gadget.isFlipper()){
                ((Flipper) gadget).updateFlipper(timeRemaining);
            }
        }
        for ( GoodbyePortalBall message : goodbyePortalBallMessages){
            this.removeBall(message.getBall());   
        }
        for ( GoodbyeWallBall message : goodbyeWallBallMessages){
        this.removeBall(message.getBall());
        }
        checkRep();
    }
    
    /**
     * Checks to see if a ball will bounce off a corner and, if it will, updates the ball to its post-collision velocity
     * @param currentBall the ball to corner collide
     * @param timeRemaining the timeframe to check for a collision in 
     * @return time left in the timeframe after the collision
     */
    public long updateCornerCollisions(Ball currentBall, long timeRemaining){
        // check corner collisions
        for(Circle corner : this.boardCorners){
            // need currentBall's Cartesian circle coordinates
            Circle cartesianCurrentBallCircle = new Circle(this.displayToCartesian(currentBall.getCircle().getCenter()),currentBall.getRadius());
            // if we hit a corner (already Cartesian)
            long timeToCornerHit = (long) Geometry.timeUntilCircleCollision(corner, cartesianCurrentBallCircle, this.displayToCartesian(currentBall.getVelocity().times(1/1000.)));
            if(timeToCornerHit <= timeRemaining){
                // bounce and update remaining time
                currentBall.update(timeToCornerHit, gravity, friction1, friction2);
                currentBall.setVelocity(Geometry.reflectCircle(corner.getCenter(), this.displayToCartesian(currentBall.getPosition()), this.displayToCartesian(currentBall.getVelocity())));
                
                timeRemaining -= timeToCornerHit;
            }
        }
        return timeRemaining;
    }
    
    /**
     * Checks to see if a ball will bounce off a wall and, if it will, updates the ball to its post-collision velocity
     * @param currentBall the ball to corner collide
     * @param timeRemaining the timeframe to check for a collision in 
     * @return time left in the timeframe after the collision
     */
    public long updateWallCollisions(Ball currentBall, long timeRemaining){
        // check board wall collisions
        for (Wall wallObject : walls.values()) {
            LineSegment wall = wallObject.getShape();
            // need currentBall's Cartesian circle coordinates
            Circle cartesianCurrentBallCircle = new Circle(
                    this.displayToCartesian(currentBall.getCircle().getCenter()),
                    currentBall.getRadius());
            // if we hit a wall (already in Cartesian)
            long timeToWallHit = (long) (Geometry.timeUntilWallCollision(
                    wall,
                    cartesianCurrentBallCircle,
                    this.displayToCartesian(currentBall.getVelocity().times(
                            1 / 1000.))));
            if (timeToWallHit <= timeRemaining) {
                // bounce and update remaining time
                currentBall.update(timeToWallHit, gravity, friction1, friction2);
                // transparent
                if (wallObject.isTransparent()) {
                    goodbyeWallBallMessages.add(new GoodbyeWallBall(this
                            .getName(), wallObject.getType(), currentBall));
                } else { // if it's not transparent
                    currentBall.setVelocity(this.cartesianToDisplay(Geometry
                            .reflectWall(wall, this
                                    .displayToCartesian(currentBall
                                            .getVelocity()))));
                }
                timeRemaining -= timeToWallHit;
            }
        }
        return timeRemaining;
    }
    
    /**
     * Checks to see if a ball will bounce off another ball and, if it will, updates it to its post-collision velocity
     * @param currentBall the ball to corner collide
     * @param timeRemaining the timeframe to check for a collision in 
     * @return time left in the timeframe after the collision
     */
    public long updateBallBallCollisions(Ball currentBall, long timeRemaining){
        // check ball-ball collisions
        for(Ball otherBall : balls){
            // need currentBall's Cartesian circle coordinates
            Circle cartesianCurrentBallCircle = new Circle(this.displayToCartesian(currentBall.getCircle().getCenter()),currentBall.getRadius());
            // need otherBall's Cartesian circle coordinates
            Circle cartesianOtherBallCircle = new Circle(this.displayToCartesian(otherBall.getCircle().getCenter()),otherBall.getRadius()); 
            // if we're going to hit the otherBall
            long timeToBallHit = (long) Geometry.timeUntilBallBallCollision(cartesianCurrentBallCircle, this.displayToCartesian(currentBall.getVelocity()), cartesianOtherBallCircle, this.displayToCartesian(otherBall.getVelocity()));
            if(currentBall != otherBall && timeToBallHit <= timeRemaining){
                // get them both to update location
                currentBall.update(timeToBallHit, gravity, friction1, friction2);
                otherBall.update(timeToBallHit, gravity, friction1, friction2);
                // collide
                VectPair newVelocities = Geometry.reflectBalls(cartesianCurrentBallCircle.getCenter(), 1, this.displayToCartesian(currentBall.getVelocity()), cartesianOtherBallCircle.getCenter(), 1, this.displayToCartesian(otherBall.getVelocity()));
                currentBall.setVelocity(this.cartesianToDisplay(newVelocities.v1));
                otherBall.setVelocity(this.cartesianToDisplay(newVelocities.v2));
                // subtract time taken from the clock
                timeRemaining -= timeToBallHit;
            }
        }
        return timeRemaining;
    }
    
    /**
     * Checks to see if a ball will bounce off a gadget and, if it will, updates the ball to its post-collision velocity
     * @param currentBall the ball to corner collide
     * @param timeRemaining the timeframe to check for a collision in 
     * @return time left in the timeframe after the collision 
     */
    public long updateBallGadgetCollisions(Ball currentBall, long timestep){
        // check ball-gadget collisions
        boolean ballCollides = false;
        for (Gadget gadget : gadgets){
           if (gadget.ballHitsGadgetThisTimestep(currentBall,timestep)<Double.POSITIVE_INFINITY){
               ballCollides = true;
               long timeToHit = (long) gadget.ballHitsGadgetThisTimestep(currentBall,timestep);
               currentBall.update((long) gadget.ballHitsGadgetThisTimestep(currentBall,timestep), 
                       gravity, friction1, friction2);
               gadget.collisionBallGadget(currentBall, timestep);
               gadget.triggerGadgets(currentBall);
               timestep -= timeToHit;
           }   
           if (gadget.getGoodbyePortalBallMessages().size() >0){
               for (GoodbyePortalBall message : gadget.getGoodbyePortalBallMessages()){
                   goodbyePortalBallMessages.add(message);
               }
               gadget.emptyBallsToEject();
           }
        }
        if (!ballCollides && currentBall.isImmune()){
            currentBall.setImmunue(false);
        }
        return timestep;
    }
    
    /**
     * Setter for displayArray
     */
    public void setDisplayArray(String[][] newDisplayArray){
        this.displayArray = newDisplayArray;
    }
    
    /**
     * Gets a list of messages for balls that the portal has eaten
     * @return a list of messages to send to other boards
     */
    public List<GoodbyePortalBall> getGoodbyePortalBallMessages(){
        return goodbyePortalBallMessages;
    }
    
    /**
     * empties the list of portal balls to eject
     */
    public void emptyGoodbyePortalBallMessages(){
        goodbyePortalBallMessages.clear();
    }
    
    /**
     * Gets a list of messages for balls that the portal has eaten
     * @return a list of messages to send to other boards
     */
    public List<GoodbyeWallBall> getGoodbyeWallBallMessages(){
        return goodbyeWallBallMessages;
    }
    
    /**
     * empties the list of portal balls to eject
     */
    public void emptyGoodbyeWallBallMessages(){
        goodbyeWallBallMessages.clear();
    }
    
    /**
     * Gets the name of this board
     * @return the name of this board
     */
    public String getName(){
        return this.name;
    }
    
    // implements observational equality
    @Override
    public boolean equals(Object other){
        if (other instanceof Board){
            Board otherBoard = (Board) other;
            if (this.getNumberOfGadgets() == otherBoard.getNumberOfGadgets()
                    && this.drawBoard().equals(otherBoard.drawBoard())
                    && (this.name == otherBoard.name || (this.name != null && otherBoard.name != null && this.name.equals(otherBoard.name)))
                    && this.getNumberOfBalls()==otherBoard.getNumberOfBalls()){
                for (Gadget gadget : this.gadgets) {
                    if (!otherBoard.gadgets.contains(gadget)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Board: " + this.name + " " + gadgets;
    }
    
    /**
     * Gets the portal of the name in the board. A portal with name portalName
     * must exist in the board
     * @param portalName the name of the portal to get
     * @return the Portal of name portalName
     */
    public Portal getPortalByName(String portalName) {
        if (portalMap.containsKey(portalName)) {
            return portalMap.get(portalName);
        }
        throw new RuntimeException("The portal does not exist in the Board.");
    }
    

    /**
     * Takes in a top/bottom/left/right string and the name of the board that this one is being connecting to,
     * and then makes this board have that wall be transparent and show the name (will cut off names longer than 20 chars)
     * @param wallType ::= "top" or "bottom" or "left" or "right"
     * @param otherBoard name of other board
     */
    public void joinWall(String wallType, String otherBoard) {
        WallType type = getWallType(wallType);
        walls.get(type).makeTransparent(otherBoard);
        connectedWalls.put(type, otherBoard);
    }
    
    /**
     * Look through boards that are connected and set portals to be 
     * connected if the portals are connected to those boards
     * @param boardNames an array of names of boards to connect
     */
    public void connectBoardsToPortals(String[] boardNames) {
        for (String boardName : boardNames) {
            for (Portal portal : portalMap.values()) {
                if (portal.getDestinationBoard().equals(boardName)) {
                    portal.setConnected(true);
                }
            }
        }
    }
    
    /**
     * Gets the map of connected walls
     * @return a map of WallType type --> String otherBoard 
     */
    public Map<WallType, String> getConnectedWalls(){
        return connectedWalls;
    }

    /**
     * Removes the board with name string from the list of boards that this
     *  board is connected to, clearing the name of that board from being
     *  written in the walls of this board as well.
     * This board might not be connected to the other board.
     *  
     * @param board the name of the board to remove from the connection of this board
     */
    public void removeConnectedBoard(String board) {
        for (Map.Entry<WallType, String> entry : connectedWalls.entrySet()) {
            if (entry.getValue().equals(board)) {
                walls.get(entry.getKey()).makeSolid();
                connectedWalls.put(entry.getKey(), "");
            }
        } 
    }
    
    /**
     * Set portals to be disconnected if the portals are connected to the disconnected board
     * @param boardName the name of the disconnected board
     */
    public void disconnectSingleBoardFromPortals(String boardName) {
        for (Portal portal : portalMap.values()) {
            if (portal.getDestinationBoard().equals(boardName)) {
                portal.setConnected(false);
            } 
        }
    }
    
    /**
     * Disconnects all walls of this board.
     */
    public void disconnect() {
        for (WallType type : walls.keySet()) {
            walls.get(type).makeSolid();
            connectedWalls.put(type, "");
        }
    }
    
    /**
     * Gets a list of the other boards that this board is 
     * connected to.
     * @return a list of string names
     */
    public List<String> getWallNames(){
        List<String> wallNames = new ArrayList<String>();
        for (String name : connectedWalls.values()) {
            wallNames.add(name);
        }
        return wallNames;
    }
}



