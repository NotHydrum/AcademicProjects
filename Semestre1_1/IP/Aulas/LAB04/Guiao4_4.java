public class Guiao4_4 {

  public static void main(String[] args) {

    int n = 16;

    int fact = 1;
    for (int l = n; l > 0; l -= 1) {
      fact *= l;
    }
    System.out.println("Factorial: " + fact);

    System.out.print("Divisores: ");
    for (int l = 1; l <= n; l += 1) {
      if (n % l == 0) {
        System.out.print(l + " ");
      }
    }
    System.out.println();

    int numDiv = 0;
    for (int l = 1; l <= n; l += 1) {
      if (n % l == 0) {
        numDiv += 1;
      }
    }
    System.out.println("Número de Divisores: " + numDiv);

    int somaDiv = 0;
    for (int l = 1; l <= n; l += 1) {
      if (n % l == 0) {
        somaDiv += l;
      }
    }
    System.out.println("Soma dos Divisores: " + somaDiv);

  }

}
