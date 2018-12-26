package by.bsu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.Integer.valueOf;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class Main {

    public static void main(String[] args) throws IOException {
        String DELIMITER = " ";
        String testFile = "test.txt";
        String resultFile = "result.txt";
        if (args.length > 0) {
            testFile = args[0];
        }

        List<String> results = Files.lines(Paths.get(testFile)).map(str -> {
            String[] values = str.split(DELIMITER);
            if (values.length == 3) {
                try {
                    Integer p = valueOf(values[0]);
                    Integer a = valueOf(values[1]);
                    Integer b = valueOf(values[2]);

                    EllipticCurve curve = new EllipticCurve(p, a, b);

                    List<String> points = curve.getPoints().stream()
                            .map(point -> point + " " + curve.order(point))
                            .collect(toList());

                    List<String> result = new ArrayList<>();
                    result.add("O 1");
                    result.addAll(points);
                    return result;

                } catch (NumberFormatException e) {
                    return singletonList("Invalid data: " + str);
                }
            } else {
                return singletonList("Invalid data: " + str);
            }
        }).flatMap(Collection::stream)
                .peek(System.out::println)
                .collect(toList());

        Files.write(Paths.get(resultFile), results);
    }
}
