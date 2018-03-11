package pingball;

import java.util.List;

import physics.Circle;
import physics.LineSegment;
import physics.Vect;


/**
 * Square Bumper Class
 * 
 * these are the square shapes stay on the board space without moving and reflects the colliding balls.
 * 
 * methods
 * - adjusts the velocity of ball after collision
 * - calculated time until collision
 * - draws the shape # on board
 */

public class SquareBumper extends Bumper {

    /** The squareBumper's name */ 
    private final String name;
    
    /**
     * create a 1x1 square bumper at the specified location
     * @param position the origin of gadget
     * @param gadgetsToTrigger list of gadgets that will be triggered when this gadget is triggered
     */
    public SquareBumper(String name, Vect position, List<Gadget> gadgetsToTrigger){
        super(name, position, getEdges(position), getCorners(position), gadgetsToTrigger);
        this.name=name; 
        checkRep();
    }
    
    /**
     * Get the edges of the square. (Helper function)
     * @param position the origin of gadget
     * @return An array of edges as line segments
     */
    private static LineSegment[] getEdges(Vect position) {
        double x = position.x();
        double y = position.y();
        return new LineSegment[] {
                new LineSegment(x, y, x+1, y),
                new LineSegment(x+1, y, x+1, y+1),
                new LineSegment(x+1, y+1, x, y+1),
                new LineSegment(x, y+1, x, y)
        };        
    }
    
    /**
     * Get the corners of the square. (Helper function)
     * @param position the origin of gadget
     * @return An array of vectors of the corners as circles
     */
    private static Circle[] getCorners(Vect position) {
        double x = position.x();
        double y = position.y();
        return new Circle[] {
                new Circle(x, y, 0),
                new Circle(x+1, y, 0),
                new Circle(x+1, y+1, 0),
                new Circle(x, y+1, 0)
        };
    }

    /**
     * @returns the square bumper position in string
     */
    @Override
    public String toString() {
        return "square bumper(" + getPosition().x() +"," + getPosition().y() + ")";
    }

    
    /** Getter method to return the name of the squareBumper
     * @return the name of the squareBumper*/
    public String getName(){
        return name;
    }
    
    
    /**
     * place a '#' character into the passed array to mark the square bumper's 
     * position on the board 
     * @param board the board in which this bumper show
     */
    @Override
    public char drawGadget(Vect position) {
        return '#';
    }
    
    /** Returns true if Rep Invariant for Square Bumper is maintained. */
    private void checkRep(){
        assert getEdges(this.getPosition()).length==4;
        assert getCorners(this.getPosition()).length==4;
        assert this.getPosition() !=null; 
    }
    
    /**
     * Bumpers do not transport balls
     */
	@Override
	public boolean transport(Ball ball) {
		return false;
	}
    

}
