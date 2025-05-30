package main_1;
public class Seat {
    private boolean occupied;
    private String occupant_name;

    public Seat() {
        this.occupied = false;
        this.occupant_name = null;
    }

    public void set_occupied(boolean input_occupied) {
        this.occupied = input_occupied;
    }

    public void set_occupant_name(String input_occupant_name) {
        if (this.occupied == false && input_occupant_name != null) {
            throw new IllegalStateException("Cannot have occupant name if seat is empty. ");
        }
        this.occupant_name = input_occupant_name;
    } 

    public boolean is_occupied() {
        return this.occupied;
    }

    public String get_occupant_name() {
        return this.occupant_name;
    }
}
