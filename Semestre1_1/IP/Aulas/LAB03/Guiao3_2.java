public class Guiao3_2 {

    public static void main(String[] args) {

      int ano = 200;
      boolean bissexto = (ano % 400 == 0 || (ano % 4 == 0 && ano % 100 != 0));
      int hora = 13;
      int minuto = 17;
      int segundo = 32;
      int tempo = hora * 60 * 60 + minuto * 60 + segundo;
      int diaSeg = 24 * 60 * 60;
      int diaPercent = (tempo * 100 / diaSeg);

      if (bissexto)
        System.out.println("O ano " + ano + " é bissexto.");
      else
        System.out.println("O ano " + ano + " não é bissexto.");
      System.out.println("Sao " + hora +"h " + minuto + "m " + segundo + "s. Passaram " + tempo + " segundos desde a meia-noite. Equivale a " + diaPercent + "% do dia.");
      System.out.println("Sao " + hora +"h " + minuto + "m " + segundo + "s. Faltam " + (diaSeg - tempo) + " segundos para a meia-noite.");

    }

}
