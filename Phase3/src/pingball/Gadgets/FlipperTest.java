package pingball.Gadgets;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.BeforeClass;
import org.junit.Test;

import physics.Angle;
import physics.Circle;
import physics.Geometry;
import physics.Vect;
import pingball.Ball;
import pingball.Board;

public class FlipperTest {
    /*
     * Testing Strategy: 
     *          
     * Method: drawGadgetOnBoard() 
     * - Partitions: verify "|" or "-" displayed, depending on orientation.
     * 
     * Method: collisionBallGadget()
     * - Partitions: verify ball velocity changes appropriately. 
     * 
     * Method: ballHitsGadgetThisTimestep(Ball ball, long time)
     * - Partitions:  verify that a ball hits flipper if close enough
     *                and no collision if too far
     * 
     * Method: equals
     * - Partitions: verify reflexivity, and equivalence to an identical object,
     *               and non-equivalence to a different object
     */      


    //drawGadgetOnBoard() 
    @Test 
    public void testDrawBoardFlipperVertical() {
        Board testBoard = new Board();
        Gadget testFlipper = new Flipper(2, 2, 0, true, false); 
        testBoard.addGadget(testFlipper); 
        assertTrue(testBoard.drawBoard().contains("-")); 
    }

    @Test 
    public void testDrawBoardFlipperHorizontal() {
        Board testBoard = new Board();
        Gadget testFlipper = new Flipper(2, 2, 90, true, false); 
        testBoard.addGadget(testFlipper); 
        assertTrue(testBoard.drawBoard().contains("|")); 
    }


    //collisionBallGadget 
    @Test 
    public void testCollisionBallGadget() {
        Gadget testFlipper = new Flipper(2, 2, 90, true, false); 
        Ball testBall = new Ball(new Vect(1,1), new Vect(2,2));
        testFlipper.collisionBallGadget(testBall, 10000);
        assertTrue(testBall.getVelocity().equals(new Vect(-1.9,-1.9))); 
    }


    //ballHitsGadgetThisTimestep
    @Test 
    public void testBallHitsGadgetThisTimeStepTrue() {
        Gadget testFlipper = new Flipper(2, 3, 0, false, true); 
        Ball testBall = new Ball(new Vect(1.5,4), new Vect(2,2));
        assertTrue(testFlipper.ballHitsGadgetThisTimestep(testBall, 10000) < Double.POSITIVE_INFINITY);
    }

    @Test 
    public void testBallHitsGadgetThisTimeStepFalse() {
        Gadget testFlipper = new Flipper(2, 3, 0, false, true); 
        Ball testBall = new Ball(new Vect(10,10), new Vect(10,10));
        assertTrue(testFlipper.ballHitsGadgetThisTimestep(testBall, 1000) == Double.POSITIVE_INFINITY);
    }
    
  //equals
    @Test 
    public void testEqualsReflexivity() {
        Gadget testFlipper = new Flipper(2, 3, 0, false, true); 
        assertTrue(testFlipper.equals(testFlipper));
    }
    
    @Test 
    public void testEqualsIdenticalObject() {
        Gadget testFlipper1 = new Flipper(2, 3, 0, false, true);
        Gadget testFlipper2 = new Flipper(2, 3, 0, false, true);
        assertTrue(testFlipper1.equals(testFlipper2));
    }
    
    @Test 
    public void testEqualsNotEqual() {
        Gadget testFlipper1 = new Flipper(2, 3, 0, false, true);
        Gadget testFlipper2 = new Flipper(2, 3, 0, false, false);
        assertFalse(testFlipper1.equals(testFlipper2));
    }


}
