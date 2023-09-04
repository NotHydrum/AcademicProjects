public class Guiao4_1 {

  public static void main(String[] args) {

    double peso = 68.0;
    double altura = 1.65;
    double imc = peso / (altura * altura);

    System.out.println("IMC: " + imc);

    if (imc < 15) {
      System.out.println("Muito abaixo do peso normal.");
    } else if (imc >= 15 && imc < 18.5) {
      System.out.println("Abaixo do peso normal.");
    } else if (imc >= 18.5 && imc < 25) {
      System.out.println("Peso normal.");
    } else if (imc >= 25 && imc < 30) {
      System.out.println("Acima do peso normal.");
    } else if (imc >= 30 && imc < 40) {
      System.out.println("Obeso.");
    } else {
      System.out.println("Muito Obeso.");
    }

  }

}
