package pingball;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import physics.*;

/**
 * A mutable class representing a board of fixed size that contains several Ball objects and Gadget objects.
 * rep invariant:  width and height are both bigger than 0
 */
public class Board {

    private static int width;
    private static int height;
    private static final int STANDARDSIDE = 20;
    private static double gravity = 25;
    private static double mu = .025;
    private static double mu2 = .025;
    private static ArrayList<Ball> balls;
    private static List<Gadget> gadgets;
    private static final List<Gadget> STANDARDWALLS = Arrays.asList(new OuterWall(0,true,true),new OuterWall(20,true,true),new OuterWall(0,false,true),
            new OuterWall(20,false,true));
    
    /**
     * Creates a new Board with standard dimensions and no balls nor gadgets
     */
    public Board() {
        width = STANDARDSIDE;
        height = STANDARDSIDE;
        balls = new ArrayList<Ball>();
        gadgets = STANDARDWALLS;
    }
    
    /**
     * Creates a new Board with the given parameters
     * @param width
     * @param height
     * @param balls - a list of Ball objects
     * @param gadgets - a list of Gadget objects
     */
    public Board(int width, int height, ArrayList<Ball> balls, List<Gadget> gadgets) {
        this.width = width;
        this.height = height;
        this.balls = balls;
        //this.gadgets = (List<Gadget>)STANDARDWALLS;
        //this.gadgets.addAll(gadgets);
        this.gadgets = new ArrayList<Gadget>();
        this.gadgets.addAll(gadgets);
        this.gadgets.addAll(STANDARDWALLS);
    }
    
    /**
     * Creates a new Board with the given parameters
     * @param width
     * @param height
     * @param balls - a list of Ball objects
     * @param gadgets - a list of Gadget objects
     * @param gravity - acceleration due to gravity, L/sec^2
     * @param mu - friction coefficient, sec^(-1)
     * @param mu2 - friction coefficient, L^(-1)
     */
    public Board(int width, int height, ArrayList<Ball> balls, List<Gadget> gadgets, double gravity, double mu, double mu2) {
        this.width = width;
        this.height = height;
        this.balls = balls;
        this.gadgets = STANDARDWALLS;
        this.gadgets.addAll(gadgets);
        this.gravity = gravity;
        this.mu = mu;
        this.mu2 = mu2;
    }
    
    /**
     * @return The width of the Board
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * @return The height of the Board
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * @return The list of Ball objects of the Board
     */
    public List<Ball> getBalls() {
        return balls;
    }
    
    /**
     * @return The list of Gadget objects of the Board
     */
    public List<Gadget> getGadgets() {
        return gadgets;
    }
    
    public void addBall(Ball newBall){
        this.balls.add(newBall);
    }
    
    /**
     * Prints a textual drawing of the Board in its current state, using symbols to denote gadgets and balls. Each character represents a 1L x 1L square.
     * The gadgets are drawn first, and then the balls are drawn, possibly hiding a gadget underneath if they share the same square.
     * = : Absorber
     * * : Ball
     * 0 : CircleBumper
     * __ or /\ or || : Flipper
     * . : Outer wall
     * # : SquareBumper
     * \ or / : TriangleBumper
     */
    public void drawBoard() {
        int height = this.height;
        int width = this.width;
        Character[][] charBoard = new Character[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                charBoard[i][j] = ' ';
            }
        }
        //populate charBoard with gagdets
        for (Gadget gadget : this.gadgets) {
            Character gadgetSymbol = gadget.getSymbol();
            if (gadget.getSymbol() == '.'){
                //Skip OuterWall
                continue;
            }
            int locationX = gadget.getX();
            int locationY = gadget.getY();
            if (gadgetSymbol == '-' || gadgetSymbol == '|') {
                //Flipper
                String boardSquarePosition = ((Flipper) gadget).getBoardSquarePosition();
                switch (boardSquarePosition){
                case "top":
                    charBoard[locationY][locationX] = gadgetSymbol;
                    charBoard[locationY][locationX + 1] = gadgetSymbol;
                    break;
                case "left":
                    charBoard[locationY][locationX] = gadgetSymbol;
                    charBoard[locationY + 1][locationX] = gadgetSymbol;
                    break;
                case "bottom":
                    charBoard[locationY + 1][locationX] = gadgetSymbol;
                    charBoard[locationY + 1][locationX + 1] = gadgetSymbol;
                    break;
                case "right":
                    charBoard[locationY][locationX + 1] = gadgetSymbol;
                    charBoard[locationY + 1][locationX + 1] = gadgetSymbol;
                    break;
                }
            }
            else {
                //Bumper or Absorber
                for (int i = 0; i < gadget.getHeight(); i++){
                    for (int j = 0; j < gadget.getWidth(); j++) {
                        charBoard[locationY+i][locationX+j] = gadget.getSymbol();             
                    }
                }
                if (gadgetSymbol == '='){
                    //Absorber, check whether it has balls
                    if (((Absorber) gadget).getNumStoredBalls() > 0){
                        charBoard[locationY+gadget.getHeight()-1][locationX+gadget.getWidth()-1] = '*';
                    }
                }
            }       
        }
        //populate charBoard with balls
        for (Ball ball : this.balls) {
            if (!ball.getIsAbsorbed()){
                int centerX = (int) ball.getX();
                int centerY = (int) ball.getY();
                charBoard[centerY][centerX] = '*';
            }
        }
        //Translate Character array charBoard into String strBoard
        String strBoard = new String(new char[width+2]).replace("\0", ".");
        strBoard = strBoard + "\n";
        for (int i = 0; i < height; i++) {
            strBoard = strBoard + ".";
            for (int j = 0; j < width; j++) {                
                strBoard = strBoard + charBoard[i][j];
            }
            strBoard = strBoard + ".";
            strBoard = strBoard + "\n";
        }
        strBoard = strBoard + new String(new char[width+2]).replace("\0", ".");
        System.out.println(strBoard);
    }
    
    /**
     * Shoots out a ball from absorbers that are releasing a ball
     */
    public void unabsorbBalls() {
        for (Gadget gadget : this.gadgets){
            if (gadget.getSymbol() == '='){
                if (((Absorber)gadget).getHasBall()){
                    int locationX = gadget.getX();
                    int locationY = gadget.getY();
                    this.addBall(new Ball(new Vect(0,-50),gadget.getX() + gadget.getWidth() - 0.25, gadget.getY() + gadget.getHeight() - 0.25));
                    ((Absorber)gadget).setHasBall(false);
                }
            }
        }
    }
    
    /**
     * Clean up removed (absorbed) balls
     */
    public void removeAbsorbedBalls() {
        List<Ball> slatedForRemoval = new ArrayList<Ball>();
        for (Ball ball : this.balls){
            if (ball.getIsAbsorbed()){
                slatedForRemoval.add(ball);
            }
        }
        for (Ball ball : slatedForRemoval){
            this.balls.remove(ball);
        }
    }
    
    /**
     * Makes every Ball on this Board move for 1 frame according to its current velocity
     */
    public void moveBalls() {
        for (Ball ball : this.balls) {
            System.out.println("Ball coordinates");
            System.out.println(ball.getX());
            System.out.println(ball.getY());
            System.out.println(ball.getVelocity());
            if (!ball.getIsAbsorbed()){
                ball.move(gravity, mu, mu2);
                //Adjust ball coordinates if outside confines
                if (ball.getX() < 0){
                    ball.changePosition(0.1, ball.getY());
                }
                if (ball.getX() > this.width){
                    ball.changePosition(this.width-.1, ball.getY());
                }
                if (ball.getY() < 0){
                    ball.changePosition(ball.getX(), 0.1);
                }
                if (ball.getY() > this.height){
                    ball.changePosition(ball.getX(), this.height-.1);
                }
                if (ball.getVelocity().length() > 200.0){
                    double ratioShrink = ball.getVelocity().length() / 200.0;
                    ball.changeVelocity(ball.getVelocity().times(ratioShrink));
                }
            }
        }
    }
    
    /**
     * Checks every Gadget on this Board, and if they are triggered, calls their action
     */
    public void activateGadgets() {
        for (Gadget gadget : this.gadgets) {
            if (gadget.getIsTriggered()){
                gadget.action();
            }
        }
    }
    
    /**
     * Checks every ball-ball and ball-gadget pair for collision detection
     */
    public void executeCollisions() {
        int numOfBalls = this.balls.size();
        for (int i = 0; i < numOfBalls; i++){
            for (int j = i+1; j < numOfBalls; j++){
                Ball firstBall = this.balls.get(i);
                Ball secondBall = this.balls.get(j);
                if (!firstBall.equals(secondBall) && !firstBall.getIsAbsorbed() && !secondBall.getIsAbsorbed()){
                    if (firstBall.detectBallCollision(secondBall)){
                        //The two balls are not the same, and neither is absorbed
                        firstBall.collideWithBall(secondBall);
                    }
                }
            }
        }
        for (Ball ball : this.balls) {
            for (Gadget gadget : this.gadgets){
                if (!ball.getIsAbsorbed() && gadget.detectBallCollision(ball)){
                    //The ball is currently not absorbed and is colliding with gadget
                    gadget.reflectBall(ball);
                    break;
                }
            }
        }
    }
    
    /**
     * Executes all events that happen from one frame to the next: ball movement, gadget activation, ball collisions
     */
    public void nextFrame() {
        this.unabsorbBalls();
        this.removeAbsorbedBalls();
        this.moveBalls();
        this.activateGadgets();
        this.executeCollisions();
    }
    
    /**
     * Executes all events for a certain duration, and prints a text drawing of the board at each frame
     * @param frames - the duration in frames
     */
    public void deploy(int frames) throws InterruptedException {
        for (int i = 0; i < frames; i++) {
            this.nextFrame();
            this.drawBoard();
            Thread.sleep(50);
        }
    }
    
    /**
     * @return True or false, depending on whether this Board object represents a valid board
     */
    public boolean checkRep() {
        return (width > 0 && height > 0);
    }
    
}
