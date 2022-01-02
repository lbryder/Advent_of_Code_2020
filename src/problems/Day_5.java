package problems;

import helpers.Helper;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day_5 {
    public static long problem(String path) {
        Collection<String> inputLines = Helper.readFile(path);
        List<Long> seatIds = inputLines.stream()
                .map(Day_5::parseSeating)
                .map(Seat::getId)
                .collect(Collectors.toList());

        Integer freeSeat = getFreeSeat(seatIds);
        if (freeSeat != null) {
            return freeSeat;
        }

        //Get highest Seat id
        return getMax(seatIds);
    }

    private static Integer getFreeSeat(List<Long> seatIds) {
        Set<Integer> freeSeats = initEmptyPlane();
        seatIds.forEach(id -> freeSeats.remove(id.intValue()));
        return freeSeats.stream().
                filter(freeSeat -> isFreeSeat(freeSeats, freeSeat))
                .findFirst().orElse(null);
    }

    private static Set<Integer> initEmptyPlane() {
        Set<Integer> freeSeats = new HashSet<>(1032);
        for (int i = 0; i < 1032; i++) {
            freeSeats.add(i);
        }
        return freeSeats;
    }

    private static boolean isFreeSeat(Set<Integer> freeSeats, Integer freeSeat) {
        //Free seat must be between 2 taken seat-ids. Apparently..
        return !freeSeats.contains(freeSeat + 1) && !freeSeats.contains(freeSeat - 1);
    }

    private static Long getMax(Collection<Long> seatIds) {
        return seatIds.stream()
                .max(Comparator.naturalOrder())
                .orElse(0L);
    }

    private static Seat parseSeating(String input) {
        int row = findSeat(input, 128, 'F', 'B');
        int column = findSeat(input, 8, 'L', 'R');
        //        System.out.println("Seat - row: " + row + " Seat: " + column);
        return new Seat(input, row, column);
    }

    private static int findSeat(String input, int max, char downChar, char upChar) {
        int current_seat = 0;
        int to_add = max / 2;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == downChar) {
                // do nothing - Lower half
            } else if (c == upChar) {
                current_seat += to_add;
            } else {
                continue;
            }
            to_add = to_add / 2;
        }
        return current_seat;
    }


    private static class Seat {
        String input;
        int row;
        int column;

        public Seat(String input, int row, int seat) {
            this.row = row;
            this.column = seat;
            this.input = input;
        }

        public long getId() {
            return row * 8 + column;
        }

        public char getSeat() {
            int a_char_decimal = 65;
            return (char) (column + a_char_decimal);
        }
    }
}
