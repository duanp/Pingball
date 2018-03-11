package pingball.Gadgets;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import physics.Circle;
import physics.Geometry;
import physics.LineSegment;
import physics.Vect;
import pingball.Ball;
import pingball.Board;

/**
 * A mutable class to represent a triangle bumper
 */
public class TriangleBumper implements Gadget {
    
    /* 
     * AF: the bumper is made of 3 line segments and 3 circles that connect the lines, which are
     *  created based on the orientation of the bumper
     * 
     * RI: the bumper never overlaps with other objects on the board, including balls and gadgets;
     *      the bumper is always within the bounds of the board
     * 
     */
    
    private String NAME;
    private final double X_COORD;
    private final double Y_COORD;
    private int orientation;
    private List<Gadget> gadgetsToTrigger;
    
    private Object[] lineComponents;
    private Object[] circleComponents;

    /**
     * creator for triangle bumper
     * @param x x-location of the bumper
     * @param y y-location of the bumper
     * @param orientation orientation of bumper. Can be 0, 90, 180, or 270 only.
     */
    public TriangleBumper(double x, double y, int orientation) {
        this.X_COORD = x;
        this.Y_COORD = y;
        this.orientation = orientation;
        gadgetsToTrigger = new ArrayList<Gadget>();
        
        double xCoord = this.getX();
        double yCoord = -1*this.getY();
        lineComponents = new LineSegment[3];
        circleComponents = new Circle[3];
        if (orientation == 0){
            lineComponents[0] = new LineSegment(xCoord,yCoord,xCoord+1,yCoord);
            lineComponents[1] = new LineSegment(xCoord+1,yCoord,xCoord,yCoord-1);
            lineComponents[2] = new LineSegment(xCoord,yCoord-1,xCoord,yCoord);
            circleComponents[0] = new Circle(xCoord,yCoord,0);
            circleComponents[1] = new Circle(xCoord+1,yCoord,0);
            circleComponents[2] = new Circle(xCoord,yCoord-1,0);
        }
        else if (orientation == 90){
            lineComponents[0] = new LineSegment(xCoord,yCoord,xCoord+1,yCoord);
            lineComponents[1] = new LineSegment(xCoord+1,yCoord,xCoord+1,yCoord-1);
            lineComponents[2] = new LineSegment(xCoord+1,yCoord-1,xCoord,yCoord);
            circleComponents[0] = new Circle(xCoord,yCoord,0);
            circleComponents[1] = new Circle(xCoord+1,yCoord,0);
            circleComponents[2] = new Circle(xCoord+1,yCoord-1,0);
        }
        else if (orientation == 180){
            lineComponents[0] = new LineSegment(xCoord+1,yCoord,xCoord+1,yCoord-1);
            lineComponents[1] = new LineSegment(xCoord+1,yCoord-1,xCoord,yCoord-1);
            lineComponents[2] = new LineSegment(xCoord,yCoord-1,xCoord+1,yCoord);
            circleComponents[0] = new Circle(xCoord+1,yCoord,0);
            circleComponents[1] = new Circle(xCoord+1,yCoord-1,0);
            circleComponents[2] = new Circle(xCoord,yCoord-1,0);
        }
        else if (orientation == 270){
            lineComponents[0] = new LineSegment(xCoord,yCoord,xCoord+1,yCoord-1);
            lineComponents[1] = new LineSegment(xCoord+1,yCoord-1,xCoord,yCoord-1);
            lineComponents[2] = new LineSegment(xCoord,yCoord-1,xCoord,yCoord);
            circleComponents[0] = new Circle(xCoord,yCoord,0);
            circleComponents[1] = new Circle(xCoord+1,yCoord-1,0);
            circleComponents[2] = new Circle(xCoord,yCoord-1,0);
        }
        else {
            throw new RuntimeException("Bad triangle orientation");
        }
        checkRep();

    }
    
    /**
     * creator for triangle bumper
     * @param name the name of the gadget
     * @param x x-location of the bumper
     * @param y y-location of the bumper
     * @param orientation orientation of bumper. Can be 0, 90, 180, or 270 only.
     */
    public TriangleBumper(String name, double x, double y, int orientation) {
        this(x, y, orientation);
        this.NAME = name;
        checkRep();
    }
    /**
     * 
     * Check to make sure rep invariant is preserved: Bumper can only be in the 4 allowed orientations,
     * and must lie entirely within the board.
     */
    private void checkRep(){
    	try {
			assertTrue(this.getX() < 20);
			assertTrue(this.getY() < 20);
			assertTrue(this.getX() >= 0);
			assertTrue(this.getY() >= 0);
    	} catch (IndexOutOfBoundsException e) {
    	    System.err.println("IndexOutOfBoundsException: " + e.getMessage());
		}
    	
    	try {
    		assertTrue((this.getOrientation() == 0) ||(this.getOrientation() == 90)
    				|| (this.getOrientation() == 180) || (this.getOrientation() == 270));  
    	}	catch(RuntimeException e) {
        		System.err.println("Orientation can only be 0, 90, 180, or 270: " + e.getMessage());
    	}
    	
    	try {
    		int numOfCircles = 3;
    		assertTrue(circleComponents.length == numOfCircles);
    		for (int i = 0; i < numOfCircles; i++){
    			assertTrue(((Circle) (this.circleComponents[i])).getRadius() == 0);
    		}
    	} catch (IndexOutOfBoundsException e) {
    	    System.err.println("Broken Circle Components: " + e.getMessage());
		}
    	
    	try {
    		int numOfLines = 3;
    		assertTrue(this.lineComponents.length == numOfLines);
    		for (int i = 0; i < numOfLines; i++){
    			assertTrue(((LineSegment) (this.lineComponents[i])).length() == 1 || 
    			        ((LineSegment) (this.lineComponents[i])).length() == Math.sqrt(2));
    		}
    	} catch (IndexOutOfBoundsException e) {
    	    System.err.println("Broken Line Components: " + e.getMessage());
		}
    }
    
    @Override
    public double getY() {
        return this.Y_COORD;
    }

    @Override
    public double getX() {
        return this.X_COORD;
    }
    
    @Override
    public int getOrientation() {
        return this.orientation;
    }

    @Override
	public int getWidth() {
		return 1;
	}

	@Override
	public int getHeight() {
		return 1;
	}
    
    @Override
    public void drawGadgetOnBoard(Board board) {
        String[][] boardDisplayArray = board.getDisplayArray();
        int X = (int)this.getX();
        int Y = (int)this.getY();
        if (this.getOrientation() == 0 || this.getOrientation() == 180){
            boardDisplayArray[Y][X] = "/";         
        }
        else if (this.getOrientation() == 90 || this.getOrientation() == 270){
            boardDisplayArray[Y][X] = "\\";        
        }
        else {
            throw new RuntimeException("Bad orientation!"); // should probably be a different name or something?
        }
        //board.setDisplayArray(boardDisplayArray);
    }

    @Override
    public double getReflectionCoeff() {
        return 1.0;
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
		// No action
	}

    @Override
    public double ballHitsGadgetThisTimestep(Ball ball, long time) {       
        Geometry.setForesight(time);
        Circle cartesianBallCircle = new Circle(ball.getPosition().x(),-1*ball.getPosition().y(),ball.getRadius());
        Vect cartesianBallVel = new Vect(ball.getVelocity().x(),-1*ball.getVelocity().y());
        for (Object lineComponent : lineComponents){
            if (Geometry.timeUntilWallCollision((LineSegment) lineComponent, cartesianBallCircle, cartesianBallVel)<=time/1000.0){
                
                return Geometry.timeUntilWallCollision((LineSegment) lineComponent, cartesianBallCircle, cartesianBallVel);
            }
        }
        for (Object circleComponent : circleComponents){
            if (Geometry.timeUntilCircleCollision((Circle) circleComponent, cartesianBallCircle, cartesianBallVel)<=time/1000.0){
                return Geometry.timeUntilCircleCollision((Circle) circleComponent, cartesianBallCircle, cartesianBallVel);
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public void collisionBallGadget(Ball ball, long time) {
        Geometry.setForesight(time);
        double soonestWallTime = Double.POSITIVE_INFINITY;
        double soonestCircleTime = Double.POSITIVE_INFINITY;
        LineSegment closestWall = new LineSegment(0,0,0,0);
        Circle closestCircle = new Circle(0,0,0);
        Circle cartesianBallCircle = new Circle(ball.getPosition().x(),-1*ball.getPosition().y(),ball.getRadius());
        Vect cartesianBallVel = new Vect(ball.getVelocity().x(),-1*ball.getVelocity().y());
        for (Object lineComponent : lineComponents){    
            if (Geometry.timeUntilWallCollision((LineSegment) lineComponent, cartesianBallCircle, cartesianBallVel)<soonestWallTime){
                soonestWallTime = Geometry.timeUntilWallCollision((LineSegment) lineComponent, cartesianBallCircle, cartesianBallVel);
                closestWall = (LineSegment) lineComponent;
            }
        }
        for (Object circleComponent : circleComponents){
            if (Geometry.timeUntilCircleCollision((Circle) circleComponent, cartesianBallCircle, cartesianBallVel)<soonestCircleTime){
                soonestCircleTime = Geometry.timeUntilCircleCollision((Circle) circleComponent, cartesianBallCircle, cartesianBallVel);
                closestCircle = (Circle) circleComponent;
            }
        }
        if (soonestWallTime <= soonestCircleTime && soonestWallTime < (time + 10)/1000.){
            collisionBallWall(ball, closestWall);
        }
        else if (soonestCircleTime <= soonestWallTime && soonestCircleTime < (time + 10)/1000.){
            collisionBallCircle(ball, closestCircle);
        }
        else {
            throw new RuntimeException("Um there's nothing to collide with?"); 
        }
            

    }
    
    /**
     * Mutates the ball's velocity to what it is after it collides with the wall
     * IMPORTANT: assumes that the ball is at the edge of the wall, at point of impact 
     * @param ball of the gadget that is getting hit
     * @param lineComponent
     */
    private void collisionBallWall(Ball ball, Object lineComponent) {
        //Circle cartesianBallCircle = new Circle(ball.getPosition().x(),-1*ball.getPosition().y(),ball.getRadius());
        Vect cartesianBallVel = new Vect(ball.getVelocity().x(),-1*ball.getVelocity().y());
        Vect newVelocity = Geometry.reflectWall((LineSegment)lineComponent,cartesianBallVel, this.getReflectionCoeff());  
        ball.setVelocity(newVelocity);
    }

    /**
     * Mutates the ball's velocity to what it is after it collides with the circle
     * IMPORTANT: assumes that the ball is at the edge of the circle, at point of impact 
     * @param ball that's going to collide with the gadget
     * @param circleComponent of the gadget that is getting hit
     */
    private void collisionBallCircle(Ball ball, Object circleComponent) {
        Circle cartesianBallCircle = new Circle(ball.getPosition().x(),-1*ball.getPosition().y(),ball.getRadius());
        Vect cartesianBallVel = new Vect(ball.getVelocity().x(),-1*ball.getVelocity().y());
        Vect newVelocity = Geometry.reflectCircle(((Circle) circleComponent).getCenter(), cartesianBallCircle.getCenter(), cartesianBallVel, this.getReflectionCoeff());
        ball.setVelocity(newVelocity);
    }

    @Override
    public boolean equals(Object other){
        if (other instanceof TriangleBumper){
            TriangleBumper otherBumper = (TriangleBumper) other;
            if (this.getX() == otherBumper.getX()
                    && this.getY() == otherBumper.getY()
                    && this.getWidth() == otherBumper.getWidth()
                    && this.getHeight() == otherBumper.getHeight()
                    && this.getNamesOfGadgetsToTrigger().equals(otherBumper.getNamesOfGadgetsToTrigger())
                    && Arrays.equals(this.lineComponents,otherBumper.lineComponents)
                    && Arrays.equals(this.circleComponents,otherBumper.circleComponents)
                    && this.orientation==otherBumper.orientation
                    && (this.NAME == otherBumper.NAME || (this.NAME != null && 
                        otherBumper.NAME != null && this.NAME.equals(otherBumper.NAME)))) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "TriangleBumper with name " + NAME + " X " 
                    + this.getX() + " Y " + this.getY() + " width " + this.getWidth() + " height " + this.getHeight() + " orientation " + this.orientation;
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
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];
        int index = 0;
        if (orientation != 180){
            xPoints[index] = (int) (X_COORD * scale);
            yPoints[index] = (int) (Y_COORD * scale);
            index += 1;
        }
        if (orientation != 270){
            xPoints[index] = (int) ((X_COORD+1) * scale);
            yPoints[index] = (int) (Y_COORD * scale);
            index += 1;
        }
        if (orientation != 0){
            xPoints[index] = (int) ((X_COORD+1) * scale);
            yPoints[index] = (int) ((Y_COORD+1) * scale);
            index += 1;
        }
        if (orientation != 90){
            xPoints[index] = (int) (X_COORD * scale);
            yPoints[index] = (int) ((Y_COORD+1) * scale);
            index += 1;
        }
        int numSides = 3;
        return new Polygon(xPoints, yPoints, numSides);
    }
    
    @Override
    public Color getDrawColor(){
        return new Color(128,64,32);
    }
}
