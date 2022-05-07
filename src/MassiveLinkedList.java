import java.util.Collections;
import java.util.Iterator;

// A list of massive objects implemented as a linked list.
// The number of elements of the list is not limited.
public class MassiveLinkedList implements Iterable<Massive>{

    private GenericLinkedListNode<Massive> head, tail;

    // Initializes 'this' as an empty list.
    public MassiveLinkedList() {
    }

    // Initializes 'this' as an independent copy of the specified list 'list'.
    // Calling methods of this list will not affect the specified list 'list'
    // and vice versa.
    // Precondition: list != null.
    public MassiveLinkedList(BodyLinkedList list) {
        var size = list.size();
        for (int i = 0; i < size; i++) {
            addLast(list.get(i));
        }
    }

    // Inserts the specified element 'body' at the beginning of this list.
    public void addFirst(Massive body) {
        if (head == null) {
            head = new GenericLinkedListNode<>(body);
            tail = head;
        } else {
            head = head.appendBefore(body);
        }
    }

    // Appends the specified element 'body' to the end of this list.
    public void addLast(Massive body) {
        if (tail == null) {
            //This list is empty
            addFirst(body);
        } else {
            tail = tail.appendAfter(body);
        }
    }

    // Returns the last element in this list.
    // Returns 'null' if the list is empty.
    public Massive getLast() {
        if (tail == null)
            return null;
        return tail.getValue();
    }

    // Returns the first element in this list.
    // Returns 'null' if the list is empty.
    public Massive getFirst() {
        if (head == null)
            return null;

        return head.getValue();
    }

    // Retrieves and removes the first element in this list.
    // Returns 'null' if the list is empty.
    public Massive pollFirst() {
        if (head == null)
            return null; //List is empty

        var value = head.getValue();
        if (head == tail) {
            //this list only has one element
            head = null;
            tail = null;

            return value;
        }
        head = head.remove();

        return value;
    }

    // Retrieves and removes the last element in this list.
    // Returns 'null' if the list is empty.
    public Massive pollLast() {
        if (tail == null)
            return null; //List is empty

        var value = tail.getValue();
        if (head == tail) {
            //this list only has one element
            head = null;
            tail = null;

            return value;
        }

        tail = tail.remove();

        return value;
    }

    // Inserts the specified element at the specified position in this list.
    // Precondition: i >= 0 && i <= size().
    public void add(int i, Massive m) {
        if (head == null) {
            if (i != 0)
                throw new IndexOutOfBoundsException();
            addFirst(m);
        }

        head.add(i, m);

        //Check if the tail is still correct
        if (tail.getNext() != null)
            tail = tail.getNext();
    }

    // Returns the element at the specified position in this list.
    // Precondition: i >= 0 && i < size().
    public Massive get(int i) {
        if (head == null)
            throw new IndexOutOfBoundsException();

        return head.get(i);
    }

    // Returns the index of the first occurrence of the specified element in this list, or -1 if
    // this list does not contain the element.
    public int indexOf(Massive m) {
        if (head == null)
            throw new IndexOutOfBoundsException();

        return head.indexOf(m);
    }

    // Returns the number of elements in this list.
    public int size() {
        if (head == null)
            return 0;
        return head.size();
    }

    @Override
    public Iterator<Massive> iterator() {
        return head != null ? head.iterator() : Collections.emptyIterator();
    }
}
