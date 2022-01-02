package problems;

import helpers.Helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Day_7 {
    public static long problem(String path) {
        Collection<String> inputLines = Helper.readFile(path);
        Collection<Bag> bags = parseBags(inputLines);
        Bag myBag = getbagOfColor(bags, "shiny gold");

//        Set<Bag> allContaining = getAllContainingBags(bags, myBag);
//        long count = bags.stream()
//                .filter(b -> b.containsBag(bags, myBag))
//                .count();
        int containingBags = getAllContainingBags(bags, myBag).size();
        return containingBags;

//        long bagsContained = getNumberOfContainedBags(bags, myBag);
//        return bagsContained;

    }

    private static Bag getbagOfColor(Collection<Bag> bags, String myColor) {
        return bags.stream()
                .filter(b -> b.isOfType(myColor))
                .findFirst().orElseThrow(RuntimeException::new);
    }

    private static List<Bag> parseBags(Collection<String> inputLines) {
        List<Bag> bags = new ArrayList<>();
        for (String inputLine : inputLines) {
            Bag bag = Bag.parse(inputLine);
            bags.add(bag);
        }

        return bags;
    }

    private static long getNumberOfContainedBags(Collection<Bag> bags, Bag myBag) {
        return myBag.numberOfContainedBags(bags);
    }

    private static Set<Bag> getAllContainingBags(Collection<Bag> bags, Bag myBag) {
        Set<Bag> allContaining = new HashSet<>();
        Set<Bag> containingBags = findContainingBags(bags, myBag, allContaining);
        return allContaining;
    }

    private static Set<Bag> findContainingBags(Collection<Bag> bags, Bag myBag, Set<Bag> allContaining) {
        Set<Bag> newContaining = findNewContainingBags(bags, myBag, allContaining);
        allContaining.addAll(newContaining);

        for (Bag bag : newContaining) {
            findContainingBags(bags, bag, allContaining);
        }

        return newContaining;
    }

    private static Set<Bag> findNewContainingBags(Collection<Bag> bags, Bag myBag, Set<Bag> allContaining) {
        Set<Bag> containing = bags.stream()
                .filter(b -> b.containsBag(myBag))
                .collect(Collectors.toSet());

        //return only new containing bags
        containing.removeAll(allContaining);
        Set<Bag> newContaining = containing;

        return newContaining;
    }



    private static class Bag {
        String color;
        Map<String, Integer> innerBags = new HashMap<>();

        public Bag(String color) {
            this.color = color;
        }

        public static Bag parse(String inputLine) {
            String[] split = inputLine.replace(" bags ", "")
                    .replace(" bag ", "")
                    .replace(".", "")
                    .split("contain");

            String bagColor = split[0].replace("bags", "").trim();
            Bag bag = new Bag(bagColor);

            String[] innerBags = split[1].split(",");
            for (String innerBag : innerBags) {
                addInnerBag(bag, innerBag.trim());
            }

            return bag;
        }

        private static void addInnerBag(Bag bag, String innerBag) {
            if (innerBag.startsWith("no")) {
                return;
            }
            String[] split = innerBag.split(" ");
            int numberIn = Integer.parseInt(split[0]);
            String bagType = split[1] + " " + split[2];
            bag.addInner(bagType, numberIn);
        }

        private void addInner(String bag, int numberIn) {
            this.innerBags.put(bag, numberIn);
        }

        public boolean containsBag(Collection<Bag> bags, Bag myBag) {
            for (String innerBagColor : innerBags.keySet()) {
                if (innerBagColor.equals(myBag.color)) {
                    return true;
                }
                Bag innerBag = getbagOfColor(bags, innerBagColor);
                if (innerBag.containsBag(bags, myBag)) {
                    return true;
                }
            }
            return false;
        }

        public boolean containsBag(Bag bag) {
            return innerBags.containsKey(bag.color);
        }

        public boolean isOfType(String bagType) {
            return this.color.equals(bagType);
        }

        public long numberOfContainedBags(Collection<Bag> allBags) {
            long total = 0;
            for (Map.Entry<String, Integer> entry : innerBags.entrySet()) {
                String bagType = entry.getKey();
                Integer amountContained = entry.getValue();

                Long bagsContainedInInner = allBags.stream().filter(b -> b.isOfType(bagType)).findFirst()
                        .map(b -> b.numberOfContainedBags(allBags))
                        .orElse(0L);

                //Count both the bags further inside bags, and the outer bags in this.
                long bagsInInnerBag = bagsContainedInInner * amountContained;
                total += bagsInInnerBag + amountContained;
            }
            return total;
        }

    }
}
