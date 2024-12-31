import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class PaymentWindow extends JFrame {
    private JTextField paymentField;
    private JTextArea summaryArea;
    private String selectedMovie;
    private int moviePrice;
    private int selectedRow;
    private int selectedSeat;

    public PaymentWindow(String movie, int price, int row, int seat) {
        this.selectedMovie = movie;
        this.moviePrice = price;
        this.selectedRow = row;
        this.selectedSeat = seat;

        setTitle("Payment - " + movie);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        createPaymentPanel();
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

    private void createSummaryPanel() {
        summaryArea = new JTextArea(10, 30);
        summaryArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(summaryArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void processPayment() {
        try {
            int payment = Integer.parseInt(paymentField.getText());
            if (payment < moviePrice) {
                JOptionPane.showMessageDialog(this, "Insufficient payment. Please enter at least ₱" + moviePrice);
                return;
            }

            int change = payment - moviePrice;
            String ticketNumber = String.format("%04d", new Random().nextInt(10000));

            String summary = "--- Reservation Summary ---\n" +
                    "Movie: " + selectedMovie + "\n" +
                    "Seat: Row " + (selectedRow + 1) + ", Seat " + (selectedSeat + 1) + "\n" +
                    "Price: ₱" + moviePrice + "\n" +
                    "Paid: ₱" + payment + "\n" +
                    "Change: ₱" + change + "\n" +
                    "Ticket Number: " + ticketNumber + "\n" +
                    "Enjoy your movie!";

            summaryArea.setText(summary);
            paymentField.setEnabled(false);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid payment amount.");
        }
    }
}