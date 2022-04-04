import java.util.Arrays;

// A map that associates a body with a force exerted on it. The number of
// key-value pairs is not limited.
//
public class BodyForceMap {

    private Body[] keys;
    private Vector3[] values;
    private int size;

    // Initializes this map with an initial capacity.
    // Precondition: initialCapacity > 0.
    public BodyForceMap(int initialCapacity) {
        keys = new Body[initialCapacity];
        values = new Vector3[initialCapacity];
    }

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise 'null' is returned.
    // Precondition: key != null.
    public Vector3 put(Body key, Vector3 force) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int middle = left + ((right - left) / 2);

            if (keys[middle] == key) {
                //We have to overwrite the current value
                Vector3 oldForce = values[middle];
                values[middle] = force;

                return oldForce;
            }
            if (keys[middle].mass() < key.mass()) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }

        if (size == keys.length) {
            //We have to expand our arrays
            keys = Arrays.copyOf(keys, keys.length*2);
            values = Arrays.copyOf(values, values.length*2);
        }

        for (int i = size; i > right + 1 ; i--) {
            keys[i] = keys[i-1];
            values[i] = values[i-1];
        }

        keys[right+1] = key;
        values[right+1] = force;
        size++;

        return null;
    }

    public void clearBelow(double threshold) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int middle = left + ((right - left) / 2);

            if (keys[middle].mass() < threshold) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }


        for (int i = right; i < size; i++) {
            keys[i] = null;
            values[i] = null;
        }
    }

    // Returns the value associated with the specified key, i.e. the returns the force vector
    // associated with the specified body. Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Vector3 get(Body key) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int middle = left + ((right - left) / 2);

            if (keys[middle] == key) {
                //value found!
                return values[middle];
            }

            if (keys[middle].mass() < key.mass()) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }

        return null;
    }
}
