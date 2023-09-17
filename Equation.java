/* This is an equation class
 * It represents the equation needed to be found
 */
public class Equation {
  // Expression represents the expresson fo the euation without the answer
  private String expression;

  public Equation(String equation) {
    this.expression = equation;
  }

  // We also have getters and setter
  public String getExpression() {
    return this.expression;
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }

  /*
   * In this method we return the string which represents what color the Board
   * needs to change colros to
   */
  public String display(String other) {
    // We set the return string
    String returnString = "";
    // We loop through the other expression's length
    for (int index = 0; index < other.length(); index++) {
      // We get the char
      char c = other.charAt(index);
      // If it is an exact match, we add a G
      if (c == expression.charAt(index)) {
        returnString += "G";
      }
      // We add Y if it is in the wrong spot
      else if (expression.contains("" + c)) {
        returnString += "Y";
      }
      // We add B for black if it is not in the equation
      else {
        returnString += "B";
      }
    }
    // We return the return string
    return returnString;
  }
}