board name=board_lots_of_balls gravity = 20.0
 
# define so many balls
# define one for every month!
# testing more comments     and      whitespace
ball name=January x=1.8 y=4.5 xVelocity=10.4 yVelocity=10.3 
ball name=February x=2.8 y=4.3 xVelocity=1.4 yVelocity=10.0
ball name=March x=1.8 y=4.5 xVelocity=10.4 yVelocity=15.0
ball name=April x=1.8 y=4.0 xVelocity=5.0 yVelocity=5.0
ball name=May x=2.2 y=5.5 xVelocity=6.0 yVelocity=6.0
ball name=June x=1.8 y=4.5 xVelocity=7.0 yVelocity=7.1
ball name=July x=1.1 y=4.0 xVelocity=11.4 yVelocity=11.4
ball name=August x=1.8 y=4.5 xVelocity=6.6 yVelocity=6.7
ball name=September x=1.8 y=4.3 xVelocity=9.9 yVelocity=10.0
ball name=October x=1.2 y=4.5 xVelocity=22.4 yVelocity=22.5
ball name=November  x=1.1 y=4.7 xVelocity=7.7 yVelocity=7.9
ball name=December x=0.8 y=10.0 xVelocity=4.5 yVelocity=4.6
 
# define some bumpers
squareBumper name=SquareA x=0 y=14
squareBumper name=SquareB x=1 y=14
squareBumper name=SquareC x=2 y=14
squareBumper name=SquareD x=3 y=14
 
circleBumper name=Circle x=4 y=2
triangleBumper name=Tri x=10 y=3 orientation=270
 
 
# define some flippers
  leftFlipper name=Lefty x=10 y=7 orientation=90
rightFlipper name=Righty x=12 y=7 orientation=0
 
 
# define an absorber to catch the ball!!
 absorber name=Abs x=14 y=17 width=3 height=4
 
# define a portal 
portal name=SecretPortal x=2 y=7 otherPortal=Jupiter
portal name=Jupiter x=15 y=7 otherBoard=manyPortalBoard
 
# make the absorber self-triggering
 fire trigger=Abs action=Abs 
 
#define other actions
fire trigger=SquareA action=SquareB
fire trigger=SquareB action=SquareC
fire trigger=SquareC action=Righty
fire trigger=Circle action=Lefty