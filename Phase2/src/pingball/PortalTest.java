package pingball;

import static org.junit.Assert.*;
import org.junit.Test;
import physics.Vect;


/**
 * Testing partitions for Portal
 * - position of the portal: 
 * - 
 * @author chaewonlee
 *
 */

/**
 * Testing partition (method tested + partitions)
 * - position of the portal: edge of board, in board, corner of board
 * - getServerMessage
 * - timeUntilCollision: 0, < positive infinity, infinity
 * - getWidth, getHeight
 * - transport method: output is true (ball collides with portal), false (ball away from portal)
 * 						*note:assumes that the new portal exists (other case handled by client)
 * @author chaewonlee
 *
 */

public class PortalTest {
    public String portalName1 = "portal1";
    public Vect portalPosition1 = new Vect(0,0); 
    public String otherBoard1 = "otherBoard1";
    public String otherPortal1 = "otherPortal1";
    public Portal portal1 = new Portal (portalName1, portalPosition1, otherBoard1, otherPortal1);
    
    public String portalName2 = "portal2";
    public Vect portalPosition2 = new Vect(5,5); 
    public String otherBoard2 = "otherBoard2";
    public String otherPortal2 = "otherPortal2";
    public Portal portal2 = new Portal (portalName2, portalPosition2, otherBoard2, otherPortal2);
    
    @Test
    public void testPortalExists() {
    	
    	
    	assertEquals(portal1.getWidth(), 1);
    	assertEquals(portal1.getHeight(), 1);
    	assertEquals(portal1.getPosition(), portalPosition1);
    	assertEquals(portal1.getServerMessage(), "otherPortal1otherBoard1");
    	assertEquals(portal1.getName(), "portal1");
    	assertEquals(portal1.otherBoard(), "otherBoard1");
    	assertEquals(portal1.otherPortal(), "otherPortal1");
    	
    	assertEquals(portal2.getWidth(), 1);
    	assertEquals(portal2.getHeight(), 1);
    	assertEquals(portal2.getPosition(), portalPosition2);
    	assertEquals(portal2.getServerMessage(), "otherPortal2otherBoard2");
    	assertEquals(portal2.getName(), "portal2");
    	assertEquals(portal2.otherBoard(), "otherBoard2");
    	assertEquals(portal2.otherPortal(), "otherPortal2");
    }
    

    @Test
    public void testPortalTransportsBall() {
    	
    	//time until collision is 0
    	//System.out.println(Geometry.timeUntilCircleCollision(new Circle(1.0,0.0, 0.5), new Circle(0.25,0.0, 0.25), new Vect(5,0)));
    	Portal portal = new Portal("portal1",new Vect(1.0, 0.0), "otherBoard1", "otherPortal1");
    	Ball ball = new Ball(new Vect(0.25, 0.0),1.0, 1.0, 1.0, "otherBoard1");
    	ball.setVelocity(new Vect(5,0));
    	assertEquals(portal.transport(ball), true);

    }
    
    
    @Test
    public void testPortalNoTransportBall_timeIsPositive() {
    	//time until collision is positive (1.0)
    	Portal portal = new Portal("portal2",new Vect(6.0, 0.0), "otherBoard2", "otherPortal2");
    	Ball ball = new Ball(new Vect(0.0, 0.0),1.0, 1.0, 1.0, "otherBoard2");
    	ball.setVelocity(new Vect(5,0));
    	assertEquals(portal.transport(ball), false);
    }
    
    
    @Test
    public void testPortalNoTransportBall_timeIsInf() {
    	//time until collision is positive infinity
    	Portal portal = new Portal("portal2",new Vect(6.0, 0.0), "otherBoard2", "otherPortal2");
    	Ball ball = new Ball(new Vect(0.0, 0.0),1.0, 1.0, 1.0, "otherBoard2");
    	ball.setVelocity(new Vect(-5,0));
    	assertEquals(portal2.transport(ball), false);
    }
    
    

    
    
}
