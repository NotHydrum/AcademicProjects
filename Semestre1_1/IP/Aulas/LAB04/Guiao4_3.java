public class Guiao4_3 {

  public static void main(String[] args) {

    int n = 17;

    for (int l = 1; l <= n; l += 1) {
      System.out.print(3 * l + " ");
    }
    System.out.println();

    for (int l = 0; l <= (n - 1); l += 1) {
      System.out.print(2 * l + 1 + " ");
    }
    System.out.println();

    long pot10 = 1;
    for (int l = 1; l <= n; l += 1) {
      pot10 *= 10;
      System.out.print(pot10 + " ");
    }
    System.out.println();

  }

}
