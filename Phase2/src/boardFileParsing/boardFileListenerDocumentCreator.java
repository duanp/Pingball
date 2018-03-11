package boardFileParsing;
import physics.Vect;
import pingball.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.misc.OrderedHashMap;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * This class creates an ANTLR listener to walk through a parse tree. 
 * The listener then can create a board, or map of board objects, among other things. 
 * @author kellypet
 *
 */
public class boardFileListenerDocumentCreator extends boardGrammarBaseListener{

    List<Gadget> circleBumpers = new ArrayList<Gadget>();//add circle bumper
    List<Gadget> triangleBumpers = new ArrayList<Gadget>();//add triangle bumper
    List<Gadget> squareBumpers = new ArrayList<Gadget>();//add square bumper
    List<Gadget> leftFlippers = new ArrayList<Gadget>();//add left flipper
    List<Gadget> rightFlippers = new ArrayList<Gadget>();//add right flipper
    List<Ball> balls = new ArrayList<Ball>(); //add balls
    List<Gadget> absorbers = new ArrayList<Gadget>();//add absorbers
    List<Gadget> portals = new ArrayList<Gadget>();//add portals
      
  //Create new lists of triggered gadgets 
    List<Gadget> circleBumpersNew = new ArrayList<Gadget>();
    List<Gadget> triangleBumpersNew = new ArrayList<Gadget>();
    List<Gadget> squareBumpersNew = new ArrayList<Gadget>();
    List<Gadget> leftFlippersNew = new ArrayList<Gadget>();
    List<Gadget> rightFlippersNew = new ArrayList<Gadget>();
    List<Gadget> absorbersNew = new ArrayList<Gadget>();
    List<Gadget> portalsNew = new ArrayList<Gadget>();

    List<Board> listOfCreatedBoards = new ArrayList<Board>(); //board list 
    List<String> listOfCreatedBoardNames = new ArrayList<String>();//board name list 

    Map<Object, String> ballNameMap = new OrderedHashMap<Object, String>();
    Map<String,Gadget> gadgetNameMap = new OrderedHashMap<String,Gadget>(); //map of gadget, gadget name 
    Map<String,TriangleBumper> triangleBumperNameMap = new OrderedHashMap<String,TriangleBumper>();
    Map<String,CircleBumper> circleBumperNameMap = new OrderedHashMap<String,CircleBumper>();
    Map<String,Absorber> absorberNameMap = new OrderedHashMap<String,Absorber>();
    Map<String,SquareBumper> squareBumperNameMap = new OrderedHashMap<String,SquareBumper>();
    Map<String,Portal> portalNameMap = new OrderedHashMap<String,Portal>();
    Map<String,Flipper> rightFlipperNameMap = new OrderedHashMap<String,Flipper>();
    Map<String,Flipper> leftFlipperNameMap = new OrderedHashMap<String,Flipper>();
    
    /**
     * List to store each boards parameters in the following order: 
     * boardParameter[0] = gravity; boardParameter[1] = friction1; boardParameter[2] = friction2; 
     */
    List<Double>boardParameters = new ArrayList<Double>();

    /**
     * Method to make a map of gadgets and balls to put on a board. Gadgets do not contain info about which gadget they will trigger. 
     * @return boardMap
     *              a map of all gadgets and balls on a board. 
     */
    public HashMap<String, List<Gadget>> makeBoardMap(){
        HashMap<String, List<Gadget>> boardMap = new HashMap<String, List<Gadget>>();
        boardMap.put("circle_bumper",circleBumpers);
        boardMap.put("triangle_bumper",triangleBumpers);      
        boardMap.put("square_bumper",squareBumpers);
        boardMap.put("right_flipper",rightFlippers);
        boardMap.put("left_flipper",leftFlippers);
        boardMap.put("absorber",absorbers);
        boardMap.put("portal",portals);
        return boardMap;
    }
    
    /** 
     * Makes a mapping of gadget name and gadget; the gadget will contain correct info about which gadget it should trigger. 
     * @return boardMap
     *              a mapping of gadget names to gadgets. The gadgets will trigger other gadgets according to board file input.
     */
    public HashMap<String, List<Gadget>> makeTriggeredBoardMap(){
        HashMap<String, List<Gadget>> boardMap = makeBoardMap();
        for (TriangleBumper triBump: triangleBumperNameMap.values()){
            String triName = triBump.getName();
            if (fireTriggerMap.keySet().contains(triName)){ //check if key set contains name of gadget
                String nameOfGadgetToBeTriggered=fireTriggerMap.get(triName);           
                Gadget newTriggeredGadg;
                if (gadgetNameMap.keySet().contains(nameOfGadgetToBeTriggered)){//check if list of gadgets contains this gadget{
                    List<Gadget> gadgetTriggeredList = new ArrayList<Gadget>();
                    newTriggeredGadg = gadgetNameMap.get(nameOfGadgetToBeTriggered);
                    triBump.getGadgetsToTrigger();
                    gadgetTriggeredList.add(newTriggeredGadg); //add triggered Gadget to its list of gadgets to be triggered 
                    TriangleBumper triBumperNew = new TriangleBumper(triBump.getName(),triBump.getPosition(),gadgetTriggeredList);
                    triangleBumpersNew.add(triBumperNew);
                } else { 
                    triangleBumpersNew.add(triBump);
                }
            } else { 
                triangleBumpersNew.add(triBump);
            }
        }for (CircleBumper circBump: circleBumperNameMap.values()){
            String circName = circBump.getName();
            if (fireTriggerMap.keySet().contains(circName)){ //check if key set contains name of gadget:
                String nameOfGadgetToBeTriggered=fireTriggerMap.get(circName);    
                Gadget newTriggeredGadg;
                if (gadgetNameMap.keySet().contains(nameOfGadgetToBeTriggered)){//check if list of gadgets contains this gadget{
                    newTriggeredGadg = gadgetNameMap.get(nameOfGadgetToBeTriggered);
                    List<Gadget> gadgetTriggeredList = new ArrayList<Gadget>();
                    circBump.getGadgetsToTrigger();
                    gadgetTriggeredList.add(newTriggeredGadg); //add triggered Gadget to its list of gadgets to be triggered 
                    CircleBumper circBumperNew = new CircleBumper(circBump.getName(),circBump.getPosition(),gadgetTriggeredList);
                    circleBumpersNew.add(circBumperNew);
                } else { 
                    circleBumpersNew.add(circBump);
                } 
            } else { 
                circleBumpersNew.add(circBump);
            }
        }for (SquareBumper squareBump: squareBumperNameMap.values()){
            String squareBumpName = squareBump.getName();
            if (fireTriggerMap.keySet().contains(squareBumpName)){ //check if key set contains name of gadget:
                String nameOfGadgetToBeTriggered=fireTriggerMap.get(squareBumpName);    
                Gadget newTriggeredGadg;
                if (gadgetNameMap.keySet().contains(nameOfGadgetToBeTriggered)){//check if list of gadgets contains this gadget{
                    newTriggeredGadg = gadgetNameMap.get(nameOfGadgetToBeTriggered);
                    List<Gadget> gadgetTriggeredList = new ArrayList<Gadget>();
                    squareBump.getGadgetsToTrigger();
                    gadgetTriggeredList.add(newTriggeredGadg); //add triggered Gadget to its list of gadgets to be triggered 
                    SquareBumper squareBumperNew = new SquareBumper(squareBump.getName(),squareBump.getPosition(),gadgetTriggeredList);
                    squareBumpersNew.add(squareBumperNew);
                } else { 
                    squareBumpersNew.add(squareBump);
                } 
            } else { 
                squareBumpersNew.add(squareBump);
            }
        }for (Flipper rightFlipper: rightFlipperNameMap.values()){
            String rightFlipperName = rightFlipper.getName();
            if (fireTriggerMap.keySet().contains(rightFlipperName)){ //check if key set contains name of gadget:
                String nameOfGadgetToBeTriggered=fireTriggerMap.get(rightFlipperName);    
                Gadget newTriggeredGadg;
                if (gadgetNameMap.keySet().contains(nameOfGadgetToBeTriggered)){//check if list of gadgets contains this gadget{
                    newTriggeredGadg = gadgetNameMap.get(nameOfGadgetToBeTriggered);
                    List<Gadget> gadgetTriggeredList = new ArrayList<Gadget>();
                    rightFlipper.getGadgetsToTrigger();
                    gadgetTriggeredList.add(newTriggeredGadg); //add triggered Gadget to its list of gadgets to be triggered 
                    Flipper newRightFlipper = new Flipper(rightFlipper.getName(),rightFlipper.getPosition(),rightFlipper.getOrientation(),rightFlipper.getRotation(),false,gadgetTriggeredList);
                    rightFlippersNew.add(newRightFlipper);
                } else { 
                    rightFlippersNew.add(rightFlipper);
                }
            } else { 
               rightFlippersNew.add(rightFlipper);
            }
        }for (Flipper leftFlipper: leftFlipperNameMap.values()){
            String leftFlipperName = leftFlipper.getName(); 
            if (fireTriggerMap.keySet().contains(leftFlipperName)){ //check if key set contains name of gadget:
                String nameOfGadgetToBeTriggered=fireTriggerMap.get(leftFlipperName);    
                Gadget newTriggeredGadg;
                if (gadgetNameMap.keySet().contains(nameOfGadgetToBeTriggered)){//check if list of gadgets contains this gadget{
                    newTriggeredGadg = gadgetNameMap.get(nameOfGadgetToBeTriggered);
                    List<Gadget> gadgetTriggeredList = new ArrayList<Gadget>();
                    leftFlipper.getGadgetsToTrigger();
                    gadgetTriggeredList.add(newTriggeredGadg); //add triggered Gadget to its list of gadgets to be triggered 
                    Flipper newLeftFlipper = new Flipper(leftFlipper.getName(),leftFlipper.getPosition(),leftFlipper.getOrientation(),leftFlipper.getRotation(),true,gadgetTriggeredList);
                    leftFlippersNew.add(newLeftFlipper);
                } else { 
                    leftFlippersNew.add(leftFlipper);
                }
            } else { 
                leftFlippersNew.add(leftFlipper);
            }
        }for (Absorber absorber: absorberNameMap.values()){
            String absorberName = absorber.getName();
            if (fireTriggerMap.keySet().contains(absorberName)){ //check if key set contains name of gadget:
                String nameOfGadgetToBeTriggered=fireTriggerMap.get(absorberName);    
                Gadget newTriggeredGadg;
                if (gadgetNameMap.keySet().contains(nameOfGadgetToBeTriggered)){//check if list of gadgets contains this gadget{
                    newTriggeredGadg = gadgetNameMap.get(nameOfGadgetToBeTriggered);
                    List<Gadget> gadgetTriggeredList = new ArrayList<Gadget>();
                    absorber.getGadgetsToTrigger();
                    gadgetTriggeredList.add(newTriggeredGadg); //add triggered Gadget to its list of gadgets to be triggered 
                    Absorber newAbs = new Absorber(absorber.getName(),absorber.getPosition(),absorber.getWidth(),absorber.getHeight(),gadgetTriggeredList);
                    absorbersNew.add(newAbs);
                } else { 
                    absorbersNew.add(absorber);
                }
            } else { 
                absorbersNew.add(absorber);
            }
        }for (Portal portal: portalNameMap.values()){
            String portalName = portal.getName();
            if (fireTriggerMap.keySet().contains(portalName)){ //check if key set contains name of gadget:
                String nameOfGadgetToBeTriggered=fireTriggerMap.get(portalName);    
                Gadget newTriggeredGadg;
                if (gadgetNameMap.keySet().contains(nameOfGadgetToBeTriggered)){//check if list of gadgets contains this gadget{
                    newTriggeredGadg = gadgetNameMap.get(nameOfGadgetToBeTriggered);
                    List<Gadget> gadgetTriggeredList = new ArrayList<Gadget>();
                    portal.getGadgetsToTrigger();
                    gadgetTriggeredList.add(newTriggeredGadg); //add triggered Gadget to its list of gadgets to be triggered 
                    Portal newPortal = new Portal(portal.getName(),portal.getPosition(),portal.otherBoard(),portal.otherPortal());
                    portalsNew.add(newPortal);
                } else { 
                    portalsNew.add(portal);
                } 
            }else { 
                    portalsNew.add(portal);
                }       
            }
      
            boardMap.put("circle_bumper",circleBumpersNew);
            boardMap.put("triangle_bumper",triangleBumpersNew);      
            boardMap.put("square_bumper",squareBumpersNew);
            boardMap.put("right_flipper",rightFlippersNew);
            boardMap.put("left_flipper",leftFlippersNew);
            boardMap.put("absorber",absorbersNew);
            boardMap.put("portal",portalsNew);
            return boardMap; 
        }
 

    /**
     * Constructor method. 
     * Creates a board with the appropriate gadgets and balls. 
     * @return boardNew 
     *              a board whose gadgets correspond to those specified in a board file. 
     */
    public Board makeBoard(){
        HashMap<String, List<Gadget>> boardMap = makeTriggeredBoardMap(); //map of gadget name and gadget type 
        List<Gadget> boardGadgets = new ArrayList<Gadget>();
        ArrayList<Ball> ballList = new ArrayList<Ball>();
       
        boardGadgets.addAll((List<Gadget>)(Object)(boardMap.get("circle_bumper")));
        boardGadgets.addAll((List<Gadget>)(Object)(boardMap.get("triangle_bumper")));
        boardGadgets.addAll((List<Gadget>)(Object)(boardMap.get("square_bumper")));
        boardGadgets.addAll((List<Gadget>)(Object)(boardMap.get("circle_bumper")));
        boardGadgets.addAll((List<Gadget>)(Object)(boardMap.get("left_flipper")));
        boardGadgets.addAll((List<Gadget>)(Object)(boardMap.get("right_flipper")));
        boardGadgets.addAll((List<Gadget>)(Object)(boardMap.get("portal")));
        boardGadgets.addAll((List<Gadget>)(Object)(boardMap.get("absorber")));

        Double gravity = boardParameters.get(0);
        Double friction = boardParameters.get(1);
        Double drag = boardParameters.get(2); 
        String newBoardName = listOfCreatedBoardNames.get(0); 

        Board  boardNew = new Board(newBoardName,gravity,friction,drag,boardGadgets);
        for (Ball ball: balls){
            boardNew.addBall(ball.getPosition(),ball.getName());
        }
       // boardNew.drawBoard();
        boardParameters.clear(); //clear boardParameters so we don't reuse them for next board
        listOfCreatedBoardNames.remove(0); 
        balls.clear(); 
        circleBumpersNew.clear();
        triangleBumpersNew.clear();
        absorbersNew.clear();
        rightFlippersNew.clear();
        leftFlippersNew.clear();
        squareBumpersNew.clear();
        portalsNew.clear();      
        return boardNew; 
    }
    
/** 
 * Method using ANTLR parse to define a gadget from board file
 * 
 */
    @Override public void exitGadget_define(@NotNull boardGrammarParser.Gadget_defineContext ctx) { 
        String Name = ctx.name().NAMEX().getText(); 
        String type = ctx.type().getText(); 
        if (type.equals("squareBumper")){

            Integer xInt = Integer.valueOf(ctx.position().xPos().numvalue().getText());
            Integer yInt = Integer.valueOf(ctx.position().xPos().numvalue().getText());
            Vect position = new Vect(xInt,yInt); 
            String squareBumperName = ctx.name().NAMEX().getText(); 

            List gadgetsToTrigger = new ArrayList<Gadget>();
            SquareBumper newSquareBumper = new SquareBumper(squareBumperName,position,gadgetsToTrigger);
            squareBumperNameMap.put(squareBumperName, newSquareBumper); //puts into a map so we can see if it's triggered
            gadgetNameMap.put(squareBumperName,newSquareBumper);
            squareBumpers.add(newSquareBumper);
        } else if (type.equals("triangleBumper")){
            Integer xInt = Integer.valueOf(ctx.position().xPos().numvalue().getText());
            Integer yInt = Integer.valueOf(ctx.position().yPos().numvalue().getText());
            Vect position = new Vect(xInt,yInt); 
            String triangleBumperName = ctx.name().NAMEX().getText(); 
            int orientation;
            if (ctx.orientation() !=null){
                orientation =  Integer.valueOf(ctx.orientation().numvalue().getText());
            } else {
                orientation = 0; 
            }

            List gadgetsToTrigger = new ArrayList<Gadget>();
            TriangleBumper newTriangleBumper = new TriangleBumper(triangleBumperName, position, orientation, gadgetsToTrigger); 

            //add newly created triangle bumper to list of triangleBumpers to be put on board
            triangleBumpers.add(newTriangleBumper);
            triangleBumperNameMap.put(triangleBumperName, newTriangleBumper); //puts into a map so we can see if it's triggered
            gadgetNameMap.put(triangleBumperName,newTriangleBumper);

        } else if (type.equals("circleBumper")){
            Integer xInt = Integer.valueOf(ctx.position().xPos().numvalue().getText());
            Integer yInt = Integer.valueOf(ctx.position().yPos().numvalue().getText());
            Vect position = new Vect(xInt,yInt); 
            String circleBumperName = ctx.name().NAMEX().getText(); 

            List gadgetsToTrigger = new ArrayList<Gadget>();
            CircleBumper newCircleBumper = new CircleBumper(circleBumperName, position,gadgetsToTrigger); 
            circleBumpers.add(newCircleBumper); //add circleBumper to list of circleBumpers
            circleBumperNameMap.put(circleBumperName, newCircleBumper); 
            gadgetNameMap.put(circleBumperName, newCircleBumper);

        } else if (type.equals("absorber")){
            Integer xInt = Integer.valueOf(ctx.position().xPos().numvalue().getText());
            Integer yInt = Integer.valueOf(ctx.position().yPos().numvalue().getText());
            Vect position = new Vect(xInt,yInt); 
            String absorberName = ctx.name().NAMEX().getText(); 
            Integer width = Integer.valueOf(ctx.size().width().numvalue().getText());
            Integer height = Integer.valueOf(ctx.size().height().numvalue().getText());


            List gadgetsToTrigger = new ArrayList<Gadget>(); //will need to fill this in
            Absorber newAbsorber = new Absorber(absorberName,position, width, height,gadgetsToTrigger); 

            absorbers.add(newAbsorber);
            absorberNameMap.put(absorberName, newAbsorber); 
            gadgetNameMap.put(absorberName, newAbsorber);

        } else if (type.equals("leftFlipper")){
            Integer xInt = Integer.valueOf(ctx.position().xPos().numvalue().getText());
            Integer yInt = Integer.valueOf(ctx.position().yPos().numvalue().getText());
            Vect position = new Vect(xInt,yInt); 
            String leftFlipperName = ctx.name().NAMEX().getText(); 

            int orientation = Integer.valueOf(ctx.orientation().numvalue().getText());//orientation


            //initially set flipper rotation int speed to be 0.... not sure if this is right
            Flipper newLeftFlipper = new Flipper(leftFlipperName, position, orientation, 0, true); 

            leftFlippers.add(newLeftFlipper);
            leftFlipperNameMap.put(leftFlipperName, newLeftFlipper); 
            gadgetNameMap.put(leftFlipperName, newLeftFlipper);

        } else if (type.equals("rightFlipper")){
            Integer xInt = Integer.valueOf(ctx.position().xPos().numvalue().getText());
            Integer yInt = Integer.valueOf(ctx.position().yPos().numvalue().getText());
            Vect position = new Vect(xInt,yInt); 
            String rightFlipperName = ctx.name().NAMEX().getText(); 
            Integer orientation = Integer.valueOf(ctx.orientation().numvalue().getText()); 


            //initially set flipper rotation int speed to be 0.... not sure if this is right
            Flipper newRightFlipper = new Flipper(rightFlipperName, position, orientation,0, false); 

            rightFlippers.add(newRightFlipper);
            rightFlipperNameMap.put(rightFlipperName, newRightFlipper); 
            gadgetNameMap.put(rightFlipperName, newRightFlipper);

        }else if (type.equals("ball")){
            Double xInt = Double.valueOf(ctx.position().xPos().numvalue().getText());
            Double yInt = Double.valueOf(ctx.position().yPos().numvalue().getText());
            Vect position = new Vect(xInt,yInt); 
            Double xvel = Double.valueOf(ctx.speed().numvalue(0).getText());
            Double yvel = Double.valueOf(ctx.speed().numvalue(1).getText());
            Vect speed = new Vect(xvel,yvel); 
            String ballName = ctx.name().NAMEX().getText(); 
            Double boardGravity = boardParameters.get(0); 
            Double boardFriction1 = boardParameters.get(1); 
            Double boardFriction2 = boardParameters.get(2); 
            Ball newBall = new Ball(position,boardGravity,boardFriction1,boardFriction2,ballName); 

            ballNameMap.put(newBall,ballName); 
            balls.add(newBall);

        } else if (type.equals("portal")){
            Integer xInt = Integer.valueOf(ctx.position().xPos().numvalue().getText());
            Integer yInt = Integer.valueOf(ctx.position().yPos().numvalue().getText());
            Vect position = new Vect(xInt,yInt); 
            String otherPortalName;
            String otherBoardName; 
            if ((ctx.otherboard() !=null)){
                otherBoardName = ctx.otherboard().NAMEX().getText(); 
            } else {
                otherBoardName = "";
            }  if ((ctx.otherportal() !=null)){   
                otherPortalName = ctx.otherportal().NAMEX().getText();
            } else {
                otherPortalName = ""; 
            } String portalName = ctx.name().NAMEX().getText();

            Portal newPortal = new Portal(portalName, position, otherBoardName, otherPortalName);
            portals.add(newPortal); 
            portalNameMap.put(portalName, newPortal); 
            gadgetNameMap.put(portalName, newPortal);
        }

    }


    //defines board as: board name=NAME gravity=FLOAT friction1=FLOAT friction2=FLOAT
    //note that friction2 = drag 
    @Override 
    public void exitDefine_board(@NotNull boardGrammarParser.Define_boardContext ctx) { 
        Double friction2;
        Double friction1;
        Double gravity;
        String boardName;
        if ((ctx.gravity() !=null)){
            gravity = Double.valueOf(ctx.gravity().numvalue().getText());
        } else {
            gravity = 25.0; 
        } 
        if ((ctx.friction1() !=null)){

            friction1 = Double.valueOf(ctx.friction1().numvalue().getText());
        }  else {
            friction1 = 0.025;
        }
        if ((ctx.friction2() !=null)){
            friction2 = Double.valueOf(ctx.friction2().numvalue().getText());
        }  else 
            friction2= 0.025;
        if (ctx.name().NAMEX() !=null){
            boardName = ctx.name().NAMEX().getText(); 

        }  else boardName = ""; 
        List<Gadget> gadgets = new ArrayList<Gadget>();

        Board newBoard = new Board(boardName, gravity, friction1, friction2, gadgets);
        boardParameters.add(gravity); 
        boardParameters.add(friction1);
        boardParameters.add(friction2); 
        listOfCreatedBoards.add(newBoard); 
        listOfCreatedBoardNames.add(boardName); 
    }

    List<Gadget>triangleBumperList = new ArrayList<Gadget>();

    Map<String, String> fireTriggerMap = new OrderedHashMap<String,  String>();
    Set<String> actionsToBeTriggered = new HashSet<String>();

    /** Parse board file lines defining events between gadgets */
    @Override 
    public void exitFire_trigger_action(@NotNull boardGrammarParser.Fire_trigger_actionContext ctx) { 
        
        String gadgetFiringTrigger = ctx.fire_trigger().NAMEX().getText(); //get name of gadget doing the triggering
        String gadgetToBeTriggered = ctx.action().NAMEX().getText(); //get name of gadget to be triggered        
        fireTriggerMap.put(gadgetFiringTrigger, gadgetToBeTriggered);
    }

   

}












