package pingball;

import java.util.ArrayList;

import physics.*;

/**
 * Represents a circle bumper with diameter 1
 * 
 * rep invariant:  x and y coordinates must both be within the interval
 * [0,19], diameter must be 1, and coefficent of reflection
 * is 1.0
 *
 */
public class CircleBumper implements Gadget {
    
    private int x;
    private int y;
    private double radius;
    private double coefficientOfReflection;
    public boolean isTriggered;
    private Circle circle;
    private char symbol;
    private ArrayList<Gadget> triggerTargets;
    
    private static final double TIME_BETWEEN_FRAMES = 1.0/20.0;
    
    public CircleBumper(int x, int y) {
        this.x = x;
        this.y = y;
        this.circle = new Circle(this.x + 0.5, this.y + 0.5, 1.0);
        this.radius = 0.5;
        this.symbol = '0';
        this.coefficientOfReflection = 1.0;
        this.isTriggered = false;
        this.triggerTargets = new ArrayList<Gadget>();
        checkRep();
    }
    
    public CircleBumper(int x, int y, ArrayList<Gadget> triggerTargets) {
        this.x = x;
        this.y = y;
        this.circle = new Circle(this.x + 0.5, this.y + 0.5, 1.0);
        this.radius = 0.5;
        this.symbol = '0';
        this.coefficientOfReflection = 1.0;
        this.isTriggered = false;
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
    
    // A collision with another circle there the Gadget itself 
    // is stationary
    public void reflectBall(Ball ball) {
        Vect newBallVect = Geometry.reflectCircle(this.circle.getCenter(), new Vect(ball.getX(), ball.getY()), ball.getVelocity(), this.coefficientOfReflection);
        ball.changeVelocity(newBallVect);
        this.trigger(this);
        
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public double getCoefficientOfReflection() {
        return this.coefficientOfReflection;
    }
    
    public char getSymbol() {
        return this.symbol;
    }
    
    public int getWidth() {
        return (int)(this.radius*2);
    }
    
    public int getHeight() {
        return (int)(this.radius*2);
    }
    
    /**
     * Checks to see if the distance between the center of the circles
     * is smaller than the sum of their radii, with small amount of foresight buffer - avoids nasty edge cases with multiple circles
     */
    public boolean detectBallCollision(Ball ball) {
        if (Geometry.timeUntilCircleCollision(this.circle, ball.getCircle(), ball.getVelocity()) < TIME_BETWEEN_FRAMES/10){
            return true;
        }
        return false;
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
        assert (this.radius == 0.5);
        assert (this.coefficientOfReflection == 1.0);
    }

}
