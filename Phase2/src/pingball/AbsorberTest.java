package pingball;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import physics.*;

// Testing strategy for Absorber

//  1. test collidable methods between the absorber and one or more balls
//  2. test interaction between multiple gadgets with triggering
//  3. 
/**
 * Testing strategy for absorber
 * - location of ball: in absorber, released
 * - number of balls: single, multiple
 * - location of absorber
 * - time until collision
 * - draw
 * @author chaewonlee
 */

public class AbsorberTest {
	
	//Ball: Vect position, double gravity, double friction, double drag
	//Absorber: Vect position, double width, double height, List<Gadget> gadgetsToTrigger
	
    @Test public void testAbsorberAbsorbs(){

      
        Absorber absorber = new Absorber("test absorber",new Vect(5,5), 4, 3, new ArrayList<Gadget>());

        Ball ball = new Ball(new Vect(0,0), 0, 0, 0, "ball_name");
        

        absorber.performCollision(ball);
        assertTrue(ball.getPosition().x() == 8.75);
        assertTrue(ball.getPosition().y() == 7.75); //instantly shoots up
        absorber.trigger();
        assertTrue(absorber.numCapturedBalls()==0);

    }
    
    @Test public void testAbsorberBallIsNotMovedByGravity(){

        
        Absorber absorber = new Absorber("test absorber",new Vect(5,5), 5, 5, new ArrayList<Gadget>());
        Ball ball = new Ball(new Vect(0,0), 25, 10, 10,"ball_name");

        absorber.performCollision(ball);
        Vect pos0 = ball.getPosition();
        ball.setVelocity(new Vect(50, 50));
        Vect pos1 = ball.getPosition();
        assertTrue(pos0.equals(pos1));
        
    }

    
    @Test public void testAbsorberTrigger(){

       

        Ball ball = new Ball(new Vect(0, 0),0,0,0, "ball_name");

        ball.setVelocity(new Vect(15,25));
        Absorber absorber = new Absorber("test absorber",new Vect(0,19), 20, 1, new ArrayList<Gadget>());
        assertTrue(absorber.numCapturedBalls() == 0);
        absorber.performCollision(ball);
        assertTrue(absorber.numCapturedBalls() == 1);
        absorber.trigger();
        assertTrue(ball.getVelocity().equals(new Vect(0,-50)));
        assertTrue(ball.getPosition().equals(new Vect(19.75, 18.75)));
    }
    
    @Test public void testAbsorberDraw(){
        Absorber absorber = new Absorber("test absorber",new Vect(0, 15), 5, 4, new ArrayList<Gadget>());
        for (int i=0; i < 5; i++) {
        	for (int j = 15; j < 19; j++) {
        		assertTrue(absorber.drawGadget(new Vect(i,j))== '=');
        	}
        }
    }
    
    @Test public void testAbsorberMultipleBalls(){
    	
    	//shoot out all balls 
        Absorber absorber = new Absorber("test absorber", new Vect(0, 15), 5, 4, new ArrayList<Gadget>());
        

       

        Ball b1 = new Ball(new Vect(1,0), 0,0,0, "ball_name1");
        Ball b2 = new Ball(new Vect(2,0), 0,0,0, "ball_name2");
        Ball b3 = new Ball(new Vect(3,0), 0,0,0, "ball_name3");

        
        b1.setVelocity(new Vect(0,10));
        b2.setVelocity(new Vect(0,10));
        b3.setVelocity(new Vect(0,10));
        
        double time = absorber.timeUntilCollision(b1);
        absorber.performCollision(b1);
        absorber.performCollision(b2);
        absorber.performCollision(b3);
        
        absorber.trigger();
        absorber.trigger();
        absorber.trigger();
        assertTrue(absorber.numCapturedBalls()==0);

    }
    
    @Test public void testAbsorberTimeUntilCollision(){
    	Absorber absorber = new Absorber("test absorber",new Vect(0, 15), 5, 4, new ArrayList<Gadget>());
        Ball b1 = new Ball(new Vect(1,0), 0,0,0, "ball_name1");
        b1.setVelocity(new Vect(0,10));
        System.out.println(absorber.timeUntilCollision(b1));
        assertTrue(absorber.timeUntilCollision(b1) == 1.475);
    }
   
    
    

}


