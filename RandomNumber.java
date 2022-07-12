import java.util.ArrayList;

/** Imitates a random number generator. */
public class RandomNumber {
  private static int accessor = 0;

  private static ArrayList<Integer> randomNumbers = new ArrayList<Integer>();

  static {
    // Statically add 23 numbers as this is the max size the stack can be if 'r' is processed 23 times
    randomNumbers.add(1804289383);
    randomNumbers.add(846930886);
    randomNumbers.add(1681692777);
    randomNumbers.add(1714636915);
    randomNumbers.add(1957747793);
    randomNumbers.add(424238335);
    randomNumbers.add(719885386);
    randomNumbers.add(1649760492);
    randomNumbers.add(596516649);
    randomNumbers.add(1189641421);
    randomNumbers.add(1025202362);
    randomNumbers.add(1350490027);
    randomNumbers.add(783368690);
    randomNumbers.add(1102520059);
    randomNumbers.add(2044897763);
    randomNumbers.add(1967513926);
    randomNumbers.add(1365180540);
    randomNumbers.add(1540383426);
    randomNumbers.add(304089172);
    randomNumbers.add(1303455736);
    randomNumbers.add(35005211);
    randomNumbers.add(521595368);
    randomNumbers.add(1804289383);
  }

  /** Method that gets a number from the randomNumbers ArrayList at the current accessor.
  *
  * @returns {int} 
  */
  public static int getRandomNumber() {
    int randomNumber = randomNumbers.get(accessor);
    accessor++;
    return randomNumber;
  }
}