package problems;

import helpers.Helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Day_6 {
    public static long problem(String path) {
        Collection<String> inputLines = Helper.readFile(path);

        Collection<DeclarationGroup> groups = parseDeclarationGroups(inputLines);
        Integer maxAnswer = groups.stream()
                .map(DeclarationGroup::numberOfDistinctAnswers)
                .reduce(0, Integer::sum);

        Long unanimousAnswers = groups.stream()
                .map(DeclarationGroup::numberOfUnanimousAnswers)
                .reduce(0L, Long::sum);

        return unanimousAnswers;
    }

    private static Collection<DeclarationGroup> parseDeclarationGroups(Collection<String> inputLines) {
        Collection<DeclarationGroup> groups = new ArrayList<>();
        DeclarationGroup group = new DeclarationGroup();
        for (String inputLine : inputLines) {
            if (inputLine.isEmpty()) {
                groups.add(group);
                group = new DeclarationGroup();
            } else {
                group.addAnswers(inputLine);
            }
        }
        groups.add(group);
        return groups;
    }

    private static class DeclarationGroup {
        Set<Character> distinctAnswers = new HashSet<>();
        Set<Set<Character>> allAnswers = new HashSet<>();

        public void addAnswers(String inputLine) {
            HashSet<Character> answer = new HashSet<>();
            for (char c : inputLine.toCharArray()) {
                answer.add(c);
            }
            this.distinctAnswers.addAll(answer);
            allAnswers.add(answer);
        }

        public int numberOfDistinctAnswers() {
            return distinctAnswers.size();
        }

        public long numberOfUnanimousAnswers() {
            return distinctAnswers.stream()
                    .filter(this::isUnanimous)
                    .count();
        }

        private boolean isUnanimous(Character c) {
            return allAnswers.stream()
                    .allMatch(set -> set.contains(c));
        }
    }
}
