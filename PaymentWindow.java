import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class PaymentWindow extends JFrame {
    private JTextField paymentField;
    private JTextArea summaryArea;
    private String selectedMovie;
    private String selectedShowTime;
    private int moviePrice;
    private List<Point> selectedSeats;
    private JCheckBox popcornCheckBox;
    private JCheckBox drinkCheckBox;
    private JFrame previousWindow;

    public PaymentWindow(String movie, String showTime, int price, List<Point> seats, JFrame previousWindow) {
        this.selectedMovie = movie;
        this.selectedShowTime = showTime;
        this.moviePrice = price;
        this.selectedSeats = seats;
        this.previousWindow = previousWindow;

        setTitle("Payment - " + movie);
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(240, 240, 240));

        createPaymentPanel();
        createConcessionsPanel();
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

    private void createConcessionsPanel() {
        JPanel concessionsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        concessionsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Concessions"));
        concessionsPanel.setOpaque(false);
        popcornCheckBox = new JCheckBox("Popcorn (₱100)");
        popcornCheckBox.setFont(new Font("Arial", Font.PLAIN, 16));
        drinkCheckBox = new JCheckBox("Drink (₱50)");
        drinkCheckBox.setFont(new Font("Arial", Font.PLAIN, 16));
        concessionsPanel.add(popcornCheckBox);
        concessionsPanel.add(drinkCheckBox);
        add(concessionsPanel, BorderLayout.CENTER);
    }

    private void createSummaryPanel() {
        JPanel summaryPanel = new JPanel(new BorderLayout(10, 10));
        summaryPanel.setOpaque(false);
        summaryArea = new JTextArea(20, 35);
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
            int totalPrice = moviePrice * selectedSeats.size();
            if (popcornCheckBox.isSelected()) totalPrice += 100;
            if (drinkCheckBox.isSelected()) totalPrice += 50;

            int payment = Integer.parseInt(paymentField.getText());
            if (payment < totalPrice) {
                JOptionPane.showMessageDialog(this, "Insufficient payment. Please enter at least ₱" + totalPrice);
                return;
            }

            int change = payment - totalPrice;
            List<String> ticketNumbers = generateTicketNumbers(selectedSeats.size());

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

            summary.append("Snacks and Drinks:\n");
            if (popcornCheckBox.isSelected()) summary.append("  Popcorn: ₱100\n");
            if (drinkCheckBox.isSelected()) summary.append("  Drink: ₱50\n");
            summary.append("\n");

            summary.append("Total Price: ₱").append(totalPrice).append("\n");
            summary.append("Paid: ₱").append(payment).append("\n");
            summary.append("Change: ₱").append(change).append("\n\n");
            summary.append("Enjoy your movie!");

            summaryArea.setText(summary.toString());
            paymentField.setEnabled(false);
            popcornCheckBox.setEnabled(false);
            drinkCheckBox.setEnabled(false);

            JButton backToMenuButton = (JButton) ((JPanel) getContentPane().getComponent(2)).getComponent(1);
            backToMenuButton.setEnabled(true);

            JOptionPane.showMessageDialog(this, "Your reservation is complete. Enjoy your movie!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid payment amount.");
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

    private void backToMenu() {
        new MovieSelectionWindow();
        this.dispose();
    }
}