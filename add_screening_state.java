package main_1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class add_screening_state extends gui_cinema_manager_state{

    add_screening_state(gui_cinema_manager_state prev_state) {
        super(prev_state);
    }

    
    protected void render_state() {
        panel.setLayout(null);
        process_frame.setContentPane(panel);
        JComboBox<Integer> room_combobox = new JComboBox<>();
        //JTextField start_year_field = new JTextField("year:");
        //JComboBox<Date_month> start_month_select = new JComboBox<>();
        //JComboBox<Integer> start_day_select = new JComboBox<>();
        Date_and_time selected_date_and_time = new Date_and_time();
        JComboBox<String> movie_combobox = new JComboBox<>();
        movie_combobox.setBounds(200, 290, 200, 30);
        Linked_list<String> display_movie_list = new Linked_list<>();
        extract_movie_names_to_list(application.get_movie_file(), display_movie_list);
        home_state.add_elements_of_list_to_combobox(display_movie_list, movie_combobox);
        JLabel room_id_label = new JLabel("room id: ");
        JLabel movie_label = new JLabel("movie: ");
        room_id_label.setBounds(100, 50, 100, 30);
        movie_label.setBounds(50, 290, 100, 30);
        room_combobox.setBounds(200, 50, 100, 30);
        Linked_list<Integer> room_id_list = new Linked_list<>();
        extract_room_ids_to_list(application.get_room_file(), room_id_list);
        home_state.add_elements_of_list_to_combobox(room_id_list, room_combobox);
        // start_year_field.setBounds(200, 200, 100, 30);
        // start_month_select.setBounds(300, 200, 100, 30);

        panel.add(room_combobox);
        // panel.add(start_year_field);
        // panel.add(start_month_select);
        // panel.add(start_day_select);
        panel.add(room_id_label);
        panel.add(movie_label);

        panel.add(movie_combobox);
        // panel.add(enter_room_id_button);
        // panel.setLayout(null);
        // JLabel year_label = new JLabel();
        // year_label.setBounds(50, 100, 50, 30);
        // year_label.setText("year: ");
        // panel.add(year_label);
        JButton add_screening_button = new JButton("add new screening");
        
        add_screening_button.setBounds(100, 400, 200, 30);
        add_screening_button.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected_movie_name = (String) movie_combobox.getSelectedItem();
                if (selected_movie_name == null) {
                    show_no_movie_selected_dialogue_box();
                    return;
                }

                Integer selected_room_id = (Integer) room_combobox.getSelectedItem();
                if (selected_room_id == null) {
                    show_no_room_selected_dialogue_box();
                    return;
                }
                Screening screening_to_add;
                try {
                    screening_to_add = new Screening();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "Couldn't read ID from the screening IDs file. Could be that the BufferedReader failed. ", "Problem when reading screening ID file", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                    return;
                }
                screening_to_add.set_movie_name(selected_movie_name);
                screening_to_add.set_room_id(selected_room_id);
                Seat[][] seat_info_arr = new Seat[
                    home_state
                    .get_cinema_room_with_id(
                        selected_room_id, 
                        application
                    )
                    .get_length_of_dim_1_rows()
                ][
                    home_state
                    .get_cinema_room_with_id(
                        selected_room_id, 
                        application
                    )
                    .get_length_of_dim_2_columns()
                ];
                populate_seat_arr_blank(seat_info_arr);
                screening_to_add.set_seat_info(seat_info_arr, application);
                screening_to_add.set_start(selected_date_and_time);
                write_screening_to_json(application.get_screening_file(), screening_to_add);
                show_screening_added_dialogue_box(selected_movie_name);
                
            }
        });
        panel.add(add_screening_button);

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
                        home_state.show_invalid_year_dialogue_box(panel);
                    }
                    selected_date_and_time.set_year(Integer.parseInt(year_field.getText()));
                    home_state.add_months_to_combobox(month_combobox, Integer.parseInt(year_field.getText())); 
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
                                                        JSpinner minute_spinner = new JSpinner();
                                                        SpinnerModel minute_spinner_model = new SpinnerNumberModel(1, 0, 59, 1);
                                                        JLabel minute_label = new JLabel("minute: ");
                                                        minute_label.setBounds(50, 240, 70, 30);
                                                        panel.add(minute_label);
                                                        minute_spinner.setBounds(100, 240, 70, 30);
                                                        minute_spinner.setModel(minute_spinner_model);
                                                        panel.add(minute_spinner);
                                                        JButton enter_minute_button = new JButton("Enter");
                                                        enter_minute_button.setBounds(170, 240, 100, 30);
                                                        enter_minute_button.addActionListener(
                                                            new ActionListener() {
                                                                @Override
                                                                public void actionPerformed(ActionEvent e) {
                                                                    selected_date_and_time.set_minute((int) minute_spinner.getValue());
                                                                }
                                                            }
                                                        );
                                                        panel.add(enter_minute_button);
                                                        //update_combobox(screening_combobox, selected_date_and_time, room_id_wrapper[0], search_term_wrapper[0]);
                                                    }
                                                }
                                            );
                                            panel.add(enter_hour_button);
                                            //update_combobox(screening_combobox, selected_date_and_time, room_id_wrapper[0], search_term_wrapper[0]);

                                        }
                                    }
                                );
                                panel.add(enter_day_button);
                                //update_combobox(screening_combobox, selected_date_and_time, room_id_wrapper[0], search_term_wrapper[0]);
                            }
                        }
                    );
                    panel.add(month_combobox);
                    //update_combobox(screening_combobox, selected_date_and_time, room_id_wrapper[0], search_term_wrapper[0]);
                }
            }
        );
        panel.add(enter_year_button);
        panel.add(add_screening_button);


        
    }

    protected void populate_seat_arr_blank(Seat[][] seat_info_arr) {
        int iter_index_1 = 0;
        while (iter_index_1 < seat_info_arr.length) {
            int iter_index_2 = 0;
            while (iter_index_2 < seat_info_arr[0].length) {
                seat_info_arr[iter_index_1][iter_index_2] = new Seat();
                iter_index_2 += 1;
            }
            iter_index_1 += 1;
        }    
    }


    private void extract_room_ids_to_list(File get_room_file, Linked_list<Integer> room_id_list) {
    JsonFactory json_factory = new JsonFactory();
    try (JsonParser movie_reader = json_factory.createParser(application.get_room_file())) {

        if (movie_reader.nextToken() != JsonToken.START_ARRAY) {
            throw new IllegalStateException("Expected JSON array at root");
        }

        while (movie_reader.nextToken() == JsonToken.START_OBJECT) {
            Integer store_room_id = null;

            while (movie_reader.nextToken() != JsonToken.END_OBJECT) {
                if (movie_reader.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String field_name = movie_reader.currentName();
                    movie_reader.nextToken(); 

                    if (field_name.equals("identification")) {
                        String id_string = movie_reader.getText();
                        try {
                            store_room_id = Integer.parseInt(id_string);
                        } catch (NumberFormatException nfe) {
                            System.err.println("Invalid ID format: " + id_string);
                        }
                    } else {
                        movie_reader.skipChildren();
                    }
                }
            }

            if (store_room_id != null) {
                room_id_list.add_at_the_end(store_room_id);
            }
        }

    } catch (IOException adsks) {
        adsks.printStackTrace();
    }
}


    private void show_screening_added_dialogue_box(String selected_movie) {
        JOptionPane.showMessageDialog(process_frame, "New screening of movie " + selected_movie + " added to file. ", "New screening added.", JOptionPane.INFORMATION_MESSAGE);
    }

    private void write_screening_to_json(File screening_file, Screening input_screening) {
        try (
            RandomAccessFile file_modifier = new RandomAccessFile(screening_file, "rw");
            OutputStream output_stream = new FileOutputStream(file_modifier.getFD());
            Writer writer = new OutputStreamWriter(output_stream);
        ) {
            JsonFactory factory = new JsonFactory();
            JsonGenerator generator = factory.createGenerator(writer);
            long length = file_modifier.length();
            long position = length;
            byte current_char_byte;
            long index_of_last_char_before_array_end = create_account_state.last_non_whitespace_index(file_modifier, position);
            file_modifier.seek(index_of_last_char_before_array_end);
            current_char_byte = file_modifier.readByte();

            if ((char) current_char_byte != ']') {
                throw new IOException("Expected closing rectangular bracket at end of JSON array but got \"" + (char) current_char_byte + "\" in the file \"" + application.get_account_file().getName() + "\".");
            }

            position = index_of_last_char_before_array_end - 1;
            long index_of_last_char_before_closing_bracket = create_account_state.last_non_whitespace_index(file_modifier, position);
            file_modifier.seek(index_of_last_char_before_closing_bracket);
            current_char_byte = file_modifier.readByte();
            
            boolean found_object_end = ((char) current_char_byte == '}');
            boolean found_start_of_array = ((char) current_char_byte == '[');
            if (!(found_object_end || found_start_of_array)) {
                throw new IOException("Expected closing curly bracket at end of JSON object or start of array but got \"" + (char) current_char_byte + "\" in the file \"" + application.get_account_file().getName() + "\" at position " + index_of_last_char_before_closing_bracket + ".");
            }
            
            file_modifier.setLength(length - 1);

            if (found_object_end) {
                writer.write(",\n\t"); 
                writer.flush();
            } else {
                writer.write("\n\t");
                writer.flush();
            }

            String screening_to_json = get_json_screening_fields(input_screening);
            writer.write(screening_to_json);
            writer.write("\n]");
            writer.flush();
            generator.close();
            
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String get_json_screening_fields(Screening screening_to_write) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String screening_to_json = mapper.writeValueAsString(screening_to_write);
        return screening_to_json;
    }

    public void extract_movie_names_to_list(File input_movie_file, Linked_list<String> input_list) {
        JsonFactory json_factory = new JsonFactory();
    try (JsonParser movie_reader = json_factory.createParser(input_movie_file)) {

        if (movie_reader.nextToken() != JsonToken.START_ARRAY) {
            throw new IllegalStateException("Expected JSON array at root");
        }

        while (movie_reader.nextToken() != JsonToken.END_ARRAY) {
            if (movie_reader.getCurrentToken() == JsonToken.START_OBJECT) {
                String current_name = null;

                while (movie_reader.nextToken() != JsonToken.END_OBJECT) {
                    String field_name = movie_reader.currentName();
                    movie_reader.nextToken(); 

                    if ("name".equals(field_name)) {
                        current_name = movie_reader.getValueAsString();
                    } else {
                        movie_reader.skipChildren(); 
                    }
                }

                if (current_name != null) {
                    input_list.add_at_the_end(current_name);
                }
            }
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
     
    }

    private void show_no_room_selected_dialogue_box() {
        JOptionPane.showMessageDialog(process_frame, "Select a room for the screening. ", "No room selected.", JOptionPane.ERROR_MESSAGE);
    }

    private void show_no_movie_selected_dialogue_box() {
        JOptionPane.showMessageDialog(process_frame, "Select a movie for the screening. ", "No movie selected.", JOptionPane.ERROR_MESSAGE);
    }
    
}