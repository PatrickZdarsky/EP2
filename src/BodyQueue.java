import java.util.Arrays;

// A queue of bodies. A collection designed for holding bodies prior to processing.
// The bodies of the queue can be accessed in a FIFO (first-in-first-out) manner,
// i.e., the body that was first inserted by 'add' is retrieved first by 'poll'.
// The number of elements of the queue is not limited.
//
public class BodyQueue {

    private Body[] bodies;
    private int index;

    // Initializes this queue with an initial capacity.
    // Precondition: initialCapacity > 0.
    public BodyQueue(int initialCapacity) {
        bodies = new Body[initialCapacity];
    }

    // Initializes this queue as an independent copy of the specified queue.
    // Calling methods of this queue will not affect the specified queue
    // and vice versa.
    // Precondition: q != null.
    public BodyQueue(BodyQueue q) {
        bodies = Arrays.copyOf(q.bodies, q.bodies.length);
        index = q.index;
    }

    // Adds the specified body 'b' to this queue.
    public void add(Body b) {

        //Check if the array is full
        if (index == bodies.length) {
            bodies = Arrays.copyOf(bodies, bodies.length*2);
        }

        bodies[index++] = b;
    }

    // Retrieves and removes the head of this queue, or returns 'null'
    // if this queue is empty.
    public Body poll() {
        if (index == 0)
            return null;

        Body result = bodies[0];

        //shift other entries
        for (int i = 0; i < index-1; i++) {
            bodies[i] = bodies[i+1];
        }

        //Decrease index since we have taken an element
        index--;

        return result;
    }

    // Returns the number of bodies in this queue.
    public int size() {
        return index;
    }
}
