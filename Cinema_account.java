package main_1;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class Cinema_account {
    private String username;
    private char[] password;
    private Linked_list<Seat_address> reserved_seats;
    private boolean admin;

    public void add_to_reserved_seats(Seat_address to_reserve) {
        if (!(reserved_seats.contains(to_reserve))) {
            this.reserved_seats.add_at_the_end(to_reserve);
        }
    }

    public void unreserve_all_seats() {
        this.reserved_seats = new Linked_list<>();
    }

    public Cinema_account(String input_username, char[] input_password) {
        this.username = input_username;
        this.password = input_password;
    }

    public Cinema_account() {

    }

    @JsonProperty("username")
    public String get_username() {
        return this.username;
    }

    @JsonProperty("password")
    public char[] get_password() {
        return this.password;
    }

    public void set_username(String input_username) {
        this.username = input_username;
    }

    public void set_password(char[] input_password) {
        this.password = input_password;
    }

    public void set_admin(boolean input_admin) {
        this.admin = input_admin;
    }

    public boolean is_admin() {
        return this.admin;
    }

    public static boolean is_user_with_name_admin(File account_file, String search_name) {
        JsonFactory json_factory = new JsonFactory();
        try (JsonParser account_reader = json_factory.createParser(account_file)) {
            
            if (account_reader.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected JSON array at root");
            }

            while (account_reader.nextToken() == JsonToken.START_OBJECT) {
                String candidate_username = null;
                //Linked_list<Character> password_character_list = new Linked_list<>();
                boolean store_admin = false;

                while (account_reader.nextToken() != JsonToken.END_OBJECT) {
                    if (account_reader.currentToken() == JsonToken.FIELD_NAME) {
                        account_reader.nextToken();
                        String field_name = account_reader.currentName();

                        if (field_name.equals("username")) {
                            candidate_username = account_reader.getValueAsString();
                        } else if (field_name.equals("admin")) {
                            store_admin = account_reader.getBooleanValue();
                        } else {
                            account_reader.skipChildren();
                        }
                    }    
                }
                
                return store_admin;
            }

            return false;
        } catch (IOException adsks) {
            adsks.printStackTrace();
        }
        return false; 
    }

    public static boolean is_there_account_with_username(File account_file, String search_username) {
        JsonFactory json_factory = new JsonFactory();
        try (JsonParser account_reader = json_factory.createParser(account_file)) {
            
            if (account_reader.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected JSON array at root");
            }

            while (account_reader.nextToken() == JsonToken.START_OBJECT) {
                String candidate_username = null;

                while (account_reader.nextToken() != JsonToken.END_OBJECT) {
                    if (account_reader.currentToken() == JsonToken.FIELD_NAME) {
                        account_reader.nextToken();
                        String field_name = account_reader.currentName();

                        if (field_name.equals("username")) {
                            candidate_username = account_reader.getValueAsString();
                        } 
                    }    
                }
                boolean username_match = candidate_username.equals(search_username);
                if (username_match) {
                    return true;
                }
            }
            return false;
        } catch (IOException adsks) {
            adsks.printStackTrace();
        }
        return false; 
    }
}