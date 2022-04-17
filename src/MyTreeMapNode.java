/**
 * @author Patrick Zdarsky / Rxcki
 */
public class MyTreeMapNode {

    private Body key;
    private Vector3 value;

    private MyTreeMapNode left, right;

    /**
     * Adds a new key-value association to this map.
     *
     * Precondition: key != null.
     * @param key The key for the given value
     * @param value The value to be saved
     * @return If the key already exists in this map, the value is replaced and the old value is returned. Otherwise, 'null' is returned.
     */
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

            right = new MyTreeMapNode();
            right.key = key;
            right.value = value;
        } else {
            if (left != null)
                return left.put(key, value);

            left = new MyTreeMapNode();
            left.key = key;
            left.value = value;
        }

        return null;
    }

    /**
     * Returns the value associated with the specified key
     * @param key The key of which the value should be returned. Must not be null.
     * @return The value associated with the given key or null otherwise.
     */
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

    /**
     * Checks if a given key is present in the node tree
     * @param key The key to check. Must not be null.
     * @return True if this node tree contains the given key, false otherwise.
     */
    public boolean containsKey(Body key) {
        return get(key) != null;
    }

    /**
     *
     * @return Returns a readable representation of this map, in which key-value
     * pairs are ordered descending according to the mass of the bodies.
     */
    public String toString() {
        String s = "";
        //Add the right one since it is the highest
        if (right!= null)
            s = right + "\n";

        //Now add ourself
        s += key + " => " + value;

        //Finally add the left
        if (left != null)
            s += "\n" + left;

        return s;
    }

}
