import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class MovieSelectionWindow extends JFrame {
    private static final String[] MOVIES = {"Avengers: Endgame", "The Lion King", "Joker", "Toy Story 4"};
    private static final int[] PRICES = {300, 250, 320, 280};
    private static final String[] DESCRIPTIONS = {
        "The Avengers take a final stand against Thanos in this thrilling conclusion to the Infinity Saga.",
        "A young lion prince flees his kingdom only to learn the true meaning of responsibility and bravery.",
        "A gritty character study of Arthur Fleck, a man disregarded by society, and a broader cautionary tale.",
        "Woody, Buzz Lightyear and the rest of the gang embark on a road trip with new toys and old friends."
    };
    private static final String[] POSTERS = {
        "https://m.media-amazon.com/images/M/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_SX300.jpg",
        "https://m.media-amazon.com/images/M/MV5BMjIwMjE1Nzc4NV5BMl5BanBnXkFtZTgwNDg4OTA1NzM@._V1_SX300.jpg",
        "https://m.media-amazon.com/images/M/MV5BNGVjNWI4ZGUtNzE0MS00YTJmLWE0ZDctN2ZiYTk2YmI3NTYyXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_SX300.jpg",
        "https://m.media-amazon.com/images/M/MV5BMTYzMDM4NzkxOV5BMl5BanBnXkFtZTgwNzM1Mzg2NzM@._V1_SX300.jpg"
    };
    private static final String[][] SHOW_TIMES = {
        {"10:00 AM", "2:00 PM", "6:00 PM", "9:00 PM"},
        {"11:00 AM", "3:00 PM", "7:00 PM", "10:00 PM"},
        {"12:00 PM", "4:00 PM", "8:00 PM", "11:00 PM"},
        {"1:00 PM", "5:00 PM", "9:00 PM", "12:00 AM"}
    };

    private JComboBox<String> movieComboBox;
    private JLabel posterLabel;
    private JTextArea descriptionArea;
    private JComboBox<String> showTimeComboBox;
    private JLabel priceLabel;

    public MovieSelectionWindow() {
        setTitle("Movie Selection");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        movieComboBox = new JComboBox<>(MOVIES);
        movieComboBox.addActionListener(e -> updateMovieInfo());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(movieComboBox, gbc);

        posterLabel = new JLabel();
        updatePosterImage(0);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        mainPanel.add(posterLabel, gbc);

        descriptionArea = new JTextArea(DESCRIPTIONS[0]);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setOpaque(false);
        descriptionArea.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        mainPanel.add(descriptionArea, gbc);

        showTimeComboBox = new JComboBox<>(SHOW_TIMES[0]);
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(showTimeComboBox, gbc);

        priceLabel = new JLabel("Price: ₱" + PRICES[0]);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(priceLabel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> openSeatSelection());
        buttonPanel.add(nextButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateMovieInfo() {
        int selectedIndex = movieComboBox.getSelectedIndex();
        updatePosterImage(selectedIndex);
        descriptionArea.setText(DESCRIPTIONS[selectedIndex]);
        showTimeComboBox.setModel(new DefaultComboBoxModel<>(SHOW_TIMES[selectedIndex]));
        priceLabel.setText("Price: ₱" + PRICES[selectedIndex]);
    }

    private void updatePosterImage(int index) {
        try {
            URL imageUrl = new URL(POSTERS[index]);
            ImageIcon imageIcon = new ImageIcon(imageUrl);
            Image image = imageIcon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            e.printStackTrace();
            posterLabel.setIcon(null);
        }
    }

    private void openSeatSelection() {
        String selectedMovie = (String) movieComboBox.getSelectedItem();
        String selectedShowTime = (String) showTimeComboBox.getSelectedItem();
        int moviePrice = PRICES[movieComboBox.getSelectedIndex()];
        new SeatSelectionWindow(selectedMovie, selectedShowTime, moviePrice, this);
        this.setVisible(false);
    }
}