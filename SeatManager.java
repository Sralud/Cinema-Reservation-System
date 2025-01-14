import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatManager {
    private static final int ROWS = 7;
    private static final int SEATS_PER_ROW = 10;
    private static Map<String, boolean[][]> movieSeats = new HashMap<>();

    public static boolean[][] getSeatsForMovie(String movie) {
        return movieSeats.computeIfAbsent(movie, k -> new boolean[ROWS][SEATS_PER_ROW]);
    }

    public static void reserveSeat(String movie, int row, int seat) {
        boolean[][] seats = getSeatsForMovie(movie);
        seats[row][seat] = true;
    }

    public static void reserveSeats(String movie, List<Point> selectedSeats) {
        for (Point seat : selectedSeats) {
            reserveSeat(movie, seat.x, seat.y);
        }
    }
}