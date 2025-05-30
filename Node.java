package main_1;
public class Node<T> {
    private T value;
    private Node<T> previous;
    private Node<T> next;

    public Node (T input) {
        this.value = input;
        this.previous = null;
        this.next = null;
    }

    public Node (Node<T> input_previous, T input_value, Node<T> input_next) {
        this.previous = input_previous;
        this.value = input_value;
        this.next = input_next;
    }

    public void set_next(Node<T> input) {
        this.next = input;
    }

    public void set_previous(Node<T> input) {
        this.previous = input;
    }

    public void set_value(T input) {
        this.value = input;
    }

    public T get_value() {
        return this.value;
    }

    public Node<T> get_next() {
        return this.next;
    }

    public Node<T> get_previous() {
        return this.previous;
    }

    public void delete() {
        this.previous = null;
        this.value = null;
        this.next = null;
    }
}