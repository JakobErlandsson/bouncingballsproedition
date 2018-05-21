
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

    float areaWidth, areaHeight;
    float gravity = 9.82f;
    int time = 0;

    Ball[] balls;

    Model(float width, float height) {
        areaWidth = width;
        areaHeight = height;

        // Initialize the model with a few balls
        balls = new Ball[2];
        balls[0] = new Ball(width * 0.25f, height * 0.75f, 2f, 0, 0.2f);
        balls[1] = new Ball(width * 0.5f, height * 0.70f, -2f, 0, 0.25f);

    }

    void step(double deltaT) {
        // TODO this method implements one step of simulation with a step deltaT

        for (Ball b : balls) {
            // detect collision with another ball
            Ball other = null;
            //System.out.println(time);
            if (time < 0) {
                other = collision(b);
            } else {
                time--;
            }
            if (other != null) {

                float deltaX = (b.x - other.x);
                float deltaY = (b.y - other.y);
                float theta = (float)Math.atan(deltaY / deltaX);


                float m1 = b.mass;
                float m2 = other.mass;

                Vector u1 = rotate(b.v, -theta), u2 = rotate(other.v, -theta);

                float mom1 = b.v.x*m1 + other.v.x*m2;
                float kin1 = b.v.x*b.v.x*m1/2 + other.v.x*other.v.x*m2/2;



                float r = u1.x-u2.x;

                float i = u1.x * m1 + u2.x * m2;
                float v1 = (i - m2 * r) / (m1 + m2);
                float v2 = (i + m1 * r) / (m1 + m2);
                u1 = new Vector(v1, u1.y);
                u2 = new Vector(v2, u2.y);



                System.out.println("Difference in momentum: \t" + ((u1.x*m1 + u1.x*m2)-mom1));
                System.out.println("Difference in energy: \t\t" + ((u1.x*u1.x*m1/2 + u1.x*u1.x*m2/2)-kin1) + "\n");



                b.v = rotate(u1, theta);
                other.v = rotate(u2, theta);

            }
            // detect collision with the border
            else if (b.x < b.radius)
                // only change direction if the ball is moving towards a border
                // this is to prevent the ball from getting stuck
                b.v.x = b.v.x < 0 ? b.v.x * -1 : b.v.x;

            else if (b.x > areaWidth - b.radius)
                b.v.x = b.v.x < 0 ? b.v.x : b.v.x * -1;

                // detect collision with the floor
            else if (b.y < b.radius)
                b.v.y = b.v.y > 0 ? b.v.y : b.v.y * -1;

                // detect collision with the roof
            else if (b.y > areaHeight - b.radius)
                b.v.y = b.v.y > 0 ? b.v.y * -1 : b.v.y;

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
    float getDist(Ball b1, Ball b2) {
        float xDist = Math.abs((b1.x - b2.x));
        float yDist = Math.abs((b1.y - b2.y));
        return (float)Math.sqrt((xDist * xDist) + (yDist * yDist));
    }

    Ball collision(Ball b) {
        for (Ball other : balls) {
            if (b != other) {
                if (getDist(b, other) <= (b.radius + other.radius)) {
                    time =2;
                    return other;
                }
            }
        }
        return null;
    }

    Vector rotate(Vector vector, double ang) {
        float x = (float)(vector.x * Math.cos(ang) - vector.y * Math.sin(ang));
        float y = (float)(vector.x * Math.sin(ang) + vector.y * Math.cos(ang));
        return new Vector(x, y);
    }

    /**
     * Simple inner class describing balls.
     */
    class Ball {

        Ball(float x, float y, float vx, float vy, float r) {
            this.x = x;
            this.y = y;
            v = new Vector(vx, vy);
            this.radius = r;
            mass = r*r*r;
        }

        /**
         * Position, speed, and radius of the ball. You may wish to add other attributes.
         */
        float x, y, radius;
        Vector v;
        float mass;
    }

    class Vector {

        float x, y;

        Vector(float vx, float vy) {
            this.x = vx;
            this.y = vy;
        }
    }
}
