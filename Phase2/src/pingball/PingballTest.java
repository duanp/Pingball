package pingball;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import physics.Circle;
import physics.Geometry;
import physics.Vect;
import pingball.Wall.WallType;

import org.junit.BeforeClass;
import org.junit.Test;



/**
 * Testing strategy for the Pingball class:
 * - check parsing of sample files: resultant boards have correct number of balls, gadgets
 * - 
 * 
 * 
 * @author chaewonlee
 *
 */
public class PingballTest {
    File triggersTestBoard = new File("boards/triggers.pb"); 
	@BeforeClass
	public static void setUp() throws UnknownHostException, IOException {
		
		//Pingball(String hostname, int port, Board board) 
		Board board1 = new Board("boardName");		
		Pingball pingball1 = new Pingball("hostname", 4444, board1 );
		
		Board triggersBoard = new Board("triggersTestBoard");        
        Pingball pingball2 = new Pingball("hostname", 4444, triggersBoard);
	}
	
	@Test
	public void testSampleBoard1() throws UnknownHostException, IOException {
	   

	}
	
}