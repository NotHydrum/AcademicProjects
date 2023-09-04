public class Guiao3_2_5 {

    public static void main(String[] args) {

      int diaSegundos = 23567;
      int horas = diaSegundos / 3600;
      int minutos = diaSegundos / 60 - horas * 60 ;
      int segundos = diaSegundos - minutos * 60 - horas * 3600;

      System.out.println("Sao " + horas +"h " + minutos + "m " + segundos + "s.");

    }

}
