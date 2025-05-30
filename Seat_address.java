package main_1;
public class Seat_address {
    private long screening_ident;
    private int dim_1_row_pos;
    private int dim_2_column_pos;

    public void set_screening_id(long input_screening_ident) {
        this.screening_ident = input_screening_ident;
    }

    public void set_dim_1_row_pos(int input_dim_1_row_pos) {
        this.dim_1_row_pos = input_dim_1_row_pos;
    }

    public void set_dim_2_column_pos(int input_dim_2_column_pos) {
        this.dim_2_column_pos = input_dim_2_column_pos;
    }

    public long get_screening_ident() {
        return this.screening_ident;
    }

    public int get_dim_1_row_pos() {
        return this.dim_1_row_pos;
    }

    public int get_dim_2_column_pos() {
        return this.dim_2_column_pos;
    }
}