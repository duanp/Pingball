package pingball;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import physics.Vect;

/**
 * Testing Strategy
 * 
 * createVerticalBoardName 
 * partitions - single-letter, multiple-letter board names
 * 
 * Manual Testing Strategy for GUI
 * 
 * - Run locally for several different boards
 * - Run while connected to a server, join walls
 * - Run while connected to a server, join walls, 
 *   connect and disconnect multiple times
 * - Run locally and w/ server while testing out pausing, resuming, and restarting
 * 
 *
 */

public class PingballGUITest {


    @Test 
    public void testCreateVerticalBoardNameSingleLetter() {
            assertEquals("<html>b<br></html>", PingballGUI.createVerticalBoardName("b"));
    }
    
    @Test 
    public void testCreateVerticalBoardNameMultipleLetters() {
            assertEquals("<html>t<br>e<br>s<br>t<br></html>", PingballGUI.createVerticalBoardName("test"));
    }


}
