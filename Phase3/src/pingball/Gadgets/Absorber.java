package pingball.Gadgets;
import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
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

public class Absorber implements Gadget {
    
    /**
     * Absorber gadget 
     * 
     * A mutable ball-return mechanism in a pingball game 
     * 
     * Size and shape: A rectangle kL × mL where k and m are positive integers <= 20
     * Orientation: not applicable (only one orientation is allowed)
     * Coefficient of reflection: not applicable; the ball is captured
     * Trigger: generated whenever the ball hits it
     * Action: shoots out a stored ball
     * 
     * Abstraction Function: represents an absorber gadget at the specified position on a Pingball board
     * Rep Invariant: gadget height <=20 && length <= 20
     * 
     */
    
    private String name;
    private final double X_COORD;
    private final double Y_COORD;
    private final int LENGTH;
    private final int HEIGHT;
    private List<Gadget> gadgetsToTrigger;
    
    private Object[] lineComponents;
    private Object[] circleComponents;
    //private List<Ball> ballsContained;
    private ArrayList<Ball> ballsContained;
    private long lastRelease;
    private Ball lastBallReleased;
    
    /*
     * Constructs an absorber, which simulates the ball-return mechanism in a pinball game. 
     * 
     * When a ball hits an absorber, the absorber stops the ball and holds it (unmoving) 
     * in the bottom right-hand corner of the absorber. The ball’s center is .25L from 
     * the bottom of the absorber and .25L from the right side of the absorber.
     * 
	 * If the absorber is holding a ball, then the action of an absorber, when it is 
	 * triggered, is to shoot the ball straight upwards in the direction of the top of 
	 * the playing area. By default, the initial velocity of the ball should be 50L/sec. 
	 * 
	 * With the default gravity and the default values for friction, the value of 50L/sec 
	 * gives the ball enough energy to lightly collide with the top wall, if the bottom 
	 * of the absorber is at y=20L. If the absorber is not holding the ball, or if the 
	 * previously ejected ball has not yet left the absorber, then the absorber takes 
	 * no action when it receives a trigger signal.
	 * 
	 * An absorber can be made self-triggering by connecting its trigger to its own action. 
	 * When the ball hits a self-triggering absorber, it should be moved to the bottom 
	 * right-hand corner as described above, and then be shot upward as described above. 
	 * 
	 * There is no short delay between these events, at our discretion.
	 * 
	 * @param x: the x coordinate of the absorber's upper left hand corner
	 * @param y: the y coordinate of the absorber's upper left hand corner
	 * @param k: the width of the absorber
	 * @param m: the height of the absorber
     */
    public Absorber(double x, double y, int k, int m) {
        this.X_COORD = x;
        this.Y_COORD = y;
        this.LENGTH = k;
        this.HEIGHT = m;
        this.ballsContained = new ArrayList<Ball>();
        gadgetsToTrigger = new ArrayList<Gadget>();
        
        
        double xCoord = this.getX();
        double yCoord = -1*this.getY();
        lineComponents = new LineSegment[4];
        lineComponents[0] = new LineSegment(xCoord,yCoord,xCoord+k,yCoord);
        lineComponents[1] = new LineSegment(xCoord+k,yCoord,xCoord+k,yCoord-m);
        lineComponents[2] = new LineSegment(xCoord+k,yCoord-m,xCoord,yCoord-m);
        lineComponents[3] = new LineSegment(xCoord,yCoord-m,xCoord,yCoord);
        circleComponents = new Circle[4];
        circleComponents[0] = new Circle(xCoord,yCoord,0);
        circleComponents[1] = new Circle(xCoord+k,yCoord,0);
        circleComponents[2] = new Circle(xCoord+k,yCoord-m,0);
        circleComponents[3] = new Circle(xCoord,yCoord-m,0);
        
        checkRep();
    }
    
    public Absorber(String name, double x, double y, int k, int m) {
        this(x, y, k, m);
        this.name = name;
        checkRep();
    }
    
    /**
     * 
     * Check to make sure rep invariant is preserved: Absorber must lie within the bounds of the 
     * board, both at its origin and including its width and length.
     * It must also contain 4 circleComponents and 4 lineComponents
     * 
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
    	
    	try{
    		assertTrue(this.getHeight() < (21 - this.getY()));
    		assertTrue(this.getWidth() < (21 - this.getX()));
    	} catch(RuntimeException e) {
    		System.err.println("Height and Length must not go off the board: " + e.getMessage());
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
    			assertTrue((((LineSegment) (this.lineComponents[i])).length() == this.getHeight())
    					|| (((LineSegment) (this.lineComponents[i])).length() == this.getWidth()));
    		}
    	} catch (IndexOutOfBoundsException e) {
    	    System.err.println("Broken Circle Components: " + e.getMessage());
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
		return this.LENGTH;
	}

	@Override
	public int getHeight() {
		return this.HEIGHT;
	}
    
    @Override
    public void drawGadgetOnBoard(Board board) {
        String[][] boardDisplayArray = board.getDisplayArray();
        double X = this.getX();
        double Y = this.getY();
        int length = this.getWidth();
        double height = this.getHeight();
        for (int ycoord = (int) Y; ycoord < Y+height; ycoord++) {
            for (int xcoord = (int) X; xcoord< X+length; xcoord++) {
                boardDisplayArray[(int) (ycoord)][(int)xcoord] = "=";
            }    
        }
    }

    @Override
    public int getOrientation() {
        return 0;
    }

    @Override
    public double getReflectionCoeff() {
        return 0;
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
	    final int LAUNCH_VEL = -50;
	    final double BALL_RAD = .25;
	    if (ballsContained.size()>0){
	        Ball ballToEject = ballsContained.get(0);
	        double homeX = this.getX()+this.getWidth()-BALL_RAD;
	        double homeY = this.getY();
	        Vect homePosition = new Vect(homeX,homeY);
	        Vect launchUpwards = new Vect(0,LAUNCH_VEL);

	        ballToEject.setPosition(homePosition);
	        ballToEject.setVelocity(launchUpwards);
	        ballsContained.remove(ballToEject);
	        lastRelease = System.currentTimeMillis();
	        lastBallReleased = ballToEject; 
	    }

	} 

    @Override
    public double ballHitsGadgetThisTimestep(Ball ball, long time) {
        Geometry.setForesight(time);
        Circle cartesianCircle = new Circle(ball.getPosition().x(),-1*ball.getPosition().y(),ball.getRadius());
        Vect cartesianVelocity = new Vect(ball.getVelocity().x(),-1*ball.getVelocity().y());
        for (Object lineComponent : lineComponents){
            if (Geometry.timeUntilWallCollision((LineSegment) lineComponent,cartesianCircle,cartesianVelocity)<=time/1000.){
                return Geometry.timeUntilWallCollision((LineSegment) lineComponent,cartesianCircle,cartesianVelocity);
            }
        }
        for (Object circleComponent : circleComponents){
            if (Geometry.timeUntilCircleCollision((Circle) circleComponent, cartesianCircle,cartesianVelocity)<=time/1000.){
                return Geometry.timeUntilCircleCollision((Circle) circleComponent, cartesianCircle,cartesianVelocity);
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public void collisionBallGadget(Ball ball, long time) {
        double ballRadius = 0.25;
        double homeX = this.getX() + this.getWidth() - ballRadius;
        double homeY = this.getY() + this.getHeight() - ballRadius-.1; // .1 allows the ball to not fall through bottom of absorber
        ball.setVelocity(new Vect(0, 0)); 
        ball.setPosition(new Vect(homeX, homeY));
        if (!this.ballsContained.contains(ball)){
            this.ballsContained.add(ball);       
        }
    }
    
    @Override
    public boolean isAbsorber(){
        return true;
    }
    
    @Override
    public boolean equals(Object other){
        if (other instanceof Absorber){
            Absorber otherAbsorber = (Absorber) other;
            if (this.getX() == otherAbsorber.getX()
                    && this.getY() == otherAbsorber.getY()
                    && this.getWidth() == otherAbsorber.getWidth()
                    && this.getHeight() == otherAbsorber.getHeight()
                    && this.getNamesOfGadgetsToTrigger().equals(otherAbsorber.getNamesOfGadgetsToTrigger())
                    && Arrays.equals(this.lineComponents,otherAbsorber.lineComponents)
                    && Arrays.equals(this.circleComponents,otherAbsorber.circleComponents)
                    &&  (this.name == otherAbsorber.name || (this.name != null && otherAbsorber.name != null && this.name.equals(otherAbsorber.name)))){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Absorber with name " + name;
    }

    @Override
    public boolean isPortal() {
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Gadget> getGadgetsToTrigger() {
        return gadgetsToTrigger;
    }
    
    @Override
    public Shape getShape(int scale){
        double x = X_COORD * scale;
        double y = Y_COORD * scale;
        double width = LENGTH * scale;
        double height = HEIGHT * scale;
        return new Rectangle2D.Double(x,y,width,height);
    }
    
    @Override
    public Color getDrawColor(){
        return Color.LIGHT_GRAY;
    }
}
