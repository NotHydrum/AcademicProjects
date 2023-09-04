public class Guiao5_2 {

  public static void main(String[] args) {
    int k = 17;
    int m = 7;
    int n = 5;
    int log = logInteiro(n, 2);
    System.out.println("Maior número natural que pode ser usado como expoente numa potencia de 2 sem exceder " + n + ": " + log);
    int min = 0;
    boolean endLoop = false;
    for (int i = 1; endLoop == false; i += 1) {
      if (factorial(i) > k) {
        min = i;
        endLoop = true;
      }
    }
    System.out.println("Menor número natural cujo fatorial é maior que " + k + ": " + min);
  }

  public static int logInteiro(int n, int base) {
    int log = 0;
    while (n >= base) {
      n /= base;
      log += 1;
    }
    return log;
  }

  public static int factorial(int n) {
    int fact = 1;
    for (int f = n; f > 0; f -= 1) {
      fact *= f;
    }
    return fact;
  }

}
