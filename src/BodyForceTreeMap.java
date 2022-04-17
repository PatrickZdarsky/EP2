// A map that associates a Body with a Vector3 (typically this is the force exerted on the body).
// The number of key-value pairs is not limited.
public class BodyForceTreeMap {


    private MyTreeMapNode headNode;

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise 'null' is returned.
    // Precondition: key != null.
    public Vector3 put(Body key, Vector3 value) {
        if (headNode == null)
            headNode = new MyTreeMapNode();

        return headNode.put(key, value);
    }

    // Returns the value associated with the specified key, i.e. the method returns the force vector
    // associated with the specified key. Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Vector3 get(Body key) {
        return headNode == null ? null : headNode.get(key);
    }

    // Returns 'true' if this map contains a mapping for the specified key.
    public boolean containsKey(Body key) {
        return headNode != null && headNode.containsKey(key);
    }

    // Returns a readable representation of this map, in which key-value pairs are ordered
    // descending according to the mass of the bodies.
    public String toString() {
        return headNode == null ? "" : headNode.toString();
    }
}
