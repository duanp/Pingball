package pingball;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import physics.*;
import pingball.*;
import pingball.Wall.WallType;
import boardFileParsing.*;

/**
 * Each client of the pingball game, can be started as a new thread if playing with server
 * or run for single play.  It initializes the board for the client
 * and begins running the pingball program with the board specified via command line argument.
 */
public class Pingball{

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    public static final double GRAVITY = 25;
    public static final double FRICTION = .025;
    public static final double DRAG = .025;
    private Board board;
    /** Maximum port number as defined by ServerSocket. */
    private static final int MAXIMUM_PORT = 65535;
    Queue<String> removedBalls;

    /**
     * Constructor creating Client thread from a file
     * 
     * @param gadgetList - the list of Gadgets attained from parsing the file
     * @param ballList - the list of Balls attained from parsing the file
     * @throws IOException 
     * @throws UnknownHostException 
     */
    // public Pingball(ArrayList<Gadget> gadgetList, ArrayList<Ball> ballList, String hostname, int port, String boardName) throws UnknownHostException, IOException{
    //! remember to get board to take a blocking queue
    public Pingball(String hostname, int port, Board board) throws UnknownHostException, IOException{    
        this.removedBalls = new LinkedBlockingQueue<String>();

        
        this.board = board;

        this.socket = new Socket(hostname, port);
        // message format "boardname connect listofportalnames"
        PrintWriter connectedInfoPrintWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        connectedInfoPrintWriter.println(board.getName() + " connect " + this.board.getPortalNames());
        connectedInfoPrintWriter.flush();

        this.removedBalls = board.messageQueue();

    }


  /*  public Pingball(ArrayList<Gadget> gadgetList, ArrayList<Ball> ballList){
        this.board = new Board("",GRAVITY,FRICTION,DRAG,gadgetList);
        for(Ball ballObj : ballList){
            Ball ball = (Ball)ballObj;

            this.board.addBall(ball.getPosition(),ball.getName());
        }
    }*/

    public Pingball(Board board){
        this.board = board;

    }

    // !message should be of the form "boardname which_wall x y xvel yvel ballname"
    // or invisible top
    public void handleInput(Socket socket) throws IOException{
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        // handle messages about balls entering and leaving and messages
        try {


            String line;
            while ((line = in.readLine()) != null) {

                String command[] = line.split(" ");


                if (command[0].equals(this.board.getName()) || command[0].equals("all")){
                    if (command[1].equals("invisible")){
                        if(command[2].equals("top")){
                            this.board.connectWall(command[3], WallType.TOP);

                        } else if (command[2].equals("bottom")){
                            this.board.connectWall(command[3], WallType.BOTTOM);
                        }else if (command[2].equals("left")){
                            this.board.connectWall(command[3], WallType.LEFT);
                        } else if (command[2].equals("right")){
                            this.board.connectWall(command[3], WallType.RIGHT);
                        } 

                    } else if (command[1].equals("checkOwnPortal")){
                        for(String otherBoardString: command[2].split(";")){
                            String[] otherBoardArray = otherBoardString.split(":");
                            for (Portal portal: this.board.getPortals()){
                                if (portal.otherBoard().length() > 0){
                                    if (portal.otherBoard().equals(otherBoardArray[0])){
                                        for (int i = 1; i < otherBoardArray.length; i++ ){
                                            if (portal.otherPortal().equals(otherBoardArray[i])){
                                                portal.setIsOpen(true);
                                            }
                                        }



                                    } }
                                else {
                                    for (int i = 1; i < otherBoardArray.length; i++ ){
                                        if (portal.otherPortal().equals(otherBoardArray[i])){

                                            portal.setOtherBoard(otherBoardArray[0]);
                                            portal.setIsOpen(true);
                                        }
                                    }
                                }

                            }


                        }} else if (command[1].equals("newBoardOpen")) {
                            for (Portal portal: this.board.getPortals()){
                                if (portal.otherBoard().length() > 0){
                                    if (portal.otherBoard().equals(command[2])){
                                        for (int i = 3; i < command.length; i++ ){
                                            if (portal.otherPortal().equals(command[i])){
                                                portal.setIsOpen(true);
                                            }
                                        }

                                    }
                                } else {
                                    for (int i = 3; i < command.length; i++ ){
                                        if (portal.otherPortal().equals(command[i])){
                                            portal.setOtherBoard(command[2]);
                                            portal.setIsOpen(true);
                                        }
                                    }

                                }

                            }

                        } else if (command[1].equals("receiveBallFromPortal")){
                            for (Portal portal: this.board.getPortals()){

                                if (portal.getName().equals(command[2])){
                                    Vect ballPosition = portal.getPosition();
                                    Vect ballVelocity = new Vect(Double.parseDouble(command[5]), Double.parseDouble(command[6]));
                                    if (command.length > 7){

                                        this.board.addBall(ballPosition, ballVelocity, command[7]);
                                    } else {

                                        this.board.addBall(ballPosition, ballVelocity, "unamedBall");

                                    }

                                }

                            }

                        } else if (command[1].equals("disconnectWall")) {


                            if(command[2].equals("top")){
                                this.board.disconnectWall(command[0], WallType.TOP);

                            } else if (command[2].equals("bottom")){
                                this.board.disconnectWall(command[0], WallType.BOTTOM);
                            }else if (command[2].equals("left")){
                                this.board.disconnectWall(command[0], WallType.LEFT);
                            } else if (command[2].equals("right")){
                                this.board.disconnectWall(command[0], WallType.RIGHT);
                            } 


                        } else if (command[1].equals("disconnectPortals")) {
                            String disconnectedBoardName = command[2];
                            for (Portal portal: this.board.getPortals()){
                                if (portal.otherBoard().equals(disconnectedBoardName) || portal.otherBoard().length() == 0){
                                    for (int i = 3; i < command.length; i++){
                                        if(portal.otherPortal().equals(command[i])){
                                            portal.setIsOpen(false);
                                        }
                                    }
                                }
                            }

                        }
                        else {

                            Vect ballPosition;
                            if (command[1].equals("left")){
                                ballPosition = new Vect(19.0, Double.parseDouble(command[3]));
                            } else if (command[1].equals("right")) {
                                ballPosition = new Vect(1.0, Double.parseDouble(command[3]));
                            }
                            else if (command[1].equals("top")){

                                ballPosition = new Vect(Double.parseDouble(command[2]) , 19.0);
                            } else {
                                ballPosition = new Vect(Double.parseDouble(command[2]) , 1.0);
                            }
                            Vect ballVelocity = new Vect(Double.parseDouble(command[4]), Double.parseDouble(command[5]));
                            if (command.length > 6){
                                this.board.addBall(ballPosition, ballVelocity, command[6]);
                            } else {

                                this.board.addBall(ballPosition, ballVelocity, "unamedBall");

                            }
                        }

                }

            }

        } finally {

        }

    }

    public void handleOutput(String ballinfo) throws IOException{
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        // handle messages about balls entering and leaving


        out.println(this.board.getName() + " " + ballinfo);
        out.flush();




        //      out.close();

    }

    /**
     * Constructor that initializes the client with no board file provided
     * uses the default board


    public Pingball(BlockingQueue<String> Requests, BlockingQueue<String> Replies){
      //load benchmark boards and initialize them
        BenchmarkBoards boards = new BenchmarkBoards();
        HashMap<String, List<Object>> defaultBoardMap = boards.defaultBoard();
        List<Gadget> boardGadgets = new ArrayList<Gadget>();
        boardGadgets.addAll((List<Gadget>)(Object)(defaultBoardMap.get("circle_bumper")));
        boardGadgets.addAll((List<Gadget>)(Object)(defaultBoardMap.get("triangle_bumper")));
        boardGadgets.addAll((List<Gadget>)(Object)(defaultBoardMap.get("square_bumper")));
        this.board = new Board(GRAVITY,FRICTION,DRAG,boardGadgets);
        for(Object ballObj : defaultBoardMap.get("ball")){
            Ball ball = (Ball)ballObj;

            this.board.addBall(ball.getPosition());
        }
        this.in = Requests;
        this.out = Replies;
    }
     */





    /*  /**
     * This function is called when the thread is started, it runs the board

    public void run(){
        this.board.run();
    }*/

    public static final void testException(){
        throw new RuntimeException("debug thread");
    }



    /**
     * Start a Pingball Game using the given arguments.
     * 
     * <br> Usage:
     *      Pingball [--host HOST] [--port PORT] [--file FILE]
     *      
     * <br> HOST: the host is the server that the Client is supposed to connect to
     *            is the host is not specified, then a single-play game is run
     *            
     * <br> Port: an optional integer in the range 0 to 65535 inclusive, specifying the port that the client
     *            should use to connect to the server
     *            
     * <br> FILE:  an optional argument specifying a file pathname where a board has been stored. If this
     *             argument is given, the stored board should be loaded as the starting board.
     * @throws IOException 
     * @throws UnknownHostException 
     *
     */
    public static void main(String[] args) throws UnknownHostException, IOException {
        BlockingQueue<String> Requests = new LinkedBlockingQueue<>();
        BlockingQueue<String> Replies = new LinkedBlockingQueue<>();

        // Parses the command line input and runs a client game appropriately
        int port = -1;
        String host = "";
        String boardName = "";
        Optional<File> file = Optional.empty();
        File newFile = new File("");
        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));

        // if host is not specified, otherwise server will take care of it

        while ( ! arguments.isEmpty()) {
            String flag = arguments.remove();

            if (flag.equals("--host")) {
                host = arguments.remove();
            } else if (flag.equals("--port")) {
                port = Integer.parseInt(arguments.remove());
                if (port < 0 || port > MAXIMUM_PORT) {
                    throw new IllegalArgumentException("port " + port + " out of range");
                }
            }
            else if (flag.equals("board1") || flag.equals("board2") || flag.equals("board3")){
                boardName = flag;
            } else if (flag.length() >= 1) {
                // parse file and set board appropiately
            //    file = Optional.of(new File("/Users/duanp/Documents/new_duanp_workspace/new_pingball-phase2/boards/flippers.pb"));
                file = Optional.of(new File(flag));
              //  newFile = new File(flag);
                if ( ! file.get().isFile()) {
                    throw new IllegalArgumentException("file not found: \"" + file.get() + "\"");
                }
            } else {
                throw new IllegalArgumentException("unknown option: \"" + flag + "\"");
            }

        }



        Board board;
        ArrayList<Ball> ballList = new ArrayList<Ball>();
        ArrayList<Gadget> boardGadgets = new ArrayList<Gadget>();
        
        if (file.equals(Optional.empty())){
            BenchmarkBoards boards = new BenchmarkBoards();
            HashMap<String, List<Object>> defaultBoardMap = boards.defaultBoard2();
            boardGadgets.addAll((List<Gadget>)(Object)(defaultBoardMap.get("circle_bumper")));
            boardGadgets.addAll((List<Gadget>)(Object)(defaultBoardMap.get("triangle_bumper")));
            boardGadgets.addAll((List<Gadget>)(Object)(defaultBoardMap.get("square_bumper")));
            boardGadgets.addAll((List<Gadget>)(Object)(defaultBoardMap.get("left_flipper")));
            boardGadgets.addAll((List<Gadget>)(Object)(defaultBoardMap.get("right_flipper")));
            boardGadgets.addAll((List<Gadget>)(Object)(defaultBoardMap.get("portals")));
            board = new Board(boardName,GRAVITY,FRICTION,DRAG,boardGadgets);

            for(Object ballObj : defaultBoardMap.get("ball")){
                Ball ball = (Ball)ballObj;

                board.addBall(ball.getPosition(), ball.getName());
            }


        } else {
            File defaultTestBoard = new File("boards/default.pb"); 
            Board outputBoard = MakeInputStream.parseFile(defaultTestBoard);
            board = MakeInputStream.parseFile(file.get());
            
            //parser.java will return a gadget and ball list
        }
        Pingball client;
        
        if (port >= 0){
            board.isSinglePlay = false;

            client = new Pingball(host, port, board);



            (new Thread() {
                public void run(){
                    try {

                        client.handleInput(client.socket);

                    } catch (Exception ioe) {
                        ioe.printStackTrace();
                    } finally {

                    }

                }}).start();
            new Thread(new BoardConnection(client)).start();

            client.board.run();

        } else {
            client = new Pingball(board);
            client.board.run();


        }
    }





    /**
     * Gets the board of the client 
     * @return - the board of the client
     */
    public Board getBoard(){
        return this.board;
    }

    public class ServerConnection implements Runnable{
        Pingball client;
        public ServerConnection(){

        }
        public void run(){
            try {
                //throw new RuntimeException("debug thread");
                handleInput(socket);
            } catch (Exception ioe) {
                ioe.printStackTrace(); // but don't terminate serve()
            } finally {

            }
        }

    }

    public static class BoardConnection implements Runnable{
        Pingball client;
        public BoardConnection(Pingball client){
            this.client = client;

        }
        public void run(){
            try {
                while(true){
                    String ballInfo = ((LinkedBlockingQueue<String>) client.removedBalls).take();
                    client.handleOutput(ballInfo);
                }

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {

            }
        }

    }


}