package problems;

import helpers.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day_19 {

    public static long problem(String path) {
        List<String> inputLines = Helper.readFile(path);
        Map<Integer, Rule> rules = parseRules(inputLines);
        int i = inputLines.indexOf("");
        List<String> input = inputLines.subList(i + 1, inputLines.size());

        Long aLong = countMatches(input, 0, rules);


        return aLong;
    }

    private static Long countMatches(List<String> input, int ruleIndex, Map<Integer, Rule> rules) {
        Rule rule = rules.get(ruleIndex);
        List<String> matchesRule = new ArrayList<>();
        for (String s : input) {
            boolean ok = rule.isOK(s, rules);
            if (ok) {
                matchesRule.add(s);
            }
        }
        return Long.valueOf(matchesRule.size());
    }

    private static Map<Integer, Rule> parseRules(List<String> inputLines) {
        Map<Integer, Rule> rules = new HashMap<>();
        boolean part_2 =
                false;
//                true;

        for (String inputLine : inputLines) {
            if (inputLine.equals("")) {
                break;
            } else if (part_2) {
                if (inputLine.startsWith("8:")) {
                    inputLine = "8: 42 | 42 8";
                } else if (inputLine.startsWith("11:")) {
                    inputLine = "11: 42 31 | 42 11 31";
                }
            }
            Rule rule = new Rule(inputLine);
            rules.put(rule.index, rule);
        }
        return rules;
    }

    private static class Rule {

        List<List<Integer>> ruleIndexes = new ArrayList<>();
        String rule;
        String match;
        int index;

        public Rule(String inputLine) {
            String[] split = inputLine.split(":");
            String index = split[0];

            String rule = split[1].trim();
            boolean simpleRule = rule.contains("\"");
            if (simpleRule) {
                this.match = rule.replaceAll("\"", "");
            } else {
                parseAbstractRule(rule);
            }

            this.index = Integer.parseInt(index);
        }

        private void parseAbstractRule(String rule) {
            String[] split3 = rule.split("\\|");

            this.ruleIndexes = Arrays.stream(split3)
                    .map(this::parseRuleCombo)
                    .collect(Collectors.toList());
        }

        private List<Integer> parseRuleCombo(String indexes_0) {
            String inString = indexes_0.trim();
            return Arrays.stream(inString.split(" "))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }

        public int matches(String s, Map<Integer, Rule> rules) {
            if (this.isSimpleRule()) {
                if (s.indexOf(this.match) == 0) {
                    return 0;
                }
                return -1;
            } else {
                for (List<Integer> ruleIndex : ruleIndexes) {
                    int match = isMatch(s, ruleIndex, rules);
                    if (match >= 0) {
                        return match;
                    }
                }
                return -1;
            }
        }

        private int isMatch(String s, List<Integer> ruleIndex, Map<Integer, Rule> rules) {
            List<Rule> conditionRules = getRules(ruleIndex, rules);

            String checkString = s;
            int matchedIndex = -1;
            for (Rule conditionRule : conditionRules) {
                int match = conditionRule.matches(checkString, rules);
                if (match < 0) {
                    return match;
                } else if (match + 1 < checkString.length()) {
                    checkString = checkString.substring(match + 1);
                }
                matchedIndex += match + 1;

            }

            return matchedIndex;
        }

        private List<Rule> getRules(List<Integer> ruleIndex, Map<Integer, Rule> rules) {
            List<Rule> conditionRules = new ArrayList<>();
            for (Integer index : ruleIndex) {
                Rule rule = rules.get(index);
                conditionRules.add(rule);
            }
            return conditionRules;
        }

        private boolean isSimpleRule() {
            return this.match != null;
        }

        public boolean isOK(String s, Map<Integer, Rule> rules) {
            int matches = matches(s, rules);
            return matches + 1 == s.length();
        }
    }
}
