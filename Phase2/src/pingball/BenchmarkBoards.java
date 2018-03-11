package pingball;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import physics.Vect;

/*
 * This class hard-codes the default, absorber, 
 * and flipper board given to us in the specs.
 * 
 * The following is how  the parameters of each 
 * gadget/item are interpreted before being added to the board:
 * 
 * ball: (Vect coordinates,List<Gadget> triggers)
 * circle_bumper: (Vect coordinates,List<Gadget> triggers)
 * triangle_bumper: (Vect coordinates, int orientation,List<Gadget> triggers)
 * square_bumper: (Vect coordinates,List<Gadget> triggers)
 * left_flipper: (Vect coordinates,int orientation,int rotation,List<Gadget> triggers)
 * right_flipper: (Vect coordinates,int orientation,int rotation,List<Gadget> triggers)
 * absorber: (Vect coordinates,int k,int m,List<Gadget> triggers)
 * 
 * where triggers can be any finite length
 */

public class BenchmarkBoards {
    final double gravity = 25;
    final double friction = .025;
    final double drag = .025;

    public HashMap<String, List<Object>> defaultBoard1(){
        HashMap<String, List<Object>> board = new HashMap<String, List<Object>>();

        //add balls
        List<Object> balls = new ArrayList<Object>();



        Ball ball1 = new Ball(new Vect(1.25,1.25),gravity,friction,drag, "ball1");
        ball1.setVelocity(new Vect(100.,10.));
        balls.add(ball1);
        balls.add(new Ball(new Vect(5.25,1.25),gravity,friction,drag, "ball2"));
        balls.add(new Ball(new Vect(0.25,1.25),gravity,friction,drag, "ball3"));
        balls.add(new Ball(new Vect(2.25,1.25),gravity,friction,drag, "ball4"));

        board.put("ball",balls);

        //add circle bumper
        List<Object> circleBumpers = new ArrayList<Object>();
        circleBumpers.add(new CircleBumper("",new Vect(1,10),Arrays.asList()));
        circleBumpers.add(new CircleBumper("",new Vect(7,18),Arrays.asList()));
        circleBumpers.add(new CircleBumper("",new Vect(8,18),Arrays.asList()));
        circleBumpers.add(new CircleBumper("",new Vect(9,18),Arrays.asList()));
        board.put("circle_bumper",circleBumpers);

        List<Object> portals = new ArrayList<Object>();
        portals.add(new Portal("portal1",new Vect(2,15), "portal2"));
        portals.add(new Portal("portal2",new Vect(3,17), "board1","portal1"));
        board.put("portals",portals);

        //add triangle bumper
        List<Object> triangleBumpers = new ArrayList<Object>();
        triangleBumpers.add(new TriangleBumper("",new Vect(12,15),180,Arrays.asList()));
        board.put("triangle_bumper",triangleBumpers);

        //add square bumper
        List<Object> squareBumpers = new ArrayList<Object>();
        squareBumpers.add(new SquareBumper("",new Vect(0,17),Arrays.asList()));
        squareBumpers.add(new SquareBumper("",new Vect(1,17),Arrays.asList()));
        squareBumpers.add(new SquareBumper("",new Vect(2,17),Arrays.asList()));

        board.put("square_bumper",squareBumpers);

        return board;
    }

    public HashMap<String, List<Object>> defaultBoard2(){
        HashMap<String, List<Object>> board = new HashMap<String, List<Object>>();

        //add balls
        List<Object> balls = new ArrayList<Object>();



        Ball ball1 = new Ball(new Vect(1.25,1.25),gravity,friction,drag, "ball5");
        ball1.setVelocity(new Vect(100.,10.));
        balls.add(ball1);
        balls.add(new Ball(new Vect(5.25,1.25),gravity,friction,drag, "ball6"));
        balls.add(new Ball(new Vect(0.25,1.25),gravity,friction,drag, "ball7"));
        balls.add(new Ball(new Vect(2.25,1.25),gravity,friction,drag, "ball8"));

        board.put("ball",balls);

        //add circle bumper
        List<Object> circleBumpers = new ArrayList<Object>();
        circleBumpers.add(new CircleBumper("",new Vect(1,10),Arrays.asList()));
        circleBumpers.add(new CircleBumper("",new Vect(7,18),Arrays.asList()));
        circleBumpers.add(new CircleBumper("",new Vect(8,18),Arrays.asList()));
        circleBumpers.add(new CircleBumper("",new Vect(9,18),Arrays.asList()));
        board.put("circle_bumper",circleBumpers);

        List<Object> portals = new ArrayList<Object>();
        portals.add(new Portal("portal3",new Vect(10,15), "board2","portal4"));
        portals.add(new Portal("portal4",new Vect(4,18), "board2","portal3"));
        board.put("portals",portals);

        //add triangle bumper
        List<Object> triangleBumpers = new ArrayList<Object>();
        triangleBumpers.add(new TriangleBumper("",new Vect(12,15),180,Arrays.asList()));
        board.put("triangle_bumper",triangleBumpers);

        //add square bumper
        List<Object> squareBumpers = new ArrayList<Object>();
        squareBumpers.add(new SquareBumper("",new Vect(0,17),Arrays.asList()));
        squareBumpers.add(new SquareBumper("",new Vect(1,17),Arrays.asList()));
        squareBumpers.add(new SquareBumper("",new Vect(2,17),Arrays.asList()));

        board.put("square_bumper",squareBumpers);

        Flipper leftFlipper1 = new Flipper("", new Vect(0,8),0,90,true, Arrays.asList());
        Flipper leftFlipper3 = new Flipper("", new Vect(9,8),0,90, true, Arrays.asList());
        Flipper leftFlipper4 = new Flipper("", new Vect(15,8),0,90, true, Arrays.asList());
        Flipper rightFlipper1 = new Flipper("", new Vect(2,15),0,0,false, Arrays.asList());
        Flipper rightFlipper2 = new Flipper("", new Vect(17,15),0,0,false, Arrays.asList());

        //add left flipper
        List<Object> leftFlippers = new ArrayList<Object>();

        leftFlippers.add(leftFlipper1);
        leftFlippers.add(new Flipper("", new Vect(4,10),0,90,true,Arrays.asList()));
        leftFlippers.add(leftFlipper3);
        leftFlippers.add(leftFlipper4);
        board.put("left_flipper",leftFlippers);

        //add right flipper
        List<Object> rightFlippers = new ArrayList<Object>();

        rightFlippers.add(rightFlipper1);
        rightFlippers.add(rightFlipper2);
        board.put("right_flipper",rightFlippers);

        return board;
    }

    public HashMap<String, List<Object>> defaultBoard3(){
        HashMap<String, List<Object>> board = new HashMap<String, List<Object>>();

        //add balls
        List<Object> balls = new ArrayList<Object>();



        Ball ball1 = new Ball(new Vect(1.25,1.25),gravity,friction,drag, "ball9");
        ball1.setVelocity(new Vect(100.,10.));
        balls.add(ball1);
        balls.add(new Ball(new Vect(5.25,1.25),gravity,friction,drag, "ball10"));
        balls.add(new Ball(new Vect(0.25,1.25),gravity,friction,drag, "ball11"));
        balls.add(new Ball(new Vect(2.25,1.25),gravity,friction,drag, "ball12"));

        board.put("ball",balls);

        //add circle bumper
        List<Object> circleBumpers = new ArrayList<Object>();
        circleBumpers.add(new CircleBumper("",new Vect(1,10),Arrays.asList()));
        circleBumpers.add(new CircleBumper("",new Vect(7,18),Arrays.asList()));
        circleBumpers.add(new CircleBumper("",new Vect(8,18),Arrays.asList()));
        circleBumpers.add(new CircleBumper("",new Vect(9,18),Arrays.asList()));
        board.put("circle_bumper",circleBumpers);

        List<Object> portals = new ArrayList<Object>();
        portals.add(new Portal("portal5",new Vect(2,15),"portal6"));
        portals.add(new Portal("portal6",new Vect(3,17), "board3","portal5"));
        board.put("portals",portals);

        //add triangle bumper
        List<Object> triangleBumpers = new ArrayList<Object>();
        triangleBumpers.add(new TriangleBumper("",new Vect(12,15),180,Arrays.asList()));
        board.put("triangle_bumper",triangleBumpers);

        //add square bumper
        List<Object> squareBumpers = new ArrayList<Object>();
        squareBumpers.add(new SquareBumper("",new Vect(0,17),Arrays.asList()));
        squareBumpers.add(new SquareBumper("",new Vect(1,17),Arrays.asList()));
        squareBumpers.add(new SquareBumper("",new Vect(2,17),Arrays.asList()));

        board.put("square_bumper",squareBumpers);

        return board;
    }
    /* public HashMap<String, List<Object>> absorberBoard(){
        HashMap<String, List<Object>> board = new HashMap<String, List<Object>>();

        Absorber absorber = new Absorber("absorber name",new Vect(0,18),20,2,Arrays.asList());

        //add balls
        List<Object> balls = new ArrayList<Object>();
        balls.add(new Ball("ball1",new Vect(10.25,15.25),gravity,friction,drag));
        balls.add(new Ball("ball2",new Vect(19.25,3.25),gravity,friction,drag));
        balls.add(new Ball("ball3",new Vect(1.25,5.25),gravity,friction,drag));
        board.put("ball",balls);

        //add circle bumper
        List<Object> circleBumpers = new ArrayList<Object>();
        circleBumpers.add(new CircleBumper(new Vect(1,10),Arrays.asList(absorber)));
        circleBumpers.add(new CircleBumper(new Vect(2,10),Arrays.asList(absorber)));
        circleBumpers.add(new CircleBumper(new Vect(3,10),Arrays.asList(absorber)));
        circleBumpers.add(new CircleBumper(new Vect(4,10),Arrays.asList(absorber)));
        circleBumpers.add(new CircleBumper(new Vect(5,10),Arrays.asList(absorber)));
        board.put("circle_bumper",circleBumpers);

        //add triangle bumper
        List<Object> triangleBumpers = new ArrayList<Object>();
        triangleBumpers.add(new TriangleBumper(new Vect(19,0),90,Arrays.asList()));
        board.put("triangle_bumper",triangleBumpers);

        board.put("absorber",Arrays.asList(absorber));

        return board;
    }

    public HashMap<String, List<Object>> flipperBoard(){
        HashMap<String, List<Object>> board = new HashMap<String, List<Object>>();

        Flipper leftFlipper1 = new Flipper(new Vect(0,8),0,90,true, Arrays.asList());
        Flipper leftFlipper3 = new Flipper(new Vect(9,8),0,90, true, Arrays.asList());
        Flipper leftFlipper4 = new Flipper(new Vect(15,8),0,90, true, Arrays.asList());
        Flipper rightFlipper1 = new Flipper(new Vect(2,15),0,0,false, Arrays.asList());
        Flipper rightFlipper2 = new Flipper(new Vect(17,15),0,0,false, Arrays.asList());

        Absorber absorber = new Absorber("absorber", new Vect(0,19),20,1,Arrays.asList());
        absorber = new Absorber("absorber", new Vect(0,19),20,1,Arrays.asList(rightFlipper1,rightFlipper2,absorber));

        //add balls
        List<Object> balls = new ArrayList<Object>();
        balls.add(new Ball("ball1", new Vect(0.25,3.25),gravity,friction,drag));
        balls.add(new Ball("ball2",new Vect(5.25,3.25),gravity,friction,drag));
        balls.add(new Ball("ball3",new Vect(10.25,3.25),gravity,friction,drag));
        balls.add(new Ball("ball4",new Vect(15.25,3.25),gravity,friction,drag));
        balls.add(new Ball("ball5",new Vect(19.25,3.25),gravity,friction,drag));
        board.put("ball",balls);

        //add left flipper
        List<Object> leftFlippers = new ArrayList<Object>();

        leftFlippers.add(leftFlipper1);
        leftFlippers.add(new Flipper(new Vect(4,10),0,90,true,Arrays.asList()));
        leftFlippers.add(leftFlipper3);
        leftFlippers.add(leftFlipper4);
        board.put("left_flipper",leftFlippers);

        //add right flipper
        List<Object> rightFlippers = new ArrayList<Object>();

        rightFlippers.add(rightFlipper1);
        rightFlippers.add(rightFlipper2);
        board.put("right_flipper",rightFlippers);

        //add circle bumper
        List<Object> circleBumpers = new ArrayList<Object>();
        circleBumpers.add(new CircleBumper(new Vect(5,18),Arrays.asList(absorber)));
        circleBumpers.add(new CircleBumper(new Vect(7,13),Arrays.asList(absorber)));
        circleBumpers.add(new CircleBumper(new Vect(0,5),Arrays.asList(leftFlipper1)));
        circleBumpers.add(new CircleBumper(new Vect(5,5),Arrays.asList(absorber)));
        circleBumpers.add(new CircleBumper(new Vect(10,5),Arrays.asList(leftFlipper3)));
        circleBumpers.add(new CircleBumper(new Vect(15,5),Arrays.asList(leftFlipper4)));
        board.put("circle_bumper",circleBumpers);

        //add triangle bumper
        List<Object> triangleBumpers = new ArrayList<Object>();
        triangleBumpers.add(new TriangleBumper(new Vect(19,0),90,Arrays.asList()));
        triangleBumpers.add(new TriangleBumper(new Vect(10,18),180,Arrays.asList()));
        board.put("triangle_bumper",triangleBumpers);

        board.put("absorber",Arrays.asList(absorber));

        return board;
    }*/

}
