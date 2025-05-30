package main_1;
public class Linked_list<T> {
    private Node<T> first;
    private Node<T> last;
    private int length;

    public Linked_list() {
        this.first = null;
        this.last = null;
        this.length = 0;
    }

    public int length() {
        return this.length;
    }

    public Linked_list<T> copy_list() {
        Linked_list<T> output_list = new Linked_list<>();
        int iter_index = 0;
        while (iter_index < this.length) {
            output_list.add_at_the_end(this.get(iter_index));
            iter_index += 1;
        }
        return output_list;
    }

    public static <C>Linked_list<C> from_array(C[] array) {
        Linked_list<C> output_list = new Linked_list<>();
        int index = 0;
        while (index < array.length) {
            output_list.add_at_the_end(array[index]);
            index += 1;
        } 
        return output_list;
    }

    public void add_at_the_end(T input) {
        if (this.first == null) {
            this.first = new Node<T> (null, input, null);
            this.last = this.first;
            length += 1;
            return;
        }
        Node<T> extra_node = new Node<>(this.last, input, null); 
        this.last.set_next(extra_node);
        this.last = extra_node;
        length += 1;
    }

    public void add_at_the_beginning(T input) {
        if (this.first == null) {
            this.first = new Node<T> (null, input, null);
            this.last = this.first;
            return;
        }
        Node<T> extra_node = new Node<>(null, input, first); 
        this.first.set_previous(extra_node);
        this.first = extra_node;
        length += 1;
    }

    public void remove_first_element() {
        if (this.first == null) {
            throw new NullPointerException("\nCannot remove elements from an empty list. ");
        }
        Node<T> second_node = this.first.get_next();
        second_node.set_previous(null);
        this.first = second_node;
        length -= 1;
    }

    public void remove_last_element() {
        if (this.first == null) {
            throw new NullPointerException("\nCannot remove elements from an empty list. ");
        }
        if (this.length == 1) {
            this.first = null;
            this.last = null;
            this.length = 0;
            return;
        }
        Node<T> before_last_node = this.last.get_previous();
        before_last_node.set_next(null);
        this.last = before_last_node;
        length -= 1;
    }

    public void add_at_index(T element, int index) {
        Node<T> node_before_index = this.get_node_at_index(index - 1);
        Node<T> node_after_index = node_before_index.get_next().get_next();
        Node<T> additional_node = new Node<T>(node_before_index, element, node_after_index);
        node_before_index.set_next(additional_node);
        node_after_index.set_previous(additional_node);
        length += 1;
    }

    public void remove_at_index(int index) {
        Node<T> node_before_index = this.get_node_at_index(index - 1);
        Node<T> node_after_index = node_before_index.get_next().get_next();
        node_before_index.set_next(node_after_index);
        node_after_index.set_previous(node_before_index);
        length -= 1;
    }

    private Node<T> get_node_at_index(int index) {
        Node<T> node_at_index = this.first;
        int iterator = 0;
        while (iterator < index) {
            node_at_index = node_at_index.get_next();
            iterator += 1;
        } 
        return node_at_index;
    }

    public T get(int index) {
        return this.get_node_at_index(index).get_value();
    }

    public void set(T input, int index) {
        this.get_node_at_index(index).set_value(input);
    }
    
    public boolean contains(T search_term) {
        int index = 0;
        Node<T> current_node = this.first;
        while (index < this.length) {
            if (current_node.get_value() == search_term) {
                return true;
            }
            index += 1;
            current_node = current_node.get_next();
        }
        return false;
    }

    public String to_string() {
        StringBuilder strb = new StringBuilder();
        int index = 0;
        Node<T> current_node = this.first;
        while (index < this.length) {
            strb.append(current_node.get_value().toString() + "; ");
            index += 1;
            current_node = current_node.get_next();
        }
        return strb.toString();
    }

    public java.util.HashSet<T> get_set_of_elements() {
        java.util.HashSet<T> set_of_elements = new java.util.HashSet<>();
        int index = 0;
        while (index < this.length) {
            set_of_elements.add(this.get(index));
            index += 1;
        }
        return set_of_elements;
    }

    public void swap_entries(int index_1, int index_2) {
        T temporary = this.get(index_1);
        this.set(this.get(index_2), index_1);
        this.set(temporary, index_2);
    }

    // public T[] to_array() {
        
    //     T[] output_array = (T[]) (new Object[this.length]);
    //     int iter_index = 0;
        
    //     while (iter_index < this.length) {
    //         output_array[iter_index] = this.get(iter_index);
    //         iter_index += 1;
    //     }

    //     return output_array;
    // }
}
