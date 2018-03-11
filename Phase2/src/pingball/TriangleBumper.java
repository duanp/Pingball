package pingball;

import java.util.List;

import physics.Circle;
import physics.LineSegment;
import physics.Vect;


/**
 * Triangle Bumper Class
 * 
 * these are the triangle / shapes stay on the board space without moving and reflects the colliding balls.
 * 
 * methods
 * - adjusts the velocity of ball after collision
 * - calculated time until collision
 * - draws the shape # on board
 */
public class TriangleBumper extends Bumper {
    
    /** The triangleBumper's name */ 
    private final String name;

    //Orientation: the default orientation (0 degrees) places one corner in the northeast, one corner in the northwest, and the last corner in the southwest. The diagonal goes from the southwest corner to the northeast corner.
    private final int orientation; //rep invariant: must be multiple of 90

    //Coefficient of reflection: 1.0

    /**
     * Create a triangle bumper at position with default orientation.
     * Meets rep invariant-- if not default triangle bumper created.
     * @param position the origin of gadget
     * @param gadgetsToTrigger list of gadgets that will be triggered when this gadget is triggered
     * 
     */
    public TriangleBumper(String name, Vect position, List<Gadget> gadgetsToTrigger){
        this(name, position, 0, gadgetsToTrigger);
        checkRep();
    }

    /**
     * Create a triangle bumper at position with specified orientation.
     * Meets rep invariant-- if not default triangle bumper created.
     * @param position the position vector
     * @param orientation facing the facing angle of the bumper
     *          0|90|180|270
     * @param gadgetsToTrigger list of gadgets that will be triggered when this gadget is triggered
     * 
     */
    public TriangleBumper(String name, Vect position, int orientation, List<Gadget> gadgetsToTrigger){
        super(name, position, getEdges(position, orientation), getCorners(position, orientation), gadgetsToTrigger);
        this.name=name; 
        this.orientation = orientation;
        checkRep();
    }
    
    
    public int getOrientation() {
        int newOrientation = new Integer(orientation);
        return newOrientation;
    };
    /**
     * Get the vertices of the triangle. (Helper function)
     * @param position the position vector of origin
     * @param orientation the facing angle of bumper. Must be 0, 90, 180, or 270.
     * @return list of vertices as vectors
     */
    private static Vect[] getVertices(Vect position, int orientation) {
        assert(orientation >= 0 && orientation < 360 && orientation%90 == 0);
        double x = position.x();
        double y = position.y();
        switch(orientation){
        case 0:
            return new Vect[] {new Vect(x,y), new Vect(x,y+1), new Vect(x+1,y)};
        case 90:
            return new Vect[] {new Vect(x,y), new Vect(x+1,y), new Vect(x+1,y+1)};
        case 180:
            return new Vect[] {new Vect(x,y+1), new Vect(x+1,y+1), new Vect(x+1,y)};
        case 270:
            return new Vect[] {new Vect(x,y), new Vect(x+1,y+1), new Vect(x,y+1)};
        default:
            return new Vect[] {new Vect(x,y), new Vect(x,y+1), new Vect(x+1,y)};
        }

	}
    
    /**
     * Get the edges of the triangle. (Helper function)
     * @param position the position vector of origin
     * @param orientation the facing angle of bumper. Must be 0, 90, 180, or 270.
     * @return list of edges as line segments
     */
    private static LineSegment[] getEdges(Vect position, int orientation) {
        Vect[] vertices = getVertices(position, orientation);
        return new LineSegment[] {
                new LineSegment(vertices[0], vertices[1]),
                new LineSegment(vertices[1], vertices[2]),
                new LineSegment(vertices[2], vertices[0])
        };
    }
    
    /**
     * Get the corners of the triangle. (Helper function)
     * @param position the position vector of origin
     * @param orientation the facing angle of bumper. Must be 0, 90, 180, or 270.
     * @return list of corners as circles
     */
    private static Circle[] getCorners(Vect position, int orientation) {
        Vect[] vertices = getVertices(position, orientation);
        return new Circle[] {
                new Circle(vertices[0], 0),
                new Circle(vertices[1], 0),
                new Circle(vertices[2], 0)
        };
    }
    
    /**
     * @return a string containing the position of this bumper
     */
    @Override 
    public String toString(){
        return "Triangle bumper(" + getPosition().x() +"," + getPosition().y() + ")";
    }

    /**
     * place '\' or '/' character, as appropriate, into the passed position.
     * / for orientation 0 or 180, \ for orientation 90 or 270
     * @param position the position at which this bumper should draw itself
     */
    @Override
    public char drawGadget(Vect position) {
        if (this.orientation == 0 || this.orientation == 180) {
            return '/';
        }
        return '\\';
    }
    
    /** Getter method to return the name of the triangleBumper
     * @return the name of the triangleBumper*/
    public String getName(){
        return name;
    }
    
    /** Returns true if Rep Invariant for Triangle Bumper is maintained. */
    private void checkRep(){
        assert getEdges(this.getPosition(), this.orientation).length==3;
        assert getCorners(this.getPosition(), this.orientation).length==3;
        assert this.getPosition() !=null; 
        assert(orientation >= 0 && orientation < 360 && orientation%90 == 0);
        assert getVertices(this.getPosition(), this.orientation).length==3;
    }
    
    /**
     * Bumpers do not does not transport balls
     * 
     */
	@Override
	public boolean transport(Ball ball) {
		return false;
	}
}
