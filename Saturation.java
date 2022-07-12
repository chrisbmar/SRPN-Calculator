/** Encapsulates the logic to check if an integer is within the saturation bounds. */
public class Saturation {
  private static final int MAX = Integer.MAX_VALUE;
  private static final int MIN = Integer.MIN_VALUE;

  /** Method that checks for saturation and returns the saturated or non-saturated integer.
  *
  * @param {Long} totalLong - value to check for saturation
  * @param {int} totalInt - non-saturated integer
  * @returns {int} 
  */
  public static int checkSaturation(Long totalLong, int totalInt){
    if (totalLong > MAX) {
      return MAX;
    } else if (totalLong < MIN) {
        return MIN;
    } else {
        return totalInt;
    }
  }
}