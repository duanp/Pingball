package pingball.Messages;

import pingball.WallType;
import physics.Vect;
import pingball.Ball;

/**
 * Mutable class to represent messages for balls leaving walls going
 * to a different wall on a different board
 *
 */
public class GoodbyeWallBall implements Messages{
    /* Abstraction function:
     * - an instance represents a single message for a ball leaving a wall going
     *      to another wall on a different board
     * 
     * Rep invariant:
     * - messageString() method always returns a message that meets the grammar
     *
     * Message format:
     * "GoodbyeWallBall" + boardname + TOP/BOTTOM/LEFT/RIGHT + x + y + vx + vy
     */
    
    private String boardname;
    private WallType walltype;
    private Ball ball;

    public GoodbyeWallBall(String boardname, WallType walltype, Ball ball) {
        this.boardname = boardname;
        this.walltype = walltype;
        this.ball = ball;
    }

    public GoodbyeWallBall(String goodbyeWallBallString) {
        String[] messageParts = goodbyeWallBallString.split(" ");
        this.boardname = messageParts[1];
        if (messageParts[2].equals("TOP")){
            this.walltype = WallType.TOP;
        }
        else if (messageParts[2].equals("BOTTOM")){
            this.walltype = WallType.BOTTOM;
        }
        else if (messageParts[2].equals("LEFT")){
            this.walltype = WallType.LEFT;
        }
        else if (messageParts[2].equals("RIGHT")){
            this.walltype = WallType.RIGHT;
        }
        Vect position = new Vect(Double.parseDouble(messageParts[3]),Double.parseDouble(messageParts[4]));
        Vect velocity = new Vect(Double.parseDouble(messageParts[5]),Double.parseDouble(messageParts[6]));
        this.ball = new Ball(position, velocity);
    }

    @Override
    public String messageString() {
        String toSend = "GoodbyeWallBall ";
        toSend += this.getBoardName();
        toSend += " ";
        toSend +=this.getWalltype().toString();
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
     * Sets the ball of the message
     * @param ball the ball to set
     */
    public void setBall(Ball ball) {
        this.ball = ball;
        checkRep();
    }

    /**
     * Gets the name of the destination board
     * @return name of the destination board as a string
     */
    public String getBoardName() {
        return boardname;
    }

    /**
     * Sets the name of the destination board
     * @param destinationBoard a string name of the destination board
     */
    public void setBoardName(String boardname) {
        this.boardname = boardname;
        checkRep();
    }

    /**
     * Gets the type of wall
     * @return the type of the wall
     */
    public WallType getWalltype() {
        return walltype;
    }

    /**
     * Sets the type of the wall
     * @param walltype an enum of the type of the wall
     */
    public void setWalltype(WallType walltype) {
        this.walltype = walltype;
        checkRep();
    }

    // implements observational equality
    @Override
    public boolean equals(Object other){
        if (other instanceof GoodbyeWallBall){
            GoodbyeWallBall otherMessage = (GoodbyeWallBall) other;
            if (this.boardname.equals(otherMessage.boardname) &&
                    this.walltype.equals(otherMessage.walltype) &&
                    this.ball.equals(otherMessage.ball)) {
                return true;
            }
        }
        return false;
    }
    
    //"GoodbyeWallBall" + boardname + TOP/BOTTOM/LEFT/RIGHT + x + y + vx + vy
    private void checkRep() {
        String NAME = "([A-Za-z_][A-Za-z_0-9]*)";
        String FLOAT = "((-?([0-9]+.[0-9]*|.?[0-9]+))(E-|E)?([0-9]*)?)";
        String messageRegex = "(GoodbyeWallBall) " + NAME + " " + 
                "(TOP|BOTTOM|LEFT|RIGHT)" + " " + FLOAT +" " + 
                FLOAT + " " + FLOAT + " " + FLOAT;
        assert(this.messageString().matches(messageRegex));
    }


}
