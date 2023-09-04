/**
 * Number Search
 * @author Miguel Nunes - fc56338
 * @author Bruno Raimundo - fc56322
 */
public class NumberSearch {

  /**
   * Main procedure
   */
  public static void main(String[] args) {
    int numberDigits = 9;
    searchSequence(numberDigits, 198745334, 533);
    searchSequence(numberDigits, 198745334, 335);
    searchSequence(numberDigits, 198745334, 5334);
    searchSequence(numberDigits, 198745334, 121);
    searchSequence(numberDigits, 19874533, 335);
    searchSequence(numberDigits, 198745334, 305);
    searchSequence(numberDigits, 19874533, 305);
    System.out.println();
    printSequence(198745334, 6, 8);
    printSequence(198745334, 1, 5);
    printSequence(198745334, 8, 6);
    printSequence(198745334, 6, 12);
  }

  /**
   * Verifies if the given row and sequence are valid, and if they are both valid prints whether
   * or not the sequence is hidden in the row. If either of them isn't valid, prints out what isn't
   * valid.
   * @param numberDigits Number of digits row and sequence have to have to be considered valid.
   * @param row The row.
   * @param sequence The sequence.
   * @requires {@code numberDigits >= 1}
   */
  public static void searchSequence(int numberDigits,int row,int sequence) {
    boolean isValid = true;
    if (!isValidRow(row, numberDigits)) {
      System.out.print("The row " + row + " is not valid. ");
      isValid = false;
    }
    if (!isValidSequence(sequence, numberDigits)) {
      System.out.print("The sequence " + sequence + " is not valid. ");
      isValid = false;
    }
    if (isValid) {
      if (isSubsequence(sequence, row)) {
        System.out.println("The sequence " + sequence + " is hidden in row " + row + ".");
      }
      else {
        System.out.println("The sequence " + sequence + " is not hidden in row " + row + ".");
      }
    }
    else {
      System.out.println();
    }
  }

  /**
   * Verifies if the given 'from-to' range is valid, and if yes, prints the sequence of the row
   * within that range.
   * @param row The row.
   * @param from First position of the wanted range, including.
   * @param to Last position of the wanted range, including.
   * @requires {@code row > 0}
   */
  public static void printSequence(int row, int from,int to) {
    if (from >= 1 && to >= from && digits(row) >= to) {
      System.out.println("The sequence from position " + from + " to position " + to + " in row "
                         + row + " is " + subsequence(row, from, to) + ".");
    }
    else {
      System.out.println("The range from " + from + " to " + to + " is not valid in row " + row
                         + ".");
    }
  }

  /**
   * Returns the number of digits of a given number.
   * @param num The number.
   * @return The number of digits of the given number.
   * @requires {@code num > 0}
   * @ensures {@code \return >= 1}
   */
  public static int digits(int num) {
    int digitsNum = 1;
    while (num >= 10) {
      num /= 10;
      digitsNum += 1;
    }
    return digitsNum;
  }

  /**
   * Reverses the digits of a given number.
   * @param num The number.
   * @return The reverse of the given number.
   * @requires {@code num > 0}
   * @ensures {@code \return > 0}
   */
  public static int reverseDigits(int num) {
    int reverseNum = 0;
    while (num >= 10) {
      reverseNum += (num % 10);
      reverseNum *= 10;
      num /= 10;
    }
    reverseNum += num;
    return reverseNum;
  }

  /**
   * Determines if a given number (num1) is a subsquence of second given number (num2).
   * @param num1 Number which the function will determine is or isn't a subsequence of num2.
   * @param num2 Number.
   * @return Returns "true" if num1 is subsquence of num2, or "false" if not.
   * @requires {@code num1 > 0 && num2 > 0}
   * @ensures {@code /return == true || /return == false}
   */
  public static boolean isSubsequence(int num1, int num2) {
    int temporaryNum2 = num2;
    int digitsNum1 = digits(num1);
    int powerTen = 1;
    while (digitsNum1 > 0) {
      powerTen *= 10;
      digitsNum1 -= 1;
    }
    boolean isSub = false;
    while (!isSub && temporaryNum2 > 0) {
      if (num1 == (temporaryNum2 % powerTen)) {
        isSub = true;
      } else {
        temporaryNum2 /= 10;
      }
    }
    temporaryNum2 = num2;
    int reverseNum1 = reverseDigits(num1);
    int digReverseNum1 = digits(reverseNum1);
    powerTen = 1;
    while (digReverseNum1 > 0) {
      powerTen *= 10;
      digReverseNum1 -= 1;
    }
    while (!isSub && temporaryNum2 > 0) {
      if (reverseNum1 == (temporaryNum2 % powerTen)) {
        isSub = true;
      } else {
        temporaryNum2 /= 10;
      }
    }
    return isSub;
  }

  /**
   * Determines the subsequence of a given number within a given range.
   * @param num The number.
   * @param from First position of the wanted range, including.
   * @param to Last position of the wanted range, including.
   * @return The subsequence of the given number within the given range.
   * @requires {@code num > 0 && from >= 1 && to >= to && digits(num) >= to}
   * @ensures {@code /return >= 0 && /return <= num}
   */
  public static int subsequence(int num, int from, int to) {
    int digitsNum = digits(num);
    int powerTen = 1;
    while (digitsNum > to) {
      digitsNum -= 1;
      powerTen *= 10;
      num /= 10;
    }
    int wantedSub = 0;
    int powerTen2 = 1;
    while (digitsNum >= from) {
      wantedSub += ((num % 10) * powerTen2);
      digitsNum -= 1;
      powerTen *= 10;
      powerTen2 *= 10;
      num /= 10;
    }
    return wantedSub;
  }

  /**
   * Determines if a given row is valid. A valid row is positive, has the given number of digits,
   * and its digits cannot be zero.
   * @param num Row to validate.
   * @param numberDigits Number of digits row has to have to be considered valid.
   * @return Returns "true" if valid, "false" if not.
   * @requires {@code numberDigits >= 1}
   * @ensures {@code /return == true || /return == false}
   */
  public static boolean isValidRow(int num, int numberDigits) {
    boolean isValid;
    if (num > 0 && digits(num) == numberDigits) {
      isValid = isValidNumber(num);
    }
    else {
      isValid = false;
    }
    return isValid;
  }

  /**
   * Determines if a given sequence is valid. A valid sequence is positive, has the given number of
   * digits or lower, and its digits cannot be zero.
   * @param num Sequence to validate.
   * @param numberDigits Number of maximum digits sequence can to have to be considered valid.
   * @return Returns "true" if valid, "false" if not.
   * @requires {@code numberDigits >= 1}
   * @ensures {@code /return == true || /return == false}
   */
  public static boolean isValidSequence(int num, int numberDigits) {
    boolean isValid;
    if (num > 0 && digits(num) <= numberDigits) {
      isValid = isValidNumber(num);
    }
    else {
      isValid = false;
    }
    return isValid;
  }

  /**
   * Determines if a given number is valid. A valid number's digits cannot be zero.
   * @param num Number to validate.
   * @return Returns "true" if valid, "false" if not.
   * @requires {@code num >= 0}
   * @ensures {@code /return == true || /return == false}
   */
  private static boolean isValidNumber(int num) {
    boolean isValid = true;
    for (int digitsNum = digits(num); digitsNum > 0; digitsNum -= 1) {
      if (num % 10 == 0) {
        isValid = false;
        digitsNum = 0;
      }
      num /= 10;
    }
    return isValid;
  }

}
