package pingball;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import physics.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test file for Gadget 
 * Each method is tested and is partitioned as follows:
 * 
 * 1. All the getters and trigger() are tested on a single Gadget instance
 * 2. DetectBallCollision: tested on every Gadget variant, and the cases
 *                         are no collision, collision with each type of
 *                         side, and corner collisions
 * 3. Action: tested to a single variant with no action, then tested on flipper
 *            and absorber
 * 4. ReflectBall: tested on every variant
 *                      
 * @category no_didit
 * 
 *
 */
public class GadgetTest {
    private static final int MOVING_ANGULAR_VELOCITY = 1080;
    // Framerate is 20 fps
    private static final double TIME_BETWEEN_FRAMES = 1.0/20.0;
    Gadget circleBumper1 = Gadget.circleBumper(1, 1);
    Gadget circleBumper2 = Gadget.circleBumper(3, 1);
    Gadget squareBumper1 = Gadget.squareBumper(10, 10);
    Gadget verticalLeftOuterWall = Gadget.outerWall(0, true, true);
    Gadget rotatedTriangleBumper1 = Gadget.triangleBumper(13, 14, Angle.DEG_90);
    Ball ball1_circle = new Ball(new Vect(1.0, 1.0), 3.2, 1.2);
    Ball ball1_square = new Ball(new Vect(1.0, 1.0), 12., 11.);
    Ball ball2_square = new Ball(new Vect(1.0, 1.0), 10.5, 9.8);
    Ball ball3_square = new Ball(new Vect(1.0, 1.0), 10., 11.25);
    Ball ball1_triangle = new Ball(new Vect(1.0, 1.0), 13.5, 13.75);
    Ball ball2_triangle = new Ball(new Vect(1.0, 1.0), 13.5, 14.7);
    Ball ball3_triangle = new Ball(new Vect(1.0, 1.0), 14.25, 14.);
    
    

    @Test
    public void testTrigger() {
        circleBumper1.trigger(circleBumper2);
        assertTrue(circleBumper2.getIsTriggered());
    }
    
    @Test
    public void testGetX() {
        assertEquals(circleBumper2.getX(), 3);
    }
    
    @Test
    public void testGetY() {
        assertEquals(circleBumper1.getY(), 1);
    }
    
    @Test
    public void testGetCoefficientOfReflection() {
        assertTrue(circleBumper1.getCoefficientOfReflection() == 1.0);
    }
    
    @Test
    public void testGetSymbol() {
        assertEquals(circleBumper1.getSymbol(), '0');
    }
    
    Gadget absorber1 = Gadget.absorber(4, 5, 6, 3);
    
    @Test
    public void testGetWidthAbsorber() {
        assertEquals(absorber1.getWidth(), 6);
    }
    
    @Test
    public void testGetHeightAbsorber() {
        assertEquals(absorber1.getHeight(), 3);
    }
    
    @Test
    public void testGetWidthOuterWall() {
        assertEquals(verticalLeftOuterWall.getWidth(), 1);
    }
    
    @Test
    public void testGetHeightOuterWall() {
        assertEquals(verticalLeftOuterWall.getHeight(), 20);
    }
    
    @Test
    public void testGetIsTriggered() {
        assertFalse(absorber1.getIsTriggered());
    }
    
    @Test
    public void testSetIsTriggered() {
        absorber1.setIsTriggered(true);
        assertTrue(absorber1.getIsTriggered());
    }
    
    @Test
    public void testDetectBallCollisionCircleBumperNoCollision() {
        assertFalse(circleBumper1.detectBallCollision(ball1_circle));
    }
    
    @Test
    public void testDetectBallCollisionCircleBumperCollision() {
        assertTrue(circleBumper2.detectBallCollision(ball1_circle));
    }
    
    @Test
    public void testDetectBallCollisionSquareBumperNoCollision() {
        assertFalse(squareBumper1.detectBallCollision(ball1_square));
    }
    
    @Test
    public void testDetectBallCollisionSquareBumperSideCollision() {
        assertTrue(squareBumper1.detectBallCollision(ball2_square));
    }
    
    @Test
    public void testDetectBallCollisionSquareBumperCornerCollision() {
        assertTrue(squareBumper1.detectBallCollision(ball3_square));
    }
    
    @Test
    public void testDetectBallCollisionTriangleBumperSideCollision() {
        assertTrue(rotatedTriangleBumper1.detectBallCollision(ball1_triangle));
    }
    
    @Test
    public void testDetectBallCollisionTriangleBumperHypotenuseCollision() {
        assertTrue(rotatedTriangleBumper1.detectBallCollision(ball2_triangle));
    }
    
    @Test
    public void testDetectBallCollisionTriangleBumperCornerCollision() {
        assertTrue(rotatedTriangleBumper1.detectBallCollision(ball3_triangle));
    }
    
    @Test
    public void testDetectBallCollisionVerticalLeft() {
        Gadget verticalLeftFlipper = Gadget.flipper(15, 15, true, new Angle(0.));
        Ball ball = new Ball(new Vect(1.0, 1.0), 15.1, 15.5);
        assertTrue(verticalLeftFlipper.detectBallCollision(ball));
    }
    
    @Test
    public void testDetectBallCollisionHorizontalRightFlipper() {
        Gadget horizontalRightFlipper = Gadget.flipper(15, 15, false, Angle.DEG_90);
        Ball ball = new Ball(new Vect(1.0, 1.0), 16., 17.1);
        assertTrue(horizontalRightFlipper.detectBallCollision(ball));
    }
    
    @Test
    public void testDetectBallCollisionRotatedLeftFlipper() {
        Gadget rotatedLeftFlipper = Gadget.flipper(15, 15, true, new Angle(0.));
        rotatedLeftFlipper.action();
        Ball ball = new Ball(new Vect(1.0, 1.0), 16., 16.);
        assertTrue(rotatedLeftFlipper.detectBallCollision(ball));
    }
    
    @Test 
    public void testDetectBallCollisionAbsorberExterior(){
        Gadget absorber1 = Gadget.absorber(6,7,10,3);
        Ball ball = new Ball(new Vect(1.0, 1.0), 16.15, 9.);
        assertTrue(absorber1.detectBallCollision(ball));
    }
    
    @Test 
    public void testDetectBallCollisionAbsorberInterior(){
        Gadget absorber1 = Gadget.absorber(6,7,10,3);
        Ball ball = new Ball(new Vect(1.0, 1.0), 7., 9.);
        assertTrue(absorber1.detectBallCollision(ball));
    }
    
    @Test 
    public void testDetectBallCollisionOuterWall(){
        Gadget outerWall = Gadget.outerWall(0,true,true);
        Ball ball = new Ball(new Vect(1.0, 1.0), 0.25, 10.);
        assertTrue(outerWall.detectBallCollision(ball));
    }
    
    @Test
    public void testActionNoAction(){
        Gadget circleBumper = Gadget.circleBumper(3,3, new ArrayList<Gadget>(Arrays.asList(circleBumper1, circleBumper2)));
        circleBumper.action();
        assertFalse(circleBumper.getIsTriggered());
        assertTrue(circleBumper1.getIsTriggered());
        assertTrue(circleBumper2.getIsTriggered());
       
    }
    
    @Test
    public void testActionFlipper(){
        Gadget horizontalRightFlipper = Gadget.flipper(15, 15, false, Angle.DEG_90, new ArrayList<Gadget>(Arrays.asList(circleBumper1, circleBumper2)));
        LineSegment newLine = new LineSegment(15., 17.,17.,17.);
        newLine = Geometry.rotateAround(newLine, new Vect(17.,17.), new Angle(MOVING_ANGULAR_VELOCITY*TIME_BETWEEN_FRAMES*Math.PI/180.));
        horizontalRightFlipper.action();
        HashSet<Vect> newLineEndpoints= new HashSet<Vect>();
        newLineEndpoints.add(newLine.p1());
        newLineEndpoints.add(newLine.p2());
        HashSet<Vect> flipperEndpoints= new HashSet<Vect>();
        flipperEndpoints.add(((Flipper)horizontalRightFlipper).side.p1());
        flipperEndpoints.add(((Flipper)horizontalRightFlipper).side.p2());
        assertEquals(flipperEndpoints, newLineEndpoints);
        assertFalse(horizontalRightFlipper.getIsTriggered());
        assertTrue(circleBumper2.getIsTriggered());
        
       
    }
    
    @Test
    public void testActionFlipperRotate90(){
        Gadget horizontalRightFlipper = Gadget.flipper(15, 15, false, Angle.DEG_90, new ArrayList<Gadget>(Arrays.asList(circleBumper1, circleBumper2)));
        LineSegment newLine = new LineSegment(15., 17.,17.,17.);
        newLine = Geometry.rotateAround(newLine, new Vect(17.,17.), new Angle(Math.PI/2));
        horizontalRightFlipper.action();
        horizontalRightFlipper.action();
        HashSet<Vect> newLineEndpoints= new HashSet<Vect>();
        newLineEndpoints.add(newLine.p1());
        newLineEndpoints.add(newLine.p2());
        HashSet<Vect> flipperEndpoints= new HashSet<Vect>();
        flipperEndpoints.add(((Flipper)horizontalRightFlipper).side.p1());
        flipperEndpoints.add(((Flipper)horizontalRightFlipper).side.p2());
        assertEquals(flipperEndpoints, newLineEndpoints);
        assertFalse(horizontalRightFlipper.getIsTriggered());
        assertTrue(circleBumper2.getIsTriggered());
        
       
    }
    
    @Test 
    public void testActionAbsorber(){
        Gadget absorber1 = Gadget.absorber(4, 5, 6, 3, new ArrayList<Gadget>(Arrays.asList(circleBumper1, circleBumper2)));
        absorber1.reflectBall(new Ball(new Vect(0, 1.0), 10.5, 10));
        absorber1.action();
        assertTrue(((Absorber)absorber1).getHasBall());
        assertFalse(absorber1.getIsTriggered());
        assertTrue(circleBumper1.getIsTriggered());
        assertTrue(circleBumper2.getIsTriggered());

    }
    
    @Test
    public void testReflectBallSquareBumper() {
        Gadget squareBumper = Gadget.squareBumper(10, 10);
        Ball ball = new Ball(new Vect(0, 1.0), 10.5, 10);
        squareBumper.reflectBall(ball);
        assertEquals(new Vect(0, -1.0), ball.getVelocity());
    }
    
    @Test
    public void testReflectBallCircleBumper() {
        Gadget circleBumper = Gadget.circleBumper(10, 10);
        Ball ball = new Ball(new Vect(0, 1.0), 10.5, 10);
        circleBumper.reflectBall(ball);
        assertEquals(new Vect(0, -1.0), ball.getVelocity());
    }
    
    @Test
    public void testReflectBallTriangleBumper() {
        Gadget triangleBumper = Gadget.triangleBumper(10, 10, Angle.ZERO);
        Ball ball = new Ball(new Vect(0, 1.0), 10.5, 10);
        triangleBumper.reflectBall(ball);
        assertEquals(new Vect(0, -1.0), ball.getVelocity());
    }
    
    @Test
    public void testReflectBallTriangleBumperFlipped() {
        Gadget triangleBumper = Gadget.triangleBumper(10, 10, Angle.DEG_180);
        Ball ball = new Ball(new Vect(0, 1.0), 10.5, 10.5);
        triangleBumper.reflectBall(ball);
        assertEquals(new Vect(-1.0, 0), ball.getVelocity());
    }
    
    @Test
    public void testReflectBallFlipper() {
        Gadget leftFlipper = Gadget.flipper(10, 10, true, Angle.DEG_90);
        Ball ball = new Ball(new Vect(0, 1.0), 10.5, 10.5);
        leftFlipper.reflectBall(ball);
        //Coefficient of reflection is 0.95
        assertEquals(new Vect(0, -0.95), ball.getVelocity());
    }
    
    @Test
    public void testReflectOuterWall() {
        Gadget outerWall = Gadget.outerWall(20, false, true);
        Ball ball = new Ball(new Vect(0, 1.0), 10, 19.5);
        outerWall.reflectBall(ball);
        //Coefficient of reflection is 0.95
        assertEquals(new Vect(0, -1.0), ball.getVelocity());
    }
    
    @Test 
    public void testReflectAbsorber(){
        Gadget absorber1 = Gadget.absorber(4, 5, 6, 3, new ArrayList<Gadget>(Arrays.asList(circleBumper1, circleBumper2)));
        Ball ball = new Ball(new Vect(0, 1.0), 10.5, 10);
        absorber1.reflectBall(ball);
        assertEquals(new Vect(0., 0.), ball.getVelocity());

    }
}
