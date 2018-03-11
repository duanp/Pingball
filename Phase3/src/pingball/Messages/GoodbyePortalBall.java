package pingball.Messages;

import physics.Vect;
import pingball.Ball;
import pingball.Gadgets.Portal;

/**
 * Mutable class to represent messages for balls leaving portals going
 * to a different portal on a different board
 *
 */
public class GoodbyePortalBall implements Messages {
    
    /* Abstraction function:
     * - an instance represents a single message for a ball leaving a portal going
     *      to another portal on a different board
     * 
     * Rep invariant:
     * - messageString() method always returns a message that meets the grammar
     *
     * Message format:
     * "GoodbyePortalBall" + destinationBoard + destinationPortal + x + y + vx + vy
     */

    private String destinationBoard;
    private String destinationPortal;
    private Ball ball;
    
    /**
     * Constructs a message from the constituent parts
     * @param destinationBoard the board the ball is going to
     * @param destinationPortal the portal the ball is going to
     * @param ball the ball that is exiting
     */
    public GoodbyePortalBall(String destinationBoard, String destinationPortal, Ball ball) {
        this.destinationBoard = destinationBoard;
        this.destinationPortal = destinationPortal;
        this.ball = ball;
    }

    /**
     * Constructs a message from a string matching the required message format.
     * The message can have no extraneous whitespace
     * @param goodbyePortalBallString the message that was received
     */
    public GoodbyePortalBall(String goodbyePortalBallString) {
        String[] messageParts = goodbyePortalBallString.split(" ");
        
        Vect position = new Vect(Double.parseDouble(messageParts[3]),Double.parseDouble(messageParts[4]));
        Vect velocity = new Vect(Double.parseDouble(messageParts[5]),Double.parseDouble(messageParts[6]));
        
        this.destinationBoard = messageParts[1];
        this.destinationPortal = messageParts[2];
        this.ball = new Ball(position, velocity);
    }

    @Override
    public String messageString() {
        String toSend = "GoodbyePortalBall ";
        toSend += this.getDestinationBoard();
        toSend += " ";
        toSend +=this.getDestinationPortal();
        toSend += " ";
        toSend +=String.valueOf(this.getBall().getPosition().x());
        toSend += " ";
        toSend +=String.valueOf(this.getBall().getPosition().y());
        toSend += " ";
        toSend +=String.valueOf(this.getBall().getVelocity().x());
        toSend += " ";
        toSend +=String.valueOf(this.getBall().getVelocity().y());
        return toSend;
    }

    @Override
    public Ball getBall() {
        return this.ball;
    }
    
    /**
     * Gets the name of the destination board
     * @return name of the destination board as a string
     */
    public String getDestinationBoard() {
        return destinationBoard;
    }

    /**
     * Sets the name of the destination board
     * @param destinationBoard a string name of the destination board
     */
    public void setDestinationBoard(String desintationBoard) {
        this.destinationBoard = desintationBoard;
        checkRep();
    }

    /**
     * Gets the name of the destination portal
     * @return name of the destination portal as a string
     */
    public String getDestinationPortal() {
        return destinationPortal;
    }

    /**
     * Sets the name of the destination portal
     * @param destinationPortal a string name of the destination portal
     */
    public void setDestinationPortal(String destinationPortal) {
        this.destinationPortal = destinationPortal;
        checkRep();
    }

    /**
     * Sets the ball of the message
     * @param the ball that is going from one portal to another
     */
    public void setBall(Ball ball) {
        this.ball = ball;
        checkRep();
    }
    
    // implements observational equality
    @Override
    public boolean equals(Object other){
        if (other instanceof GoodbyePortalBall){
            GoodbyePortalBall otherMessage = (GoodbyePortalBall) other;
            if (this.destinationBoard.equals(otherMessage.destinationBoard) &&
                    this.destinationPortal.equals(otherMessage.destinationPortal) &&
                    this.ball.equals(otherMessage.ball)) {
                return true;
            }
        }
        return false;
    }
    
    //"GoodbyePortalBall" + destinationBoard + destinationPortal + x + y + vx + vy
    private void checkRep() {
        String NAME = "([A-Za-z_][A-Za-z_0-9]*)";
        String FLOAT = "((-?([0-9]+.[0-9]*|.?[0-9]+))(E-|E)?([0-9]*)?)";
        String messageRegex = "(GoodbyePortalBall) " + NAME + " " + 
                NAME + " " + FLOAT +" " + 
                FLOAT + " " + FLOAT + " " + FLOAT;
        assert(this.messageString().matches(messageRegex));
    }

}
