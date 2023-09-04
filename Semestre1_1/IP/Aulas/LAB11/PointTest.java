public class PointTest {

  public static void main(String[] args) {
    Point p1 = new Point();
    Point p2 = new Point(3.0, 7.0);
    System.out.println("p1 " + p1);
    System.out.println("p2 " + p2);
    System.out.println("Distância de p1 a p2 = " + p1.distance(p2));
    Point p3 = p1.copy();
    p3.translate(5, 9);
    System.out.println("p1 " + p1);
    System.out.println("p2 " + p2);
    System.out.println("p3 " + p3);
  }

}
