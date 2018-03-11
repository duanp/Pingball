package pingball;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import com.sun.java_cup.internal.runtime.Symbol;

import physics.Vect;
import pingball.Wall.WallType;

/**
 * Board class
 * 
 * a data type that stores the topological information of a pingball board including:
 * - gadget states, positions
 * - ball position and velocities
 * - walls
 * - portal gadget, etc
 * 
 * methods include:
 * - advanceSimulation() that does an internal simulation
 * - hasPendingMessage(), takeMessage() that are used for network play (wall connection
 * is overwritten, or ball becomes transported through wall, etc)
 * 
 *
 *
 */
public class Board {

    //the width and height of the board
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;

    //the board's name
    private final String name;
    public boolean isSinglePlay;

    //component representations
    private Wall[][] walls;
    private Wall[] topWall;
    private Wall[] bottomWall; 
    private Wall[] leftWall;
    private Wall[] rightWall;

    private List<Ball> balls= new ArrayList<Ball>();
    private Gadget[][] gadgetPositions = new Gadget[22][22];
    private List<Portal> portals;

    //message queue fed by simulation
    private Queue<String> messageQueue;

    //physics parameters
    double gravity, frictionCoefficient, dragCoefficient;

    /**
     * make a new empty board with default physics parameters, and no balls or
     * gadgets in play
     * 
     * @param name the name of this board
     */
    public Board(String name) {
        this.name = name;
        this.gravity = 0; //TODO: check default values
        this.frictionCoefficient = 0;
        this.dragCoefficient = 0;
    }

    private List<List<Character>> canvas = new ArrayList<List<Character>>(); //2d text array of characters, ready for drawBoard
    public final double deltaTime= .005;

    /**
     * Create a new game board with all the necessary gadgets and no balls at the initial state of t=0
     * @param gravity, friction coefficient, drag coefficient, list of gadgets to be added to the board
     */
    public Board(String name, double gravity, double friction, double drag, List<Gadget> gadgets) {


        for(int i=0; i<22;i++){
            List <Character> column= new ArrayList<Character>();
            for(int j=0;j<22;j++){
                column.add(' ');
            }
            canvas.add(column);
        }
        this.name = name;
        this.gravity=gravity;
        this.frictionCoefficient=friction;
        this.dragCoefficient=drag;
        this.messageQueue = new LinkedBlockingQueue<String>();

        this.topWall = new Wall[22];
        this.bottomWall = new Wall[22];
        this.leftWall = new Wall[22];
        this.rightWall = new Wall[22];
        this.portals = new ArrayList<Portal>();
        this.isSinglePlay = true;
        Wall[][] array = { topWall, bottomWall, leftWall, rightWall };
        walls = array;



        for(Gadget gadget: gadgets){
            if (gadget.isPortal()){
                this.portals.add((Portal) gadget);

            }

            for(int i=0;i<gadget.getWidth();i++){
                for(int j=0;j<gadget.getHeight();j++){
                    gadgetPositions[(int) gadget.getPosition().x()+i+1][(int) gadget.getPosition().y()+j+1]=gadget;
                    canvas.get((int) gadget.getPosition().y()+j+1).set((int) gadget.getPosition().x()+i+1, gadget.drawGadget(new Vect(gadget.getPosition().x()+i,gadget.getPosition().y()+j))); 
                }
            }
        }
        for (Portal portal: portals){
            for (Portal otherPortal: portals){
                if(portal.otherPortal().equals(otherPortal.getName()) && (portal.otherBoard().length() == 0 || portal.otherBoard().equals(name))){
                    portal.setIsOpen(true);
                }
            }
        }
        for(int i=-1;i<21;i++){
            this.topWall[i+1]= new Wall(new Vect(i,-1));
            this.bottomWall[i+1]= new Wall(new Vect(i,20));
            gadgetPositions[i+1][0]= this.topWall[i+1];
            gadgetPositions[i+1][21]= this.bottomWall[i+1];
            canvas.get(0).set(i+1, '.');
            canvas.get(21).set(i+1, '.');

        }

        for(int i=-1;i<21;i++){
            this.leftWall[i+1] = new Wall(new Vect(-1,i));
            this.rightWall[i+1] = new Wall(new Vect(20,i));
            gadgetPositions[0][i+1]= this.leftWall[i+1];
            gadgetPositions[21][i+1]= this.rightWall[i+1];
            canvas.get(i+1).set(0, '.');
            canvas.get(i+1).set(21, '.');

        }
        checkRep();
    }


    //observers

    /**
     * Getter method for name of board
     * @return the name of the board
     */
    public String getName() {
        return name;
    }

    /**
     * @return the width of the board
     */
    public int width() {
        return WIDTH;
    }

    /**
     * @return the height of the board
     */
    public int height() {
        return HEIGHT;
    }


    /**
     * Portal/Wall
     * (other board name) (other portal name) / Wall 
     * Ball x, y , xVel, yVel
     */
    public void message(String gadget, Ball ball, String info) {
        String xPos = Double.toString(ball.getPosition().x());
        String yPos = Double.toString(ball.getPosition().y());
        String xVel = Double.toString(ball.getVelocity().x());
        String yVel = Double.toString(ball.getPosition().y());

        messageQueue.add(gadget + " " + info + " " + xPos + " " + yPos + " " + xVel + " " + yVel + " " + " " + ball.getName());

    }

    /**
     * @return the Message queue of the board
     */
    public Queue<String> messageQueue() {
        return messageQueue;
    }

    /** Getter method to prevent rep exposure and return the gravity value of the board. */ 
    public Double getBoardGravity(){
        Double boardGravity = new Double (this.gravity); 
        return boardGravity; 

    }

    /** Getter method to prevent rep exposure and return the friction value of the board. */
    public Double getBoardFriction(){
        Double boardFriction = new Double (this.frictionCoefficient); 
        return boardFriction;

    }

    /** Getter method to prevent rep exposure and return the drag value of the board. */
    public Double getBoardDrag(){
        Double boardDrag = new Double (this.dragCoefficient); 
        return boardDrag; 

    }

    public String getPortalNames(){
        String portalNames = "";
        for (Portal portal: portals){
            portalNames += portal.getName() + " ";

        }
        return portalNames;
    }

    public List<Portal> getPortals(){
        return this.portals;
    }

    /**
     * Sets each block of a Wall array to be invisible
     * @param walls the wall be set as invisible
     */
    public void setInvisible(Wall[] walls){
        for (Wall wall: walls){
            wall.setInvisible(true);
        }
    }

    public void setVisible(Wall[] walls){
        for (Wall wall: walls){
            wall.setInvisible(false);
        }
    }



    /**
     * connect this board to another with the specified name, across the
     * specified wall. 
     * Currently, the name is written from the 1st index of the wall.
     * 
     * @param boardName the name of the neighboring board. Must be length of at least one.
     * @param type the type of the wall across which to be connected
     */
    public void connectWall(String boardName, WallType type) {
        for (Wall[] wall : walls) {      
            if (wall[10].type().equals(type)) { //the wall to change *note the corners are unreliable
                setInvisible(wall);
                int j = 0;
                for (int i = 1; i<22; i++) { 
                    wall[i].setDraw(boardName.charAt(j));
                    j+=1;
                    if (j >= boardName.length()){
                        break;
                    }            
                }
            }
        }
    }

    public void disconnectWall(String boardName, WallType type) {
        for (Wall[] wall : walls) {      
            if (wall[10].type().equals(type)) { //the wall to change *note the corners are unreliable
                setVisible(wall);

                for (int i = 1; i<22; i++) { //TODO: fix to be better than arbitrarily starting at 4
                    wall[i].setDraw('.');

                }
            }
        }
    }


    //////////////////

    // observers
    /**
     * @return the number of balls on the board
     */
    public int getBallNumber() {
        return balls.size();
    }

    /** 
     * @return the Wall array of blocks of the top wall
     */
    public Wall[] getTopWall(){
        return topWall;
    }

    /**
     * @return the Wall array of blocks of the bottom wall
     */
    public Wall[] getBottomWall(){
        return bottomWall;
    }

    /**
     * @return the Wall array of blocks of the left wall
     */
    public Wall[] getLeftWall(){
        return leftWall;
    }

    /**
     * @return the Wall array of blocks of the right wall
     */
    public Wall[] getRightWall(){
        return rightWall;
    }



    /**
     * Adds a ball to the game board at the given position. Should be called before starting board.
     * @param position to insert the ball at, measured as the center of the ball
     */
    public void addBall(Vect position, String name) {
        balls.add(new Ball(position, gravity, frictionCoefficient, dragCoefficient, name));
        canvas.get((int) position.y()).set((int) position.x(), '*');
    }

    /**
     * Adds a ball to the game board at the given position. Should be called before starting board.
     * @param position to insert the ball at, measured as the center of the ball
     */
    public void addBall(Vect position, Vect velocity, String name) {
        Ball ball = new Ball(position, gravity, frictionCoefficient, dragCoefficient, name);
        ball.setVelocity(velocity);        
        balls.add(ball);
        canvas.get((int) position.y()).set((int) position.x(), '*');
    }


    /**
     * Remove a ball from a Board when the ball 
     * i) passes through a portal to a new board, or 
     * ii) passes through a wall connecting boards and goes into the new board. 
     */
    public void removeBall(Ball ball) {
        try{
            balls.remove(ball);
        } catch (ConcurrentModificationException e){
            System.out.println("DHSKJSDFSD" + ball.getName());

        }
        canvas.get((int) ball.getCircle().getCenter().y()).set((int) ball.getCircle().getCenter().x(), ' ');
    }

    /**
     * Runs the board, 
     * causing the balls present on the board to move with every loop and gadget movement triggers to become active 
     * The board is also drawn at board time increments of deltaTime
     */
    public void run() {
       
        while(true) {
            int a=0;
            for(Gadget[] gadgets: gadgetPositions) {
                int b=0;
                for(Gadget gadget: gadgets) {
                    if(gadget!=null) {
                        gadget.updateState();
                        //add gadget in its current text state to canvas to be drawn
                        char draw = gadget.drawGadget(new Vect(a-1,b-1));

                        //TODO: get the name of the connected board and redraw board
                        //name of joined boards
                        /**
                         * if (joined == true) {
                         *      if (!invisible) 
                         *          
                         * }
                         */

                        canvas.get(b).set( a, draw);
                    }
                    else{
                        canvas.get(b).set( a, ' ');
                    }
                    b+=1;
                }
                a+=1;

            }
            ArrayList<Ball> BallsToRemove = new ArrayList<Ball>();
            ArrayList<Ball> ballsCopy = new ArrayList<Ball>(balls);
            for(Ball ball: ballsCopy){
                boolean transportBall = false;
                String info = "";
                String whichGadget = "Wall";
                //if ball is not absorbed(in queue of the absorber)

                if (!ball.getInQueue()){
                    for (Wall boardWall:topWall){
                        if (boardWall.transport(ball)){
                            info = "top";
                            transportBall = true;
                            break;
                        }

                    }

                    for (Wall boardWall:bottomWall){

                        if (boardWall.transport(ball)){
                            info = "bottom";
                            transportBall = true;
                            break;
                        }
                    }

                    for (Wall boardWall:leftWall){
                        if (boardWall.transport(ball)){
                            info = "left";
                            transportBall = true;
                            break;
                        }
                    }
                    for (Wall boardWall:rightWall){
                        if (boardWall.transport(ball)){
                            info = "right";
                            transportBall = true;
                            break;
                        }
                    }
                    for (Portal portal: portals){
                        if (portal.transport(ball)){
                            if (isSinglePlay){
                                for (Portal otherPortal: portals){
                                    if (portal.otherPortal().equals(otherPortal.getName())){
                                        ball.setPosition(otherPortal.getPosition());
                                        break;
                                    }
                                }
                            } else {
                            info = portal.otherBoard() + " " + portal.otherPortal();
                            whichGadget = "portal";
                            }
                            transportBall = true;
                            break;
                        }

                    }
                    if (transportBall){

                        BallsToRemove.add(ball);

                        message(whichGadget, ball, info);
                        continue;
                    }

                    double posX=ball.getPosition().x();
                    double posY=ball.getPosition().y();
                    boolean collision=false;
                    for(int i=(int) (posX);i< posX+2;i++){
                        for(int j= (int) (posY);j<posY+2;j++){
                            Gadget gadget=gadgetPositions[i][j];
                            if(gadget!=null){
                                //if gadget is colliding with ball before next boar update
                                if(gadget.timeUntilCollision(ball)<deltaTime){
                                    //perform collision(set velocities of the balls to those after the collision)
                                    gadget.performCollision(ball);
                                    collision=true;
                                    break;
                                }
                            }
                        }
                        if(collision){
                            break;
                        }
                    }

                    for(Ball otherBall: balls){
                        if(!ball.equals(otherBall)){
                            if(otherBall.timeUntilCollision(ball)<deltaTime){
                                otherBall.performCollision(ball);
                            }
                        }
                    }
                    //              //move ball by velocity*deltaTime
                    //            ball.move(deltaTime);
                }
                if (!transportBall){
                    //move ball by velocity*deltaTime
                    ball.move(deltaTime);
                    //add ball to canvas to be drawn
                    if ((int) ball.getPosition().y()+1<22){
                    canvas.get((int) ball.getPosition().y()+1).set((int) ball.getPosition().x()+1, '*');
                    }
                }
            }
            if (!isSinglePlay){
                for(Ball removedBall: BallsToRemove){
                    try{
                        removeBall(removedBall);
                    } catch (ConcurrentModificationException e){


                    }

                }
            }
            BallsToRemove = new ArrayList<Ball>();
            // align and print the gadget and ball characters in the canvas
            drawBoard();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /*
     * Draws the present state of the board by printing the character representation of 
     * board objects in the 2d list canvas in a grid
     */
    public void drawBoard() {
        String drawing="";
        for(List<Character> row: canvas){
            for(Character pixel: row){
                //add character to drawing of canvas
                drawing+=pixel;
            }
            //insert line to draw next
            drawing+="\n";
        }
        //print the drawing of the canvas
        System.out.println(drawing);
    }

    /**
     * checks rep invariant for triangle bumper (there are balls and gadgets)
     */
    private void checkRep(){
        // assert !(this.balls.isEmpty());
        assert this.gadgetPositions.length>0;
    }
}