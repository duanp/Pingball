package warmup;
import physics.*;

/*
 * A class representing a board of fixed size that contains a single Ball. Mutable, because the ball attribute can change.
 */
public class Board {
    // maybe make a method that takes a ball and modifies it's vector?
    private static int width;
    private static int height;
    private static final int STANDARDSIDE = 20;
    private Ball ball;
    
    public Board() {
        width = STANDARDSIDE;
        height = STANDARDSIDE;
        Ball ball = new Ball();
    }
    
    public Board(int width, int height, Ball ball) {
        this.width = width;
        this.height = height;
        this.ball = ball;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public Ball getBall() {
        return ball;
    }
    
    //Determines whether the ball collides with the floor or the ceiling
    //Returns a boolean
    public boolean ballHitFloorCeiling() {
        double ballVelocityY = this.getBall().getVector().y();
        if (this.ball.getY() <= 1) {
            return (ballVelocityY < 0);
        }
        else if (this.ball.getY() >= this.height -2) {
            return (ballVelocityY > 0);
        }
        return false;
    }
    
    //Determines whether the ball collides with the side walls
    //Returns a boolean
    public boolean ballHitSideWalls() {
        double ballVelocityX = this.getBall().getVector().x();
        if (this.ball.getX() <= 1) {
            return (ballVelocityX < 0);
        }
        else if (this.ball.getX() >= this.width -2) {
            return (ballVelocityX > 0);
        }
        return false;
    }
    
    public void drawBoard() {
        String board = "";
        int ballCurrentX = (int) this.ball.getX();
        int ballCurrentY = (int) this.ball.getY();
        for(int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++){
                if (col == ballCurrentX && row == ballCurrentY) {
                    board += "*";
                } else if (row == 0 || row == this.height - 1 ||
                        col == 0 || col == this.width - 1) {
                    board += ".";
                } else {
                    board += " ";
                }
                
            }
            board += "\n";
        }
        board += "\n";
        System.out.println(board);
        System.out.println(this.getBall().getVector());
    }
    
    public void moveBall() {
        Ball bouncingBall = this.ball;
        if (bouncingBall.getX() < this.width - 2 && bouncingBall.getX() > 1
                && bouncingBall.getY() < this.height - 2 && bouncingBall.getY() > 1) {
            bouncingBall.move();
        } else {                      
            if (this.ballHitSideWalls()) {
                bouncingBall.bounceX();
            } 
            if (this.ballHitFloorCeiling()) {
                bouncingBall.bounceY();
            }
            bouncingBall.move();
        }
    }
}
