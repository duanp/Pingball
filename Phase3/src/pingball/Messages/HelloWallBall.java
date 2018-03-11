package pingball.Messages;

import physics.Vect;
import pingball.Ball;
import pingball.WallType;

/**
 * Mutable class to represent messages for balls entering walls
 * from another wall
 *
 */
public class HelloWallBall implements Messages {
    /* Abstraction function:
     * - an instance represents a single message for a ball entering through a wall
     * 
     * Rep invariant:
     * - messageString() method always returns a message that meets the grammar
     *
     * Message format:
     * "HelloWallBall" + TOP/BOTTOM/LEFT/RIGHT + x + y + vx + vy
     */

    private WallType walltype;
    private Ball ball;

    public HelloWallBall(WallType walltype, Ball ball) {
        this.walltype = walltype;
        this.ball = ball;
    }

    public HelloWallBall(String helloWallBallString) {
        String[] messageParts = helloWallBallString.split(" ");
        if (messageParts[1].equals("TOP")){
            this.walltype = WallType.TOP;
        }
        else if (messageParts[1].equals("BOTTOM")){
            this.walltype = WallType.BOTTOM;
        }
        else if (messageParts[1].equals("LEFT")){
            this.walltype = WallType.LEFT;
        }
        else if (messageParts[1].equals("RIGHT")){
            this.walltype = WallType.RIGHT;
        }
        Vect position = new Vect(Double.parseDouble(messageParts[2]),Double.parseDouble(messageParts[3]));
        Vect velocity = new Vect(Double.parseDouble(messageParts[4]),Double.parseDouble(messageParts[5]));
        this.ball = new Ball(position, velocity);
        checkRep();
    }

    @Override
    public String messageString() {
        String toSend = "HelloWallBall ";
        toSend += this.getWalltype().toString();
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

    public WallType getWalltype() {
        return walltype;
    }

    public void setWalltype(WallType walltype) {
        this.walltype = walltype;
        checkRep();
    }

    public void setBall(Ball ball) {
        this.ball = ball;
        checkRep();
    }


    // implements observational equality
    @Override
    public boolean equals(Object other){
        if (other instanceof HelloWallBall){
            HelloWallBall otherMessage = (HelloWallBall) other;
            if (this.walltype.equals(otherMessage.walltype) &&
                    this.ball.equals(otherMessage.ball)) {
                return true;
            }
        }
        return false;
    }
    
    //"HelloWallBall" + TOP/BOTTOM/LEFT/RIGHT + x + y + vx + vy
    private void checkRep() {
        String NAME = "([A-Za-z_][A-Za-z_0-9]*)";
        String FLOAT = "((-?([0-9]+.[0-9]*|.?[0-9]+))(E-|E)?([0-9]*)?)";
        String messageRegex = "(HelloWallBall) " + "(TOP|BOTTOM|LEFT|RIGHT)" + " " +
                FLOAT + " " + FLOAT + " " + FLOAT + " " + FLOAT;
        assert(this.messageString().matches(messageRegex));
    }

}
