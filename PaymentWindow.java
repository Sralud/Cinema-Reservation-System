import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentWindow extends JFrame {
    private JTextField paymentField;
    private JTextArea summaryArea;
    private String selectedMovie;
    private String selectedShowTime;
    private int moviePrice;
    private List<Point> selectedSeats;
    // Remove this line since we don't need to track the previous window anymore
    // private JFrame previousWindow;

    private Map<String, JSpinner> snackSpinners = new HashMap<>();
    private Map<String, JSpinner> drinkSpinners = new HashMap<>();

    private static final String[] SNACKS = {"Popcorn", "Pizza", "Hotdog", "Chips", "French Fries"};
    private static final int[] SNACK_PRICES = {100, 150, 80, 50, 70};
    private static final String[] DRINKS = {"Milktea", "Iced Tea", "Coke", "Mountain Dew", "Water"};
    private static final int[] DRINK_PRICES = {90, 60, 50, 50, 30};

    // Update the constructor to remove the previousWindow parameter
    public PaymentWindow(String movie, String showTime, int price, List<Point> seats) {
        this.selectedMovie = movie;
        this.selectedShowTime = showTime;
        this.moviePrice = price;
        this.selectedSeats = seats;
        // Remove this line
        // this.previousWindow = previousWindow;

        setTitle("Payment - " + movie);
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(240, 240, 240));

        createPaymentPanel();
        createFoodAndDrinkPanel();
        createSummaryPanel();

        setVisible(true);
    }

    private void createPaymentPanel() {
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        paymentPanel.setOpaque(false);
        JLabel paymentLabel = new JLabel("Payment Amount: ₱");
        paymentLabel.setFont(new Font("Arial", Font.BOLD, 16));
        paymentPanel.add(paymentLabel);
        paymentField = new JTextField(10);
        paymentField.setFont(new Font("Arial", Font.PLAIN, 16));
        paymentPanel.add(paymentField);
        JButton payButton = new JButton("Pay");
        payButton.setFont(new Font("Arial", Font.BOLD, 16));
        payButton.addActionListener(e -> processPayment());
        paymentPanel.add(payButton);
        add(paymentPanel, BorderLayout.NORTH);
    }

    private void createFoodAndDrinkPanel() {
        JPanel foodAndDrinkPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        foodAndDrinkPanel.setOpaque(false);

        JPanel snackPanel = createItemPanel("Snacks", SNACKS, SNACK_PRICES, snackSpinners);
        JPanel drinkPanel = createItemPanel("Drinks", DRINKS, DRINK_PRICES, drinkSpinners);

        foodAndDrinkPanel.add(snackPanel);
        foodAndDrinkPanel.add(drinkPanel);

        add(foodAndDrinkPanel, BorderLayout.CENTER);
    }

    private JPanel createItemPanel(String title, String[] items, int[] prices, Map<String, JSpinner> spinners) {
        JPanel panel = new JPanel(new GridLayout(items.length + 1, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title));
        panel.setOpaque(false);

        for (int i = 0; i < items.length; i++) {
            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            itemPanel.setOpaque(false);
            JLabel itemLabel = new JLabel(items[i] + " (₱" + prices[i] + ")");
            itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            itemPanel.add(itemLabel);

            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
            spinner.setPreferredSize(new Dimension(50, 30));
            itemPanel.add(spinner);

            spinners.put(items[i], spinner);
            panel.add(itemPanel);
        }

        return panel;
    }

    private void createSummaryPanel() {
        JPanel summaryPanel = new JPanel(new BorderLayout(10, 10));
        summaryPanel.setOpaque(false);
        summaryArea = new JTextArea(25, 40);
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(summaryArea);
        summaryPanel.add(scrollPane, BorderLayout.CENTER);

        JButton backToMenuButton = new JButton("Back to Menu");
        backToMenuButton.setFont(new Font("Arial", Font.BOLD, 16));
        backToMenuButton.addActionListener(e -> backToMenu());
        backToMenuButton.setEnabled(false);
        summaryPanel.add(backToMenuButton, BorderLayout.SOUTH);

        add(summaryPanel, BorderLayout.EAST);
    }

    private void processPayment() {
        try {
            int totalPrice = calculateTotalPrice();

            int payment = Integer.parseInt(paymentField.getText());
            if (payment < totalPrice) {
                JOptionPane.showMessageDialog(this, "Insufficient payment. Please enter at least ₱" + totalPrice);
                return;
            }

            int change = payment - totalPrice;
            List<String> ticketNumbers = generateTicketNumbers(selectedSeats.size());

            // Reserve the seats
            SeatManager.reserveSeats(selectedMovie, selectedSeats);

            StringBuilder summary = new StringBuilder("--- Reservation Summary ---\n\n");
            summary.append("Movie: ").append(selectedMovie).append("\n");
            summary.append("Show Time: ").append(selectedShowTime).append("\n\n");
            summary.append("Individual Tickets:\n");

            for (int i = 0; i < selectedSeats.size(); i++) {
                Point seat = selectedSeats.get(i);
                summary.append(String.format("Ticket %d:\n", i + 1));
                summary.append(String.format("  Seat: %c%d\n", (char)('A' + seat.x), seat.y + 1));
                summary.append(String.format("  Ticket Number: %s\n", ticketNumbers.get(i)));
                summary.append(String.format("  Price: ₱%d\n\n", moviePrice));
            }

            summary.append("Food and Drinks:\n");
            appendItemSummary(summary, "Snacks", SNACKS, SNACK_PRICES, snackSpinners);
            appendItemSummary(summary, "Drinks", DRINKS, DRINK_PRICES, drinkSpinners);

            summary.append("\nTotal Price: ₱").append(totalPrice).append("\n");
            summary.append("Paid: ₱").append(payment).append("\n");
            summary.append("Change: ₱").append(change).append("\n\n");
            summary.append("Enjoy your movie!");

            summaryArea.setText(summary.toString());
            paymentField.setEnabled(false);
            disableSpinners();

            JButton backToMenuButton = (JButton) ((JPanel) getContentPane().getComponent(2)).getComponent(1);
            backToMenuButton.setEnabled(true);

            JOptionPane.showMessageDialog(this, "Your reservation is complete. Enjoy your movie!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid payment amount.");
        }
    }

    private int calculateTotalPrice() {
        int total = moviePrice * selectedSeats.size();
        total += calculateItemsTotal(SNACKS, SNACK_PRICES, snackSpinners);
        total += calculateItemsTotal(DRINKS, DRINK_PRICES, drinkSpinners);
        return total;
    }

    private int calculateItemsTotal(String[] items, int[] prices, Map<String, JSpinner> spinners) {
        int total = 0;
        for (int i = 0; i < items.length; i++) {
            int quantity = (int) spinners.get(items[i]).getValue();
            total += quantity * prices[i];
        }
        return total;
    }

    private void appendItemSummary(StringBuilder summary, String category, String[] items, int[] prices, Map<String, JSpinner> spinners) {
        summary.append("  ").append(category).append(":\n");
        for (int i = 0; i < items.length; i++) {
            int quantity = (int) spinners.get(items[i]).getValue();
            if (quantity > 0) {
                summary.append(String.format("    %s x%d: ₱%d\n", items[i], quantity, quantity * prices[i]));
            }
        }
    }

    private List<String> generateTicketNumbers(int count) {
        List<String> ticketNumbers = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            ticketNumbers.add(String.format("%04d", random.nextInt(10000)));
        }
        return ticketNumbers;
    }

    private void disableSpinners() {
        for (JSpinner spinner : snackSpinners.values()) {
            spinner.setEnabled(false);
        }
        for (JSpinner spinner : drinkSpinners.values()) {
            spinner.setEnabled(false);
        }
    }

    private void backToMenu() {
        new MovieSelectionWindow();
        this.dispose();
    }
}