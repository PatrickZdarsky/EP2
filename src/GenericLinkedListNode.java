import java.util.Iterator;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class GenericLinkedListNode<T> implements Iterable<T>{

    private T value;

    private GenericLinkedListNode<T> next, previous;

    public GenericLinkedListNode(T value) {
        this.value = value;
    }

    public GenericLinkedListNode(T value, GenericLinkedListNode<T> previous, GenericLinkedListNode<T> next) {
        this.value = value;
        this.next = next;
        this.previous = previous;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public GenericLinkedListNode<T> getNext() {
        return next;
    }

    public GenericLinkedListNode<T> getPrevious() {
        return previous;
    }

    /**
     * Appends the given value after the current one, moving the current next back if there is one
     * @param newValue The value to append
     * @return The newly generated node, which is behind the current one
     */
    public GenericLinkedListNode<T> appendAfter(T newValue) {
        next = new GenericLinkedListNode<>(newValue, this, next);
        if (next.next != null)
            next.next.previous = next;

        return next;
    }

    /**
     * Appends the given value in front of the current one, this also works, if the current one is the head
     * @param newValue The value to append
     * @return The newly generated node, which is in front of the current one
     */
    public GenericLinkedListNode<T> appendBefore(T newValue) {
        previous = new GenericLinkedListNode<>(newValue, previous, this);
        if (previous.previous != null)
            previous.previous.next = previous;

        return previous;
    }

    public void add(int i, T newValue) {
        if (i == 0) {
            //Append it right here
            T oldValue = value;
            value = newValue;

            var previousNext = next;
            next = new GenericLinkedListNode<>(oldValue, this, previousNext);
        } else {
            next.add(--i, newValue);
        }
    }

    public int indexOf(T searchedValue) {
        if (value == searchedValue)
            return 0; //We have found it

        if (next == null)
            return -1; //We are at the end and haven't found it

        int index = next.indexOf(searchedValue);
        //Pass the -1 along
        if (index == -1)
            return -1;

        //Count up when traversing back to the head
        return ++index;
    }

    /**
     * Removes this node from the list, by connecting the previous and next with each other
     *
     * @return The previous one if no next one is found, the next one if no previous is found and null if it was in the middle
     */
    public GenericLinkedListNode<T> remove() {
        if (previous != null) {
            previous.next = next;
        }
        if (next != null) {
            next.previous = previous;
        }

        if (previous != null && next == null)
            return previous;
        if (previous == null && next != null)
            return next;
        return null;
    }

    public int size() {
        //Check if we are at the head of the queue, and we are empty => size 0
        if (value == null)
            return 0;

        //Check if we are the last element
        if (next == null)
            return 1; //Return 1 since we are also one element

        return next.size() + 1;
    }

    public T get(int i) {
        if (i == 0)
            return value;
        return next.get(--i);
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator(this);
    }

    public class LinkedListIterator implements Iterator<T> {

        private boolean traversed = false;
        private final GenericLinkedListNode<T> node;
        private Iterator<T> next;

        public LinkedListIterator(GenericLinkedListNode<T> node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            if (!traversed)
                return node.getValue() != null;

            return next != null && next.hasNext();
        }

        @Override
        public T next() {
            if (!traversed) {
                next = node.next != null ? node.next.iterator() : null;
                traversed = true;
                return node.getValue();
            }

            return next != null ? next.next() : null;
        }
    }
}
