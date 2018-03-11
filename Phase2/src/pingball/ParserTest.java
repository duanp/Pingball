package pingball;
import boardFileParsing.*; 
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;

import org.antlr.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.junit.Test;


//Testing strategies:
//Test that parser correctly parses all staff board plus our 3 created boards. 
//Testing partitions:
//(1) # of players
//   (a) 1
//   (b) >1
//(2) gravity, friction, and drag of board
//   (a) negative double
//   (b) 0
//   (c) very large double, e.g. MAXIMUM_DOUBLE
//(3) portals in board
//  (a) 0
//  (b) 1
//  (c) >1
//(4) # of trigger actions between objects
//  (a) 0
//  (b) 1
//  (c) >1
//(5) # of gadgets in board
//  (a) 0
//  (b) 1
//  (c) >1
//(6) type of gadget in board
//  (a) bumpers
//  (b) balls
//  (c) absorber
//  (d) portals
//  (e) flipper
//(7) optional parameters excluded in gadget declaration of board file
//  (a) name missing
//  (b) otherBoard missing for portal
//  (c) gravity, friction1, or friction2 missing for board decl.
//  (d) orientation, position, height, or width missing from gadget decl.
//(8) extraneous white space in board file
//  (a) extra whitespace within line of gadget declaration
//  (b) extra whitespace/new lines between comments 
//(9) incorrect declaration of gadget
//  (a) assigned invalid parameters
public class ParserTest {
    
    File defaultTestBoard = new File("boards/default.pb"); 
    File boardTestBoard = new File("boards/boardTest.pb"); 
    File absorberTestBoard = new File("boards/absorber.pb"); 
    File flippersTestBoard = new File("boards/flippers.pb"); 
    File multiplayerRightTestBoard = new File("boards/multiplayer_right.pb"); 
    File multiPlayerLeftTestBoard = new File("boards/multiplayer_left.pb"); 
    File simpleTestBoard = new File("boards/simple_board.pb"); 
    File multTriggerFastBallTestBoard = new File("boards/fastBallBoard.pb"); 
    File lotsOfBallsTestBoard = new File("boards/board_many_balls.pb"); 
    File manyPortalTestBoard = new File("boards/many_portal_board.pb"); 
    File triggersTestBoard = new File("boards/triggers.pb"); 
    
    //This first test just verifies we can read the first line of a file.
    @Test
    public void testBoardParsing() throws IOException{
       
        Board outputBoard = MakeInputStream.parseFile(boardTestBoard);
        String boardName = outputBoard.getName();
        String expectedBoardName = "Default";
        assertEquals(expectedBoardName,boardName); 
    }
    @Test
    public void testDefaultBoardParsing() throws IOException{
       
        Board outputBoard = MakeInputStream.parseFile(defaultTestBoard);
        String boardName = outputBoard.getName();
        String expectedBoardName = "Default";
        Double boardGravity = outputBoard.getBoardGravity();
        int numBalls = outputBoard.getBallNumber();
        Double boardDrag= outputBoard.getBoardDrag(); 
        Double boardFriction = outputBoard.getBoardFriction();
        
        int expectedNumBalls = 1;
        Double expectedGravity = 25.0;
        Double expectedBoardFriction = 0.025;
        Double expectedBoardDrag =  0.025;
        

        assertEquals(expectedNumBalls, numBalls);
        assertEquals(expectedGravity,boardGravity); 
        assertEquals(expectedBoardDrag, boardDrag);
        assertEquals(expectedBoardName,boardName); 
        assertEquals(expectedBoardFriction, boardFriction);   
        assertEquals(expectedBoardName,boardName); 
        assertEquals(expectedBoardName,boardName); 
    }
    
    @Test
    public void testAbsorberBoardParsing() throws IOException{
        
        Board outputBoard = MakeInputStream.parseFile(absorberTestBoard);
        String boardName = outputBoard.getName();
        
        String expectedBoardName = "Absorber";
        
        Double boardGravity = outputBoard.getBoardGravity();
        int numBalls = outputBoard.getBallNumber();
        Double boardDrag= outputBoard.getBoardDrag(); 
        Double boardFriction = outputBoard.getBoardFriction();
        
        int expectedNumBalls = 3;
        Double expectedGravity = 25.0;
        Double expectedBoardFriction = 0.025;
        Double expectedBoardDrag =  0.025;
        

        assertEquals(expectedNumBalls, numBalls);
        assertEquals(expectedGravity,boardGravity); 
        assertEquals(expectedBoardDrag, boardDrag);
        assertEquals(expectedBoardName,boardName); 
        assertEquals(expectedBoardFriction, boardFriction);   
        assertEquals(expectedBoardName,boardName); 
    }
    @Test
    public void testTriggersBoardParsing() throws IOException{
        
        Board outputBoard = MakeInputStream.parseFile(triggersTestBoard);
        String boardName = outputBoard.getName();
        String expectedBoardName = "Triggers";
        
        Double boardGravity = outputBoard.getBoardGravity();
        int numBalls = outputBoard.getBallNumber();
        Double boardDrag= outputBoard.getBoardDrag(); 
        Double boardFriction = outputBoard.getBoardFriction();
        
        int expectedNumBalls = 2;
        Double expectedGravity = 10.0;
        Double expectedBoardFriction = 0.025;
        Double expectedBoardDrag =  0.025;
        

        assertEquals(expectedNumBalls, numBalls);
        assertEquals(expectedGravity,boardGravity); 
        assertEquals(expectedBoardDrag, boardDrag);
        assertEquals(expectedBoardName,boardName); 
        assertEquals(expectedBoardFriction, boardFriction);   
        assertEquals(expectedBoardName,boardName); 
    }

    @Test
    public void testFlipperBoardParsing() throws IOException{
        Board outputBoard = MakeInputStream.parseFile(flippersTestBoard);
        String boardName = outputBoard.getName();
        String expectedBoardName = "Flippers";
        int numBalls = outputBoard.getBallNumber();
        Double boardDrag= outputBoard.getBoardDrag(); 
        Double boardGravity= outputBoard.getBoardGravity(); 
        Double boardFriction = outputBoard.getBoardFriction();
        
        int expectedNumBalls = 5;
        Double expectedGravity = 25.0;
        Double expectedBoardFriction = 0.025;
        Double expectedBoardDrag =  0.025;
        

        assertEquals(expectedNumBalls, numBalls);
        assertEquals(expectedGravity,boardGravity); 
        assertEquals(expectedBoardDrag, boardDrag);
        assertEquals(expectedBoardName,boardName); 
        assertEquals(expectedBoardFriction, boardFriction);   
        assertEquals(expectedBoardName,boardName); 
    }
    
    @Test
    public void testMultiPlayerRightBoardParsing() throws IOException{
        Board outputBoard = MakeInputStream.parseFile(multiplayerRightTestBoard);
        String boardName = outputBoard.getName();
        String expectedBoardName = "MultiplayerRight";
        Double boardGravity = outputBoard.getBoardGravity();
        int numBalls = outputBoard.getBallNumber();
        Double boardDrag= outputBoard.getBoardDrag(); 
        Double boardFriction = outputBoard.getBoardFriction();        
        
        Double expGravity = 20.0;
        Double expectedBoardDrag =  0.020;
        Double expectedBoardFriction = 0.020;
        int expectedNumBalls = 0;
        
        assertEquals(expGravity, boardGravity); 
        assertEquals(expectedBoardName,boardName); 
        assertEquals(expectedNumBalls, numBalls);
        assertEquals(expectedBoardDrag, boardDrag);
        assertEquals(expectedBoardFriction, boardFriction);           
    }
    @Test
    public void testMultiPlayerLeftBoardParsing()throws IOException{
        Board outputBoard = MakeInputStream.parseFile(multiPlayerLeftTestBoard);
        String boardName = outputBoard.getName();
        int numBalls = outputBoard.getBallNumber();
        Double boardDrag= outputBoard.getBoardDrag(); 
        Double boardFriction = outputBoard.getBoardFriction();
        Double boardGravity = outputBoard.getBoardGravity();
        
        String expectedBoardName = "MultiplayerLeft";
        Double expGravity = 20.0;
        Double expectedBoardDrag =  0.020;
        Double expectedBoardFriction = 0.020;
        int expectedNumBalls = 1;
        
        assertEquals(expGravity, boardGravity); 
        assertEquals(expectedBoardName,boardName); 
        assertEquals(expectedNumBalls, numBalls);
        assertEquals(expectedBoardDrag, boardDrag);
        assertEquals(expectedBoardFriction, boardFriction);     
    }
    
    @Test
    public void testSimpleBoardParsing()throws IOException{
        Board outputBoard = MakeInputStream.parseFile(simpleTestBoard);
        String boardName = outputBoard.getName();
        Double boardFriction = outputBoard.getBoardFriction();
        int numBalls = outputBoard.getBallNumber();
        Double boardDrag= outputBoard.getBoardDrag(); 
        Double boardGravity= outputBoard.getBoardGravity(); 
        
        int expectedNumBalls = 1;
        Double expectedGravity = 20.0;
        Double expectedBoardFriction = 0.025;
        Double expectedBoardDrag =  0.025;
        String expectedBoardName = "SimpleBoard";

        assertEquals(expectedNumBalls, numBalls);
        assertEquals(expectedGravity,boardGravity); 
        assertEquals(expectedBoardDrag, boardDrag);
        assertEquals(expectedBoardName,boardName); 
        assertEquals(expectedBoardFriction, boardFriction);        
    }
    
    @Test
    public void testLotsOfBallsOnBoardParsing()throws IOException{
        Board outputBoard = MakeInputStream.parseFile(lotsOfBallsTestBoard);
        String boardName = outputBoard.getName();
        Double boardFriction = outputBoard.getBoardFriction();
        int numBalls = outputBoard.getBallNumber();
        Double boardDrag= outputBoard.getBoardDrag(); 
        Double boardGravity= outputBoard.getBoardGravity(); 
        
        int expectedNumBalls = 12;
        Double expectedGravity = 20.0;
        Double expectedBoardFriction = 0.025;
        Double expectedBoardDrag =  0.025;
        String expectedBoardName = "board_lots_of_balls";
        
        assertEquals(expectedNumBalls, numBalls);
        assertEquals(expectedGravity,boardGravity); 
        assertEquals(expectedBoardDrag, boardDrag);
        assertEquals(expectedBoardName,boardName); 
        assertEquals(expectedBoardFriction, boardFriction);   
    }
    
    @Test
    public void testFastBallBoardParsing()throws IOException{
        Board outputBoard = MakeInputStream.parseFile(multTriggerFastBallTestBoard);
        String boardName = outputBoard.getName();
        Double boardFriction = outputBoard.getBoardFriction();
        int numBalls = outputBoard.getBallNumber();
        Double boardDrag= outputBoard.getBoardDrag(); 
        Double boardGravity= outputBoard.getBoardGravity(); 
        
        int expectedNumBalls = 4;
        Double expectedGravity = 10.0;
        Double expectedBoardFriction = 0.030;
        Double expectedBoardDrag =  0.030;
        String expectedBoardName = "fastBallBoard";
        
        assertEquals(expectedNumBalls, numBalls);
        assertEquals(expectedGravity,boardGravity); 
        assertEquals(expectedBoardDrag, boardDrag);
        assertEquals(expectedBoardName,boardName); 
        assertEquals(expectedBoardFriction, boardFriction); 
    }
    
    @Test
    public void testMultPortalBoardParsing()throws IOException{
        Board outputBoard = MakeInputStream.parseFile(manyPortalTestBoard);
        String boardName = outputBoard.getName();
        Double boardFriction = outputBoard.getBoardFriction();
        int numBalls = outputBoard.getBallNumber();
        Double boardDrag= outputBoard.getBoardDrag(); 
        Double boardGravity= outputBoard.getBoardGravity(); 
        
        int expectedNumBalls = 4;
        Double expectedGravity = 15.0;
        Double expectedBoardFriction = 0.025;
        Double expectedBoardDrag =  0.025;
        String expectedBoardName = "many_portal_board";
        
        assertEquals(expectedNumBalls, numBalls);
        assertEquals(expectedGravity,boardGravity); 
        assertEquals(expectedBoardDrag, boardDrag);
        assertEquals(expectedBoardName,boardName); 
        assertEquals(expectedBoardFriction, boardFriction) ;
        
    }
}