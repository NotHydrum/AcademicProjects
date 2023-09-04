import java.util.Random;
import java.util.Arrays;
public class Guiao10_2 {

  static final int months = 12;
  static final int days = 31;
  static final int hours = 24;

  public static void main(String args[]) {
    int[][][] accidents = new int[months][days][hours];
    Random rd = new Random();
    for (int i = 0; i < months; i++) {
      for (int f = 0; f < days; f++) {
        for (int k = 0; k < hours; k++) {
          accidents[i][f][k] = rd.nextInt(15);
        }
      }
    }
    int[] dayWithMostDeaths = new int[2];
    dayWithMostDeaths = getDayWithMostDeaths(accidents);
    System.out.println(dayWithMostDeaths[0] + " " + dayWithMostDeaths[1]);
    System.out.println(getHourWithMostAccidents(accidents));
    deathsPerMonth(accidents);
  }

  public static int[] getDayWithMostDeaths(int[][][] accidents) {
    int accidentsInDay = -1;
    int[] dayWanted = {-1, -1};
    for (int i = 0; i < months; i++) {
      for (int f = 0; f < days; f++) {
        for (int k = 0; k < hours; k++) {
          if (accidents[i][f][k] > accidentsInDay) {
            accidentsInDay = accidents[i][f][k];
            dayWanted[0] = f + 1;
            dayWanted[1] = i + 1;
          }
        }
      }
    }
    return dayWanted;
  }

  public static int getHourWithMostAccidents(int[][][] accidents) {
    long[] accidentsPerHour = new long[24];
    Arrays.fill(accidentsPerHour, 0);
    for (int i = 0; i < months; i++) {
      for (int f = 0; f < days; f++) {
        for (int k = 0; k < hours; k++) {
          accidentsPerHour[k] += accidents[i][f][k];
        }
      }
    }
    int hourWanted = 0;
    for (int i = 1; i < 23; i++) {
      if (accidentsPerHour[i] > accidentsPerHour[i - 1]) {
        hourWanted = i;
      }
    }
    return hourWanted;
  }

  public static void deathsPerMonth(int[][][] accidents) {
    int deaths;
    for (int i = 0; i < accidents.length; i++) {
      deaths = 0;
      for (int f = 0; f < accidents[0].length; f++) {
        for (int k = 0; k < accidents[0][0].length; k++) {
          deaths += accidents[i][f][k];
        }
      }
      System.out.println((i + 1) + ": " + deaths);
    }
  }

}
