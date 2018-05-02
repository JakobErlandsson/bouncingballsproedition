
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
        balls[0] = new Ball(width / 3, height * 0.9, 1.2, 1.6, 0.2);
        balls[1] = new Ball(2 * width / 3, height * 0.6, -0.6, 0.6, 0.3);
    }

    void step(double deltaT) {
        // TODO this method implements one step of simulation with a step deltaT
        for (Ball b : balls) {
            // detect collision with the border
            if (b.x < b.radius || b.x > areaWidth - b.radius || collision(b))
                b.vx *= -1; // change direction of ball

            if (b.y <= b.radius || b.y >= areaHeight - b.radius || collision(b)) {
                if (b.y < b.radius - 0.1) {
                    b.y = b.radius;
                }
                else
                    b.vy = b.vy > 1 ? b.vy : b.vy * -1;
            }


            // compute new position according to the speed of the ball
            b.vy -= deltaT * gravity;
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

    boolean collision(Ball b) {
        for (Ball other : balls) {
            if (b != other) {
                if (getDist(b, other) < b.radius + other.radius) {
                    return true;
                }
            }
        }
        return false;
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
