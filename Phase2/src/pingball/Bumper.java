package pingball;


import java.util.Arrays;
import java.util.List;

import physics.Circle;
import physics.Geometry;
import physics.LineSegment;
import physics.Vect;

/**
 * Bumper Class
 * 
 * shapes static on the board. reflects the colliding balls.
 * 
 * methods
 * - adjusts the velocity of ball after collision
 * - calculated time until collision
 * - draws the shape # on board
 */
abstract public class Bumper extends Gadget {

    private LineSegment[] edges;
    private Circle[] circularParts;
    
    /**  The bumpers's name */ 
    private final String name;

    /**
     * Construct a new bumper.
     * @param position origin of the bumper
     * @param gadgetsToTrigger list of gadgets that will be triggered when this gadget is triggered
     */
    public Bumper(String name, Vect position, List<Gadget> gadgetsToTrigger){
        this(name, position, new LineSegment[0], new Circle[0], gadgetsToTrigger);
        checkRep();
    }

    public Bumper(String name, Vect position, LineSegment[] edges, Circle[] circularParts, List<Gadget> gadgetsToTrigger) {
        super(position, gadgetsToTrigger);
        this.edges = Arrays.copyOf(edges, edges.length);
        this.name = name; 
        this.circularParts = Arrays.copyOf(circularParts, circularParts.length);
        checkRep(); 
    }

    //trigger function same as super class Gadget-- no need to override

    @Override
    public void updateState(){
        //doesn't need to do anything since static
    }

    @Override
    public double timeUntilCollision(Ball ball) {
        double timeToCollision = Double.POSITIVE_INFINITY;
        Circle ballCircle = ball.getCircle();
        Vect velocity = ball.getVelocity();

        for(Circle circle: circularParts){
            double t = Geometry.timeUntilCircleCollision(circle, ballCircle, velocity);
            if(t < timeToCollision){
                timeToCollision = t;
            }
        }for(LineSegment edge : edges){
            double t = Geometry.timeUntilWallCollision(edge, ballCircle, velocity);
            if(t < timeToCollision){
                timeToCollision = t;
            }
        }
        return timeToCollision;
    }

    @Override
    public void performCollision(Ball ball) {
        assert(this.timeUntilCollision(ball) != Double.POSITIVE_INFINITY);
        Circle ballCircle = ball.getCircle();
        Vect velocity = ball.getVelocity();

        double timeToLineCollision = Double.POSITIVE_INFINITY;
        double timeToCircleCollision = Double.POSITIVE_INFINITY;
        LineSegment collidingLine = null;
        Circle collidingCircle = null;

        for(Circle circle: circularParts){
            double t = Geometry.timeUntilCircleCollision(circle, ballCircle, velocity);
            if(t < timeToCircleCollision){
                timeToCircleCollision = t;
                collidingCircle = circle;
            }
        }
        for(LineSegment edge : edges){
            double t = Geometry.timeUntilWallCollision(edge, ballCircle, velocity);
            if(t < timeToLineCollision){
                timeToLineCollision = t;
                collidingLine = edge;
            }
        }
        Vect newVelocity;
        // note: since there is a collision, there must be a component of wall with timeUntilCollision < infinity
        if(timeToLineCollision < timeToCircleCollision) {
            newVelocity = Geometry.reflectWall(collidingLine, ball.getVelocity());
        }
        else {
            newVelocity = Geometry.reflectCircle(collidingCircle.getCenter(), ball.getPosition(), ball.getVelocity());
        }
        ball.setVelocity(newVelocity);
        trigger();
    }

    /**
     * @return a string containing the position of this bumper
     */
    @Override
    public String toString(){
        return " bumper(" + getPosition().x() +"," + getPosition().y() + ")";
    }

    /**
     * @returns the width of the bumper: 1 L
     */
    @Override
    public int getWidth() {
        return 1;
    }

    /**
     * @returns the width of the bumper: 1 L
     */
    @Override
    public int getHeight() {
        return 1;
    }

    /** Returns true if Rep Invariant is maintained. A bumper is comprised of 0 or more edges, and 0 or more circular parts.
     * A bumper must have width and height of 1, by default, and must have a non-null position. 
     */
    private void checkRep(){
        assert this.edges.length>=0; 
        assert this.circularParts.length>=0; 
        assert this.getWidth()==1;
        assert this.getHeight() ==1;
        assert this.getPosition() !=null; 
    }

}
