board name=many_portal_board gravity = 15.0
 
# define a ball
ball name=Apple x=1.8 y=4.5 xVelocity=10.4 yVelocity=10.3 
ball name=Banana x=2.6 y=11.0 xVelocity=11.5 yVelocity=12.0
ball name=Cranberry x=1.0 y=4.0 xVelocity=8.0 yVelocity=8.3
ball name=DragonFruit x=1.3 y=4.3 xVelocity=11.0 yVelocity=11.2
 
# define some bumpers
squareBumper name=Square x=0 y=11
squareBumper name=SquareB x=1 y=11
squareBumper name=SquareC x=2 y=11
squareBumper name=SquareD x=3 y=11
 
circleBumper name=HiImACircle x=6 y=6
triangleBumper name=Tri x=5 y=12 orientation=180
 
 
# define flippers
leftFlipper name=FlipL x=10 y=7 orientation=270
rightFlipper name=FlipR x=12 y=7 orientation=270
 
 
# define an absorber to catch the ball
absorber name=Absorber12234 x=10 y=17 width=10 height=2
 
#define the many portals
portal name=portalA x=5 y=4 otherPortal=Jupiter
portal name=Jupiter x=13 y=13 otherPortal=theMoon
portal name=portalB x=1 y=10 otherPortal=portalC
portal name=portalC x=8 y=11 otherPortal=portalD
portal name=portalD x=15 y=15 otherPortal=portalK
 
 
# make the absorber self-triggering
fire trigger=Abs action=Abs 
 
 
# define some flippers
leftFlipper name=Lefty x=10 y=7 orientation=90
rightFlipper name=Righty x=12 y=7 orientation=0
 
 
# define an absorber to catch the ball!!
absorber name=Abs x=12 y=12 width=5 height=1
 
# define a portal 
portal name=SecretPortal x=2 y=7 otherPortal=Jupiter
portal name=Jupiter x=18 y=7 otherBoard=manyPortalBoard otherPortal=Jerry
 
# make the absorber self-triggering
fire trigger=Abs action=Abs 
 
#define other actions
fire trigger=SquareA action=SquareB
fire trigger=SquareB action=SquareC
fire trigger=SquareC action=Righty
fire trigger=Circle action=Lefty