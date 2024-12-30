import java.util.Scanner;
import java.util.Random;

public class CinemaReservation {
    private static final int ROWS = 5;
    private static final int SEATS_PER_ROW = 8;
    private static final String[] MOVIES = {"Avengers: Endgame", "The Lion King", "Joker", "Toy Story 4"};
    private static final int[] PRICES = {300, 250, 320, 280};
    private static boolean[][] seatAvailability = new boolean[ROWS][SEATS_PER_ROW];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < SEATS_PER_ROW; j++) {
                seatAvailability[i][j] = true;
            }
        }

        System.out.println("Welcome to the Cinema Reservation System!");

        System.out.println("\nAvailable movies:");
        for (int i = 0; i < MOVIES.length; i++) {
            System.out.printf("%d. %s (₱%d)\n", i + 1, MOVIES[i], PRICES[i]);
        }

        System.out.print("Enter the number of the movie you want to watch: ");
        int movieChoice = scanner.nextInt() - 1;
        String selectedMovie = MOVIES[movieChoice];
        int moviePrice = PRICES[movieChoice];

        System.out.printf("You selected: %s (₱%d)\n", selectedMovie, moviePrice);

        System.out.println("\nSeat Availability (O = Available, X = Taken):");
        displaySeatAvailability();

        int row, seat;
        do {
            System.out.print("Enter the row number (1-5): ");
            row = scanner.nextInt() - 1;
            System.out.print("Enter the seat number (1-8): ");
            seat = scanner.nextInt() - 1;

            if (!seatAvailability[row][seat]) {
                System.out.println("This seat is already taken. Please choose another seat.");
            }
        } while (!seatAvailability[row][seat]);

        seatAvailability[row][seat] = false;
        System.out.printf("You selected seat: Row %d, Seat %d\n", row + 1, seat + 1);

        System.out.printf("\nTotal amount due: ₱%d\n", moviePrice);
        System.out.print("Enter the amount you're paying: ₱");
        int payment = scanner.nextInt();

        while (payment < moviePrice) {
            System.out.println("Insufficient payment. Please enter an amount equal to or greater than the ticket price.");
            System.out.print("Enter the amount you're paying: ₱");
            payment = scanner.nextInt();
        }

        int change = payment - moviePrice;
        System.out.printf("Change: ₱%d\n", change);

        String ticketNumber = String.format("%04d", random.nextInt(10000));

        System.out.println("\n--- Reservation Summary ---");
        System.out.println("Movie: " + selectedMovie);
        System.out.printf("Seat: Row %d, Seat %d\n", row + 1, seat + 1);
        System.out.printf("Price: ₱%d\n", moviePrice);
        System.out.printf("Paid: ₱%d\n", payment);
        System.out.printf("Change: ₱%d\n", change);
        System.out.println("Ticket Number: " + ticketNumber);
        System.out.println("Enjoy your movie!");

        scanner.close();
    }

    private static void displaySeatAvailability() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < SEATS_PER_ROW; j++) {
                System.out.print(seatAvailability[i][j] ? "O " : "X ");
            }
            System.out.println();
        }
    }
}