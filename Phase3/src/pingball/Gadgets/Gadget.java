package pingball.Gadgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.awt.Color;
import java.awt.Shape;

import pingball.Ball;
import pingball.Board;
import pingball.Messages.GoodbyePortalBall;

/**
 * A mutable gadget for the Pingball game.
 * 
 * Abstraction Function: Represents a gadget in a Pingball game.
 * Rep Invariant: position must be integer coordinates. Width 
 * and height must also be integers.
 */

public interface Gadget {
    
    /**
     * Creates an instance of square bumper
     * @param x location in Display Coordinates
     * @param y location in Display Coordinates
     * @return an instance of a squareBumper
     */
    public static Gadget squareBumper(int x, int y) {
        return new SquareBumper(x,y);
    }
    
    /**
     * Creates an instance of circle bumper
     * @param x location in Display Coordinates
     * @param y location in Display Coordinates
     * @return an instance of a circleBumper
     */
    public static Gadget circleBumper(int x, int y) {
        return new CircleBumper(x,y);
    }
    
    /**
     * Creates an instance of triangle bumper
     * @param x location in Display Coordinates
     * @param y location in Display Coordinates
     * @param orientation either 0, 90, 180, 270 degrees; the default orientation (0 degrees) places 
     *              one corner in the northeast, one corner in the northwest, and the last corner in the southwest. 
     *              The diagonal goes from the southwest corner to the northeast corner 
     * @return an instance of a triangleBumper
     */
    public static Gadget triangleBumper(int x, int y, int orientation) {
        return new TriangleBumper(x,y,orientation);
    }
    
    /**
     * Creates an instance of left flipper
     * @param x location in Display Coordinates
     * @param y location in Display Coordinates
     * @param orientation 0, 90, 180, 270 degrees; the default orientation (0 degrees) 
     *              places the flipper’s pivot point in the northwest corner
     * @return an instance of a leftFlipper
     */
    public static Gadget leftFlipper(int x, int y, int orientation) {
        return new Flipper(x,y,orientation,false,true);
    }
    
    /**
     * Creates an instance of right flipper
     * @param x location in Display Coordinates
     * @param y location in Display Coordinates
     * @param orientation 0, 90, 180, 270 degrees; the default orientation (0 degrees)
     *               puts the pivot point in the northeast corner
     * @return an instance of a rightFlipper
     */
    public static Gadget rightFlipper(int x, int y, int orientation) {
        return new Flipper(x,y,orientation,false,false);
    }
    
    /**
     * Creates an instance of absorber
     * @param x location in Display Coordinates
     * @param y location in Display Coordinates
     * @param k length of the absorber in Display Coordinates 
     * @param m height of the absorber in Display Coordinates
     * @return an instance of an absorber
     */
    public static Gadget absorber(int x, int y, int k, int m) {
        return new Absorber(x,y,k,m);
    }

    /**
     * Getter for x location of gadget (top left)
     * @return the x location in Display Coordinates
     */
    public double getX();
    
    /**
     * Getter for y location of gadget (top right)
     * @return the y location in Display Coordinates
     */
    public double getY();
    
    /**
     * Getter for width of gadget
     * @return the width of a gadget in units L
     */
    public int getWidth();
    
    /**
     * Getter for height of gadget
     * @return the height of a gadget in units L
     */
    public int getHeight();
    
    /**
     * Draw gadget on given board.
     * @param board to draw the gadget on
     */
    public void drawGadgetOnBoard(Board board);
    
    /**
     * Get orientation of gadget
     * @return the orientation in degrees of the gadget
     */
    public int getOrientation();
    
    /**
     * Get reflection coefficient of gadget
     * @return the coefficient of reflection of this gadget
     */
    public double getReflectionCoeff();
    
    /**
     * Causes this gadget to trigger the passed in gadget to take action when this gadget is hit.
     * @param gadget to be triggered when this gadget is hit
     */
    public void addGadgetToTrigger(Gadget gadget);
    
    
    /**
     * When ball collides with this gadget, trigger all necessary actions 
     * @param b ball that collides with the gadget
     */
    public void triggerGadgets(Ball b);
    
    /**
     * Action that occurs when the gadget is triggered
     * @param ball which will be moved if absorber is triggered
     */
    public void activateGadget();
    
    /**
     * Checks to see if a ball collides with this gadget within the time frame [0, time], assuming the ball isn't inside it
     * IMPORTANT: assumes that time step is short enough to allow for instantaneous velocity 
     *          calculations, i.e. gravity and friction not accounted for, velocity is constant
     * @param ball to check collision path of
     * @param time in ms to check for a collision in
     * @return time before collision, unless it will not collide: then INFINITY
     */
    public double ballHitsGadgetThisTimestep(Ball ball, long time);

    /**
     * Mutates the ball's velocity to what it is after it collides with the gadget, assuming the ball isn't inside it
     * IMPORTANT: assumes that the ball is at the edge of the gadget, at point of impact 
     * @param ball that's going to collide with the gadget
     * @param time in ms that the collision will occur in 
     */
    public void collisionBallGadget(Ball ball, long time);
    
    /**
     * Returns whether the gadget is an absorber
     * @return true if it's an absorber.
     */
    public default boolean isAbsorber(){
        return false;
    }
    
    /**
     * Returns whether the gadget is an absorber
     * @return true if it's an absorber.
     */
    public default boolean isFlipper(){
        return false;
    }
    
    /**
     * Gets a list of outgoing messages for balls that that the portal has eaten; the list is empty for non-portal gadgets
     * @return a list of outgoing messages to send to other boards
     */
    public default List<GoodbyePortalBall> getGoodbyePortalBallMessages(){
        return new ArrayList<GoodbyePortalBall>(Arrays.asList());
    }
    
    /**
     * Clears list of balls to eject; does not affect non-portal gadgets
     */
    public default void emptyBallsToEject(){
        // do nothing
    }
    
    /**
     * Checks whether the gadget is a portal
     * @return true if it is a portal
     */
    public boolean isPortal();
    
    /**
     * Gets the name of the gadget
     * @return name of gadget
     */
    public String getName();
    
    /**
     * Gets a list of references to the gadgets that this gadget triggers
     * @return list of gadgets that this gadget triggers
     */
    public List<Gadget> getGadgetsToTrigger();
    
    /**
     * Gives a shape representing this gadget which can be drawn onto a PingballPanel.
     * The shape must be at the exact pixel coordinates that it would be on the pingball
     * board, to the scale given, with the origin at the upper left and coordinates increasing
     * down and to the right.
     * @param scale the number of pixels in the width and height of a single 1L by 1L square
     *        on the pingball board
     * @return the shape of this gadget at the correct location
     */
    public Shape getShape(int scale);
    
    /**
     * Returns the color the gadget is supposed to be drawn in on the user interface.
     * @return the intended color of the gadget on the interface
     */
    public Color getDrawColor();
    
    /**
     * Gets a list of names of the gadgets that this gadget triggers
     * @return names of gadgets that this gadget triggers
     */
    public default List<String> getNamesOfGadgetsToTrigger() {
        List<Gadget> gadgetsToTrigger = getGadgetsToTrigger();
        List<String> namesOfGadgets = new ArrayList<String>();
        for (Gadget gadget : gadgetsToTrigger) {
            if (gadget.getName() != null) {
                namesOfGadgets.add(gadget.getName());
            }
        }
        return namesOfGadgets;
    }
}
