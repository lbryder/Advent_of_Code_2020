package problems;

import helpers.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day_4 {

    public static long problem(String path) {
        Map<String, Predicate<String>> requiredFields = new HashMap<>();
        Set<String> optionalFields = new HashSet<>();

        requiredFields.put("byr", Day_4::birthRule); // (Birth Year)
        requiredFields.put("iyr", Day_4::issueRule); // (Issue Year)
        requiredFields.put("eyr", Day_4::expirationRule); // (Expiration Year)
        requiredFields.put("hgt", Day_4::heightRule); // (Height)
        requiredFields.put("hcl", Day_4::hairRule); // (Hair Color)
        requiredFields.put("ecl", Day_4::eyeRule); // (Eye Color)
        requiredFields.put("pid", Day_4::passportIdRule); // (Passport ID)

        optionalFields.add("cid"); // (Country ID)

        Collection<String> lines = Helper.readFile(path);
        List<Passport> passports = parseAsPassports(lines);

        return validatePassports(requiredFields, passports);
    }

    private static boolean passportIdRule(String s) {

        return s.length() == 9 && s.matches("([0-9]){9}");
    }

    private static boolean eyeRule(String s) {
        Set<String> validEyes = new HashSet<>();
        validEyes.add("amb");
        validEyes.add("blu");
        validEyes.add("brn");
        validEyes.add("gry");
        validEyes.add("grn");
        validEyes.add("hzl");
        validEyes.add("oth");

        return validEyes.stream().anyMatch(s::equalsIgnoreCase);
    }

    private static boolean hairRule(String s) {
        if (s.isEmpty() || s.charAt(0) != '#') {
            return false;
        } else {
            String substring = s.substring(1);
            if (substring.length() != 6) {
                return false;
            }
            return substring.matches("([0-f]){6}");
        }
    }

    private static boolean heightRule(String s) {
        if (s.contains("cm")) {
            int i = Integer.parseInt(s.replace("cm", ""));
            return 150 <= i && i <= 193 ;
        } else if (s.contains("in")) {
            int i = Integer.parseInt(s.replace("in", ""));
            return 59 <= i && i <= 76 ;
        } else {
            return false;
        }
    }

    private static boolean expirationRule(String s) {
        int i = Integer.parseInt(s);
        return 2020 <= i && i <= 2030 ;
    }

    private static boolean issueRule(String s) {
        int i = Integer.parseInt(s);
        return 2010 <= i && i <= 2020 ;
    }

    private static boolean birthRule(String s) {
        int i = Integer.parseInt(s);
        return (1920 <= i && i <= 2002);
    }

    private static long validatePassports(Map<String, Predicate<String>> requiredFields, List<Passport> passports) {
        return passports.stream()
                .filter(p -> p.isValid(requiredFields))
                .count();
    }


    private static List<Passport> parseAsPassports(Collection<String> lines) {
        List<Passport> passportList = new ArrayList<>();
        Passport passport = new Passport();
        for (String line : lines) {
            if (!line.isEmpty()) {
                passport.addLine(line);
            } else {
                passportList.add(passport);
                passport = new Passport();
            }
        }
        passportList.add(passport);
        return passportList;
    }

    private static class Passport {
        Map<String, String> fields = new HashMap<>();

        public void addLine(String line) {
            String[] props = line.split(" ");
            Map<String, String> values = Arrays.stream(props).map(s -> s.split(":"))
                    .collect(Collectors.toMap(c -> c[0], c -> c[1]));
            fields.putAll(values);
        }

        public boolean hasField(String field) {
            return fields.containsKey(field);
        }

        public boolean isValid(Map<String, Predicate<String>> requiredFields) {
            return requiredFields.entrySet().stream().allMatch(e -> isValidField(e.getKey(), e.getValue()));
        }

        private boolean isValidField(String key, Predicate<String> value) {
            boolean hasField = this.hasField(key);
            if (!hasField) {
                return false;
            } else {
                String s = this.fields.get(key);
                boolean test = value.test(s);
                if (!test) {
//                    System.out.println("Failed: " + key + " Input: " + s);
                }
                return test;
            }
        }
    }
}
