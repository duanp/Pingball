package pingball;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import physics.Vect;

/*
 * Test strategy
 *  trigger:
 *      simple bumper
 *      chained bumpers
 *  updateState:
 *      make sure that rotation starts
 *      make sure that rotation is limited to [0,90]
 *  timeUntilCollision:
 *      line area (non-endpoint) of flipper
 *      endpoints of flipper
 *      rotating flipper
 *      ball's velocity not perpendicular to flipper
 *      ball contacts but does not collide with flipper
 *  performCollision:
 *      line area (non-endpoint) of flipper
 *      endpoints of flipper
 *      rotating flipper
 *      ball's velocity not perpendicular to flipper
 *  drawGadget:
 *      left flipper
 *      right flipper
 */
public class FlipperTest {
    
    static Flipper singleFlipper, baseFlipper, chainedFlipper;
    
    @BeforeClass
    public static void setup() {
        singleFlipper = new Flipper("single Flipper", new Vect(5,5), 0, 0, true);
        chainedFlipper = new Flipper("chained Flipper", new Vect(9,4), 0, 45, false);
        baseFlipper = new Flipper("base Flipper", new Vect(3,8), 0, 90, true, Arrays.asList(chainedFlipper));
    }
    
    @Test
    public void testTrigger() {
        setup();
        singleFlipper.trigger();
        assertTrue(singleFlipper.isTurning());
    }
    
    @Test
    public void testTriggerChained() {
        setup();
        baseFlipper.trigger();
        assertTrue(chainedFlipper.isTurning());
    }
    
    @Test
    public void testUpdateStateShort() {
        setup();
        singleFlipper.trigger();
        long startTime = System.currentTimeMillis();
        try {Thread.sleep(20);} catch(Exception ex) {}
        int actualTimeElapsed = (int) (System.currentTimeMillis()-startTime);
        singleFlipper.updateState();        
        assertEquals(Flipper.ANGULAR_VELOCITY/1000*actualTimeElapsed, singleFlipper.getRotation(), 1e-6);
    }

    @Test
    public void testUpdateStateLong() {
        setup();
        baseFlipper.trigger();
        try {Thread.sleep(100);} catch(Exception ex) {} // note: it only takes 83 ms to complete rotation
        baseFlipper.updateState();
        
        assertEquals(0, baseFlipper.getRotation(), 0.01);
    }
    
    @Test
    public void testTimeUntilCollisionLine() {
        setup();

        

        Ball ball = new Ball(new Vect(6,6), 0, 0, 0, "ball_name");

        ball.setVelocity(new Vect(-1,0));
        assertEquals(0.75, singleFlipper.timeUntilCollision(ball), 1e-6);
    }
    
    @Test
    public void testTimeUntilCollisionPivot() {
        setup();

        
        Ball ball1 = new Ball(new Vect(3,5), 0, 0, 0, "ball_name");

        ball1.setVelocity(new Vect(1,0));
        assertEquals(1.75, singleFlipper.timeUntilCollision(ball1), 1e-6);
    }

    @Test
    public void testTimeUntilCollisionEndpoints() {
        setup();

        

        Ball ball2 = new Ball(new Vect(6,8), 0, 0, 0, "ball_name");

        ball2.setVelocity(new Vect(-1,0));
        assertEquals(0.75, baseFlipper.timeUntilCollision(ball2), 1e-6);
    }
        
    @Test
    public void testTimeUntilCollisionRotatingFlipper() {
        setup();

      
        Ball ball = new Ball(new Vect(8,2), 0, 0, 0, "ball_name");

        ball.setVelocity(new Vect(0,1));
        chainedFlipper.trigger();
        assertEquals(1.75, chainedFlipper.timeUntilCollision(ball), 1e-6);
    }
    
    @Test
    public void testTimeUntilCollisionNotPerpendicular() {
        setup();


        Ball ball = new Ball(new Vect(7.5, 4.5), 0, 0, 0, "ball_name");

        ball.setVelocity(new Vect(1,0));
        assertEquals(2-0.25*Math.sqrt(2), chainedFlipper.timeUntilCollision(ball), 1e-6);
    }
    
    @Test
    public void testTimeUntilCollisionNoCollision() {
        setup();

       

        Ball ball = new Ball(new Vect(2.75,3), 0, 0, 0, "ball_name");

        ball.setVelocity(new Vect(1,0));
        assertEquals(Double.POSITIVE_INFINITY, baseFlipper.timeUntilCollision(ball), 1e-6);
    }
    
    @Test
    public void testPerformCollisionLine() {
        setup();

     
        Ball ball = new Ball(new Vect(3.9,7.75), 0, 0, 0, "ball_name");

        ball.setVelocity(new Vect(0,1));
        assert(baseFlipper.timeUntilCollision(ball) == 0);
        baseFlipper.performCollision(ball);
        assertEquals(0, ball.getVelocity().x(), 1e-6);
        assertEquals(-Flipper.RELFECTION_COEFF, ball.getVelocity().y(), 1e-6);
    }
    
    @Test
    public void testPerformCollisionPivot() {
        setup();


        Ball ball = new Ball(new Vect(5,4.75), 0, 0, 0, "ball_name");

        ball.setVelocity(new Vect(0,1));
        assert(singleFlipper.timeUntilCollision(ball) == 0);
        singleFlipper.performCollision(ball);
        assertEquals(0, ball.getVelocity().x(), 1e-6);
        assertEquals(-Flipper.RELFECTION_COEFF, ball.getVelocity().y(), 1e-6);
    }
    
    @Test
    public void testPerformCollisionOuterEndpoint() {
        setup();


        Ball ball = new Ball(new Vect(5.25,8), 0, 0, 0, "ball_name");

        ball.setVelocity(new Vect(-1,0));
        assert(baseFlipper.timeUntilCollision(ball) == 0);
        baseFlipper.performCollision(ball);
        assertEquals(Flipper.RELFECTION_COEFF, ball.getVelocity().x(), 1e-6);
        assertEquals(0, ball.getVelocity().y(), 1e-6);
    }
    
    @Test
    public void testPerformCollisionRotatingFlipper() {
        setup();

        Ball ball = new Ball(new Vect(4.5,8.25), 0, 0, 0, "ball_name");

        ball.setVelocity(new Vect(0,-3));
        assert(baseFlipper.timeUntilCollision(ball) == 0);
        baseFlipper.trigger();
        baseFlipper.performCollision(ball);
        assertEquals(0, ball.getVelocity().x(), 1e-6);
        double expectedYSpeed = 1.5*(1+Flipper.RELFECTION_COEFF)*Math.toRadians(Flipper.ANGULAR_VELOCITY)+3*Flipper.RELFECTION_COEFF;
        assertEquals(expectedYSpeed, ball.getVelocity().y(), 1e-6);
    }

    @Test
    public void testPerformCollisionNotPerpendicular() {
        setup();

        Ball ball = new Ball(new Vect(3.9,7.75), 0, 0, 0, "ball_name");

        ball.setVelocity(new Vect(1,1));
        assert(baseFlipper.timeUntilCollision(ball) == 0);
        baseFlipper.performCollision(ball);
        assertEquals(1, ball.getVelocity().x(), 1e-6);
        assertEquals(-Flipper.RELFECTION_COEFF, ball.getVelocity().y(), 1e-6);
    }

    @Test
    public void testDrawGadgetLeft() {
        Flipper lf1 = new Flipper("Left flipper1",new Vect(13,13), 90, 35, true);
        assertEquals(' ', lf1.drawGadget(new Vect(13,13)));
        assertEquals(' ', lf1.drawGadget(new Vect(14,13)));
        assertEquals('-', lf1.drawGadget(new Vect(13,14)));
        assertEquals('-', lf1.drawGadget(new Vect(14,14)));
        
        Flipper lf2 = new Flipper("Left flipper2",new Vect(13,13), 270, 69, true);
        assertEquals(' ', lf2.drawGadget(new Vect(13,13)));
        assertEquals('|', lf2.drawGadget(new Vect(14,13)));
        assertEquals(' ', lf2.drawGadget(new Vect(13,14)));
        assertEquals('|', lf2.drawGadget(new Vect(14,14)));
    }
    
    @Test
    public void testDrawGadgetRight() {
        Flipper rf1 = new Flipper("Right flipper1", new Vect(3,8), 180, 44, false);
        assertEquals('|', rf1.drawGadget(new Vect(3,8)));
        assertEquals(' ', rf1.drawGadget(new Vect(4,8)));
        assertEquals('|', rf1.drawGadget(new Vect(3,9)));
        assertEquals(' ', rf1.drawGadget(new Vect(4,9)));
        
        Flipper rf2 = new Flipper("Right flipper2", new Vect(3,8), 0, 77, false);
        assertEquals('-', rf2.drawGadget(new Vect(3,8)));
        assertEquals('-', rf2.drawGadget(new Vect(4,8)));
        assertEquals(' ', rf2.drawGadget(new Vect(3,9)));
        assertEquals(' ', rf2.drawGadget(new Vect(4,9)));        
    }
    
}
