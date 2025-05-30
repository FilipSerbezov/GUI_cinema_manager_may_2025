package main_1;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

abstract class gui_cinema_manager_state {
    protected JFrame process_frame;
    protected JPanel panel;
    protected gui_cinema_manager_1 application;
    protected abstract void render_state();

    gui_cinema_manager_state() {
        process_frame = new JFrame();
        process_frame.setSize(400, 500);
        process_frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        process_frame.setLocationRelativeTo(null);
        panel = new JPanel();
        panel.setLayout(null);
        process_frame.setContentPane(panel);
    }

    void clear_state() {
        process_frame.getContentPane().removeAll();
        process_frame.repaint();
    }

    gui_cinema_manager_state (gui_cinema_manager_state prev_state) {
        this.process_frame = prev_state.get_main_frame();
        this.panel = prev_state.get_panel();
        this.application = prev_state.get_application();
    }
    
    public JFrame get_main_frame() {
        return this.process_frame;
    }

    public void set_main_frame(JFrame input_main_frame) {
        this.process_frame = input_main_frame;
    }

    public JPanel get_panel() {
        return this.panel;
    }

    public void set_panel(JPanel input_panel) {
        this.panel = input_panel;
    }

    public void set_application(gui_cinema_manager_1 input_application) {
        this.application = input_application;
    }

    public gui_cinema_manager_1 get_application() {
        return this.application;
    }
}