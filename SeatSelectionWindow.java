import javax.swing.*;
import java.awt.*;

public class SeatSelectionWindow extends JFrame {
    private static final int ROWS = 5;
    private static final int SEATS_PER_ROW = 8;
    private static boolean[][] seatAvailability = new boolean[ROWS][SEATS_PER_ROW];

    private JButton[][] seatButtons;
    private int selectedRow = -1;
    private int selectedSeat = -1;
    private String selectedMovie;
    private int moviePrice;

    public SeatSelectionWindow(String movie, int price) {
        this.selectedMovie = movie;
        this.moviePrice = price;

        setTitle("Seat Selection - " + movie);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeSeats();
        createSeatPanel();

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> openPaymentWindow());
        add(nextButton, BorderLayout.SOUTH);

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
        JPanel seatPanel = new JPanel(new GridLayout(ROWS, SEATS_PER_ROW));
        seatButtons = new JButton[ROWS][SEATS_PER_ROW];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < SEATS_PER_ROW; j++) {
                seatButtons[i][j] = new JButton("R" + (i + 1) + "S" + (j + 1));
                int row = i, seat = j;
                seatButtons[i][j].addActionListener(e -> selectSeat(row, seat));
                seatPanel.add(seatButtons[i][j]);
            }
        }
        add(seatPanel, BorderLayout.CENTER);
    }

    private void selectSeat(int row, int seat) {
        if (seatAvailability[row][seat]) {
            if (selectedRow != -1 && selectedSeat != -1) {
                seatButtons[selectedRow][selectedSeat].setBackground(null);
            }
            selectedRow = row;
            selectedSeat = seat;
            seatButtons[row][seat].setBackground(Color.GREEN);
        } else {
            JOptionPane.showMessageDialog(this, "This seat is already taken.");
        }
    }

    private void openPaymentWindow() {
        if (selectedRow == -1 || selectedSeat == -1) {
            JOptionPane.showMessageDialog(this, "Please select a seat first.");
            return;
        }
        new PaymentWindow(selectedMovie, moviePrice, selectedRow, selectedSeat);
        this.dispose();
    }
}