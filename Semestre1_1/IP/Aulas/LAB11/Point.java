public class Point {

  private double x;
  private double y;

  public Point() {
    x = 0.0;
    y = 0.0;
  }

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  public Point copy() {
    Point p = new Point(x, y);
    return p;
  }

  public double distance(Point other) {
    double distance;
    distance = Math.hypot(other.getX() - x, other.getY() - y);
    return distance;
  }

  public void translate(double dx, double dy) {
    x += dx;
    y += dy;
  }

}
