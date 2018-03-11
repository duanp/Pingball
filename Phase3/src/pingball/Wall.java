package pingball;

import physics.LineSegment;
import pingball.Gadgets.Gadget;


/**
 * A mutable class that represents a wall in the Pingball game.
 *
 */
public class Wall {
    // Abstraction function:
    //      represents a wall in the Pingball board
    // 
    // Rep invariant:
    //      it's type and shape never changes (maintained by field structure)
    //      getDisplayString is always a string of length 22
    //

    private final WallType type;
    private final LineSegment shape;
    
    private boolean transparent = false;
    private String otherBoard = "";
    
    /**
     * Constructs an instance of the wall of the specified WallType.
     * @param type what type of wall it is, can be WallType.TOP, 
     *      WallType.BOTTOM, WallType.LEFT, WallType.RIGHT
     */
    public Wall(pingball.WallType type) {
        this.type = type;
        
        // initialize shape
        if (type.equals(WallType.TOP)) {
            shape = new LineSegment(0,0,20,0);
        } else if (type.equals(WallType.BOTTOM)) {
            shape = new LineSegment(0,-20,20,-20);
            
        } else if (type.equals(WallType.LEFT)) {
            shape = new LineSegment(0,0,0,-20);
        } else {
            // type is WallType.RIGHT
            shape = new LineSegment(20,0,20,-20);
        }
        checkRep();
    }
    
    /**
     * Makes the wall transparent and connected to the board other.
     * @param other the board to connect the wall to
     */
    public void makeTransparent(String other) {
        transparent = true;
        otherBoard = other;
        checkRep();
    }
    
    /**
     * Makes the wall solid again and disconnects it from the 
     * other board.
     */
    public void makeSolid() {
        transparent = false;
        otherBoard = "";
        checkRep();
    }
    
    /**
     * Gets the physical shape of the wall.
     * @return the shape as represented in the physics library (LineSegment)
     */
    public LineSegment getShape() {
        return shape;
    }
    
    /**
     * Checks whether or not the wall is transparent
     * @return true if transparent
     */
    public boolean isTransparent() {
        return transparent;
    }
    
    /**
     * Gets the type of the wall
     * @return an instance of the WallType enum
     */
    public WallType getType() {
        return type;
    }
    
    /**
     * Gets the string representation of the wall
     * @return
     */
    public String getDisplayString() {
        String display = "";
        
        if (type.equals(WallType.TOP) || type.equals(WallType.BOTTOM)) {
            display += ".";
        }
        display += otherBoard;
        
        if (display.length() >= 22) {
            return display.substring(0,  22);
        }
        
        int startLength = display.length();
        int maxLength = 22;
        for (int i = 0; i < maxLength - startLength ; i++) {
            display += ".";
        }
        return display;
    }
    
    
    // implements observational equality
    @Override
    public boolean equals(Object other){
        if (other instanceof Wall) {
            Wall otherWall = (Wall) other;
            if (type.equals(otherWall.type) && shape.equals(otherWall.shape) &&
                    transparent == otherWall.transparent && 
                    otherBoard.equals(otherWall.otherBoard)) {
                        return true;
                    }
        }
        return false;
    }
    
    // asserts rep invariant
    private void checkRep() {
        assert getDisplayString().length() == 22;
    }
}




