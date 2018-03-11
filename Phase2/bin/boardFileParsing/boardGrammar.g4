grammar boardGrammar; 
@header{
package boardFileParsing;
}

root: define_board (gadget_define | COMMENT|fire_trigger_action)* EOF ;
//text: gadget* | COMMENT* |NEWLINE*|; 
 
//STRING : '"' .*? '"' ;

COMMENT : '#' ~( '\r' | '\n' )*-> skip;
WHITESPACE : [ \t\r\n]+ -> skip ; // toss out whitespace
NEWLINE:'\r'? '\n' ; // return newlines to parser (is end-statement signal)


BALL: 'ball'; 
NAME: 'name'; 
NAMEX: [A-Za-z][A-Za-z0-9_]*;
//FLOAT: [0-9]+'.'[0-9]+|[0]'.'?[0-9]+;

INTEGER : [0-9]+;
FLOAT: '-'?([0-9]+'.'[0-9]*|'.'?[0-9]+);
EQUALS: '=';

//Parser Rules 

numvalue: FLOAT|INTEGER; 
xPos: 'x' EQUALS numvalue;
yPos: 'y' EQUALS numvalue; 
position: xPos yPos; 
type: 'squareBumper' | 'triangleBumper' | 'circleBumper' | 'absorber' | 'rightFlipper'| 'leftFlipper' | 'ball' | 'portal';
name: NAME EQUALS NAMEX; 
width:'width' EQUALS numvalue;
height:'height' EQUALS numvalue;

size: width height;
speed: 'xVelocity' EQUALS numvalue 'yVelocity' EQUALS numvalue;
orientation: 'orientation' EQUALS numvalue;  //orientation values should only be 0, 90. 180, or 170;
otherboard: 'otherBoard' EQUALS NAMEX;
otherportal: 'otherPortal' EQUALS NAMEX; 

gadget_define: type name? position size? speed? orientation? otherboard? otherportal?; 
//squareBumper name=Square x=0 y=2

//board parameters
gravity: 'gravity' EQUALS numvalue;
friction1: 'friction1' EQUALS numvalue;
friction2: 'friction2' EQUALS numvalue;

///make board 
define_board: 'board' name gravity? friction1? friction2?; 

fire_trigger: 'fire' 'trigger' EQUALS NAMEX; 
action: 'action' EQUALS NAMEX; 
fire_trigger_action: fire_trigger action; 



