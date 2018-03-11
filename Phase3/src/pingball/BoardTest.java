package pingball;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import physics.Vect;
import pingball.Gadgets.CircleBumper;

public class BoardTest {

    /*
     * Testing Strategy:
     *      
     * Method: getDisplayArray()
     * - Partitions: Ensure it is filled with " "
     *      
     *      
     * Method: drawBoard() 
     * - Partitions: 
     *      - Number of gadgets in board
     *          - 0 
     *          - >= 0
     *      
     *      
     * Method: update(), using helper functions 
     * - Partitions: 
     *      - Timestep
     *          - 0 
     *          - >= 0
     */         
    
    //setter and getter methods
    
    //getDisplayArray
    @Test
    public void testGetDisplayArray() {
        Board testBoard = new Board();
        assertTrue(testBoard.getDisplayArray()[19][19] == " "); 
    }
    
    
    //drawBoard() 
    @Test 
    public void testDrawBoardEmptyBoard() {
        Board testBoard = new Board();
        assertTrue(testBoard.drawBoard().contains(" ")); 
    }
    
    @Test 
    public void testDrawBoardMultipleGadgets() {
        Board testBoard = new Board();
        testBoard.addGadget(new CircleBumper(8,8)); 
        assertTrue(testBoard.drawBoard().contains("O")); 
    }
    
    
    //update() 
    
    @Test 
    public void testUpdateSomeTimePassed() {
        Ball testBall = new Ball(new Vect(1,1), new Vect(2,2));
        Board testBoard = new Board();
        testBoard.addBall(testBall); 

        for (int i = 0; i < 1; i++) {
            try {
                Thread.sleep(1000); //gives 20 fps
            } catch(InterruptedException ie) {}
            testBoard.update(1000);
        }
     
        assertEquals(testBall.getPosition(), new Vect(3.0,15.5));
    }
   
    @Test 
    public void testUpdateNoTimePassed() {
        Ball testBall = new Ball(new Vect(1,1), new Vect(2,2));
        Board testBoard = new Board();
        testBoard.addBall(testBall); 
        testBoard.update(0);
        assertEquals(testBall.getPosition(), new Vect(1,1));
    }
    
    @Test
    public void equalsTest(){
        Board testBoard = new Board();
        Board testBoard2 = new Board("fluffy");
        assertFalse(testBoard.equals(testBoard2));
        Board test1 = BenchmarkBoards.FLIPPERS;
        Board test2 = BenchmarkBoards.FLIPPERS;
        assertTrue(test1.equals(test2));
        assertFalse(test1.equals(testBoard));
    }
    
    @Test
    public void joinTest(){
        Board board = new Board();
        board.joinWall("top", "Top");
        board.joinWall("bottom", "BOTTOM");
        board.joinWall("left", "leFT");
        board.joinWall("right", "HelloMumboJumboYouNeedToPressWumbo");
        String predicted = ".Top..................\n"
                      + "l                    H\n"
                      + "e                    e\n"
                      + "F                    l\n"
                      + "T                    l\n"
                      + ".                    o\n"
                      + ".                    M\n"
                      + ".                    u\n"
                      + ".                    m\n"
                      + ".                    b\n"
                      + ".                    o\n"
                      + ".                    J\n"
                      + ".                    u\n"
                      + ".                    m\n"
                      + ".                    b\n"
                      + ".                    o\n"
                      + ".                    Y\n"
                      + ".                    o\n"
                      + ".                    u\n"
                      + ".                    N\n"
                      + ".                    e\n"
                      + ".BOTTOM...............";
       String actual = board.drawBoard();
       assertEquals(predicted, board.drawBoard());
    }
    
}
