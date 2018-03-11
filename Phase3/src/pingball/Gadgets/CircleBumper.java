package pingball.Gadgets;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
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
 * A mutable circle shaped bumper in the pingBall game.
 * 
 * Abstraction Function: 
 *      A circle bumper in pingball.
 * Rep Invariant: 
 *      position x and y >=0 and <= board dimension. 
 *      Must have radius=0.5L (Diameter=1L), refCoeff=1.0
 */

public class CircleBumper implements Gadget {

    private String NAME;
    private final double X_COORD;
    private final double Y_COORD;
    private List<Gadget> gadgetsToTrigger;
    private Object[] circleComponents;

    /**
     * Circle bumper makes a bumper having a circular shape with diameter 1L
     * @param x x-coordinate of the position
     * @param y y-coordinate of the position
     */
    public CircleBumper(double x, double y) {
        this.X_COORD = x;
        this.Y_COORD = y;
        gadgetsToTrigger = new ArrayList<Gadget>();

        final double RADIUS = .5;
        double xCoord = this.getX();
        double yCoord = -1*this.getY();
        circleComponents = new Object[1]; 
        circleComponents[0] = new Circle(xCoord+RADIUS, yCoord-RADIUS, RADIUS);
        checkRep();
    }
    
    /**
     * Circle bumper makes a bumper having a circular shape with diameter 1L
     * @param name the name of the gadget
     * @param x x-coordinate of the position
     * @param y y-coordinate of the position
     */
    public CircleBumper(String name, double x, double y) {
        this(x, y);
        this.NAME = name;
        checkRep();
    }
    
    /**
     * rep invariant: the bumper must be within the bounds of the board
     */

    protected void checkRep(){
    	try {
			assertTrue(this.getX() < 20);
			assertTrue(this.getY() < 20);
			assertTrue(this.getX() >= 0);
			assertTrue(this.getY() >= 0);
    	} catch (IndexOutOfBoundsException e) {
    	    System.err.println("IndexOutOfBoundsException: " + e.getMessage());
		}
    	
    	try {
    		assertTrue(((Circle) (circleComponents[0])).getRadius() == 0.5);
    	} catch (IndexOutOfBoundsException e) {
    	    System.err.println("IndexOutOfBoundsException: " + e.getMessage());
		}
    }
    
    /**
     * Gets the name of this CircleBumper
     * @return the name of this bumper
     */
    public String getName() {
        return this.NAME;
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
        double X = this.getX();
        double Y= this.getY();
        boardDisplayArray[(int)Y][(int)X] = "O";
        board.setDisplayArray(boardDisplayArray);
    }

    @Override
    public int getOrientation() {
        return 1;
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
		//No action		
	}

    @Override
    public double ballHitsGadgetThisTimestep(Ball ball, long time) {
        double doubleTime = time;
        Geometry.setForesight(doubleTime);

        for (Object circleComponent : circleComponents){
            Circle cartesianCircle = new Circle(ball.getPosition().x(),-1*ball.getPosition().y(),ball.getRadius());
            Vect cartesianVelocity = new Vect(ball.getVelocity().x(),-1*ball.getVelocity().y());
            if (Geometry.timeUntilCircleCollision((Circle) circleComponent, cartesianCircle, cartesianVelocity)<=time/1000.0){
                return Geometry.timeUntilCircleCollision((Circle) circleComponent, cartesianCircle, cartesianVelocity);
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public void collisionBallGadget(Ball ball, long time) {
        final double RADIUS = .5;
        Circle cartesianCircle = new Circle(ball.getPosition().x(),-1*ball.getPosition().y(),ball.getRadius());
        Vect cartesianVelocity = new Vect(ball.getVelocity().x(),-1*ball.getVelocity().y());
        Vect gadgetCircle = new Vect(this.getX()*1.0+RADIUS,-1.0*this.getY()-RADIUS);
        Vect newCartesianVelocity = Geometry.reflectCircle(gadgetCircle, cartesianCircle.getCenter(), cartesianVelocity, this.getReflectionCoeff());
        Vect newDisplayVelocity = new Vect(newCartesianVelocity.x(),-1*newCartesianVelocity.y());
        ball.setVelocity(newDisplayVelocity);
        
    }
    
    /**
     * Gets the list of gadgets that this bumper is triggering
     * @return the list of gadgets to trigger
     */
    public List<Gadget> getGadgetsToTrigger() {
        return gadgetsToTrigger;
    }
    
    @Override
    public Shape getShape(int scale){
        double x = X_COORD * scale;
        double y = Y_COORD * scale;
        double width = scale;
        double height = scale;
        return new Ellipse2D.Double(x, y, width, height);
    }
    
    @Override
    public Color getDrawColor(){
        return new Color(64,0,128);
    }
    
    @Override
    public boolean equals(Object other){
        if (other instanceof CircleBumper){
            CircleBumper otherBumper = (CircleBumper) other;
            if (this.getX() == otherBumper.getX()
                    && this.getY() == otherBumper.getY()
                    && this.getWidth() == otherBumper.getWidth()
                    && this.getHeight() == otherBumper.getHeight()
                    && this.getNamesOfGadgetsToTrigger().equals(otherBumper.getNamesOfGadgetsToTrigger())
                    // no line components
                    && Arrays.equals(this.circleComponents,otherBumper.circleComponents)
                    &&  (this.NAME == otherBumper.NAME || (this.NAME != null && otherBumper.NAME != null && this.NAME.equals(otherBumper.NAME)))){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "CircleBumper with name " + NAME; 
    }

    @Override
    public boolean isPortal() {
        return false;
    }
}
