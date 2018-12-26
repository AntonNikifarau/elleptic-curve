package by.bsu;

import java.util.Objects;

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
        return "(" + x + ", " + y + ')';
    }
}
