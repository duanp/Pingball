package warmup;
import physics.*;

/*
 * A class representing a ball with a position (two doubles) and a velocity vector (Vect). All of its attributes are mutable.
 */
public class Ball {

    private Vect vector;
    private double xCoordinate;
    private double yCoordinate;
    
    public Ball() {
        this.vector = new Vect(0.,0.);
        this.xCoordinate = 0.;
        this.yCoordinate = 0.;
    }
    
    public Ball(Vect vector, double xCoordinate, double yCoordinate) {
        this.vector = vector;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }
    
    public Vect getVector() {
        return this.vector;
    }
    
    public double getX() {
        return this.xCoordinate;
    }
    
    public double getY() {
        return this.yCoordinate;
    }
    
    public void changeVector(Vect newVector) {
        this.vector = newVector;
    }
    
    public void move(){
        this.xCoordinate += this.vector.x();
        this.yCoordinate += this.vector.y();
        
    }
    
    public void bounceX(){
        double newX = -1*this.vector.x();
        this.vector = new Vect(newX, this.vector.y());
    }
    
    public void bounceY(){
        double newY = -1*this.vector.y();
        this.vector = new Vect(this.vector.x(), newY);
    }
}
