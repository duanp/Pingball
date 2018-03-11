package pingball.Gadgets;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import physics.Vect;
import pingball.Ball;
import pingball.Board;

public class SquareBumperTest {

    /*
     * Testing Strategy: 
     *          
     * Method: drawGadgetOnBoard() 
     * - Partitions: verify "#" displayed.
     * 
     * Method: collisionBallGadget()
     * - Partitions: verify ball velocity changes appropriately. 
     * 
     * Method: ballHitsGadgetThisTimestep(Ball ball, long time)
     * - Partitions:  verify that a ball hits Square Bumper if close enough
     *                and no collision if too far
     * 
     * Method: equals
     * - Partitions: verify reflexivity, and equivalence to an identical object,
     *               and non-equivalence to a different object
     */      
    
    
    //drawGadgetOnBoard() 
    @Test 
    public void testDrawBoardSquareBumper() {
        Board testBoard = new Board();
        Gadget testSqBumper = Gadget.squareBumper(1, 1); 
        testBoard.addGadget(testSqBumper); 
        assertTrue(testBoard.drawBoard().contains("#")); 
    }
    
    //collisionBallGadget 
    @Test 
    public void testCollisionBallGadget() {
        Gadget testSqBumper = Gadget.squareBumper(1, 1); 
        Ball testBall = new Ball(new Vect(1,1), new Vect(2,2));
        testSqBumper.collisionBallGadget(testBall, 10000);
        assertEquals(testBall.getVelocity().x(), 2, 0.00005);
        assertEquals(testBall.getVelocity().y(), -2, 0.00005);
    }
    
  //ballHitsGadgetThisTimestep
    @Test 
    public void testBallHitsGadgetThisTimeStepTrue() {
        Gadget testSqBumper = Gadget.squareBumper(1, 3); 
        Ball testBall = new Ball(new Vect(2.5,3.5), new Vect(-2,0));
        assertTrue(testSqBumper.ballHitsGadgetThisTimestep(testBall, 10000) < Double.POSITIVE_INFINITY);
    }
    
    @Test 
    public void testBallHitsGadgetThisTimeStepFalse() {
        Gadget testSqBumper = Gadget.squareBumper(1, 3); 
        Ball testBall = new Ball(new Vect(10,10), new Vect(-2,0));
        assertTrue(testSqBumper.ballHitsGadgetThisTimestep(testBall, 1000) == Double.POSITIVE_INFINITY);
    }
    
    //equals
    @Test 
    public void testEqualsReflexivity() {
        Gadget testSqBumper = Gadget.squareBumper(1, 3);
        assertTrue(testSqBumper.equals(testSqBumper));
    }
    
    @Test 
    public void testEqualsIdenticalObject() {
        Gadget testSqBumper1 = Gadget.squareBumper(1, 3);
        Gadget testSqBumper2 = Gadget.squareBumper(1, 3);
        assertTrue(testSqBumper1.equals(testSqBumper2));
    }
    
    @Test 
    public void testEqualsNotEqual() {
        Gadget testSqBumper1 = Gadget.squareBumper(1, 3);
        Gadget testSqBumper2 = Gadget.squareBumper(1, 5);
        assertFalse(testSqBumper1.equals(testSqBumper2));
    }
}
