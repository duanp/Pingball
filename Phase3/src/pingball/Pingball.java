package pingball;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import physics.Vect;
import pingball.Gadgets.Portal;
import pingball.Messages.GoodbyePortalBall;
import pingball.Messages.GoodbyeWallBall;
import pingball.Messages.HelloPortalBall;
import pingball.Messages.HelloWallBall;


/**
 * 
 * Client for a Pingball game, for both singleplayer and multiplayer. Can communicate with a
 * server, allowing this Pingball game to interact with other Pingball games (multiplayer).
 *
 */


/*
 * Thread Safety Argument
 * 
 * For single-user play, there are no concurrency issues from multiple users. 
 * 
 * For client-server play with multiple users, there are two primary types of threads in our Pingball client. 
 * One thread listens for incoming messages from the server regarding incoming balls and new connections
 * for portals. Another thread updates the board according to a prescribed framerate. The aforementioned incoming
 * messages are processed in a thread safe manner involving synchronization on a portalMessageLock, wallMessageLock, 
 * and connectDisconnectMessageLock. These lock for three synchronized lists.
 * Thus, the Board state is updated atomically and the client is thread safe. 
 * 
 * 
 * 
 */

public class Pingball {

    // number of times the board is updated between each print
    public final static double UPDATES_PER_PRINT = 20.0;
    // total amount of time between each drawBoard
    public final static long TIME_RESOLUTION = (long) (1000.0/UPDATES_PER_PRINT - .5);
    private static Map<String, Board> nameToBoard;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private static final Object portalMessageLock = new Object();

    private static final Object wallMessageLock = new Object();
    private static final Object connectDisconnectMessageLock = new Object();
    private static List<HelloPortalBall> incomingPortalBallMessages = new ArrayList<>(); 
    private static List<HelloWallBall> incomingWallBallMessages = new ArrayList<>();
    private static List<String> incomingConnectDisconnectMessages = Collections.synchronizedList(new ArrayList<>());

    private final BlockingQueue<String> incomingConnectionMessages = new LinkedBlockingQueue<String>();
    
    // private Board board = new Board();
    private Board board;
    private static final int DEFAULT_PORT = 10987;
    private static final int MAXIMUM_PORT = 65535; 
    
    private AtomicBoolean paused = new AtomicBoolean(false); // pause board update
    private AtomicBoolean isConnected = new AtomicBoolean(false); // connection to server
    private AtomicBoolean runningBoardUpdate = new AtomicBoolean(false);
    
    private static String currentHost;
    private static int currentPort;
    
    public Pingball() {

    }
    
    /**
     * 
     * @param inputBoard
     */
    public Pingball(Board inputBoard){
        board = inputBoard;
        isConnected.set(false);
    }
    
    /**
     * Make a Pingball client and connect it to a server running on
     * hostname at the specified port.
     * @throws IOException if can't connect
     */
    public Pingball(String hostname, int port, Board inputBoard) throws IOException {
        board = inputBoard;
        connect(hostname, port);
    }

     /**
     * Constructs a Pingball client initialized with a Board.
     * @param inputBoard the Board the client should be running with
     * @param hostname
     * @param port
     * @throws IOException
     */
    public void connect(String hostname, int port) throws IOException {
        currentHost = hostname;
        currentPort = port;
        socket = new Socket(hostname, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        isConnected.set(true);
        sendRequest("Set boardname: " + getBoard().getName());
    }
    
    /**
     * Closes the client's connection to the server.
     * This client is now "closed". Requires this is "open".
     * @throws IOException if close fails
     */
    synchronized public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
        isConnected.set(false);
    }
    
    /**
     * Sets the current board of the client.
     * @param inputBoard the board to set
     */
    public void setBoard(Board inputBoard){
        board = inputBoard;
    }
    
    /**
     * Gets the current board of the client
     * @return the current board
     */
    public Board getBoard(){
        return board;
    }
    
    /**
     * Forces board updates to pause.
     */
    public void pause() {
        paused.set(true);
    }
    
    /**
     * Lets board updates resume.
     */
    public void resume() {
        paused.set(false);
    }

    /**
     * Runs the board chosen and displays the Pingball simulation.
     * @param args can choose what board to display
     * @throws InterruptedException 
     * @throws IOException 
     */
    public static void main(String[] args) throws InterruptedException, IOException {

        nameToBoard = new HashMap<String, Board>();

        nameToBoard.put("default", BenchmarkBoards.DEFAULT);
        nameToBoard.put("flippers",  BenchmarkBoards.FLIPPERS);
        nameToBoard.put("absorber",  BenchmarkBoards.ABSORBER);
        nameToBoard.put("portal",  BenchmarkBoards.PORTAL);
        nameToBoard.put("transparent",  BenchmarkBoards.TRANSPARENT);
        nameToBoard.put("empty", new Board());
        nameToBoard.put("side",  BenchmarkBoards.SIDEWAYS);

        Board selectBoard = nameToBoard.get("default");

        String file = "";
        String host = "";
        int port = DEFAULT_PORT;

        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        try {
            while (!arguments.isEmpty()) {
                String flag = arguments.remove();
                try {
                    if (flag.equals("--host")) {
                        host = arguments.remove();
                        flag = arguments.remove();
                        if (flag.equals("--port")) {
                            port = Integer.parseInt(arguments.remove());
                            if (port < 0 || port > MAXIMUM_PORT) {
                                throw new IllegalArgumentException("port "
                                        + port + " out of range");
                            }
                        } else {
                            file = flag;
                        }
                    } else {
                        file = flag;
                    }
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException(
                            "unable to parse number for " + flag);
                }
            }

        } catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
            System.err
            .println("usage for single-machine play: Pingball [FILE]");
            System.err
            .println("usage for client-server play: Pingball [--host HOST] [--port PORT] [FILE]");
            return;
        }
        if (!file.equals("")){
            selectBoard = FileParsing.createBoardFromFile(new File("boards/" + file));
        }
        
        try{
            Optional<Board> oBoard;
            Optional<String> oHost;
            Optional<Integer> oPort;
            if (!host.equals("")){
                oHost = Optional.of(host);
                oPort = Optional.of(port);
                if (!file.equals("")){
                    oBoard = Optional.of(selectBoard);
                } else {
                    oBoard = Optional.empty();
                }
            } else {
                oHost = Optional.empty();
                oPort = Optional.empty();
                if (!file.equals("")){
                    oBoard = Optional.of(selectBoard);
                } else {
                    oBoard = Optional.empty();
                }
            }
            PingballGUI gui = new PingballGUI(oBoard, oHost, oPort);
            gui.setSize(555, 730);
            gui.setVisible(true);
        } catch (IOException e){
            System.err.println("IOException in main method of Pingball");
        }
    }


    /**
     * Handle one client connection. Returns when client disconnects.
     * @param socket socket where client is connected
     * @throws IOException if connection encounters an error
     */
    private void handleIncomingMessages(Socket socket) throws IOException {
        System.err.println("client connected");
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        try {
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                if (line != null && isConnected.get()) {
                    System.out.println("Reply = " + line);
                    //parse the input string and update board. 
                    String[] messageParts = line.split(" ");
                    if (messageParts[0].equals("HelloPortalBall")){
                        synchronized(portalMessageLock){
                            incomingPortalBallMessages.add(new HelloPortalBall(line));
                        }
                    }
                    else if (messageParts[0].equals("HelloWallBall")){
                        synchronized(wallMessageLock){
                            incomingWallBallMessages.add(new HelloWallBall(line));    
                        }        
                    }
                    else if (messageParts[0].equals("JoinWalls") || 
                            messageParts[0].equals("Connected:") || 
                            messageParts[0].equals("Disconnected:") || 
                            messageParts[0].equals("disconnectwalls:")) {  
                        incomingConnectionMessages.put(line);
                        synchronized(connectDisconnectMessageLock){
                            System.out.println(messageParts[0]);
                            incomingConnectDisconnectMessages.add(line);    
                        }        
                    } else {
                        throw new RuntimeException("Bad incoming message");
                    }
                }
            }
        } catch (SocketException se) {
            // mute socket exception
            return;
        } catch (InterruptedException e) {
            // mute exception
            return;
        } finally {
            out.close();
            in.close();
        }
    }


    /**
     * Send a request to the server. Requires this is "open".
     * @param x message about ball leaving
     * @throws IOException if network or server failure
     */
    public synchronized void sendRequest(String message) throws IOException {
        if (out != null) {
            out.println(message);
        }
    }


    /**
     * Get a reply from the next request that was submitted.
     * i.e. about an incoming ball
     * Requires this is "open".
     * @return square of requested number
     * @throws IOException if network or server failure
     */
    public String getReply() throws IOException {
        String reply = in.readLine();
        return reply;
    }



    /**
     * Disconnects all shared walls between this client and other clients.
     * Maintains connection with server.
     */
    public void restart() {
        String request = "restart " + board.getName();
        try {
            sendRequest("Set boardname: " + getBoard().getName());
            sendRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Makes thread one, which receives messages about incoming balls and updates boards as necessary
     */
    //    private void startThreadListenMessages(Pingball client) {
    public void startThreadListenMessages(Pingball client) {
        //Thread 1: for message receiving about incoming balls; updates board as necessary. 
        Thread listenMessages = new Thread(new Runnable() {
            public void run() {
                try {
                    try {
                        System.out.println("thread listenMessages accessed"); 
                        client.handleIncomingMessages(client.socket);
                    } finally {
                        System.out.println("Client disconnected.");
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
        // start Thread 1
        listenMessages.start();
    }

    /**
     * Makes another second thread for each client that periodically updates the board state for that client's Pingball
     * @param client
     */
    public void startThreadBoardUpdate(Pingball client) {
        //Thread 2: for periodically updating the board state during a simulation of Pingball
        Thread boardUpdate = new Thread(new Runnable() {
            public void run() {
                try {
                    if (runningBoardUpdate.get()) {
                        return;
                    }
                    System.out.println("thread boardUpdate accessed");
                    runningBoardUpdate.set(true);
                    while (true) {
                        // if paused, don't update
                        if (paused.get()) {
                            continue;
                        }
                        for (int j = 0; j < 20; j++) {
                            Thread.sleep(TIME_RESOLUTION / 20);

                            if (isConnected.get()) {
                                handleMessagesInBoardUpdate();
                            }
                            board.update(TIME_RESOLUTION / 20);

                            for (GoodbyePortalBall message : board
                                    .getGoodbyePortalBallMessages()) {
                                // returns a list of balls that need to be ejected,
                                // with their velocities and positions and such;
                                client.sendRequest(message.messageString());    
                            }
                            board.emptyGoodbyePortalBallMessages();

                            for (GoodbyeWallBall message : board
                                    .getGoodbyeWallBallMessages()) {
                                // returns a list of balls that need to be ejected,
                                // with their velocities and positions and such;
                                client.sendRequest(message.messageString());
                            }
                            board.emptyGoodbyeWallBallMessages();
                        }
                        //System.out.println(board.drawBoard());
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }

        });
        // start Thread 2
        boardUpdate.start(); 
    }
    
    private void handleMessagesInBoardUpdate() {
        //get balls to add to board
        synchronized(portalMessageLock){
            if (incomingPortalBallMessages.size() > 0){
                for (HelloPortalBall message : incomingPortalBallMessages){
                    Ball ball = message.getBall();
                    ball.setImmunue(true); 
                    Portal portal = board.getPortalByName(message.getDestinationPortal());
                    ball.setPosition(new Vect(portal.getX(), portal.getY()));
                    board.addBall(ball);
                }
                incomingPortalBallMessages.clear();
            }
        }
        synchronized(wallMessageLock){
            for (HelloWallBall message : incomingWallBallMessages){
                Ball ball = message.getBall();
                if (message.getWalltype().equals(WallType.TOP)){
                    ball.setPosition(new Vect(ball.getPosition().x(),0.25005));
                }
                else if (message.getWalltype().equals(WallType.BOTTOM)){
                    ball.setPosition(new Vect(ball.getPosition().x(),19.7495));
                }
                else if (message.getWalltype().equals(WallType.LEFT)){
                    ball.setPosition(new Vect(0.25005,ball.getPosition().y()));
                }
                else if (message.getWalltype().equals(WallType.RIGHT)){
                    ball.setPosition(new Vect(19.7495,ball.getPosition().y()));
                }
                else{
                    throw new RuntimeException("Um i guess the enum is messed up??");
                }
                board.addBall(ball);
            }
            incomingWallBallMessages.clear();
        }
        synchronized(connectDisconnectMessageLock){
            while (!incomingConnectDisconnectMessages.isEmpty()) {
                String message = incomingConnectDisconnectMessages.get(0);
                incomingConnectDisconnectMessages.remove(0);

                String[] messageParts = message.split(" ");
                if (messageParts[0].equals("JoinWalls")){
                    board.joinWall(messageParts[1],messageParts[2]); //JoinWalls top/bottom/left/right otherBoardName thisBoardName(ignored)                                         
                }

                else if (messageParts[0].equals("Connected:")){ // tells the client that another board has connected; portals must react                                
                    // message format: "Connected: board1 board2 board3 ...."
                    board.connectBoardsToPortals(Arrays.copyOfRange(messageParts, 1, messageParts.length));
                }
                else if (messageParts[0].equals("Disconnected:")){// tells the client that another board has disconnected; portals and walls must react
                    // message format: "Disconnected: board"
                    board.disconnectSingleBoardFromPortals(messageParts[1]);
                    board.removeConnectedBoard(messageParts[1]);
                }
                else if (messageParts[0].equals("disconnectwalls:")){
                    // tells the client to disconnect a board from one of its sides
                    // message format: "disconnectwalls: left/right/bottom/top board"
                    board.removeConnectedBoard(messageParts[2]);
                }

            }
        }
    }
    
    public void createGUI(){
        
    }
}
