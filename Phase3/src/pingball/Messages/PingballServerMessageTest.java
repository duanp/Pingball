package pingball.Messages;

import static org.junit.Assert.*;

import org.junit.Test;

public class PingballServerMessageTest {

    /*
     * Testing Strategy 
     * 
     * getMessage()
     * - Partitions
     *      - Message
     *          - null
     *          - empty string
     *          - nonempty string 
     * 
     * setDestinationID()
     * - Partitions
     *      - Message
     *          - null
     *          - empty string
     *          - nonempty string 
     * 
     * getOriginID()
     * - Partitions
     *      - Message
     *          - null
     *          - empty string
     *          - nonempty string 
     * 
     * getDestinationID()
     * - Partitions
     *      - Message
     *          - null
     *          - empty string
     *          - nonempty string 
     * 
     */
    
    //getMessage()
    @Test
    public void testGetMessageNull() {
        PingballServerMessage test = new PingballServerMessage(null, null, null); 
        assertEquals(test.getMessage(), null); 
    }
    
    @Test
    public void testGetMessageEmptyString() {
        PingballServerMessage test = new PingballServerMessage(null, null, ""); 
        assertEquals(test.getMessage(), ""); 
    }

    @Test
    public void testGetMessageNonemptyString() {
        PingballServerMessage test = new PingballServerMessage(null, null, "test"); 
        assertEquals(test.getMessage(), "test"); 
    }
    
    //setDestinationID() and getDestinationID()
    @Test
    public void testSetAndGetDestinationIDNull() {
        PingballServerMessage test = new PingballServerMessage(null, "testID", null); 
        test.setDesinationID(null); 
        assertEquals(test.getDestinationID(), null); 
    }
    
    @Test
    public void testSetAndGetDestinationEmptyString() {
        PingballServerMessage test = new PingballServerMessage(null, null, ""); 
        test.setDesinationID(""); 
        assertEquals(test.getDestinationID(), ""); 
    }

    @Test
    public void testSetAndGetDestinationNonemptyString() {
        PingballServerMessage test = new PingballServerMessage(null, "test", "test"); 
        test.setDesinationID("test"); 
        assertEquals(test.getDestinationID(), "test"); 
    }
    
    //setDestinationID() and getDestinationID()
    @Test
    public void testGetOriginIDNull() {
        PingballServerMessage test = new PingballServerMessage(null, "testID", null); 
        assertEquals(test.getOriginID(), null); 
    }
    
    @Test
    public void testGetOriginIDEmptyString() {
        PingballServerMessage test = new PingballServerMessage("", null, ""); 
        assertEquals(test.getOriginID(), "");
    }

    @Test
    public void testGetOriginIDNonemptyString() {
        PingballServerMessage test = new PingballServerMessage("test", "test", "test"); 
        assertEquals(test.getOriginID(), "test"); 
    }
}
