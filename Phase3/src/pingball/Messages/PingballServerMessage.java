package pingball.Messages;

/**
 * 
 * A message for internal Pingball server use. 
 * 
 * Abstraction Function: Represents a Pingball message containing source ID, destination ID, and a message. 
 * Rep Invariant: sourceID, message is immutable, *ensured through use of private final Strings. 
 *
 */

public class PingballServerMessage {
    
    private final String sourceSocketID;
    private String destinationSocketID; 
    private final String message; 
    
    /**
     * Creates a new PingballServerMessage
     * 
     * @param sourceID of socket origin
     * @param destID of socket destination
     * @param message String message
     */
    public PingballServerMessage(String sourceID, String destID, String message) {
        this.sourceSocketID = sourceID; 
        this.destinationSocketID = destID; 
        this.message = message; 
    }
    
    /**
     * Return the String message of the PingballServerMessage
     * @return message String
     */
    public String getMessage() {
        return this.message; 
    }
    
    /**
     * Specify destinationID of PingballServerMessage
     * @param destinationID of destination Socket
     */
    public void setDesinationID(String destinationID) {
        this.destinationSocketID = destinationID; 
    }
    
    /**
     * get originID of PingballServerMessage
     * @return originID of destination Socket
     */
    public String getOriginID() {
        return this.sourceSocketID; 
    }
    
    /**
     * get destinationID of PingballServerMessage
     * @return destinationID of destination Socket
     */
    public String getDestinationID() {
        return this.destinationSocketID;  
    }
    

}
