package pingball;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import physics.Vect;

public class BallTest {
	
// Testing strategy for Balls
//  Test proper position and movement of balls over time given different positions in the first quadrant, velocities, and gravity, friction, 
//	and drag values
//  Test proper change of velocity with collisions
//	Test equality of balls given same/different positions and same/different velocity
//  Test getCircle method
//
//	partitions:
//		initial velocity: 0, negative, positive
//		gravity: no gravity, small gravity, large gravity
//  	friction: no friction, small friction, large friction
//  	drag: no drag, small drag, large drag
//		COLLISIONS:
//			no collision
//			head on collision
//			back collision
	
	@Test public void positionAndVelocityTest(){
		Vect pos = new Vect(0,0);

		
		Ball ball = new Ball(pos, 0, 0, 0,"ball_name");

		assertTrue(ball.getPosition().equals(pos));
		ball.move(10);
		assertTrue(ball.getPosition().equals(pos));	
		ball.setVelocity(new Vect(1,0));
		ball.move(1);
		assertTrue(ball.getPosition().equals(new Vect(1,0)));
		ball.move(1);
		assertTrue(ball.getPosition().equals(new Vect(2,0)));
		ball.setPosition(new Vect(-1,-1));
		assertTrue(ball.getPosition().equals(new Vect(-1,-1)));
		ball.setVelocity(new Vect(-1,-1));	
		ball.move(1);
		assertTrue(ball.getPosition().equals(new Vect(-2,-2)));
	}
	@Test public void gravityEffects(){
		Vect pos = new Vect(0,0);

	

		Ball ball = new Ball(pos, 10, 0, 0, "ball_name");

		ball.move(1);
		assertTrue(ball.getVelocity().equals(new Vect(0,10)));
		ball.move(1);
		assertTrue(ball.getVelocity().equals(new Vect(0,20)));
	}
	@Test public void ballBallEquals(){
		Vect pos = new Vect(0,0);

	

		Ball ball = new Ball(pos, 0, 0, 0, "ball_name");
		Ball ball2 = new Ball(pos, 0, 0, 0, "ball_name2");
		assertTrue(ball.equals(ball2));
		ball2.setVelocity(new Vect(0,1));
		assertTrue(!ball.equals(ball2));
		Ball ball3 = new Ball(new Vect(0,1), 0, 0, 0, "ball_name3");

		assertTrue(!ball.equals(ball3));
	}
}
