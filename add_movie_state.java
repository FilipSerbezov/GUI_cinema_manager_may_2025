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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class add_movie_state extends gui_cinema_manager_state{
    add_movie_state(gui_cinema_manager_state prev_state) {
        super(prev_state);
    }

    
    protected void render_state() {
        panel.setLayout(null);
        process_frame.setContentPane(panel);

        JLabel name_label = new JLabel("name: ");
        name_label.setBounds(50, 50, 50, 30);
        panel.add(name_label);

        JTextField name_field = new JTextField();
        name_field.setBounds(100, 50, 100, 30);
        panel.add(name_field);

        JLabel duration_label = new JLabel("duration: ");
        duration_label.setBounds(50, 100, 50, 30);
        panel.add(duration_label);

        JTextField duration_field = new JTextField();
        duration_field.setBounds(100, 100, 100, 30);
        panel.add(duration_field);

        // JLabel dim_2_length_label = new JLabel("column length");
        // dim_2_length_label.setBounds(50, 150, 50, 30);
        // panel.add(dim_2_length_label); 

        // JTextField dim_2_length_field = new JTextField();
        // dim_2_length_field.setBounds(100, 150, 100, 30);
        // panel.add(dim_2_length_field);

        JButton add_movie_button = new JButton("add movie");
        add_movie_button.setBounds(150, 50, 100, 30);
        add_movie_button.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (name_field.getText() == null || duration_field.getText() == null ) {
                        show_null_movie_parameters_dialogue_box();
                        return;
                    }

                    String input_name = name_field.getText();
                    int duration_in_minutes = Integer.parseInt(duration_field.getText());

                    if (home_state.get_movie_with_name(input_name, application) != null) {
                        show_movie_exists_dialogue_box();
                        return;
                    }

                    Movie output_movie = new Movie();
                    output_movie.set_name(input_name);
                    output_movie.set_length_in_minutes(duration_in_minutes);
                    add_movie(output_movie, application);
                    
                }
            }
        );
        panel.add(add_movie_button);
    }

    protected void add_movie(Movie input_movie, gui_cinema_manager_1 input_app) {
        try (
            RandomAccessFile file_modifier = new RandomAccessFile(input_app.get_movie_file(), "rw");
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

            String movie_to_json = get_json_movie_fields(input_movie);
            writer.write(movie_to_json);
            writer.write("\n]");
            writer.flush();
            generator.close();
            show_movie_added_dialogue_box(input_movie.get_name());
            
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }

    // protected void add_cinema_room(Cinema_room input_room, File room_file) {
    //     try (
    //         RandomAccessFile file_modifier = new RandomAccessFile(room_file, "rw");
    //         OutputStream output_stream = new FileOutputStream(file_modifier.getFD());
    //         Writer writer = new OutputStreamWriter(output_stream);
    //     ) {
    //         JsonFactory factory = new JsonFactory();
    //         JsonGenerator generator = factory.createGenerator(writer);
    //         long length = file_modifier.length();
    //         long position = length;
    //         byte current_char_byte;
    //         long index_of_last_char_before_array_end = create_account_state.last_non_whitespace_index(file_modifier, position);
    //         file_modifier.seek(index_of_last_char_before_array_end);
    //         current_char_byte = file_modifier.readByte();

    //         if ((char) current_char_byte != ']') {
    //             throw new IOException("Expected closing rectangular bracket at end of JSON array but got \"" + (char) current_char_byte + "\" in the file \"" + application.get_account_file().getName() + "\".");
    //         }

    //         position = index_of_last_char_before_array_end - 1;
    //         long index_of_last_char_before_closing_bracket = create_account_state.last_non_whitespace_index(file_modifier, position);
    //         file_modifier.seek(index_of_last_char_before_closing_bracket);
    //         current_char_byte = file_modifier.readByte();
            
    //         boolean found_object_end = ((char) current_char_byte == '}');
    //         boolean found_start_of_array = ((char) current_char_byte == '[');
    //         if (!(found_object_end || found_start_of_array)) {
    //             throw new IOException("Expected closing curly bracket at end of JSON object or start of array but got \"" + (char) current_char_byte + "\" in the file \"" + application.get_account_file().getName() + "\" at position " + index_of_last_char_before_closing_bracket + ".");
    //         }
            
    //         file_modifier.setLength(length - 1);

    //         if (found_object_end) {
    //             writer.write(",\n\t"); 
    //             writer.flush();
    //         } else {
    //             writer.write("\n\t");
    //             writer.flush();
    //         }

    //         String room_to_json = get_json_room_fields(input_room);
    //         writer.write(room_to_json);
    //         writer.write("\n]");
    //         writer.flush();
    //         generator.close();
            
    //     } catch (FileNotFoundException fnfe) {
    //         fnfe.printStackTrace();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    
    // }

    // private String get_json_room_fields(Cinema_room input_room) throws JsonProcessingException {
    //     ObjectMapper mapper = new ObjectMapper();
    //     String room_to_json = mapper.writeValueAsString(input_room);
    //     return room_to_json;
    // }

    // private void show_null_movie_parameters_dialogue_box() {
    //     JOptionPane.showMessageDialog(process_frame, "Cannot add movie with null parameters. ", "Enter parameters in the fields.", JOptionPane.ERROR_MESSAGE);
    // }

    // private void show_room_exists_dialogue_box() {
    //     JOptionPane.showMessageDialog(process_frame, "A room with this ID already exists.", "Room ID is taken.", JOptionPane.ERROR_MESSAGE);
    // }

    private void show_movie_added_dialogue_box(String input_name) {
        JOptionPane.showMessageDialog(process_frame, "Movie with name \"" + input_name + "\" is added.", "", JOptionPane.INFORMATION_MESSAGE);

    }


    private String get_json_movie_fields(Movie input_movie) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String movie_to_json = mapper.writeValueAsString(input_movie);
        return movie_to_json;
    }


    protected void show_movie_exists_dialogue_box() {
        JOptionPane.showMessageDialog(process_frame, "Pick a different movie name. ", "A movie with this name already exists.", JOptionPane.ERROR_MESSAGE);
    }


    private void show_null_movie_parameters_dialogue_box() {
        JOptionPane.showMessageDialog(process_frame, "Cannot add movie with null parameters. ", "Enter parameters in the fields.", JOptionPane.ERROR_MESSAGE);
    }
}
