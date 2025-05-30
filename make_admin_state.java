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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class make_admin_state extends gui_cinema_manager_state {

    make_admin_state(gui_cinema_manager_state prev_state) {
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

        JButton make_admin_button = new JButton("provide admin permissions");
        make_admin_button.setBounds(150, 50, 100, 30);
        make_admin_button.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (name_field.getText() == null ) {
                        show_null_account_parameters_dialogue_box();
                        return;
                    }

                    String input_name = (name_field.getText());
                   
                    if (!Cinema_account.is_there_account_with_username(application.get_account_file(), input_name)) {
                        show_no_account_exists_dialogue_box();
                        return;
                    }

                    make_admin(input_name, application.get_account_file());
                }
            }
        );
        panel.add(make_admin_button);
    }


    protected void make_admin(String input_name, File account_file) {
        JsonFactory json_factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root_node = mapper.readTree(account_file);
            if (root_node.isArray()) {
                ArrayNode file_array = (ArrayNode) root_node;
                int iter_index = 0;
                while (iter_index < file_array.size()) {
                    JsonNode current_object = file_array.get(iter_index);
                    if (current_object.isObject() && current_object.get("username").asText().equals(input_name)) {
                        ((ObjectNode) current_object).put("admin", true);
                        show_admin_permission_provided_message(input_name);
                        
                    }
                    iter_index += 1;
                }
                mapper.writerWithDefaultPrettyPrinter().writeValue(application.get_account_file(), file_array);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }


    private void show_admin_permission_provided_message(String input_name) {
        JOptionPane.showMessageDialog(process_frame, "Account with name: \"" + input_name + "\" is now an admin. ", "Admin permissions provided", JOptionPane.INFORMATION_MESSAGE);

    }


    protected void show_no_account_exists_dialogue_box() {
        JOptionPane.showMessageDialog(process_frame, "No account with this username exists. ", "Pick a different username.", JOptionPane.ERROR_MESSAGE);
    }


    protected void show_null_account_parameters_dialogue_box() {
        JOptionPane.showMessageDialog(process_frame, "Username cannot be null. ", "Enter a username. ", JOptionPane.ERROR_MESSAGE);
    }

}