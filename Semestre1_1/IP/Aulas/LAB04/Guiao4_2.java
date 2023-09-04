public class Guiao4_2 {

  public static void main(String[] args) {

    int teste1 = 16;
    int teste2 = 5;
    int teste3 = 19;
    int media = 0;

    if (teste1 <= teste2 && teste1 <= teste3) {
      media = (int)((teste2 + teste3) / 2.0 + 0.5);
    } else if (teste2 <= teste1 && teste2 <= teste3) {
      media = (int)((teste1 + teste3) / 2.0 + 0.5);
    } else if (teste3 <= teste1 && teste3 <= teste2) {
      media = (int)((teste1 + teste2) / 2.0 + 0.5);
    }

    System.out.println("Média: " + media);

  }

}
