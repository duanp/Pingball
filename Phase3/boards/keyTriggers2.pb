board name=KeyTriggers2

# define a ball
ball name=BallA x=0.25 y=3.25 xVelocity=0 yVelocity=0
ball name=BallB x=5.25 y=3.25 xVelocity=0 yVelocity=0 


# define some triangle bumpers
triangleBumper name=TriA x=19 y=0 orientation=90
triangleBumper name=TriB x=10 y=18 orientation=180

# define an absorber
absorber name=Abs x=0 y=19 width=20 height=1 

# define some circle bumpers
circleBumper name=CircleA x=5 y=18
circleBumper name=CircleB x=7 y=13
circleBumper name=CircleC x=0 y=5
circleBumper name=CircleD x=5 y=5
circleBumper name=CircleE x=10 y=5
circleBumper name=CircleF x=15 y=5

leftFlipper name=FlipA x=0 y=8 orientation=90 
leftFlipper name=FlipB x=18 y=10 orientation=90 


# define some right flippers 
rightFlipper name=FlipE x=2 y=15 orientation=0
rightFlipper name=FlipF x=17 y=15 orientation=0

keydown key=left action=FlipA
keydown key=right action= FlipB

keyup key=e action=FlipE
keyup key=r action=FlipF

keydown key=space action=Abs


