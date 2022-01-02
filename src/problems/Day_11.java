package problems;

import helpers.Helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Day_11 {

    static final Character floor = '.';
    static final Character empty = 'L';
    static final Character taken = '#';

    public static long problem(String path) {
        Collection<String> inputLines = Helper.readFile(path);
        List<List<Character>> currentSeats = parse(inputLines);
        List<List<Character>> newSeats = changeState(currentSeats);

        while (!currentSeats.equals(newSeats)) {
            printSeats(newSeats);
            currentSeats = newSeats;
            newSeats = changeState(currentSeats);
        }
        printSeats(currentSeats);
        return countOccupied(newSeats);
    }

    private static void printSeats(List<List<Character>> currentSeats) {
        for (List<Character> row : currentSeats) {
            for (Character currentSeat : row) {
                System.out.print(currentSeat);
            }
            System.out.println("");
        }
        System.out.println("");
    }

    private static long countOccupied(List<List<Character>> newSeats) {
        return newSeats.stream().flatMap(Collection::stream).filter(c -> c.equals(taken)).count();
    }

    private static List<List<Character>> changeState(List<List<Character>> currentState) {
        List<List<Character>> newState = new ArrayList<>();
        for (int i = 0, currentStateSize = currentState.size(); i < currentStateSize; i++) {
            List<Character> row = currentState.get(i);
            List<Character> newRow = new ArrayList<>();
            for (int j = 0; j < row.size(); j++) {
                Character newSeat = transform(i, j, currentState);
                newRow.add(newSeat);
            }
            newState.add(newRow);
        }

        return newState;
    }

    private static Character transform(int i, int j, List<List<Character>> currentState) {
        Character seat = currentState.get(i).get(j);
        if (seat.equals(floor)) {
            return floor;
        } else {
            List<Character> seeableSeats = seatsToSee(i, j, currentState);
            if (seat.equals(empty)) {
                return shouldTake(seeableSeats) ? taken : empty;
            } else {
                return shouldAbandon_2(seeableSeats) ? empty : taken;
            }
        }
    }

    private static boolean shouldAbandon_2(List<Character> seeableSeats) {
        return seeableSeats.stream().filter(taken::equals).count() >= 5L;
    }

    private static boolean shouldTake(List<Character> seeableSeats) {
        return seeableSeats.stream().noneMatch(taken::equals);
    }

    private static List<Character> seatsToSee(int i, int j, List<List<Character>> currentState) {
        List<Character> res = new ArrayList<>();
        for (int horisontal = -1; horisontal <= 1; horisontal++) {
            for (int vertical = -1; vertical <= 1; vertical++) {
                Character firstSeatInDirection = getFirstSeeableSeatInDirection(i, j, horisontal, vertical, currentState);
                res.add(firstSeatInDirection);
            }
        }
        return res;
    }

    private static Character getFirstSeeableSeatInDirection(int i, int j, int horisontal, int vertical, List<List<Character>> currentState) {
        Character character = floor;
        int rowIndex = i + horisontal;
        int colIndex = j + vertical;
        while (character == floor && isValidIndex(currentState, rowIndex, colIndex)) {
            if (rowIndex == i && colIndex == j) {
                return floor;
            }
            character = currentState.get(rowIndex).get(colIndex);
            rowIndex += horisontal;
            colIndex += vertical;
        }
        return character;
    }

    private static boolean isValidIndex(List<List<Character>> currentState, int rowIndex, int colIndex) {
        return rowIndex >= 0
                && rowIndex < currentState.size()
                && colIndex >= 0
                && colIndex < currentState.get(rowIndex).size();
    }

    private static boolean shouldAbandon(int i, int j, List<List<Character>> currentState) {
        int adjecentOccupied = 0;
        for (int r = Integer.max(i - 1, 0); r <= Integer.min(i + 1, currentState.size() - 1); r++) {
            List<Character> row = currentState.get(r);
            for (int k = Integer.max(j - 1, 0); k <= Integer.min(j + 1, row.size() - 1); k++) {
                if (i == r && j == k) {
                    continue;
                }
                Character character = row.get(k);
                if (character.equals(taken)) {
                    adjecentOccupied++;
                    if (adjecentOccupied > 3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean shouldTake(int i, int j, List<List<Character>> currentState) {
        boolean seen = false;
        for (int r = Integer.max(i - 1, 0); r <= currentState.size() || seen; r++) {
            List<Character> row = currentState.get(r);
            for (int k = Integer.max(j - 1, 0); k <= Integer.min(j + 1, row.size() - 1); k++) {
                if (i == r && j == k) {
                    continue;
                }
                Character character = row.get(k);
                if (character.equals(taken)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static List<List<Character>> parse(Collection<String> inputLines) {
        List<List<Character>> list = new ArrayList<>();
        for (String inputLine : inputLines) {
            List<Character> row = parseLine(inputLine);
            list.add(row);
        }
        return list;
    }

    private static List<Character> parseLine(String inputLine) {
        ArrayList<Character> row = new ArrayList<>();
        for (char c : inputLine.toCharArray()) {
            row.add(c);
        }
        return row;
    }

}
