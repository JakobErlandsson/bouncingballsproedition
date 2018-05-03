
/**
* The physics model.
* <p>
* This class is where you should implement your bouncing balls model.
* <p>
* The code has intentionally been kept as simple as possible, but if you wish, you can improve the design.
*
* @author Simon Robillard
*/
public class Model {
    
    double areaWidth, areaHeight;
    double gravity = 9.82;
    int time = 0;
    
    Ball[] balls;
    
    Model(double width, double height) {
        areaWidth = width;
        areaHeight = height;
        
        // Initialize the model with a few balls
        balls = new Ball[2];
        balls[0] = new Ball(width / 3, height * 0.9, 1.3, 1.6, 0.2);
        balls[1] = new Ball(2 * width / 3, height * 0.9, -0.6, 0.6, 0.3);
    }
    
    Vector rotate(Vector vector, Double ang) {
        double x = vector.x * Math.cos(ang) - vector.y * Math.sin(ang);
        double y = vector.x * Math.sin(ang) + vector.y * Math.cos(ang);
        return new Vector(x, y);
    }
    
    void step(double deltaT) {
        // TODO this method implements one step of simulation with a step deltaT
        for (Ball b : balls) {
            // detect collision with another ball
            Ball other = null;
            if (time == 0){
                other = collision(b);
            } else{ time --; }
            if(other != null) {
                double deltaX = b.x - other.x;
                double deltaY = b.y - other.y;
                double theta = Math.atan(deltaY/deltaX);
                
                
                
                // Vector u1 = rectToPolar(b.v);
                // Vector u2 = rectToPolar(other.v);
                // u1.y -= theta;
                // u2.y -= theta;
                // u1 = polarToRect(u1);
                // u2 = polarToRect(u2);
                
                Vector u1 = rotate(b.v, theta), u2 = rotate(other.v, theta);
                
                double m1 = b.radius;
                double m2 = other.radius;
                double i = u1.x*m1 + u2.x*m2;
                double r = u1.x-u2.x;
                double v1 = (i-m2*r)/(m1+m2);
                double v2 = (i+m1*r)/(m1+m2);
                
                Vector vB = new Vector(v1, u1.y);
                Vector vOther = new Vector(v2, u2.y);
                // b.v.x = v1;
                // other.v.x = v2;
                
                // u1 = rectToPolar(b.v);
                // u2 = rectToPolar(other.v);
                // u1.y += theta;
                // u2.y += theta;
                // b.v = polarToRect(u1);
                // other.v = polarToRect(u2);
                
                b.v = rotate(vB, - theta);
                other.v = rotate(vOther, - theta);
                
            }
            // detect collision with the border
            else if (b.x < b.radius) {
                // only change direction if the ball is moving towards a border
                // this is to prevent the ball from getting stuck
                b.v.x = b.v.x < 0 ? b.v.x * -1 : b.v.x;
            }
            else if(b.x > areaWidth - b.radius){
                b.v.x = b.v.x < 0 ? b.v.x : b.v.x * -1;
            }
            // detect collision with the floor
            else if (b.y < b.radius) {
                b.v.y = b.v.y > 0 ? b.v.y : b.v.y * -1;
            }
            // detect collision with the roof
            else if( b.y > areaHeight - b.radius){
                b.v.y = b.v.y > 0 ? b.v.y * -1 : b.v.y;
            }
            // if no collision, change speed of ball according to gravity.
            else
            b.v.y -= deltaT * gravity;
            
            
            // compute new position according to the speed of the ball
            b.x += deltaT * b.v.x;
            b.y += deltaT * b.v.y;
        }
    }
    
    
    // calculating the distance between the center of two balls with the
    // Pythagorean theorem
    double getDist(Ball b1, Ball b2) {
        double xDist = Math.abs((b1.x - b2.x));
        double yDist = Math.abs((b1.y - b2.y));
        return Math.sqrt(xDist * xDist + yDist * yDist);
    }
    
    Ball collision(Ball b) {
        for (Ball other : balls) {
            if (b != other) {
                if (getDist(b, other) <= b.radius + other.radius) {
                    System.out.println("coll" + String.valueOf(Math.random()));
                    time = 5;
                    return other;
                }
            }
        }
        return null;
    }
    
    Vector rectToPolar(Vector v){
        double r = Math.sqrt(v.x*v.x + v.y*v.y);
        double q = Math.atan(v.y/v.x);
        return new Vector(r,q);
        
    }
    Vector polarToRect(Vector v){
        double x = v.x*Math.cos(v.y);
        double y = v.x*Math.sin(v.y);
        return new Vector(x,y);
    }
    
    /**
    * Simple inner class describing balls.
    */
    class Ball {
        
        Ball(double x, double y, double vx, double vy, double r) {
            this.x = x;
            this.y = y;
            v = new Vector(vx, vy);
            this.radius = r;
        }
        
        /**
        * Position, speed, and radius of the ball. You may wish to add other attributes.
        */
        double x, y, radius;
        Vector v;
    }
    
    class Vector {
        
        double x, y;
        
        Vector(double vx, double vy){
            this.x = vx;
            this.y = vy;
        }
    }
}
