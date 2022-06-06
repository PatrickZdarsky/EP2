import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadDataUtil {

    // Reads the position and velocity vector on the specified 'day' from the file with the
    // specified 'path', and sets position and current velocity of 'b' accordingly. If
    // successful the method returns 'true'. If the specified 'day' was not found in the file,
    // 'b' is unchanged and the method returns 'false'.
    // The file format is validated before reading the state.
    // Lines before the line "$$SOE" and after the line "$$EOE" the are ignored. Each line of the
    // file between the line "$$SOE" and the line "$$EOE" is required to have the following format:
    // JDTDB, TIME, X, Y, Z, VX, VY, VZ
    // where JDTDB is interpretable as a 'double' value, TIME is a string and X, Y, Z, VX, VY and
    // VZ are interpretable as 'double' values. JDTDB can be ignored. The character ',' must only
    // be used as field separator. If the file is not found, an exception of the class
    // 'StateFileNotFoundException' is thrown. If it does not comply with the format described
    // above, the method throws an exception of the class 'StateFileFormatException'. Both
    // exceptions are subtypes of 'IOException'.
    // Precondition: b != null, path != null, day != null and has the format YYYY-MM-DD.
    public static boolean readConfiguration(NamedBody b, String path, String day)
            throws IOException {

        var filePath = Path.of(path);
        if (!Files.exists(filePath))
            throw new StateFileNotFoundException(path);

        try (var reader = Files.newBufferedReader(filePath)) {
            //Read until $$SOE
            while (reader.ready() && !reader.readLine().equals("$$SOE")) {
            }

            String line = null;
            while (reader.ready()) {
                line = reader.readLine();

                var split = line.split(",");

                //Check for invalid format
                if (split.length != 8)
                    throw new StateFileFormatException("Data not properly separated");

                if (split[1].contains(day)) {
                    //We have found the searched entry

                    var x = Double.parseDouble(split[2]);
                    var y = Double.parseDouble(split[3]);
                    var z = Double.parseDouble(split[4]);
                    var position = new Vector3(x, y, z);

                    var vx = Double.parseDouble(split[5]) * 3600;
                    var vy = Double.parseDouble(split[6]) * 3600;
                    var vz = Double.parseDouble(split[7]) * 3600;
                    var velocity = new Vector3(vx, vy, vz);

                    b.setState(position, velocity);
                    return true;
                }


                if (line.equals("$$EOE"))
                    return false;
            }

            //Invalid file
            if (line == null)
                throw new StateFileFormatException("No data found");

        }
        return false;
    }
}

