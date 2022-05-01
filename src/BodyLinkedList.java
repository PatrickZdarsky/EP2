// A list of bodies implemented as a linked list.
// The number of elements of the list is not limited.
public class BodyLinkedList {

    private MyListNode headNode;

    // Initializes 'this' as an empty list.
    public BodyLinkedList() {}

    // Initializes 'this' as an independent copy of the specified list 'list'.
    // Calling methods of this list will not affect the specified list 'list'
    // and vice versa.
    // Precondition: list != null.
    public BodyLinkedList(BodyLinkedList list) {
        if (list.headNode != null)
            headNode = new MyListNode(list.headNode);
    }

    private BodyLinkedList(MyListNode listNode) {
        headNode = listNode;
    }

    public void removeSubList(int fromIndex, int toIndex) {
        if (toIndex <= fromIndex || fromIndex < 0 || fromIndex >= size())
            return;

        MyListNode fromNode = headNode.getNode(fromIndex);
        MyListNode toNode = headNode.getNode(toIndex);

        fromNode.setNext(toNode);
    }



    // Inserts the specified element 'body' at the beginning of this list.
    public void addFirst(Body body) {
        if (headNode == null)
            headNode = new MyListNode();

        headNode.addFirst(body);
    }

    // Appends the specified element 'body' to the end of this list.
    public void addLast(Body body) {
        if (headNode == null)
            headNode = new MyListNode();

        headNode.addLast(body);
    }

    // Returns the last element in this list.
    // Returns 'null' if the list is empty.
    public Body getLast() {
        return headNode == null ? null : headNode.getLast();
    }

    // Returns the first element in this list.
    // Returns 'null' if the list is empty.
    public Body getFirst() {
        return headNode == null ? null : headNode.getValue();
    }

    // Retrieves and removes the first element in this list.
    // Returns 'null' if the list is empty.
    public Body pollFirst() {
        return headNode == null ? null : headNode.pollFirst();
    }

    // Retrieves and removes the last element in this list.
    // Returns 'null' if the list is empty.
    public Body pollLast() {
        return headNode == null ? null : headNode.pollLast();
    }

    // Inserts the specified element 'body' at the specified position in this list.
    // Precondition: i >= 0 && i <= size().
    public void add(int i, Body body) {
        if (headNode == null)
            headNode = new MyListNode();

        headNode.add(i, body);
    }

    // Returns the element at the specified position in this list.
    // Precondition: i >= 0 && i < size().
    public Body get(int i) {
        return headNode == null ? null : headNode.get(i);
    }

    // Returns the index of the first occurrence of the specified element in this list, or -1 if
    // this list does not contain the element.
    public int indexOf(Body body) {
        return headNode == null ? -1 : headNode.indexOf(body);
    }

    // Removes all bodies of this list, which are colliding with the specified
    // body. Returns a list with all the removed bodies.
    public BodyLinkedList removeCollidingWith(Body body) {
        if (headNode == null)
            return null;

        return new BodyLinkedList(headNode.removeCollidingWith(body));
    }

    // Returns the number of bodies in this list.
    public int size() {
        return headNode == null ? 0 : headNode.size();
    }

    public void addAll(BodyLinkedList other) {
        int size = other.size();
        for (int i = 0; i < size; i++) {
            addLast(other.get(i));
        }
    }
}
