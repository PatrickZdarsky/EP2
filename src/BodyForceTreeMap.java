// A map that associates a Body with a Vector3 (typically this is the force exerted on the body).
// The number of key-value pairs is not limited.
public class BodyForceTreeMap {

    private Body key;
    private Vector3 value;

    private BodyForceTreeMap left, right;

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise 'null' is returned.
    // Precondition: key != null.
    public Vector3 put(Body key, Vector3 value) {
        if (this.key == null) {
            //Save it in this node, since we are empty => this is also the root node
            this.key = key;
            this.value = value;

            return null;
        }

        if (this.key == key) {
            //Overwrite this value
            Vector3 old = this.value;
            this.value = value;

            return old;
        } else if (this.key.mass() < key.mass()) {
            if (right != null)
                return right.put(key, value);

            right = new BodyForceTreeMap();
            right.key = key;
            right.value = value;
        } else {
            if (left != null)
                return left.put(key, value);

            left = new BodyForceTreeMap();
            left.key = key;
            left.value = value;
        }

        return null;
    }

    // Returns the value associated with the specified key, i.e. the method returns the force vector
    // associated with the specified key. Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Vector3 get(Body key) {
        if (this.key == key)
            return value;

        if (this.key.mass() < key.mass()) {
            if (right != null)
                return right.get(key);
        } else {
            if (left != null)
                return left.get(key);
        }

        return null;
    }

    // Returns 'true' if this map contains a mapping for the specified key.
    public boolean containsKey(Body key) {
        return get(key) != null;
    }

    // Returns a readable representation of this map, in which key-value pairs are ordered
    // descending according to the mass of the bodies.
    public String toString() {
        //Check if we are a leaf => return our value
        if (right == null && left == null)
            return key + " => " + value;

        String s = "";

        //Todo: the root of the tree is missing, but I don't know how to check if we are the root node :c

        if (left != null)
            s = left.toString();// + "\n";
        if (right != null)
            s = right.toString() + "\n" + s;

        return s;
    }
}
