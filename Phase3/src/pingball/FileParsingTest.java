package pingball;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import physics.Vect;
import pingball.Gadgets.Absorber;
import pingball.Gadgets.CircleBumper;
import pingball.Gadgets.Flipper;
import pingball.Gadgets.Gadget;
import pingball.Gadgets.Portal;
import pingball.Gadgets.SquareBumper;
import pingball.Gadgets.TriangleBumper;

public class FileParsingTest {

    /*
     * Testing strategy
     * 
     * Method: createBoardFromFile
     * - Partitions:
     *      valid files
     *      - board declaration uses 0, 1, all optional arguments
     *      - triangleBumper has/has not orientation argument
     *      - extra whitespace
     *          at beginning of lines
     *          in between factors of declarations
     *          at end of lines
     *      - comments and empty lines before board declaration
     *      - all different gadgets
     *      - add triggers to all different gadgets
     *      - negative velocities
     *      
     *      invalid files
     *      - other declarations before board declaration
     *          (ball, gadget (also absorber), trigger)
     *      - declaration does not meet specific grammar
     *      - repeated gadget names
     *      
     *      key triggers
     *      - up and down key presses
     *      - [a-z], [0-9], space/others

     */
    
    // ==================== TESTS FOR VALID BOARD FILES =========================
    @Test public void testBoardDeclarationUsesZeroOptionalArguments() throws IOException {
        Board boardActual = FileParsing.createBoardFromFile(new File("boards/boardUsesZeroOptionalArguments.pb"));
        Board boardPredicted = new Board("testBoard");
        assertEquals(boardPredicted, boardActual);
    }
    
    @Test public void testBoardDeclarationUsesAllOptionalArguments() throws IOException {
        Board boardActual = FileParsing.createBoardFromFile(new File("boards/boardDeclarationUsesAllOptionalArguments.pb"));
        Board boardPredicted = new Board("testBoard");
        assertEquals(boardPredicted, boardActual);
    }
    
    @Test public void testTriangleBumperUsesAndUsesNotOrientationArgument() throws IOException {
        Board boardActual = FileParsing.createBoardFromFile(
                new File("boards/triangleBumperUsesAndUsesNotOrientationArgument.pb"));
        Board boardPredicted = new Board("testBoard");
        boardPredicted.addGadget(new TriangleBumper("tri1", 10, 10, 180));
        boardPredicted.addGadget(new TriangleBumper("tri2", 10, 11, 0));
        assertEquals(boardPredicted, boardActual);
    }
    
    @Test public void testExtraWhiteSpaceAtBeginningOfLines() throws IOException {
        Board boardActual = FileParsing.createBoardFromFile(
                new File("boards/extraWhiteSpaceAtBeginningOfLines.pb"));
        Board boardPredicted = new Board("testBoard");
        boardPredicted.addGadget(new CircleBumper("circleA", 10, 10));
        boardPredicted.addGadget(new CircleBumper("circleB", 10, 11));
        assertEquals(boardPredicted, boardActual);
        
    }
    
    @Test public void testExtraWhiteSpaceInBetweenFactorsOfDeclarations() throws IOException {
        Board boardActual = FileParsing.createBoardFromFile(
                new File("boards/extraWhiteSpaceInBetweenFactorsOfDeclarations.pb"));
        Board boardPredicted = new Board("testBoard");
        boardPredicted.addGadget(new CircleBumper("circleA", 10, 10));
        boardPredicted.addGadget(new CircleBumper("circleB", 10, 11));
        assertEquals(boardPredicted, boardActual);
    }
    
    @Test public void testExtraWhiteSpaceAtEndOfLines() throws IOException {
        Board boardActual = FileParsing.createBoardFromFile(
                new File("boards/extraWhiteSpaceAtEndOfLines.pb"));
        Board boardPredicted = new Board("testBoard");
        boardPredicted.addGadget(new CircleBumper("circleA", 10, 10));
        boardPredicted.addGadget(new CircleBumper("circleB", 10, 11));
        assertEquals(boardPredicted, boardActual);
    }
    
    @Test public void testCommentsAndEmptyLinesBeforeBoardDeclaration() throws IOException {
        Board boardActual = FileParsing.createBoardFromFile(
                new File("boards/commentsAndEmptyLinesBeforeBoardDeclaration.pb"));
        Board boardPredicted = new Board("testBoard");
        assertEquals(boardPredicted, boardActual);
    }
    
    @Test public void testConnectedPortal() throws IOException {
        Board boardActual = FileParsing.createBoardFromFile(
                new File("boards/connectedPortal.pb"));
        Board boardPredicted = new Board("Portals");
        boardPredicted.addGadget(new Portal("portalA1", 10, 10, "otherPortalB1",
                "otherB1"));
        boardPredicted.addGadget(new Portal("portalA2", 10, 11, "otherPortalB2",
                "otherB2"));
        assertEquals(boardPredicted, boardActual);
    }
    
    @Test public void testPortalOnItself() throws IOException {
        Board boardActual = FileParsing.createBoardFromFile(
                new File("boards/unconnectedPortal.pb"));
        Board boardPredicted = new Board("UnconnectedPortal");
        boardPredicted.addGadget(new Portal("portalA", 10, 10, "portalB", "BoardB"));
        assertEquals(boardPredicted, boardActual);
    }
    
    @Test public void testNegativeVelocities() throws IOException {
        Board boardActual = FileParsing.createBoardFromFile(
                new File("boards/negativeBallVelocities.pb"));
        Board boardPredicted = new Board("NegativeVelocity");
        boardPredicted.addBall(new Ball("BallA", new Vect(10, 10), new Vect(-5, 0)));
        assertEquals(boardPredicted, boardActual);
    }
    
    // ==================== TESTS FOR INVALID BOARD FILES =========================
    
    @Test(expected=IOException.class)
    public void testOtherDeclarationsBeforeBoard() throws IOException {
        FileParsing.createBoardFromFile(
                new File("boards/otherDeclarationsBeforeBoard.pb"));
    }
    @Test(expected=IOException.class)
    public void testDeclarationDoesNotMeetGrammar() throws IOException {
        FileParsing.createBoardFromFile(
                new File("boards/declarationDoesNotMeetGrammar.pb"));
    }
    
    @Test(expected=IOException.class)
    public void testRepeatedGadgetNames() throws IOException {
        FileParsing.createBoardFromFile(
                new File("boards/repeatedGadgetNames.pb"));
    }
    
    // =========== TESTS ENTIRE STAFF-PROVIDED ORIGINAL ABSORBER BOARD ==================
    
    @Test public void testEntireAbsorberBoard() throws IOException {
        Board boardActual = FileParsing.createBoardFromFile(new File("boards/oldAbsorber.pb"));
       
        Ball ballA = new Ball("ballA", new Vect(10.25, 15.25),
                new Vect(0.1, 0.1));
        Ball ballB = new Ball("ballB", new Vect(19.25, 3.25),
                new Vect(0.1, 0.1));
        Ball ballC = new Ball("ballC", new Vect(1.25, 5.25),
                new Vect(0.1, 0.1));
        
        TriangleBumper Tri = new TriangleBumper("Tri", 19, 0, 180);
        CircleBumper CircleA = new CircleBumper("CircleA", 1, 10);
        CircleBumper CircleB = new CircleBumper("CircleB", 2, 10);
        CircleBumper CircleC = new CircleBumper("CircleC", 3, 10);
        CircleBumper CircleD = new CircleBumper("CircleD", 4, 10);
        CircleBumper CircleE = new CircleBumper("CircleE", 5, 10);
        
        Absorber Abs = new Absorber("Abs", 0, 18, 20, 2);
        
        CircleA.addGadgetToTrigger(Abs);
        CircleB.addGadgetToTrigger(Abs);
        CircleC.addGadgetToTrigger(Abs);
        CircleD.addGadgetToTrigger(Abs);
        CircleE.addGadgetToTrigger(Abs);
        
        // add all gadgets to Board
        Board boardPredicted = new Board("oldAbsorber", 25.0);
        boardPredicted.addBall(ballA);
        boardPredicted.addBall(ballB);
        boardPredicted.addBall(ballC);
        boardPredicted.addGadget(Tri);
        boardPredicted.addGadget(CircleA);
        boardPredicted.addGadget(CircleB);
        boardPredicted.addGadget(CircleC);
        boardPredicted.addGadget(CircleD);
        boardPredicted.addGadget(CircleE);
        boardPredicted.addGadget(Abs);

        assertEquals(boardActual, boardPredicted);
    }
    
    // =========== TESTS ENTIRE STAFF-PROVIDED PHASE 1 BOARDS ==================
    
    @Test public void testPhase1AbsorberBoard() throws IOException {
        Board boardActual = FileParsing.createBoardFromFile(new File("boards/absorber.pb"));
        Board boardPredicted = BenchmarkBoards.ABSORBER;
        assertEquals(boardPredicted, boardActual);
    }

    @Test public void testPhase1FlippersBoard() throws IOException {
        Board boardActual = FileParsing.createBoardFromFile(new File("boards/flippers.pb"));
        Board boardPredicted = BenchmarkBoards.FLIPPERS;
        assertEquals(boardPredicted, boardActual);
    }
    
    @Test public void testPhase1DefaultBoard() throws IOException {
        Board boardActual = FileParsing.createBoardFromFile(new File("boards/default.pb"));
        Board boardPredicted = BenchmarkBoards.DEFAULT;
        assertEquals(boardPredicted, boardActual);
    }
    
    // =========== TESTS MORE STAFF BOARDS ==================
    
    @Test public void testMultiplayerLeft() throws IOException {
        Board boardActual = FileParsing.createBoardFromFile(new File("boards/multiplayer_left.pb"));
        
    }

    @Test public void testMultiplayerRight() throws IOException {
        Board boardActual = FileParsing.createBoardFromFile(new File("boards/multiplayer_right.pb"));
        
    }
    

    @Test public void SimpleBoard() throws IOException {
        Board boardActual = FileParsing.createBoardFromFile(new File("boards/simple_board.pb"));
        
    }

    
    // ==================== TESTS FOR triggerDeclarations =========================
    @Test public void testAddingTriggersToAllGadgets() throws IOException {
        Board boardActual = FileParsing.createBoardFromFile(new File("boards/triggers.pb"));
        
        Board boardPredicted = new Board("testTriggers");
        
        TriangleBumper Tri = new TriangleBumper("Tri", 10, 10, 180);
        CircleBumper Circle = new CircleBumper("Circle", 11, 10);
        SquareBumper Square = new SquareBumper("Square", 12, 10);
        Flipper FlipperL = new Flipper("FlipperL", 10, 15, 0, false, true);
        Flipper FlipperR = new Flipper("FlipperR", 15, 15, 0, false, false);
        Absorber Abs = new Absorber("Abs", 0, 18, 20, 2);
        Portal aPortal = new Portal("aPortal", 3, 3, "Beta", "testTriggers");
        CircleBumper last = new CircleBumper("last", 5, 5);
        
        // add triggers
        Tri.addGadgetToTrigger(Circle);
        Circle.addGadgetToTrigger(Square);
        Square.addGadgetToTrigger(FlipperL);
        FlipperL.addGadgetToTrigger(FlipperR);
        FlipperR.addGadgetToTrigger(Abs);
        Abs.addGadgetToTrigger(aPortal);
        aPortal.addGadgetToTrigger(last);
        
        // add gadgets to boardPredicted
        boardPredicted.addGadget(Tri);
        boardPredicted.addGadget(Circle);
        boardPredicted.addGadget(Square);
        boardPredicted.addGadget(FlipperL);
        boardPredicted.addGadget(FlipperR);
        boardPredicted.addGadget(Abs);
        boardPredicted.addGadget(aPortal);
        boardPredicted.addGadget(last);

        
        assertEquals(boardPredicted, boardActual);

    }      
    
}
