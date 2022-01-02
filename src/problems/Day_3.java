package problems;

import helpers.Helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day_3 {

    public static Long problem(String path) {
        Collection<String> inputLines = Helper.readFile(path);
        TreeMap map = parseMap(inputLines);

        ArrayList<Coordinate> strategies = new ArrayList<>();
        strategies.add(new Coordinate(1, 1));
        strategies.add(new Coordinate(3, 1));
        strategies.add(new Coordinate(5, 1));
        strategies.add(new Coordinate(7, 1));
        strategies.add(new Coordinate(1, 2));

        Set<Long> trees = strategies.stream().map(c -> countTrees(map, c)).collect(Collectors.toSet());
        return trees.stream().reduce(1L, (integer, integer2) -> integer * integer2);
    }

    private static long countTrees(TreeMap map, Coordinate direction) {
        Route route = traverseMap(map, direction);
        long trees = map.countTrees(route);
        System.out.println("Direction: " + direction + " Number of trees: " + trees);
        return trees;
    }

    private static Route traverseMap(TreeMap map, Coordinate direction) {
        Route route = new Route();
        Coordinate coord = new Coordinate(0, 0);
        int depth = map.depth();
        while (coord.y <= depth) {
            route.add(coord);
            coord = coord.move(direction);
        }
        return route;
    }

    private static TreeMap parseMap(Collection<String> inputLines) {
        return new TreeMap(inputLines);
    }

    private static class Coordinate {
        int x;
        int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Coordinate move(Coordinate direction) {
            return new Coordinate(this.x + direction.x, y + direction.y);
        }

        @Override
        public String toString() {
            return "x: " + x + ", y: " + y;
        }
    }

    private static class Route {
        List<Coordinate> coordinates = new ArrayList();

        public List<Coordinate> getCoordinates() {
            return coordinates;
        }

        public void add(Coordinate coord) {
            coordinates.add(coord);
        }
    }

    private static class TreeMap {
        List<List<Boolean>> map = new ArrayList<>();

        public TreeMap(Collection<String> input) {
            for (String inputLine : input) {
                List<Boolean> mapRow = new ArrayList<>();
                for (char c : inputLine.toCharArray()) {
                    boolean isTree = c == '#';
                    mapRow.add(isTree);
                }
                map.add(mapRow);
            }
        }

        Boolean isTree(Coordinate coordinate) {
            List<Boolean> row = map.get(coordinate.y);
            int numberOfColumns = row.size();
            return row.get(coordinate.x % numberOfColumns);
        }

        public long countTrees(Route route) {
            return route.coordinates.stream()
                    .filter(this::isTree)
                    .count();
        }

        public int depth() {
            return map.size() - 1;
        }
    }
}
