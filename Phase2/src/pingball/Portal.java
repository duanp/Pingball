package pingball;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import physics.Circle;
import physics.Geometry;
import physics.LineSegment;
import physics.Vect;

/**
 * A Portal is a circular hole with diameter 1L which teleports a ball to another portal gadget, possibly on a different board.
 * It has no orientation or coefficient of reflection. Portals may be connected with other portals, but may not be symmetrically connected. 
 * @author kellypet
 * 
 * Rep Invariant: 
 * has a diameter of 1L;
 * triggered when a ball hits it. 
 * static (i.e. portal's position on board cannot change once board has been instantiated). 
 *
 */
public class Portal extends Gadget{
    

    /** The name of the source portal */
    private String connectedPortalName;
    
    /** The name of the board on which the source portal is located.*/
    private String connectedBoardName;


    /** The name of the portal. Cannot be changed once instantiated. */ 
	private final String name;
	private static final double PORTAL_RADIUS = 0.5;
	
	/** The circle constructing the edge of the portal. */ 
	private final Circle edge; 
	
	/** The position at which the portal is located. Given x and y coordinates to construct the portal,
	 * the portal's center will be at: (x+0.5, y+0.5).
	 * */
	private final Vect position; 
	
	/** optional argument in a board file denoting the name of the board on which the target portal is located. */
    private String otherBoard; 
    
    /** The name of the target Portal connected to the source portal */
    private final String otherPortal; 
    
    public boolean isOpen;

   
    
    /**
     * Creates a portal 
     * 
     * @param name the name of the portal
     * @param position the position (top left corner) of the portal; must be within the boundaries of the board
     * @param otherBoard the Board that is connected to this portal, possibly another board
     * @param otherPortal the other portal that is connected to this portal
     */
    public Portal(String name, Vect position, String otherBoard, String otherPortal) {
        super(position);
        this.name = name;
        this.position = position;
        this.otherBoard = otherBoard;
        this.otherPortal = otherPortal;
        this.edge = new Circle(position.x(), position.y(), PORTAL_RADIUS);
        this.isOpen = false;
        //this.edge = new Circle(position.x() + PORTAL_RADIUS , position.y() + PORTAL_RADIUS, PORTAL_RADIUS);

        
    }
    public Portal(String name, Vect position, String otherPortal) {
        super(position);
        this.name = name;
        this.position = position;
        this.otherBoard = "";
        this.otherPortal = otherPortal;
        this.edge = new Circle(position.x(), position.y(), PORTAL_RADIUS);
        this.isOpen = false;
    }
    
    /**
     * the get server message returns information about otherPortal and this Portal
     * @return concatentated String of the otherPortal and this Portal
     */
    public String getServerMessage(){
        return this.otherPortal +" " + this.otherBoard;

    }
    
    //observer methods
    /**
     * create a portal at position x,y with radius 0.5
     * @param position the origin of portal. 
     * @param portalsConnectedTo
     *              list of portals this portal is connected to. The i-th portal in this list corresponds to the 
     *              i-th board in the list boardsConnectedTo.       
     *  @param boardsConnectedTo
     *              list of boards this portal is connected to. The i-th board in this list corresponds to 
     *              the i-th portal in the list portalsConnectedTo. 
     */
 /*   public Portal(Vect position, LineSegment[] edges, Circle[] circularParts, List<String> portalsConnectedTo, List<String> boardsConnectedTo) {
        super(position);
        this.portalEdges = Arrays.copyOf(edges, edges.length);
        this.circularParts = Arrays.copyOf(circularParts, circularParts.length);
        checkRep(); 
    }*/

    /**
    * Getter method for the position of the portal.
    * @return portalPosition
    *                the vector position (top left) of the portal
    */
   public Vect getPosition() {
       Vect portalPosition = new Vect(this.position.x(), this.position.y());
       return portalPosition;
 }
   public void setOtherBoard(String boardName){
       this.otherBoard = boardName;
   }
    

   /**
    * Getter method to return a portal's assigned name as a String.
    * @return portalName
    *.       the name of the portal object
    */
   public String getName() {
       String portalName = this.name;
       return portalName;
   }
   
   public boolean isPortal(){
       return true;
   }
   
   /**
    * Transports the ball that hits to portal to the otherPortal
    * 
    * @param ball
    *         the ball that hits the source portal. The velocity of the ball is greater than 0.
    * @return return true if the ball will be transported to a new portal and pass through it.
    * 		  return false if target portal does not exist or the ball has not collided with portal.
    */
    public boolean transport(Ball ball) {  
        if (!isOpen){
            return false;
        }
    	return Geometry.timeUntilCircleCollision(edge, ball.getCircle(), ball.getVelocity()) <= 0.005;
    }	
 
    
    /**
     * returns 1 because a Portal has diameter of 1
     */
    @Override
    public int getWidth() {
        return 1;
    }

    /**
     * returns 1 because a Portal has diameter of 1.
     */
    @Override
    public int getHeight() { 
        return 1;
    }

    /**
     * Getter method to return the name of the board, denoted as "otherBoard" in a board file, where the target portal is found.
     * @return otherBoardName
     *                an optional parameter that names the board on which the target portal is located.
     */
    public String otherBoard() {
        String otherBoardName = new String(otherBoard); 
        return otherBoardName;
    }
    
    /**
     * Getter method to return the name of the target portal, denoted "otherPortal" in a board file.
     * @return otherPortal
     *               the name of the target portal. When a ball collides with the source portal, the ball is teleported to the
     * target portal, exiting the target portal with the same velocity vector with which it entered source portal. If target portal
     * does not exist, then the ball passes unaffected over the source portal, without teleporting or reflecting.
     */
    public String otherPortal() {
        String otherPortalName = new String(otherPortal); 
        return otherPortalName;
    }
    
    /**
     * Portal does not need to be updated
     */
    @Override
    public void updateState() { }

    /**
     * Find the time(seconds) until a collision occurs between a ball and this portal
     * *note: assumed constant velocity of the ball
     * 
     * @param ball the ball whose collision time is to be checked
     * @return the number of seconds before a collision will take place
     *          or positive infinity if no collision will take place
     */
    @Override
    public double timeUntilCollision(Ball ball) {
        return Geometry.timeUntilCircleCollision(edge, ball.getCircle(), ball.getVelocity());
    }

    /**
     * draws the portal on the board. A portal is represented as a lowercase letter ‘o’.
     * @position the position at which the portal will be drawn
     * @return char 'o'
     */
    @Override
    public char drawGadget(Vect position) {
        return 'o';
    }
    
    public void setIsOpen(boolean isOpen){
        this.isOpen = isOpen;
    }

    /**
     * portal does not need to perform collision; done in client
     */
	@Override
	public void performCollision(Ball ball) {	}

    


}
