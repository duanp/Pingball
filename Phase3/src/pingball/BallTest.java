package pingball;

import static org.junit.Assert.*;

import org.junit.Test;

import physics.Angle;
import physics.Circle;
import physics.Vect;

public class BallTest {

    /*
     * Testing Strategy:
     * 
     * Methods: getters and setters: getCircle(), getPosition(), getRadius(), getVelocity(), setPosition(), setVelocity()
     * - Partitions: Simple verification
     *      - For setVelocity() 
     *          - Speed
     *              - -200 < speed < 200 
     *              - speed > 200 or speed < -200 
     * 
     * Method: predictPositionUpdate()
     * - Partitions: 
     *      - timestep (sec)
     *          - 0 
     *          - >0 
     *          
     *      - gravity
     *          - No gravity - Vect(0,0)
     *          - Gravity - nonzero vector 
     *          
     * 
     * 
     * Method: predictVelocityUpdate() 
     * - Partitions: 
     *      - timestep (sec)
     *          - 0 
     *          - >0 
     *          
     *      - gravity
     *          - No gravity - Vect(0,0)
     *          - Gravity - nonzero vector 
     *      
     *      
     * Method: update() 
     * - Partitions: 
     *      - timestep (sec)
     *          - 0 
     *          - >0 
     *          
     *      - gravity
     *          - No gravity - Vect(0,0)
     *          - Gravity - nonzero vector 
     *       
     */     
    
    private double default_friction1 = 0.025;
    private double default_friction2 = 0.025;
    
    
    //getters and setters 
    @Test 
    public void testGetCircle() {
        Ball testBall = new Ball(new Vect(1,1), new Vect(2, 2));
        Circle compareCircle = new Circle(1, 1, 0.25); 
        assertTrue(testBall.getCircle().equals(compareCircle)); 
    }
    
    @Test 
    public void testGetPosition() {
        Ball testBall = new Ball(new Vect(1,1), new Vect(2, 2));
        assertTrue(testBall.getPosition().equals(new Vect(1,1))); 
    }
    
    @Test 
    public void testGetRadius() {
        Ball testBall = new Ball(new Vect(1,1), new Vect(2, 2));
        assertTrue(testBall.getRadius() == 0.25); 
    }
    
    @Test 
    public void testGetVelocity() {
        Ball testBall = new Ball(new Vect(1,1), new Vect(2, 2));
        assertTrue(testBall.getVelocity().equals(new Vect(2,2))); 
    }
    
    @Test 
    public void testSetPosition() {
        Ball testBall = new Ball(new Vect(1,1), new Vect(2, 2));
        testBall.setPosition(new Vect(3,3)); 
        assertTrue(testBall.getPosition().equals(new Vect(3,3))); 
    }
    
    @Test 
    public void testSetVelocityWithinThreshold() {
        Ball testBall = new Ball(new Vect(1,1), new Vect(2, 2));
        testBall.setVelocity(new Vect(4, 4)); 
        assertTrue(testBall.getVelocity().equals(new Vect(4, 4))); 
    }
    
    @Test 
    public void testSetVelocityExceedThreshold() {
        Ball testBall = new Ball(new Vect(1,1), new Vect(2, 2));
        testBall.setVelocity(new Vect(10001, 10001)); 
        assertTrue(testBall.getVelocity().equals(new Vect(141.4213562373095, 141.4213562373095))); 
    }
    
    //predictPositionUpdate() 
    @Test
    public void testPredictPositionUpdateZeroTimestep() {
        Ball testBall = new Ball(new Vect(1,1), new Vect(2, 2));
        Vect newVect = testBall.predictPositionUpdate(0, new Vect(1,1)); 
        assertTrue(newVect.equals(new Vect(1,1))); 
    }
    
    @Test
    public void testPredictPositionUpdateNonzeroTimestepZeroGravity() {
        Ball testBall = new Ball(new Vect(1,1), new Vect(2, 0));
        Vect newVect = testBall.predictPositionUpdate(1000, new Vect(0,0));
        assertTrue(newVect.equals(new Vect(3,1))); 
    }
    
    @Test
    public void testPredictPositionUpdateNonzeroTimestepNonzeroGravity() {
        Ball testBall = new Ball(new Vect(0,1), new Vect(2, 2));
        Vect newVect = testBall.predictPositionUpdate(1, new Vect(1,0)); 
        assertTrue(newVect.equals(new Vect(0.0020005,1.002))); 
    }
    
    //predictVelocityUpdate() 
    @Test
    public void testPredictVelocityUpdateZeroTimestep() {
        Ball testBall = new Ball(new Vect(1,1), new Vect(2, 2));
        Vect newVect = testBall.predictVelocityUpdate(0, new Vect(1,1), 
                default_friction1, default_friction2);
        assertEquals(newVect.x(), 2, 0.00005); 
        assertEquals(newVect.y(), 2, 0.00005); 
    }
    
    @Test
    public void testPredictVelocityUpdateNonzeroTimestepZeroGravity() {
        Ball testBall = new Ball(new Vect(1,1), new Vect(2, 0));
        Vect newVect = testBall.predictVelocityUpdate(1000, new Vect(0,0),
                default_friction1, default_friction2);
        assertEquals(newVect.x(), 1.85, 0.00005); 
        assertEquals(newVect.y(), 0, 0.00005); 
    }
    
    @Test
    public void testPredictVelocityUpdateNonzeroTimestepNonzeroGravity() {
        Ball testBall = new Ball(new Vect(0,1), new Vect(2, 2));
        Vect newVect = testBall.predictVelocityUpdate(1000, new Vect(1,0),
                default_friction1, default_friction2); 
        assertEquals(newVect.x(), 2.654583654340201, 0.00005); 
        assertEquals(newVect.y(), 1.7697224362268005, 0.00005);
    }
    
    
    //update() 
    @Test
    public void testUpdateZeroTimestep() {
        Ball testBall = new Ball(new Vect(1,1), new Vect(2, 2));
        testBall.update(0, new Vect(1,1), default_friction1, default_friction2); 
        assertTrue(testBall.getPosition().equals(new Vect(1,1))); 
    }
    
    @Test
    public void testUpdateNonzeroTimestepZeroGravity() {
        Ball testBall = new Ball(new Vect(1,1), new Vect(2, 0));
        testBall.update(1000, new Vect(0,0), default_friction1, default_friction2);
        assertTrue(testBall.getPosition().equals(new Vect(3,1))); 
    }
    
    @Test
    public void testUpdateNonzeroTimestepNonzeroGravity() {
        Ball testBall = new Ball(new Vect(0,1), new Vect(2, 2));
        testBall.update(1, new Vect(1,0), default_friction1, default_friction2); 
        assertTrue(testBall.getPosition().equals(new Vect(0.0020005,1.002))); 
    }
    
}
