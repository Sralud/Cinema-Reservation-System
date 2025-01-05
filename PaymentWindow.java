import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class PaymentWindow extends JFrame {
    private JTextField paymentField;
    private JTextArea summaryArea;
    private String selectedMovie;
    private String selectedShowTime;
    private int moviePrice;
    private int selectedRow;
    private int selectedSeat;
    private JCheckBox popcornCheckBox;
    private JCheckBox drinkCheckBox;
    private JFrame previousWindow;

    public PaymentWindow(String movie, String showTime, int price, int row, int seat, JFrame previousWindow) {
        this.selectedMovie = movie;
        this.selectedShowTime = showTime;
        this.moviePrice = price;
        this.selectedRow = row;
        this.selectedSeat = seat;
        this.previousWindow = previousWindow;

        setTitle("Payment - " + movie);
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        createPaymentPanel();
        createConcessionsPanel();
        createSummaryPanel();

        setVisible(true);
    }

    private void createPaymentPanel() {
        JPanel paymentPanel = new JPanel();
        paymentPanel.add(new JLabel("Payment: ₱"));
        paymentField = new JTextField(10);
        paymentPanel.add(paymentField);
        JButton payButton = new JButton("Pay");
        payButton.addActionListener(e -> processPayment());
        paymentPanel.add(payButton);
        add(paymentPanel, BorderLayout.NORTH);
    }

    private void createConcessionsPanel() {
        JPanel concessionsPanel = new JPanel();
        popcornCheckBox = new JCheckBox("Popcorn (₱100)");
        drinkCheckBox = new JCheckBox("Drink (₱50)");
        concessionsPanel.add(popcornCheckBox);
        concessionsPanel.add(drinkCheckBox);
        add(concessionsPanel, BorderLayout.CENTER);
    }

    private void createSummaryPanel() {
        summaryArea = new JTextArea(15, 30);
        summaryArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(summaryArea);
        add(scrollPane, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBack());
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void processPayment() {
        try {
            int totalPrice = moviePrice;
            if (popcornCheckBox.isSelected()) totalPrice += 100;
            if (drinkCheckBox.isSelected()) totalPrice += 50;

            int payment = Integer.parseInt(paymentField.getText());
            if (payment < totalPrice) {
                JOptionPane.showMessageDialog(this, "Insufficient payment. Please enter at least ₱" + totalPrice);
                return;
            }

            int change = payment - totalPrice;
            String ticketNumber = String.format("%04d", new Random().nextInt(10000));

            String summary = "--- Reservation Summary ---\n" +
                    "Movie: " + selectedMovie + "\n" +
                    "Show Time: " + selectedShowTime + "\n" +
                    "Seat: " + (char)('A' + selectedRow) + (selectedSeat + 1) + "\n" +
                    "Price: ₱" + moviePrice + "\n";

            if (popcornCheckBox.isSelected()) summary += "Popcorn: ₱100\n";
            if (drinkCheckBox.isSelected()) summary += "Drink: ₱50\n";

            summary += "Total Price: ₱" + totalPrice + "\n" +
                    "Paid: ₱" + payment + "\n" +
                    "Change: ₱" + change + "\n" +
                    "Ticket Number: " + ticketNumber + "\n" +
                    "Enjoy your movie!";

            summaryArea.setText(summary);
            paymentField.setEnabled(false);

            // Updated confirmation message
            JOptionPane.showMessageDialog(this, "Your reservation is complete. Enjoy your movie!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid payment amount.");
        }
    }

    private void goBack() {
        previousWindow.setVisible(true);
        this.dispose();
    }
}