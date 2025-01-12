import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

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
        setSize(600, 500);
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
        summaryArea = new JTextArea(15, 30);
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Arial", Font.PLAIN, 14));
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
            String ticketNumber = String.format("%04d", new Random().nextInt(10000));

            StringBuilder summary = new StringBuilder("--- Reservation Summary ---\n");
            summary.append("Movie: ").append(selectedMovie).append("\n");
            summary.append("Show Time: ").append(selectedShowTime).append("\n");
            summary.append("Seats: ");
            for (Point seat : selectedSeats) {
                summary.append(String.format("%c%d ", (char)('A' + seat.x), seat.y + 1));
            }
            summary.append("\n");
            summary.append("Price per Ticket: ₱").append(moviePrice).append("\n");
            summary.append("Total Ticket Price: ₱").append(moviePrice * selectedSeats.size()).append("\n");

            if (popcornCheckBox.isSelected()) summary.append("Popcorn: ₱100\n");
            if (drinkCheckBox.isSelected()) summary.append("Drink: ₱50\n");

            summary.append("Total Price: ₱").append(totalPrice).append("\n");
            summary.append("Paid: ₱").append(payment).append("\n");
            summary.append("Change: ₱").append(change).append("\n");
            summary.append("Ticket Number: ").append(ticketNumber).append("\n");
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

    private void backToMenu() {
        new MovieSelectionWindow();
        this.dispose();
    }
}