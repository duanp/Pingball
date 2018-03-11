package pingball.Gadgets;

import static org.junit.Assert.*;

import org.junit.Test;

import physics.Vect;
import pingball.Ball;

public class PortalTest {

    /*
     * Testing Strategy:   
     * 
     * Method: collisionBallGadget()
     * - Partitions: verify ball message is added to message list if collision occurs and 
     *               the portal is connected and message is not added if no collision and 
     *               or portal is not connected
     * 
     * Method: equals
     * - Partitions: verify reflexivity, and equivalence to an identical object,
     *               and non-equivalence to a different object
     *
     * Remaining functionality is tested in CircleBumper as well as manually
     * to see the transfer of balls between portals and boards.
     */

    //collisionBallGadget
    @Test 
    public void testCollisionBallGadgetCollisionConnected() {
        Gadget testPortal = new Portal("name", 3, 3, "name2", "name3");
        ((Portal) testPortal).setConnected(true);
        Ball testBall = new Ball(new Vect(1,1), new Vect(2,2));
        testPortal.collisionBallGadget(testBall, 10000);
        assertEquals(1, testPortal.getGoodbyePortalBallMessages().size());
    }
    
    @Test 
    public void testCollisionBallGadgetCollisionNotConnected() {
        Gadget testPortal = new Portal("name", 3, 3, "name2", "name3");
        Ball testBall = new Ball(new Vect(1,1), new Vect(2,2));
        testPortal.collisionBallGadget(testBall, 10000);
        assertEquals(0, testPortal.getGoodbyePortalBallMessages().size());
    }
    
    @Test 
    public void testCollisionBallGadgetNoCollision() {
        Gadget testPortal = new Portal("name", 3, 3, "name2", "name3");
        Ball testBall = new Ball(new Vect(15,15), new Vect(2,2));
        testPortal.collisionBallGadget(testBall, 1000);
        assertEquals(0, testPortal.getGoodbyePortalBallMessages().size());
    }
    
    
    //equals
    @Test
    public void equalsTest() {
        Portal portaldude = new Portal("name", 2, 2, "name2", "name3");
        CircleBumper circledude = new CircleBumper("name", 2, 2);
        Portal otherportaldude = new Portal("name", 2, 2, "name2", "name3");
        assertFalse(portaldude.equals(circledude));
        assertTrue(portaldude.equals(otherportaldude));
        assertTrue(portaldude.equals(portaldude));
    }

}
