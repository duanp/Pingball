package pingball;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.sun.javafx.binding.BidirectionalBinding;

import pingball.Gadgets.Portal;
import pingball.Messages.GoodbyePortalBall;
import pingball.Messages.HelloPortalBall;
import pingball.Messages.PingballServerMessage;

/**
 * 
 * Server for a multiplayer Pingball game. 
 * The class is mutable.
 * 
 */

public class PingballServer {

    /* Thread Safety Argument 
     * 
     * There are three primary types of threads in our PingballServer. The main thread listens 
     * for incoming socket connections. Each socket connection for each client is assigned to its own 
     * thread. Also, two other threads takes messages off two queues queue and assigns it to the appropriate socket. 
     * The incoming ball messages are put onto and taken off a thread-safe BlockingQueue. In addition, other
     * messages about client board connectivity are put onto and taken off another thread-safe BlockingQueue. 
     * The messages themselves are immutable private final Strings defined inside a PingballServerMessage ADT. 
     * Additionally, the routing logic uses concurrent maps to store origin and destination
     * addresses. Thus, routing of information by the server is accomplished in a thread-safe manner and the 
     * system is thread-safe. 
     * 
     * =============================================================================
     * 
     * Wire protocol:
     * 
     * ==== MESSAGES FOR INCOMING / OUTGOING BALLS ====
     * Message for ball leaving portal:
     * GoodbyePortalBall destinationBoard destinationPortal x y vx vy
     * 
     * Message for ball entering portal:
     * HelloPortalBall destinationPortal x y vx vy
     * 
     * Message for ball leaving wall:
     * GoodbyeWallBall boardname wallType x y vx vy
     * 
     * Message for ball entering wall:
     * HelloWallBall wallType x y vx vy
     * 
     * 
     * wallType ::= TOP|BOTTOM|LEFT|RIGHT
     * destinationBoard ::= NAME
     * destionationPortal ::= NAME
     * NAME ::= [A-Za-z_][A-Za-z_0-9]*
     * x ::= INTEGER
     * y ::= INTEGER
     * vx ::= FLOAT
     * vy ::= FLOAT
     * INTEGER ::= [0-9]+
     * FLOAT ::= -?([0-9]+.[0-9]*|.?[0-9]+)
     * 
     * 
     * ==== MESSAGES FOR CONNECTING/DISCONNECTING CLIENTS/WALLS ====
     * 
     * Messages to connect walls:
     * JoinWalls bottom NAME_bottom NAME_top
     * JoinWalls top NAME_top NAME_bottom
     * 
     * Messages to disconnect walls:
     * disconnectwalls: left NAME_left
     * disconnectwalls: right NAME_right
     * disconnectwalls: top NAME_top
     * disconnectwalls: bottom NAME_bottom
     * 
     * Messages to connect/disconnect client (from server to client)
     * Connected: ID
     * Disconnected: ID
     * 
     * Message to reset walls
     * restart ID
     * 
     * ID ::= NAME
     * NAME_left ::= NAME
     * NAME_right ::= NAME
     * NAME ::= [A-Za-z_][A-Za-z_0-9]*
     */

    public static final int PINGBALL_PORT = 4949;
    /** Default server port. */
    private static final int DEFAULT_PORT = 10987;
    /** Maximum port number as defined by ServerSocket. */
    private static final int MAXIMUM_PORT = 65535;
    private ServerSocket serverSocket;

    private final BlockingQueue<PingballServerMessage> in;
    private final BlockingQueue<PingballServerMessage> out;
    private final BlockingQueue<PingballServerMessage> disconnect;

    private final ConcurrentMap<ClientSocket, PingballServerMessage> socketMap = new ConcurrentHashMap<>(); 
    private final ConcurrentMap<String, String> bidirectionalAddressMapFromLeft = new ConcurrentHashMap<>(); 
    private final ConcurrentMap<String, String> bidirectionalAddressMapFromRight = new ConcurrentHashMap<>(); 
    private final ConcurrentMap<String, String> bidirectionalAddressMapFromTop = new ConcurrentHashMap<>(); 
    private final ConcurrentMap<String, String> bidirectionalAddressMapFromBottom = new ConcurrentHashMap<>(); 
    private final ConcurrentMap<String, String> bidirectionalAddressMapPortal = new ConcurrentHashMap<>(); 


    /**
     * Makes a PingballServer that listens for connections on port.
     * @param port Port number 
     * @param requests BlockingQueue for requests
     * @param replies BlockingQueue for replies 
     * @throws IOException
     */
    public PingballServer(int port, BlockingQueue<PingballServerMessage> requests, BlockingQueue<PingballServerMessage> replies, BlockingQueue<PingballServerMessage> disconnect) throws IOException {
        serverSocket = new ServerSocket(port);
        this.in = requests;
        this.out = replies;
        this.disconnect = disconnect; 
    }


    /**
     * Specifies linked walls and portals. 
     * @param origin String name of origin wall or portal
     * @param destination String name of destination wall or portal
     */
    public void linkHorizontally(String left, String right) {
        bidirectionalAddressMapFromLeft.put(left, right);
        bidirectionalAddressMapFromRight.put(right, left); 
    }


    /**
     * Specifies linked walls and portals in the vertical direction. 
     * @param origin String name of origin wall or portal
     * @param destination String name of destination wall or portal
     */
    public void linkVertically(String top, String bottom) {
        bidirectionalAddressMapFromTop.put(top, bottom);
        bidirectionalAddressMapFromBottom.put(bottom, top); 
    }


    /**
     * Disconnects linked walls and portals as needed 
     * @param origin String name of origin wall or portal. 
     */
    public void breakLink(String origin) {
        bidirectionalAddressMapPortal.remove(origin); 
        bidirectionalAddressMapFromBottom.remove(origin); 
        bidirectionalAddressMapFromLeft.remove(origin); 
        bidirectionalAddressMapFromRight.remove(origin); 
        bidirectionalAddressMapFromTop.remove(origin); 
    }

    /**
     * Sets the server socket to a new server socket
     * @param port - the port this server socket connects to
     * @throws IOException
     */
    public void setServerSocket(int port) throws IOException{
        serverSocket = new ServerSocket(port);
    }


    /**
     * Routes message to appropriate socket. 
     * @param message message to route to another socket. 
     */
    public void route(PingballServerMessage message) {
        try {
            String[] tokens = message.getMessage().split(" "); 
            String destinationID = ""; 

            if (tokens[0].equals("GoodbyeWallBall")) {
                if (tokens[2].equals("LEFT")) {
                    destinationID = bidirectionalAddressMapFromRight.get(message.getOriginID()); 
                } else if (tokens[2].equals("RIGHT")) {
                    destinationID = bidirectionalAddressMapFromLeft.get(message.getOriginID()); 
                } else if (tokens[2].equals("TOP")) {
                    destinationID = bidirectionalAddressMapFromBottom.get(message.getOriginID()); 
                } else if (tokens[2].equals("BOTTOM")) {
                    destinationID = bidirectionalAddressMapFromTop.get(message.getOriginID()); 
                }
            } else if (tokens[0].equals("GoodbyePortalBall")) {
                for (String destination: bidirectionalAddressMapPortal.keySet()) {
                    if (destination.equals(tokens[1])) {
                        destinationID = destination; 
                    }
                }
            } else if (tokens[0].equals("restart")) {
                String origin = message.getOriginID();
                String[] walls = {"left", "right", "top", "bottom"};
                for (String wall : walls) {
                    String restartMessage = "disconnectwalls: " + wall + " " + origin;
                    System.out.println(restartMessage);
                    PingballServerMessage restartSocketMessage = new PingballServerMessage(
                            null, null, restartMessage);
                    disconnect.put(restartSocketMessage);
                }
            }
            else {
                destinationID = null; 
            }


            message.setDesinationID(destinationID); 
            out.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 
    }


    /**
     * Run the server, listening for client connections and handling them.
     * Never returns unless an exception is thrown.
     * 
     * @throws IOException if the main server socket is broken
     *                     (IOExceptions from individual clients do *not* terminate serve()) 
     */
    public void serve() throws IOException {
        while (true) {
            // block until a client connects
            Socket socket = serverSocket.accept();

            // modified server to handle multiple client connections on their own thread
            Thread handler = new Thread(new Runnable() {
                public void run() {

                    try {
                        try {
                            System.out.println("client connected");
                            handleConnection(socket);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            socket.close();
                            System.out.println("socket closed"); 
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }); 

            handler.start();
        }
    }



    /**
     * Handle a single client connection. Returns when client disconnects.
     * 
     * @param socket socket where the client is connected
     * @throws IOException if the connection encounters an error or terminates unexpectedly
     * @throws InterruptedException 
     * 
     */
    private void handleConnection(Socket socket) throws IOException, InterruptedException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        ClientSocket newClientSocket = new ClientSocket(null, socket, in, out); 

        socketMap.put(newClientSocket, new PingballServerMessage(newClientSocket.getId(), null, null)); 

        try {
            for (String line = in.readLine(); line != null; line = in.readLine()) {

                if (line.contains("Set boardname:")) {
                    String[] tokens = line.split(" "); 
                    String boardname = tokens[2]; 
                    newClientSocket.setID(boardname); 

                    String newline = "Connected:"; 

                    for (ClientSocket socket2: socketMap.keySet()) {
                        newline += " " + socket2.getId(); 
                    }

                    bidirectionalAddressMapPortal.put(boardname, "");

                    PingballServerMessage disconnectedSocketMessage = new PingballServerMessage(null, null, newline); 
                    disconnect.put(disconnectedSocketMessage); 
                } else {
                    handleRequest(line, newClientSocket.getId());
                }
            }
        } finally {

            // put a new message on the disconnectQueue. 
            String newMessage = "Disconnected: " + newClientSocket.getId(); 
            PingballServerMessage disconnectedSocketMessage = new PingballServerMessage(null, null, newMessage); 
            disconnect.put(disconnectedSocketMessage); 

            // delete all the address mappings. 
            if (newClientSocket.getId() != null) {
                this.breakLink(newClientSocket.getId()); 
            }

            // remove the associated clientSocket from the socketMap
            socketMap.remove(newClientSocket); 

            out.close();
            in.close();
        }
    }


    /**
     * Handler for client input, performing requested operations and returning an output message.
     * 
     * @param input message from client
     * @return message to client, or null if none
     * 
     * Added synchronization for thread safety. 
     */
    private void handleRequest(String input, String correspondingSocket) {
        PingballServerMessage newClientMessage = new PingballServerMessage(correspondingSocket, null, input);
        this.route(newClientMessage);
    }


    /**
     * Start a PingballServer using the given arguments.
     * 
     * <br> Usage:
     *      PingballServer [--port PORT] 
     * 
     * <br> PORT is an optional integer in the range 0 to 65535 inclusive, specifying the port the server
     *      should be listening on for incoming connections.
     * <br> E.g. "PingballServer --port 1234" starts the server listening on port 1234.
     * 
     * @param args arguments as described
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        try {
            while ( ! arguments.isEmpty()) {
                String flag = arguments.remove();
                try {
                    if (flag.equals("--port")) {
                        port = Integer.parseInt(arguments.remove());

                        System.out.println(port); 

                        if (port < 0 || port > MAXIMUM_PORT) {
                            throw new IllegalArgumentException("port " + port + " out of range");
                        }
                    }
                } catch (NoSuchElementException nsee) {
                    throw new IllegalArgumentException("missing argument for " + flag);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("unable to parse number for " + flag);
                }
            }
        } catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
            System.err.println("usage: MinesweeperServer [--debug | --no-debug] [--port PORT] [--size SIZE_X,SIZE_Y | --file FILE]");
            return;
        }

        BlockingQueue<PingballServerMessage> in = new LinkedBlockingQueue<>(); 
        BlockingQueue<PingballServerMessage> out = new LinkedBlockingQueue<>(); 
        BlockingQueue<PingballServerMessage> disconnect = new LinkedBlockingQueue<>();
        PingballServer server = new PingballServer(port, in, out, disconnect);


        server.startThreadSendMessages(server);
        server.startThreadReadConsole(server); 
        server.startThreadDisconnectMessages(server); 

        // Use main thread for server.
        try {
            server.serve();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    } 

    /**
     * Handles horizontal joins of the given boards
     * @param NAME_left - name of the left board of the join
     * @param NAME_right - name of the right board of the join
     * @return - true if the join was successful, false otherwise
     * @throws InterruptedException
     */
    public boolean handleHorizontalBoardJoins(String NAME_left, String NAME_right ) throws InterruptedException{
        boolean nameLeftFound = false; 
        boolean nameRightFound = false; 

        for (ClientSocket socket: socketMap.keySet()) {
            if (socket.getId() != null) {
                if (socket.getId().equals(NAME_left)) {
                    nameLeftFound = true; 
                } 

                if (socket.getId().equals(NAME_right)) {
                    nameRightFound = true; 
                }
            }
        }


        if (nameLeftFound && nameRightFound) {
            //disconnectwalls
            //Find if message had prior link.
            if (bidirectionalAddressMapFromLeft.containsKey(NAME_left)) {
                bidirectionalAddressMapFromLeft.remove(NAME_left); 
                bidirectionalAddressMapFromRight.remove(NAME_right); 

                String newMessage3 = "disconnectwalls: " + "left " + NAME_left; 
                PingballServerMessage disconnectedSocketMessage3 = new PingballServerMessage(null, null, newMessage3); 
                disconnect.put(disconnectedSocketMessage3); 

                String newMessage4 = "disconnectwalls: " + "right " + NAME_right; 
                PingballServerMessage disconnectedSocketMessage4 = new PingballServerMessage(null, null, newMessage4); 
                disconnect.put(disconnectedSocketMessage4); 
            }


            linkHorizontally(NAME_left, NAME_right);
            //create a new message and put it on the disconnect Queue
            String newMessage = "JoinWalls right " +  NAME_right + " " + NAME_left; 
            PingballServerMessage disconnectedSocketMessage = new PingballServerMessage(null, null, newMessage); 
            disconnect.put(disconnectedSocketMessage); 

            String newMessage2 = "JoinWalls left " +  NAME_left + " " + NAME_right; 
            PingballServerMessage disconnectedSocketMessage2 = new PingballServerMessage(null, null, newMessage2); 
            disconnect.put(disconnectedSocketMessage2); 
            return true;

        } else {
            System.out.println("Board with entered name(s) are not connected to server.");
            return false;
        }

    }

    /**
     * Handles vertical joins of the given boards
     * @param NAME_top - name of the top board of the join
     * @param NAME_bottom - name of the bottom board of the join
     * @return - true if the join was successful, false otherwise
     * @throws InterruptedException
     */

    public boolean handleVerticalBoardJoins(String NAME_top, String NAME_bottom) throws InterruptedException{
        boolean nameTopFound = false; 
        boolean nameBottomFound = false; 

        for (ClientSocket socket: socketMap.keySet()) {

            if (socket.getId() != null) {

                if (socket.getId().equals(NAME_top)) {
                    nameTopFound = true; 
                } 

                if (socket.getId().equals(NAME_bottom)) {
                    nameBottomFound = true; 
                }
            }
        }

        if (nameTopFound && nameBottomFound) {
            if (bidirectionalAddressMapFromTop.containsKey(NAME_top)) {
                bidirectionalAddressMapFromTop.remove(NAME_top); 
                bidirectionalAddressMapFromBottom.remove(NAME_bottom); 

                String newMessage3 = "disconnectwalls: " + "top " + NAME_top; 
                PingballServerMessage disconnectedSocketMessage3 = new PingballServerMessage(null, null, newMessage3); 
                disconnect.put(disconnectedSocketMessage3); 

                String newMessage4 = "disconnectwalls: " + "bottom " + NAME_bottom; 
                PingballServerMessage disconnectedSocketMessage4 = new PingballServerMessage(null, null, newMessage4); 
                disconnect.put(disconnectedSocketMessage4); 
            }

            linkVertically(NAME_top, NAME_bottom);

            //create a new message and put it on the disconnect Queue 
            String newMessage = "JoinWalls bottom " +  NAME_bottom + " " + NAME_top; 
            PingballServerMessage disconnectedSocketMessage = new PingballServerMessage(null, null, newMessage); 
            disconnect.put(disconnectedSocketMessage); 

            String newMessage2 = "JoinWalls top " +  NAME_top + " " + NAME_bottom; 
            PingballServerMessage disconnectedSocketMessage2 = new PingballServerMessage(null, null, newMessage2); 
            disconnect.put(disconnectedSocketMessage2); 
            return true;

        } else {
            System.out.println("Board with entered name(s) are not connected to server.");
            return false;
        }

    }

    /**
     * Starts thread to send messages to clients. 
     * 
     * @param server instance of Pingball server
     */
    public void startThreadSendMessages(PingballServer server) {
        Thread sendMessages = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        PingballServerMessage messageToBeSent = out.take();

                        String destinationIDSocket = messageToBeSent.getDestinationID(); 

                        //split the string of messagetobeSent
                        String input = messageToBeSent.getMessage(); 

                        String[] tokens = input.split(" "); 

                        String sendMessageOverSocket = input; 

                        //handle Portal and Wall ball messages
                        if (tokens[0].equals("GoodbyePortalBall")) {
                            String helloPortalBallString = "HelloPortalBall " + tokens[2] + " " + tokens[3] + " " + tokens[4] + " " + tokens[5] + " " + tokens[6]; 
                            sendMessageOverSocket = helloPortalBallString; 
                        } else if (tokens[0].equals("GoodbyeWallBall")) {
                            String newWall = ""; 

                            if (tokens[2].equals("TOP")) {
                                newWall = "BOTTOM"; 
                            } else if (tokens[2].equals("BOTTOM")) {
                                newWall = "TOP";
                            } else if (tokens[2].equals("LEFT")) {
                                newWall = "RIGHT"; 
                            } else {
                                newWall = "LEFT"; 
                            }

                            String helloWallBallString = "HelloWallBall " + newWall + " " + tokens[3] + " " + tokens[4] + " " + tokens[5] + " " + tokens[6]; 
                            sendMessageOverSocket = helloWallBallString; 
                        } 


                        for (ClientSocket socket: server.socketMap.keySet()) {
                            if (socket.getId().equals(destinationIDSocket)) {
                                socket.getOutputStream().println(sendMessageOverSocket); 
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //socket.close();
                    System.out.println("Client disconnected.");
                }
            }
        });

        sendMessages.start(); // start Thread 1
    }

    /**
     * Starts thread to read from system.in to connect walls. 
     * 
     * @param server instance of Pingball server
     */
    public void startThreadReadConsole(PingballServer server) {
        Thread readConsole = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                        System.out.println("Enter user commands");
                        String input;
                        try {
                            while ((input = br.readLine()) != null) {
                                // if grammar not met, ignore input
                                String regexInput = "(\\s*)(h|v)(\\s+)([A-Za-z_][A-Za-z_0-9]*)(\\s+)([A-Za-z_][A-Za-z_0-9]*)";
                                if (!input.matches(regexInput)) {
                                    System.out.println("You messed up the message."); 

                                    continue;
                                }
                                String[] tokens = input.split(" ");
                                // parse input
                                if (tokens[0].replaceAll("\\s+","").equals("h")) {

                                    String NAME_left = tokens[1].replaceAll("\\s+","");
                                    String NAME_right = tokens[2].replaceAll("\\s+","");
                                    handleHorizontalBoardJoins(NAME_left, NAME_right );
                                }

                                else if (tokens[0].replaceAll("\\s+","").equals("v")) {
                                    String NAME_top = tokens[1].replaceAll("\\s+","");
                                    String NAME_bottom = tokens[2].replaceAll("\\s+","");
                                    handleVerticalBoardJoins(NAME_top, NAME_bottom);
                                }
                            }
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } finally {
                }
            }
        });
        readConsole.start();
    }

    /**
     * Starts thread to detect disconnects and send out disconnect messages to all sockets to update broken portal mappings.
     * 
     * @param server instance of Pingball server
     */
    public void startThreadDisconnectMessages(PingballServer server) {

        Thread disconnectMessages = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        PingballServerMessage messageToBeSent = disconnect.take(); 

                        String input = messageToBeSent.getMessage(); 

                        String sendMessageOverSocket = input; 

                        //handle connect and disconnect messages. 
                        String[] tokens = sendMessageOverSocket.split(" "); 

                        if (tokens[0].equals("Connected:")) {
                            for (ClientSocket socket: server.socketMap.keySet()) {
                                socket.getOutputStream().println(sendMessageOverSocket); 
                            }

                        } else if (tokens[0].equals("Disconnected:")) {
                            for (ClientSocket socket: server.socketMap.keySet()) {
                                socket.getOutputStream().println(sendMessageOverSocket); 
                            }

                        } else if (tokens[0].equals("disconnectwalls:")) {
                            for (ClientSocket socket: server.socketMap.keySet()) {
                                socket.getOutputStream().println(sendMessageOverSocket); 
                            }

                        } else {
                            for (ClientSocket socket: server.socketMap.keySet()) {
                                if (socket.getId().equals(tokens[3])) {
                                    socket.getOutputStream().println(sendMessageOverSocket); 
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //socket.close();
                    System.out.println("Client disconnected.");
                }
            }
        });

        disconnectMessages.start(); // start Thread 3

    }



}
