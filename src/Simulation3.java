import codedraw.CodeDraw;

import java.awt.Color;
import java.util.Random;

// Simulates the formation of a massive solar system.
//
public class Simulation3 {

    // The main simulation method using instances of other classes.
    public static void main(String[] args) {

        // simulation
        CodeDraw cd = new CodeDraw();
        BodyLinkedList bodies = new BodyLinkedList();
        BodyForceTreeMap forceOnBody = new BodyForceTreeMap();

        Random random = new Random(2022);

        for (int i = 0; i < Simulation.NUMBER_OF_BODIES; i++) {
            bodies.addLast(new Body(Math.abs(random.nextGaussian()) * Simulation.OVERALL_SYSTEM_MASS / Simulation.NUMBER_OF_BODIES, // kg
                    new Vector3(0.2 * random.nextGaussian() * Simulation.AU, 0.2 * random.nextGaussian() * Simulation.AU, 0.2 * random.nextGaussian() * Simulation.AU),
                    new Vector3(0 + random.nextGaussian() * 5e3, 0 + random.nextGaussian() * 5e3, 0 + random.nextGaussian() * 5e3)));
        }


        double seconds = 0;

        // simulation loop
        while (true) {
            seconds++; // each iteration computes the movement of the celestial bodies within one second.


            // merge bodies that have collided
            for (int i = 0; i < bodies.size(); i++) {
                Body body = bodies.get(i);

                BodyLinkedList collidingBodies = bodies.removeCollidingWith(body);
                int size = collidingBodies == null ? 0 : collidingBodies.size();
                if (size > 1) {
                    //Merge bodies
                    for (int j = 0; j < size; j++) {
                        Body otherBody = collidingBodies.get(j);
                        if (otherBody != body) {
                            body = body.merge(otherBody);
                        }
                    }
                }

                //removeCollidingWith also removes the body which was given to check, so re-add it here
                bodies.addLast(body);
            }

            // for each body: compute the total force exerted on it.
            int size = bodies.size();
            for (int i = 0; i < size; i++) {
                Body body = bodies.get(i);
                forceOnBody.put(body, new Vector3()); // begin with zero

                for (int j = 0; j < size; j++) {
                    Body otherBody = bodies.get(j);
                    if (body != otherBody) {
                        Vector3 forceToAdd = body.gravitationalForce(otherBody);

                        forceOnBody.put(body, forceOnBody.get(body).plus(forceToAdd));
                    }
                }
            }

            // for each body: move it according to the total force exerted on it.
            for (int i = 0; i < size; i++) {
                Body body = bodies.get(i);
                body.move(forceOnBody.get(body));
            }

            // show all movements in the canvas only every hour (to speed up the simulation)
            if (seconds % (3600) == 0) {
                // clear old positions (exclude the following line if you want to draw orbits).
                cd.clear(Color.BLACK);

                // draw new positions
                for (int i = 0; i < size; i++) {
                    bodies.get(i).draw(cd);
                }

                // show new positions
                cd.show();
            }

        }

    }
}
