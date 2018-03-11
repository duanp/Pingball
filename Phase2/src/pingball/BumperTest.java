package pingball;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import physics.Vect;

// Testing strategy for Bumpers (Triange, Square, Circle)

//  1. test collidable methods between the absorber and one or more balls
//  2. test interaction between multiple gadgets with triggering
//  3. special considerations per absorber
//  4. timeUntilCollision: time before, at the time of, and after collision

/**
 * Testing strategy for Bumpers (Triangle, Square, Circle)
 * partitions:
 * - collision at middle of edge, corner of bumper | different sides of bumper
 * - location of bumper: 
 * - draw method
 * 
 * @author chaewonlee
 *
 */

public class BumperTest {
	

    // ///\\\ TriangleBumper ///

	//additional partition: orientation 0, 90, 180, 270
	
	
	
    @Test public void testTriangleCollisionMiddleOfEdge(){
    	TriangleBumper triangle  = new TriangleBumper("name",new Vect(6,0),0, new ArrayList<Gadget>());
    	Ball ball = new Ball(new Vect(0.5, 0.5), 0, 0, 0, "ball_name");
    	ball.setVelocity(new Vect(50,0));
    	assertTrue(triangle.timeUntilCollision(ball) == 0.105);
    	ball.move(triangle.timeUntilCollision(ball));
    	assertTrue(triangle.timeUntilCollision(ball) == 0);
    	triangle.performCollision(ball);
    	assertTrue(ball.getVelocity().equals(new Vect(-50, 0)));
    	assertTrue(triangle.timeUntilCollision(ball) == Double.POSITIVE_INFINITY);	
    }
    
    @Test public void testTriangleCollisionOnCorner(){
    	TriangleBumper triangle = new TriangleBumper("name",new Vect(6,0), 270, new ArrayList<Gadget>());
    	Ball ball = new Ball(new Vect(0,0), 0, 0, 0, "ball_name");
    	ball.setVelocity(new Vect(10,0));
    	assertTrue(triangle.timeUntilCollision(ball) == 0.575);
    	ball.move(triangle.timeUntilCollision(ball));
    	assertTrue(triangle.timeUntilCollision(ball) == 0);
    	triangle.performCollision(ball);
    	assertTrue(triangle.timeUntilCollision(ball) == Double.POSITIVE_INFINITY);
    }
    
    @Test public void testTriangleDraw180(){
        TriangleBumper triangle = new TriangleBumper("name",new Vect(7, 8), 180, new ArrayList<Gadget>());
        for (int i = 0; i < 10; i++) {
        	for (int j = 0; j< 10; j++) {
        		if (i==7 && j==8) {
        			assertTrue(triangle.drawGadget(new Vect(i, j)) == '/');
        		}
        		 //way to test other spaced: TODO 
        	}
        }
    }
    
    @Test public void testTriangleDraw0(){
        TriangleBumper triangle = new TriangleBumper("name",new Vect(7, 8), 0, new ArrayList<Gadget>());
        for (int i = 0; i < 10; i++) {
        	for (int j = 0; j< 10; j++) {
        		if (i==7 && j==8) {
        			assertTrue(triangle.drawGadget(new Vect(i, j)) == '/');
        		}
        		 //way to test other spaced: TODO 
        	}
        }
    }
    
    @Test public void testTriangleDraw90(){
        TriangleBumper triangle = new TriangleBumper("name",new Vect(7, 8), 90, new ArrayList<Gadget>());
        for (int i = 0; i < 10; i++) {
        	for (int j = 0; j< 10; j++) {
        		if (i==7 && j==8) {
        			assertTrue(triangle.drawGadget(new Vect(i, j)) == '\\');
        		}
        		 //way to test other spaced: TODO 
        	}
        }
    }
    
    @Test public void testTriangleDraw270(){
        TriangleBumper triangle = new TriangleBumper("name",new Vect(7, 8), 270, new ArrayList<Gadget>());
        for (int i = 0; i < 10; i++) {
        	for (int j = 0; j< 10; j++) {
        		if (i==7 && j==8) {
        			assertTrue(triangle.drawGadget(new Vect(i, j)) == '\\');
        		}
        		 //way to test other spaced: TODO 
        	}
        }
       
    }
    
    
    //
    //OOO CircleBumper OOO
    //
    @Test public void testCircleCollisionNormal(){
        CircleBumper circle = new CircleBumper("name",new Vect(10,0), new ArrayList<Gadget>());
        Ball ball = new Ball(new Vect(0,0.5),0,0,0, "ball_name");
        ball.setVelocity(new Vect(10, 0));
        assertTrue(circle.timeUntilCollision(ball)==0.975);
        ball.move(circle.timeUntilCollision(ball));
        assertTrue(circle.timeUntilCollision(ball) == 0);
        circle.performCollision(ball);
        assertTrue(ball.getVelocity().equals(new Vect(-10.0, 0.0)));
        assertTrue(circle.timeUntilCollision(ball) == Double.POSITIVE_INFINITY);
        
    }
    
    @Test public void testCircleCollisionOnePoint(){
        CircleBumper circle = new CircleBumper("name",new Vect(10,3), new ArrayList<Gadget>());
        Ball ball = new Ball(new Vect(0,3),0,0,0, "ball_name");
        ball.setVelocity(new Vect(10, 0));
        //for normal, collision time 0.975
        assertTrue(circle.timeUntilCollision(ball) > 0.97);
        assertTrue(circle.timeUntilCollision(ball) < 1);
        ball.move(circle.timeUntilCollision(ball));
        assertTrue(circle.timeUntilCollision(ball) == 0);
   
        circle.performCollision(ball);
        assertTrue(ball.getVelocity().x() < 0);
        assertTrue(ball.getVelocity().y() < 0); //TODO: check behavior
        assertTrue(circle.timeUntilCollision(ball) == Double.POSITIVE_INFINITY);
    }
    
    
    @Test public void testCircleDraw(){
        CircleBumper circle = new CircleBumper("name",new Vect(10,10), new ArrayList<Gadget>());
        assertTrue( circle.drawGadget(new Vect(10,10)) == '0');
    }
    
    //
    //### SquareBumper ###
    //
    @Test public void testSquareCollisionOnEdge(){
        SquareBumper square = new SquareBumper("name",new Vect(5,5), new ArrayList<Gadget>());
        Ball ball = new Ball(new Vect(0, 5.5), 0,0,0, "ball_name");
        ball.setVelocity(new Vect(50,0));
        assertTrue(square.timeUntilCollision(ball) == 0.095);
        ball.move(square.timeUntilCollision(ball));
        assertTrue(square.timeUntilCollision(ball) == 0.0);
        square.performCollision(ball);
        assertTrue(ball.getVelocity().equals(new Vect(-50,0)));
        assertTrue(square.timeUntilCollision(ball) == Double.POSITIVE_INFINITY);
    }
    
    @Test public void testSquareCollisionOnCorner(){
        SquareBumper square = new SquareBumper("name",new Vect(5,5), new ArrayList<Gadget>());
        Ball ball = new Ball(new Vect(0, 5), 0,0,0, "ball_name");
        ball.setVelocity(new Vect(50,0));
        assertTrue(square.timeUntilCollision(ball) == 0.095); //TODO: CHECK THIS IS CORNER
        ball.move(square.timeUntilCollision(ball));
        assertTrue(square.timeUntilCollision(ball) == 0.0);
        square.performCollision(ball);
        assertTrue(ball.getVelocity().equals(new Vect(-50,0)));
        assertTrue(square.timeUntilCollision(ball) == Double.POSITIVE_INFINITY);
    }
    
    @Test public void testSquareCollisionOnCorner2(){
        SquareBumper square = new SquareBumper("name",new Vect(0,0), new ArrayList<Gadget>());
        Ball ball = new Ball(new Vect(6.5, 6.5), 0,0,0, "ball_name");
        ball.setVelocity(new Vect(-50,-50));
        assertTrue(square.timeUntilCollision(ball) > 0.105);
        assertTrue(square.timeUntilCollision(ball) < 0.110);
        ball.move(square.timeUntilCollision(ball));
        assertTrue(square.timeUntilCollision(ball) == 0);
        square.performCollision(ball);
        assertTrue(ball.getVelocity().x() - 50 < 0.00001);
        assertTrue(ball.getVelocity().y() - 50 < 0.00001);
        assertTrue(square.timeUntilCollision(ball) == Double.POSITIVE_INFINITY);
    }
    
    
    //hello
    @Test public void testSquareDraw(){
    	SquareBumper square = new SquareBumper("name",new Vect(0,0), new ArrayList<Gadget>());
    	CircleBumper circle = new CircleBumper("name",new Vect(10,10), new ArrayList<Gadget>());
    	assertTrue(square.drawGadget(new Vect(0,0)) == '#');
    	assertFalse(circle.drawGadget(new Vect(10,10)) == '#');
    }
	
}