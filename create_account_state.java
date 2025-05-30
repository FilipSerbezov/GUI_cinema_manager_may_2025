package main_1;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

class create_account_state extends gui_cinema_manager_state {
    
    create_account_state(gui_cinema_manager_state prev_state) {
        super(prev_state);
    }

    public void render_state() {
        panel.setLayout(null);
        process_frame.setContentPane(panel);
        JTextField username_field = new JTextField();
        JPasswordField password_field = new JPasswordField();
        JLabel set_username_label = new JLabel("username of new account: ");
        JLabel set_password_label = new JLabel("password of new account: ");
        set_username_label.setBounds(100, 70, 300, 30);
        set_password_label.setBounds(100, 170, 300, 30);
        username_field.setBounds(200, 100, 100, 30);
        password_field.setBounds(200, 200, 100, 30);
        panel.add(username_field);
        panel.add(password_field);
        panel.add(set_username_label);
        panel.add(set_password_label);
        JButton create_account_button = new JButton("create new account");
        create_account_button.setBounds(100, 400, 250, 30);
        create_account_button.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = username_field.getText();
                char[] password = password_field.getPassword();
                Cinema_account created_account = new Cinema_account(username, password); 
                boolean there_is_account_with_username = Cinema_account.is_there_account_with_username(application.get_account_file(), username);
                
                if (there_is_account_with_username) {
                    show_used_username_dialogue_box(username);
                    return;
                }

                boolean written_account = write_accont_to_json_file(created_account, application.get_account_file());// && !there_is_account_with_username);
            
                if (written_account) {
                    show_account_created_dialogue_box(username);
                } else {
                    show_failed_account_creation_dialogue_box();
                }
            }

            private void show_failed_account_creation_dialogue_box() {
                JOptionPane.showMessageDialog(process_frame, "Failed to create account. ", "Account creation failed.", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(create_account_button);

        JButton back_to_login_button = new JButton("back to login");
        back_to_login_button.setBounds(100, 300, 250, 30);

        back_to_login_button.addActionListener(new java.awt.event.ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                clear_state();
                application.set_state(new login_state(create_account_state.this));
                application.get_state().render_state();
            }
        });
        panel.add(back_to_login_button);
    }

    // private static boolean is_there_account_with_username(File account_file, String search_username) {
    //     JsonFactory json_factory = new JsonFactory();
    //     try (JsonParser account_reader = json_factory.createParser(account_file)) {
            
    //         if (account_reader.nextToken() != JsonToken.START_ARRAY) {
    //             throw new IllegalStateException("Expected JSON array at root");
    //         }

    //         while (account_reader.nextToken() == JsonToken.START_OBJECT) {
    //             String candidate_username = null;

    //             while (account_reader.nextToken() != JsonToken.END_OBJECT) {
    //                 if (account_reader.currentToken() == JsonToken.FIELD_NAME) {
    //                     account_reader.nextToken();
    //                     String field_name = account_reader.currentName();

    //                     if (field_name.equals("username")) {
    //                         candidate_username = account_reader.getValueAsString();
    //                     } 
    //                 }    
    //             }
    //             boolean username_match = candidate_username.equals(search_username);
    //             if (username_match) {
    //                 return true;
    //             }
    //         }
    //         return false;
    //     } catch (IOException adsks) {
    //         adsks.printStackTrace();
    //     }
    //     return false; 
    // }

    private boolean write_accont_to_json_file(Cinema_account account_to_write, File json_array) {
        try (
            RandomAccessFile file_modifier = new RandomAccessFile(json_array, "rw");
            OutputStream output_stream = new FileOutputStream(file_modifier.getFD());
            Writer writer = new OutputStreamWriter(output_stream);
        ) {
            JsonFactory factory = new JsonFactory();
            JsonGenerator generator = factory.createGenerator(writer);
            long length = file_modifier.length();
            long position = length;
            byte current_char_byte;
            long index_of_last_char_before_array_end = last_non_whitespace_index(file_modifier, position);
            file_modifier.seek(index_of_last_char_before_array_end);
            current_char_byte = file_modifier.readByte();

            if ((char) current_char_byte != ']') {
                throw new IOException("Expected closing rectangular bracket at end of JSON array but got \"" + (char) current_char_byte + "\" in the file \"" + application.get_account_file().getName() + "\".");
            }

            position = index_of_last_char_before_array_end - 1;
            long index_of_last_char_before_closing_bracket = last_non_whitespace_index(file_modifier, position);
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

            write_account_fields(generator, account_to_write);
            writer.write("\n]");
            writer.flush();
            generator.close();
            
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void write_account_fields(JsonGenerator generator, Cinema_account account_to_write) throws IOException {
        generator.writeStartObject();
        generator.writeStringField("username", account_to_write.get_username());
        generator.writeFieldName("password");
        write_password_arr(generator, account_to_write.get_password());
        generator.writeEndObject();
        generator.writeBooleanField("admin", account_to_write.is_admin());
        generator.flush();
    }

    private void write_password_arr(JsonGenerator generator, char[] char_arr) {
        try {
            generator.writeStartArray();
            int iter_index = 0;
            while (iter_index < char_arr.length) {
                generator.writeString("" + char_arr[iter_index]);
                iter_index += 1;
            }
            generator.writeEndArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static long last_non_whitespace_index(RandomAccessFile file_modifier, long position) throws IOException {
        byte current_char_byte;

        do {
            position -= 1;
            file_modifier.seek(position);
            current_char_byte = file_modifier.readByte();
        } while (Character.isWhitespace((char) current_char_byte) && position >= 0);

        return position;
    }

    public void show_account_created_dialogue_box(String username) {
        JOptionPane.showMessageDialog(process_frame, "An account with username \"" + username + "\" was created. ", "New account created.", JOptionPane.INFORMATION_MESSAGE);
    }

    public void show_used_username_dialogue_box(String username) {
        JOptionPane.showMessageDialog(process_frame, "An account with username \"" + username + "\" already exists. No new account created. ", "Username taken. ", JOptionPane.ERROR_MESSAGE);
    }

}