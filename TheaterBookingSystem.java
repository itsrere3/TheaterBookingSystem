package bookingsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

public class TheaterBookingSystem {

    // -------------------- Model --------------------
    // This class stores the Theater  seats and booking data
    static class TheaterModel {

        // Map to store seat name and its booking status
        private final Map<String, Boolean> seats = new LinkedHashMap<>();

        // Create all seats from A1 to J25
        public TheaterModel() {

            for (char row = 'A'; row <= 'J'; row++) {
                for (int col = 1; col <= 25; col++) {
                    String seat = row + String.valueOf(col);
                    seats.put(seat, false); // false means the seat is not booked
                }
            }
        }

        // Check if a seat is already booked
        public boolean isSeatBooked(String seatName) {
            return seats.getOrDefault(seatName, false);
        }

        // Try to book a seat
        public boolean bookSeat(String seatName) {

            // Check if the seat exists and is not booked
            if (seats.containsKey(seatName) && !seats.get(seatName)) {
                seats.put(seatName, true); // mark seat as booked
                return true;
            }

            // Return false if the seat is already booked
            return false;
        }

        // Return all seats
        public Map<String, Boolean> getSeats() {
            return seats;
        }
    } 
   

    // -------------------- View --------------------
    // This class shows the user interface
    static class TheaterView {

        private JFrame frame;
        private JPanel seatPanel;
        private JLabel statusLabel;
        private TheaterModel model;

        // Listener for seat buttons
        private ActionListener seatButtonListener;

        // Create the view and connect it with the model
        public TheaterView(TheaterModel model) {
            this.model = model;
            initialize();
        }

        // Build the main window and layout
        private void initialize() {

            frame = new JFrame("Theater  Booking System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 500);
            frame.setLayout(new BorderLayout());

            // Panel that shows all seat buttons
            seatPanel = new JPanel(new GridLayout(10, 25, 4, 4));
            updateSeatView();

            JPanel controlPanel = new JPanel();

            // Label to show system messages
            statusLabel = new JLabel("Click a seat to book");
            controlPanel.add(statusLabel);

            frame.add(new JScrollPane(seatPanel), BorderLayout.CENTER);
            frame.add(controlPanel, BorderLayout.SOUTH);
        }

        // Set the action listener for seat buttons
        public void setSeatButtonListener(ActionListener listener) {
            this.seatButtonListener = listener;
            updateSeatView();
        }

        // Refresh the seat buttons based on the model
        public void updateSeatView() {

            seatPanel.removeAll();

            // Create a button for each seat
            for (Map.Entry<String, Boolean> entry : model.getSeats().entrySet()) {

                JButton seatButton = new JButton(entry.getKey());

                // Disable the button if the seat is booked
                seatButton.setEnabled(!entry.getValue());

                // Change color based on seat status
                seatButton.setBackground(entry.getValue() ? Color.RED : Color.GREEN);
                seatButton.setOpaque(true);
                seatButton.setBorderPainted(false);

                // Store the seat name in the button
                seatButton.setActionCommand(entry.getKey());

                // Add listener only if the seat is not booked
                if (!entry.getValue() && seatButtonListener != null) {
                    seatButton.addActionListener(seatButtonListener);
                }

                seatPanel.add(seatButton);
            }

            // Update the panel
            seatPanel.revalidate();
            seatPanel.repaint();
        }

        // Update the message shown to the user
        public void updateStatus(String message) {
            statusLabel.setText(message);
        }

        // Show the window on the screen
        public void show() {
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    // -------------------- Controller --------------------
    // This class handles user actions and connects model and view
    static class TheaterController implements ActionListener {

        private TheaterModel model;
        private TheaterView view;

        // Connect the controller with the model and the view
        public TheaterController(TheaterModel model, TheaterView view) {
            this.model = model;
            this.view = view;
            this.view.setSeatButtonListener(this);
        }

        // Handle button click events
        @Override
        public void actionPerformed(ActionEvent e) {

            // Get the selected seat name
            String seatName = e.getActionCommand();

            // Try to book the selected seat
            if (model.bookSeat(seatName)) {
                view.updateStatus("Seat " + seatName + " booked successfully");
                view.updateSeatView();
            } else {
                view.updateStatus("Seat " + seatName + " is already booked");
            }
        }
    }

    // -------------------- Main --------------------
    // Program entry point
    public static void main(String[] args) {

        // Start the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {

            TheaterModel model = new TheaterModel();
            TheaterView view = new TheaterView(model);
            new TheaterController(model, view);

            view.show();
        });
    }
}
