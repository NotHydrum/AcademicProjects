import java.util.Scanner;
public class Guiao7_2 {

  public static void main(String[] args) {
    System.out.print("Password para validar: ");
    Scanner sc = new Scanner(System.in);
    System.out.println(passwordValida(sc.nextLine()));
  }

  public static boolean passwordValida(String password) {
    StringBuilder sb = new StringBuilder(password);
    boolean isValid = true;
    if (sb.length() >= 8) {
      int charNumber = 0;
      int numberDigits = 0;
      do {
        if (Character.isLetter(sb.charAt(charNumber)) || Character.isDigit(sb.charAt(charNumber))) {
          if (Character.isDigit(sb.charAt(charNumber))) {
            numberDigits += 1;
          }
        }
        else {
          isValid = false;
        }
        charNumber += 1;
      } while (isValid && charNumber < sb.length());
      if (numberDigits < 2) {
        isValid = false;
      }
    }
    else {
      isValid = false;
    }
    return isValid;
  }

}
