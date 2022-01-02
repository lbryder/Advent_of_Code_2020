package problems;

import helpers.Helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day_10 {

    private static Map<Long, Long> possibilities = new HashMap<>();

    public static long problem(String path) {
        Collection<String> inputLines = Helper.readFile(path);
        List<Long> adapters = parse(inputLines);
        adapters.add(0L);
        adapters.sort(Comparator.naturalOrder());
        int tolerance = 3;
        int builtInt = 3;
        int result = problemOne(adapters, tolerance, builtInt);

        Map<Long, Set<Long>> adapterMatches = groupAdaptersInMatches(adapters, tolerance, builtInt);
        populatePossibilites(adapters, adapterMatches);
        Long first = adapters.get(0);


        long total = possibilities.get(first);
//226775649501184
        return total;

    }

    private static void populatePossibilites(List<Long> adapters, Map<Long, Set<Long>> adapterMatches) {
        adapters.stream().sorted(Comparator.reverseOrder()).forEach(v -> calcPossibilities(v, adapterMatches));
    }

    private static Map<Long, Set<Long>> groupAdaptersInMatches(List<Long> adapters, int tolerance, int builtInt) {
        Map<Long, Set<Long>> adapterMatches = new HashMap<>();
        for (Long adapter : adapters) {
            Set<Long> matchingAdapters = adapters.stream()
                    .filter(match -> match >= adapter && match <= adapter + tolerance && !adapter.equals(match))
                    .collect(Collectors.toSet());
            if (matchingAdapters.isEmpty()) {
                matchingAdapters = Collections.singleton(adapter + builtInt);
                possibilities.put(adapter + builtInt, 1L);
            }
            adapterMatches.put(adapter, matchingAdapters);
        }
        return adapterMatches;
    }

    private static void calcPossibilities(Long key, Map<Long, Set<Long>> adapterMatches) {
        long l = adapterMatches.get(key).stream()
                .map(ne -> possibilities.get(ne))
                .reduce(Long::sum).orElse(0L);
        possibilities.putIfAbsent(key, l);
    }

    private static int problemOne(List<Long> adapters, int tolerance, int builtInt) {
        List<Long> joltDiffs = new ArrayList<>();
        Long currentJolt = 0L;
        for (Long adapter : adapters) {
            if (currentJolt <= adapter && (currentJolt + tolerance) >= adapter) {
                //can be used
                Long e = adapter - currentJolt;
                joltDiffs.add(e);
                currentJolt = adapter;
            }
        }
        joltDiffs.add(3L);
        Map<Long, List<Long>> collect = joltDiffs.stream().collect(Collectors.groupingBy(Function.identity()));
        int result = collect.getOrDefault(1L, new ArrayList<>()).size() * collect.getOrDefault(3L, new ArrayList<>()).size();
        return result;
    }

    private static List<Long> parse(Collection<String> inputLines) {
        List<Long> list = new ArrayList<>();
        for (String inputLine : inputLines) {
            Long parseLong = Long.parseLong(inputLine);
            list.add(parseLong);
        }
        return list;
    }

}
