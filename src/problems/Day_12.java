package problems;

import helpers.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Day_12 {

    private static Character north = 'N';
    private static Character east = 'E';
    private static Character west = 'W';
    private static Character south = 'S';
    public static List<Character> compassTurningRigth = Arrays.asList(north, east, south, west);

    public static long problem(String path) {
        Collection<String> inputLines = Helper.readFile(path);
        List<Direction> directions = parsePositions(inputLines);
        Ship ship = new Ship(0,0);
        ship.setWaypoint(1, 10);
        for (Direction d : directions) {
//            ship.move_1(d);
            ship.move_2(d);
        }


        return ship.getDistance();
    }

    private static List<Direction> parsePositions(Collection<String> inputLines) {
        List<Direction> list = new ArrayList<>();
        for (String inputLine : inputLines) {
            Direction pos = parsePosition(inputLine);
            list.add(pos);
        }
        return list;
    }

    private static Direction parsePosition(String inputLine) {

        return new Direction(inputLine.charAt(0), Integer.parseInt(inputLine.substring(1)));
    }

    private static class Ship {
        char currentDirection = east;
        int northSouth = 0;
        int eastWest = 0;
        Ship waypoint = null;

        public Ship(int northSouth, int eastWest) {
            this.northSouth = northSouth;
            this.eastWest = eastWest;
        }

        public Ship move_2(Direction direction) {
            waypoint.rotate(direction);
            int forward = direction.getForwardFactor();
            this.northSouth += forward * waypoint.northSouth;
            this.eastWest += forward * waypoint.eastWest;

            waypoint.eastWest += direction.getEastWest();
            waypoint.northSouth += direction.getNorthSouth();

            return this;
        }

        private void rotate(Direction direction) {
            int degrees = direction.getDegrees();
            int moves = (degrees % 360) / 90;
            int timesRight = (moves + 4) % 4;
            for (int i = 0; i < timesRight; i++) {
                this.rotateRight();
            }
        }

        private void rotateRight() {
            int newNorthSouth = -this.eastWest;
            int newEastWest = this.northSouth;
            this.northSouth = newNorthSouth;
            this.eastWest = newEastWest;
        }

        public Ship move_1(Direction direction) {
            currentDirection = direction.calcNewDirection(currentDirection);
            direction.getForward(currentDirection)
                    .ifPresent(this::move_1);
            this.northSouth += direction.getNorthSouth();
            this.eastWest += direction.getEastWest();
            return this;
        }

        public long getDistance() {
            return Math.abs(northSouth) + Math.abs(eastWest);
        }

        public void setWaypoint(int northSouth, int eastWest) {
            this.waypoint = new Ship(northSouth, eastWest);
        }
    }

    private static class Direction {
        private final char direction;
        private final int length;

        public Direction(char move, int length) {
            this.direction = move;
            this.length = length;
        }

        public int getNorthSouth() {
            return distInDirection(north, south);
        }

        public int getEastWest() {
            return distInDirection(east, west);
        }

        private int distInDirection(Character forward, Character backward) {
            if (forward.equals(direction)) {
                return length;
            } else if (backward.equals(direction)) {
                return -length;
            } else {
                return 0;
            }
        }

        public char calcNewDirection(char currentDirection) {
            int degrees = getDegrees();
            return getNewDirection(currentDirection, degrees);
        }

        private int getDegrees() {
            int degrees = 0;
            if ('R' == this.direction) {
                degrees = this.length;
            } else if ('L' == this.direction) {
                degrees = -length;
            }
            return degrees;
        }

        private Character getNewDirection(char currentDirection, int degrees) {
            int current = compassTurningRigth.indexOf(currentDirection);
            int shifts = (degrees % 360) / 90;
            int newIndex = (current + shifts + compassTurningRigth.size()) % compassTurningRigth.size();
            return compassTurningRigth.get(newIndex);
        }

        public int getForwardFactor() {
            return this.direction == 'F'? length : 0;
        }

        public Optional<Direction> getForward(char currentDirection) {
            if (this.direction == 'F') {
                return Optional.of(new Direction(currentDirection, length));
            }
            return Optional.empty();
        }
    }
}
