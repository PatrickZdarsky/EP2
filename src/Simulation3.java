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
                    for (int j = 0; j < size; j++) {
                        Body otherBody = collidingBodies.get(j);
                        if (otherBody != body) {
                            body = body.merge(otherBody);
                        }
                    }

                    // since the body index i changed size there might be new collisions
                    // at all positions of bodies, so start all over again
                //    i = -1;   Todo: This is broken!
                }
                bodies.addLast(body);

//                for (int j = i + 1; j < Simulation.NUMBER_OF_BODIES; j++) {
//                    Body otherBody = bodies.get(j);
//
//                    if (body.distanceTo(otherBody) <
//                            body.radius() + otherBody.radius()) {
//                        bodies[i] = bodies[i].merge(bodies[j]);
//                        Body[] bodiesOneRemoved = new Body[bodies.length - 1];
//                        for (int k = 0; k < bodiesOneRemoved.length; k++) {
//                            bodiesOneRemoved[k] = bodies[k < j ? k : k + 1];
//                        }
//                        bodies = bodiesOneRemoved;
//
//                        // since the body index i changed size there might be new collisions
//                        // at all positions of bodies, so start all over again
//                        i = -1;
//                        j = bodies.length;
//                    }
//                }
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



            //Create copies since we are losing the objects when traversing through the queue
//            BodyQueue loopQueue = new BodyQueue(bodies);
//            BodyQueue otherBodies = new BodyQueue(bodies);
//            Body body, otherBody;
//            while((body = loopQueue.poll()) != null) {
//                forceOnBody.put(body, new Vector3()); // begin with zero
//
//                //Use our queue like a ring-buffer in order not to waste so many objects
//                //Get the first body, in order to check when we have looped
//                Body firstBody = otherBodies.poll();
//                otherBody = firstBody;
//                do {
//                    if (body != otherBody) {
//                        Vector3 forceToAdd = body.gravitationalForce(otherBody);
//
//                        forceOnBody.put(body, forceOnBody.get(body).plus(forceToAdd));
//                    }
//                    //Re-add the body since it has been removed by the loop advancement
//                    otherBodies.add(otherBody);
//
//                    //Get the next body and break the loop if we have reached the first body
//                } while ((otherBody = otherBodies.poll()) != firstBody);
//                //Re-add the first body since we have removed it by checking the next body
//                otherBodies.add(firstBody);
//            }

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
