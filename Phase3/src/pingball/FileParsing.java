package pingball;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import physics.Vect;
import pingball.Gadgets.Absorber;
import pingball.Gadgets.CircleBumper;
import pingball.Gadgets.Flipper;
import pingball.Gadgets.Gadget;
import pingball.Gadgets.Portal;
import pingball.Gadgets.SquareBumper;
import pingball.Gadgets.TriangleBumper;


/**
 * An immutable class with helper methods to parse Board files
 */
public class FileParsing {
    
    // Abstraction function:
    //   it does not represent a specific structure, but provides
    //   helper methods to parse board files
    //
    // Rep invariant:
    //  immutable
    //
    
    // default board gravity
    private static final double DEFAULT_GRAVITY = 25;
    
    // default friction values
    private static double friction1 = .025;
    private static double friction2 = .025;
    
    // basic regexes for NAME, INTEGER, and FLOAT as in problem set
    private static final String NAME = "[A-Za-z_][A-Za-z_0-9]*";
    private static final String INTEGER = "[0-9]+"; 
    private static final String FLOAT = "(-?([0-9]+.[0-9]*|.?[0-9]+))";
    
    // generic regexes for other declarations
    private static final String name = "(\\s+name\\s*=\\s*" + NAME +")";
    private static final String onlyWhiteSpace = "(\\s*)";
    private static final String comment = "(#.*)";
    private static final String xInt = "(\\s+x\\s*=" + INTEGER + ")";
    private static final String yInt = "(\\s+y\\s*=" + INTEGER + ")";
    private static final String xDouble = "(\\s+x\\s*=" + FLOAT + ")";
    private static final String yDouble = "(\\s+y\\s*=" + FLOAT + ")";   

    // regex for board declaration
    private static final String regexGravity = "(\\s+gravity\\s*=\\s*" + FLOAT + ")?";
    private static final String regexFriction1 = "(\\s+friction1\\s*=\\s*" + FLOAT + ")?";
    private static final String regexFriction2 = "(\\s+friction2\\s*=\\s*" + FLOAT + ")?";
    private static final String regexBoard = "(\\s*board)" + name + regexGravity + regexFriction1 + 
            regexFriction2 + onlyWhiteSpace;
    
    // regex for comment lines
    private static final String regexComment = onlyWhiteSpace + comment;
    
    // regex for ball declaration
    private static final String xVelocity = "(\\s+xVelocity\\s*=\\s*" + FLOAT + ")";
    private static final String yVelocity = "(\\s+yVelocity\\s*=\\s*" + FLOAT + ")";   
    private static final String regexBall = "(\\s*ball)" + name + xDouble + yDouble + xVelocity 
            + yVelocity + onlyWhiteSpace;
    
    // regex for gadget declarations
    private static final String regexGadgetType = "(\\s*squareBumper|\\s*circleBumper|\\s*triangleBumper|"
            + "\\s*rightFlipper|\\s*leftFlipper)";
    private static final String orientation = "(\\s+orientation\\s*=\\s*(0|90|180|270))?";
    private static final String regexGadget = regexGadgetType + name + xInt + yInt + orientation + onlyWhiteSpace;
    
    // regex for absorber declaration
    private static final String widthInt = "(\\s+width\\s*=\\s*" + INTEGER + ")";
    private static final String heightInt = "(\\s+height\\s*=\\s*" + INTEGER + ")";
    private static final String regexAbsorber = "(\\s*absorber)" + name + xInt + yInt + widthInt + 
            heightInt + onlyWhiteSpace;
    
    // regex for fire declarations
    private static final String trigger = "(\\s+trigger\\s*=\\s*" + NAME + ")";
    private static final String action = "(\\s+action\\s*=\\s*" + NAME +  ")";
    private static final String regexFire = "(\\s*fire)" + trigger + action + onlyWhiteSpace;

    // regex for portal declarations
    private static final String otherPortal =  "(\\s+otherPortal\\s*=\\s*" + NAME + ")?";
    private static final String otherBoard =  "(\\s+otherBoard\\s*=\\s*" + NAME + ")?";
    private static final String regexPortal = "(\\s*portal)" + name + xInt + yInt + otherBoard + 
            otherPortal + onlyWhiteSpace;
    
    
    // regex for key declarations
    /* keyup key=KEY action=NAME
     * keydown key=KEY action=NAME
     * KEY ::=   [a-z] 
        | [0-9]
        | shift | ctrl | alt | meta
        | space
        | left | right | up | down
        | minus | equals | backspace
        | openbracket | closebracket | backslash
        | semicolon | quote | enter
        | comma | period | slash
     */
    private static final String regexKEY = "(\\s+key=\\s*([a-z]|[0-9]|shift|ctrl|alt|meta|space|"
            + "left|right|up|down|minus|equals|backspace|openbracket|closebracket|"
            + "backslash|semicolon|quote|enter|comma|period|slash))";
    private static final String regexKeyDeclaration = "(\\s*(keyup|keydown))" + regexKEY + action + onlyWhiteSpace;
    
    // words to access elements of parsed line
    private static final String nameWord = "name";
    private static final String xWord = "x";
    private static final String yWord = "y";
    private static final String orientationWord = "orientation";
    private static final String otherBoardWord = "otherBoard";
    private static final String otherPortalWord = "otherPortal";
    private static final String actionWord = "action";
    private static final String triggerWord = "trigger";
    private static final String heightWord = "height";
    private static final String widthWord = "width";
    private static final String keyWord = "key";
    private static final String xVelocityWord = "xVelocity";
    private static final String yVelocityWord = "yVelocity";
    
    
    
    
    /**
     * Helper method to get list of matched regex group in a line.
     * @param line
     * @param regex
     * @return list of factors in a single declaration in board file
     */
    private  static List<String> getMatchedGroups(String line, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(line);

        List<String> matchedGroups = new ArrayList<String>();
        while (m.find()) { 
            // the first element is always the original string, so ignore it
            for (int i = 1; i <= m.groupCount(); i++) { 
                String group = m.group(i);
                // ignore unmatched and empty spaces
                if (group != null && !group.matches("\\s*")) {
                    matchedGroups.add(group);
                }
            }
        }
        return matchedGroups;
    }
    
    /**
     * Helper method to get attribute-value pairs for board and gadgets
     * declared in the board files.
     * @param groups
     * @return map of attribute -> value for declarations in board file
     */
    private static Map<String, String> getPairsFromLine(String line, String regex) {
        List<String> groups = getMatchedGroups(line, regex);
        Map<String, String> pairs = new HashMap<String, String>();
        // first element is always the type of element
        for (int i = 1; i < groups.size(); i++) {
            String declaration = groups.get(i).replaceAll("\\s+",""); // remove superfluous whitespace
            String[] pair = declaration.split("=");
            if (pair.length == 2) {
                pairs.put(pair[0], pair[1]);
            }
        }
        return pairs;
    }
    
    /**
     * Creates a Board as specified in a file
     * @param filename path to file
     * @return instance of Board according to file contents
     * @throws IOException
     */
    public static Board createBoardFromFile(File file) throws IOException {
        // track names of all elements to ensure no repeated names
        Set<String> addedNames = new HashSet<String>();
        
        // track gadgets that have been created in order to add triggers
        Map<String, Gadget> addedGadgets= new HashMap<String, Gadget>();
        
        // ensure that board declaration is first line & occurs only once
        boolean boardIsDeclared = false;
        
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        Board board = new Board(); // placeholder
        while ((line = br.readLine()) != null) {
            if (line.matches(regexBoard)) {
                board = parseBoardRegex(line, boardIsDeclared, addedNames);
                boardIsDeclared = true;
            }
            else if (line.matches(regexComment) || line.matches(onlyWhiteSpace)) {
                continue; // ignore comment lines and empty lines
            }
            else if (line.matches(regexBall)) {
                checkBoardIsDeclared(boardIsDeclared);
                parseBallRegex(board, line, addedNames);   
            }
            else if (line.matches(regexGadget)) {
                checkBoardIsDeclared(boardIsDeclared);
                parseGadgetRegex(board, line, addedGadgets, addedNames);
            }
            else if (line.matches(regexAbsorber)) {
                checkBoardIsDeclared(boardIsDeclared);
                parseAbsorberRegex(board, line, addedGadgets, addedNames);
            }
            else if (line.matches(regexFire)) {
                checkBoardIsDeclared(boardIsDeclared);
                Map<String, String> pairs = getPairsFromLine(line, regexFire);
                addedGadgets.get(pairs.get(triggerWord)).addGadgetToTrigger(
                        addedGadgets.get(pairs.get(actionWord)));
            }
            else if (line.matches(regexPortal)) {
                checkBoardIsDeclared(boardIsDeclared);
                parsePortalRegex(board, line, addedGadgets, addedNames, board.getName());
            }
            else if (line.matches(regexKeyDeclaration)) {
                checkBoardIsDeclared(boardIsDeclared);
                parseKeyRegex(board, line, addedGadgets);
            }
            else {
                br.close();
                throw new IOException("The file is not properly formatted.");
            }
        }
        br.close();
        return board;
    }
    
    /**
     * Checks that the board is already declared and throws an exception if not.
     * The file format requires that the first declaration in file is 
     * board declaration.
     * @param boardIsDeclared whether or not the board is already declared
     * @throws IOException if baord not already declared
     */
    private static void checkBoardIsDeclared(boolean boardIsDeclared) throws IOException {
        if (!boardIsDeclared) {
            throw new IOException("Board must be declared first in the file");
        }
    }
    
    /**
     * Helper method to create a Board from a board declaration line
     * @param line the line in which the Board is declared
     * @return an instance of Board
     * @throws IOException 
     */
    private static Board parseBoardRegex(String line, boolean isDeclared, 
            Set<String> addedNames) throws IOException {
        if (isDeclared) {
            throw new IOException("There can only be one board "
                    + "declaration line in a board file");
        }
        
        Map<String, String> pairs = getPairsFromLine(line, regexBoard);
        double gravity = DEFAULT_GRAVITY;
        String gravityWord = "gravity";
        if (pairs.containsKey(gravityWord)) {
            gravity = Double.parseDouble(pairs.get(gravityWord));
        } 
        String friction1word = "friction1";
        // friction constants are used when initializing ball
        if (pairs.containsKey(friction1word)) {
            friction1 = Double.parseDouble(pairs.get(friction1word));
        } 
        String friction2word = "friction2";
        if (pairs.containsKey(friction2word)) {
            friction2 = Double.parseDouble(pairs.get(friction2word));
        }

        String name = pairs.get(nameWord);
        addedNames.add(name);
        Board board = new Board(name, gravity, friction1, friction2);
        return board;
    }
    
    /**
     * Helper method to add a ball that is declared to the new Board
     * @param board the new Board that is created in the board file
     * @param line the line in which the ball is declared
     * @throws IOException 
     */
    private static void parseBallRegex(Board board, String line,  Set<String> addedNames) throws IOException {
        Map<String, String> pairs = getPairsFromLine(line, regexBall);
        String ballName = pairs.get(nameWord);
        
        // ensure that all added elements have unique names
        if (addedNames.contains(ballName)) {
            throw new IOException("Names in a board file must be unique");
        }
        addedNames.add(ballName);

        Vect position = new Vect(Double.parseDouble(pairs.get(xWord)), 
                                   Double.parseDouble(pairs.get(yWord)));
        Vect velocity = new Vect(Double.parseDouble(pairs.get(xVelocityWord)), 
                Double.parseDouble(pairs.get(yVelocityWord)));
        Ball newBall = new Ball(ballName, position, velocity);
        board.addBall(newBall);
    }
    
    /**
     * Helper method to add a gadget that is declared to the new Board
     * @param board the new Board that is created in the board file
     * @param line the line in which the gadget is declared
     * @throws IOException 
     */
    private static void parseGadgetRegex(Board board, String line, 
            Map<String, Gadget> addedGadgets,  Set<String> addedNames) throws IOException {
        List<String> groups = getMatchedGroups(line, regexGadget);
        Map<String, String> pairs = getPairsFromLine(line, regexGadget);;
        // get constructor arguments
        String gadgetName = pairs.get(nameWord);
        
        // ensure that all added elements have unique names
        if (addedNames.contains(gadgetName)) {
            throw new IOException("Names in a board file must be unique");
        }

        addedNames.add(gadgetName);

        int gadgetX = Integer.parseInt(pairs.get(xWord));
        int gadgetY = Integer.parseInt(pairs.get(yWord));
        int gadgetOrientation = 0;
        
        if (pairs.containsKey(orientationWord)) {
            gadgetOrientation = Integer.parseInt(pairs.get(orientationWord));
        }
        
        // check which gadget to create
        String gadgetType = groups.get(0).replaceAll("\\s+","");
        Gadget newGadget;
        switch (gadgetType) {
        case "squareBumper":
            newGadget = new SquareBumper(gadgetName, gadgetX, gadgetY);
            board.addGadget(newGadget);
            addedGadgets.put(gadgetName, newGadget);
            break;
        case "circleBumper":
            newGadget = new CircleBumper(gadgetName, gadgetX, gadgetY);
            board.addGadget(newGadget);
            addedGadgets.put(gadgetName, newGadget);
            break;
        case "triangleBumper":
            newGadget = new TriangleBumper(gadgetName, gadgetX, gadgetY, gadgetOrientation);
            board.addGadget(newGadget);
            addedGadgets.put(gadgetName, newGadget);
            break;
        case "rightFlipper":
            newGadget = new Flipper(gadgetName, gadgetX, gadgetY, gadgetOrientation, false, false);
            addedGadgets.put(gadgetName, newGadget);
            board.addGadget(newGadget);
            break;
        case "leftFlipper":
            newGadget = new Flipper(gadgetName, gadgetX, gadgetY, gadgetOrientation, false, true);
            board.addGadget(newGadget);
            addedGadgets.put(gadgetName, newGadget);
            break;
        }
    }
    
    /**
     * Helper method to add an absorber that is declared to the new Board
     * @param board the new Board that is created in the board file
     * @param line the line in which the absorber is declared
     * @throws IOException 
     */
    private static void parseAbsorberRegex(Board board, String line, 
            Map<String, Gadget> addedGadgets,  Set<String> addedNames) throws IOException {
        Map<String, String> pairs = getPairsFromLine(line, regexAbsorber);
        String name = pairs.get(nameWord);
        
        // ensure that all added elements have unique names
        if (addedNames.contains(name)) {
            throw new IOException("Names in a board file must be unique");
        }
        addedNames.add(name);
        Absorber newAbsorber = new Absorber(name, 
                Integer.parseInt(pairs.get(xWord)),
                Integer.parseInt(pairs.get(yWord)),
                Integer.parseInt(pairs.get(widthWord)),
                Integer.parseInt(pairs.get(heightWord)));
        board.addGadget(newAbsorber);
        addedGadgets.put(name, newAbsorber);
    }
    
    /**
     * Helper method to add a portal that is declared to the new Board
     * @param board the new Board that is created in the board file
     * @param line the line in which the portal is declared
     * @throws IOException 
     */
    private static void parsePortalRegex(Board board, String line, Map<String, 
            Gadget> addedGadgets,  Set<String> addedNames, String residentBoardName) throws IOException {
        Map<String, String> pairs = getPairsFromLine(line, regexPortal);
        String name = pairs.get(nameWord);

        // ensure that all added elements have unique names
        if (addedNames.contains(name)) {
            throw new IOException("Names in a board file must be unique");
        }
        addedNames.add(name);
        Portal newPortal;
        if (pairs.containsKey(otherBoardWord)) {
             newPortal = new Portal (name,
                    Integer.parseInt(pairs.get(xWord)),
                    Integer.parseInt(pairs.get(yWord)),
                    pairs.get(otherPortalWord),
                    pairs.get(otherBoardWord));
        }
        else {
            newPortal = new Portal (name,
                    Integer.parseInt(pairs.get(xWord)),
                    Integer.parseInt(pairs.get(yWord)),
                    pairs.get(otherPortalWord),
                    residentBoardName);
        }
        board.addGadget(newPortal);
        addedGadgets.put(name, newPortal);
    }
    
    /**
     * Helper method to add a key trigger to the board.
     * @param board the new Board that is created in the board file
     * @param line the line in which the key trigger is declared
     */
    private static void parseKeyRegex(Board board, String line, Map<String, 
            Gadget> addedGadgets) {
        Map<String, String> pairs = getPairsFromLine(line, regexKeyDeclaration);
        String key = pairs.get(keyWord);
        Gadget gadget = addedGadgets.get(pairs.get(actionWord));
        String keyType = line.split(" ")[0].replace(" ", "");
        board.addKeyTrigger(key, gadget, keyType.equals("keyup"));
    }
    
}







