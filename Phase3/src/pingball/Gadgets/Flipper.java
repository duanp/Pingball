package pingball.Gadgets;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import physics.Angle;
import physics.Circle;
import physics.Geometry;
import physics.LineSegment;
import physics.Vect;
import pingball.Ball;
import pingball.Board;

/**
 *
 * A mutable class to represent a flipper in the Pingball game.
 * Balls that collide with it will reflect off it. If something triggers it,
 * it flips clockwise or counterclockwise depending on whether it is a left
 * or right flipper.
 *
 */
public class Flipper implements Gadget{
    
    // Abstraction function: 
    //   Represents a flipper in the Pingball game.
    //
    // Rep invariant:
    //   Orientation is 0, 90, 180, or 270
    //   It has exactly two corners, each represented by a circle of radius 0
    //   It has exactly one 'edge' of length 2
    
    private String NAME;
    private final double X_COORD;
    private final double Y_COORD;
    private final double ROTATION_SPEED = 18.85; //rotation speed in rad/sec of flipper
    private final double TIME_OF_ROTATION = 83.33; // time it takes to flip the flipper 90 degrees
    private int orientation;
    private List<Gadget> gadgetsToTrigger;
    private long lastFlipInitiated;
    private boolean isFlipped;
    
    private Object[] lineComponents;
    private Object[] circleComponents;
    private Vect pivotPoint;
    private Vect endPoint;
    private boolean isLeft;
    
    private double rotationTimeSoFar;
    private int motion; // 0 is no rotation, 1 is rotation in positive direction,
                        // -1 is rotation in negative direction
    
    /**
     * Flipper creates a left or right flipper;
     *      left flipper  begins its rotation in a counterclockwise direction
     *      right flipper begins its rotation in a clockwise direction
     * @param x coordinate in display coords
     * @param y coordinate in display coords
     * @param orientation - which of [0,90,180,270] is the flipper based at?  
     * @param rotated - is the flipper flipped?
     * @param isLeft - true if this is a left flipper, false if this is a right flipper
     */
    public Flipper(double x, double y, int orientation, boolean isFlipped, boolean isLeft) {
        this.X_COORD = x;
        this.Y_COORD = y;
        this.orientation = orientation;
        this.isLeft = isLeft;
        this.gadgetsToTrigger = new ArrayList<Gadget>();
        this.lastFlipInitiated = 0;
        this.isFlipped = isFlipped;
        this.pivotPoint = new Vect(0, 0);
        this.endPoint = new Vect(0,0);
        this.motion = 0;
        this.rotationTimeSoFar = 0.0;

        double xCoord = this.getX();
        double yCoord = -1*this.getY();
        lineComponents = new LineSegment[1];
        circleComponents = new Circle[2];
        
        if (this.isLeft){ // set pivotPoint and endPoint for a left flipper
            if (orientation == 0){
                pivotPoint = new Vect(xCoord, yCoord);
                endPoint = new Vect(xCoord,yCoord-2);  
            }        
            else if (orientation == 90){
                pivotPoint = new Vect(xCoord+2, yCoord);
                endPoint = new Vect(xCoord,yCoord);  
            }
            else if (orientation == 180){
                pivotPoint = new Vect(xCoord+2, yCoord-2);
                endPoint = new Vect(xCoord+2,yCoord);  
            }
            else if (orientation == 270){
                pivotPoint = new Vect(xCoord, yCoord-2);
                endPoint = new Vect(xCoord+2,yCoord-2);  
            }
            else{
                throw new RuntimeException("Flipper orientation is bad"); 
            }
        }
        if (!this.isLeft){ // set pivotPoint and endPoint for a right flipper
            if (orientation == 0){
                pivotPoint = new Vect(xCoord+2, yCoord);
                endPoint = new Vect(xCoord+2,yCoord-2);  
            }        
            else if (orientation == 90){
                pivotPoint = new Vect(xCoord+2, yCoord-2);
                endPoint = new Vect(xCoord,yCoord-2);  
            }
            else if (orientation == 180){
                pivotPoint = new Vect(xCoord, yCoord-2);
                endPoint = new Vect(xCoord,yCoord);  
            }
            else if (orientation == 270){
                pivotPoint = new Vect(xCoord, yCoord);
                endPoint = new Vect(xCoord+2,yCoord);  
            }
            else{
                throw new RuntimeException("Flipper orientation is bad"); 
            }
        }


        lineComponents[0] = new LineSegment(pivotPoint.x(),pivotPoint.y(),endPoint.x(),endPoint.y());
        circleComponents[0] = new Circle(pivotPoint.x(),pivotPoint.y(),0);
        circleComponents[1] = new Circle(endPoint.x(),endPoint.y(),0);
        
        checkRep();
    }

    /**
     * Flipper creates a left or right flipper;
     *      left flipper  begins its rotation in a counterclockwise direction
     *      right flipper begins its rotation in a clockwise direction
     * @param name the name of the gadget
     * @param x coordinate
     * @param y coordinate
     * @param orientation - which of [0,90,180,270] is the flipper based at?  
     * @param rotated - is the flipper flipped?
     * @param isLeft - true if this is a left flipper, false if this is a right flipper
     */
    public Flipper(String name, double x, double y, int orientation, boolean isFlipped, boolean isLeft) {
        this(x, y, orientation, isFlipped, isLeft);
        this.NAME = name;
        checkRep();
    }
    
    /**
     * 
     * Check to make sure rep invariant is preserved: Flipper can only be in the 4 allowed orientations 
     * and 2 allowed rotations, and must lie entirely within the board.
     * They must contain 4 circleComponents of radius 0, and 4 lineComponents of length 2.
     */
    private void checkRep(){
        try {
            assertTrue(this.getX() < 19);
            assertTrue(this.getY() < 19);
            assertTrue(this.getX() >= 0);
            assertTrue(this.getY() >= 0);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("IndexOutOfBoundsException: " + e.getMessage());
        }
        
        try {
            assertTrue((this.getOrientation() == 0) || (this.getOrientation() == 90)
                    || (this.getOrientation() == 180) || (this.getOrientation() == 270));  
        }   catch(RuntimeException e) {
                System.err.println("Orientation can only be 0,90,180,270: " + e.getMessage());
        }
        
        try {
            int numOfCircles = 2;
            assertTrue(this.circleComponents.length == numOfCircles);
            for (int i = 0; i < numOfCircles; i++){
                assertTrue(((Circle) (this.circleComponents[i])).getRadius() == 0);
            }
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Broken Circle Components: " + e.getMessage());
        }
        
        try {
            int numOfLines = 1;
            assertTrue(this.lineComponents.length == numOfLines);
            for (int i = 0; i < numOfLines; i++){
                assertTrue(((LineSegment) (this.lineComponents[i])).length() == 2);
            }
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Broken Line Components: " + e.getMessage());
        }
    }

    @Override
    public double getX() {
        return this.X_COORD;

    }

    @Override
    public double getY() {
        return this.Y_COORD;
    }
    
    @Override
    public int getOrientation() {
        return this.orientation;
    }
    
    @Override
    public int getWidth() {
        return 2;
    }

    @Override
    public int getHeight() {
        return 2;
    }
    
    /**
     * Gets the time at which the last flip was initiated
     * @return time of last flip 
     */
    private long getLastFlipTime(){
        return this.lastFlipInitiated;
    }
    
    /**
     * Gets the pivot point about which the flipper rotates
     * @return the pivot point of the flipper as if the board were in cartesian coordinates
     */
    private Vect getPivotPoint(){
        return this.pivotPoint;
    }
    
    /**
     * Gets end point of the flipper as if the board were in cartesian coordinates
     * @return the flipper end point
     */
    private Vect getEndPoint(){
      return this.endPoint;
    }
    
    /**
     * Sets the end point of the flipper as if the board were in cartesian coordinates
     */
    private void setEndPoint(Vect vector){
      this.endPoint = vector;
      lineComponents[0] = new LineSegment(this.getPivotPoint().x(),this.getPivotPoint().y(),vector.x(),vector.y());
    }
    
    /**
     * Gets whether a flipper has been flipped
     * @return true if flipped
     */
    private boolean getIsFlipped(){
        return this.isFlipped;
    }
    
    @Override
    public void drawGadgetOnBoard(Board board) {
        String[][] boardDisplayArray = board.getDisplayArray();
        int X = (int) this.getX();
        int Y = (int) this.getY();
        
        if (this.isLeft){
            if (this.getOrientation() == 0){
                if (!this.getIsFlipped()){
                    boardDisplayArray[Y][X] = "|";   
                    boardDisplayArray[Y+1][X] = "|";  
                }
                else{
                    boardDisplayArray[Y][X] = "-";    
                    boardDisplayArray[Y][X+1] = "-"; 
                }
            }
            else if (this.getOrientation() == 90){
                if (!this.getIsFlipped()){
                    boardDisplayArray[Y][X] = "-";   
                    boardDisplayArray[Y][X+1] = "-";  
                }
                else{
                    boardDisplayArray[Y][X+1] = "|";    
                    boardDisplayArray[Y+1][X+1] = "|"; 
                }
            }
            else if (this.getOrientation() == 180){
                if (!this.getIsFlipped()){
                    boardDisplayArray[Y][X+1] = "|";   
                    boardDisplayArray[Y+1][X+1] = "|";  
                }
                else{
                    boardDisplayArray[Y+1][X] = "-";    
                    boardDisplayArray[Y+1][X+1] = "-"; 
                }
            }
            else if (this.getOrientation() == 270){
                if (!this.getIsFlipped()){
                    boardDisplayArray[Y+1][X] = "-";   
                    boardDisplayArray[Y+1][X+1] = "-";  
                }
                else{
                    boardDisplayArray[Y][X] = "|";    
                    boardDisplayArray[Y+1][X] = "|"; 
                }
            }
            else {
                throw new RuntimeException("Bad orientation!"); 
            } 
        }
        else if (!this.isLeft){
            if (this.getOrientation() == 0){
                if (!this.getIsFlipped()){
                    boardDisplayArray[Y][X+1] = "|";   
                    boardDisplayArray[Y+1][X+1] = "|";  
                }
                else{
                    boardDisplayArray[Y][X] = "-";    
                    boardDisplayArray[Y][X+1] = "-"; 
                }
            }
            else if (this.getOrientation() == 90){
                if (!this.getIsFlipped()){
                    boardDisplayArray[Y+1][X] = "-";   
                    boardDisplayArray[Y+1][X+1] = "-";  
                }
                else{
                    boardDisplayArray[Y][X+1] = "|";    
                    boardDisplayArray[Y+1][X+1] = "|"; 
                }
            }
            else if (this.getOrientation() == 180){
                if (!this.getIsFlipped()){
                    boardDisplayArray[Y][X] = "|";   
                    boardDisplayArray[Y+1][X] = "|";  
                }
                else{
                    boardDisplayArray[Y+1][X] = "-";    
                    boardDisplayArray[Y+1][X+1] = "-"; 
                }
            }
            else if (this.getOrientation() == 270){
                if (!this.getIsFlipped()){
                    boardDisplayArray[Y][X] = "-";   
                    boardDisplayArray[Y][X+1] = "-";  
                }
                else{
                    boardDisplayArray[Y][X] = "|";    
                    boardDisplayArray[Y+1][X] = "|"; 
                }
            }
            else {
                throw new RuntimeException("Bad orientation!"); 
            }
        }
        else {
            throw new RuntimeException("Not a left or right flipper?");
        }
    }

    @Override
    public double getReflectionCoeff() {
        return .95;
    }

    @Override
    public void addGadgetToTrigger(Gadget gadget) {
        gadgetsToTrigger.add(gadget);        
    }

    @Override
    public void triggerGadgets(Ball b) {
        int numberOfGadgetsBeingTriggered = this.gadgetsToTrigger.size();
        for (int i = 0; i < numberOfGadgetsBeingTriggered; i++){
            this.gadgetsToTrigger.get(i).activateGadget();
        }
    }

    @Override
    public void activateGadget() {
        if (motion != 0){
            return; // don't want the flipper to react if it's already moving
        }
        this.lastFlipInitiated = System.currentTimeMillis();
        if ((this.isFlipped && this.isLeft) || (!this.isFlipped && !this.isLeft)){
            motion = -1;
        } 
        else if ((!this.isFlipped && this.isLeft) || (this.isFlipped && !this.isLeft)) {
            motion = 1;
        }
        else {
            throw new RuntimeException("Bad flipper/flipped combo");
        }
        rotationTimeSoFar = 0.0;
        this.isFlipped = !this.getIsFlipped();
    }
    
    /**
     * Moves the flipper the amount necessary, stopping it if necessary.
     */
    public void updateFlipper(long time){
        if (time + rotationTimeSoFar < TIME_OF_ROTATION){
            Vect newEndpoint = Geometry.rotateAround(this.getEndPoint(), this.getPivotPoint(), new Angle(ROTATION_SPEED * time/1000.0 * motion));
            this.setEndPoint(newEndpoint);
            this.circleComponents[1] = new Circle(endPoint.x(),endPoint.y(),0);
            rotationTimeSoFar += time;
        } else {
            double t = TIME_OF_ROTATION - rotationTimeSoFar;
            Vect newEndpoint = Geometry.rotateAround(this.getEndPoint(), this.getPivotPoint(), new Angle(ROTATION_SPEED * t/1000.0 * motion));
            this.setEndPoint(newEndpoint);
            this.circleComponents[1] = new Circle(endPoint.x(),endPoint.y(),0);
            motion = 0;
        }
    }

    @Override
    public double ballHitsGadgetThisTimestep(Ball ball, long time) {  
        Geometry.setForesight(time);
        Circle cartesianCircle = new Circle(ball.getPosition().x(),-1*ball.getPosition().y(),ball.getRadius()); // the ball's position
        Vect cartesianVelocity = new Vect(ball.getVelocity().x(),-1*ball.getVelocity().y());    // the ball's velocity
        long timeElapsed = System.currentTimeMillis() - this.getLastFlipTime();
        if (timeElapsed <= TIME_OF_ROTATION){ // it's been rotated
            Angle angleToRotate = new Angle(0);
            // Define the direction to rotate, depending on left/right flipper and if it's flipping up/down
            if (this.isFlipped){ // it was just flipped
                // set initial x,y position of endpoint
                if (this.isLeft){
                    angleToRotate = new Angle(timeElapsed*ROTATION_SPEED);   
                }
                else if (!this.isLeft){
                    angleToRotate = new Angle(-timeElapsed*ROTATION_SPEED);
                }
                else {
                    throw new RuntimeException("angleToRotate not assigned");
                }
            }
            
            else if (!this.isFlipped){ // it was just unflipped
                if (this.isLeft){
                    angleToRotate = new Angle(-timeElapsed*ROTATION_SPEED);    
                }
                else if (!this.isLeft){
                    angleToRotate = new Angle(timeElapsed*ROTATION_SPEED);
                }
                else {
                    throw new RuntimeException("angleToRotate not assigned");
                } 
            }
            else{
                throw new RuntimeException("Um flipper problem");
            }
            // define the endpoint and the line, considering the initial partial rotation
            Circle rotatingEnd = new Circle(this.getEndPoint(),0); //a circle representing the initial location and size of the rotating circle
            rotatingEnd = Geometry.rotateAround(rotatingEnd,this.getPivotPoint(),angleToRotate); // partially rotate it to where it is at the beginning of the timestep
            LineSegment rotatingLine = new LineSegment(this.getPivotPoint(), this.getEndPoint()); //a line representing the initial location and size of the rotating line
            rotatingLine = Geometry.rotateAround(rotatingLine,this.getPivotPoint(),angleToRotate); // partially rotate it to where it is at the beginning of the timestep
            Vect center = this.getPivotPoint(); // the point about which it's rotating
            // rotate the endpoint circle and the line of the clipper
            if ((Geometry.timeUntilRotatingCircleCollision(rotatingEnd,center,ROTATION_SPEED,cartesianCircle,cartesianVelocity))<=time/1000.){
                     return Geometry.timeUntilRotatingCircleCollision(rotatingEnd,center,ROTATION_SPEED,cartesianCircle,cartesianVelocity);
            }
            if ((Geometry.timeUntilRotatingWallCollision(rotatingLine,center,ROTATION_SPEED,cartesianCircle,cartesianVelocity))<=time/1000.){
                    return Geometry.timeUntilRotatingWallCollision(rotatingLine,center,ROTATION_SPEED,cartesianCircle,cartesianVelocity);
            } 
        }
        else {

            for (Object lineComponent : lineComponents){
                if (Geometry.timeUntilWallCollision((LineSegment) lineComponent, cartesianCircle, cartesianVelocity)<=time/1000.){
                    return Geometry.timeUntilWallCollision((LineSegment) lineComponent, cartesianCircle, cartesianVelocity);
                }
            }
            for (Object circleComponent : circleComponents){
                if (Geometry.timeUntilCircleCollision((Circle) circleComponent, cartesianCircle, cartesianVelocity)<=time/1000.){
                    return Geometry.timeUntilCircleCollision((Circle) circleComponent, cartesianCircle, cartesianVelocity);
                }
            }   
        }
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public void collisionBallGadget(Ball ball, long time) {
        Geometry.setForesight(time);
        Circle cartesianCircle = new Circle(ball.getPosition().x(),-1*ball.getPosition().y(),ball.getRadius());
        Vect cartesianVelocity = new Vect(ball.getVelocity().x(),-1*ball.getVelocity().y());
        long timeElapsed = System.currentTimeMillis() - this.getLastFlipTime();
        if (timeElapsed <= TIME_OF_ROTATION){ // it's been rotated 
            Angle angleToRotate = new Angle(0);
            // Define the direction to rotate, depending on left/right flipper and if it's flipping up/down
            if (this.isFlipped){ // it was just flipped               
                // set initial x,y position of endpoint
                if (this.isLeft){
                    angleToRotate = new Angle(timeElapsed*ROTATION_SPEED);   
                }
                else if (!this.isLeft){
                    angleToRotate = new Angle(-timeElapsed*ROTATION_SPEED);
                }
                else {
                    throw new RuntimeException("angleToRotate not assigned");
                }
            }
            
            else if (!this.isFlipped){ // it was just unflipped
                if (this.isLeft){
                    angleToRotate = new Angle(-timeElapsed*ROTATION_SPEED);    
                }
                else if (!this.isLeft){
                    angleToRotate = new Angle(timeElapsed*ROTATION_SPEED);
                }
                else {
                    throw new RuntimeException("angleToRotate not assigned");
                } 
            }
            else{
                throw new RuntimeException("Um flipper problem");
            }
            // define the endpoint and the line, considering the initial partial rotation
            Circle rotatingEnd = new Circle(this.getEndPoint(),0); //a circle representing the initial location and size of the rotating circle
            rotatingEnd = Geometry.rotateAround(rotatingEnd,this.getPivotPoint(),angleToRotate); // partially rotate it to where it is at the beginning of the timestep
            LineSegment rotatingLine = new LineSegment(this.getPivotPoint(), this.getEndPoint()); //a line representing the initial location and size of the rotating line
            rotatingLine = Geometry.rotateAround(rotatingLine,this.getPivotPoint(),angleToRotate); // partially rotate it to where it is at the beginning of the timestep
            Vect center = this.getPivotPoint(); // the point about which it's rotating
            // rotate the endpoint circle and the line of the clipper
            if ((Geometry.timeUntilRotatingCircleCollision(rotatingEnd,center,ROTATION_SPEED,cartesianCircle,cartesianVelocity))<=time/1000.){
                    Vect newVel = Geometry.reflectRotatingCircle(rotatingEnd,center,ROTATION_SPEED,cartesianCircle,cartesianVelocity,this.getReflectionCoeff());
                    ball.setVelocity(vectMakeDisplay(newVel));
            }
            if ((Geometry.timeUntilRotatingWallCollision(rotatingLine,center,ROTATION_SPEED,cartesianCircle,cartesianVelocity))<=time/1000.){
                    Vect newVel = Geometry.reflectRotatingWall(rotatingLine,center,ROTATION_SPEED,cartesianCircle,cartesianVelocity,this.getReflectionCoeff());
                    ball.setVelocity(vectMakeDisplay(newVel));
            } 
        }
        else {

            for (Object lineComponent : lineComponents){                
                if (Geometry.timeUntilWallCollision((LineSegment) lineComponent, cartesianCircle, cartesianVelocity)<=time/1000.){
                    Vect newVel = Geometry.reflectWall((LineSegment) lineComponent, cartesianVelocity, this.getReflectionCoeff());
                    ball.setVelocity(vectMakeDisplay(newVel));
                }
            }
            for (Object circleComponent : circleComponents){
                if (Geometry.timeUntilCircleCollision((Circle) circleComponent, cartesianCircle, cartesianVelocity)<=time/1000.){
                    Vect newVel = Geometry.reflectCircle(((Circle) circleComponent).getCenter(), cartesianCircle.getCenter(), cartesianVelocity, this.getReflectionCoeff());
                    ball.setVelocity(vectMakeDisplay(newVel));
                }
            }   
        }
            
            
        
    }
    
    /**
     * Takes a vector in either display or cartesian coordinates and converts it to display coordinates.
     * @param vector to convert
     * @return the vector in display coordinates
     */
    private Vect vectMakeDisplay(Vect vector){
        Vect displayVect = new Vect(vector.x(), -1*vector.y());
        return displayVect;
    }
    
    /**
     * Takes a vector in display coordinates and converts it to cartesian coordinates.
     * @param vector to convert
     * @return the vector in cartesian coordinates
     */
    private Vect vectMakeCart(Vect vector){
        Vect cartVect = new Vect(vector.x(), -1*vector.y());
        return cartVect;
    }
    
    /**
     * Takes a circle in cartesian coordinates and converts it to cartesian coordinates.
     * @param circ circle to convert
     * @return circle in display coordinates
     */
    private Circle circleMakeDisplay(Circle circ){
        Circle displayCirc = new Circle(circ.getCenter().x(), -1*circ.getCenter().y(), circ.getRadius());
        return displayCirc;
    }
    
    /**
     * Takes a circle in display coordinates and converts it to cartesian coordinates.
     * @param circ circle to convert
     * @return circle in cartesian coordinates
     */
    private Circle circleMakeCart(Circle circ){
        Circle displayCirc = new Circle(circ.getCenter().x(), -1*circ.getCenter().y(), circ.getRadius());
        return displayCirc;
    }

    @Override
    public boolean isFlipper(){
        return true;
    }
    
    @Override
    public boolean equals(Object other){
        if (other instanceof Flipper){
            Flipper otherFlipper = (Flipper) other;
            if (this.getX() == otherFlipper.getX()
                    && this.getY() == otherFlipper.getY()
                    && this.getWidth() == otherFlipper.getWidth()
                    && this.getHeight() == otherFlipper.getHeight()
                    && this.getNamesOfGadgetsToTrigger().equals(otherFlipper.getNamesOfGadgetsToTrigger())
                    && Arrays.equals(this.lineComponents,otherFlipper.lineComponents)
                    && Arrays.equals(this.circleComponents,otherFlipper.circleComponents)
                    && this.orientation == otherFlipper.orientation
                    && this.isLeft == otherFlipper.isLeft
                    && this.isFlipped == otherFlipper.isFlipped
                    && this.pivotPoint.equals(otherFlipper.pivotPoint)
                    && this.endPoint.equals(otherFlipper.endPoint)
                    &&  (this.NAME == otherFlipper.NAME || (this.NAME != null && otherFlipper.NAME != null && this.NAME.equals(otherFlipper.NAME)))){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Flipper with name " + NAME;
    }
    
    @Override
    public boolean isPortal() {
        return false;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<Gadget> getGadgetsToTrigger() {
        return gadgetsToTrigger;
    }
    
    @Override
    public Shape getShape(int scale){
        int numSides = 4;
        double pivotX = pivotPoint.x();
        double pivotY = pivotPoint.y();
        double endX = endPoint.x();
        double endY = endPoint.y();
        
        // The way I'm drawing the flipper is I'm drawing a line from the pivot point to 
        // the end point and then making that line about 3 pixels thick by making it a
        // rectangle.  The original, width 0 line is in the middle of the thicker line.
        // So in order to that, I had to do some trigonometry.
        double slope = ((double) (endY - pivotY))/(endX - pivotX);
        slope = -1.0/slope;
        double widthX = 1.5 * Math.cos(Math.atan(slope));
        double widthY = 1.5 * Math.sin(Math.atan(slope));
        int[] xs = {(int) (pivotX*scale + widthX), (int) (pivotX*scale - widthX), (int) (endX*scale - widthX), (int) (endX*scale + widthX)};
        int[] ys = {-(int) (pivotY*scale - widthY), -(int) (pivotY*scale + widthY), -(int) (endY*scale + widthY), -(int) (endY*scale - widthY)};
        return new Polygon(xs, ys, numSides);
    }
    
    @Override
    public Color getDrawColor(){
        return Color.RED;
    }
}
