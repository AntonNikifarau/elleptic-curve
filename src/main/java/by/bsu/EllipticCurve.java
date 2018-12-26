package by.bsu;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static by.bsu.Point.p;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.LongStream.range;
import static java.util.stream.Stream.concat;

class EllipticCurve {
    private long a;
    private long b;
    private long mod;

    EllipticCurve(long mod, long a, long b) {
        this.a = a;
        this.b = b;
        this.mod = mod;
    }

    static long mod(long a, long b) {
        long m = a % b;
        if (m < 0) {
            m = (b < 0) ? m - b : m + b;
        }
        return m;
    }

    long bin_pow(long base, long p) {
        if (p == 1) {
            return base;
        }

        if (mod(p, 2) == 0L) {
            long t = bin_pow(base, p / 2);
            return mod(t * t, mod);
        } else {
            return mod(bin_pow(base, p - 1) * base, mod);
        }
    }

    long inverse(long x) {
        return bin_pow(x, mod - 2);
    }

    Point sum(Point p1, Point p2) {

        if(p1 == null) {
            return p2;
        }
        if(p2 == null) {
            return p1;
        }

        long x1 = p1.getX();
        long y1 = p1.getY();
        long x2 = p2.getX();
        long y2 = p2.getY();
        long m;

        if (x1 != x2) {
            m = (y1 - y2) * inverse(x1 - x2);
        } else {
            if (y1 == 0 && y2 == 0) {
                // This may only happen if p1 = p2 is a root of the elliptic
                // curve, hence the line is vertical.
                return null;
            } else if (y1 == y2) {
                // The points are the same, but the line is not vertical.
                m = (3 * x1 * x1 + a) * inverse(2 * y1);
            } else {
                // The points are not the same and the line is vertical.
                return null;
            }
        }

        long x3 = mod((m * m - x1 - x2) , mod);
        long y3 = mod(m * (x1 - x3) - y1, mod);

        return p(x3, y3);
    }

    long order(Point p) {
        long o = 1;
        Point temp = p;
        while (temp != null) {
            ++o;
            temp = sum(p, temp);
        }
        return o;
    }

    private boolean equation(long x, long y) {
        return mod(x * x * x + a * x + b - y * y, mod) == 0;
    }

    public List<Point> getPoints() {
        return range(0L, mod).boxed()
                .flatMap(x -> range(0L, mod).boxed().map(y -> p(x, y)))
                .filter(point -> equation(point.getX(), point.getY()))
                .collect(toList());
    }

    private long equation(long x) {
        return mod(x * x * x + a * x + b, mod);
    }

    // linear time
    public List<Point> getPointsNew() {
        // sqrt returns double
        Map<Long, Long> squares = range(0L, mod).boxed().collect(toMap(l -> l * l, identity()));

        //handle x=0
        Stream<Point> points = squares.entrySet().stream()
                .filter(e -> b == mod(e.getKey(), mod))
                .map(e -> p(0, e.getValue()));

        return concat(points, range(0L, mod).boxed()
                .flatMap(x -> {
                    long y_2 = equation(x);
                    if (squares.containsKey(y_2)) {
                        long y1 = squares.get(y_2);
                        long y2 = mod - y1;
                        return Stream.of(p(x, y1), p(x, y2)).distinct();
                    }
                    return Stream.empty();
                })).collect(toList());
    }
}
