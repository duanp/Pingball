package pingball;

import physics.Vect;
import pingball.Gadgets.Absorber;
import pingball.Gadgets.CircleBumper;
import pingball.Gadgets.Flipper;
import pingball.Gadgets.Portal;
import pingball.Gadgets.SquareBumper;
import pingball.Gadgets.TriangleBumper;

/**
 * An immutable class to hold the different benchmark boards.
 *
 */
public class BenchmarkBoards {
    
    //default benchmark board
    static Board DEFAULT = new Board("Default");
    static{
        DEFAULT.addBall(new Ball("BallA", new Vect(1.25, 1.25), Vect.ZERO));
        DEFAULT.addGadget(new TriangleBumper("Tri", 12, 15, 180));
        DEFAULT.addGadget(new SquareBumper("SquareA", 0, 17));
        DEFAULT.addGadget(new SquareBumper("SquareB", 1, 17));
        DEFAULT.addGadget(new SquareBumper("SquareC", 2, 17));
        DEFAULT.addGadget(new CircleBumper("CircleA", 1, 10));
        DEFAULT.addGadget(new CircleBumper("CircleB", 7, 18));
        DEFAULT.addGadget(new CircleBumper("CircleC", 8, 18));
        DEFAULT.addGadget(new CircleBumper("CircleD", 9, 18));
    }
    
    //absorber benchmark board
    static Board ABSORBER = new Board("Absorber");
    static{
        ABSORBER.addBall(new Ball("BallA", new Vect(10.25,15.25), Vect.ZERO));
        ABSORBER.addBall(new Ball("BallB", new Vect(19.25,3.25), Vect.ZERO));
        ABSORBER.addBall(new Ball("BallC", new Vect(1.25,5.25), Vect.ZERO));
        Absorber absorber0 = new Absorber("Abs", 0,18,20,2);
        ABSORBER.addGadget(absorber0); //gadget 0
        ABSORBER.addGadget(new TriangleBumper("Tri", 19,0,90)); //gadget 1
        CircleBumper circleBumper2 = new CircleBumper("CircleA", 1,10);
        circleBumper2.addGadgetToTrigger(absorber0);
        ABSORBER.addGadget(circleBumper2); //gadget 2
        CircleBumper circleBumper3 = new CircleBumper("CircleB", 2,10);
        circleBumper3.addGadgetToTrigger(absorber0);
        ABSORBER.addGadget(circleBumper3); //gadget 3
        CircleBumper circleBumper4 = new CircleBumper("CircleC", 3,10);
        circleBumper4.addGadgetToTrigger(absorber0);
        ABSORBER.addGadget(circleBumper4); //gadget 4
        CircleBumper circleBumper5 = new CircleBumper("CircleD", 4,10);
        circleBumper5.addGadgetToTrigger(absorber0);
        ABSORBER.addGadget(circleBumper5); //gadget 5
        CircleBumper circleBumper6 = new CircleBumper("CircleE", 5,10);
        circleBumper6.addGadgetToTrigger(absorber0);
        ABSORBER.addGadget(circleBumper6); //gadget 6
    }
    
    
    
    static Board PORTAL = new Board();
    static{ 
        PORTAL.addBall(new Ball(new Vect(5.25,3.25), Vect.ZERO));
        Portal onePortal = new Portal("mynameis",5,8,"mynameis","anotherboard");
        PORTAL.addGadget(onePortal);
    }
    
    static Board TRANSPARENT = new Board();
    static{
        //TRANSPARENT.addBall(new Ball(new Vect(5.25,3.25), Vect.ZERO));
        TRANSPARENT.addBall(new Ball(new Vect(5.25,3.25), new Vect(10, 10)));
        TRANSPARENT.addBall(new Ball(new Vect(3.25,1.25), new Vect(10, 10)));
        // TRANSPARENT.makeWallTransparent("bottom");
        // TRANSPARENT.makeWallTransparent("top");
        
    }
    
    static Board SIDEWAYS = new Board("s");
    static{ 
        SIDEWAYS.addBall(new Ball(new Vect(4.25,5.25), new Vect(5.0,5.0)));
        //SIDEWAYS.makeWallTransparent("top"); 
        //SIDEWAYS.makeWallTransparent("left");
        //SIDEWAYS.makeWallTransparent("right");
    }
    
    //flipper benchmark board
    static Board FLIPPERS = new Board("Flippers");
    static{ 
        FLIPPERS.addBall(new Ball("BallA", new Vect(0.25,3.25), Vect.ZERO));
        FLIPPERS.addBall(new Ball("BallB", new Vect(5.25,3.25), Vect.ZERO));
        FLIPPERS.addBall(new Ball("BallC", new Vect(10.25,3.25), Vect.ZERO));
        FLIPPERS.addBall(new Ball("BallD", new Vect(15.25,3.25), Vect.ZERO));
        FLIPPERS.addBall(new Ball("BallE", new Vect(19.25,3.25), Vect.ZERO));
        //LeftFlipper1 @ (0,8), Orientation: 0°, Rotation: 90° 
        //LeftFlipper2 @ (4,10), Orientation: 0°, Rotation: 90° 
        //LeftFlipper3 @ (9,8), Orientation: 0°, Rotation: 90° 
        //LeftFlipper4 @ (15,8), Orientation: 0°, Rotation: 90°
        Flipper leftFlipper1 = new Flipper("FlipA", 0, 8, 90, false, true);
        Flipper leftFlipper2 = new Flipper("FlipB", 4, 10, 90, false, true);
        Flipper leftFlipper3 = new Flipper("FlipC", 9, 8, 90, false, true);
        Flipper leftFlipper4 = new Flipper("FlipD", 15, 8, 90, false, true);
        FLIPPERS.addGadget(leftFlipper1);
        FLIPPERS.addGadget(leftFlipper2);
        FLIPPERS.addGadget(leftFlipper3);
        FLIPPERS.addGadget(leftFlipper4);
        //Circle Bumper @ (5,18) 
        //Circle Bumper @ (7,13) 
        //Circle Bumper @ (0,5), Triggered Gadgets: LeftFlipper1 
        //Circle Bumper @ (5,5) 
        //Circle Bumper @ (10,5), Triggered Gadgets: LeftFlipper3 
        //Circle Bumper @ (15,5), Triggered Gadgets: LeftFlipper4 
        CircleBumper circleBumper1 = new CircleBumper("CircleA", 5, 18);
        CircleBumper circleBumper2 = new CircleBumper("CircleB", 7, 13);
        CircleBumper circleBumper3 = new CircleBumper("CircleC", 0, 5);
        circleBumper3.addGadgetToTrigger(leftFlipper1);
        CircleBumper circleBumper4 = new CircleBumper("CircleD", 5, 5);
        CircleBumper circleBumper5 = new CircleBumper("CircleE", 10, 5);
        circleBumper5.addGadgetToTrigger(leftFlipper3);
        CircleBumper circleBumper6 = new CircleBumper("CircleF", 15, 5);
        circleBumper6.addGadgetToTrigger(leftFlipper4);
        FLIPPERS.addGadget(circleBumper1);
        FLIPPERS.addGadget(circleBumper2);
        FLIPPERS.addGadget(circleBumper3);
        FLIPPERS.addGadget(circleBumper4);
        FLIPPERS.addGadget(circleBumper5);
        FLIPPERS.addGadget(circleBumper6);
        //Triangle Bumper @ (19,0), Orientation: 90° 
        //Triangle Bumper @ (10,18), Orientation: 180°
        FLIPPERS.addGadget(new TriangleBumper("TriA", 19, 0, 90));
        FLIPPERS.addGadget(new TriangleBumper("TriB", 10, 18, 180));
        //RightFlipper1 @ (2,15), Orientation: 0°, Rotation 0° 
        //RightFlipper2 @ (17,15), Orientation: 0°, Rotation 0°
        Flipper rightFlipper1 = new Flipper("FlipE", 2, 15, 0, false, false);
        Flipper rightFlipper2 = new Flipper("FlipF", 17, 15, 0, false, false);
        FLIPPERS.addGadget(rightFlipper1);
        FLIPPERS.addGadget(rightFlipper2);
        //Absorber @ (0,19), k = 20, m = 1, Triggered Gadgets: RightFlipper1, RightFlipper2, Absorber
        Absorber absorber = new Absorber("Abs", 0, 19, 20, 1);
        FLIPPERS.addGadget(absorber);
        absorber.addGadgetToTrigger(rightFlipper1);
        absorber.addGadgetToTrigger(rightFlipper2);
        absorber.addGadgetToTrigger(absorber);
    }
}
