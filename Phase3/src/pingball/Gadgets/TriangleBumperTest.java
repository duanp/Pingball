package pingball.Gadgets;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import physics.Vect;
import pingball.Ball;
import pingball.Board;

public class TriangleBumperTest {
    /*
     * Testing Strategy: 
     *          
     * Method: drawGadgetOnBoard() 
     * - Partitions: verify "\\" or "//" displayed, depending on orientation.
     * 
     * Method: collisionBallGadget()
     * - Partitions: verify ball velocity changes appropriately. 
     * 
     * Method: ballHitsGadgetThisTimestep(Ball ball, long time)
     * - Partitions:  verify that a ball hits Triangle Bumper if close enough
     *                and no collision if too far
     * 
     * Method: equals
     * - Partitions: verify reflexivity, and equivalence to an identical object,
     *               and non-equivalence to a different object
     */      
    
    
    //drawGadgetOnBoard() 
    @Test 
    public void testDrawBoardTriangleBumper() {
        Board testBoard = new Board();
        Gadget testTriBumper = Gadget.triangleBumper(1, 1, 90); 
        testBoard.addGadget(testTriBumper); 
        assertTrue(testBoard.drawBoard().contains("\\")); 
    }
    
    @Test 
    public void testDrawBoardCircleBumperOtherOrientation() {
        Board testBoard = new Board();
        Gadget testTriBumper = Gadget.triangleBumper(1, 1, 180); 
        testBoard.addGadget(testTriBumper); 
        assertTrue(testBoard.drawBoard().contains("/")); 
    }
    
    //collisionBallGadget 
    @Test 
    public void testCollisionBallGadget() {
        Gadget testTriBumper = Gadget.triangleBumper(1, 1, 180); 
        Ball testBall = new Ball(new Vect(1,1), new Vect(2,2));
        testTriBumper.collisionBallGadget(testBall, 10000);
        assertTrue(testBall.getVelocity().equals(new Vect(-2,2))); 
    }
    
  //ballHitsGadgetThisTimestep
    @Test 
    public void testBallHitsGadgetThisTimeStepTrue() {
        Gadget testTriBumper = Gadget.triangleBumper(1, 3, 0); 
        Ball testBall = new Ball(new Vect(2.5,3.5), new Vect(-2,0));
        assertTrue(testTriBumper.ballHitsGadgetThisTimestep(testBall, 10000) < Double.POSITIVE_INFINITY);
    }
    
    @Test 
    public void testBallHitsGadgetThisTimeStepFalse() {
        Gadget testTriBumper = Gadget.triangleBumper(1, 3, 0); 
        Ball testBall = new Ball(new Vect(10,10), new Vect(-2,0));
        assertTrue(testTriBumper.ballHitsGadgetThisTimestep(testBall, 1000) == Double.POSITIVE_INFINITY);
    }
    
    //equals
    @Test 
    public void testEqualsReflexivity() {
        Gadget testTriBumper = Gadget.triangleBumper(1, 3, 0);
        assertTrue(testTriBumper.equals(testTriBumper));
    }
    
    @Test 
    public void testEqualsIdenticalObject() {
        Gadget testTriBumper1 = Gadget.triangleBumper(1, 3, 0);
        Gadget testTriBumper2 = Gadget.triangleBumper(1, 3, 0);
        assertTrue(testTriBumper1.equals(testTriBumper2));
    }
    
    @Test 
    public void testEqualsNotEqual() {
        Gadget testTriBumper1 = Gadget.triangleBumper(1, 3, 0);
        Gadget testTriBumper2 = Gadget.triangleBumper(1, 5, 90);
        assertFalse(testTriBumper1.equals(testTriBumper2));
    }

}