public class LinkedListOwn {
    private Node head = null;
    private int numberOfEl = 0;

    public int getNumberOfElements() {
        return numberOfEl;
    }

    public Node getHead() {
        return head;
    }

    public void insertFront(String name){
        head = new Node(name, head);
        ++numberOfEl;
    }

    public void insertBack(String name) {
        if (head != null) {
            Node current = head;
            while (current.getNext() != null) current = current.next;
            current.next = new Node(name, null);
        } else head = new Node(name, null);
        ++numberOfEl;
    }

    public Node remove(Node n) {
        Node previous = null;
        Node current = head;
        while (current != null && current != n) {
            previous = current;
            current = current.next;
        }
        if (current != null){
            if(previous != null) previous.next = current.next;
            else head = current.next;
            --numberOfEl;
            return current;
        }
        else return null;
    }

    public boolean contains(String name) {
        Node current = head;
        while (current != null) {
            if (current.getElement().equals(name)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    public void printList() {
        Node current = head;
        while (current != null) {
            System.out.print(current.getElement() + " -> ");
            current = current.getNext();
        }
        System.out.println("null");
    }

}
