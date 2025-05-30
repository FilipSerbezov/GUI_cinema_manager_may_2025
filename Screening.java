package main_1;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
@JsonPropertyOrder({"room_id", "start", "movie", "seat_info", "ident"})
public class Screening {
    private Date_and_time start;
    private Integer room_id;
    private String movie_name;
    private Seat[][] seat_info; 
    private long ident;
    private File screening_file = new File("screenings.json"); 
    private File screening_index_file = new File("screening_number.txt");

    public Screening() throws IOException {
        this.movie_name = null;
        this.room_id = null;
        this.start = null;
        this.seat_info = null;
        long number_in_file = get_number(screening_index_file);
        if (number_in_file == 0) {
            throw new IOException("Something wrong happened when reading the file storing the identification number to put on the screening being instantiated. ");
        }
        this.ident = number_in_file + 1;
        increment_number(screening_index_file);
    }

    @JsonProperty("ident")
    public long get_ident() {
        return this.ident;
    }

    private long get_number(File number_file) throws NumberFormatException, IOException {
         
        BufferedReader number_reader = new BufferedReader(new FileReader(number_file));
        long read_number = Long.parseLong(number_reader.readLine(), 16);
        number_reader.close();
        return read_number;
        
        // return 0;
    }

    @JsonProperty("start")
    public Date_and_time get_start() {
        return this.start;
    }

    void set_ident(long input_ident) {
        this.ident = input_ident;
    }

    private void increment_number(File number_file) throws NumberFormatException, IOException {
        long number_in_file = get_number(number_file);
        BufferedWriter number_writer = new BufferedWriter(new FileWriter(number_file));
        number_writer.write(Long.toString(number_in_file, 16));
        number_writer.close();
        
    }

    @JsonProperty("room_id")
    public Integer get_room_id() {
        return this.room_id;
    }

    public static Screening get_screening_by_id(long search_ident, File screening_file, gui_cinema_manager_1 application) {
        JsonFactory json_factory = new JsonFactory();
        try (JsonParser screening_reader = json_factory.createParser(screening_file);) {

            if (screening_reader.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected JSON array at root");
            }

            while (screening_reader.nextToken() == JsonToken.START_OBJECT) {
                Date_and_time store_start = new Date_and_time();
                int store_room_id = 0;
                String store_movie_name = null;
                Seat[][] store_seat_info = null;
                lvl_2_loop:
                while (screening_reader.nextToken() != JsonToken.END_OBJECT) {
                    if (screening_reader.currentToken() == JsonToken.FIELD_NAME) {
                        screening_reader.nextToken();
                        String field_name = screening_reader.currentName();
                        if (field_name.equals("ident")) {
                            long current_ident = screening_reader.getValueAsLong();
                            if (current_ident != search_ident) {
                                break lvl_2_loop;
                            }
                        } else if (field_name.equals("start")) {
                            store_start = home_state.read_date_time(screening_reader);
                        } else if (field_name.equals("room")) {
                            store_room_id = screening_reader.getValueAsInt();
                        } else if (field_name.equals("movie_name")) {
                            store_movie_name = screening_reader.getValueAsString();
                        } else if (field_name.equals("seat_info")) {
                            if (store_room_id != 0) {
                                Cinema_room current_room = home_state.get_cinema_room_with_id(store_room_id, application);
                                int dim_1_row_length = current_room.get_length_of_dim_1_rows();
                                int dim_2_column_length = current_room.get_length_of_dim_2_columns();
                                store_seat_info = home_state.read_seat_info(screening_reader, dim_1_row_length, dim_2_column_length);
                            }
                        }
                    }
                }
                Screening current_screening = new Screening();
                //Movie current_movie = home_state.get_movie_with_name(store_movie_name, application);
                current_screening.set_movie_name(store_movie_name);
                current_screening.set_room_id(store_room_id);
                current_screening.set_seat_info(store_seat_info, application);
                current_screening.set_start(store_start);
                current_screening.set_ident(search_ident);
                return current_screening;
            }
        } catch (IOException e) {
            
            e.printStackTrace();
        }
        return null;
    }

    public Screening(String input_movie_name, int input_room_id, Date_and_time input_start, Seat[][] input_seat_arr, gui_cinema_manager_1 application) {
        if (!(input_seat_arr.length == home_state.get_cinema_room_with_id(input_room_id, application).get_length_of_dim_1_rows() && input_seat_arr[0].length == home_state.get_cinema_room_with_id(input_room_id, application).get_length_of_dim_2_columns())) {
            throw new IllegalArgumentException("The array storing information about which seats are occupied has dimensions that don't match with those of the corresponding room. ");
        }
        this.movie_name = input_movie_name;
        this.room_id = input_room_id;
        this.start = input_start;
        this.seat_info = input_seat_arr;
    }

    @JsonProperty("seat_info")
    public Seat[][] get_seat_info() {
        return this.seat_info;
    }

    public void set_seat_info(Seat[][] input_seat_info, gui_cinema_manager_1 application) {
        if (
            input_seat_info.length != home_state.get_cinema_room_with_id(room_id, application).get_length_of_dim_1_rows() 
            || input_seat_info[0].length != home_state.get_cinema_room_with_id(room_id, application).get_length_of_dim_2_columns()
        ) {
            throw new IllegalArgumentException("Dimensions of the input array do not match those of the cinema room. ");
        }
        this.seat_info = input_seat_info;
    }

    public void set_movie_name(String input_movie_name) {
        this.movie_name = input_movie_name;
    }

    public void set_room_id(Integer input_room_id) {
        this.room_id = input_room_id;
    }

    public void set_start(Date_and_time input_start) {
        this.start = input_start;
    }
    
    @JsonProperty("movie_name")
    public String get_movie_name() {
        return this.movie_name;
    }

    @Override
    public String toString() {
        return movie_name + ", (" + start.get_year() + "/" + start.get_month() + "/" + start.get_day_in_month() + "); (" + start.get_hour() + ":" + start.get_minute() + "); ";
    }

}