package problems;

import helpers.Helper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class Day_17 {

    public static long problem(String path) {
        Grid gridStart = initCubes(path);
        Grid grid = gridStart;
        int steps = 6;
        for (int i = 0; i < steps; i++) {
            grid = grid.step();
        }

        return grid.numberOfActive();

    }

    private static Grid initCubes(String path) {
        List<String> inputLines = Helper.readFile(path);
        List<Cube> cubes = new ArrayList<>();
        for (int y = 0; y < inputLines.size(); y++) {
            String inputLine = inputLines.get(y);
            char[] charArray = inputLine.toCharArray();
            for (int x = 0, charArrayLength = charArray.length; x < charArrayLength; x++) {
                char c = charArray[x];
                if (c == '#') {
                    Cube cube = new Cube(x, y, 0, true);
                    cubes.add(cube);
                } else {
                    //Inactive. Skip
                }
            }
        }
        return new Grid(cubes);
    }


    private static class Cube {
        private boolean active;
        private final int x;
        private final int y;
        private final int z;
        private final int w;

        public Cube(int x, int y, int z, int w, boolean active) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.active = active;
            this.w = w;
        }

        public Cube(int x, int y, int z, boolean active) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = 0;
            this.active = active;
        }

        public boolean isOn(Cube cube) {
            return this.distanceTo(cube) == 0;
        }

        public boolean isCloseTo(Cube c) {
            int dist = this.distanceTo(c);
            return dist == 1;
        }

        private int distanceTo(Cube c) {
            int distX = Math.abs(this.x - c.x);
            int distY = Math.abs(this.y - c.y);
            int distZ = Math.abs(this.z - c.z);
            int distW = Math.abs(this.w - c.w);

            return IntStream.of(distX, distY, distZ, distW).max().orElse(0);

        }
    }

    private static class Grid {
        private final List<Cube> cubes;

        public Grid(List<Cube> cubes) {
            this.cubes = cubes;
        }

        public Grid step() {
            List<Cube> cubes = this.cubes;
            List<Cube> newCubes = new ArrayList<>();
            int minW = cubes.stream().map(c -> c.w - 1).min(Comparator.naturalOrder()).orElse(0);
            int maxW = cubes.stream().map(c -> c.w + 1).max(Comparator.naturalOrder()).orElse(0);

            int minZ = cubes.stream().map(c -> c.z - 1).min(Comparator.naturalOrder()).orElse(0);
            int maxZ = cubes.stream().map(c -> c.z + 1).max(Comparator.naturalOrder()).orElse(0);

            int minX = cubes.stream().map(c -> c.x - 1).min(Comparator.naturalOrder()).orElse(0);
            int maxX = cubes.stream().map(c -> c.x + 1).max(Comparator.naturalOrder()).orElse(0);

            int minY = cubes.stream().map(c -> c.y - 1).min(Comparator.naturalOrder()).orElse(0);
            int maxY = cubes.stream().map(c -> c.y + 1).max(Comparator.naturalOrder()).orElse(0);

            for (int w = minW; w <= maxW; w++) {

                for (int z = minZ; z <= maxZ; z++) {
                    for (int x = minX; x <= maxX; x++) {
                        for (int y = minY; y <= maxY; y++) {
                            Cube newCube = stepCube(cubes, x, y, z, w);
                            if (newCube.active) {
                                newCubes.add(newCube);
                            }
                        }
                    }
                }
            }

            return new Grid(newCubes);
        }

        private Cube stepCube(List<Cube> cubes, int x, int y, int z, int w) {
            Cube newCube = new Cube(x, y, z, w, false);
            long count = cubes.stream().filter(cube -> cube.isCloseTo(newCube)).count();
            if (count == 2) {
                boolean alreadyActive = this.isActive(newCube);
                if (alreadyActive) {
                    newCube.active = true;
                }
            } else if (count == 3) {
                newCube.active = true;
            }
            return newCube;
        }

        private boolean isActive(Cube newCube) {
            return cubes.stream()
                    .filter(c -> c.isOn(newCube))
                    .findFirst()
                    .map(c -> c.active).orElse(false);
        }

        public long numberOfActive() {
            return cubes.size();
        }
    }
}
