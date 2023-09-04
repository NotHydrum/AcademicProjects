public class Guiao3_3 {

    public static void main(String[] args) {

      int numero = 357;
      int a1 = numero % 10;
      int a2 = (numero/10) % 10;
      int a3 = (numero/10/10) % 10;
      int soma = a1 + a2 + a3;

      System.out.println("Soma: " + soma);

    }

}
