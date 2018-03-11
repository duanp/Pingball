package pingball.Messages;

import static org.junit.Assert.*;

import org.junit.Test;

public class GoodbyeWallBallTest {
    /*
     * Testing strategy:
     *      Circularity
     *      - a message that is converted to string and then back to message
     * - negative velocities
     * 
     * Message format:
     * "GoodbyeWallBall" + boardname + TOP/BOTTOM/LEFT/RIGHT + x + y + vx + vy
     */
    
    @Test
    public void testCircularBehavior() {
        String testMessage = "GoodbyeWallBall someBoard TOP 5.2 3.5 4.5 2.3";
        GoodbyeWallBall predicted = new GoodbyeWallBall(testMessage);
        GoodbyeWallBall recreated = new GoodbyeWallBall(predicted.messageString());
        assertEquals(predicted, recreated);
    }
    
    @Test
    public void testIntegerValues() {
        String testMessage = "GoodbyeWallBall someBoard LEFT 5 3 4 2";
        GoodbyeWallBall predicted = new GoodbyeWallBall(testMessage);
        GoodbyeWallBall recreated = new GoodbyeWallBall(predicted.messageString());
        assertEquals(predicted, recreated);
    }
    
    @Test
    public void testNegativeVelocities() {
        String testMessage = "GoodbyeWallBall someBoard RIGHT 5.2 3.5 -4.5 2.3";
        GoodbyeWallBall predicted = new GoodbyeWallBall(testMessage);
        GoodbyeWallBall recreated = new GoodbyeWallBall(predicted.messageString());
        assertEquals(predicted, recreated);
        
        double xVel = predicted.getBall().getVelocity().x();
        assertEquals(-4.5, xVel, 0.00000001);
    }

}
