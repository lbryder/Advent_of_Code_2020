package problems;

import helpers.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day_15 {

    public static long problem(String path) {
        List<String> inputLines = Helper.readFile(path);
        List<Integer> collect = Arrays.stream(inputLines.get(0).split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        final int turnToReturn = 30_000_000;
        List<Integer> calledNumbers = Arrays.asList(new Integer[turnToReturn]);
        int turn = 1;
        int call = -1;
        while (turn <= turnToReturn) {
            int lastCall = call;

            if (turn <= collect.size()) {
              call = collect.get(turn - 1);
            } else if (calledNumbers.get(call) != null) {
                call = turn - 1 - calledNumbers.get(call);
            } else {
                call = 0;
            }
            if (lastCall > -1) {
                calledNumbers.set(lastCall, turn - 1);
            }
            turn++;
        }

        return call;
    }

}
