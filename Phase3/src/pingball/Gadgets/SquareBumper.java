package pingball.Gadgets;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import physics.*;
import pingball.Ball;
import pingball.Board;
import static org.junit.Assert.assertTrue;

/**
 * A mutable class to represent a square bumper in the Pingball game.
 * Balls that collide with it will reflect off it.
 */
public class SquareBumper implements Gadget {

    // Abstraction function: 
    //   Represents a square bumper in the Pingball game.
    //
    // Rep invariant:
    //  It is contained within the board
    //  It has exactly four corners represented by four circles of radius 0
    //  It has exactly four edges each of length 1
    
    private String NAME;
    private final double X_COORD;
    private final double Y_COORD;

    private List<Gadget> gadgetsToTrigger;

    private Object[] lineComponents;
    private Object[] circleComponents;

    /**
     * SquareBumper creates a square bumper with edge length 1L
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public SquareBumper(double x,double y) {
        this.X_COORD = x;
        this.Y_COORD = y;
        gadgetsToTrigger = new ArrayList<Gadget>();

        
        double xCoord = this.getX();
        double yCoord = -1*this.getY();
        lineComponents = new LineSegment[4];
        lineComponents[0] = new LineSegment(xCoord,yCoord,xCoord+1,yCoord);
        lineComponents[1] = new LineSegment(xCoord+1,yCoord,xCoord+1,yCoord-1);
        lineComponents[2] = new LineSegment(xCoord+1,yCoord-1,xCoord,yCoord-1);
        lineComponents[3] = new LineSegment(xCoord,yCoord-1,xCoord,yCoord);
        circleComponents = new Circle[4];
        circleComponents[0] = new Circle(xCoord,yCoord,0);
        circleComponents[1] = new Circle(xCoord+1,yCoord,0);
        circleComponents[2] = new Circle(xCoord+1,yCoord-1,0);
        circleComponents[3] = new Circle(xCoord,yCoord-1,0);
        
        checkRep();
    }

    /**
     * SquareBumper creates a square bumper with edge length 1L
     * @param name the name of the gadget
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public SquareBumper(String name, double x,double y) {
        this(x, y);
        this.NAME = name;
        checkRep();
    }

   
    
    /**
     * Asserts the rep invariant: 
     *      the bumper must be within the bounds of the board
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
        		int numOfCircles = 4;
        		assertTrue(this.circleComponents.length == numOfCircles);
        		for (int i = 0; i < numOfCircles; i++){
        			assertTrue(((Circle) (this.circleComponents[i])).getRadius() == 0);
        		}
        	} catch (IndexOutOfBoundsException e) {
        	    System.err.println("Broken Circle Components: " + e.getMessage());
    		}
        	
        	try {
        		int numOfLines = 4;
        		assertTrue(this.lineComponents.length == numOfLines);
        		for (int i = 0; i < numOfLines; i++){
        			assertTrue(((LineSegment) (this.lineComponents[i])).length() == 1);
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
        boardDisplayArray[Y][X] = "#";
        board.setDisplayArray(boardDisplayArray);
    }

    @Override
    public int getOrientation() {
        return 0;
    }

    @Override
    public double getReflectionCoeff() {
        return 1;
    }

    @Override
    public void addGadgetToTrigger(Gadget gadget) {
        gadgetsToTrigger.add(gadget);
    }

    @Override
    public double ballHitsGadgetThisTimestep(Ball ball, long time) {       
        Geometry.setForesight(time);
        Circle cartesianCircle = new Circle(ball.getPosition().x(),-1*ball.getPosition().y(),ball.getRadius());
        Vect cartesianVelocity = new Vect(ball.getVelocity().x(),-1*ball.getVelocity().y());
        for (Object lineComponent : lineComponents){
            if (Geometry.timeUntilWallCollision((LineSegment) lineComponent, cartesianCircle, cartesianVelocity)<=time/1000){
                return Geometry.timeUntilWallCollision((LineSegment) lineComponent, cartesianCircle, cartesianVelocity);
            }
        }
        for (Object circleComponent : circleComponents){
            if (Geometry.timeUntilCircleCollision((Circle) circleComponent, cartesianCircle, cartesianVelocity)<=time/1000){
                return Geometry.timeUntilCircleCollision((Circle) circleComponent, cartesianCircle, cartesianVelocity);
            }
        }
        return Double.POSITIVE_INFINITY;
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
    public void collisionBallGadget(Ball ball, long time) {
        Geometry.setForesight(time);
        double soonestWallTime = time + 10;
        double soonestCircleTime = time + 10;
        LineSegment closestWall = new LineSegment(0,0,0,0);
        Circle closestCircle = new Circle(0,0,0);
        Circle cartesianCircle = new Circle(ball.getPosition().x(),-1*ball.getPosition().y(),ball.getRadius());
        Vect cartesianVelocity = new Vect(ball.getVelocity().x(),-1*ball.getVelocity().y());
        for (Object lineComponent : lineComponents){    
            if (Geometry.timeUntilWallCollision((LineSegment) lineComponent, cartesianCircle, cartesianVelocity)<=soonestWallTime){
                soonestWallTime = Geometry.timeUntilWallCollision((LineSegment) lineComponent, cartesianCircle, cartesianVelocity);
                closestWall = (LineSegment) lineComponent;
            }
        }
        for (Object circleComponent : circleComponents){
            if (Geometry.timeUntilCircleCollision((Circle) circleComponent, cartesianCircle, cartesianVelocity)<=soonestCircleTime){
                soonestCircleTime = Geometry.timeUntilCircleCollision((Circle) circleComponent, cartesianCircle, cartesianVelocity);
                closestCircle = (Circle) circleComponent;
            }
        }
        if (soonestWallTime <= soonestCircleTime && soonestWallTime < time + 10){
            collisionBallWall(ball, closestWall);
        }
        else if (soonestCircleTime <= soonestWallTime && soonestCircleTime < time + 10){
            collisionBallCircle(ball, closestCircle);
        }else {
        throw new RuntimeException("Nothing to collide?");
        }
    }

    /**
     * Mutates the ball's velocity to what it is after it collides with the wall
     * IMPORTANT: assumes that the ball is at the edge of the wall, at point of impact 
     * @param ball of the gadget that is getting hit
     * @param lineComponent
     */
    private void collisionBallWall(Ball ball, Object lineComponent) {
        Circle cartesianCircle = new Circle(ball.getPosition().x(),-1*ball.getPosition().y(),ball.getRadius());
        Vect cartesianVelocity = new Vect(ball.getVelocity().x(),-1*ball.getVelocity().y());
        Vect newVelocity = Geometry.reflectWall((LineSegment)lineComponent,cartesianVelocity, this.getReflectionCoeff());  
        ball.setVelocity(new Vect(newVelocity.x(), -1*newVelocity.y()));
    }

    /**
     * Mutates the ball's velocity to what it is after it collides with the circle
     * IMPORTANT: assumes that the ball is at the edge of the circle, at point of impact 
     * @param ball that's going to collide with the gadget
     * @param circleComponent of the gadget that is getting hit
     */
    private void collisionBallCircle(Ball ball, Object circleComponent) {
        Circle cartesianCircle = new Circle(ball.getPosition().x(),-1*ball.getPosition().y(),ball.getRadius());
        Vect cartesianVelocity = new Vect(ball.getVelocity().x(),-1*ball.getVelocity().y());
        Vect newVelocity = Geometry.reflectCircle(((Circle) circleComponent).getCenter(), cartesianCircle.getCenter(), cartesianVelocity, this.getReflectionCoeff());
        ball.setVelocity(new Vect(newVelocity.x(), -1*newVelocity.y()));
    }
    
    @Override
    public boolean equals(Object other){
        if (other instanceof SquareBumper){
            SquareBumper otherBumper = (SquareBumper) other;
            if (this.getX() == otherBumper.getX()
                    && this.getY() == otherBumper.getY()
                    && this.getWidth() == otherBumper.getWidth()
                    && this.getHeight() == otherBumper.getHeight()
                    && this.getNamesOfGadgetsToTrigger().equals(otherBumper.getNamesOfGadgetsToTrigger())
                    && Arrays.equals(this.lineComponents,otherBumper.lineComponents)
                    && Arrays.equals(this.circleComponents,otherBumper.circleComponents)
                    && (this.NAME == otherBumper.NAME || (this.NAME != null && otherBumper.NAME != null && this.NAME.equals(otherBumper.NAME)))){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "SquareBumper with name " + NAME;
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
        double x = X_COORD * scale;
        double y = Y_COORD * scale;
        double width = scale;
        double height = scale;
        return new Rectangle2D.Double(x,y,width,height);
    }
    
    @Override
    public Color getDrawColor(){
        return new Color(0,128,128);
    }
}
