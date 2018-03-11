package pingball;

import static org.junit.Assert.*;
import physics.Circle;
import physics.Geometry;
import physics.Vect;
import pingball.Wall.WallType;

import org.junit.BeforeClass;
import org.junit.Test;

/*Testing Strategy:
 * -Confirm that individual wall unit is made with SingleBlock test
 * -Check that timeUntilCollision function is working properly with timeUntilCollision test
 * -Test proper behavior with corners of walls
 * 
 * phase 2 testing
 * partitions:
 * - type: test left, right, bottom, top
 * - visibility: invisible, not visible
 * - ball trasported, not transported
 * - wall: corner, edge
 * - timeUntilCollision, 0, 0<t<0.5, >0.5
 * 
 * test new methods by the above partitions:
 * - isInvisible
 * - type
 * - setInvisible
 * - transport
 * - setDraw
 * - drawGadget
 */
public class WallTest {
    static Wall wall1, wall2, wall3, wall4;
    static Wall wallTop, wallLeft, wallBottom, wallRight;

    @BeforeClass
    public static void setUp() {
        wall1 = new Wall(new Vect(0.0,0.0));
        wall2 = new Wall(new Vect(5.0,0.0));
        wall3 = new Wall(new Vect(1.0,0.0));
        wall4 = new Wall(new Vect(0.0,1.0));
        
        //note: no need to test for the corner of walls because board only uses middle wall block 
        //to determine the type of entire wall array
    	final int BOARD_WIDTH = 22;
    	final int BOARD_LENGTH = 22;
    	final int BOARD_MIDDLE = 10;
    	final double xLeftWall = -1;
    	final double xRightWall = BOARD_WIDTH -2;
    	final double yTopWall = -1;
    	final double yBottomWall = BOARD_LENGTH - 2;
    	
        
        wallTop = new Wall(new Vect(BOARD_MIDDLE, yTopWall));
        wallLeft = new Wall(new Vect(xLeftWall, BOARD_MIDDLE)); 
        wallRight = new Wall(new Vect(xRightWall, BOARD_MIDDLE)); 
        wallBottom = new Wall(new Vect(BOARD_MIDDLE, yBottomWall));
        
    }

    @Test
    public void WallTestSingleBlock() {
        setUp();
        
        assertEquals(wall1.getHeight(),1);
        assertEquals(wall1.getWidth(),1);
    }
    
    @Test
    public void WallTestTimeUntilCollision() {
        setUp();
        Ball ball = new Ball(new Vect(5,5), 0, 0, 0, "ballName");
        ball.setVelocity(new Vect(0,-1));
        assertEquals(5 * 0.75, wall2.timeUntilCollision(ball), 1e-6);
    }
    
    @Test 
    public void WallTestVelocityAfterCollision() {
    	setUp();
    	Ball ball = new Ball(new Vect(5,5), 0, 0, 0, "ballName");
    	ball.setVelocity(new Vect(0, -1));
    	ball.move(wall2.timeUntilCollision(ball));
    	wall2.performCollision(ball);
    	assertEquals(ball.getVelocity(), new Vect(0,1));
    }

    @Test
    public void WallTestTestCorner() {
        setUp();
        Ball ball = new Ball(new Vect(1,1), 0, 0, 0, "ballName");
        ball.setVelocity(new Vect(-1,-1));
        assertEquals(0.75, wall1.timeUntilCollision(ball), 1e-6);
    }
    
    //phase 2 testing
    
    @Test
    public void testWallLeftisInvisible() {
    	setUp();
    	assertEquals(wallLeft.type(), WallType.LEFT);
    	assertEquals(wallLeft.isInvisible(), false);
    	wallLeft.setInvisible(true);
    	assertEquals(wallLeft.isInvisible(), true);
    	wallLeft.setInvisible(false);
    }
    
    @Test
    public void testWallRightisInvisible() {
    	setUp();
    	assertEquals(wallRight.type(), WallType.RIGHT);
    	assertEquals(wallRight.isInvisible(), false);
    	wallRight.setInvisible(true);
    	assertEquals(wallRight.isInvisible(), true);
    	wallRight.setInvisible(false);
    }
    
    @Test
    public void testWallBottomisInvisible() {
    	setUp();
    	assertEquals(wallBottom.type(), WallType.BOTTOM);
    	assertEquals(wallBottom.isInvisible(), false);
    	wallBottom.setInvisible(true);
    	assertEquals(wallBottom.isInvisible(), true);
    	wallBottom.setInvisible(false);
    	
    }
    
    @Test
    public void testWallTopisInvisible() {
    	setUp();
    	assertEquals(wallTop.type(), WallType.TOP);
    	assertEquals(wallTop.isInvisible(), false);
    	wallTop.setInvisible(true);
    	assertEquals(wallTop.isInvisible(), true);
    	wallTop.setInvisible(false);
    }
    
    @Test 
    public void testWallDrawVisible() {
    	setUp();
    	//bottom wall
    	assertEquals(wallBottom.drawGadget(new Vect(10, 20)), '.');
    	wallBottom.setInvisible(true);
    	assertEquals(wallBottom.drawGadget(new Vect(10, 20)), ' ');
    	
    	//top wall
    	assertEquals(wallTop.drawGadget(new Vect(10, 20)), '.');
    	wallTop.setInvisible(true);
    	assertEquals(wallTop.drawGadget(new Vect(10, 20)), ' ');
    	
    	//left wall
    	assertEquals(wallLeft.drawGadget(new Vect(10, 20)), '.');
    	wallLeft.setInvisible(true);
    	assertEquals(wallLeft.drawGadget(new Vect(10, 20)), ' ');
    	
    	//right wall
    	assertEquals(wallRight.drawGadget(new Vect(10, 20)), '.');
    	wallRight.setInvisible(true);
    	assertEquals(wallRight.drawGadget(new Vect(10, 20)), ' ');
    }
    
    //collisions with ball to invisible wall tested in client
    
    @Test
    public void testTransportVisibleWall() {
    	setUp();
    	Ball ball = new Ball(new Vect(0.25,10), 1, 1, 1, "ballCloseToRight");
    	ball.setVelocity(new Vect(50, 0));
    	System.out.println(wallRight.timeUntilCollision(ball));
    	assertEquals(wallRight.isInvisible(), false);
    	assertEquals(wallRight.transport(ball), false);
    }
    
    @Test
    public void testTransportInvisibleWallTransport() {
    	setUp();
    	//time until collision == 0 (transport)
    	System.out.println("testTransportInvisibleWallTransport()");
    	System.out.println(Geometry.timeUntilCircleCollision(new Circle(1.0,0.0, 0.5), new Circle(0.25,0.0, 0.25), new Vect(5,0)));
    	Wall wall1 = new Wall(new Vect(1.0, 0.0));
    	Ball ball = new Ball(new Vect(0.25, 0.0),1.0, 1.0, 1.0, "otherBoard1");
    	ball.setVelocity(new Vect(5,0));
    	wall1.setInvisible(true);
    	assertEquals(wall1.isInvisible(), true);
    	assertEquals(wall1.transport(ball), true);
    	
    
    	
    	//0 <time until collision <0.5 (transport)
    	
    	//time until collision 2 (no transport)
    }
    

}
