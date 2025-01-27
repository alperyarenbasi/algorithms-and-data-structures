public class Node {
    String element;
    Node next;

    public Node(String element, Node next) {
        this.element = element;
        this.next = next;
    }

    public String getElement(){
        return element;
    }

    public Node getNext(){
        return next;
    }
}
