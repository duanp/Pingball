package pingball;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import physics.*;

/**
 * A class for simulating various boards, including the benchmark boards. All boards are 20L x 20L, and have standard physical constants.
 */
public class Simulator {
    
    /**
     * Simulates a Board with the following:
     * Ball @ (1.25,1.25) 
     */
    public static void simulateSandbox() throws InterruptedException{
        Ball b1 = new Ball(new Vect(-1,-20),3,18);
        SquareBumper s1 = new SquareBumper(0,15);
        SquareBumper s2 = new SquareBumper(1,15);
        SquareBumper s3 = new SquareBumper(2,15);
        SquareBumper s4 = new SquareBumper(3,15);
        ArrayList<Ball> balls = new ArrayList(Arrays.asList(b1));
        List<Gadget> gadgets = Arrays.asList(s1,s2,s3,s4);
        Board simulation = new Board(20,20,balls,gadgets);
        simulation.deploy(1000);
    }
    
    /**
     * Simulates a Board with the following:
     * Ball @ (1.25,1.25) 
     * Circle Bumper @ (1,10) 
     * Triangle Bumper @ (12,15), Orientation: 180 degrees
     * Square Bumper @ (0,17) 
     * Square Bumper @ (1,17) 
     * Square Bumper @ (2,17) 
     * Circle Bumper @ (7,18) 
     * Circle Bumper @ (8,18) 
     * Circle Bumper @ (9,18)
     */
    public static void simulateDefault() throws InterruptedException {
        Ball b1 = new Ball(new Vect(0,0),1.0,1.0);
        CircleBumper c1 = new CircleBumper(1,10);
        TriangleBumper t1 = new TriangleBumper(12,15,Angle.DEG_180);
        SquareBumper s1 = new SquareBumper(0,17);
        SquareBumper s2 = new SquareBumper(1,17);
        SquareBumper s3 = new SquareBumper(2,17);
        CircleBumper c2 = new CircleBumper(7,18);
        CircleBumper c3 = new CircleBumper(8,18);
        CircleBumper c4 = new CircleBumper(9,18);
        ArrayList<Ball> balls = new ArrayList(Arrays.asList(b1));
        List<Gadget> gadgets = Arrays.asList(c1,c2,c3,c4,s1,s2,s3,t1);
        Board simulation = new Board(20,20,balls,gadgets);
        simulation.deploy(1000);
    }
    
    /**
     * Simulates a Board with the following:
     * Ball @ (10.25,15.25) 
     * Ball @ (19.25,3.25) 
     * Ball @ (1.25,5.25) 
     * Absorber @ (0,18), k = 20, m = 2 
     * Triangle Bumper @ (19,0), Orientation: 90 degrees
     * Circle Bumper @ (1,10), Triggered Gadgets: Absorber 
     * Circle Bumper @ (2,10), Triggered Gadgets: Absorber 
     * Circle Bumper @ (3,10), Triggered Gadgets: Absorber 
     * Circle Bumper @ (4,10), Triggered Gadgets: Absorber 
     * Circle Bumper @ (5,10), Triggered Gadgets: Absorber
     */
    public static void simulateAbsorber() throws InterruptedException {
        Ball b1 = new Ball(new Vect(0,0),10.25,15.25);
        Ball b2 = new Ball(new Vect(0,0),19.25,3.25);
        Ball b3 = new Ball(new Vect(0,0),1.25,5.25);
        Absorber a1 = new Absorber(0,18,20,2);
        TriangleBumper t1 = new TriangleBumper(19,0,Angle.DEG_90);
        CircleBumper c1 = new CircleBumper(1,10, new ArrayList(Arrays.asList(a1)));
        CircleBumper c2 = new CircleBumper(2,10, new ArrayList(Arrays.asList(a1)));
        CircleBumper c3 = new CircleBumper(3,10, new ArrayList(Arrays.asList(a1)));
        CircleBumper c4 = new CircleBumper(4,10, new ArrayList(Arrays.asList(a1)));
        CircleBumper c5 = new CircleBumper(5,10, new ArrayList(Arrays.asList(a1)));
        ArrayList<Ball> balls = new ArrayList(Arrays.asList(b1,b2,b3));
        List<Gadget> gadgets = Arrays.asList(c1,c2,c3,c4,c5,t1,a1);
        Board simulation = new Board(20,20,balls,gadgets);
        simulation.deploy(1000);
    }
    
    /**
     * Simulates a Board with the following:
     * Ball @ (0.25,3.25) 
     * Ball @ (5.25,3.25) 
     * Ball @ (10.25,3.25) 
     * Ball @ (15.25,3.25) 
     * Ball @ (19.25,3.25) 
     * LeftFlipper1 @ (0,8), Orientation: 90 degrees
     * LeftFlipper2 @ (4,10), Orientation: 90 degrees
     * LeftFlipper3 @ (9,8), Orientation: 90 degrees
     * LeftFlipper4 @ (15,8), Orientation: 90 degrees
     * Circle Bumper @ (5,18) 
     * Circle Bumper @ (7,13) 
     * Circle Bumper @ (0,5), Triggered Gadgets: LeftFlipper1 
     * Circle Bumper @ (5,5) 
     * Circle Bumper @ (10,5), Triggered Gadgets: LeftFlipper3 
     * Circle Bumper @ (15,5), Triggered Gadgets: LeftFlipper4 
     * Triangle Bumper @ (19,0), Orientation: 90 degrees
     * Triangle Bumper @ (10,18), Orientation: 180 degrees
     * RightFlipper1 @ (2,15), Orientation: 0 degrees
     * RightFlipper2 @ (17,15), Orientation: 0 degrees
     * Absorber @ (0,19), k = 20, m = 1, Triggered Gadgets: RightFlipper1, RightFlipper2, Absorber
     */
    public static void simulateFlippers() throws InterruptedException {
        Ball b1 = new Ball(new Vect(0,0),0.25,3.25);
        Ball b2 = new Ball(new Vect(0,0),5.25,3.25);
        Ball b3 = new Ball(new Vect(0,0),10.25,3.25);
        Ball b4 = new Ball(new Vect(0,0),15.25,3.25);
        Ball b5 = new Ball(new Vect(0,0),19.25,3.25);
        Absorber a1 = new Absorber(0,19,20,1, new ArrayList(), true);
        Flipper l1 = new Flipper(0, 8, true, Angle.DEG_90);
        Flipper l2 = new Flipper(4, 10, true, Angle.DEG_90);
        Flipper l3 = new Flipper(9, 8, true, Angle.DEG_90);
        Flipper l4 = new Flipper(15, 8, true, Angle.DEG_90);      
        CircleBumper c1 = new CircleBumper(5,18);
        CircleBumper c2 = new CircleBumper(7,13);
        CircleBumper c3 = new CircleBumper(0,5, new ArrayList(Arrays.asList(l1)));
        CircleBumper c4 = new CircleBumper(5,5);
        CircleBumper c5 = new CircleBumper(10,5, new ArrayList(Arrays.asList(l3)));
        CircleBumper c6 = new CircleBumper(15,5, new ArrayList(Arrays.asList(l4)));
        TriangleBumper t1 = new TriangleBumper(19,0,Angle.DEG_90);
        TriangleBumper t2 = new TriangleBumper(10,18,Angle.DEG_180);
        Flipper r1 = new Flipper(2, 15, false, Angle.ZERO, new ArrayList(Arrays.asList(a1)));
        Flipper r2 = new Flipper(17, 15, false, Angle.ZERO, new ArrayList(Arrays.asList(a1)));      
       
        ArrayList<Ball> balls = new ArrayList(Arrays.asList(b1,b2,b3,b4,b5));
        List<Gadget> gadgets = Arrays.asList(l1,l2,l3,l4,c1,c2,c3,c4,c5,c6,t1,t2,r1,r2,a1);
        Board simulation = new Board(20,20,balls,gadgets);
        simulation.deploy(1000);
    }
    
    public static void main(String[] args) throws InterruptedException{
        simulateDefault();
        //simulateAbsorber();
        //simulateFlippers();       
        //simulateSandbox();
    }
}
