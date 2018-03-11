package pingball.Gadgets;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import physics.Circle;
import physics.Geometry;
import physics.Vect;
import pingball.Ball;
import pingball.Board;
import pingball.Messages.GoodbyePortalBall;

/** 
 * A mutable class to represent a Pingball portal.
 *
 */
public class Portal extends CircleBumper {
    
    /*
     * AF: 
     * Portal makes a portal with a name; it function exactly the same as a circle bumper,
     *      except that collisions are handled differently 
     *      (balls are either eaten or unaffected instead of bouncing)
     *      
     * RI: is located within the boundaries of the board
     * 
     */
    
    private List<GoodbyePortalBall> goodbyePortalBallMessages = new ArrayList<GoodbyePortalBall>();

    private boolean isConnected;
    private String destinationPortal;
    private String destinationBoard;
    
    /**
     * Portal makes a portal with a name; it functions exactly the same 
     * as a circle bumper, except that collisions are handled differently 
     *      (balls are either eaten or unaffected instead of bouncing)
      * @param name the name of the portal
      * @param x x-coordinate of the position
      * @param y y-coordinate of the position
      * @param destinationPortal the name of the destination portal
      * @param destinationBoard the name of the destination board
     */
    public Portal(String name, int x, int y, String destinationPortal,
            String destinationBoard) {
        super(name, x, y);
        this.destinationPortal = destinationPortal;
        this.destinationBoard = destinationBoard;
        this.isConnected = false;
        super.checkRep();
    }
    
    /**
     * If a ball is going to collide with a portal within a given time, 
     *      the portal eats the ball if it's connected
     *      and does nothing if it's unconnected.
     * @param ball that is colliding with portal
     * @time time that it is going to collide within
     */
    @Override
    public void collisionBallGadget(Ball ball, long time) {
        if (this.isConnected() && !ball.isImmune()){
            goodbyePortalBallMessages.add(new GoodbyePortalBall(
                    this.destinationBoard, this.destinationPortal, ball)); 
        }        
    }
    
    /**
     * Gets a list of balls that the portal has eaten
     * @return a list of balls to send to other boards
     */
    @Override
    public List<GoodbyePortalBall> getGoodbyePortalBallMessages(){
        return goodbyePortalBallMessages;
    }
    
    /**
     * Empties the list of balls that the portal has to eject.
     */
    @Override
    public void emptyBallsToEject(){
        goodbyePortalBallMessages.clear();
    }

    /**
     * Checks to see if this portal is connected to another one
     * @return true if this portal is connected to another, false if not
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Sets if this portal is connected to another one
     * @param isConnected: true if this portal is connected to another, false if not
     */
    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }
    
    /**
     * Gets the name of the destination board.
     * Can be null if destination board not initialized.
     * @return the destination board
     */
    public String getDestinationBoard() {
        return destinationBoard;
    }
    
    @Override
    public boolean isPortal() {
        return true;
    }
    
    @Override
    public boolean equals(Object other){
        if (other instanceof Portal){
            Portal otherBumper = (Portal) other;
            if (this.getX() == otherBumper.getX()
                    && this.getY() == otherBumper.getY()
                    && this.getWidth() == otherBumper.getWidth()
                    && this.getHeight() == otherBumper.getHeight()
                    && this.getNamesOfGadgetsToTrigger().equals(otherBumper.getNamesOfGadgetsToTrigger())
                    && this.getGoodbyePortalBallMessages().equals(otherBumper.getGoodbyePortalBallMessages())
                    && super.equals(otherBumper)
                    && this.isConnected==otherBumper.isConnected
                    && (this.getName() == otherBumper.getName() || 
                        (this.getName() != null && otherBumper.getName() != null && 
                        this.getName().equals(otherBumper.getName())))){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Portal with name " + getName();
    }
    
    @Override
    public void drawGadgetOnBoard(Board board) {
        String[][] boardDisplayArray = board.getDisplayArray();
        double X = this.getX();
        double Y= this.getY();
        boardDisplayArray[(int)Y][(int)X] = "o";
        board.setDisplayArray(boardDisplayArray);
    }
    
    @Override
    public Color getDrawColor(){
        return Color.MAGENTA;
    }
}
