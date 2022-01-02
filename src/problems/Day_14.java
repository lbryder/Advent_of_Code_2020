package problems;

import helpers.Helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Day_14 {

    public static long problem(String path) {
        List<String> inputLines = Helper.readFile(path);
        Map<Integer, Long> mem = new HashMap<>();
        Map<String, Integer> mem2 = new HashMap<>();


        String mask = "";
        for (String inputLine : inputLines) {
            if (inputLine.startsWith("mask")) {
                mask = parseMask(inputLine);
            } else {
                setMemory(inputLine, mask, mem);
                setMemoryVersion2(inputLine, mask, mem2);
            }
        }


        Long sumMaskedValues = mem.values().stream().reduce(Long::sum).orElse(0L);

        Long sum_version2 = sumMaskedAdresses(mem2);

        return sum_version2;
    }

    private static Long sumMaskedAdresses(Map<String, Integer> mem2) {
        return mem2.values().stream()
                .map(Integer::longValue)
                .reduce(Long::sum)
                .orElse(0L);
    }

    private static Collection<String> getAdresses(String adress) {
        int firstFloatingBit = adress.indexOf('X');

        if (firstFloatingBit > -1) {
            String adressWith_0 = adress.replaceFirst("X", "0");
            Collection<String> adresses_0 = getAdresses(adressWith_0);
            String adressWith_1 = adress.replaceFirst("X", "1");
            Collection<String> adresses_1 = getAdresses(adressWith_1);

            HashSet<String> adresses = new HashSet<>(adresses_0);
            adresses.addAll(adresses_1);
            return adresses;
        } else {
            return Collections.singleton(adress);
        }
    }

    private static Map.Entry<String, Integer> getEntryAtIndex(List<Map<String, Integer>> mem2, int i) {
        Map<String, Integer> mapAtIndex = mem2.get(i);
        Map.Entry<String, Integer> entry = mapAtIndex.entrySet().stream().findAny().orElse(null);
        return entry;
    }

    private static void setMemoryVersion2(String inputLine, String mask, Map<String, Integer> mem) {
        int adress = getAdress(inputLine);
        int value = getValue(inputLine);
        String adressMasked = applyMaskBase(adress, mask, '0');

        Collection<String> adresses = getAdresses(adressMasked);
        for (String adressToAdd : adresses) {
            mem.put(adressToAdd, value);
        }
    }

    private static void setMemory(String inputLine, String mask, Map<Integer, Long> mem) {
        int adress = getAdress(inputLine);
        int value = getValue(inputLine);
        long maskedValue = applyMask(value, mask);

        mem.put(adress, maskedValue);
    }

    private static int getValue(String inputLine) {
        int valueStart = inputLine.indexOf('=');
        String valueString = inputLine.substring(valueStart + 1).trim();
        return Integer.parseInt(valueString);
    }

    private static int getAdress(String inputLine) {
        int adressStart = inputLine.indexOf('[');
        int adressEnd = inputLine.indexOf(']');
        String adressString = inputLine.substring(adressStart + 1, adressEnd);

        return Integer.parseInt(adressString);
    }

    private static long applyMask(int value, String mask) {
        String maskedBinary = applyMaskBase(value, mask, 'X');
        long maskedValue = Long.parseLong(maskedBinary, 2);

        return maskedValue;
    }

    private static String applyMaskBase(int value, String mask, char neutralChar) {
        String paddedBinary = asPaddedBinary(value, 36);
        StringBuilder builder = new StringBuilder();
        char[] charArray = mask.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c == neutralChar) {
                builder.append(paddedBinary.charAt(i));
            } else {
                builder.append(c);
            }
        }

        String maskedBinary = builder.toString();
        return maskedBinary;
    }

    private static String asPaddedBinary(int value, int length) {
        String binValue = Integer.toBinaryString(value);
        String paddedBinary = String.format("%"+ length + "s", binValue).replace(" ", "0");
        return paddedBinary;
    }

    private static String parseMask(String inputLine) {
        int i = inputLine.indexOf('=');
        String mask = inputLine.substring(i + 2).trim();
        return mask;
    }

}
