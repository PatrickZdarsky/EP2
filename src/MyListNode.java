/**
 * @author Patrick Zdarsky / Rxcki
 */
public class MyListNode {

    private Body value;
    private MyListNode next;


    public MyListNode() {}

    public MyListNode(MyListNode toCopy) {
        value = toCopy.value;

        //Copy the next one if it is not null
        if (toCopy.next != null)
            next = new MyListNode(toCopy.next);
    }

    public Body getValue() {
        return value;
    }

    // Returns the first element in this list.
    // Returns 'null' if the list is empty.
    //public Body getFirst() {
    //    return value;
    //}

    /**
     * Inserts the specified body at this node and shifts the other values back.
     * @param body The body to add
     */
    public void addFirst(Body body) {
        Body oldValue = value;
        value = body;

        if (next != null) {
            next.addFirst(oldValue);
        } else {
            next = new MyListNode();
            next.value = oldValue;
        }
    }

    /**
     * Appends the specified body to the end of these linked nodes
     * @param body The body to add
     */
    public void addLast(Body body) {
        //Check if we are the head and are empty ==> add it to ourself
        if (value == null) {
            value = body;
            return;
        }

        if (next == null) {
            next = new MyListNode();
            next.value = body;
        } else {
            //Pass it along
            next.addLast(body);
        }
    }

    /**
     * Retrieves the last Body in this linked node list
     * @return The last value or null if none exists.
     */
    public Body getLast() {
        if (next == null)
            return value;

        //Pass it along
        return next.getLast();
    }


    // Retrieves and removes the first element in this list.
    // Returns 'null' if the list is empty.
    public Body pollFirst() {
        Body first = value;
        if (next != null) {
            //Pull the next value to this one
            value = next.value;
            //Set our current next entry as the next's next entry, basically removing it from our list
            next = next.next;
        } else {
            value = null;
        }

        return first;
    }

    // Retrieves and removes the last element in this list.
    // Returns 'null' if the list is empty.
    public Body pollLast() {
        if (next == null) {
            Body last = value;
            value = null;
            return last;
        } else if (next.next == null) {
            //We are at the second to last entry => remove the next one
            Body last = next.value;
            next = null;

            return last;
        }

        return next.pollLast();
    }

    // Inserts the specified element 'body' at the specified position in this list.
    // Precondition: i >= 0 && i <= size().
    public void add(int i, Body body) {
        if (i == 0) {
            //Append it right here
            Body oldValue = value;
            value = body;

            MyListNode previousNext = next;
            next = new MyListNode();
            next.value = oldValue;
            next.next = previousNext;
        } else {
            next.add(--i, body);
        }
    }

    // Returns the element at the specified position in this list.
    // Precondition: i >= 0 && i < size().
    public Body get(int i) {
        if (i == 0)
            return value;
        return next.get(--i);
    }

    public MyListNode getNode(int i) {
        if (i == 0)
            return this;
        return next.getNode(--i);
    }

    void setNext(MyListNode node) {
        this.next = node;
    }

    // Returns the index of the first occurrence of the specified element in this list, or -1 if
    // this list does not contain the element.
    public int indexOf(Body body) {
        if (value == body)
            return 0; //We have found it

        if (next == null)
            return -1; //We are at the end and haven't found it

        int index = next.indexOf(body);
        //Pass the -1 along
        if (index == -1)
            return -1;

        //Count up when traversing back to the head
        return ++index;
    }

    // Removes all bodies of this list, which are colliding with the specified
    // body. Returns a list with all the removed bodies.
    public MyListNode removeCollidingWith(Body body) {
        MyListNode removed = null;

        //Check if we are at the second-to-last since we cannot remove the last one from itself
        if (next != null && next.next == null) {
            //Check if we are colliding
            if (next.value.distanceTo(body) < next.value.radius() + body.radius()) {
                if (removed == null)
                    removed = new MyListNode();
                removed.addLast(body);


                next = null;
            }

            return removed;
        }

        if (next != null) {
            removed = next.removeCollidingWith(body);
        }

        //Check if we are colliding
        if (value.distanceTo(body) < value.radius() + body.radius()) {
            if (removed == null)
                removed = new MyListNode();
            removed.addLast(body);

            //Remove this body by taking the next one and inserting it into this one, overwriting this one in the process
            if (next == null) {
                value = null;
            } else {
                value = next.value;
                next = next.next;
            }
        }

        return removed;
    }

    // Returns the number of bodies in this list.
    public int size() {
        //Check if we are at the head of the queue and we are empty => size 0
        if (value == null)
            return 0;

        //Check if we are the last element
        if (next == null)
            return 1; //Return 1 since we are also one element

        return next.size() + 1;
    }
}
