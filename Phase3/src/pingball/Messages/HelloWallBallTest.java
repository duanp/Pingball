package pingball.Messages;

import static org.junit.Assert.*;

import org.junit.Test;

public class HelloWallBallTest {
    /*
     * Testing strategy:
     *   Circularity
     *      - a message that is converted to string and then back to message
     *   Negative velocities
     * 
     * Message format:
     * "HelloWallBall" + TOP/BOTTOM/LEFT/RIGHT + x + y + vx + vy
     */
    
    @Test
    public void testCircularBehavior() {
        String testMessage = "HelloWallBall TOP 5.2 3.5 4.5 2.3";
        HelloWallBall predicted = new HelloWallBall(testMessage);
        HelloWallBall recreated = new HelloWallBall(predicted.messageString());
        assertEquals(predicted, recreated);
    }
    
    @Test
    public void testIntegerValues() {
        String testMessage = "HelloWallBall RIGHT 5 3 4 2";
        HelloWallBall predicted = new HelloWallBall(testMessage);
        HelloWallBall recreated = new HelloWallBall(predicted.messageString());
        assertEquals(predicted, recreated);
    }
    
    @Test
    public void testNegativeVelocities() {
        String testMessage = "HelloWallBall BOTTOM 5.2 3.5 -4.5 2.3";
        HelloWallBall predicted = new HelloWallBall(testMessage);
        HelloWallBall recreated = new HelloWallBall(predicted.messageString());
        assertEquals(predicted, recreated);
        
        double xVel = predicted.getBall().getVelocity().x();
        assertEquals(-4.5, xVel, 0.00000001);
    }
    

}
