package pingball.Messages;

import static org.junit.Assert.*;

import org.junit.Test;

public class GoodbyePortalBallTest {
    
    /*
     * Testing strategy:
     *   Circularity
     *      - a message that is converted to string and then back to message
     *   Negative velocities
     * 
     * Message format:
     * "GoodbyePortalBall" + destinationBoard + destinationPortal + x + y + vx + vy
     */
    
    @Test
    public void testCircularBehavior() {
        String testMessage = "GoodbyePortalBall someBoard somePortal 5.2 3.5 4.5 2.3";
        GoodbyePortalBall predicted = new GoodbyePortalBall(testMessage);
        GoodbyePortalBall recreated = new GoodbyePortalBall(predicted.messageString());
        assertEquals(predicted, recreated);
    }
    
    @Test
    public void testIntegerValues() {
        String testMessage = "GoodbyePortalBall someBoard somePortal 5 3 4 2";
        GoodbyePortalBall predicted = new GoodbyePortalBall(testMessage);
        GoodbyePortalBall recreated = new GoodbyePortalBall(predicted.messageString());
        assertEquals(predicted, recreated);
    }
    
    @Test
    public void testNegativeVelocities() {
        String testMessage = "GoodbyePortalBall someBoard somePortal 5.2 3.5 -4.5 2.3";
        GoodbyePortalBall predicted = new GoodbyePortalBall(testMessage);
        GoodbyePortalBall recreated = new GoodbyePortalBall(predicted.messageString());
        assertEquals(predicted, recreated);
        
        double xVel = predicted.getBall().getVelocity().x();
        assertEquals(-4.5, xVel, 0.00000001);
    }
    

}
