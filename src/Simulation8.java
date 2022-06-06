import codedraw.CodeDraw;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.stream.StreamSupport;

// Simulates the solar system.
//
public class Simulation8 {

    // gravitational constant
    public static final double G = 6.6743e-11;

    // one astronomical unit (AU) is the average distance of earth to the sun.
    public static final double AU = 150e9; // meters

    // set some system parameters
    public static final double SECTION_SIZE = 10 * AU; // the size of the square region in space

    // all quantities are based on units of kilogram respectively second and meter.

    // The main simulation method using instances of other classes.
    public static void main(String[] args) {

        // simulation
        CodeDraw cd = new CodeDraw();

        // create solar system with 13 bodies
        MassiveForceTreeMap map = new MassiveForceTreeMap();

        map.put(new NamedBody("Oumuamua", 8e6, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Earth", 5.972E24, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Moon", 7.349E22, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Mars1", 6.41712E23, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Deimos", 1.8E20, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Phobos", 1.08E20, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Mercury", 3.301E23, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Venus", 4.86747E24, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Vesta", 2.5908E20, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Pallas", 2.14E20, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Hygiea", 8.32E19, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Ceres1", 9.394E20, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));


        var iterator = map.getKeys().iterator();
        while (iterator.hasNext()) {
            var body = (NamedBody) iterator.next();

            var readSuccess = false;
            try {
                readSuccess = ReadDataUtil.readConfiguration(body,  args[0] + File.pathSeparator + body.getName() + ".txt", args[1]);
            }catch(IOException e) {

            }
            if (!readSuccess) {
                map.remove(body);
                System.out.println("Warning: State not available for "+body.getName()+".\nRunning simulation without "+body.getName()+".");
            }
        }

        // add sun after states have been read from files.
        map.put(new NamedBody("Sun", 1.989E30, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));


        for (int seconds = 0; true; seconds++){
            for (var massive : map.getKeys()) {
                map.put(massive, new Vector3()); // begin with zero

                for (var otherMassive : map.getKeys()) {
                    if (massive != otherMassive) {
                        Vector3 forceToAdd = massive.gravitationalForce(otherMassive);

                        map.put(massive, map.get(massive).plus(forceToAdd));
                    }
                }
            }
            StreamSupport.stream(map.getKeys().spliterator(), false).forEach(massive -> massive.move(map.get(massive)));

            if(seconds % 3600 == 0){

                cd.clear(Color.BLACK);
                StreamSupport.stream(map.getKeys().spliterator(), false).forEach(massive -> massive.draw(cd));
                cd.show();
            }
        }
    }
}
