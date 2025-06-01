package main_1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class book_ticket_state extends gui_cinema_manager_state {
    private Screening chosen_screening;

    book_ticket_state(gui_cinema_manager_state prev_state) {
        super(prev_state);
    }

    book_ticket_state(gui_cinema_manager_state prev_state, Screening input_chosen_screening) {
        super(prev_state);
        this.set_chosen_screening(input_chosen_screening);
    }

    void set_chosen_screening(Screening input_screening) {
        this.chosen_screening = input_screening;
    }
 
    public void display_inner_panel(JPanel input_panel) {
        JScrollPane room_scroll_pane = new JScrollPane();
        room_scroll_pane.getViewport().add(input_panel);
        room_scroll_pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        room_scroll_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        process_frame.add(room_scroll_pane);
        process_frame.getContentPane().setLayout(new BorderLayout());
        process_frame.getContentPane().add(room_scroll_pane, BorderLayout.CENTER);
    }

    private JButton[][] get_seat_buttons_arr(Linked_list<Seat_address> selected_seats, Seat[][] seat_arr_to_visualize) {
        JButton[][] seat_button_arr = new JButton[seat_arr_to_visualize.length][seat_arr_to_visualize[0].length];
        int dim_1_offset = 20;
        int dim_2_offset = 20;
        int dim_1_size = 100;
        int dim_2_size = 100;

        int iter_index_1 = 0;
        

        while (iter_index_1 < seat_arr_to_visualize.length) {

            int iter_index_2 = 0;
            while (iter_index_2 < seat_arr_to_visualize[0].length) {
                JButton current_seat_button = new JButton(Integer.toString(iter_index_1) + "; " + Integer.toString(iter_index_2));
                current_seat_button.setBounds(dim_1_offset, dim_2_offset, dim_1_size, dim_2_size);
                Seat current_seat = seat_arr_to_visualize[iter_index_1][iter_index_2];
                boolean current_seat_is_occupied = current_seat.is_occupied();
                if (current_seat_is_occupied) {
                    if (current_seat.get_occupant_name().equals(application.get_current_user())) {
                        current_seat_button.setBackground(new Color(255, 200, 140));
                    } else {
                        current_seat_button.setBackground(new Color(255, 200, 200));
                    }
                }
                Seat_address current_seat_address = new Seat_address();
                current_seat_address.set_screening_id(chosen_screening.get_ident());
                current_seat_address.set_dim_1_row_pos(iter_index_1);
                current_seat_address.set_dim_2_column_pos(iter_index_2);
                current_seat_button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (current_seat_is_occupied) {
                            show_seat_taken_dialogue_box();
                            return;
                        }
                        selected_seats.add_at_the_end(current_seat_address);
                        current_seat_button.setBackground(new Color(255, 255, 190));
                    }
                });
                seat_button_arr[iter_index_1][iter_index_2] = (current_seat_button);

                dim_1_offset += 110;
               
                iter_index_2 += 1;
            }
            dim_1_offset = 20;
            dim_2_offset += 110;
            iter_index_1 += 1;
        }

        return seat_button_arr;
    }

    private void add_seat_buttons(JButton[][] seat_button_arr, JPanel seat_panel) {
        int iter_index_1 = 0;
        while (iter_index_1 < seat_button_arr.length) {
            int iter_index_2 = 0;
            while (iter_index_2 < seat_button_arr[0].length) {
                seat_panel.add(seat_button_arr[iter_index_1][iter_index_2]);
                iter_index_2 += 1;
            }
            iter_index_1 += 1;
        }

    }

    public void render_state() {
        int dim_1_length = chosen_screening.get_seat_info().length;
        int dim_2_length = chosen_screening.get_seat_info()[0].length;
        JPanel seat_panel = new JPanel();
        seat_panel.setPreferredSize(new Dimension(500, 500));
        process_frame.setContentPane(panel);
        Seat[][] seat_arr_to_visualize = chosen_screening.get_seat_info();
        seat_panel.setLayout(null); 

        Linked_list<Seat_address> selected_seats = new Linked_list<>();
        int dim_1_size = 100;
        int dim_2_size = 100;

        
        int dim_1_offset = 20;
        Button_arr_wrapper seat_button_arr_wrapper = new Button_arr_wrapper();
        seat_button_arr_wrapper.set_button_arr(get_seat_buttons_arr(selected_seats, seat_arr_to_visualize));
        add_seat_buttons(seat_button_arr_wrapper.get_button_arr(), seat_panel);
        JButton reservation_button = new JButton("Reserve selected seats");
        reservation_button.setBounds(50, dim_1_offset + (dim_1_size + 10)*dim_1_length, 100, 30);
        reservation_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                occupy_seats(selected_seats);
            }
        });
        seat_panel.add(reservation_button);

        Linked_list<Seat_address> user_seats = get_reserved_seats(chosen_screening, application.get_current_user());//new Linked_list<>();

        JButton cancel_button = new JButton("Cancel reservation");
        cancel_button.setBounds(200,  dim_1_offset + (dim_1_size + 10)*dim_1_length, 100, 30);
        cancel_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unreserve_seats(user_seats);
            }
        });
        seat_panel.add(cancel_button);

        seat_panel.setPreferredSize(new Dimension(220 + (dim_2_size + 10)*dim_2_length, 200 + dim_1_offset + (dim_1_size + 10)*dim_1_length));

        display_inner_panel(seat_panel);
    }

    private void remove_button_arr_from_panel(JButton[][] button_arr_to_remove, JPanel input_panel) {
        int iter_index_1 = 0;
        while (iter_index_1 < button_arr_to_remove.length) {
            int iter_index_2 = 0;
            while (iter_index_2 < button_arr_to_remove.length) {
                input_panel.remove(button_arr_to_remove[iter_index_1][iter_index_2]);
                iter_index_2 += 1;
            }
            iter_index_1 += 1;
        }
    }

    private Linked_list<Seat_address> get_reserved_seats(Screening current_screening, String search_name) {
        Linked_list<Seat_address> output_list = new Linked_list<>();
        Seat[][] seat_arr = current_screening.get_seat_info();
        int iter_index_1 = 0;
        while (iter_index_1 < seat_arr.length) {
            int iter_index_2 = 0;
            while (iter_index_2 < seat_arr[0].length) {
                Seat current_seat = seat_arr[iter_index_1][iter_index_2];
                if (current_seat.is_occupied()) {
                    if (current_seat.get_occupant_name().equals(search_name)) {
                        Seat_address address_of_current_seat = new Seat_address();
                        address_of_current_seat.set_dim_1_row_pos(iter_index_1);
                        address_of_current_seat.set_dim_2_column_pos(iter_index_2);
                        address_of_current_seat.set_screening_id(current_screening.get_ident());
                        output_list.add_at_the_end(address_of_current_seat);
                    }
                }
                iter_index_2 += 1;
            }
            iter_index_1 += 1;
        }
        return output_list;
    }

    private void unreserve_seats(Linked_list<Seat_address> input_list) {
        int length = input_list.length();
        int iter_index = 0;
        while (iter_index < length) {
            unreserve_seat(input_list.get(iter_index));
            iter_index += 1; 
        }
    }

    private void unreserve_seat(Seat_address seat_to_unreserve) {
        JsonFactory json_factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root_node = mapper.readTree(application.get_screening_file());
            if (root_node.isArray()) {
                ArrayNode file_array = (ArrayNode) root_node;
                int iter_index = 0;
                while (iter_index < file_array.size()) {
                    JsonNode current_object = file_array.get(iter_index);
                    if (current_object.isObject() && current_object.get("ident").asLong() == seat_to_unreserve.get_screening_ident()) {
                        ArrayNode seat_info_node = (ArrayNode) current_object.get("seat_info");
                        ArrayNode seat_info_dim_1_row_node = (ArrayNode) seat_info_node.get(seat_to_unreserve.get_dim_1_row_pos());
                        ObjectNode seat_node = (ObjectNode) seat_info_dim_1_row_node.get(seat_to_unreserve.get_dim_2_column_pos());
                        (seat_node).put("occupied", "false");
                        (seat_node).put("occupant_name", (String) null);
                    }
                    iter_index += 1;
                }
                mapper.writerWithDefaultPrettyPrinter().writeValue(application.get_screening_file(), file_array);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void occupy_seats(Linked_list<Seat_address> seats_to_occupy) {
        int number_of_seats = seats_to_occupy.length();
        int iter_index = 0;
        while (iter_index < number_of_seats) {
            occupy_seat(seats_to_occupy.get(iter_index));
            iter_index += 1;
        }

    }

    private void occupy_seat(Seat_address seat_to_occupy) {
        JsonFactory json_factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root_node = mapper.readTree(application.get_screening_file());
            if (root_node.isArray()) {
                ArrayNode file_array = (ArrayNode) root_node;
                int iter_index = 0;
                while (iter_index < file_array.size()) {
                    JsonNode current_object = file_array.get(iter_index);
                    if (current_object.isObject() && current_object.get("ident").asLong() == seat_to_occupy.get_screening_ident()) {
                        ArrayNode seat_info_node = (ArrayNode) current_object.get("seat_info");
                        ArrayNode seat_info_dim_1_row_node = (ArrayNode) seat_info_node.get(seat_to_occupy.get_dim_1_row_pos());
                        ObjectNode seat_node = (ObjectNode) seat_info_dim_1_row_node.get(seat_to_occupy.get_dim_2_column_pos());
                        (seat_node).put("occupied", "true");
                        (seat_node).put("occupant_name", application.get_current_user());
                    }
                    iter_index += 1;
                }
                mapper.writerWithDefaultPrettyPrinter().writeValue(application.get_screening_file(), file_array);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void show_seat_taken_dialogue_box() {
        JOptionPane.showMessageDialog(process_frame, "That seat has already been reserved.", "Cannot reserve that seat.", JOptionPane.ERROR_MESSAGE);
    }

}
