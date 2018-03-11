package pingball;
import java.util.ArrayList;

import physics.*;

/**
 * Gadgets consist of Square Bumpers, Circle Bumpers, Triangle Bumpers,
 * Left and Right Flippers, Absorbers, and the Outer Wall
 * Gadgets are placed at specific locations on the Board and should 
 * reflect or absorb Balls.  They can also trigger other Gadgets to perform
 * a certain action
 */

public interface Gadget {
    
    /**
     * Creates a new Square Bumper at the given x and y coordinates
     * @param x - x coordinate of Square Bumper, must within the interval [0,19]
     * @param y - y coordinate of Square Bumper, must within the interval [0,19]
     * @return a new Square Bumper object
     */
    public static Gadget squareBumper(int x, int y) {
        return new SquareBumper(x, y);
    }
    
    /**
     * Creates a new Square Bumper at the given x and y coordinates with triggered gadgets
     * @param x - x coordinate of Square Bumper, must within the interval [0,19]
     * @param y - y coordinate of Square Bumper, must within the interval [0,19]
     * @param triggerTargets a list of gadgets that are triggered on this gadget's action
     * @return a new Square Bumper object
     */
    public static Gadget squareBumper(int x, int y, ArrayList<Gadget> triggerTargets) {
        return new SquareBumper(x, y, triggerTargets);
    }
    
    /**
     * Creates a new Circle Bumper at the given x and y coordinates
     * @param x - x coordinate of Circle Bumper, must within the interval [0,19]
     * @param y - y coordinate of Circle Bumper, must within the interval [0,19]
     * @return a new Circle Bumper object
     */
    public static Gadget circleBumper(int x, int y) {
        return new CircleBumper(x, y);
    }
    
    /**
     * Creates a new Circle Bumper at the given x and y coordinates with triggered gadgets
     * @param x - x coordinate of Circle Bumper, must within the interval [0,19]
     * @param y - y coordinate of Circle Bumper, must within the interval [0,19]
     * @param triggerTargets a list of gadgets that are triggered on this gadget's action
     * @return a new Circle Bumper object
     */
    public static Gadget circleBumper(int x, int y, ArrayList<Gadget> triggerTargets) {
        return new CircleBumper(x, y, triggerTargets);
    }
    
    /**
     * Creates a new Triangle Bumper at the given x and y coordinates
     * @param x - x coordinate of Triangle Bumper, must within the interval [0,19]
     * @param y - y coordinate of Triangle Bumper, must within the interval [0,19]
     * @return a new Triangle Bumper object, must either be 0, 90, 180, or 270 degrees
     */
    public static Gadget triangleBumper(int x, int y, Angle a) {
        return new TriangleBumper(x, y, a);
    }
    
    /**
     * Creates a new Triangle Bumper at the given x and y coordinates with triggered gadgets
     * @param x - x coordinate of Triangle Bumper, must within the interval [0,19]
     * @param y - y coordinate of Triangle Bumper, must within the interval [0,19]
     * @param triggerTargets a list of gadgets that are triggered on this gadget's action
     * @return a new Triangle Bumper object, must either be 0, 90, 180, or 270 degrees
     */
    public static Gadget triangleBumper(int x, int y, Angle a, ArrayList<Gadget> triggerTargets) {
        return new TriangleBumper(x, y, a, triggerTargets);
    }
    
    /**
     * Creates a new (left or right) Flipper at the given x and y coordinates
     * @param x - x coordinate of Flipper, must within the interval [0,19]
     * @param y - y coordinate of Flipper, must within the interval [0,19]
     * @param isLeft - boolean indicating the type of flipper, true for left
     *                 flipper and false for right flipper
     * @return a new Flipper object
     */
    public static Gadget flipper(int x, int y, boolean isLeft, Angle a) {
        return new Flipper(x, y, isLeft, a);
    }
    
    /**
     * Creates a new (left or right) Flipper at the given x and y coordinates with triggered gadgets
     * @param x - x coordinate of Flipper, must within the interval [0,19]
     * @param y - y coordinate of Flipper, must within the interval [0,19]
     * @param isLeft - boolean indicating the type of flipper, true for left
     *                 flipper and false for right flipper
     * @param triggerTargets a list of gadgets that are triggered on this gadget's action
     * @return a new Flipper object
     */
    public static Gadget flipper(int x, int y, boolean isLeft, Angle a, ArrayList<Gadget> triggerTargets) {
        return new Flipper(x, y, isLeft, a, triggerTargets);
    }
    
    /**
     * Creates a new Absorber at the given x and y coordinates with given height and width
     * @param x - x coordinate of Absorber, must within the interval [0,19]
     * @param y - y coordinate of Absorber, must within the interval [0,19]
     * @param width - width of Absorber, must within the interval [1,20]
     * @param height - height of Absorber, must within the interval [1,20]
     * @return a new Absorber object
     */
    public static Gadget absorber(int x, int y, int width, int height) {
        return new Absorber(x, y, width, height);
    }
    
    /**
     * Creates a new Absorber at the given x and y coordinates with given height and width with triggered gadgets
     * @param x - x coordinate of Absorber, must within the interval [0,19]
     * @param y - y coordinate of Absorber, must within the interval [0,19]
     * @param width - width of Absorber, must within the interval [1,20]
     * @param height - height of Absorber, must within the interval [1,20]
     * @param triggerTargets a list of gadgets that are triggered on this gadget's action
     * @return a new Absorber object
     */
    public static Gadget absorber(int x, int y, int width, int height, ArrayList<Gadget> triggerTargets) {
        return new Absorber(x, y, width, height, triggerTargets);
    }
    
    /**
     * Creates a solid or invisible, vertical or horizontal outer wall at the given axes
     * @param axes - axes along which the wall lies, must either be 0 or 20
     * @param isVertical - boolean indicating whether this outer wall is vertical (true)
     *                     or horizontal (false)
     * @param isSolid - boolean indicating whether this outer wall is solid (true)
     *                  or invisible (false)
     * @return a new OuterWall object
     */
    public static Gadget outerWall(int axes, boolean isVertical, boolean isSolid) {
        return new OuterWall(axes, isVertical, isSolid);
    }
    
    /**
     * Creates a solid or invisible, vertical or horizontal outer wall at the given axes with triggered gadgets
     * @param axes - axes along which the wall lies, must either be 0 or 20
     * @param isVertical - boolean indicating whether this outer wall is vertical (true)
     *                     or horizontal (false)
     * @param isSolid - boolean indicating whether this outer wall is solid (true)
     *                  or invisible (false)
     * @param triggerTargets a list of gadgets that are triggered on this gadget's action
     * @return a new OuterWall object
     */
    public static Gadget outerWall(int axes, boolean isVertical, boolean isSolid, ArrayList<Gadget> triggerTargets) {
        return new OuterWall(axes, isVertical, isSolid, triggerTargets);
    }
    
    /**
     * Triggers another gadget to perform its action, called by Board when
     * it detects a collision with ball
     * @param gadget - the gadget whose action is to be triggered
     */
    public void trigger(Gadget gadget);
    
    /**
     * The action to be performed by the Gadget when triggered
     */
    public void action();
    
    /**
     * Reflects the Ball object upon collision
     * @param ball - the ball that collided with the gadget
     */
    public void reflectBall(Ball ball);
    
    /**
     * Gets the x-coordinate of Gadget
     * @return - x-coordinate of the Gadget, which is an integer
     *           in the range [0,19]
     */
    public int getX();
    
    /**
     * Gets the y-coordinate of Gadget
     * @return - y-coordinate of the Gadget, which is an integer
     *           in the range [0,19]
     */
    public int getY();
    
    /**
     * Gets the coefficient of reflection of the Gadget
     * @return - the coefficent of reflection of the Gadget, which is
     * a double
     */
    public double getCoefficientOfReflection();
    
    /**
     * Gets the symbol representing the Gadget on the board
     * @return the symbol representing the Gadget, which is a char
     */
    public char getSymbol();
    
    /**
     * Gets the width of the gadget (diameter if the gadget is circular)
     * @return - the number of L's this gadget's width occupies
     */
    public int getWidth();
    
    /**
     * Gets the height of the gadget (diameter if the gadget is circular)
     * @return - the number of L's this gadget's height occupies
     */
    public int getHeight();
    
    /**
     * Detects collisions with the ball 
     * @param ball - the ball that the gadget is checking collision with
     * @return - a boolean indicating whether or not the Gadget collides
     *          with the ball
     */
    public boolean detectBallCollision(Ball ball);
    
    /**
     * Set whether or not the gadget is triggered to the given boolean
     * @param isTriggered - boolean indicating whether or not the gadget
     *                      is triggered
     */
    public void setIsTriggered(boolean isTriggered);
    
    /**
     * Checks whether or not the gadget is triggered
     * @return - a boolean indicating whether or not the gadget is triggered
     */
    public boolean getIsTriggered();
    
    

}
