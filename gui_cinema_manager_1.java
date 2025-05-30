package main_1;

import java.io.File;

import javax.swing.JFrame;

import javax.swing.JPanel;

import javax.swing.WindowConstants;

public class gui_cinema_manager_1 {
    private gui_cinema_manager_state state;
    private String current_user;
    private final File screening_file = new File("screenings.json");
    private final File screening_index_file = new File("screening_number.txt");
    private final File account_file = new File("cinema_accounts.json");
    private final File room_file = new File("cinema_rooms.json");
    private final File movie_file = new File("movies.json");

    public File get_movie_file() {
        return this.movie_file;
    }

    public String get_current_user() {
        return this.current_user;
    }

    public void set_current_user(String input_username) {
        this.current_user = input_username;
    }

    public gui_cinema_manager_state get_state() {
        return this.state;
    }

    public void set_state(gui_cinema_manager_state input_state) {
        this.state = input_state;
    }

    public File get_screening_file() {
        return this.screening_file;
    }

    public void set_screening_list(File input_screening_file) {
        this.screening_file = input_screening_file;
    }
    
    public File get_room_file() {
        return this.room_file;
    }

    public File get_account_file() {
        return this.account_file;
    }

    public gui_cinema_manager_1() {
        
    }

    public void initialize_manager() {
        login_state init_state = new login_state();
        
        init_state.set_application(this);
        this.set_state(init_state);
        this.state.render_state();
    }



    public void open_gui() {

    }

    public static void main(String[] args) {
        gui_cinema_manager_1 gcm = new gui_cinema_manager_1();
        gcm.initialize_manager();
    }
}