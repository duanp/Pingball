package pingball.Gadgets;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import physics.Vect;
import pingball.Ball;
import pingball.Board;

public class CircleBumperTest {

    /*
     * Testing Strategy: 
     *          
     * Method: drawGadgetOnBoard() 
     * - Partitions: verify "0" displayed.
     * 
     * Method: collisionBallGadget()
     * - Partitions: verify ball velocity changes appropriately. 
     * 
     * Method: ballHitsGadgetThisTimestep(Ball ball, long time)
     * - Partitions:  verify that a ball hits Circle Bumper if close enough
     *                and no collision if too far
     * 
     * Method: equals
     * - Partitions: verify reflexivity, and equivalence to an identical object,
     *               and non-equivalence to a different object
     */      
    
    
    //drawGadgetOnBoard() 
    @Test 
    public void testDrawBoardCircleBumper() {
        Board testBoard = new Board();
        Gadget testCircBumper = Gadget.circleBumper(1, 1); 
        testBoard.addGadget(testCircBumper); 
        assertTrue(testBoard.drawBoard().contains("O")); 
    }
    
    //collisionBallGadget 
    @Test 
    public void testCollisionBallGadget() {
        Gadget testCircBumper = Gadget.circleBumper(9, 9); 
        Ball testBall = new Ball(new Vect(1,1), new Vect(2,2));
        testCircBumper.collisionBallGadget(testBall, 10000);
        assertTrue(testBall.getVelocity().equals(new Vect(-2,-2))); 
    }
    
  //ballHitsGadgetThisTimestep
    @Test 
    public void testBallHitsGadgetThisTimeStepTrue() {
        Gadget testCircBumper = Gadget.circleBumper(1, 3); 
        Ball testBall = new Ball(new Vect(2,3.5), new Vect(-2,0));
        assertTrue(testCircBumper.ballHitsGadgetThisTimestep(testBall, 10000) < Double.POSITIVE_INFINITY);
    }
    
    @Test 
    public void testBallHitsGadgetThisTimeStepFalse() {
        Gadget testCircBumper = Gadget.circleBumper(1, 3); 
        Ball testBall = new Ball(new Vect(10,10), new Vect(-2,0));
        assertTrue(testCircBumper.ballHitsGadgetThisTimestep(testBall, 1000) == Double.POSITIVE_INFINITY);
    }
    
    //equals
    @Test 
    public void testEqualsReflexivity() {
        Gadget testCircBumper = Gadget.circleBumper(1, 3);
        assertTrue(testCircBumper.equals(testCircBumper));
    }
    
    @Test 
    public void testEqualsIdenticalObject() {
        Gadget testCircBumper1 = Gadget.circleBumper(1, 3);
        Gadget testCircBumper2 = Gadget.circleBumper(1, 3);
        assertTrue(testCircBumper1.equals(testCircBumper2));
    }
    
    @Test 
    public void testEqualsNotEqual() {
        Gadget testCircBumper1 = Gadget.circleBumper(1, 3);
        Gadget testCircBumper2 = Gadget.circleBumper(1, 5);
        assertFalse(testCircBumper1.equals(testCircBumper2));
    }

}
