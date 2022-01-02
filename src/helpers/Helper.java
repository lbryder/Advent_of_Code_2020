package helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Helper {
    public static Set<Integer> toInts(Collection<String> lines) {
        return lines.stream()
                .map(Integer::new)
                .collect(Collectors.toSet());
    }

    public static List<String> readFile(String path) {
        try {
            File in = new File(path);
            FileReader in1 = new FileReader(in);
            BufferedReader reader = new BufferedReader(in1);

            List<String> lines = new ArrayList<>();
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            return lines;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
