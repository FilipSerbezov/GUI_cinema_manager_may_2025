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
import javax.swing.JTextField;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class add_room_state extends gui_cinema_manager_state{
    add_room_state(gui_cinema_manager_state prev_state) {
        super(prev_state);
    }

    
    protected void render_state() {
        panel.setLayout(null);
        process_frame.setContentPane(panel);

        JLabel id_label = new JLabel("room id:");
        id_label.setBounds(50, 50, 50, 30);
        panel.add(id_label);

        JTextField id_field = new JTextField();
        id_field.setBounds(100, 50, 100, 30);
        panel.add(id_field);

        JLabel dim_1_length_label = new JLabel("row length");
        dim_1_length_label.setBounds(50, 100, 50, 30);
        panel.add(dim_1_length_label);

        JTextField dim_1_length_field = new JTextField();
        dim_1_length_field.setBounds(100, 100, 100, 30);
        panel.add(dim_1_length_field);

        JLabel dim_2_length_label = new JLabel("column length");
        dim_2_length_label.setBounds(50, 150, 50, 30);
        panel.add(dim_2_length_label); 

        JTextField dim_2_length_field = new JTextField();
        dim_2_length_field.setBounds(100, 150, 100, 30);
        panel.add(dim_2_length_field);

        JButton add_room_button = new JButton("add room");
        add_room_button.setBounds(150, 50, 100, 30);
        add_room_button.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (id_field.getText() == null || dim_1_length_field.getText() == null || dim_2_length_field.getText() == null) {
                        show_null_room_parameters_dialogue_box();
                        return;
                    }

                    int input_id = Integer.parseInt(id_field.getText());
                    int input_dim_1_row_length = Integer.parseInt(dim_1_length_field.getText());
                    int input_dim_2_column_length = Integer.parseInt(dim_2_length_field.getText());

                    if (home_state.get_cinema_room_with_id(input_id, application) != null) {
                        show_room_exists_dialogue_box();
                        return;
                    }

                    Cinema_room created_room = new Cinema_room();
                    created_room.set_identification(input_id);
                    created_room.set_length_of_dim_1_rows(input_dim_1_row_length);
                    created_room.set_length_of_dim_2_columns(input_dim_2_column_length);
                    add_cinema_room(created_room, application.get_room_file());
                }
            }
        );
        panel.add(add_room_button);
    }

    protected void add_cinema_room(Cinema_room input_room, File room_file) {
        try (
            RandomAccessFile file_modifier = new RandomAccessFile(room_file, "rw");
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

            String room_to_json = get_json_room_fields(input_room);
            writer.write(room_to_json);
            writer.write("\n]");
            writer.flush();
            generator.close();
            show_room_added_dialogue_box(input_room.get_identification());
            
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }

    private void show_room_added_dialogue_box(int input_id) {
        JOptionPane.showMessageDialog(process_frame, "Room with id " + input_id + " is added.", "", JOptionPane.INFORMATION_MESSAGE);

    }


    private String get_json_room_fields(Cinema_room input_room) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String room_to_json = mapper.writeValueAsString(input_room);
        return room_to_json;
    }

    private void show_null_room_parameters_dialogue_box() {
        JOptionPane.showMessageDialog(process_frame, "Cannot add room with null parameters. ", "Enter parameters in the fields.", JOptionPane.ERROR_MESSAGE);
    }

    private void show_room_exists_dialogue_box() {
        JOptionPane.showMessageDialog(process_frame, "A room with this ID already exists.", "Room ID is taken.", JOptionPane.ERROR_MESSAGE);
    }
}
