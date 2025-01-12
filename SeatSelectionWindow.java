import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SeatSelectionWindow extends JFrame {
    private static final int ROWS = 7;
    private static final int SEATS_PER_ROW = 10;
    private static boolean[][] seatAvailability = new boolean[ROWS][SEATS_PER_ROW];

    private JButton[][] seatButtons;
    private List<Point> selectedSeats = new ArrayList<>();
    private String selectedMovie;
    private String selectedShowTime;
    private int moviePrice;
    private JFrame previousWindow;
    private JLabel selectedSeatsLabel;

    public SeatSelectionWindow(String movie, String showTime, int price, JFrame previousWindow) {
        this.selectedMovie = movie;
        this.selectedShowTime = showTime;
        this.moviePrice = price;
        this.previousWindow = previousWindow;

        setTitle("Seat Selection - " + movie + " (" + showTime + ")");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(240, 240, 240));

        initializeSeats();
        createSeatPanel();

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        selectedSeatsLabel = new JLabel("Selected Seats: ");
        selectedSeatsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bottomPanel.add(selectedSeatsLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.addActionListener(e -> goBack());
        buttonPanel.add(backButton);

        JButton nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 16));
        nextButton.addActionListener(e -> openPaymentWindow());
        buttonPanel.add(nextButton);

        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void initializeSeats() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < SEATS_PER_ROW; j++) {
                seatAvailability[i][j] = true;
            }
        }
    }

    private void createSeatPanel() {
        JPanel seatPanel = new JPanel(new GridBagLayout());
        seatPanel.setBackground(new Color(240, 240, 240));
        seatButtons = new JButton[ROWS][SEATS_PER_ROW];
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < SEATS_PER_ROW; j++) {
                if (j == SEATS_PER_ROW / 2) {
                    gbc.gridx = j;
                    gbc.gridy = i;
                    seatPanel.add(Box.createHorizontalStrut(30), gbc);
                }

                seatButtons[i][j] = new JButton(String.format("%c%d", (char)('A' + i), j + 1));
                seatButtons[i][j].setPreferredSize(new Dimension(50, 50));
                int row = i, seat = j;
                seatButtons[i][j].addActionListener(e -> toggleSeat(row, seat));

                if (i >= ROWS - 2) {
                    seatButtons[i][j].setBackground(new Color(255, 255, 200));
                    seatButtons[i][j].setToolTipText("Premium Seat");
                } else {
                    seatButtons[i][j].setBackground(new Color(200, 255, 200));
                }

                gbc.gridx = j < SEATS_PER_ROW / 2 ? j : j + 1;
                gbc.gridy = i;
                seatPanel.add(seatButtons[i][j], gbc);
            }
        }

        JLabel screenLabel = new JLabel("SCREEN", SwingConstants.CENTER);
        screenLabel.setPreferredSize(new Dimension(600, 30));
        screenLabel.setOpaque(true);
        screenLabel.setBackground(Color.LIGHT_GRAY);
        screenLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.add(screenLabel, BorderLayout.NORTH);
        mainPanel.add(seatPanel, BorderLayout.CENTER);

        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
    }

    private void toggleSeat(int row, int seat) {
        if (seatAvailability[row][seat]) {
            Point seatPoint = new Point(row, seat);
            if (selectedSeats.contains(seatPoint)) {
                selectedSeats.remove(seatPoint);
                seatButtons[row][seat].setBackground(row >= ROWS - 2 ? new Color(255, 255, 200) : new Color(200, 255, 200));
            } else {
                selectedSeats.add(seatPoint);
                seatButtons[row][seat].setBackground(Color.GREEN);
            }
            updateSelectedSeatsLabel();
        } else {
            JOptionPane.showMessageDialog(this, "This seat is already taken.");
        }
    }

    private void updateSelectedSeatsLabel() {
        StringBuilder sb = new StringBuilder("Selected Seats: ");
        for (Point seat : selectedSeats) {
            sb.append(String.format("%c%d ", (char)('A' + seat.x), seat.y + 1));
        }
        selectedSeatsLabel.setText(sb.toString());
    }

    private void openPaymentWindow() {
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one seat.");
            return;
        }
        int result = JOptionPane.showConfirmDialog(this, 
            "You have selected " + selectedSeats.size() + " seat(s). Do you want to proceed to payment?",
            "Confirm Seats",
            JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            new PaymentWindow(selectedMovie, selectedShowTime, moviePrice, selectedSeats, this);
            this.setVisible(false);
        }
    }

    private void goBack() {
        previousWindow.setVisible(true);
        this.dispose();
    }
}