package main_1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class pick_seats_state extends gui_cinema_manager_state {
    private Screening current_screening;

    public pick_seats_state(Screening input_screening) {
        this.current_screening = input_screening;
    }
    
}