package problems;

import helpers.Helper;

import java.util.Collection;
import java.util.Set;

public class Day_1 {
    public static int problemOne(Collection<String> lines, int target) {
        Set<Integer> numberSet = Helper.toInts(lines);
        for (int inputNumber : numberSet) {
            int match = target - inputNumber;
            boolean matchExists = numberSet.contains(match);
            if (matchExists) {
                return match * inputNumber;
            }
        }
        return 0;
    }

    public static int problemTwo(Collection<String> lines) {
        Set<Integer> integers = Helper.toInts(lines);
        for (Integer input : integers) {
            int target = 2020 - input;
            int partProduct = problemOne(lines, target);
            boolean matchFound = partProduct != 0;
            if (matchFound) {
                return partProduct * input;
            }
        }
        return 0;
    }
}
