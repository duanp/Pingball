package pingball;

import physics.*;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * 
 * Ball
 * 
 * A mutable ball in a Pingball game.
 * 
 * Abstraction Function: Represents a ball at the specified position (x,y) on a Pingball board
 * Rep Invariant: Position cannot be outside of board playing area of 20L X 20 L
 * Ball velocity is capped at .2L/ms. If you try to make it go faster,
 * it won't fail, but instead only update velocity up to .2L/ms. This is so you don't get errors
 * if a gadget decides to throw your ball around at lightspeed, instead ball refuses to move so fast,
 * and you can still play a reasonable speed game of pingball.
 * 
 * 
 * 
 */
public class Ball {
    // Rep invariant: 
    //      A Ball is mutable. It's position and velocity are free to be anything,
    //           and can change. There isn't really an invariant, so no checkrep.
    
    private String NAME;
    private Vect position;
    private Vect velocity;
    private double radius = .25;

    private boolean isImmune;
    
    
    /**
     * Constructor for Ball, takes in values in display coordinates.
     * @param name name of ball
     * @param position Vect of initial position of ball, display coordinates
     * @param velocity Vect of initial velocity of ball, display coordinates
     */
    public Ball(String name, Vect position, Vect velocity){
        this(position, velocity);
        this.NAME = name;
    }
    
    /**
     * Constructor for Ball, takes in values in display coordinates.
     * @param position Vect of initial position of ball, display coordinates
     * @param velocity Vect of initial velocity of ball, display coordinates
     *@param friction1 the friction1 parameter
     * @param friction2 the friction2 parameter
     */
    public Ball(Vect position, Vect velocity){
        //convert out to cartesian
        this.position = new Vect(position.x(), -position.y());
        double speed = velocity.length();
        speed = capSpeed(speed);
        
        //this.velocity = new Vect(Angle.ZERO.minus(velocity.angle()), speed);
        this.velocity = new Vect(velocity.x(),-1*velocity.y());

        this.isImmune = false;
    }
    
    
    /**
     * draws the ball into the given board's display array. The ball has to exist on the board.
     * @param board the board the ball belongs to.
     */
    public void drawBallOnBoard(Board board){
        int ballX = (int) this.getPosition().x();
        int ballY = (int) this.getPosition().y();
        board.getDisplayArray()[ballY][ballX] = "*";
    }
    
    /**
     * Getter for circle representation of the Ball (in display coordinates)
     * @return a circle representing the ball's actual size.
     */
    public Circle getCircle(){
        return new Circle(this.getPosition().x(), this.getPosition().y(), this.radius);
    }
    
    /**
     * Getter for position vector (in display coordinates)
     * @return the position vector
     */
    public Vect getPosition(){
        return new Vect(this.position.x(),-this.position.y());
    }
    
    /**
     * Getter for radius. Returns display coordinates.
     * @return ball radius.
     */
    public double getRadius(){
        return this.radius;
    }
    
    /**
     * Getter for velocity vector. Returns display coordinates, in L/s.
     * @return the velocity vector
     */
    public Vect getVelocity(){
        Vect displayVelocity = new Vect(this.velocity.x(),-1*this.velocity.y());
        return displayVelocity;
    }
    
    /**
     * Predicts, given no obstacles or limitations, where the ball will be after positive timestep milliseconds.
     * Returns display coordinates.
     * @param timestep the positive number of milliseconds that will have passed
     * @param gravty the value of gravity for the board the ball is in L/s.
     * @return the position vector where the ball is predicted to be, display coordinates.
     */
    public Vect predictPositionUpdate(long timestep, Vect gravity){
        Vect newPosition = this.velocity.times((double) timestep/1000.)
                .plus(gravity.times(.5*timestep/1000.*timestep/1000.))
                .plus(this.position);
        return new Vect(newPosition.x(),-newPosition.y());
    }
    
    /**
     * Predicts, given no obstacles or limitations, the velocity of the ball after timestep in milliseconds.
     * @param timestep the positive number of milliseconds that will have passed
     * @param gravity the value of gravity for the board the ball is in, in L/s.
     * @return the velocity vector where the ball is predicted to be going, display coordinates
     */
    public Vect predictVelocityUpdate(long timestep, Vect gravity, double friction1, double friction2) {
        Vect newVelocityGravity = this.velocity.plus(gravity.times(timestep/1000.));
        Vect newVelocityFriction = newVelocityGravity.times(1 - friction1 * timestep/1000. - friction2 * Math.abs(newVelocityGravity.length()) * timestep/1000.);
        double speed = newVelocityFriction.length();
        speed = capSpeed(speed);
        Vect newVelocity = new Vect(Angle.ZERO.minus(newVelocityFriction.angle()), speed);
        return newVelocity;
    }
    
    /**
     * Updates the ball object to reflect time passing, given no obstacles or other limitations.
     * @param timestep amount of time that has passed
     * @param gravity the gravity vector of the board the ball is in.
     */
    public void update(long timestep, Vect gravity, double friction1, double friction2) {
        //account for gravity and friction
        Vect newPosition = this.velocity.times((double) timestep/1000.)
                .plus(gravity.times(.5*timestep/1000.*timestep/1000.))
                .plus(this.position);
        Vect newVelocityGravity = this.velocity.plus(gravity.times(timestep/1000.));
        Vect newVelocityFriction = newVelocityGravity.times(1 - friction1 * timestep/1000. - friction2 * Math.abs(newVelocityGravity.length()) * timestep/1000.);
        this.position = newPosition;
        double speed = newVelocityFriction.length();
        speed = capSpeed(speed);
        this.velocity = new Vect(newVelocityFriction.angle(), speed);
    }
    
    /**
     * Setter for position vector Takes in display coordinates.
     */
    public void setPosition(Vect newPosition){
        this.position = new Vect(newPosition.x(), -1*newPosition.y());
    }
    
    /**
     * Setter for velocity vector. Takes in display coordinates.
     */
    public void setVelocity(Vect newVelocity){
        double speed = newVelocity.length();
        speed = capSpeed(speed);
        Vect newLimitedVelocity = new Vect(newVelocity.angle(), speed);
        this.velocity = new Vect(newLimitedVelocity.x(), -1*newLimitedVelocity.y());
    }
    
    /**
     * Deals with capping the speed at 200, as per the specs
     * @param speed the speed that the ball is going at
     * @return a capped velocity for the ball
     */
    public double capSpeed(double speed){
        if(speed > 200){
            speed = 200;
        }else if(speed < -200){
            speed = -200;
        }
        return speed;
    }
    
    // implements observational equality
    @Override
    public boolean equals(Object other){
        if (other instanceof Ball){
            Ball otherBall = (Ball) other;
             
            if ( ((NAME == null && otherBall.NAME == null) || (NAME!=null && otherBall.NAME!=null && NAME.equals(otherBall.NAME))) && 
                    position.equals(otherBall.position) &&
                    velocity.equals(otherBall.velocity) && radius == otherBall.radius) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Ball " + NAME + " at position " + position + " with velocity " + velocity; 
    }
    
    /**
     * Makes the ball immune; same as setImmune(true); prevents the ball from getting
     * sucked into portals until set as unimmune again
     */
    public void makeImmune() {
        this.setImmunue(true);   
    }
    
    /**
     * Makes the ball unimmune; same as setImmune(false); allows the ball to get
     * sucked into portals
     */
    public void makeUnimmune() {
        this.setImmunue(false);   
    }

    /**
     * Tells whether or not the ball is immune or not;
     * if it is immune, the ball will not get sucked into portals
     * @return the immunity of the ball: true for immune, false for not
     */
    public boolean isImmune() {
        return this.isImmune;
    }

    /**
     * Sets the immunity of the ball; if true, ball is immune and will not go
     * through portals it collides with. If false, ball will go through portals
     * it collides with.
     * @param isImmune
     */
    public void setImmunue(boolean isImmune) {
        this.isImmune = isImmune;
    }
    
    /**
     * Gives a shape representing this ball which can be drawn onto a PingballPanel.
     * The shape must be at the exact pixel coordinates that it would be on the pingball
     * board, to the scale given, with the origin at the upper left and coordinates increasing
     * down and to the right.
     * @param scale the number of pixels in the width and height of a single 1L by 1L square
     *        on the pingball board
     * @return the shape of this gadget at the correct location
     */
    public Shape getShape(int scale){
        double x = position.x() * scale - scale/4.0;
        double y = -1*(position.y() * scale) - scale/4.0;
        double width = scale/2.0;
        double height = scale/2.0;
        return new Ellipse2D.Double(x,y,width,height);
    }
}