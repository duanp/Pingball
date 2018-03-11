package pingball.Messages;

import pingball.Ball;
import pingball.WallType;

public interface Messages {
    
    /*
     * This interface manages messages passed between server and clients about the
     * transfer of balls between clients due to transparent walls and portals. 
     * 
     * Messages account for balls leaving due to transparent walls, 
     *      balls leaving due to portals,
     *      balls arriving due to transparent walls, and
     *      balls arriving due to portals.
     *      
     * Each message includes the necessary information to reroute the ball and
     * recreate it on the other side. Each message also can be circularly turned 
     * into a string representation of itself, and then back to an instance of a 
     * message with the same properties, for in/out stream compatibility. 
     */
    
    /**
     * Creates a message to send to server about a ball leaving a board through a transparent wall.  
     * @param boardname it's leaving from
     * @param walltype it's leaving from
     * @param ball that is leaving
     * @return an instance of GoodbyeWallBall containing necessary information
     */
    public static GoodbyeWallBall goodbyeWallBall(String boardname, WallType walltype, Ball ball) {
        return new GoodbyeWallBall(boardname, walltype, ball);
    }
    
    /**
     * Creates a message to send to server about a ball leaving a board through a transparent wall
     *      from a string representation of GoodbyeWallBall.
     */
    public static GoodbyeWallBall goodbyeWallBall(String goodbyeWallBallString) {
        return new GoodbyeWallBall(goodbyeWallBallString);
    }
    
    /**
     * Creates a message to send to server about a ball leaving a board through a portal.
     * @param destinationBoard board it's going to
     * @param destinationPortal portal it's going to
     * @param ball that is leaving
     * @return an instance of GoodbyePortalBall containing necessary information
     */
    public static GoodbyePortalBall goodbyePortalBall(String destinationBoard, String destinationPortal, Ball ball){
        return new GoodbyePortalBall(destinationBoard, destinationPortal, ball);

    }
    
    /**
     * Creates a message to send to server about a ball leaving a board through a portal
     *      from a string representation of GoodbyePortalBall.
     */
    public static GoodbyePortalBall goodbyePortalBall(String goodbyePortalBallString) {
        return new GoodbyePortalBall(goodbyePortalBallString);
    }
    
    /**
     * Creates a message that the client receives from the server about a ball entering a board through a transparent wall.
     * @param walltype it is going to
     * @param ball that's arriving
     * @return an instance of HelloWallBall containing necessary information
     */
    public static HelloWallBall helloWallBall(WallType walltype, Ball ball) {
        return new HelloWallBall(walltype, ball);
    }
    
    /**
     * Creates a message that the client receives from the server about a ball entering a board through a transparent wall
     *      from a string representation of HelloWallBall.
     */
    public static HelloWallBall helloWallBall(String helloWallBallString) {
        return new HelloWallBall(helloWallBallString);
    }
    
    /**
     * Creates a message that the client receives from the server about a ball entering a board through a portal
     * @param destinationPortal portal it's going to
     * @param ball that's arriving
     * @return an instance of HelloPortalBall containing necessary information
     */
    public static HelloPortalBall helloPortalBall(String destinationPortal, Ball ball){
        return new HelloPortalBall(destinationPortal, ball);

    }
    
    /**
     * Creates a message that the client receives from the server about a ball entering a board through a portal
     *      from a string representation of HelloPortalBall.
     */
    public static HelloPortalBall helloPortalBall(String helloPortalBallString) {
        return new HelloPortalBall(helloPortalBallString);
    }
    
    /**
     * The string representation of this message in the wire protocol regex
     * @return The string representation of this message in the wire protocol regex
     */
    public String messageString();
    
    /**
     * Getter for the instance of ball
     * @return the ball associated with this message
     */
    public Ball getBall();
    

}
