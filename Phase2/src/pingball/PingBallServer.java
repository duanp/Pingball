package pingball;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Multiplayer Pingball Server, allows multiple clients to connect
 * 
 * Thread Safety Argument:  A message queue is used by the server to send and receive messages from the
 * clients
 *
 */
public class PingBallServer {
    /** Default server port. */
    private static final int DEFAULT_PORT = 10987;
    /** Maximum port number as defined by ServerSocket. */
    private static final int MAXIMUM_PORT = 65535;
    private ServerSocket serverSocket;
    /** Maps the name of the board to a hashmap mapping each side of the board
     * to the name of the board it is connected to 
     */
    public HashMap<String, HashMap<String, String>> boardConnectionMap;

    /** Maps the name of the board to the actual board object*/
    private HashMap<String, List<String>> connectedPortalsMap;
    private final BlockingQueue<String> inQueue;
    private final BlockingQueue<String> outQueue;
    public ArrayList<BufferedReader> clientReader;
    public ArrayList<PrintWriter> clientWriter;
    public ArrayList<Socket> clientSockets;
    private HashMap<String, PrintWriter> printWriterMap;
    private HashMap<String, String> disconnectPortalsMap;
    private HashSet<String> disconnectedClients;
    



    /**
     * Make a PingballServer that listens for connections on port.
     * 
     * @param port port number, requires 0 <= port <= 65535
     * @throws IOException if an error occurs opening the server socket
     */
    public PingBallServer(int port, BlockingQueue<String> Replies, BlockingQueue<String> Requests) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.boardConnectionMap = new HashMap<String, HashMap<String,String>>();
        this.connectedPortalsMap = new HashMap<String, List<String>>();
        this.clientReader = new ArrayList<BufferedReader>();
        this.clientWriter = new ArrayList<PrintWriter>();
        this.clientSockets = new ArrayList<Socket>();
        this.outQueue = Replies;
        this.inQueue = Requests;
        this.printWriterMap = new HashMap<String, PrintWriter>();
        this.disconnectPortalsMap = new HashMap<String, String>();
        this.disconnectedClients = new HashSet<String>();
    }
    
    public void sendPortalsMap(String boardName){
        String portalMsg = "";
        for (String boardKey: connectedPortalsMap.keySet()){
            String boardPortals = boardKey + ":";
            for (String portalName: connectedPortalsMap.get(boardKey)){
                boardPortals += portalName + ":";
            }
            boardPortals = boardPortals.substring(0,boardPortals.length() - 1);
            boardPortals += ";";
            portalMsg += boardPortals;
        }
        outQueue.add(boardName + " checkOwnPortal " + portalMsg);
        
    }

    /**
     * Run the server for the given Socket, listening for client connections and handling them
     * appropiately based on command line arguments, and updates nameToBoardMap
     * Never returns unless an exception is thrown.
     * 
     * @param serverSocket - the server socket being served
     * @throws IOException if the main server socket is broken
     *                     (IOExceptions from individual clients do *not* terminate serve())
     */
    public void serve() throws IOException {


        new Thread(new Runnable() {
            public void run(){

                try {
                    while(true){
                        boolean isWall = true;
                        String clientMsg = inQueue.take();
                        String clientMsgArray[] = clientMsg.split(" ");
                        String boardName = clientMsgArray[0];
                        String gadgetName = "";
                        String ballInfo = "";
                        boolean shouldHandleRequest = false;
                        if (clientMsgArray[1].equals("portal")) {
                            isWall = false;
                            ballInfo = "";
                            for(int i = 4; i < clientMsgArray.length; i++){
                                ballInfo += clientMsgArray[i] + " ";}
                            //destination board name
                            boardName = clientMsgArray[2];
                            gadgetName = clientMsgArray[3];
                            shouldHandleRequest = true;
                        } else if (clientMsgArray[1].equals("Wall")){
                            gadgetName = clientMsgArray[2];
                            ballInfo = "";
                            for(int i = 3; i < clientMsgArray.length; i++){
                                ballInfo += clientMsgArray[i] + " ";

                            }
                            shouldHandleRequest = true;
                        } else if (clientMsgArray[1].equals("connect")){
                            boardConnectionMap.put(boardName, new HashMap<String,String>());
                            connectedPortalsMap.put(boardName, new ArrayList<String>());
                            String notifyAllClients = "";
                            for (int i = 2; i < clientMsgArray.length; i++){
                                connectedPortalsMap.get(boardName).add(clientMsgArray[i]);
                                notifyAllClients += clientMsgArray[i] + " ";
                            }
                            sendPortalsMap(boardName);
                            outQueue.add("all newBoardOpen " + boardName + " " + notifyAllClients);
                            // checks if any of its own portals should be open

                        }
                        else {
                            throw new IllegalArgumentException("improper message format");

                        }
                        if (shouldHandleRequest){
                            handleRequests(boardName, gadgetName, ballInfo, isWall);
                        }
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    /*   try {
                        socket.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }*/
                } finally {

                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run(){
                try {
                    while(true){
                        String serverMsg = outQueue.take();
                       
                        for (PrintWriter output: clientWriter){
                     //       System.out.println(serverMsg);

                            output.println(serverMsg);
                            output.flush();

                            if (output.checkError()){
                                for(String client:printWriterMap.keySet()){
                                    if (printWriterMap.get(client).equals(output) && !disconnectedClients.contains(client)){
                                        String clientMsg = disconnectPortalsMap.get(client);
                                        String msg = "all disconnectPortals " + clientMsg;
                                        System.out.println(msg);
                                        disconnectWall(client);
                                        outQueue.add(msg);
                                        System.out.println(msg);
                                        disconnectedClients.add(client);
                                        output.close();
                                    }
                                }
                               

                                
                            }
                        }
                        /*      for (Socket clientSocket: clientSockets){
                            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
                            System.out.println(output.toString() + "::::::::" + serverMsg);
                            output.println(serverMsg);
                            output.flush();
                        }*/
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    
                } finally {

                }
            }
        }).start();
        while(true){
            final Socket socket = serverSocket.accept();

            new Thread(new Runnable() {

                public void run(){
                    try {
                        handleConnection(socket);
                    } catch (IOException ioe) {
                        ioe.printStackTrace(); // but don't terminate serve()
                        /*  try {
                            System.out.println("In handle connection");
                         //   socket.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }*/
                    } finally {

                    }
                }
            }).start();
        }


    }


    public void handleRequests(String boardName, String gadget, String ballInfo, boolean isWall) throws InterruptedException{
        // handle the request
        if (isWall){
            if (this.boardConnectionMap.containsKey(boardName)){
                if (this.boardConnectionMap.get(boardName).containsKey(gadget)){
                    String destinationBoardName = this.boardConnectionMap.get(boardName).get(gadget);
                    outQueue.add(destinationBoardName + " " + gadget + " " + ballInfo);
                }


            }
        } else {
            outQueue.add(boardName + " receiveBallFromPortal " + gadget + " " + ballInfo);

        }


    }



    /**
     * Handles a single client connection. Returns when client disconnects.
     * 
     * @param socket socket where the client is connected
     * @throws IOException if the connection encounters an error or terminates unexpectedly
     * !! line should be "name message" 
     */
    public void handleConnection(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        this.clientReader.add(in);
        this.clientWriter.add(out);
        this.clientSockets.add(socket);
        String clientMsg = "";
        String clientName = "";
        try {
            
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                String[] clientMsgArray = line.split(" ");
                if (clientMsgArray[1].equals("connect")){
                    clientName = clientMsgArray[0];
                    clientMsg = clientMsgArray[0] + " ";
                    for (int i = 2; i < clientMsgArray.length; i++){
                        clientMsg += clientMsgArray[i] + " ";
                    }
                    clientMsg = clientMsg.substring(0,clientMsg.length() - 1);
                    this.printWriterMap.put(clientName, out);
                    this.disconnectPortalsMap.put(clientName, clientMsg);
                }
                this.inQueue.add(line);
            }
           
        } catch(IOException e) {
            
            
            out.close();
            in.close();
            
           
            
        }


    }
    
    private void disconnectWall(String clientName){
        
        if(boardConnectionMap.containsKey(clientName)){
            for (String wall:boardConnectionMap.get(clientName).keySet()){
                if(wall.equals("top")){
                    outQueue.add(boardConnectionMap.get(clientName).get(wall) + " disconnectWall bottom");
                   
                } else if(wall.equals("bottom")) {
                    outQueue.add(boardConnectionMap.get(clientName).get(wall) + " disconnectWall top");
                } else if(wall.equals("left")) {
                    outQueue.add(boardConnectionMap.get(clientName).get(wall) + " disconnectWall right");
                    System.out.println(boardConnectionMap.get(clientName).get(wall) + " disconnectWall right");
                } else if(wall.equals("right")) {
                    outQueue.add(boardConnectionMap.get(clientName).get(wall) + " disconnectWall left");
                    System.out.println(boardConnectionMap.get(clientName).get(wall) + " disconnectWall left");
                }
            }
        }
        
    }

    private void handleUserInput() throws IOException{
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader keyboard = new BufferedReader(in);
        try{
            boolean isVertical = true;
            String s = keyboard.readLine();
            if (!s.equals(null)) {
                String[] inputArray = s.split(" ");
                if (inputArray[0].equals("h")){
                    isVertical = false;

                } else if (!inputArray[0].equals("v")){
                    throw new IllegalArgumentException("wrong format");
                }
                //! msgs are "boardname invisible top"
                if (isVertical){
                    String topBoard = inputArray[1];    
                    String bottomBoard = inputArray[2];

                    if (boardConnectionMap.containsKey(topBoard) && boardConnectionMap.containsKey(bottomBoard)) {
                        boardConnectionMap.get(topBoard).put("bottom", bottomBoard);
                        boardConnectionMap.get(bottomBoard).put("top", topBoard);
                        outQueue.add(topBoard + " invisible bottom " + bottomBoard);
                        outQueue.add(bottomBoard + " invisible top " + topBoard);
                    }
                } else {
                    String leftBoard = inputArray[1];
                    String rightBoard = inputArray[2];
                    if (boardConnectionMap.containsKey(rightBoard) && boardConnectionMap.containsKey(leftBoard)) {
                        boardConnectionMap.get(leftBoard).put("right", rightBoard);
                        boardConnectionMap.get(rightBoard).put("left", leftBoard);
                        outQueue.add(leftBoard + " invisible right " + rightBoard);
                        outQueue.add(rightBoard + " invisible left " + leftBoard);
                    }

                }

            } 
        }finally {

        }

    }
    /**
     * Handles ball travelling between boards by updating the Balls list of the 
     * boards of interest
     * 
     * @param exitBoard - the board from which the ball is leaving
     * @param destinationBoard - the board that the ball is travelling to
     * @param ball - the ball that is traveling
     */
    public void handleBounce(Board exitBoard, Board destinationBoard, Ball ball){

    }

    public void handleUser(){
        new Thread(new Runnable() {
            public void run(){
                try {
                    while(true){
                        handleUserInput();
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                }
            }
        }).start();

    }


    /**
     * Start a PingBallServer using the given arguments.
     * 
     * <br> Usage:
     *      PingballServer [--port PORT] 
     * <br> PORT is an optional integer in the range 0 to 65535 inclusive, specifying the port the server
     *      should be listening on for incoming connections.
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        try {
            while ( ! arguments.isEmpty()) {
                String flag = arguments.remove();
                if (flag.equals("--port")) {
                    port = Integer.parseInt(arguments.remove());
                    if (port < 0 || port > MAXIMUM_PORT) {
                        throw new IllegalArgumentException("port " + port + " out of range");
                    }
                }
            }
        } catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
            System.err.println("usage: PingballServer[--port PORT]");
            return;
        }
        BlockingQueue<String> Requests = new LinkedBlockingQueue<String>();
        BlockingQueue<String> Replies = new LinkedBlockingQueue<String>();
        PingBallServer server = new PingBallServer(port, Replies, Requests);
        server.handleUser();
        server.serve();





    }

}
