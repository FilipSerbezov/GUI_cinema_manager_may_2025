package main_1;

import javax.swing.JButton;

public class Button_arr_wrapper {
    private JButton[][] button_arr;
    
    public JButton[][] get_button_arr() {
        return this.button_arr;
    }

    public void set_button_arr(JButton[][] input_button_arr) {
        this.button_arr = input_button_arr;
    }
}