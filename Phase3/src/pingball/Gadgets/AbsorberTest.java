package pingball.Gadgets;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import physics.Vect;
import pingball.Ball;
import pingball.Board;

import java.util.ArrayList;

public class AbsorberTest {

    /*
     * Testing Strategy: 
     *          
     * Method: drawGadgetOnBoard() 
     * - Partitions: verify "=" displayed.
     * 
     * Method: triggerGadget() 
     * - Partitions: verify ball's velocity is reset to 50 L/s and position is reset.
     * 
     * Method: collisionBallGadget()
     * - Partitions: verify ball added to ballsContained
     * 
     * 
     * Method: ballHitsGadgetThisTimestep(Ball ball, long time)
     * - Partitions:  verify that a ball hits absorber if close enough
     *                and no collision if too far
     * 
     * Method: equals
     * - Partitions: verify reflexivity, and equivalence to an identical object,
     *               and non-equivalence to a different object
     * 
     */      
    
    
    //drawGadgetOnBoard() 
    @Test 
    public void testDrawBoardAbsorber() {
        Board testBoard = new Board();
        Gadget testAbsorber = Gadget.absorber(1, 1, 1, 1);
        testBoard.addGadget(testAbsorber); 
        assertTrue(testBoard.drawBoard().contains("=")); 
    }
    
    //triggerGadget()
    @Test 
    public void testTriggerGadget() {
        Gadget testAbsorber = Gadget.absorber(1, 10, 10, 1);
        testAbsorber.addGadgetToTrigger(testAbsorber); 
        
        Ball testBall = new Ball(new Vect(1,1), new Vect(2,2));
        testAbsorber.collisionBallGadget(testBall, 100000); 
        testAbsorber.triggerGadgets(testBall); 
        
        assertTrue(testBall.getVelocity().equals(new Vect(0, -50))); 
         
    }
    
    //collisionBallGadget 
    @Test 
    public void testCollisionBallGadget() {
        Gadget testAbsorber = Gadget.absorber(1, 10, 10, 1);
        Ball testBall = new Ball(new Vect(1,1), new Vect(2,2));
        testAbsorber.collisionBallGadget(testBall, 10000); 
    }
    
    //ballHitsGadgetThisTimestep
    @Test 
    public void testBallHitsGadgetThisTimeStepTrue() {
        Gadget testAbsorber = Gadget.absorber(1, 3, 10, 1);
        Ball testBall = new Ball(new Vect(2,3.5), new Vect(2,-2));
        assertTrue(testAbsorber.ballHitsGadgetThisTimestep(testBall, 10000) < Double.POSITIVE_INFINITY);
    }
    
    @Test 
    public void testBallHitsGadgetThisTimeStepFalse() {
        Gadget testAbsorber = Gadget.absorber(1, 3, 10, 1);
        Ball testBall = new Ball(new Vect(10,10), new Vect(10,10));
        assertTrue(testAbsorber.ballHitsGadgetThisTimestep(testBall, 1000) == Double.POSITIVE_INFINITY);
    }
    
    //equals
    @Test 
    public void testEqualsReflexivity() {
        Gadget testAbsorber = Gadget.absorber(1, 3, 10, 1);
        assertTrue(testAbsorber.equals(testAbsorber));
    }
    
    @Test 
    public void testEqualsIdenticalObject() {
        Gadget testAbsorber1 = Gadget.absorber(1, 3, 10, 1);
        Gadget testAbsorber2 = Gadget.absorber(1, 3, 10, 1);
        assertTrue(testAbsorber1.equals(testAbsorber2));
    }
    
    @Test 
    public void testEqualsNotEqual() {
        Gadget testAbsorber1 = Gadget.absorber(1, 3, 10, 1);
        Gadget testAbsorber2 = Gadget.absorber(1, 3, 11, 1);
        assertFalse(testAbsorber1.equals(testAbsorber2));
    }
    

}

