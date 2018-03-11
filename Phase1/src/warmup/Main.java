package warmup;
import physics.*;

/**
 * Documentation: see Ball and Board classes
 */
public class Main {
    
    /**
     * Emulate a 20 X 20 board with a ball moving at constant speed and bouncing
     */
    
    public static void main(String[] args) throws InterruptedException{
        int framesPerSecond = 10;
        int millisecWaitTime = 1000/framesPerSecond;
        Vect vector = new Vect(2., 1.);
        Ball ball = new Ball(vector, 2., 2.) ;
        Board board = new Board(20, 20, ball);
        //Board board = new Board (20, 21, ball);
        for (int i = 0; i < 500; i++) {
            Thread.sleep(millisecWaitTime);
            board.moveBall();
            board.drawBoard();
        }
        
    }
    
}