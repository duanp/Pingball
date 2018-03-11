package pingball;



import java.util.ArrayList;

import physics.Circle;
import physics.Geometry;
import physics.LineSegment;
import physics.Vect;



/**
 * A 1x1 wall block.
 *
 */

public class Wall extends Gadget {
	
	public enum WallType { TOP, RIGHT, LEFT, BOTTOM};
    final Vect[] vertices;
    final LineSegment[] edges;
    final Circle[] corners;
    char charDraw;

    public int getWidth() {return 1;}
    public int getHeight() {return 1;}

    private boolean invisible;

   
    /**
     * Create a 1x1 wall block at specified location.
     * @param position top-left corner of the wall block
     */

    public Wall(Vect position) {
        super(position, new ArrayList<Gadget>());
        charDraw = '.'; //default
        vertices = new Vect[] {
                position,
                position.plus(Vect.X_HAT),
                position.plus(Vect.X_HAT).plus(Vect.Y_HAT),
                position.plus(Vect.Y_HAT)
        };

        edges = new LineSegment[4];
        corners = new Circle[4];
        for(int i=0; i<4; i++) {
            edges[i] = new LineSegment(vertices[i], vertices[(i+1)%4]);
            corners[i] = new Circle(vertices[i], 0);
        }
    }


    //observers
    /**
     * Tells whether the wall is invisible, 
     * in which case the ball passed through the wall to another board.
     * 
     * @return returns the invisibility of the wall (boolean)
     */
    public boolean isInvisible() {
    	return invisible;
    } 
    
    /**
     * @return the type of this wall
     */
    public WallType type(){
    	double xWall = vertices[0].x();
    	double yWall = vertices[0].y();
    	
    	final int BOARD_WIDTH = 22;
    	final int BOARD_LENGTH = 22;
    	
    	final double xLeftWall = -1;
    	final double xRightWall = BOARD_WIDTH -2;
    	final double yTopWall = -1;
    	final double yBottomWall = BOARD_LENGTH - 2;
    	
    	if (xWall == xLeftWall)
    		return WallType.LEFT;
    	else if (xWall == xRightWall) 
    		return WallType.RIGHT;
    	else if (yWall == yTopWall) 
    		return WallType.TOP;
    	else if (yWall == yBottomWall)
    		return WallType.BOTTOM;
    	return WallType.BOTTOM; //TODO: fix after testing
    }

    /**
     * The board sets the invisibility of the wall
     * @param invisible true if invisible
     */
    public void setInvisible(boolean invisible) {
    	this.invisible = invisible;
    }
 

	/**
	 * 
	 * check if a ball should be transported through this wall - the ball should
	 * be transported if the wall is invisible and the ball is colliding with
	 * the wall's long edge
	 * 
	 * @param ball
	 *            the ball to be assessed for transportation
	 * @return true iff the ball in question should be transported through this wall
	 */

	public boolean transport(Ball ball) {
		
		if (!invisible) {
			return false;
		}
		for (LineSegment edge : edges) {
			if (Geometry.timeUntilWallCollision(edge, ball.getCircle(),
			ball.getVelocity()) <= 0.05)
				return true;
		}
		return false;
	}



	/**
	 * The wall does not have a trigger
	 */
    @Override
    public void trigger() {}

    /**
     * the wall does not update state
     */
    @Override
    public void updateState() {}

    /**
     * Calculated the time it takes until a specified ball collides with this wall
     * 
     * @ball the specified ball that will collide with this wall
     * @return time(in seconds) until the specified ball hits this wall
     */
    @Override
    public double timeUntilCollision(Ball ball) {
        double minTime = Double.POSITIVE_INFINITY;
        for(LineSegment edge: edges) {
            double time = Geometry.timeUntilWallCollision(edge, ball.getCircle(), ball.getVelocity());
            if(time <= minTime) minTime = time;
        }
        for(Circle corner: corners) {
            double time = Geometry.timeUntilCircleCollision(corner, ball.getCircle(), ball.getVelocity());
            if(time <= minTime) minTime = time;
        }
        return minTime;

    }


    /**
     * performs the collision of this wall with a specified ball;
     * sets the velocity of the ball after collision
     * 
     * @ball the ball to collide with this wall
     * @return none
     */
    @Override
    public void performCollision(Ball ball) {
        assert(this.timeUntilCollision(ball) != Double.POSITIVE_INFINITY);
        // get the edge/corner the ball is colliding with, which should have minimum time until collision
        double minTimeForEdges = Double.POSITIVE_INFINITY;
        LineSegment minTimeEdge = null;
        for(LineSegment edge: edges) {
            double time = Geometry.timeUntilWallCollision(edge, ball.getCircle(), ball.getVelocity());
            if(time <= minTimeForEdges) {
                minTimeForEdges = time;
                minTimeEdge = edge;
            }
        }
        double minTimeForCorners = Double.POSITIVE_INFINITY;
        Circle minTimeCorner = null;
        for(Circle corner: corners) {
            double time = Geometry.timeUntilCircleCollision(corner, ball.getCircle(), ball.getVelocity());
            if(time <= minTimeForCorners) {
                minTimeForCorners = time;
                minTimeCorner = corner;
            }
        }

        Vect newVelocity;
        // note: since there is a collision, there must be a component of wall with timeUntilCollision < infinity
        if(minTimeForEdges < minTimeForCorners) {
            newVelocity = Geometry.reflectWall(minTimeEdge, ball.getVelocity());
        }

        else {
            newVelocity = Geometry.reflectCircle(minTimeCorner.getCenter(), ball.getPosition(), ball.getVelocity());
        }
        ball.setVelocity(newVelocity);
    }

    /**
     * Set the character of the wall;
     * if the wall is not joined, then returns "."
     * if the wall is joined to a different board, set to a character from the other board in Board class
     * 
     * @param c a character from the other board name
     */
    public void setDraw(char c) {
    	charDraw = c;
    }

    /**
     * Draws the gadget in board (for testing purposes) if the wall is not invisible
     * @position the position of the wall block to be drawn
     * @return the character at the specified position. 
     * 		   - if invisible, white space. 
     *         - else, a '.' or a character from the other boardName.
     */
    public char drawGadget(Vect position) {
            return charDraw;             

    

    }

}
