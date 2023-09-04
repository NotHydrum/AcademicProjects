public class Guiao5_1_4 {

  public static void main(String[] args) {
    int n = 5;
    for (int i = 1; i <= n; i += 1) {
      for (int j = 1; j <= n; j += 1) {
        if (i % j == 0 || j % i == 0) {
          System.out.print("*");
        }
        else {
          System.out.print(" ");
        }
      }
      System.out.println(i);
    }
  }

}
