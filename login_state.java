package main_1;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class login_state extends gui_cinema_manager_state {
    
    login_state() {
        process_frame = new JFrame();
        process_frame.setSize(400, 500);
        process_frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        process_frame.setLocationRelativeTo(null);
        panel = new JPanel();
        panel.setLayout(null);
        process_frame.setContentPane(panel);
    }

    login_state(gui_cinema_manager_state prev_state) {
        super(prev_state);
    }

    private void show_failed_login_message() {
        JOptionPane.showMessageDialog(process_frame, "No account with such username - password combination exists.", "Login failed.", JOptionPane.ERROR_MESSAGE);
    }

    public void render_state() {
        panel.setLayout(null);
        process_frame.setContentPane(panel);
        JTextField username_field = new JTextField();
        JPasswordField password_field = new JPasswordField();
        JLabel username_label = new JLabel("username: ");
        JLabel password_label = new JLabel("password: ");
        username_label.setBounds(100, 100, 100, 30);
        password_label.setBounds(100, 200, 100, 30);
        username_field.setBounds(200, 100, 100, 30);
        password_field.setBounds(200, 200, 100, 30);
        panel.add(username_field);
        panel.add(password_field);
        panel.add(username_label);
        panel.add(password_label);
        JButton create_account_button = new JButton("create new account");
        create_account_button.setBounds(100, 400, 200, 30);
        create_account_button.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        
                clear_state();
                application.set_state(new create_account_state(login_state.this));
                application.get_state().render_state();
                
            }
        });
        panel.add(create_account_button);
        JButton login_attempt_button = new JButton("attempt login");
        login_attempt_button.setBounds(100, 300, 200, 30);
        login_attempt_button.addActionListener(new java.awt.event.ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                String attempted_username = username_field.getText();
                char[] attempted_password = password_field.getPassword();
                boolean access = is_there_such_an_account(attempted_username, attempted_password);
                if (access) {
                    clear_state();
                    application.set_current_user(attempted_username);
                    application.set_state(new home_state(login_state.this));
                    application.get_state().render_state();
                } else {
                    show_failed_login_message();
                }
            }
        });
        panel.add(login_attempt_button);
        this.process_frame.setVisible(true);
        // application.set_main_frame(process_frame);
        // application.set_panel(panel);
    }

    private boolean is_there_such_an_account(String search_username, char[] search_password) {
        JsonFactory json_factory = new JsonFactory();
        try (JsonParser account_reader = json_factory.createParser(application.get_account_file())) {
            
            if (account_reader.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected JSON array at root");
            }

            while (account_reader.nextToken() == JsonToken.START_OBJECT) {
                String candidate_username = null;
                Linked_list<Character> password_character_list = new Linked_list<>();

                while (account_reader.nextToken() != JsonToken.END_OBJECT) {
                    if (account_reader.currentToken() == JsonToken.FIELD_NAME) {
                        account_reader.nextToken();
                        String field_name = account_reader.currentName();

                        if (field_name.equals("username")) {
                            candidate_username = account_reader.getValueAsString();
                        } else if (field_name.equals("password")) {
                            
                            if (account_reader.currentToken() == JsonToken.START_ARRAY) {
                                
                                while (account_reader.nextToken() != JsonToken.END_ARRAY) {
                                    String current_char = account_reader.getValueAsString();
                                    
                                    if (current_char != null && current_char.length() == 1) {
                                        password_character_list.add_at_the_end(current_char.charAt(0));
                                    }

                                }

                            } else {
                                account_reader.skipChildren();
                            }

                        } else {
                            account_reader.skipChildren();
                        }
                    }    
                }
                char[] password_arr = character_list_to_char_array(password_character_list);
                boolean username_match = candidate_username.equals(search_username);
                boolean password_match = useful_static_functions.char_arrays_are_equal(search_password, password_arr);
                if (username_match && password_match) {
                    return true;
                }
            }
            return false;
        } catch (IOException adsks) {
            adsks.printStackTrace();
        }
        return false; 
    } 

    public static char[] character_list_to_char_array(Linked_list<Character> input_character_list) {
        int length = input_character_list.length();
        char[] char_array = new char[length];
        
        int iter_index = 0;
        while (iter_index < length) {
            char_array[iter_index] = input_character_list.get(iter_index);
            iter_index += 1;
        }

        return char_array;
    }

    

}