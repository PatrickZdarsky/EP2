import codedraw.CodeDraw;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Simulates the solar system.
//
public class Simulation9 {

    public static void main(String[] args) {

        // simulation
        CodeDraw cd = new CodeDraw();

        // create solar system with 13 bodies
        Map<NamedBody, Vector3> map = new HashMap<>();

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
        map.put(new NamedBody("Ceres", 9.394E20, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));


        var iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            var body = (NamedBody) iterator.next();

            var readSuccess = false;
            try {
                readSuccess = ReadDataUtil.readConfiguration(body,  args[0] + "/" + body.getName() + ".txt", args[1]);
            }catch(IOException e) {

            }
            if (!readSuccess) {
                iterator.remove();
                System.out.println("Warning: State not available for "+body.getName()+".\nRunning simulation without "+body.getName()+".");
            }
        }

        // add sun after states have been read from files.
        map.put(new NamedBody("Sun", 1.989E30, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));


        for (int seconds = 0; true; seconds++){
            for (var massive : map.keySet()) {
                map.put(massive, new Vector3()); // begin with zero

                for (var otherMassive : map.keySet()) {
                    if (massive != otherMassive) {
                        Vector3 forceToAdd = massive.gravitationalForce(otherMassive);

                        map.put(massive, map.get(massive).plus(forceToAdd));
                    }
                }
            }
            map.keySet().parallelStream().forEach(body -> body.move(map.get(body)));

            if(seconds % 100 == 0){
                cd.clear(Color.BLACK);
                map.keySet().parallelStream().forEach(body -> body.draw(cd));
                cd.show();
            }
        }
    }




}
