import javax.swing.*;
import java.awt.*;
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
        setTitle("Cinema Reservation System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(240, 240, 240));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Select a Movie");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        movieComboBox = new JComboBox<>(MOVIES);
        movieComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        movieComboBox.addActionListener(e -> updateMovieInfo());
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(movieComboBox, gbc);

        posterLabel = new JLabel();
        updatePosterImage(0);
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(posterLabel, gbc);

        descriptionArea = new JTextArea(DESCRIPTIONS[0], 5, 20);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setOpaque(false);
        descriptionArea.setEditable(false);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane, gbc);

        JPanel timeAndPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timeAndPricePanel.setOpaque(false);
        showTimeComboBox = new JComboBox<>(SHOW_TIMES[0]);
        showTimeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        timeAndPricePanel.add(new JLabel("Show Time: "));
        timeAndPricePanel.add(showTimeComboBox);

        priceLabel = new JLabel("Price: ₱" + PRICES[0]);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timeAndPricePanel.add(Box.createHorizontalStrut(20));
        timeAndPricePanel.add(priceLabel);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(timeAndPricePanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        JButton nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 16));
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
            Image image = imageIcon.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
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