import codedraw.CodeDraw;

import java.awt.*;
import java.util.Random;

/*
    Zusatzfragen:
    Datenkapselung:
        Hierbei versteht man das Verbergen von Daten vor dem Zugriff von "außen". Stattdessen werden definierte Schnittstellen verwendet.
        In dieser Aufgabe ist die Vector3 Klasse ein gutes Beispiel. Die initial öffentlichen Koordinaten sind nun versteckt und können nur über
        die definierten Schnittstellen den Objektmethoden manipuliert werden.
    Data Hiding:
        Unter Data Hiding versteht man, Methoden/Daten nur so öffentlich zu machen wie es unbedingt notwendig ist.
        Hier sind die Klassen Vector3 und Body gute Beispiele, nachdem die Daten nun private sind da auf sie nur mehr
        innerhalb der Klasse zugegriffen wird aufgrund der angewandten Datenkapselung.
    Objektmethoden:
        Der linke Teil eines solchen Methodenaufrufs bildet der Klassenname bei einer Klassenmethode oder eine Instanz (Objekt) einer Klasse bei Objektmethoden.
        Bei Objektmethoden steht nie der Klassenname an der linken stelle, sondern eine Referenz zum Objekt(zB der Variablenname oder eine Methode die ein Objekt zurück gibt, etc.)
 */

// Simulates the formation of a massive solar system.
public class Simulation {

    // gravitational constant
    public static final double G = 6.6743e-11;

    // one astronomical unit (AU) is the average distance of earth to the sun.
    public static final double AU = 150e9; // meters

    // one light year
    public static final double LY = 9.461e15; // meters

    // some further constants needed in the simulation
    public static final double SUN_MASS = 1.989e30; // kilograms
    public static final double SUN_RADIUS = 696340e3; // meters
    public static final double EARTH_MASS = 5.972e24; // kilograms
    public static final double EARTH_RADIUS = 6371e3; // meters

    // set some system parameters
    public static final double SECTION_SIZE = 2 * AU; // the size of the square region in space
    public static final int NUMBER_OF_BODIES = 22;
    public static final double OVERALL_SYSTEM_MASS = 20 * SUN_MASS; // kilograms

    // all quantities are based on units of kilogram respectively second and meter.

    // The main simulation method using instances of other classes.
    public static void main(String[] args) {

        // simulation
        CodeDraw cd = new CodeDraw();
        BodyQueue bodies = new BodyQueue(NUMBER_OF_BODIES);
        BodyForceMap forceOnBody = new BodyForceMap(NUMBER_OF_BODIES);

        Random random = new Random(2022);

        for (int i = 0; i < NUMBER_OF_BODIES; i++) {
            bodies.add(new Body(Math.abs(random.nextGaussian()) * OVERALL_SYSTEM_MASS / NUMBER_OF_BODIES, // kg
                    new Vector3(0.2 * random.nextGaussian() * AU, 0.2 * random.nextGaussian() * AU, 0.2 * random.nextGaussian() * AU),
                    new Vector3(0 + random.nextGaussian() * 5e3, 0 + random.nextGaussian() * 5e3, 0 + random.nextGaussian() * 5e3)));
        }

        //Zusatzaufgabe
//        bodies[0] = new Body(1.989e30,new Vector3(0,0,0),new Vector3(0,0,0));
//        bodies[1] = new Body(5.972e24,new Vector3(-1.394555e11,5.103346e10,0),new Vector3(-10308.53,-28169.38,0));
//        bodies[2] = new Body(3.301e23,new Vector3(-5.439054e10,9.394878e9,0),new Vector3(-17117.83,-46297.48,-1925.57));
//        bodies[3] = new Body(4.86747e24,new Vector3(-1.707667e10,1.066132e11,2.450232e9),new Vector3(-34446.02,-5567.47,2181.10));
//        bodies[4] = new Body(6.41712e23,new Vector3(-1.010178e11,-2.043939e11,-1.591727E9),new Vector3(20651.98,-10186.67,-2302.79));


        double seconds = 0;

        // simulation loop
        while (true) {
            seconds++; // each iteration computes the movement of the celestial bodies within one second.

            // merge bodies that have collided => Aufgabe 4
//            for (int i = 0; i < bodies.length; i++) {
//                for (int j = i + 1; j < bodies.length; j++) {
//                    if (bodies[j].distanceTo(bodies[i]) <
//                            bodies[j].radius() + bodies[i].radius()) {
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
//            }

            // for each body: compute the total force exerted on it.
            BodyQueue loopQueue = new BodyQueue(bodies);
            BodyQueue otherBodies = new BodyQueue(bodies);
            Body body, otherBody;
            while((body = loopQueue.poll()) != null) {
                forceOnBody.put(body, new Vector3()); // begin with zero

                //Use our queue like a ring-buffer in order not to waste so many objects
                //Get the first body, in order to check when we have looped
                Body firstBody = otherBodies.poll();
                otherBody = firstBody;
                do {
                    if (body != otherBody) {
                        Vector3 forceToAdd = body.gravitationalForce(otherBody);

                        forceOnBody.put(body, forceOnBody.get(body).plus(forceToAdd));
                    }
                    //Re-add the body since it has been removed by the loop advancement
                    otherBodies.add(otherBody);

                    //Get the next body and break the loop if we have reached the first body
                } while ((otherBody = otherBodies.poll()) != firstBody);
                //Re-add the first body since we have removed it by checking the next body
                otherBodies.add(firstBody);
            }

            // for each body: move it according to the total force exerted on it.

            loopQueue = new BodyQueue(bodies);
            while((body = loopQueue.poll()) != null) {
                body.move(forceOnBody.get(body));
            }

            // show all movements in the canvas only every hour (to speed up the simulation)
            if (seconds % (3600) == 0) {
                // clear old positions (exclude the following line if you want to draw orbits).
                cd.clear(Color.BLACK);

                // draw new positions
                loopQueue = new BodyQueue(bodies);
                while((body = loopQueue.poll()) != null) {
                    body.draw(cd);
                }

                // show new positions
                cd.show();
            }

        }

    }
}
