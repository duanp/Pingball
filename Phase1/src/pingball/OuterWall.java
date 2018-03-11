package pingball;

import java.util.ArrayList;

import physics.*;

/**
 * This variant represents the horizontal or vertical outer walls 
 * of the board
 * 
 * rep invariant:  axes must either be 0 or 20
 */

public class OuterWall implements Gadget {
    private int axes;
    private boolean isVertical;
    private boolean isSolid;
    private double coefficientOfReflection;
    public boolean isTriggered;
    private char symbol;
    private int height;
    private int width;
    private ArrayList<Gadget> triggerTargets;
    
    public OuterWall(int axes, boolean isVertical, boolean isSolid) {
        this.axes = axes;
        this.isVertical = isVertical;
        this.isSolid = isSolid;
        this.coefficientOfReflection = 1.0;
        this.isTriggered = false;
        this.symbol = '.';
        if (this.isVertical){
        this.height = 20;
        this.width = 1;
        } else {
            this.height = 1;
            this.width = 20;
        }
        this.triggerTargets = new ArrayList<Gadget>();
        checkRep();
    }
    
    public OuterWall(int axes, boolean isVertical, boolean isSolid, ArrayList<Gadget> triggerTargets) {
        this.axes = axes;
        this.isVertical = isVertical;
        this.isSolid = isSolid;
        this.coefficientOfReflection = 1.0;
        this.isTriggered = false;
        this.symbol = '.';
        if (this.isVertical){
        this.height = 20;
        this.width = 1;
        } else {
            this.height = 1;
            this.width = 20;
        }
        this.triggerTargets = triggerTargets;
        checkRep();
    }
    
    public void trigger(Gadget gadget) {
        gadget.setIsTriggered(true);
        
    }
    
    public void action() {
        this.setIsTriggered(false);
        
        for (Gadget target: triggerTargets) {
            this.trigger(target);
        }
    }
    
    // reflects the ball with a coefficent of reflection of 1.0
    public void reflectBall(Ball ball) {
        LineSegment reflectingSegment = new LineSegment(0.,0.,1.,1.);
        if (this.isVertical){
            reflectingSegment = new LineSegment(0.,0.,0.,1.);
        }
        else {
            reflectingSegment = new LineSegment(0.,0.,1.,0.);
        }
        Vect newBallVect = Geometry.reflectWall(reflectingSegment, ball.getVelocity(), this.coefficientOfReflection);
        ball.changeVelocity(newBallVect);
    }
    
    public int getX() {
        throw new UnsupportedOperationException();
    }
    
    public int getY() {
        throw new UnsupportedOperationException();
    }
    
    public double getCoefficientOfReflection() {
        return this.coefficientOfReflection;
    }
    
    public char getSymbol() {
        return this.symbol;
    }
    
    public boolean detectBallCollision(Ball ball) {
        if (this.isVertical) {
            return Math.abs(this.axes - ball.getX()) < ball.getRadius();
        } else {
            return Math.abs(this.axes - ball.getY()) < ball.getRadius();
        }
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public int getHeight(){
        return this.height;
    }
    
    public void setIsTriggered(boolean isTriggered){
        this.isTriggered = isTriggered;
    }
    
    public boolean getIsTriggered(){
        return this.isTriggered;
    }
    
    public void checkRep(){
        assert (this.axes == 0 || this.axes == 20);
    }

}
