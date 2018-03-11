package pingball;

import java.util.ArrayList;
import java.util.List;

import physics.Angle;
import physics.Circle;
import physics.Geometry;
import physics.LineSegment;
import physics.Vect;

/**
* 
* Flipper Spec:
* 
*  Represents a single flipper in a pinball game.
* 
*  Flippers rotate 90 degrees whenever a ball hits it.
*  Flippers rotate at an angluar velocity of 1080 degrees/second 
* 
*  There are two types of flippers: left and right.
*  A left flipper rotates counterclockwise (when hit).
*  A right flipper rotates clockwise (when hit).
* 
*  A flipper has a pivot point and an arm. 
* 
*  A flipper's coefficient of reflection is 0.95.
*  A flipper by default takes up a box of dimensions 2L x 2L
*  
* @author chsamlee
*/

//Rep Invariant: 0 <= orientation <= 90
// Flipper height and width must equal 2. 
// A flipper is either a right flipper or left flipper variant; this cannot change once flipper is instantiated. 
// Flipper's name cannot be changed once instantiated. 
public class Flipper extends Gadget {

    public static final double ANGULAR_VELOCITY = 1080;
    public static final double RELFECTION_COEFF = 0.95;
    public static final int length = 2;
    
    private final Vect pivot;
    private final int orientation;
    private double rotation;
    private boolean isLeftFlipper;
    private boolean isRising;
    private boolean isTurning = false;
    private long triggerTime;

    //the flipper's name
    private final String name;
    
    //Getter methods 
    public String getName() { return name;}
    public int getOrientation() {return orientation;}
    public double getRotation() {return rotation;}
    public boolean isTurning() {return isTurning;}
    public boolean isRising() {return isRising;}
    public int getWidth() {return 2;}
    public int getHeight() {return 2;}
    
    /**
     * Create a new flipper that does not trigger other gadgets.
     * @param position the origin (top-left corner) of gadget
     * @param orientation the orientation of the flipper. must be 0, 90, 180 or 270.
     * @param rotation the initial rotation of flipper. should be between 0 and 90, inclusive.
     * @param isLeftFlipper true if the flipper is the left variant, false if it is the right variant
     */
    public Flipper(String name, Vect position, int orientation, double rotation, boolean isLeftFlipper) {
        this(name, position, orientation, rotation, isLeftFlipper, new ArrayList<Gadget>());
    }
    
    /**
     * Create a new flipper.
     * @param position the origin (top-left corner) of gadget
     * @param orientation the orientation of the flipper. must be 0, 90, 180 or 270.
     * @param rotation the initial rotation of flipper. should be between 0 and 90, inclusive.
     * @param isLeftFlipper true if the flipper is the left variant, false if it is the right variant
     * @param gadgetsToTrigger list of gadgets that will be triggered when this gadget is triggered
     */
    public Flipper(String name, Vect position, int orientation, double rotation, boolean isLeftFlipper, List<Gadget> gadgetsToTrigger) {
        super(position, gadgetsToTrigger);
        assert(orientation >= 0 && orientation < 360 && orientation%90 == 0);
        assert(rotation >= 0 && rotation <= 90);
        this.isLeftFlipper = isLeftFlipper;
        this.name = name; 
        this.orientation = orientation;
        this.rotation = rotation;
        this.isRising = (rotation == 90);
        // calculating the pivot
        Vect[] originToPivotAdjustments = {Vect.ZERO, Vect.Y_HAT, new Vect(1,1), Vect.X_HAT};
        if(isLeftFlipper) {
            this.pivot = getPosition().plus(originToPivotAdjustments[orientation/90]);
        }
        else {
            this.pivot = getPosition().plus(originToPivotAdjustments[originToPivotAdjustments.length-1-orientation/90]);
        }
        checkRep();
    }
    
    /**
     * @return angle between flipper and positive x-axis, with pivot as the vertex
     */
    private Angle getStandardAngle() {
        int turnDirectionLRFactor = isLeftFlipper ? -1 : 1;
        return new Angle(Math.toRadians(turnDirectionLRFactor*(rotation+orientation))).plus(Angle.DEG_90);
    }
    
    /**
     * Multiplier that transforms ANGULAR_VELOCITY to actual angular velocity.
     * @return 1 if rotating counterclockwise, -1 if rotating clockwise, 0 if not rotating
     */
    private int angularVelocityMultiplier() {
        if(!isTurning)
            return 0;
        else if(isRising^isLeftFlipper)
            return 1;
        else
            return -1;
    }
    
    @Override
    public void trigger() {
        triggerTime = System.currentTimeMillis();
        isTurning = true;
        isRising = !isRising;
        // trigger other gadgets
        super.trigger();
    }

    @Override
    public void updateState() {
        if(isTurning) {
            int timeSinceLastTrigger = (int) (System.currentTimeMillis()-triggerTime);
            int turnDirectionFactor = isRising ? 1 : -1;
            rotation += turnDirectionFactor*ANGULAR_VELOCITY*timeSinceLastTrigger/1000.0;
            // limit orientation between 0 and 90
            if(rotation >= 90) {
                rotation = 90;
                isTurning = false;
            }
            else if(rotation <= 0) {
                rotation = 0;
                isTurning = false;
            }
        }
    }

    @Override
    public double timeUntilCollision(Ball ball) {
        // useful quantities
        Vect flipperLineVector = new Vect(getStandardAngle(), length);
        Vect outerEndpoint = pivot.plus(flipperLineVector);
        LineSegment flipperLine = new LineSegment(pivot, outerEndpoint);
        // check time until collision with each component
        double timeUntilCollisionWithOrigin = Geometry.timeUntilCircleCollision(
                new Circle(pivot, 0),
                ball.getCircle(),
                ball.getVelocity()
                );
        double timeUntilCollisionWithOuterEndpoint = Geometry.timeUntilRotatingCircleCollision(
                new Circle(outerEndpoint, 0),
                pivot,
                Math.toRadians(ANGULAR_VELOCITY)*angularVelocityMultiplier(),
                ball.getCircle(),
                ball.getVelocity()
                );
        double timeUntilCollisionWithLine = Geometry.timeUntilRotatingWallCollision(
                flipperLine,
                pivot,
                Math.toRadians(ANGULAR_VELOCITY)*angularVelocityMultiplier(),
                ball.getCircle(),
                ball.getVelocity()
                );
        
        // adjust result if rotation is finished before collision
        if(isTurning) {
            double timeUntilRotationFinishes;
            if(isRising) {
                timeUntilRotationFinishes = (90-rotation)/ANGULAR_VELOCITY;
            }
            else {
                timeUntilRotationFinishes = rotation/ANGULAR_VELOCITY;
            }
            if(timeUntilRotationFinishes < Math.min(timeUntilCollisionWithOuterEndpoint, timeUntilCollisionWithLine)) {
                int turnDirectionLRFactor = isLeftFlipper ? -1 : 1;
                int finalRotation = isRising ? 90 : 0;
                Angle expectedEndpointAngle = new Angle(Math.toRadians(90+turnDirectionLRFactor*(finalRotation+orientation)));
                Vect expectedEndpointLoc = pivot.plus(new Vect(expectedEndpointAngle, length));
                timeUntilCollisionWithOuterEndpoint = Geometry.timeUntilCircleCollision(
                        new Circle(expectedEndpointLoc, 0),
                        ball.getCircle(),
                        ball.getVelocity()
                        );
                timeUntilCollisionWithLine = Geometry.timeUntilWallCollision(
                        new LineSegment(pivot, expectedEndpointLoc),
                        ball.getCircle(),
                        ball.getVelocity()
                        );
            }
        }
        
        return Math.min(timeUntilCollisionWithOrigin,
                Math.min(timeUntilCollisionWithOuterEndpoint,
                        timeUntilCollisionWithLine));
    }

    @Override
    public void performCollision(Ball ball) {
        Vect parallelUnitVector = new Vect(getStandardAngle(), 1);
        // flipperLineVector represents vector from origin to the other endpoint
        Vect flipperLineVector = parallelUnitVector.times(length);
        Vect outerEndpoint = pivot.plus(flipperLineVector);
        LineSegment flipperLine = new LineSegment(pivot, outerEndpoint);
        // check which part of the flipper the ball is colliding with, and take corresponding action
        Vect positionDifference = ball.getPosition().minus(pivot);
        double impactDistance = positionDifference.dot(parallelUnitVector); // distance from origin, outwards = positive
        Vect newVelocity;
        if(impactDistance <= 0) {
            newVelocity = Geometry.reflectCircle(
                    pivot,
                    ball.getPosition(),
                    ball.getVelocity(),
                    RELFECTION_COEFF
                    );
        }
        else if(impactDistance >= length) {
            newVelocity = Geometry.reflectRotatingCircle(
                    new Circle(outerEndpoint, 0),
                    pivot,
                    Math.toRadians(ANGULAR_VELOCITY)*angularVelocityMultiplier(),
                    ball.getCircle(),
                    ball.getVelocity(),
                    RELFECTION_COEFF
                    );
        }
        else {
            newVelocity = Geometry.reflectRotatingWall(
                    flipperLine,
                    pivot,
                    Math.toRadians(ANGULAR_VELOCITY)*angularVelocityMultiplier(),
                    ball.getCircle(),
                    ball.getVelocity(),
                    RELFECTION_COEFF
                    );
        }
        ball.setVelocity(newVelocity);
        trigger();
    }
    
    @Override
    public char drawGadget(Vect position) {
        // get the appropriate gadget symbol
        char nonemptyGadgetSymbol;
        // check if (orientation+rotation) is within 45 degrees of a multiple of 180
        int multiplesOfHalfPiRad = (int) Math.round(roundToNearestHalfPiRad(getStandardAngle())/(Math.PI/2));
        if(multiplesOfHalfPiRad == 1 || multiplesOfHalfPiRad == 3) {
            nonemptyGadgetSymbol = '|';
        }
        else {
            nonemptyGadgetSymbol = '-';
        }
        // check if there should be a symbol
        if(position.equals(pivot)) {
            return nonemptyGadgetSymbol;
        }
        else if(position.minus(pivot).length() > 1.4) {
            return ' ';
        }
        else {
            // angle between position-pivot vector and positive x-axis, rounded to nearest pi/2 radian.
            double relativeAngle = roundToNearestHalfPiRad(position.minus(pivot).angle());
            double standardAngleRounded = (Math.PI/2)*multiplesOfHalfPiRad;
            if(relativeAngle == standardAngleRounded) {
                return nonemptyGadgetSymbol;
            }
            else {
                return ' ';
            }
        }
    }
    
    /**
     * Round an angle to its nearest pi/2 radian.
     * @param angle angle to round
     * @return the nearest multiple of pi/2 radians, standardized to between 0 and 2*pi (includes 0)
     */
    private double roundToNearestHalfPiRad(Angle angle) {
        long multiple = Math.round(angle.radians()/(Math.PI/2));
        multiple %= 4;
        if(multiple < 0) multiple += 4;
        return (Math.PI/2)*multiple;
    }
    
    /** Returns true if Rep invariant is maintained.             */ 
    private void checkRep(){
        assert(orientation >= 0 && orientation < 360 && orientation%90 == 0);
        assert(rotation >= 0 && rotation <= 90);
        assert ((this.angularVelocityMultiplier()==1) || (this.angularVelocityMultiplier()==0) || (this.angularVelocityMultiplier()==-1));
        assert this.getHeight()==2;
        assert this.getWidth()==2; 
    }
    
    /**
     * The Flipper does not transport balls
     */
	@Override
	public boolean transport(Ball ball) {
		return false;
	}
    
}