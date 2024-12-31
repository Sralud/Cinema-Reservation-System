import javax.swing.SwingUtilities;

public class CinemaReservationSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MovieSelectionWindow());
    }
}