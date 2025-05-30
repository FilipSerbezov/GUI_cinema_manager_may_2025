package main_1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class gui_cinema_manager_home_state implements gui_cinema_manager_state{
    public void clear() {

    }

    public void render() {
        JFrame gui_cinema_manager_home = new JFrame();
        gui_cinema_manager_home.setSize(400, 500);
        // can the frame change the size
        // setResizable(false);
        // stops the program after closing the frame
        gui_cinema_manager_home.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        // set the frame to the center of the main screen according to its size
        gui_cinema_manager_home.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        gui_cinema_manager_home.setContentPane(panel);
        // JLabel labelName = new JLabel("Name:");
        // labelName.setLocation(20, 20);
        // labelName.setSize(50, 30);
        // panel.add(labelName);
        // JTextField nameField = new JTextField();
        // nameField.setBounds(labelName.getX() + labelName.getWidth() + 5, labelName.getY(), 200, 30);
        // panel.add(nameField);
        // JLabel labelSalary = new JLabel("Salary:");
        // labelSalary.setBounds(labelName.getX(), labelName.getY() + labelName.getHeight() + 10, 50, 30);
        // panel.add(labelSalary);
        // JTextField salaryField = new JTextField();
        // salaryField.setBounds(labelSalary.getX() + labelSalary.getWidth() + 5, labelSalary.getY(), 200, 30);
        // panel.add(salaryField);
        // JLabel labelHours = new JLabel("Hours:");
        // labelHours.setBounds(labelSalary.getX(), labelSalary.getY() + labelSalary.getHeight() + 10, 50, 30);
        // panel.add(labelHours);
        // JTextField hoursField = new JTextField();
        // hoursField.setBounds(labelHours.getX() + labelHours.getWidth() + 5, labelHours.getY(), 200, 30);
        // panel.add(hoursField);
        JButton button = new JButton("Book a ticket");
        button.setBounds(100, 100, 200, 30);
        panel.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<Screening> screening_list = new JComboBox<>();
                screening_list.setBounds(200, 200, 200, 30);
                screening_list.addItem(new Screening());
                screening_list.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Screening chosen_screening = (Screening) screening_list.getSelectedItem();
                        chosen_screening.display_cinema_room_scheme();
                    }
                });
                panel.add(screening_list);
            }
        });
        gui_cinema_manager_home.setVisible(true);
    }

    public static void main(String[] args) {
        gui_cinema_manager_home_state blah = new gui_cinema_manager_home_state();
        blah.render();
    }
}