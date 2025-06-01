package main_1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.gson.JsonParseException;

class home_state extends gui_cinema_manager_state {
    

    home_state(gui_cinema_manager_state prev_state) {
        super(prev_state);
    } 

    static void add_months_to_combobox(JComboBox<Date_month> input_combobox, int year) {
        int iter_index = 0;
        while (iter_index < 12) {
            if (iter_index == 1) {
                if (year % 4 == 0) {
                    input_combobox.addItem(Date_month.LEAP_FEBRUARY);
                } else {
                    input_combobox.addItem(Date_month.FEBRUARY);
                }
            } else {
                input_combobox.addItem(Date_month.values()[iter_index]);
            }
            iter_index += 1;
        }
    }

    static void show_invalid_year_dialogue_box(JPanel panel) {
        JOptionPane.showMessageDialog(panel, "The text you entered in the year field is not a number. ", "Invalid year. ", JOptionPane.ERROR_MESSAGE);
    }

    private void sort_desc_by_start(Linked_list<Screening> screening_list, Date_time_comparator comparator) {
        boolean finished = false;
        int length = screening_list.length();
        while (!finished) {
            finished = true;
            int iter_index = 0; 
            while (iter_index < length - 1) {
                if (comparator.compare(screening_list.get(iter_index).get_start(), screening_list.get(iter_index + 1).get_start()) < 0) {
                    screening_list.swap_entries(iter_index, iter_index + 1);
                    finished = false;
                }
                iter_index += 1;
            }
        }
    }
    
    private void show_invalid_room_id_dialogue_box() {
        JOptionPane.showMessageDialog(panel, "The text you entered in the year field is not a number. ", "Invalid room id. ", JOptionPane.ERROR_MESSAGE);
    }

    public void render_state() {
        JButton enter_screening_button = new JButton("Book");
        enter_screening_button.setBounds(450, 400, 130, 30);
        JComboBox<Screening> screening_combobox = new JComboBox<>();
        screening_combobox.setBounds(100, 300, 400, 30);
        Linked_list<Screening> display_screening_list = new Linked_list<>();
        extract_screenings_to_list(application.get_screening_file(), display_screening_list);
        Date_time_comparator date_comparator = new Date_time_comparator();
        sort_desc_by_start(display_screening_list, date_comparator);
        add_elements_of_list_to_combobox(display_screening_list, screening_combobox);
        enter_screening_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear_state();
                application.set_state(new book_ticket_state(home_state.this, (Screening) screening_combobox.getSelectedItem()));
                application.get_state().render_state();
            }
        });
        panel.add(screening_combobox);
        
        JLabel room_id_label = new JLabel("room id: ");
        room_id_label.setBounds(130, 340, 120, 30);
        panel.add(room_id_label);

        JTextField room_id_field = new JTextField();
        room_id_field.setBounds(200, 340, 100, 30);
        panel.add(room_id_field);

        JTextField search_field = new JTextField();
        search_field.setBounds(200, 380, 100, 30);
        panel.add(search_field);

        JLabel search_label = new JLabel();
        search_label.setText("search: ");
        search_label.setBounds(130, 380, 100, 30);
        panel.add(search_label);
        
        Date_and_time selected_date_and_time = new Date_and_time();
        Integer[] room_id_wrapper = new Integer[1];
        JButton enter_search_button = new JButton("enter");
        enter_search_button.setBounds(300, 380, 120, 30);
        String[] search_term_wrapper = new String[1];
        enter_search_button.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    search_term_wrapper[0] = search_field.getText();
                    update_combobox(screening_combobox, selected_date_and_time, room_id_wrapper[0], search_term_wrapper[0]);
                }
            }
        );
        panel.add(enter_search_button);

        JButton enter_room_id_button = new JButton("enter");
        enter_room_id_button.setBounds(300, 340, 100, 30);
        enter_room_id_button.addActionListener(
            new ActionListener() {
                @Override 
                public void actionPerformed(ActionEvent e) {
                    if (!room_id_field.getText().matches("^\\s*[+-]?\\d+\\s*$")) {
                        show_invalid_room_id_dialogue_box();
                    }
                    room_id_wrapper[0] = Integer.parseInt(room_id_field.getText());
                    update_combobox(screening_combobox, selected_date_and_time, room_id_wrapper[0], search_term_wrapper[0]);
                }

            }
        );
        panel.add(enter_room_id_button);
        panel.setLayout(null);
        JLabel year_label = new JLabel();
        year_label.setBounds(50, 100, 50, 30);
        year_label.setText("year: ");
        panel.add(year_label);
        JTextField year_field = new JTextField();
        year_field.setBounds(100, 100, 50, 30);
        panel.add(year_field);
        JLabel month_label = new JLabel("month: ");
        month_label.setBounds(50, 150, 70, 30);
        panel.add(month_label);
        JComboBox<Date_month> month_combobox = new JComboBox<>();
        month_combobox.setBounds(100, 150, 80, 30);
        JSpinner day_spinner = new JSpinner();
        JButton enter_year_button = new JButton("Enter");
        enter_year_button.setBounds(150, 100, 80, 30);
        enter_year_button.addActionListener(
            new ActionListener() {
                @Override 
                public void actionPerformed(ActionEvent e) {
                    if (!year_field.getText().matches("^\\s*[+-]?\\d+\\s*$")) {
                        show_invalid_year_dialogue_box(panel);
                    }
                    selected_date_and_time.set_year(Integer.parseInt(year_field.getText()));
                    add_months_to_combobox(month_combobox, Integer.parseInt(year_field.getText())); 
                    month_combobox.addActionListener(
                        new ActionListener() {
                            @Override 
                            public void actionPerformed(ActionEvent e) {
                                selected_date_and_time.set_month((Date_month) month_combobox.getSelectedItem());
                                SpinnerModel day_spinner_model = new SpinnerNumberModel(1, 1, ((Date_month) month_combobox.getSelectedItem()).get_number_of_days(), 1);
                                day_spinner.setModel(day_spinner_model);
                                JLabel day_label = new JLabel("day: ");
                                day_label.setBounds(200, 150, 70, 30);
                                panel.add(day_label);
                                day_spinner.setBounds(250, 150, 50, 30);
                                panel.add(day_spinner);
                                JButton enter_day_button = new JButton("Enter");
                                enter_day_button.setBounds(300, 150, 100, 30);
                                enter_day_button.addActionListener(
                                    new ActionListener() {
                                        @Override 
                                        public void actionPerformed(ActionEvent e) {
                                            selected_date_and_time.set_day_in_month((int) day_spinner.getValue());
                                            JSpinner hour_spinner = new JSpinner();
                                            SpinnerModel hour_spinner_model = new SpinnerNumberModel(1, 0, 23, 1);
                                            JLabel hour_label = new JLabel("hour: ");
                                            hour_label.setBounds(50, 200, 70, 30);
                                            panel.add(hour_label);
                                            hour_spinner.setBounds(100, 200, 70, 30);
                                            hour_spinner.setModel(hour_spinner_model);
                                            panel.add(hour_spinner);
                                            JButton enter_hour_button = new JButton("Enter");
                                            enter_hour_button.setBounds(170, 200, 100, 30);
                                            enter_hour_button.addActionListener(
                                                new ActionListener() {
                                                    @Override 
                                                    public void actionPerformed(ActionEvent e) {
                                                        selected_date_and_time.set_hour((int) hour_spinner.getValue());
                                                        update_combobox(screening_combobox, selected_date_and_time, room_id_wrapper[0], search_term_wrapper[0]);
                                                    }
                                                }
                                            );
                                            panel.add(enter_hour_button);
                                            update_combobox(screening_combobox, selected_date_and_time, room_id_wrapper[0], search_term_wrapper[0]);

                                        }
                                    }
                                );
                                panel.add(enter_day_button);
                                update_combobox(screening_combobox, selected_date_and_time, room_id_wrapper[0], search_term_wrapper[0]);
                            }
                        }
                    );
                    panel.add(month_combobox);
                    update_combobox(screening_combobox, selected_date_and_time, room_id_wrapper[0], search_term_wrapper[0]);
                }
            }
        );
        panel.add(enter_year_button);
        panel.add(enter_screening_button);

        boolean current_user_is_admin = false;
        
        if (Cinema_account.is_there_account_with_username(application.get_account_file(), application.get_current_user())) {
            current_user_is_admin = Cinema_account.is_user_with_name_admin(application.get_account_file(), application.get_current_user());
        } else {
            throw new NullPointerException("Account not found when checking if you are admin. ");
        }

        if (current_user_is_admin) {
            JButton add_screening_button = new JButton("add screening");
            add_screening_button.setBounds(400, 50, 200, 30);
            add_screening_button.addActionListener(
                new ActionListener() {
                    @Override 
                    public void actionPerformed(ActionEvent e) {
                        application.set_state(new add_screening_state(home_state.this));
                        home_state.this.clear_state();
                        application.get_state().render_state();
                    }
                }
            );
            panel.add(add_screening_button);

            JButton add_movie_button = new JButton("add movie");
            add_movie_button.setBounds(400, 100, 200, 30);
            add_movie_button.addActionListener(
                new ActionListener() {
                    @Override 
                    public void actionPerformed(ActionEvent e) {
                        application.set_state(new add_movie_state(home_state.this));
                        home_state.this.clear_state();
                        application.get_state().render_state();
                    }
                }
            );
            panel.add(add_movie_button);

            JButton add_room_button = new JButton("add room");
            add_room_button.setBounds(400, 150, 200, 30);
            add_room_button.addActionListener(
                new ActionListener() {
                    @Override 
                    public void actionPerformed(ActionEvent e) {
                        application.set_state(new add_room_state(home_state.this));
                        home_state.this.clear_state();
                        application.get_state().render_state();
                    }
                }
            );
            panel.add(add_room_button);

            JButton make_admin_button = new JButton("make admin");
            make_admin_button.setBounds(400, 200, 200, 30);
            make_admin_button.addActionListener(
                new ActionListener() {
                    @Override 
                    public void actionPerformed(ActionEvent e) {
                        application.set_state(new make_admin_state(home_state.this));
                        home_state.this.clear_state();
                        application.get_state().render_state();
                    }
                }
            );
            panel.add(make_admin_button);
        }
    }

    private void update_combobox(JComboBox<Screening> input_combobox, Date_and_time input_date_and_time, Integer room_ident, String search_term) {
        Linked_list<Screening> screening_list = new Linked_list<>();
        JsonFactory json_factory = new JsonFactory();
        try (JsonParser screening_reader = json_factory.createParser(application.get_screening_file())) {
            
            if (screening_reader.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected JSON array at root");
            }
            
            while (screening_reader.nextToken() == JsonToken.START_OBJECT) { // enter new screening
                Screening current_screening = read_screening(screening_reader);
                if (!(input_date_and_time.get_year() == null) && !(current_screening.get_start().get_year().equals(input_date_and_time.get_year()))) {

                } else if (input_date_and_time.get_month() != null && !(current_screening.get_start().get_month().equals(input_date_and_time.get_month()))) {
                    
                } else if (
                    input_date_and_time.get_day_in_month() != null 
                    && !(
                        current_screening
                        .get_start()
                        .get_day_in_month()
                        .equals(
                            input_date_and_time
                            .get_day_in_month()
                        )
                    )
                ) {
                    
                } else if (input_date_and_time.get_hour() != null && !(current_screening.get_start().get_hour().equals(input_date_and_time.get_hour()))) {
                    
                } else if (room_ident != null && !(room_ident.equals(current_screening.get_room_id()))) {

                } else if (search_term != null && !(current_screening.get_movie_name().contains(search_term))) {

                } else {
                    screening_list.add_at_the_end(current_screening);
                }
            } 

            input_combobox.removeAllItems();
            Linked_list<Screening> sorted_screening_list = (screening_list).copy_list();
            Date_time_comparator comparator = new Date_time_comparator();
            sort_desc_by_start(sorted_screening_list, comparator);
            add_elements_of_list_to_combobox(sorted_screening_list, input_combobox);
            
        } catch (IOException adsks) {
            adsks.printStackTrace();
        }
        
    }

    public Screening read_screening(JsonParser parser) throws IOException {
        Screening current_screening = new Screening();
        //int store_room_number = -1;
        long current_ident = 0;
        Movie current_movie = new Movie();
        Integer room_id = null;
        Seat[][] current_seat_info = null;
        Date_and_time read_date_time = new Date_and_time();
        
        
        while (parser.nextToken() != JsonToken.END_OBJECT) { // enter new field in screening
            if (parser.currentToken() == JsonToken.FIELD_NAME) {
                parser.nextToken();
                String field_name = parser.currentName();
                
                if (field_name.equals("ident")) {
                    current_ident = parser.getValueAsLong();
                } else if (field_name.equals("movie_name")) {
                    String current_movie_name = parser.getValueAsString();//read_movie(parser);
                    current_movie = get_movie_with_name(current_movie_name, this.application);
                    current_screening.set_movie_name(current_movie_name);
                } else if (field_name.equals("start")) {
                    read_date_time = read_date_time(parser);
                    
                } else if (field_name.equals("room_id")) {
                    room_id = parser.getValueAsInt();
                    //current_room = get_cinema_room_with_id(store_room_number, this.application);
                } else if (field_name.equals("seat_info")) {
                    current_seat_info = read_seat_info(parser, get_cinema_room_with_id(room_id, application).get_length_of_dim_1_rows(), get_cinema_room_with_id(room_id, application).get_length_of_dim_2_columns());
                }
            }    
        }
        
        current_screening.set_ident(current_ident);
        //current_screening.set_movie_name(current_movie_name);
        current_screening.set_room_id(room_id);
        current_screening.set_seat_info(current_seat_info, application);
        current_screening.set_start(read_date_time);
        return current_screening;
    }
    
    public void extract_screenings_to_list(File input_screening_file, Linked_list<Screening> input_list) {
    JsonFactory json_factory = new JsonFactory();
    try (JsonParser screening_reader = json_factory.createParser(input_screening_file)) {
        
        if (screening_reader.nextToken() != JsonToken.START_ARRAY) {
            throw new IllegalStateException("Expected JSON array at root");
        }
        
        while (screening_reader.nextToken() != JsonToken.END_ARRAY) {
            if (screening_reader.currentToken() == JsonToken.START_OBJECT) {
                Screening current_screening = read_screening(screening_reader);
                input_list.add_at_the_end(current_screening);
            } else {
                screening_reader.skipChildren(); 
            }
        } 
        
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    static Movie get_movie_with_name(String search_name, gui_cinema_manager_1 application) {
        JsonFactory json_factory = new JsonFactory();
        try (JsonParser account_reader = json_factory.createParser(application.get_movie_file())) {
            
            if (account_reader.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected JSON array at root");
            }

            while (account_reader.nextToken() == JsonToken.START_OBJECT) {
                String candidate_name = null;
                int read_length_in_minutes = 0;
                Movie output_movie = new Movie();
                boolean found = false;
                while (account_reader.nextToken() != JsonToken.END_OBJECT) {
                    if (account_reader.currentToken() == JsonToken.FIELD_NAME) {
                        account_reader.nextToken();
                        String field_name = account_reader.currentName();

                        if (field_name.equals("name")) {
                            candidate_name = account_reader.getValueAsString();
                            if (!candidate_name.equals(search_name)) {
                                break;
                            }
                            output_movie.set_name(candidate_name);
                            found = true;
                        } else if (field_name.equals("length_in_minutes")) {
                            read_length_in_minutes = account_reader.getValueAsInt();
                            output_movie.set_length_in_minutes(read_length_in_minutes);
                        } else {
                            account_reader.skipChildren();
                        }
                    }    
                }
                if (found) {
                    return output_movie;
                } else {
                    return null;
                }
            }
        } catch (IOException adsks) {
            adsks.printStackTrace();
        }
        return null;
    }

    static Date_and_time read_date_time(JsonParser date_time_reader) throws IOException {
        Date_and_time read_date_time = new Date_and_time();
        while (date_time_reader.nextToken() != JsonToken.END_OBJECT) { // enter new date_and_time
            if (date_time_reader.currentToken() == JsonToken.FIELD_NAME) {
                date_time_reader.nextToken();
                String field_name_lvl_1_p_2 = date_time_reader.currentName();
                if (field_name_lvl_1_p_2.equals("year")) {
                    int read_year = date_time_reader.getValueAsInt();
                    read_date_time.set_year(read_year);
                } else if (field_name_lvl_1_p_2.equals("month")) {
                    Date_month read_month = Date_month.from_name(date_time_reader.getValueAsString());
                    read_date_time.set_month(read_month);
                } else if (field_name_lvl_1_p_2.equals("day_in_month")) {
                    int day_in_month = date_time_reader.getValueAsInt();
                    read_date_time.set_day_in_month(day_in_month);
                } else if (field_name_lvl_1_p_2.equals("hour")) {
                    int read_hour = date_time_reader.getValueAsInt();
                    read_date_time.set_hour(read_hour);
                } else if (field_name_lvl_1_p_2.equals("minute")) {
                    int read_minute = date_time_reader.getValueAsInt();
                    read_date_time.set_minute(read_minute);
                }
            }
        }

        return read_date_time;
    }

    static Seat[][] read_seat_info(JsonParser seat_reader, int dim_1_length, int dim_2_length) throws IOException {
    Seat[][] current_seat_info = new Seat[dim_1_length][dim_2_length];

    if (seat_reader.currentToken() != JsonToken.START_ARRAY) {
        throw new JsonParseException( "Expected start of outer array");
    }

    for (int i = 0; i < dim_1_length; i++) {
        JsonToken token = seat_reader.nextToken();
        if (token != JsonToken.START_ARRAY) {
            throw new JsonParseException( "Expected start of inner array at row " + i);
        }

        for (int j = 0; j < dim_2_length; j++) {
            token = seat_reader.nextToken();

            if (token == JsonToken.VALUE_NULL) {
                current_seat_info[i][j] = null; 
            } else if (token == JsonToken.START_OBJECT) {
                current_seat_info[i][j] = read_current_seat(seat_reader);
            } else {
                throw new JsonParseException( 
                    "Expected start of seat object or null at position [" + i + "][" + j + "]");
            }
        }

        token = seat_reader.nextToken();
        if (token != JsonToken.END_ARRAY) {
            throw new JsonParseException("Expected end of inner array at row " + i);
        }
    }

    JsonToken endToken = seat_reader.nextToken();
    if (endToken != JsonToken.END_ARRAY) {
        throw new JsonParseException( "Expected end of outer array");
    }

    return current_seat_info;
}

    public static Seat read_current_seat(JsonParser seat_reader) throws IOException {
        boolean store_occupied = false;
        String store_occupant_name = null;
        while (seat_reader.nextToken() != JsonToken.END_OBJECT) {
            if (seat_reader.currentToken() == JsonToken.FIELD_NAME) {
                seat_reader.nextToken();
                String field_name = seat_reader.currentName();
                if (field_name.equals("occupied")) {
                    store_occupied = seat_reader.getValueAsBoolean();
                } else if (field_name.equals("occupant_name")) {
                    store_occupant_name = seat_reader.getValueAsString();
                }

            }
        }
        Seat current_seat = new Seat();
        current_seat.set_occupied(store_occupied);
        current_seat.set_occupant_name(store_occupant_name);
        return current_seat;
    }

    private Movie read_movie(JsonParser movie_reader) throws IOException {
        String store_movie_name = null;
        int store_length_in_minutes = 0;
        while(movie_reader.nextToken() != JsonToken.END_OBJECT) { // enter new movie 
            if (movie_reader.currentToken() == JsonToken.FIELD_NAME) {
                movie_reader.nextToken();
                String  field_name_lvl_1_p_1 = movie_reader.currentName();
                if ( field_name_lvl_1_p_1.equals("name")) {
                    store_movie_name = movie_reader.getValueAsString();
                } 
            }
        }
        Movie output_movie = new Movie();
        output_movie.set_name(store_movie_name);
        output_movie.set_length_in_minutes(store_length_in_minutes);
        return output_movie;
    }

    static Cinema_room get_cinema_room_with_id(int input_identification, gui_cinema_manager_1 input_application) {
        JsonFactory json_factory = new JsonFactory();
        try (JsonParser room_reader = json_factory.createParser(input_application.get_room_file())) {
            
            if (room_reader.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected JSON array at root");
            }

            Cinema_room output_room = new Cinema_room();
            while (room_reader.nextToken() == JsonToken.START_OBJECT) {
                boolean correct_one = false;
                int store_identification;
                int store_dim_1_row_length = -1;
                int store_dim_2_column_length = -1;
                field_loop:
                while (room_reader.nextToken() != JsonToken.END_OBJECT) {
                    if (room_reader.currentToken() == JsonToken.FIELD_NAME) {
                        room_reader.nextToken();
                        String field_name = room_reader.currentName();
                        
                        if (field_name.equals("identification")) {
                            store_identification = room_reader.getValueAsInt();
                            if (store_identification == input_identification) {
                                correct_one = true;
                            } else {
                                correct_one = false;
                            }

                            output_room.set_identification(input_identification);
                        } else if (correct_one && field_name.equals("length_of_dim_1_rows")) {
                            
                            store_dim_1_row_length = room_reader.getValueAsInt();
                            output_room.set_length_of_dim_1_rows(store_dim_1_row_length);

                        } else if (correct_one && field_name.equals("length_of_dim_2_columns")) {
                            store_dim_2_column_length = room_reader.getValueAsInt();
                            output_room.set_length_of_dim_2_columns(store_dim_2_column_length);
                        }
                        
                    }    
                }

                if (correct_one) {
                    return output_room;
                }
            }
            return null;
            
        } catch (IOException adsks) {
            adsks.printStackTrace();
        }
        return null; 
    } 
    

    static <T> void add_elements_of_list_to_combobox(Linked_list<T> input_list, JComboBox<T> input_combobox) {
        if (input_list.length() == 0) {
            return;
        }

        int iter_index = 0;
        while (iter_index < input_list.length()) {
            input_combobox.addItem(input_list.get(iter_index));
            iter_index += 1;
        }
    }
} 
