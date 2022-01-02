package problems;

import helpers.Helper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day_13 {

    public static long problem(String path) {
        List<String> inputLines = Helper.readFile(path);
        int arrival = Integer.parseInt(inputLines.get(0));
        String[] busIds = inputLines.get(1).split(",");
        int res = part_1(arrival, busIds);

        List<Integer> list = getIntegers(busIds);
        Integer max = list.stream().filter(Objects::nonNull).max(Comparator.naturalOrder()).orElse(null);
        int shift = list.indexOf(max);
        int start = max - shift;
//        long time = getTime(list, max, start);

        List<Integer> sortedBusIds = list.stream()
                .filter(Objects::nonNull).sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        Integer maxBusId = sortedBusIds.get(0);
        long time = getTimeSieving(list, sortedBusIds, maxBusId);


        return time;
    }

    //Using sieving (Chinese remaineder Theorem)
    private static long getTimeSieving(List<Integer> list, List<Integer> sortedBusIds, Integer startId) {
        long increment = startId;
        long time = increment - list.indexOf(startId);

        for (int i = 1; i < sortedBusIds.size(); i++) {
            Integer busId = sortedBusIds.get(i);
            boolean match = false;
            while (!match) {
                time += increment;
                match = ((time + list.indexOf(busId)) % busId) == 0;
            }
            increment *= busId;
        }
        return time;
    }

    //Kind of brute force, only incrementing with the highest bus-id.
    private static long getTime(List<Integer> list, Integer max, int start) {
        long time = start;
        boolean match = false;
        while (!match) {
            time+= max;
            match = isMatch(list, time);
        }
        return time;
    }

    private static boolean isMatch(List<Integer> list, long time) {
        for (int i = 0; i < list.size(); i++) {
            Integer integer = list.get(i);
            if (integer == null) {
                continue;
            }
            if ((time + i) % integer != 0) {
                return false;
            }
        }
        return true;
    }

    private static List<Integer> getIntegers(String[] busIds) {
        List<Integer> list = new ArrayList<>();
        for (String busId : busIds) {
            if (busId.equals("x")) {
                list.add(null);
            } else {
                list.add(Integer.parseInt(busId));
            }
        }
        return list;
    }

    private static int part_1(int arrival, String[] busIds) {
        int res = 0;
        int minWait = -1;
        for (String busId : busIds) {
            if (busId.equals("x")) {
                continue;
            }
            int interval = Integer.parseInt(busId);
            int nextDepart = ((arrival / interval) +1) * interval;
            int wait = nextDepart - arrival;
            if (minWait < 0 || wait < minWait) {
                minWait = wait;
                res = interval * wait;
            }

        }
        return res;
    }

    private static List<Long> parse(Collection<String> inputLines) {
        return null;
    }
}
