package problems;

import helpers.Helper;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class Day_18 {

    static Map<Character, Integer> allowedOperators = Map.of('*', 2, '+', 3);

    public static long problem(String path) {
        List<String> inputLines = Helper.readFile(path);
        long res = 0L;
        for (String inputLine : inputLines) {
            long i = calculate(inputLine);
            res += i;
        }
        return res;
    }

    private static long calculate(String inputLine) {
        Queue<String> output = convertToRPN(inputLine);
        return calculateOutput(output);
    }

    private static Queue<String> convertToRPN(String inputLine) {
        String chars = "";
        Queue<String> output = new ArrayDeque<>();
        Stack<Character> ops = new Stack<>();

        char[] charArray = inputLine.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c == ' ' || c == ')') {
                //Do nothing
            } else if (allowedOperators.containsKey(c)) {
                //Add the number build until now, to the output.
                output.add(chars);
                chars = "";
                //Handle the operatir
                Integer precedence = allowedOperators.get(c);
                while (shouldPopTopOperator(ops, precedence)) {
                    Character pop = ops.pop();
                    output.add(pop.toString());
                }
                ops.push(c);
            } else if (c == '(') {
                //Calculate the inner of the parenthesis. Could also just be added to the RPN, but..
                String subFormula = getSubFormula(inputLine, i);
                i += subFormula.length()+1;
                chars = calculateSubFormula(subFormula);
            } else {
                //digit
                chars += c;
            }
        }
        output.add(chars);

        while (!ops.isEmpty()) {
            output.add(ops.pop().toString());
        }
        return output;
    }

    private static long calculateOutput(Queue<String> output) {
        Stack<Long> numbers = new Stack<>();
        while (!output.isEmpty()) {
            long x;
            String pop = output.poll();
            if (pop.equals("+")) {
                x = numbers.pop() + numbers.pop();
            } else if (pop.equals("*")) {
                x = numbers.pop() * numbers.pop();
            } else {
                x = Long.parseLong(pop);
            }
            numbers.push(x);
        }
        return numbers.pop();
    }

    private static String calculateSubFormula(String subFormula) {
        long y = calculate(subFormula);
        return Long.valueOf(y).toString();
    }

    private static boolean shouldPopTopOperator(Stack<Character> ops, Integer precedence) {
        return !ops.isEmpty() && allowedOperators.get(ops.peek()) >= precedence;
    }

    private static String getSubFormula(String inputLine, int i) {
        int startIndex = i + 1;
        int endIndex = endOfParenthesis(inputLine, startIndex);
        return inputLine.substring(startIndex, endIndex);
    }

    private static int endOfParenthesis(String inputLine, int indexFrom) {
        String substring = inputLine.substring(indexFrom);
        int depth = 0;
        char[] charArray = substring.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                if (depth == 0) {
                    return i + indexFrom;
                } else {
                    depth--;
                }
            }
        }
        return 0;
    }


}
