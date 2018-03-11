package pingball;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import physics.Circle;
import physics.LineSegment;
import physics.Vect;

/**
 * 
 * An abstract gadget.
 * 
 * Represents all possible objects that can be placed in and interact on the
 * pinball board.
 * 
 * 
 * 
 * Gadgets can move around the board (e.g. balls) or be fixed (e.g. flippers).
 * 
 * 
 * 
 * @author chsamlee
 * 
 *
 */

abstract public class Gadget {

	private final Vect position;

	private final List<Gadget> gadgetsToTrigger;

	public Vect getPosition() {
		return position;
	};

	
	/**
	 * 
	 * @return the horizontal length of gadget
	 */

	abstract public int getWidth();

	/**
	 * 
	 * @return the vertical length of gadget
	 */

	abstract public int getHeight();

	/**
	 * 
	 * Create a new gadget that does not trigger other gadgets.
	 * 
	 * @param position
	 *            The origin (top-left corner) of gadget
	 */

	public Gadget(Vect position) {

		this(position, new ArrayList<Gadget>());

	}

	public Gadget() {

		this(new Vect(0.0, 0.0), new ArrayList<Gadget>());

	}

	/**
	 * 
	 * Create a new gadget.
	 * 
	 * @param position
	 *            The origin (top-left corner) of gadget
	 * 
	 * @param gadgetsToTrigger
	 *            List of gadgets that will be triggered when this gadget is
	 *            triggered
	 */

	public Gadget(Vect position, List<Gadget> gadgetsToTrigger) {

		this.position = position;

		this.gadgetsToTrigger = Collections.unmodifiableList(gadgetsToTrigger);

	}

	/**
	 * 
	 * Performs an action when the gadget is triggered.
	 */

	public void trigger() {

		for (Gadget gadget : getGadgetsToTrigger()) {

			gadget.trigger();

		}

	}
	
	/**
	 * Observer method. 
	 * @return false if this gadget is not a portal 
	 */
	public boolean isPortal(){
	    return false;
	}

	/**
	 * 
	 * Update the state of the gadget. If the gadget is static, this function
	 * does nothing.
	 */

	abstract public void updateState();

	/**
	 * 
	 * Calculates time until a collision occurs, assuming that the ball's
	 * velocity remains constant.
	 * 
	 * @param ball
	 *            The ball to check collision
	 * 
	 * @return time until collision in seconds, Double.POSITIVE_INFINITY if no
	 *         collision
	 */

	abstract public double timeUntilCollision(Ball ball);

	/**
	 * 
	 * Perform a collision. This function changes the ball's velocity.
	 * 
	 * @param ball
	 *            a ball that is colliding with gadget
	 */

	abstract public void performCollision(Ball ball);

	/**
	 * 
	 * Draws the text representation of the gadget at a specified position
	 * 
	 * @param position
	 *            Position to get the text representation. Must be inside the
	 *            gadget.
	 * 
	 * @return text representation of the gadget at position
	 */

	abstract public char drawGadget(Vect position);
	
	abstract public boolean transport(Ball ball);


	/**
	 * Getter function for this gadget's list of gadgets to trigger
	 * @return gadgetsToTrigger
	 *                the list of gadgets this gadget will trigger
	 */
    public List<Gadget> getGadgetsToTrigger() {
        return gadgetsToTrigger;
    }


}
