package by.bsu;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static by.bsu.Point.mod;
import static by.bsu.Point.p;
import static by.bsu.Point.sum;
import static java.util.stream.Collectors.toList;

public class Main {

    public static void main(String[] args) {
        System.out.println(getPoints(5, 3, 2));
        System.out.println(mod(-5, 29));
        System.out.println(sum(p(1, 1), p(2, 4), 5, 3));
    }

    private static List<Point> getPoints(int p, int a, int b) {
        return IntStream.range(0, p).boxed()
                .flatMap(x -> IntStream.range(0, p).boxed().map(y -> p(x, y)))
                .filter(point -> equation(point, p, a, b))
                .collect(toList());
    }

    private static boolean equation(Point point, int p, int a, int b) {
        return (point.getY() * point.getY() - point.getX() * point.getX() * point.getX() - a * point.getX() - b) % p == 0;
    }
}

class Point {
    private long x;
    private long y;

    private Point(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    static Point p(long x, long y) {
        return new Point(x, y);
    }

    static Point inverse(Point point, int p) {
        return p(point.x, mod(-1 * point.y, p));
    }

    static Point sum(Point point1, Point point2, int p, int a) {
        long m;
        if (!point1.equals(point2)) {
            m = (point1.y - point2.y) * inverse_element(point1.x - point2.x, p);
        } else {
            m = (3 * point1.x * point1.x + a) * inverse_element(2 * point1.y, p);
        }
        long x = mod(m * m - point1.x - point2.x, p);
        long y = mod(point1.y + m * (x - point1.x), p);
        return p(x, y);
    }

    static long mod(long a, long b) {
        long m = a % b;
        if (m < 0) {
            m = (b < 0) ? m - b : m + b;
        }
        return m;
    }

    private static long bin_pow(long base, long p, long mod) {
        if (p == 1) {
            return base;
        }

        if (p % 2 == 0) {
            long t = bin_pow(base, p / 2, mod);
            return t * t % mod;
        } else {
            return bin_pow(base, p - 1, mod) * base % mod;
        }
    }

    private static long inverse_element(long x, long mod) {
        return bin_pow(x, mod - 2, mod);
    }

    private static long divide(long a, long b, long mod) {
        return a * inverse_element(b, mod) % mod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "{" + x + ", " + y + '}';
    }
}