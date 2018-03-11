package pingball;

import java.util.ArrayList;

import physics.*;

/**
 * This variant represents left or right flippers that will switch orientations
 * (from vertical to horizontal or vice versa)
 * 
 * rep invariant:  x and y coordinates must both be within the interval
 * [0,19], side length must be 2, and coefficent of reflection
 * is 0.95
 *
 */

public class Flipper implements Gadget {
    
    private int x;
    private int y;
    private boolean isLeft;
    private boolean isHorizontal;
    private int sideLength;
    private double coefficientOfReflection; 
    public boolean isTriggered;
    public Angle orientation;
    public char symbol;
    private double angularVelocity;
    private double fixedX;
    private double fixedY;
    public LineSegment side;
    // angular velocity is 1080 degrees/second
    private static final int MOVING_ANGULAR_VELOCITY = 1080;
    // Framerate is 20 fps
    private static final double TIME_BETWEEN_FRAMES = 1.0/20.0;
    public double totalRotatedAngle;
    private ArrayList<Gadget> triggerTargets;
    
    public Flipper(int x, int y, boolean isLeft, Angle a) {
        this.x = x;
        this.y = y;
        this.isLeft = isLeft;
        this.sideLength = 2;
        this.coefficientOfReflection = 0.95;
        this.isHorizontal = false;
        this.isTriggered = false;
        this.orientation = a;
        if (a.equals(Angle.DEG_90) || a.equals(Angle.DEG_270)) {
            this.symbol = '-';
        } else if (a.equals(Angle.DEG_180) || a.radians() == 0.0) {
            this.symbol = '|';
        }
        this.angularVelocity = 0.0;
        if (this.isLeft){
            if (this.orientation.radians() == 0.0) {
                this.fixedX = (double) this.x;
                this.fixedY = (double) this.y;
                this.side = new LineSegment(this.fixedX, this.fixedY, this.fixedX, this.fixedY + this.sideLength);
            } else if (this.orientation.equals(Angle.DEG_90)){
                this.fixedX = (double) this.x + this.sideLength;
                this.fixedY = (double) this.y;
                this.side = new LineSegment(this.fixedX, this.fixedY, this.fixedX - this.sideLength, this.fixedY);
            } else if (this.orientation.equals(Angle.DEG_180)){
                this.fixedX = (double) this.x + this.sideLength;
                this.fixedY = (double) this.y + this.sideLength;
                this.side = new LineSegment(this.fixedX, this.fixedY, this.fixedX, this.fixedY - this.sideLength);
            } else if (this.orientation.equals(Angle.DEG_270)) {
                this.fixedX = (double) this.x;
                this.fixedY = (double) this.y + this.sideLength;
                this.side = new LineSegment(this.fixedX, this.fixedY, this.fixedX + this.sideLength, this.fixedY);
            }
        } else {
            if (this.orientation.radians() == 0.0) {
                this.fixedX = (double) this.x + this.sideLength;
                this.fixedY = (double) this.y;
                this.side = new LineSegment(this.fixedX, this.fixedY, this.fixedX, this.fixedY + this.sideLength);
            } else if (this.orientation.equals(Angle.DEG_90)) {
                this.fixedX = (double) this.x + this.sideLength;
                this.fixedY = (double) this.y + this.sideLength;
                this.side = new LineSegment(this.fixedX, this.fixedY, this.fixedX - this.sideLength, this.fixedY);
            } else if (this.orientation.equals(Angle.DEG_180)){
                this.fixedX = (double) this.x;
                this.fixedY = (double) this.y + this.sideLength;
                this.side = new LineSegment(this.fixedX, this.fixedY, this.fixedX, this.fixedY - this.sideLength);
            } else if (this.orientation.equals(Angle.DEG_270)) {
                this.fixedX = (double) this.x;
                this.fixedY = (double) this.y;
                this.side = new LineSegment(this.fixedX, this.fixedY, this.fixedX + this.sideLength, this.fixedY);
            }
            
        }
        this.totalRotatedAngle = 0.;
        this.triggerTargets = new ArrayList<Gadget>();
        checkRep();

    }
    
    public Flipper(int x, int y, boolean isLeft, Angle a, ArrayList<Gadget> triggerTargets) {
        this.x = x;
        this.y = y;
        this.isLeft = isLeft;
        this.sideLength = 2;
        this.coefficientOfReflection = 0.95;
        this.isHorizontal = false;
        this.isTriggered = false;
        this.orientation = a;
        if (a.equals(Angle.DEG_90) || a.equals(Angle.DEG_270)) {
            this.symbol = '-';
        } else if (a.equals(Angle.DEG_180) || a.radians() == 0.0) {
            this.symbol = '|';
        }
        this.angularVelocity = 0.0;
        if (this.isLeft){
            if (this.orientation.radians() == 0.0) {
                this.fixedX = (double) this.x;
                this.fixedY = (double) this.y;
                this.side = new LineSegment(this.fixedX, this.fixedY, this.fixedX, this.fixedY + this.sideLength);
            } else if (this.orientation.equals(Angle.DEG_90)){
                this.fixedX = (double) this.x + this.sideLength;
                this.fixedY = (double) this.y;
                this.side = new LineSegment(this.fixedX, this.fixedY, this.fixedX - this.sideLength, this.fixedY);
            } else if (this.orientation.equals(Angle.DEG_180)){
                this.fixedX = (double) this.x + this.sideLength;
                this.fixedY = (double) this.y + this.sideLength;
                this.side = new LineSegment(this.fixedX, this.fixedY, this.fixedX, this.fixedY - this.sideLength);
            } else if (this.orientation.equals(Angle.DEG_270)) {
                this.fixedX = (double) this.x;
                this.fixedY = (double) this.y + this.sideLength;
                this.side = new LineSegment(this.fixedX, this.fixedY, this.fixedX + this.sideLength, this.fixedY);
            }
        } else {
            if (this.orientation.radians() == 0.0) {
                this.fixedX = (double) this.x + this.sideLength;
                this.fixedY = (double) this.y;
                this.side = new LineSegment(this.fixedX, this.fixedY, this.fixedX, this.fixedY + this.sideLength);
            } else if (this.orientation.equals(Angle.DEG_90)) {
                this.fixedX = (double) this.x + this.sideLength;
                this.fixedY = (double) this.y + this.sideLength;
                this.side = new LineSegment(this.fixedX, this.fixedY, this.fixedX - this.sideLength, this.fixedY);
            } else if (this.orientation.equals(Angle.DEG_180)){
                this.fixedX = (double) this.x;
                this.fixedY = (double) this.y + this.sideLength;
                this.side = new LineSegment(this.fixedX, this.fixedY, this.fixedX, this.fixedY - this.sideLength);
            } else if (this.orientation.equals(Angle.DEG_270)) {
                this.fixedX = (double) this.x;
                this.fixedY = (double) this.y;
                this.side = new LineSegment(this.fixedX, this.fixedY, this.fixedX + this.sideLength, this.fixedY);
            }
            
        }
        this.totalRotatedAngle = 0.;
        this.triggerTargets = triggerTargets;
        checkRep();

    }
    
    public void trigger(Gadget gadget) {
        gadget.setIsTriggered(true);
        
    }
    
    // Every time its called, rotates the flipper by the appropiate angle.  Afterwards, performs a check to 
    // see if the flipper has reached its new state and then terminates flipper rotation
    public void action() {
        // determines the appropiate angle of rotataion
        if (this.isHorizontal && this.isLeft || !this.isHorizontal && !this.isLeft) {
            this.angularVelocity = MOVING_ANGULAR_VELOCITY;
        } else {
            this.angularVelocity = -1.0*MOVING_ANGULAR_VELOCITY;
        }
        this.moveFlipper();
        this.totalRotatedAngle += MOVING_ANGULAR_VELOCITY*TIME_BETWEEN_FRAMES;
        // Checks to see if the flipper has rotated to its new state
        if (this.totalRotatedAngle >= 90.) {
            // makes sure flipper only rotates 90 degrees
            if (this.isHorizontal && this.isLeft || !this.isHorizontal && !this.isLeft) {
                this.side = Geometry.rotateAround(this.side, new Vect(this.fixedX, this.fixedY), new Angle((90. - totalRotatedAngle)/180.*Math.PI));
            } else {
                this.side = Geometry.rotateAround(this.side, new Vect(this.fixedX, this.fixedY), new Angle((totalRotatedAngle - 90.)/180.*Math.PI));
            }
            this.isHorizontal = !this.isHorizontal;
            if (this.symbol == '-'){
                this.symbol = '|';
            }
            else if (this.symbol == '|'){
                this.symbol = '-';
            }
            this.totalRotatedAngle = 0.;
            this.setIsTriggered(false);
            this.angularVelocity = 0.;           
        }
        
        for (Gadget target: triggerTargets) {
            this.trigger(target);
        }
    }
    
    // reflect Ball while moving or while stationary with a coefficent
    // of reflection of 0.95
    public void reflectBall(Ball ball) {
        Vect newBallVect = Geometry.reflectRotatingWall(this.side, new Vect(this.fixedX, this.fixedY), this.angularVelocity, ball.getCircle(), ball.getVelocity(), this.coefficientOfReflection);
        ball.changeVelocity(newBallVect);
        this.trigger(this);
    }
    
    /**
     * rotates the flipper by an angle equal to its velocity*time_between_frames
     */
    public void moveFlipper(){
        this.side = Geometry.rotateAround(this.side, new Vect(this.fixedX, this.fixedY), new Angle(this.angularVelocity*TIME_BETWEEN_FRAMES/180.*Math.PI));
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
    
    /**
     * Returns where the flipper should drawn relative to its 2L x 2L bounding box
     * @return "top", "bottom", "left", or "right" depending on where in its 2L x 2L bounding box the flipper should be drawn
     */
    public String getBoardSquarePosition() {
        int rotationsFromTop = 0;
        //0 = top, 1 = left, 2 = bottom, 3 = right
        if (this.isHorizontal){
            rotationsFromTop = 0;
        }
        else{
            if (this.isLeft){
                rotationsFromTop = 1;
            }
            else{
                rotationsFromTop = 3;
            }
        }
        //account for orientation
        if (this.orientation.equals(Angle.DEG_90)){
            rotationsFromTop = (rotationsFromTop + 3) % 4;
        }
        else if (this.orientation.equals(Angle.DEG_180)){
            rotationsFromTop = (rotationsFromTop + 2) % 4;
        }
        else if (this.orientation.equals(Angle.DEG_270)){
            rotationsFromTop = (rotationsFromTop + 1) % 4;
        }
        System.out.println(rotationsFromTop);
        switch (rotationsFromTop){
        case 0: return "top";
        case 1: return "left";
        case 2: return "bottom";
        default: return "right";
        }
    }
    
    public boolean detectBallCollision(Ball ball) {
        if (ball.getX() > this.getX() && ball.getX() < this.getX() + this.sideLength){
            if (ball.getY() > this.getY() && ball.getY() < this.getY() + this.sideLength){
                if (Geometry.distanceSquared(this.side.p1(), new Vect(ball.getX(),ball.getY())) < this.sideLength + ball.getRadius()){
                    return true;
                }
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
    
    public void setIsTriggered(boolean isTriggered){
        this.isTriggered = isTriggered;
    }
    
    public boolean getIsTriggered(){
        return this.isTriggered;
    }
    
    public void checkRep(){
        assert (this.x >= 0 && this.x <= 18);
        assert (this.y >= 0 && this.y <= 18);
        assert (this.sideLength == 2);
        assert (this.coefficientOfReflection == 0.95);
    }
}
