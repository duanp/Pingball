package pingball;
import java.util.ArrayList;
import java.util.HashMap;

import physics.*;

/**
 * Represents a triangle bumper of varying orientation with side length 1 and 
 * hypotenuse sqrt(2)
 * 
 * rep invariant:  x and y coordinates must both be within the interval
 * [0,19], side length must be 1, hypotenuse is sqrt(2) and coefficent of reflection
 * is 1.0
 *
 */
public class TriangleBumper implements Gadget {
    
    private int x;
    private int y;
    private int sideLength;
    private double hypotenuse;
    private double coefficentOfReflection;
    public boolean isTriggered;
    public Angle orientation;
    private char symbol;
    private ArrayList<LineSegment> sides;
    private ArrayList<Gadget> triggerTargets;
    
    private static final double TIME_BETWEEN_FRAMES = 1.0/20.0;
  
    
    /**
     * Creates a new triangular bumper
     * @param x the x coordinate of the top left corner of this triangle bumper
     * @param y the y coordinate of the top left corner of this triangle bumper
     * @param a the orientation of the triangle bumper. The default orientation (0 degrees) places one 
     *          corner in the northeast, one corner in the northwest, and the last corner in the 
     *          southwest. The diagonal goes from the southwest corner to the northeast corner.
     */
    public TriangleBumper(int x, int y, Angle a) {
        this.x = x;
        this.y = y;
        this.sideLength = 1;
        this.hypotenuse = Math.sqrt(2.);
        this.coefficentOfReflection = 1.0;
        this.isTriggered = false;
        this.orientation = a;
        this.sides = this.getBumperSides();
        if (a.equals(Angle.DEG_90) || a.equals(Angle.DEG_270)) {
            this.symbol = '\\';
        } else if (a.equals(Angle.DEG_180) || a.radians() == 0.0) {
            this.symbol = '/';
        }
        this.triggerTargets = new ArrayList<Gadget>();
        checkRep();
    }
    
    public TriangleBumper(int x, int y, Angle a, ArrayList<Gadget> triggerTargets) {
        this.x = x;
        this.y = y;
        this.sideLength = 1;
        this.hypotenuse = Math.sqrt(2.);
        this.coefficentOfReflection = 1.0;
        this.isTriggered = false;
        this.orientation = a;
        this.sides = this.getBumperSides();
        if (a.equals(Angle.DEG_90) || a.equals(Angle.DEG_270)) {
            this.symbol = '\\';
        } else if (a.equals(Angle.DEG_180) || a.radians() == 0.0) {
            this.symbol = '/';
        }
        this.triggerTargets = triggerTargets;
        checkRep();
    }
    
    /**
     * Triggered when a ball hits this triangle bumper
     */
    public void trigger(Gadget gadget) {
        gadget.setIsTriggered(true);
        
    }
    
    /**
     * Does nothing when triggered (ball hits this triangle bumper)
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
                return;
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
    
    public HashMap<String,Double> getSize() {
        HashMap<String, Double> sizeMap = new HashMap<String, Double>();
        sizeMap.put("side", (double)this.sideLength);
        sizeMap.put("hypotenuse", this.hypotenuse);
        return sizeMap;
    }
    
    public boolean detectBallCollision(Ball ball) {
        for (LineSegment side: this.sides){
            if (collideWithSide(side, ball)) {
                return true;
            }
        }
        return false;
    }
    
    public int getWidth(){
        return this.sideLength;
    }
    
    public int getHeight(){
        return this.sideLength;
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
    
    /** 
     * Creates a List of sides (represented by a list of the x, y coordinates of both endpoints) 
     * of the triangle bumper based on its orientation
     * @return - An ArrayList of ArrayList of Doubles in the following form:  [x1, y1, x2, y2], with 
     *          the two sides listed before the hypotenuse
     */
     public ArrayList<LineSegment> getBumperSides(){
         ArrayList<LineSegment> sideList = new ArrayList<LineSegment>();
         if (this.orientation.radians() == 0.0) {
             sideList.add(new LineSegment(this.x + 1.0, (double) this.y, (double) this.x, this.y + 1.0));
             sideList.add(new LineSegment((double)this.x, (double)this.y, this.x + 1.0, (double)this.y));
             sideList.add(new LineSegment((double)this.x, (double)this.y, (double)this.x, this.y + 1.0));         
         } else if (this.orientation.equals(Angle.DEG_90)) {
             sideList.add(new LineSegment((double)this.x, (double) this.y, this.x + 1.0, this.y + 1.0));
             sideList.add(new LineSegment((double)this.x, (double)this.y, this.x + 1.0, (double)this.y));
             sideList.add(new LineSegment(this.x + 1.0, (double)this.y, this.x + 1.0, this.y + 1.0));
         } else if (this.orientation.equals(Angle.DEG_180)) {
             sideList.add(new LineSegment(this.x + 1.0, (double) this.y, (double) this.x, this.y + 1.0));
             sideList.add(new LineSegment((double)this.x, this.y + 1.0, this.x + 1.0, this.y + 1.0));
             sideList.add(new LineSegment(this.x + 1.0, (double)this.y, this.x + 1.0, this.y + 1.0));
         } else if (this.orientation.equals(Angle.DEG_270)) {
             sideList.add(new LineSegment((double)this.x, (double) this.y, this.x + 1.0, this.y + 1.0));
             sideList.add(new LineSegment((double)this.x, this.y + 1.0, this.x + 1.0, this.y + 1.0));
             sideList.add(new LineSegment((double)this.x, (double)this.y, (double)this.x, this.y + 1.0));
         }
         return sideList;
         
     }
     
     public void setIsTriggered(boolean isTriggered){
         this.isTriggered = isTriggered;
     }
     
     public boolean getIsTriggered(){
         return this.isTriggered;
     }
    
    public void checkRep(){
        assert (this.x >= 0 && this.x <= 19);
        assert (this.y >= 0 && this.y <= 19);
        assert (this.sideLength == 1);
        assert (this.hypotenuse == Math.sqrt(2.));
        assert (this.coefficentOfReflection == 1.0);
    }
}
