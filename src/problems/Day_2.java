package problems;

import helpers.Helper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Day_2 {

    public static int problem(String path) {
        Collection<String> inputLines = Helper.readFile(path);
        Set<String> invalidPasswords = new HashSet<>();
        Set<String> validPasswords = new HashSet<>();
        for (String inputLine : inputLines) {
            ParsedPassword parsedPassword = new ParsedPassword(inputLine);


            String passWord = parsedPassword.passWord;
            boolean isValid = parsedPassword.validate_1();
            boolean isValid_2 = parsedPassword.validate_2();
            if (!isValid_2) {
                invalidPasswords.add(passWord);
            } else {
                validPasswords.add(passWord);
            }
        }

        return validPasswords.size();

    }


    private static class ParsedPassword {
        int minVal;
        int maxVal;
        char ruleChar;
        String passWord;

        public ParsedPassword(String inputLine) {
            String[] split = inputLine.split(":");
            String passRule = split[0];
            String[] passRuleSplit = passRule.split(" ");

            String minMax = passRuleSplit[0];
            String[] minMaxArray = minMax.split("-");

            ruleChar = passRuleSplit[1].charAt(0);
            minVal = Integer.parseInt(minMaxArray[0]);
            maxVal = Integer.parseInt(minMaxArray[1]);
            passWord = split[1].trim();
        }

        public boolean validate_1() {
            return validation_1(minVal, maxVal, ruleChar, passWord);
        }

        public boolean validate_2() {
            return validation_2(minVal, maxVal, ruleChar, passWord);
        }

        private boolean validation_1(int min, int max, char ruleChar, String passWord) {
            char charOne = passWord.charAt(min - 1);
            char charTwo = passWord.charAt(max - 1);
            boolean matchOne = charOne == ruleChar;
            boolean matchTwo = charTwo == ruleChar;
            return matchOne != matchTwo;
        }

        private boolean validation_2(int min, int max, char ruleChar, String passWord) {
            long appearances = passWord.chars().filter(c -> c == ruleChar).count();
            return min <= appearances && appearances <= max;
        }
    }
}
