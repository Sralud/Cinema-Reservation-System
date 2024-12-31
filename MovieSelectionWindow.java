import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MovieSelectionWindow extends JFrame {
    private static final String[] MOVIES = {"Avengers: Endgame", "The Lion King", "Joker", "Toy Story 4"};
    private static final int[] PRICES = {300, 250, 320, 280};

    private JComboBox<String> movieComboBox;
    private JLabel priceLabel;

    public MovieSelectionWindow() {
        setTitle("Movie Selection");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel moviePanel = new JPanel();
        moviePanel.add(new JLabel("Select Movie: "));
        movieComboBox = new JComboBox<>(MOVIES);
        movieComboBox.addActionListener(e -> updatePrice());
        moviePanel.add(movieComboBox);

        priceLabel = new JLabel("Price: ₱" + PRICES[0]);
        moviePanel.add(priceLabel);

        add(moviePanel, BorderLayout.CENTER);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> openSeatSelection());
        add(nextButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updatePrice() {
        int selectedIndex = movieComboBox.getSelectedIndex();
        priceLabel.setText("Price: ₱" + PRICES[selectedIndex]);
    }

    private void openSeatSelection() {
        String selectedMovie = (String) movieComboBox.getSelectedItem();
        int moviePrice = PRICES[movieComboBox.getSelectedIndex()];
        new SeatSelectionWindow(selectedMovie, moviePrice);
        this.dispose();
    }
}