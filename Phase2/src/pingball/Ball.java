package pingball;
import physics.Circle;
import physics.Geometry;
import physics.Geometry.VectPair;
import physics.Vect;

/**
 * A mutable representation of a ball. 
 * Has a circle with center x and y coordinate stored in Vector position.
 * The gravity, friction, drag for the ball's movement on the board can also be set.
 * The radius is fixed at 0.25.
 */
public class Ball {
    


    
    public static final double radius = 0.25;
    final double gravity, frictionCoefficient, dragCoefficient;
    /** The ball's name */
    final String name;
    
    
    /**Position vector of ball located at (x,y) where X- and Y are coordinates of its center (in real space)
     * Rep invariant:
     *        The center must be nonnegative and no greater than 20 - (diameter/2) */
    private Vect position;
    
    /** Velocity vectors (vx,vy). Unit: lengths/second
     *  Rep invariant: 
     *      Must (at least) cover range of 0.01L/s - 200L/s. Must support 0L/s (stationary)*/
    private Vect velocity= new Vect(0,0);
    
    private boolean inQueue=false; //if inQueue is true, the ball is stationary and in the absorber
    
    // Look out for possible missed ball-ball collision
    /**
     * constructs a new ball
     * @param position, position of the ball on the board, represented by a vector
     * @param gravity, the downwards acceleration experienced by the ball per second time 
     * @param friction, the friction coefficient for the ball's movement on the board
     * @param drag, the drag coefficient for the ball's movement on the board
     */



    public Ball(Vect position, double gravity, double friction, double drag, String name) {
        this.name = name;

        this.position=position;
        this.gravity=gravity;
        frictionCoefficient=friction;
        dragCoefficient=drag;
        checkRep();
    }
    /**
     * Moves the ball to the position it will be at in a given time assuming its velocity stays constant for that time
     * Changes the velocity of the ball to the value it will have after a given time
     * @param time, for which ball moves between present and new position
     */
    public void move(double time) {
    	position=new Vect(position.x()+velocity.x()*time,position.y()+velocity.y()*time);
    	double newX=velocity.x()*(1-frictionCoefficient*time -dragCoefficient*Math.abs(velocity.x()*time));
    	double newY=velocity.y()*(1-frictionCoefficient*time -dragCoefficient*Math.abs(velocity.y()*time))+gravity*time;
    	setVelocity( new Vect(newX,newY));

    }
    
    /**
     * Returns the time after which this ball and the other ball will collide given their present velocities and circles sizes
     * @param otherBall, another ball on the board
     * @return time, change in time after which balls will collide
     */
    public double timeUntilCollision(Ball otherBall) {
    	return Geometry.timeUntilBallBallCollision( getCircle(), getVelocity(), otherBall.getCircle(), otherBall.getVelocity());
    }
    /**
     * Sets the velocity of this ball and the other ball after a collision, given they are colliding.
     * @param otherBall
     */
    public void performCollision(Ball otherBall) {
    	VectPair ballVectors=Geometry.reflectBalls(position, 1, velocity, otherBall.getPosition(), 1, otherBall.getVelocity());
    	setVelocity(ballVectors.v1);
    	otherBall.setVelocity(ballVectors.v2);
    }
    

    
    /**
     * @return the position of this ball
     */

    public Vect getPosition(){
    	return position;
    }
    
    /**
     * @return the velocity of this ball
     */
    public Vect getVelocity(){
    	return velocity;
    }
    
    /**
     * @return circle representing the geometry of the ball and its position on the board
     */
    public Circle getCircle(){
    	return new Circle(position,radius);
    }
    
    /**
     * Sets the velocity of this ball
     * @param velocity
     */
    public void setVelocity(Vect velocity){
    	this.velocity=velocity;
    }
    
    public String getName(){
        return name;
    }


    
    
    //FOR ABSORBED:
    /**
     * stop this ball and place it in queue of absorber. 
     * In the queue, balls have zero velocity and are not affected by physical accelerations
     */
    public void becomeAbsorbed(){
        inQueue = true;
        velocity = new Vect(0,0);
    }
    
    /**
     * release this ball from the absorber, allowing it to be affected by
     * accelerations, friction, etc
     */
    public void becomeReleased(){
        inQueue = false;
    }
    
    public boolean getInQueue(){
    	return inQueue;
    }

	//FOR ABSORBER:
	/**
	 * move ball to be at the specified position
	 * @param new position of the ball
	 */
	public void setPosition(Vect position) {
		this.position=position;
	}
    @Override
    public boolean equals(Object other){
        if(other instanceof Ball){
        	if(((Ball) other).getPosition().equals(position) && ((Ball) other).getVelocity().equals(velocity) && ((Ball) other).getCircle().equals(getCircle())){
        		return true;
        	}
        	return false;
        }
        return false;
    }
    /**
     * Returns true if Rep Invariant is maintained. 
     */
      private void checkRep() {
          assert this.position != null; 
          assert this.velocity !=null; 
    
          }
         
           
      }

