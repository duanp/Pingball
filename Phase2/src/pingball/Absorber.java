package pingball;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import physics.Circle;
import physics.Geometry;
import physics.LineSegment;
import physics.Vect;

/**
* 
* Absorber Spec:
* 
*  Represents the ball-return mechanism of a pinball game. Represented by a rectangular area. 
* 
*  When a ball hits an absorber, the absorber stops the ball and holds it in the bottom right-hand corner.
*  
*  If the absorber is holding a ball, the action of the absorber is to shoot the ball straight upwards in
*      the direction of the top of the playing area.
*  
*  The default velocity of the ejected ball should be 50L/sec.
* 
*  The absorber will not fire another ball until the previous fired ball has completely left the absorber.
*
*/
public class Absorber extends Gadget {

   //the absorber's name
    private final String name;
    
	//four edges of the absorber
	private LineSegment[] edges; //rep invariant: pos int <=20
	
	//the time/delay between launches
	private double triggerDelay;
	
	//contains the balls which are currently held by the absorber
	private Queue<Ball> captured;
	private List<Ball> released;
	
	//where ball is stored in absorber before launch: 
	//.25L from the bottom of the absorber and .25L from the right side of the absorber.
	private final Vect ballSlot;
	
	//where the ball is released:
	private final Vect ballRelease;
	
	//top left corner of absorber
	private Vect absorberPosition;
	
	//
	private boolean triggersSelf;
	
	//By default, the initial velocity of the ball should be 50L/sec.
	private final Vect LAUNCH_VELOCITY = new Vect(0, -50);
	
	//orientation: not applicable (only one orientation allowed)
	//coefficient of reflection: not applicable; the ball is captured
	
	private List<Gadget> gadgetsToTrigger;
	
	private int width, height;

	/**
	 * An absorber simulates the ball-return mechanism in a pinball game. 
	 * construct a new absorber with top corner at specified position with specified width and height
	 *
	 * @param position the x,y position of the top right corner of absorber as a Vector
	 * @width the width of the absorber 
	 * 		  width w is positive integer  
	 * 		  x+w <=20
	 * @height the height of the absorber 
	 * 		  height h is positive integer  
	 * 		  y+h <=20
	 */
	//private constructor using Vector
	public Absorber(String name, Vect position, int width, int height, List<Gadget> gadgetsToTrigger) {
		super(position, gadgetsToTrigger);
		this.name=name; 
		this.width = width;
		this.height = height;
		this.gadgetsToTrigger = gadgetsToTrigger;
		triggerDelay = 50; //in milliseconds
		absorberPosition = position;
		ballSlot = absorberPosition.plus(new Vect(this.width-0.25, this.height - 0.25));
		ballRelease = absorberPosition.plus(new Vect(this.width-0.25, -0.25));
		captured = new LinkedList<Ball>();
		//triggers linked? as a HashSet?
		
		//if absorber trigger itself, it must eject the ball immediately
		for(Gadget gadget : gadgetsToTrigger){
		    if(gadget instanceof Absorber){
		        triggersSelf = true;
		        break;
		    }
		}
		
		//System.out.println(triggersSelf);
		
		Vect[] corners = {
				absorberPosition,
				absorberPosition.plus(new Vect(this.width,0)),
				absorberPosition.plus(new Vect(0,this.height)),
				absorberPosition.plus(new Vect(this.width,this.height)),
		};
		LineSegment[] edgesPrivate = {
				new LineSegment(corners[0], corners[1]),
				new LineSegment(corners[1], corners[2]),
				new LineSegment(corners[2], corners[3]),
				new LineSegment(corners[3], corners[0])
		};
		edges = edgesPrivate;	
		checkRep(); 
	}
	
	
	/**
	 * @return the number of balls currently held by the absorber
	 */
	public int numCapturedBalls() {
		return captured.size();
	}
	
	
	
	//Trigger: generated whenever the ball hits it
	public void trigger(){
		System.out.println(captured);
    	if (captured.size() > 0) {
    		Ball releasedBall = captured.remove();
			releasedBall.setPosition(ballRelease);
			releasedBall.becomeReleased();
			releasedBall.setVelocity(LAUNCH_VELOCITY);
			System.out.println("Ball shot out!");
			
    	}
    }
	
    public void updateState(){
    	// not needed 
    }


	/**
     * Absorbs the colliding ball and places it in 
     * the Absorber's ball slot
     * 
     * triggers the action of any gadgets tied to this absorber's trigger
     * 
     * @param ball the ball which has collided with the absorber
     */
    public void performCollision(Ball ball){

        ball.becomeAbsorbed();
        ball.setPosition(ballSlot);
        
        if(captured.size() > 0){
            trigger();
        }
        
        captured.add(ball);

        if(triggersSelf){
            trigger();
        }

    }


	/**
     * determined the time (seconds( until a collision will occur between absorber and ball
     * @param ball the ball whose time until collision is checked
     * @return the number of seconds before collision will take place.
     */
	@Override
	public double timeUntilCollision(Ball ball) {
		double timeToCollision = Double.POSITIVE_INFINITY;
		Circle ballCircle = ball.getCircle();
		Vect velocity = ball.getVelocity();
		
        for(LineSegment edge : edges){
            double t = Geometry.timeUntilWallCollision(edge, ballCircle, velocity);
            if(t < timeToCollision){
                timeToCollision = t;
            }
        }
        return timeToCollision;
	}
	
	//draw method


    /**
     * modify the passed position to show a '=' character corresponding to a part of the absorber
     * 
     * @param position at which the absorber is. position should be within (board height +1) * (board width +1) //CHECK
     */
	@Override
	public char drawGadget(Vect position) {
		return '=';
	}
	
	/**
	 * @return the width of the absorber. positive integer
	 */
	@Override
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * @return the height of the absorber. positive integer
	 */
	@Override
	public int getHeight() {
		return this.height;
	}
	
	/** Getter method to return the name of the Absorber
	 * @return the name of the absorber*/
    public String getName(){
        return name;
    }
	/**
     * Returns true if Rep Invariant is maintained. 
     */
      private void checkRep() {
          assert this.height >0;
          assert this.width >0; 
          assert edges.length<=20; //edge must be a line segment of length <=20
          assert edges.length>0;//edge must be a line segment of length >0
           
      }

	@Override
	public boolean transport(Ball ball) {
		return false;
	}

}
