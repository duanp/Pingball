package pingball;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A wrapper class for the client sockets connecting to the server.
 * The class is mutable.
 *
 */
public class ClientSocket {
    //
    // Abstraction function:
    //      Represents a single client connected to the server,
    //      with its ID, specific socket, input & output streams.
    //
    // Rep invariant:
    //      The fields are of the correct types, and this is 
    //      statically checked.
    //
    
    private String id; 
    private final Socket socket; 
    private final BufferedReader in; 
    private final PrintWriter out; 
    
    public ClientSocket(String id, Socket socket, BufferedReader in, PrintWriter out) {
        this.id = id;
        this.socket = socket; 
        this.in = in; 
        this.out = out; 
    }
    
    /**
     * Gets the ID of this client.
     * @return the ID
     */
    public String getId() {
        return this.id;
    }
    
    /**
     * Gets the output stream for the server, so the input stream
     * for the client that is connected.
     * @return the input stream for the client
     */
    public PrintWriter getOutputStream() {
        return this.out; 
    }
    
    /**
     * Sets the ID of this client connection
     * @param id the ID to set
     */
    public void setID(String id) {
        this.id = id; 
    }
    
    /**
     * Gets the socket reference for this client.
     * @return the socket
     */
    public Socket getSocket() {
        return this.socket; 
    }
    
}
