package pingball;

import static org.junit.Assert.*;

import org.junit.Test;

import physics.Vect;

public class BallTest {
    
    @Test
    public void testDetectBallCollisionBall() {
        Ball ball1 = new Ball(new Vect(1.0, 1.0), 10, 9.75);
        Ball ball2 = new Ball(new Vect(1.0, 1.0), 10, 10.25);
        assertTrue(ball1.detectBallCollision(ball2));
        assertTrue(ball2.detectBallCollision(ball1));
    }
    
    @Test
    public void testCollideWithBall() {
        Ball ball1 = new Ball(new Vect(0.0, 1.0), 10, 10);
        Ball ball2 = new Ball(new Vect(0.0, 0.0), 10, 10.5);
        ball1.collideWithBall(ball2);
        assertEquals(ball1.getVelocity(), new Vect(0.0, 0.0));
        assertEquals(ball2.getVelocity(), new Vect(0.0, 1.0));
    }
    
    @Test
    public void testChangeCoordinateBall() {
        Ball ball = new Ball(new Vect(1.0, 1.0), 10, 10);
        ball.changeCoordinate(15, 15);
        assertTrue(ball.getX() == 15);
        assertTrue(ball.getY() == 15);
    }
    
    @Test
    public void testChangeVelocityBall() {
        Ball ball = new Ball(new Vect(1.0, 1.0), 10, 10);
        ball.changeVelocity(new Vect(0.0, 0.0));
        assertEquals(ball.getVelocity(), new Vect(0.0, 0.0));
    }

}
