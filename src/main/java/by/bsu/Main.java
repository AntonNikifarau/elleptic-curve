package by.bsu;

import java.util.List;
import java.util.Objects;

import static by.bsu.Point.p;
import static java.util.stream.Collectors.toList;
import static java.util.stream.LongStream.range;

public class Main {

    private static final long P = 5L;
    private static final long A = 3L;
    private static final long B = 2L;

    public static void main(String[] args) {
        EllipticCurve curve = new EllipticCurve(P, A, B);

        System.out.println(curve.getPoints());
        System.out.println(curve.multiply(p(1, 4), 5));
    }
}

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
            return base;    //Выход из рекурсии.
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

    long divide(long a, long b) {
        return mod(a * inverse(b), mod);
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

        long x3 = (m * m - x1 - x2) % mod;
        long y3 = (m * (x1 - x3) - y1) % mod;

        if (x3 < 0) {
            x3 += mod;
        }
        if (y3 < 0) {
            y3 += mod;
        }

        return p(x3, y3);
    }

    Point negPoint(Point p) {
        return p(p.getX(), mod - p.getY());
    }

    Point multiply(Point p, int n) {
        if (n == 0 || p == null) {
            return null;
        }

        if (n < 0) {
            n = -n;
            p = this.negPoint(p);
        }

        Point q = null;

        while (n != 0) {
            if ((n & 1) != 0) {
                q = sum(p, q);
            }

            p = sum(p, p);
            n >>= 1;
        }

        return q;
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
}

class Point {
    private long x;
    private long y;

    private Point(long x, long y) {
        this.x = x;
        this.y = y;
    }

    static Point p(long x, long y) {
        return new Point(x, y);
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
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