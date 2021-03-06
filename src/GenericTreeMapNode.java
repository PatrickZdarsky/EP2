import java.lang.annotation.Target;
import java.util.Iterator;
import java.util.NoSuchElementException;

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

    public V remove(K key, GenericTreeMapNode<K, V> parent) {
        if (entry.getKey().compareTo(key) > 0) {
            //right
            return right != null ? right.remove(key, this) : null;
        } else if (entry.getKey().compareTo(key) < 0) {
            //left
            return left != null ? left.remove(key, this) : null;
        } else {
            //this
            var removed = entry.getValue();

            if (left != null && right != null) {
                entry = minKey();
            } else if (parent.left == this) {
                parent.left = (left != null) ? left : right;
            } else if (parent.right == this) {
                parent.right = (left != null) ? left : right;
            }

            return removed;
        }

//        if (entry.getKey().equals(key)) {
//            //This node has to be removed
//            var value = entry.getValue();
//            entry = pullUp();
//
//            return value;
//        }
//
//        if (entry.getKey().compareTo(key) > 0) {
//            if (right != null) {
//                var value = right.remove(key);
//
//                //Check if the node has deleted itself
//                if (right.entry == null)
//                    right = null;
//
//                return value;
//            }
//        } else {
//            if (left != null) {
//                var value = left.remove(key);
//
//                //Check if the node has deleted itself
//                if (left.entry == null)
//                    left = null;
//
//                return value;
//            }
//        }
//
//        //The key is not in this tree
//        return null;
    }

    private GenericKeyValuePair<K, V> minKey() {
        if (right == null)
            return entry;

        return right.minKey();
    }

    public GenericKeyValuePair<K, V> pullUp() {
        GenericKeyValuePair<K, V> oldEntry = entry;
        if (right != null) {
            entry = right.pullUp();
            //Check if right has deleted itself
            if (right.entry == null)
                right = null;
        } else if (left != null) {
            entry = left.pullUp();
            //Check if left has deleted itself
            if (left.entry == null)
                left = null;
        }

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

    public GenericTreeMapKeyIterator<K, V> keyIterator(){
        return new GenericTreeMapKeyIterator<>(this);
    }

    public class GenericTreeMapKeyIterator<K extends Comparable<K>, V> implements Iterator<K> {

        private final GenericTreeMapNode<K, V> node;

        private byte mode = 0;

        private Iterator<K> iterator;

        private K current, next;

        public GenericTreeMapKeyIterator(GenericTreeMapNode<K, V> node) {
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
            current = next;
            if (current == null)
                throw new NoSuchElementException();

            next = retrieveNext();

            return current;
        }

        public GenericTreeMapNode<K, V> getNode() {
            return node;
        }
    }
}
