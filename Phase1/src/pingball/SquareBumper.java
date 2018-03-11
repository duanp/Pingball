package pingball;
import java.util.ArrayList;

import physics.*;

/**
 * Represents a square bumper with side length 1
 * 
 * rep invariant:  x and y coordinates must both be within the interval
 * [0,19], height and width must both be 1, and coefficent of reflection
 * is 1.0
 *
 */
public class SquareBumper implements Gadget{
    private int x;
    private int y;
    private int sideLength;
    private char symbol;
    private double coefficentOfReflection;
    public boolean isTriggered;
    private ArrayList<LineSegment> sides;
    private ArrayList<Gadget> triggerTargets;
    
    public static final double COEFFICIENT_OF_REFLECTION = 1.0;
    
    private static final double TIME_BETWEEN_FRAMES = 1.0/20.0;
    
    /**
     * Makes a new square bumper with a given location of its top left corner
     * @param x the x coordinate of the top left corner of the square bumper
     * @param y the y coordinate of the top left corner of the square bumper
     */
    public SquareBumper(int x, int y) {
        this.x = x;
        this.y = y;
        this.symbol = '#';
        this.coefficentOfReflection = COEFFICIENT_OF_REFLECTION;
        this.sideLength = 1;
        this.isTriggered = false;
        this.sides = this.getBumperSides();
        this.triggerTargets = new ArrayList<Gadget>();
        checkRep();
    }
    
    public SquareBumper(int x, int y, ArrayList<Gadget> triggerTargets) {
        this.x = x;
        this.y = y;
        this.symbol = '#';
        this.coefficentOfReflection = COEFFICIENT_OF_REFLECTION;
        this.sideLength = 1;
        this.isTriggered = false;
        this.sides = this.getBumperSides();
        this.triggerTargets = triggerTargets;
        checkRep();
    }
    
    /**
     * Triggered when a ball hits this square bumper
     */
    public void trigger(Gadget gadget) {
        gadget.setIsTriggered(true);
    }
    
    /**
     * Does nothing after when triggered (ball hits this square bumper)
     */
    public void action() {
        this.setIsTriggered(false);
        for (Gadget target: triggerTargets) {
            this.trigger(target);
        }
        
    }
    
    public void reflectBall(Ball ball) {
        for (LineSegment side: this.sides) {
            if (collideWithSide(side, ball)) {
                Vect newBallVect = Geometry.reflectWall(side, ball.getVelocity(), this.coefficentOfReflection);
                ball.changeVelocity(newBallVect);
                this.trigger(this);
                break;
            }   
        }  
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public double getCoefficientOfReflection() {
        return this.coefficentOfReflection;
    }
    
    public char getSymbol() {
        return this.symbol;
    }
    
    public int getWidth(){
        return this.sideLength;
    }
    
    public int getHeight(){
        return this.sideLength;
    }
    
    public boolean detectBallCollision(Ball ball) {
        for (LineSegment side: this.sides) {
            if (collideWithSide(side, ball)) {
                return true;
            }
        }
        return false;
    }
    
    public void checkRep(){
        assert (this.x >= 0 && this.x <= 19);
        assert (this.y >= 0 && this.y <= 19);
        assert (this.sideLength == 1);
        assert (this.coefficentOfReflection == 1.0);
    }
    
    
    
    /**
     * Helper to detectBallCollision and reflectBall, checks if the ball collides with 
     * the given side of the bumper
     * @param segment - the side of the bumper checked for collision
     * @param ball - the ball checked for collision
     * @return - true if the ball collides with the side
     */
    public boolean collideWithSide(LineSegment segment, Ball ball) {
        // Checks for collision with the interior of the line segment and
        // both endpoints
        return Geometry.timeUntilWallCollision(segment, ball.getCircle(), ball.getVelocity()) < TIME_BETWEEN_FRAMES/2;
            
        
        
    }
    
    public void setIsTriggered(boolean isTriggered){
        this.isTriggered = isTriggered;
    }
    
    public boolean getIsTriggered(){
        return this.isTriggered;
    }
    
    
   /** 
    * Creates a List of sides (represented by a list of the x, y coordinates of both endpoints) 
    * of the square bumper
    * @return - An ArrayList of containing ArrayLists of Doubles in the following form:  [x1, y1, x2, y2]
    */
    public ArrayList<LineSegment> getBumperSides(){
        ArrayList<LineSegment> sideList = new ArrayList<LineSegment>();
        sideList.add(new LineSegment((double)this.x, (double)this.y, this.x + 1.0, (double)this.y));
        sideList.add(new LineSegment((double)this.x, (double)this.y, (double)this.x, this.y + 1.0));
        sideList.add(new LineSegment(this.x + 1.0, (double) this.y, this.x + 1.0, this.y + 1.0));
        sideList.add(new LineSegment((double) this.x, this.y + 1.0, this.x + 1.0, this.y + 1.0));
        return sideList;
        
    }

}
