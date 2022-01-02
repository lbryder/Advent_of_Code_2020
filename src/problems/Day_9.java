package problems;

import helpers.Helper;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day_9 {
    public static long problem(String path) {
        Collection<String> inputLines = Helper.readFile(path);
        List<Long> inputNumber = parse(inputLines);
        int preambleLength = 25;
        long invalidInput = findInvalidInput(inputNumber, preambleLength);

        return calculateWeakness(inputNumber, invalidInput);
    }

    private static long calculateWeakness(List<Long> inputNumber, long invalidInput) {
        for (int i = 0; i < inputNumber.size(); i++) {
            int lastIndex = sumsToValue(i, inputNumber, invalidInput);
            if (lastIndex > 0) {
                List<Long> longs = inputNumber.subList(i, lastIndex + 1);
                long max = longs.stream().reduce(Long::max).orElse(0L);
                long min = longs.stream().reduce(Long::min).orElse(0L);
                return min + max;
            }
        }
        return -1;
    }

    private static int sumsToValue(int i, List<Long> inputNumber, long target) {
        long sum = 0;
        while (sum < target) {
            sum += inputNumber.get(i);
            i++;
        }
        return sum == target ? i - 1 : 0;
    }

    private static long findInvalidInput(List<Long> inputNumber, int preambleLength) {
        for (int index = preambleLength; index < inputNumber.size(); index++) {
            boolean valid = isValidNumber(index, inputNumber, preambleLength);
            if (!valid) {
                return inputNumber.get(index);
            }
        }
        return 0;
    }

    private static boolean isValidNumber(int index, List<Long> inputNumber, int preambleLength) {
        List<Long> preamble = inputNumber.subList(index - preambleLength, index);
        Long targetNumber = inputNumber.get(index);
        return isValid(targetNumber, preamble);
    }

    private static boolean isValid(Long targetNumber, List<Long> preamble) {
        for (Long aLong : preamble) {
            long needMatch = targetNumber - aLong;
            boolean containsMatch = preamble.contains(needMatch);
            if (needMatch != aLong && containsMatch) {
                return true;
            }
        }
        return false;
    }

    private static List<Long> parse(Collection<String> inputLines) {
        return inputLines.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

}
