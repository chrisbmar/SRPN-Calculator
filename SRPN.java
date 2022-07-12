import java.util.HashSet;
import java.util.Stack;
import java.lang.Math;

/**
 * Principles of Programming Coursework 1: SRPN.
 */

public class SRPN {
  /**
   * Enum for storing and accessing an error message at a given key.
   * 
   * @enum {String}
   */
  private static enum errorMessages {
  EMPTY("Stack empty."), UNDERFLOW("Stack underflow."), OVERFLOW("Stack overflow."),
  UNRECOGNISED("Unrecognised operator or operand"), ZERO("Divide by 0.");

    public final String label;

    private errorMessages(String label) {
      this.label = label;
    }

    public String toString() { // public method that returns the value corresponding to the given key in the enum
      return this.label;
    }
  }

  private static HashSet<String> operatorsSet = new HashSet<String>();

  static {
    // statically add all necessary operators to the operatorsSet
    operatorsSet.add("+");
    operatorsSet.add("/");
    operatorsSet.add("-");
    operatorsSet.add("*");
    operatorsSet.add("^");
    operatorsSet.add("%");
  }

  private static Stack<Integer> stack = new Stack<Integer>();

  private static int asciiSpaceValue = 32; // ascii value for the space character
  private static boolean isCommenting = false; // global var that can be switched true or false from each loop iteration

  public void processCommand(String s) {
    if (s.length() != 0) {
      // block scoped variable to temp. store the number while looping over the input
      String tmpNumber = "";

      for (int i = 0; i < s.length(); i++) { // loop through input string checking each character
        char currentChar = s.charAt(i);
        String currentCharAsString = Character.toString(currentChar); // convert char to string to avoid having to add or change constructors for existing methods

        boolean isSpace = (int) currentChar == asciiSpaceValue;
        boolean shouldProcessNumber = !tmpNumber.isEmpty() && currentChar != '-'
            && (isSpace || currentChar == '#' || operatorsSet.contains(currentCharAsString) || currentChar == '=');

        if (isSpace) { // handle a single space
          if (shouldProcessNumber) {
            processSingleCommand(tmpNumber);
            tmpNumber = "";
          }
          continue;
        }

        if (currentChar == '#') { // start and stop commenting mode
          isCommenting = !isCommenting;
          continue;
        }

        if (isCommenting) { // if currently commenting, continue
          continue;
        }

        boolean isNegativeNum = isNegativeNumber(s, i);
        boolean isFirstIteration = i == 0;
        boolean isFinalIteration = i == s.length() - 1;

        if (shouldProcessNumber) {
          processSingleCommand(tmpNumber);
          tmpNumber = "";
        }

        // if / else to catch all other cases
        if (currentChar == '-') {
          if (isNegativeNum) {
            tmpNumber = tmpNumber + '-'; // update the tmpNumber
          } else { // fall into this block if '-' is an operand
            if (!tmpNumber.isEmpty()) {
              processSingleCommand(tmpNumber);
              tmpNumber = "";
            }
            processSingleCommand(currentCharAsString);
          }
        } else if (isValidNumber(currentCharAsString)) {
          tmpNumber = tmpNumber + currentCharAsString; // update the tmpNumber
          if (isFinalIteration) { // process remaining number
            processSingleCommand(tmpNumber);
            tmpNumber = "";
          }
        } else if (isValidOperator(currentCharAsString)) {
          if (!isFinalIteration && s.charAt(i + 1) == '=') {
            processSingleCommand("="); // process '=' before processing the operator
          }
          processSingleCommand(currentCharAsString); // process operator
        } else if (currentChar == '=') {
          if (!isFirstIteration && isValidOperator(Character.toString(s.charAt(i - 1)))) {
            continue; // if prev char is an operator, continue
          } else {
            processSingleCommand(currentCharAsString); // process the current char '='
          }
        } else {
          processSingleCommand(currentCharAsString);
        }
      }
    }
  }

  /**
   * Method that checks if the '-' character is the start of a negative number, or
   * an operand.
   * 
   * @returns {boolean} true if '-' is the start of a negative number, else false
   */
  private static boolean isNegativeNumber(String s, int index) {
    // if first char is '-' and next char is a valid number
    if (index == 0 && s.length() > 1 && isValidNumber(Character.toString(s.charAt(1)))) {
      return true;
    }

    // check if previous char is a space and next char is a valid number
    try {
      boolean isNeg = (int) s.charAt(index - 1) == asciiSpaceValue
          && isValidNumber(Character.toString(s.charAt(index + 1)));
    } catch (StringIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  private static void processSingleCommand(String s) {
    char firstChar = s.charAt(s.length() - 1);

    switch (firstChar) {
    case 'd':
      handleCaseD();
      break;
    case 'r':
      handleCaseR();
      break;
    case '=':
      handleCaseEquals();
      break;
    default:
      if (isValidNumber(s)) {
        handleCaseNumber(s);
      } else if (isValidOperator(s)) {
        handleCaseOperator(s);
      } else {
        // catch-all: format and print an error message for unrecgonised value
        String unrecognisedValue = formatUnrecognisedValue(s);
        System.out.println(unrecognisedValue);
      }
    }
  }

  private static void handleCaseD() {
    if (stack.isEmpty()) {
      System.out.println(Integer.MIN_VALUE);
    } else {
      for (int item : stack) {
        System.out.println(item);
      }
    }
  }

  private static void handleCaseR() {
    if (isStackOverflow()) {
      System.out.println(errorMessages.OVERFLOW.toString());
    } else {
      int random = RandomNumber.getRandomNumber();
      stack.push(random);
    }
  }

  private static void handleCaseEquals() {
    if (stack.isEmpty()) {
      System.out.println(errorMessages.EMPTY.toString());
    } else {
      System.out.println(stack.peek());
    }
  }

  private static void handleCaseNumber(String s) {
    if (isStackOverflow()) {
      System.out.println(errorMessages.OVERFLOW.toString());
    } else {
      stack.push(Integer.parseInt(s));
    }
  }

  private static void handleCaseOperator(String s) {
    if (isStackUnderflow()) {
      System.out.println(errorMessages.UNDERFLOW.toString());
    } else {
      performCalculations(s);
    }
  }

  /**
   * Method that checks whether the stack has reached it's limit of 23.
   * 
   * @returns {boolean} true if stack size is 23, else false
   */
  private static boolean isStackOverflow() {
    return stack.size() == 23;
  }

  /**
   * Method that checks for a minimum of 2 integers on the stack.
   * 
   * @returns {boolean} true if stack size is less than 2, else false
   */
  private static boolean isStackUnderflow() {
    return stack.size() < 2;
  }

  /**
   * Method that checks whether a given argument is a numerical string.
   *
   * @param {String} str
   * @returns {boolean} true if the argument is a numerical string, else false
   */
  private static boolean isValidNumber(String str) {
    try {
      int i = Integer.parseInt(str);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  /**
   * Method that formats an error message for an unrecognised value.
   *
   * @param {String} str
   * @returns {String}
   */
  private static String formatUnrecognisedValue(String str) {
    return String.format("%s \"%s\".", errorMessages.UNRECOGNISED.toString(), str);
  }

  /**
   * Method that checks whether a given argument is a valid operator.
   *
   * @param {String} str
   * @returns {boolean} true if the argument is an operator, else false
   */
  private static boolean isValidOperator(String str) {
    return operatorsSet.contains(str);
  }

  /**
   * Method that determines the correct operator, and converts {integer} to {Long}
   * to have a bigger size to handle saturation correctly.
   * 
   * integer: 4 bytes Long: 8 bytes
   *
   * @param {String} operator
   * @returns {void}
   */
  private static void performCalculations(String operator) {
    int operand1 = stack.pop();
    int operand2 = stack.pop();

    Long l1 = new Long(operand1);
    Long l2 = new Long(operand2);

    switch (operator) {
    case "+":
      stack.push(Saturation.checkSaturation(l2 + l1, operand2 + operand1));
      break;
    case "-":
      stack.push(Saturation.checkSaturation(l2 - l1, operand2 - operand1));
      break;
    case "*":
      stack.push(Saturation.checkSaturation(l2 * l1, operand2 * operand1));
      break;
    case "/":
      if (operand1 == 0) {
        System.out.println(errorMessages.ZERO.toString());
      } else {
        stack.push(Saturation.checkSaturation(l2 / l1, operand2 / operand1));
      }
      break;
    case "^":
      double value = Math.pow(operand2, operand1);
      stack.push(Saturation.checkSaturation((long) value, (int) value));
      break;
    case "%":
      stack.push(Saturation.checkSaturation(l2 % l1, operand2 % operand1));
      break;
    }
  }
}
