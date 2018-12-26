package by.bsu;

public class Main {

    private static final long P = 11L;
    private static final long A = 4L;
    private static final long B = 3L;

    public static void main(String[] args) {
        EllipticCurve curve = new EllipticCurve(P, A, B);

        System.out.println("0 1");

        for (Point point : curve.getPointsNew()) {
            long order = curve.order(point);
            System.out.println(point + " " + order);
        }
    }
}
