package pingball;

import static org.junit.Assert.*;

import org.junit.Test;

/*
 * Testing Strategy: 
 *          
 * 
 * Method: getDisplayString()
 * - Partitions:  otherBoard is empty string, otherBoard is less than 22 characters, otherBoard
 *                longer than 22 characters
 * 
 * Method: equals
 * - Partitions: verify reflexivity, and equivalence to an identical object,
 *               and non-equivalence to a different object
 */   

public class WallTest {
    
    //testing getDisplayString()
    @Test
    public void testGetDisplayStringEmpty() {
        Wall testWall = new Wall(WallType.TOP);
        assertEquals("......................", testWall.getDisplayString());
    }

    @Test
    public void testGetDisplayStringNormal() {
        Wall testWall = new Wall(WallType.TOP);
        testWall.makeTransparent("test");
        assertEquals(".test.................", testWall.getDisplayString());
    }
    
    @Test
    public void testGetDisplayStringTooLong() {
        Wall testWall = new Wall(WallType.TOP);
        testWall.makeTransparent("testtesttesttesttesttest");
        assertEquals(".testtesttesttesttestt", testWall.getDisplayString());
    }
    
    //testingEquals
    @Test
    public void testEqualsReflexive() {
        Wall testWall = new Wall(WallType.TOP);
        assertTrue(testWall.equals(testWall));
    }
    
    @Test
    public void testEqualsIdenticalObject() {
        Wall testWall1 = new Wall(WallType.TOP);
        Wall testWall2 = new Wall(WallType.TOP);
        assertTrue(testWall1.equals(testWall2));
    }
    
    @Test
    public void testEqualsDifferentObject() {
        Wall testWall1 = new Wall(WallType.TOP);
        Wall testWall2 = new Wall(WallType.BOTTOM);
        testWall2.makeTransparent("test");
        assertFalse(testWall1.equals(testWall2));
    }
}
