package pingball;
import java.util.ArrayList;

import physics.*;

/**
 * This variant represents an absorber that absorbs balls upon collision and 
 * releases them when triggered
 * 
 * rep invariant:  x and y coordinates must both be within the interval
 * [0,19], side length must be 1 and height and width bust both be within
 * the interval [1,20]
 *
 */
public class Absorber implements Gadget {
    
    private int x;
    private int y;
    private int width;
    private int height;
    public boolean isTriggered;
    private char symbol;
    private ArrayList<Ball> absorbedBalls;
    private ArrayList<LineSegment> sides;
    private ArrayList<Gadget> triggerTargets;
    public boolean hasBall;
    public int numStoredBalls;
    private boolean selfTriggering;
    
    public Absorber(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isTriggered = false;
        this.symbol = '=';
        this.absorbedBalls = new ArrayList<Ball>();
        this.sides = getAbsorberSides();
        this.triggerTargets = new ArrayList<Gadget>();
        this.hasBall = false;
        this.numStoredBalls = 0;
        this.selfTriggering = false;
        checkRep();
    }
    
    public Absorber(int x, int y, int width, int height, ArrayList<Gadget> triggerTargets) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isTriggered = false;
        this.symbol = '=';
        this.absorbedBalls = new ArrayList<Ball>();
        this.sides = getAbsorberSides();
        this.triggerTargets = triggerTargets;
        this.selfTriggering = false;
        checkRep();
    }
    
    public Absorber(int x, int y, int width, int height, ArrayList<Gadget> triggerTargets, boolean selfTriggering) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isTriggered = false;
        this.symbol = '=';
        this.absorbedBalls = new ArrayList<Ball>();
        this.sides = getAbsorberSides();
        this.triggerTargets = triggerTargets;
        this.selfTriggering = selfTriggering;
        checkRep();
    }
    
    public void trigger(Gadget gadget) {
        gadget.setIsTriggered(true);
    }
    
    // It will release a ball it absorbed each time it is
    // triggered
    public void action() {
        if (this.numStoredBalls > 0){
            /*
            System.out.println(this.howManyAbsorbedBalls());
            Ball releasedBall = this.absorbedBalls.remove(0);
            System.out.println(releasedBall.getX());
            System.out.println(releasedBall.getY());
            System.out.println(releasedBall.getVelocity());
            releasedBall.setIsAbsorbed(false);
            releasedBall.changeCoordinate(this.getX() + this.getWidth() - 0.25, this.getY() + this.getHeight() - 0.25);
            releasedBall.changeVelocity(new Vect(0., 50.));
            System.out.println(this.howManyAbsorbedBalls());
            System.out.println(releasedBall.getX());
            System.out.println(releasedBall.getY());
            System.out.println(releasedBall.getVelocity());*/
            this.hasBall = true;
            this.numStoredBalls--;
        }
        this.setIsTriggered(false);
        for (Gadget target: triggerTargets) {
            this.trigger(target);
        }
        if (this.selfTriggering){
            this.trigger(this);
        }
    }
    
    // It will hold the ball in the upper right hang corner upon 
    // collision, NOT reflect it
    public void reflectBall(Ball ball) {
        ball.setIsAbsorbed(true);
        Vect newBallVect = new Vect(0.,0.);
        ball.changeVelocity(newBallVect);
        this.numStoredBalls++;
        this.absorbedBalls.add(ball);       
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public double getCoefficientOfReflection() {
        throw new UnsupportedOperationException();
    }
    
    public char getSymbol() {
        return this.symbol;
    }
    
    public boolean detectBallCollision(Ball ball) {
        double xLow = (double) this.x;
        double xHigh = (double) this.x + this.width;
        double yLow = (double) this.y;
        double yHigh = (double) this.y + this.height;
        // first check to see if ball is inside absorber
        if (ball.getX() >= xLow && ball.getX() <= xHigh &&
            ball.getY() >= yLow && ball.getY() <= yHigh){
            return true;
        } 
        for (LineSegment side: this.sides) {
            if (collideWithSide(side, ball)) {
                return true;
            }
        }
        return false;
        
    }
    
    public void setIsTriggered(boolean isTriggered){
        this.isTriggered = isTriggered;
    }
    
    public int getWidth(){
        return this.width;
    }
    
    /**
     * Determines whether or not the absorber has a ball to release
     * @return - boolean indicating whether or not the absorber has
     *           a ball to release
     */
    public boolean getHasBall(){
        return this.hasBall;
    }
    
    /**
     * Gets the number of balls stored in the absorber
     * @return - integer indicating the number of balls stored in absorber
     */
    public int getNumStoredBalls(){
        return this.numStoredBalls;
    }
    
    /**
     * Sets the hasBall field of Absorber
     * @param hasBall - boolean of which hasBall of the absorber should be set to
     */
    public void setHasBall(boolean hasBall){
        this.hasBall = hasBall;
    }
    
    public int getHeight(){
        return this.height;
    }
    
    public boolean getIsTriggered(){
        return this.isTriggered;
    }
    
    public boolean collideWithSide(LineSegment segment, Ball ball) {
        Vect ballCenter = new Vect(ball.getX(), ball.getY());
        Vect perpendicularPoint = Geometry.perpendicularPoint(segment, ballCenter);
        if (perpendicularPoint != null){
            if (Math.sqrt(Geometry.distanceSquared(perpendicularPoint, ballCenter)) <= ball.getRadius()){
                return true;
            }
            
        }
        if (Math.sqrt(Geometry.distanceSquared(segment.p1(), ballCenter)) <= ball.getRadius() || 
            Math.sqrt(Geometry.distanceSquared(segment.p2(), ballCenter)) <= ball.getRadius()) {
            return true;
        }
        return false;
           
    }
    
    public ArrayList<LineSegment> getAbsorberSides(){
        ArrayList<LineSegment> sideList = new ArrayList<LineSegment>();
        sideList.add(new LineSegment((double)this.x, (double)this.y, (double)this.x + this.width, (double)this.y));
        sideList.add(new LineSegment((double)this.x, (double)this.y, (double)this.x, (double)this.y + this.height));
        sideList.add(new LineSegment((double)this.x + this.width, (double) this.y, (double)this.x + this.width, (double)this.y + this.height));
        sideList.add(new LineSegment((double) this.x, (double)this.y + this.height, (double)this.x + this.width, (double)this.y + this.height));
        return sideList;
        
    }
    
    /**
     * Returns how many balls are currently absorbed by this Absorber
     * @return The number of Ball elements absorbed by this Absorber
     */
    public int howManyAbsorbedBalls(){
        return this.absorbedBalls.size();
    }
 
    
    public void checkRep(){
        assert (this.x >= 0 && this.x <= 19);
        assert (this.y >= 0 && this.y <= 19);
        assert (this.height >= 1 && this.height <= 20);
        assert (this.width >= 1 && this.width <= 20);
    }
}
