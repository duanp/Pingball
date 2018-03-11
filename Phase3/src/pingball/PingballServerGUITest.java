package pingball;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

/**
 * Testing Strategy
 * 
 * isInteger 
 * 
 * partitions - string w/ no integers, string with some integers, 
 *              negative and positive integers
 *
 *
 * Manual Testing Strategy
 * 
 * - Ran the server with an invalid port number
 * - Ran the server with valid port number and had clients connect
 * - Ran w/ clients and joined boards
 * - Ran w/ clients that disconnected and reconnected and rejoined boards
 * - Ran w/ clients that paused, resumed, and restarted
 */
public class PingballServerGUITest {

    @Test 
    public void testIsIntegerNoNumbers() {
        String testString = "test";
        assertFalse(PingballServerGUI.isInteger(testString));
    }
    
    @Test 
    public void testIsIntegerSomeNumbers() {
        String testString = "t1e3st";
        assertFalse(PingballServerGUI.isInteger(testString));
    }
    
    @Test 
    public void testIsIntegerPositiveInteger() {
        String testString = "1973";
        assertTrue(PingballServerGUI.isInteger(testString));
    }
    
    @Test 
    public void testIsIntegerNegativeInteger() {
        String testString = "-1342";
        assertFalse(PingballServerGUI.isInteger(testString));
    }

}
