package main_1;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Movie {
    private String name;
    private int length_in_minutes;

    @JsonProperty("name")
    public String get_name() {
        return this.name;
    }

    public void set_name(String input_name) {
        if (input_name == null) {
            throw new IllegalArgumentException("Name of movie can't be null. ");
        }
        this.name = input_name;
    }

    @JsonProperty("length_in_minutes")
    public int get_length_in_minutes() {
        return this.length_in_minutes;
    }
    
    public void set_length_in_minutes(int input_length) {
        if (input_length <= 0) {
            throw new IllegalArgumentException("Length of movie in minutes must be positive. ");
        }
        this.length_in_minutes = input_length;
    }

    public String toString() {
        return this.name;
    }
}