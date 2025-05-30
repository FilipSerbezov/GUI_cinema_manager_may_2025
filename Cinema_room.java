package main_1;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Cinema_room {
    private int identification;
    private int length_of_dim_1_rows; 
    private int length_of_dim_2_columns;

    public Cinema_room() {
        this.identification = -1;
        this.length_of_dim_1_rows = 0;
        this.length_of_dim_2_columns = 0;
    }

    @JsonProperty("length_of_dim_1_rows")
    public int get_length_of_dim_1_rows() {
        return this.length_of_dim_1_rows;
    }

    @JsonProperty("length_of_dim_2_columns")
    public int get_length_of_dim_2_columns() {
        return this.length_of_dim_2_columns;
    }

    @JsonProperty("identification")
    public int get_identification() {
        return this.identification;
    }
    
    public void set_length_of_dim_1_rows(int input_dim_1_rows_length) {
        if (input_dim_1_rows_length <= 0) {
            throw new IllegalArgumentException("Length of rows must be positive. ");
        }
        this.length_of_dim_1_rows = input_dim_1_rows_length;
    }

    public void set_length_of_dim_2_columns(int input_dim_2_columns_length) {
        if (input_dim_2_columns_length <= 0) {
            throw new IllegalArgumentException("Length of columns must be positive. ");
        }
        this.length_of_dim_2_columns = input_dim_2_columns_length;
    }

    public void set_identification(int input_identification) {
        this.identification = input_identification;
    }

    public String toString() {
        return ("room №" + identification + "; ");
    }
}