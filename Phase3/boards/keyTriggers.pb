board name=KeyTriggers

# define a ball
ball name=BallA x=0.25 y=3.25 xVelocity=0 yVelocity=0
ball name=BallB x=5.25 y=3.25 xVelocity=0 yVelocity=0 


# define some triangle bumpers
triangleBumper name=TriA x=19 y=0 orientation=90
triangleBumper name=TriB x=10 y=18 orientation=180

# define an absorber
absorber name=Abs x=0 y=19 width=20 height=1 

leftFlipper name=FlipA x=0 y=8 orientation=90 
leftFlipper name=FlipB x=18 y=10 orientation=90 

keydown key=left action=FlipA
keyup key=right action= FlipB

fire trigger=Abs action=Abs