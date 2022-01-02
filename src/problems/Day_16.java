package problems;

import helpers.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day_16 {

    public static long problem(String path) {
        List<String> inputLines = Helper.readFile(path);

        List<Rule> rules = getRules(inputLines);
        List<Integer> myTicket = getMyTicket(inputLines);
        List<List<Integer>> otherTickets = getOtherTickets(inputLines);
        List<List<Integer>> validTickets = otherTickets.stream()
                .filter(l -> isValidTicket(l, rules))
                .collect(Collectors.toList());

        Map<Rule, Integer> ruleOrder = deduceRuleOrders(rules, validTickets);
        Long product = ruleOrder.entrySet()
                .stream()
                .filter(e -> e.getKey().getName().contains("departure"))
                .map(Map.Entry::getValue)
                .map(myTicket::get)
                .map(Integer::longValue)
                .reduce((x, y) -> x * y)
                .orElse(0L);

//        List<Integer> errors = validateTickets(otherTickets, rules);
//        Integer errorSum = errors.stream().reduce(Integer::sum).orElse(0);

        return product;
    }

    private static Map<Rule, Integer> deduceRuleOrders(List<Rule> rules, List<List<Integer>> validTickets) {
        Map<Rule, List<Integer>> ruleOrders = new HashMap<>();
        for (Rule rule : rules) {
            List<Integer> possibleRuleOrders = deduceValidIndexes(rule, validTickets);
            ruleOrders.put(rule, possibleRuleOrders);
        }

        Map<Rule, Integer> ruleOrders2 = calculateUniqueOrder(ruleOrders);

        return ruleOrders2;
    }

    private static Map<Rule, Integer> calculateUniqueOrder(Map<Rule, List<Integer>> ruleOrders) {
        Map<Rule, Integer> result = new HashMap<>();
        List<Integer> remainingIndexes = ruleOrders.values().stream().flatMap(Collection::stream).distinct().collect(Collectors.toList());
        ArrayList<Map.Entry<Rule, List<Integer>>> entries = new ArrayList<>(ruleOrders.entrySet());
        while (!allEntriesMatched(result, entries)) {

            //Check if a rules index can now be uniquely determined.
            for (Map.Entry<Rule, List<Integer>> entry : entries) {
                List<Integer> possibleIndexes = entry.getValue();
                possibleIndexes.retainAll(remainingIndexes);
                if (possibleIndexes.size() == 1) {
                    Integer matchedIndex = possibleIndexes.get(0);
                    result.put(entry.getKey(), matchedIndex);
                } else {
                    possibleIndexes.retainAll(remainingIndexes);
                }
            }

            remainingIndexes.removeAll(result.values());
            entries.removeIf(e -> result.containsKey(e.getKey()));
        }

        return result;
    }

    private static boolean allEntriesMatched(Map<Rule, Integer> result, ArrayList<Map.Entry<Rule, List<Integer>>> entries) {
        return entries.stream().allMatch(e -> result.containsKey(e.getKey()));
    }

    private static List<Integer> deduceValidIndexes(Rule rule, List<List<Integer>> validTickets) {
        List<Integer> possibleOrder = IntStream.range(0, validTickets.get(0).size()).boxed().collect(Collectors.toList());
        for (List<Integer> validTicket : validTickets) {
            List<Integer> validIndexes = findValidIndexes(validTicket, rule);
            possibleOrder.retainAll(validIndexes);
        }

        return possibleOrder;
    }

    private static List<Integer> findValidIndexes(List<Integer> validTicket, Rule rule) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < validTicket.size(); i++) {
            Integer number = validTicket.get(i);
            if (rule.isValid(number)) {
                result.add(i);
            }
        }

        return result;
    }

    private static List<Integer> validateTickets(List<List<Integer>> tickets, List<Rule> rules) {
        List<Integer> invalids = new ArrayList<>();
        for (List<Integer> ticket : tickets) {
            for (Integer number : ticket) {
                boolean valid = matchesAnyRule(rules, number);
                if (!valid) {
                    invalids.add(number);
                }
            }
        }
        return invalids;
    }

    private static boolean isValidTicket(List<Integer> ticket, List<Rule> rules) {
        return ticket.stream().allMatch(i -> matchesAnyRule(rules, i));
    }

    private static boolean matchesAnyRule(List<Rule> rules, Integer number) {
        return rules.stream().anyMatch(r -> r.isValid(number));
    }

    private static List<List<Integer>> getOtherTickets(List<String> inputLines) {
        boolean otherTicketsReached = false;
        List<List<Integer>> otherTickets = new ArrayList<>();
        for (String inputLine : inputLines) {
            if (inputLine.trim().equals("nearby tickets:")) {
                otherTicketsReached = true;
            } else if (otherTicketsReached) {
                otherTickets.add(parseTicket(inputLine));
            }
        }
        return otherTickets;

    }

    private static List<Integer> parseTicket(String inputLine) {
        return Arrays.stream(inputLine.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private static List<Integer> getMyTicket(List<String> inputLines) {
        for (int i = 0; i < inputLines.size(); i++) {
            String inputLine = inputLines.get(i);
            if (inputLine.trim().equals("your ticket:")) {
                return parseTicket(inputLines.get(i + 1));
            }
        }
        return new ArrayList<>();

    }

    private static List<Rule> getRules(List<String> inputLines) {
        List<Rule> rules = new ArrayList<>();
        for (String inputLine : inputLines) {
            if (inputLine.isEmpty()) {
                return rules;
            }
            Rule rule = parseRule(inputLine);
            rules.add(rule);
        }
        return rules;
    }

    private static Rule parseRule(String inputLine) {
        String[] split = inputLine.split(":");
        String name = split[0];

        String[] intervals = split[1].split(" or ");
        String[] intervalLeft = intervals[0].split("-");
        String[] intervalRight = intervals[1].split("-");

        Interval interval = new Interval(intervalLeft);
        Interval interval2 = new Interval(intervalRight);

        return new Rule(name, Arrays.asList(interval, interval2));
    }


    private static class Interval {
        private final int min;
        private final int max;

        public Interval(String low, String high) {
            this.min = Integer.parseInt(low);
            this.max = Integer.parseInt(high);
        }

        public Interval(String[] intervalLeft) {
            this(intervalLeft[0].trim(), intervalLeft[1].trim());
        }

        public boolean inRange(int i) {
            return min <= i && i <= max;
        }
    }

    private static class Rule {
        public String getName() {
            return name;
        }

        private final String name;
        private final Collection<Interval> intervals;

        public Rule(String name, Collection<Interval> intervals) {
            this.name = name;
            this.intervals = intervals;
        }

        public boolean isValid(int i) {
            boolean valid = intervals.stream()
                    .anyMatch(interval -> interval.inRange(i));
            return valid;
        }

    }


}
