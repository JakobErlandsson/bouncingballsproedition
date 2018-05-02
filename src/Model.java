
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

    Ball[] balls;

    Model(double width, double height) {
        areaWidth = width;
        areaHeight = height;

        // Initialize the model with a few balls
        balls = new Ball[2];
        balls[0] = new Ball(width / 3, height * 0.9, 1.3, 1.6, 0.2);
        balls[1] = new Ball(2 * width / 3, height * 0.6, -0.6, 0.6, 0.3);
    }

    void step(double deltaT) {
        // TODO this method implements one step of simulation with a step deltaT
        for (Ball b : balls) {
            Ball other = collision(b);
            // detect collision with the border
            if (b.x < b.radius || b.x > areaWidth - b.radius) {
                b.vx *= -1; // change direction of ball
            }
            // detect collision with the floor
            else if (b.y < b.radius) {
                b.vy = b.vy > 1 ? b.vy : b.vy * -1;
            }
            // detect collision with the roof
            else if( b.y > areaHeight - b.radius){
                b.vy *= -1;
            }
            // detect collision with other balls
            else if(other != null) {
                double deltaX = b.x - other.x;
                double deltaY = b.y - other.y;
                double u1 = Math.sqrt(b.vx*b.vx + b.vy*b.vy);
                double u2 = Math.sqrt(other.vx*other.vx + other.vy*other.vy);
                double m1 = b.radius;
                double m2 = other.radius;
                double i = u1*m1 + u2*m2;
                double r = u1-u2;
                double v1 = (i-m2*r)/(m1+m2);
                double v2 = (i+m1*r)/(m1+m2);
                double theta = Math.tan(deltaX/deltaY);

                b.vx +=  Math.cos((180 - theta)*v1);
                b.vy +=  Math.sin((180 - theta)*v1);
                other.vx +=  Math.cos(theta*v2);
                other.vy +=  Math.sin(theta*v2);
            }
            // if no collision, change speed of ball according to gravity.
            else
                b.vy -= deltaT * gravity;


            // compute new position according to the speed of the ball
            b.x += deltaT * b.vx;
            b.y += deltaT * b.vy;
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
                if (getDist(b, other) < b.radius + other.radius) {
                    return other;
                }
            }
        }
        return null;
    }


    /**
     * Simple inner class describing balls.
     */
    class Ball {

        Ball(double x, double y, double vx, double vy, double r) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.radius = r;
        }

        /**
         * Position, speed, and radius of the ball. You may wish to add other attributes.
         */
        double x, y, vx, vy, radius;
    }
}
