package pingball;
import physics.*;

/**
 * 
 * A mutable representation of a ball. Has a circle (with a center (x and y coordinate stored in a 
 * vector) and a radius)
 * denoting the space it occupies, and a Vect denoting its velocity.
 *
 * Ball velocities range at least from 0.01 L/sec to 200 L/sec. 0 L/sec is also supported.
 */
public class Ball {
    
    /**
     * Ball velocities
     */
    private Vect velocity;
    private Circle circle;
    private double mass;
    private boolean isAbsorbed;
    
    // Balls have the same radius
    // A ball has diameter 0.5, so it has a radius of 0.25
    private static final double RADIUS = 0.25;
    // Each ball has the same mass. We set the default arbitrarily
    private static final double DEFAULT_MASS = 1.0;
    
    // Framerate is 20 fps
    private static final double TIME_BETWEEN_FRAMES = 1.0/20.0;
    
    /**
     * Creates a new ball
     * By default, a ball starts starts in the upper left corner with no velocity
     * Each ball has the same radius
     */
    public Ball() {
        this.velocity = new Vect(0., 0.);
        this.circle = new Circle(0.,0.,RADIUS);
        this.mass = DEFAULT_MASS;
        this.isAbsorbed = false;
    }
    
    /**
     * Creates a new ball with a given velocity and location
     * A ball still has diameter 0.5
     * 
     * @param vector initial velocity of the ball
     * @param xCoordinate x coordinate of the center of the ball
     * @param yCoordinate y coordinate of the center of the ball
     */
    public Ball(Vect vector, double xCoordinate, double yCoordinate) {
        this.velocity = vector;
        this.circle = new Circle(xCoordinate, yCoordinate, RADIUS);
        this.mass = DEFAULT_MASS;
        this.isAbsorbed = false;
    }
    
    /**
     * @return the velocity of this ball
     */
    public Vect getVelocity() {
        return this.velocity;
    }
    
    /**
     * @return the current x coordinate of the center of the ball
     */
    public double getX() {
        return this.circle.getCenter().x();
    }
    
    /**
     * @return the current y coordinate of the center of the ball
     */
    public double getY() {
        return this.circle.getCenter().y();
    }
    
    /**
     * Changes the velocity of the ball
     * @param newVelocity the new velocity of the ball after the change is made
     */
    public void changeVelocity(Vect newVelocity) {
        this.velocity = newVelocity;
    }
    
    /**
     * Changes the position of the ball
     * @param newX, newY are the new coordinates of the ball's center after the change is made
     */
    public void changePosition(double newX, double newY) {
        this.circle = new Circle(newX, newY, RADIUS);
    }
    
    public boolean getIsAbsorbed() {
        return this.isAbsorbed;
    }
    
    public void setIsAbsorbed(boolean isAbsorbed) {
        this.isAbsorbed = isAbsorbed;
    }
    
    /**
     * Changes the coordinates of the center of the ball
     * @param newX the new x coordinate of the center of the ball after the change is made
     * @param newY the new y coordinate of the center of the ball after the change is made
     */
    public void changeCoordinate(double newX, double newY) {
        this.circle = new Circle(newX, newY, RADIUS);
    }
    
    /**
     * moves the ball ('s center) according to its current velocity
     * Also updates the ball's velocity with respect to gravity and friction
     * Updates according to what it should be on the next frame
     * 
     * Should make the state of the ball the state on the next frame
     * 
     * @param gravity gravity the gravitational constant determined by the board
     * @param mu the first coefficient of friction determined by the board
     * @param mu2 the second coefficient of friction determined by the board
     */
    public void move(double gravity, double mu, double mu2){
        double xCoordinate = getX();
        double yCoordinate = getY();
        xCoordinate += this.velocity.x() * TIME_BETWEEN_FRAMES;
        yCoordinate += this.velocity.y() * TIME_BETWEEN_FRAMES;
        this.circle = new Circle(xCoordinate, yCoordinate, RADIUS);
        
        accelerateFromGravity(gravity);
        accelerateFromFriction(mu, mu2);
    }
    
    /**
     * Changes the ball's velocity due to gravity 
     * Updates according to what it should be on the next frame
     * @param gravity the gravitational constant determined by the board
     */
    public void accelerateFromGravity(double gravity) {
        // Only velocity in the y direction is affected
        double newYVelocity = this.velocity.y();
        newYVelocity += gravity * TIME_BETWEEN_FRAMES;
        this.velocity = new Vect(this.velocity.x(), newYVelocity);
        
    }
    
    /**
     * Changes the Ball's velocity due to friction
     * According to the formula:  V_new = V_old * ( 1 - mu * delta(t) - mu2 * |V_old| * delta(t) )
     * @param mu the first coefficient of friction determined by the board
     * @param mu2 the second coefficient of friction determined by the board
     */
    public void accelerateFromFriction(double mu, double mu2) {
        double oldXVelocity = this.velocity.x();
        double newXVelocity = oldXVelocity * (1- mu*TIME_BETWEEN_FRAMES 
                - mu2*Math.abs(oldXVelocity)*TIME_BETWEEN_FRAMES);
        double oldYVelocity = this.velocity.y();
        double newYVelocity = oldYVelocity * (1- mu*TIME_BETWEEN_FRAMES 
                - mu2*Math.abs(oldYVelocity)*TIME_BETWEEN_FRAMES);
        
        this.velocity = new Vect(newXVelocity, newYVelocity);
    }    
    
    /**
     * Detects whether a ball collides with another ball right now
     * This ball collides with the other ball if they overlap
     * @param otherBall the other ball to check collision with
     * @return true if the this ball collides with the other ball
     */
    public boolean detectBallCollision(Ball otherBall) {
        double centerDistances = Math.sqrt(Geometry.distanceSquared(this.circle.getCenter(), 
                otherBall.circle.getCenter()));
        double sumOfRadii = this.circle.getRadius() + otherBall.circle.getRadius();
        return (centerDistances <= sumOfRadii);
    }
    
    /**
     * Changes the Ball's velocity to reflect a collision with another Ball of equal mass
     * @param otherBall - the other Ball object
     */
    public void collideWithBall(Ball otherBall) {
        Vect newThisVelocity = Geometry.reflectBalls(this.circle.getCenter(), this.mass, this.velocity,
                otherBall.circle.getCenter(), otherBall.mass, otherBall.velocity).v1;
        Vect newOtherVelocity = Geometry.reflectBalls(this.circle.getCenter(), this.mass, this.velocity,
                otherBall.circle.getCenter(), otherBall.mass, otherBall.velocity).v2;
        
        this.velocity = newThisVelocity;
        otherBall.velocity = newOtherVelocity;
    }
    
    /**
     * Gets the value of the Ball's radius
     * @return a double equal to the value of the radius
     */
    public double getRadius(){
        return RADIUS;
    }
    
    /**
     * Gets the Circle Object representing the Ball
     * @return the Circle Object representing the Ball
     */
    public Circle getCircle(){
        return this.circle;
    }
    
    public boolean equals(Ball otherBall){
        return (this.getX() == otherBall.getX() && this.getY() == otherBall.getY() && this.getRadius() == otherBall.getRadius());
    }
}
