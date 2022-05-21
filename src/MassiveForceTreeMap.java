// A map that associates an object of 'Massive' with a Vector3. The number of key-value pairs
// is not limited.
//
public class MassiveForceTreeMap implements MassiveIterable{

    private GenericTreeMapNode<Massive, Vector3> rootNode;

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise 'null' is returned.
    // Precondition: key != null.
    public Vector3 put(Massive key, Vector3 value) {
        if (rootNode == null) {
            rootNode = new GenericTreeMapNode<>(key, value);
            return null;
        }
        return rootNode.put(key, value);
    }

    // Returns the value associated with the specified key, i.e. the method returns the force vector
    // associated with the specified key. Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Vector3 get(Massive key) {
        if (rootNode == null)
            return null;

        return rootNode.get(key);
    }

    // Returns 'true' if this map contains a mapping for the specified key.
    //Precondition: key != null
    public boolean containsKey(Massive key) {
        if (rootNode == null)
            return false;

        return rootNode.containsKey(key);
    }

    public int count() {
        if (rootNode == null)
            return 0;

        return rootNode.count();
    }

    public Vector3 remove(Massive key) {
        if (rootNode == null)
            return null;

        var value = rootNode.remove(key);

        if (rootNode.isEmpty())
            rootNode = null;

        return value;
    }

    public void clear() {
        rootNode = null;
    }

    // Returns a readable representation of this map, in which key-value pairs are ordered
    // descending according to 'key.getMass()'.
    public String toString() {
        if (rootNode == null)
            return "{}";

        return rootNode.toString();
    }

    // Returns a `MassiveSet` view of the keys contained in this tree map. Changing the
    // elements of the returned `MassiveSet` object also affects the keys in this tree map.
    public MassiveSet getKeys() {

        return new MyMassiveSet(this);
    }

    @Override
    public MassiveIterator iterator() {
        return new MassiveIterator() {

            GenericTreeMapNode<Massive, Vector3>.GenericTreeMapKeyIterator<Massive> iterator = rootNode.keyIterator();

            @Override
            public Massive next() {
                return iterator.next();
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }
}

