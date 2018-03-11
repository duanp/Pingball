package pingball.Messages;

import physics.Vect;
import pingball.Ball;

/**
 * Mutable class to represent messages for balls entering portals
 * from another portal
 *
 */
public class HelloPortalBall implements Messages{
    
    /* Abstraction function:
     * - an instance represents a single message for a ball entering a portal
     * 
     * Rep invariant:
     * - messageString() method always returns a message that meets the grammar
     *
     * Message format:
     * "HelloPortalBall" + destinationPortal + x + y + vx + vy
     */

    private String destinationPortal;
    private Ball ball;

    public HelloPortalBall(String destinationPortal, Ball ball) {
        this.destinationPortal = destinationPortal;
        this.ball = ball;
    }

    public HelloPortalBall(String helloPortalBallString) {
        String[] messageParts = helloPortalBallString.split(" ");
        this.destinationPortal = messageParts[1];
        Vect position = new Vect(Double.parseDouble(messageParts[2]),Double.parseDouble(messageParts[3]));
        Vect velocity = new Vect(Double.parseDouble(messageParts[4]),Double.parseDouble(messageParts[5]));
        this.ball = new Ball(position, velocity);
    }

    @Override
    public String messageString() {
        String toSend = "HelloPortalBall ";
        toSend += this.getDestinationPortal();
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

    public String getDestinationPortal() {
        return destinationPortal;
    }

    public void setDestinationPortal(String destinationPortal) {
        this.destinationPortal = destinationPortal;
        checkRep();
    }

    public void setBall(Ball ball) {
        this.ball = ball;
        checkRep();
    }

    // implements observational equality
    @Override
    public boolean equals(Object other){
        if (other instanceof HelloPortalBall){
            HelloPortalBall otherMessage = (HelloPortalBall) other;
            if (this.destinationPortal.equals(otherMessage.destinationPortal) &&
                    this.ball.equals(otherMessage.ball)) {
                return true;
            }
        }
        return false;
    }
    
    //"HelloPortalBall" + destinationPortal + x + y + vx + vy
    private void checkRep() {
        String NAME = "([A-Za-z_][A-Za-z_0-9]*)";
        String FLOAT = "((-?([0-9]+.[0-9]*|.?[0-9]+))(E-|E)?([0-9]*)?)";
        String messageRegex = "(HelloPortalBall) " + NAME + " " + 
                FLOAT + " " + FLOAT + " " + FLOAT  + " " + FLOAT;
        assert(this.messageString().matches(messageRegex));
    }

}
