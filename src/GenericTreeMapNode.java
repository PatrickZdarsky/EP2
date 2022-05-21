import java.util.Iterator;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class GenericTreeMapNode<K extends Comparable<K>, V> {

    private GenericKeyValuePair<K, V> entry;

    private GenericTreeMapNode<K, V> left, right;

    public GenericTreeMapNode(K key, V value) {
        entry = new GenericKeyValuePair<>(key, value);
    }

    public GenericTreeMapNode(GenericKeyValuePair<K, V> entry) {
        this.entry = entry;
    }

    /**
     * Adds a new key-value association to this map.
     *
     * Precondition: key != null.
     * @param key The key for the given value
     * @param value The value to be saved
     * @return If the key already exists in this map, the value is replaced and the old value is returned. Otherwise, 'null' is returned.
     */
    public V put(K key, V value) {
//        if (this.entry == null) {
//            //Save it in this node, since we are empty => this is also the root node
//            this.key = key;
//            this.value = value;
//
//            return null;
//        }

        if (entry.getKey().equals(key)) {
            //Overwrite this value
            V old = entry.getValue();
            entry.setValue(value);

            return old;
        } else if (entry.getKey().compareTo(key) > 0) {
            if (right != null)
                return right.put(key, value);

            right = new GenericTreeMapNode(key, value);
        } else {
            if (left != null)
                return left.put(key, value);

            left = new GenericTreeMapNode(key, value);
        }

        return null;
    }

    /**
     * Returns the value associated with the specified key
     * @param key The key of which the value should be returned. Must not be null.
     * @return The value associated with the given key or null otherwise.
     */
    public V get(K key) {
        if (entry.getKey().equals(key))
            return entry.getValue();

        if (entry.getKey().compareTo(key) > 0) {
            if (right != null)
                return right.get(key);
        } else {
            if (left != null)
                return left.get(key);
        }

        return null;
    }

    public V remove(K key) {
        if (entry.getKey().equals(key)) {
            //This node has to be removed
            var value = entry.getValue();
            entry = pullUp();

            return value;
        }

        if (entry.getKey().compareTo(key) > 0) {
            if (right != null) {
                var value = right.remove(key);

                //Check if the node has deleted itself
                if (right.entry == null)
                    right = null;

                return value;
            }
        } else {
            if (left != null) {
                var value = left.remove(key);

                //Check if the node has deleted itself
                if (left.entry == null)
                    left = null;

                return value;
            }
        }

        //The key is not in this tree
        return null;
    }

    public GenericKeyValuePair<K, V> pullUp() {
        if (right == null)
            return null;

        var oldEntry = entry;

        entry = right.pullUp();
        //Check if right has deleted itself
        if (right.entry == null)
            right = null;

        return oldEntry;
    }

    public K getKey() {
        return entry.getKey();
    }

    public V getValue() {
        return entry.getValue();
    }

    public boolean isEmpty() {
        return entry == null;
    }

    /**
     * Checks if a given key is present in the node tree
     * @param key The key to check. Must not be null.
     * @return True if this node tree contains the given key, false otherwise.
     */
    public boolean containsKey(K key) {
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
        s += entry.getKey() + " => " + entry.getValue();

        //Finally add the left
        if (left != null)
            s += "\n" + left;

        return s;
    }

    public int count() {
        var count = 1;

        if (right != null)
            count += right.count();
        if (left != null)
            count += left.count();

        return count;
    }

    public GenericTreeMapKeyIterator<K> keyIterator(){
        return new GenericTreeMapKeyIterator<>(this);
    }

    public class GenericTreeMapKeyIterator<K extends Comparable<K>> implements Iterator<K> {

        private final GenericTreeMapNode<K, ?> node;

        private byte mode = 0;

        private Iterator<K> iterator;

        private K next;

        public GenericTreeMapKeyIterator(GenericTreeMapNode<K, ?> node) {
            this.node = node;

            next = node.getKey();
        }


        private K retrieveNext() {
            if (iterator != null) {
                if (iterator.hasNext())
                    return iterator.next();
                iterator = null; //Iterator is empty
            }

            if (mode == 0) {
                mode++;
                if (node.left != null) {
                    //Use left iterator
                    iterator = node.left.keyIterator();
                }

            }
            if (iterator == null && mode == 1) {
                mode++;
                if (node.right != null) {
                    //Use right iterator
                    iterator = node.right.keyIterator();
                }
            }

            if (iterator != null)
                return iterator.next();
            return null;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public K next() {
            var currentNext = next;
            next = retrieveNext();

            return currentNext;
        }
    }
}
