board name=fastBallBoard gravity=10.0 friction1=0.030 friction2=0.030

# define a ball
ball name=BallA x=1.8 y=4.5 xVelocity=10.4 yVelocity=10.3 
ball name=BallB x=10.0 y=10.0 xVelocity=-3.4 yVelocity=-2.3 
ball name=fastBallA x=3.0 y=2.4 xVelocity=55.0 yVelocity=56.0
ball name=fastBallB x=7.1 y=7.2 xVelocity=67.0 yVelocity=62.0

# define some bumpers
squareBumper name=Square x=0 y=10
squareBumper name=SquareB x=1 y=10
squareBumper name=SquareC x=2 y=10
squareBumper name=SquareD x=3 y=10
squareBumper name=SquareE x=4 y=10
triangleBumper name=TriangleA x=5 y=10
triangleBumper name=CircleA x=6 y=10
circleBumper name=CircleB x=7 y=10

circleBumper name=CircleC x=4 y=3
triangleBumper name=TriangleB x=19 y=3 orientation=90


# define some flippers
  leftFlipper name=Lefty x=10 y=7 orientation=0 
rightFlipper name=Righty x=12 y=7 orientation=0
rightFlipper name=FlipperRight x=17 y=8 orientation=90
rightFlipper name=FlipperLeft x=16 y=8 orientation=90

# define an absorber to catch the ball
 absorber name=Abs x=10 y=17 width=10 height=2 

# define events between gizmos
fire trigger=Square action=FlipperLeft
fire trigger=SquareB action=FlipperLeft
fire trigger=SquareC action=FlipperRight
fire trigger=SquareD action=FlipperRight
fire trigger=SquareE action=FlipperRight
fire trigger=CircleB action=Lefty
fire trigger=TriangleB action=Righty
fire trigger=TriangleA action=Lefty

# make the absorber self-triggering
 fire trigger=Abs action=Abs 