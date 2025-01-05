import javax.swing.*;
import java.awt.*;

public class SeatSelectionWindow extends JFrame {
    private static final int ROWS = 7;
    private static final int SEATS_PER_ROW = 10;
    private static boolean[][] seatAvailability = new boolean[ROWS][SEATS_PER_ROW];

    private JButton[][] seatButtons;
    private int selectedRow = -1;
    private int selectedSeat = -1;
    private String selectedMovie;
    private String selectedShowTime;
    private int moviePrice;
    private JFrame previousWindow;

    public SeatSelectionWindow(String movie, String showTime, int price, JFrame previousWindow) {
        this.selectedMovie = movie;
        this.selectedShowTime = showTime;
        this.moviePrice = price;
        this.previousWindow = previousWindow;

        setTitle("Seat Selection - " + movie + " (" + showTime + ")");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeSeats();
        createSeatPanel();

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBack());
        buttonPanel.add(backButton);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> openPaymentWindow());
        buttonPanel.add(nextButton);

        add(buttonPanel, BorderLayout.SOUTH);

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
        seatButtons = new JButton[ROWS][SEATS_PER_ROW];
        GridBagConstraints gbc = new GridBagConstraints();

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
                seatButtons[i][j].addActionListener(e -> selectSeat(row, seat));

                if (i >= ROWS - 2) {
                    seatButtons[i][j].setBackground(Color.YELLOW);
                    seatButtons[i][j].setToolTipText("Premium Seat");
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

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(screenLabel, BorderLayout.NORTH);
        mainPanel.add(seatPanel, BorderLayout.CENTER);

        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
    }

    private void selectSeat(int row, int seat) {
        if (seatAvailability[row][seat]) {
            if (selectedRow != -1 && selectedSeat != -1) {
                seatButtons[selectedRow][selectedSeat].setBackground(selectedRow >= ROWS - 2 ? Color.YELLOW : null);
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
        new PaymentWindow(selectedMovie, selectedShowTime, moviePrice, selectedRow, selectedSeat, this);
        this.setVisible(false);
    }

    private void goBack() {
        previousWindow.setVisible(true);
        this.dispose();
    }
}