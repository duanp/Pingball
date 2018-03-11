package pingball;

import java.util.List;

import physics.Circle;
import physics.LineSegment;
import physics.Vect;


/**
 * Circle Bumper Class
 * 
 * Represents a circular bumper.
 *  Is characterized by the origin of its 'box space' in the play area.
*   Has default diameter 1L with coefficient of reflection 1.0
* 
*  Has a ball bounce off of itself when a ball hits it. 
 * Represented on board as the stationary circular shapes on the board space. They reflects the colliding balls.
 * 
 * methods
 * - adjusts the velocity of ball after collision
 * - calculated time until collision
 * - draws the shape # on board
 */

public class CircleBumper extends Bumper {
    
    /**  The Circle Bumpers's name */ 
    private final String name;
    
	//Size and shape: a circular shape with diameter 1L
	private static final double BUMPER_RADIUS = 0.5;
    	
	//Orientation: not applicable (symmetric to 90 degree rotations)

	/**
	 * create a circle bumper at position x,y with radius 0.5
	 * @param position the origin of gadget. Note that this is NOT the center of circle.
	 * @param gadgetsToTrigger the list of gadgets that are triggered by the circle bumper
	 */
    public CircleBumper(String name, Vect position, List<Gadget> gadgetsToTrigger){      
    	super(name, position, new LineSegment[0], new Circle[]{createCircle(position)}, gadgetsToTrigger);
    	this.name=name;
    	checkRep(); 
	}
    
    /**
     * Create a circle. (Helper function)
     * @param position the origin of gadget. Note that this is NOT the center of circle.
     * @return a circle with position as top-left corner
     */
    private static Circle createCircle(Vect position) {
        return new Circle(position.plus(new Vect(BUMPER_RADIUS, BUMPER_RADIUS)), BUMPER_RADIUS);
    }
    
    /**
     * @return a string containing the position of this bumper
     */
    @Override 
    public String toString(){
        return "circle bumper(" + getPosition().x() +"," + getPosition().y() + ")";
    }

   
    /**
     * place a '0' character into the passed position to mark the circle bumper's 
     * position on the board 
     * 
     * @param board the board in which this bumper show
     */
	@Override
	public char drawGadget(Vect position) {
		return 'O';
	}
	
	/** Getter method to return the name of the circleBumper
	 * @return the name of the circleBumper.*/
	public String getName(){
        return name;
    }

	/** Returns True if Rep invariant is maintained. /**
	 *  CircleBumper must have a non-null position, and it's radius must be 0.5L. 
	 */
	 private void checkRep(){
	     assert this.getHeight()>0; 
	     assert this.getWidth()>0;
	     assert this.getPosition() != null; 
	 }

	    /**
	     * The Bumper does not transport balls
	     */
		@Override
		public boolean transport(Ball ball) {
			return false;
		}
    
	
}
