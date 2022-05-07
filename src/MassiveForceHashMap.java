import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

// A hash map that associates a 'Massive'-object with a Vector3 (typically this is the force
// exerted on the object). The number of key-value pairs is not limited.
//
public class MassiveForceHashMap {

    private GenericKeyValuePair<Massive, Vector3>[] table;
    private int count;

    private MassiveLinkedList keyList;

    private final static float LOAD_FACTOR = 0.75f;

    // Initializes 'this' as an empty map.
    public MassiveForceHashMap() {
        table = new GenericKeyValuePair[65];
        keyList = new MassiveLinkedList();
    }

    private int find (Massive key) {
         if (key == null) return table.length - 1;
         int i = key.hashCode() & (table.length - 2);
         while (table[i] != null && !table[i].getKey().equals(key))
             i = (i + 1) & (table.length - 2);
         return i;
    }

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise 'null' is returned.
    // Precondition: key != null.
    public Vector3 put(Massive key, Vector3 value) {
        if (key == null)
            return null;

        int index = find(key);
        var oldValue = table[index] == null ? null : table[index].getValue();

        if (table[index] == null) {
            table[index] = new GenericKeyValuePair<>(key, value);
            keyList.addLast(key);

            if (++count >= LOAD_FACTOR * table.length) {
                //We have to resize the array and re-order the entries within it
                var oldEntries = table;
                table = new GenericKeyValuePair[(table.length << 1) - 1];

                for (int i = 0; i < oldEntries.length; i++) {
                    if (oldEntries[i] != null)
                        table[i = find(oldEntries[i].getKey())] = oldEntries[i];
                }
            }
        } else {
            table[index].setValue(value);
        }

        return oldValue;
    }

    // Returns the value associated with the specified key, i.e. the method returns the force vector
    // associated with the specified key. Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Vector3 get(Massive key) {
        var entry = table[find(key)];
        return entry == null ? null : entry.getValue();
    }

    // Returns 'true' if this map contains a mapping for the specified key.
    public boolean containsKey(Massive key) {
        return table[find(key)] != null;
    }

    // Returns a readable representation of this map, with all key-value pairs. Their order is not
    // defined.
    public String toString() {
        return "MassiveForceHashMap {\n" +
                Arrays.stream(table)
                        .map(entry -> entry.getKey() + " => " + entry.getValue()).collect(Collectors.joining("\n")) +
                "\n}";
    }

    // Compares `this` with the specified object for equality. Returns `true` if the specified
    // `o` is not `null` and is of type `MassiveForceHashMap` and both `this` and `o` have equal
    // key-value pairs, i.e. the number of key-value pairs is the same in both maps and every
    // key-value pair in `this` equals one key-value pair in `o`. Two key-value pairs are
    // equal if the two keys are equal and the two values are equal. Otherwise `false` is returned.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MassiveForceHashMap)) return false;
        MassiveForceHashMap that = (MassiveForceHashMap) o;
        return table.equals(that.table);
    }

    // Returns the hashCode of `this`.
    @Override
    public int hashCode() {
        return Objects.hash(table);
    }

    // Returns a list of all the keys in no specified order.
    public MassiveLinkedList keyList() {
        return keyList;
    }

}