/* This is my NerdleProject
Nerdle is similar to Wordle, but it uses numbers
In Nerdle, you enter an 8 character long equation
This is Main, our Driver class where we create the equation, set up the board and display the rules

*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// In our main method, we run the game
class Main {
  public static void main(String[] args) {
    runGame(true);
  }

  public static void runGame(boolean first) {
    // We call getEquation to get our equation
    String e = getEquation();
    Equation ans = new Equation(e);
    BoardGui board = new BoardGui(ans);
    // Here we display the rules if it is their first time
    if (first) {
      displayMenu(board);
    }
  }

  // In this method we display the Rules using a second frame
  public static void displayMenu(BoardGui board) {
    // we get our second frane
    JFrame outputFrame = board.getSecondFrame();
    // We create the string and add the rules to it
    String s = "This a Nerdle, a math version of Wordle.\nSimilar rules apply to Nerdle.\nBelow are the rules:\n";    

    s += "Each guess is a calculation.\n";
    s += "You can use 0 1 2 3 4 5 6 7 8 9 + - * / or =.\n";
    s += "It must contain one “=”.\n";
    s += "It must only have a number to the right of the “=”, not another calculation.\n";
    s += "Standard order of operations applies, so calculate * and / before + and -.\n";
    s += "If the answer we're looking for is 10+20=30, we will not accept 20+10=30 as the order is different.";
    s += "\nDifferent colors mean different things for each character.";
    s += "\nGreen means that the character is in the Nerdle and is in the right spot.";
    s += "\nYellow means that the character is in the Nerdle but is not in the right spot.";
    s += "\nGray means that the character is not in the Nerdle at all.";
    s += "\nYou have 6 guesses. Good luck!";
    // we now display the rules
    JOptionPane.showMessageDialog(outputFrame, s, "Instructions", JOptionPane.INFORMATION_MESSAGE);
  }

  
  // In this method we get the Equation
  public static String getEquation() {
    // We create a list of the numbers and opeators
    // We combine these to get a validChars string
    String nums = "0123456789";
    String ops = "-+/*";
    String validChars = nums + ops;
    // Here we generatea random number from 1-9 to start our equation with
    int r = (int) (Math.random() * 9) + 1;
    String equation = "" + r;
    // Here we loop through how many more chaacters until we put in our final numebr and then equal sign
    // The equation can either have a 3 digit, 2 digit or ne digit anwer 
    // So we pick a random number between 1 and 5 as
    // these are the options for our answer (5) digits outside of the first and last and equal to only one digit outside of that
    int loop = (int) (Math.random() * 5) + 1;
    for (int index = 1; index < loop; index ++) {
      // Now we picka  random index from our valid Chars and  get it along with getting the previous index
      int r2 = (int) (Math.random() * 14);
      String val = "" + validChars.charAt(r2);
      String previous = "" + equation.charAt(index - 1);
      // If we do not have 2 operators in a row
      if (!(ops.contains(val) && ops.contains(previous))) {
        // If the current char is a operator
        if (val.matches("\\*|/|-|\\+")) {
          // We add spaces before and after it to the equation so parsing the equation for the answer is easier
          equation += " " + val + " ";
          } 
          // If the char is a numebr
          else {
            // we add it without any extra spaces
            equation += val;
          } 
      } 
    }
    // We generate a random digit and add it to the equation
    int r3 = (int) (Math.random() * 10);
    equation += "" + r3;
    // We get the answer from our equation
    int ans = getAnswer(equation);
    // We remove all spaces 
    String eq2 = equation.replaceAll(" ", "");
    String returnString;
    // if there are no leading zeros "an operator and than a 0 in the string we continue"
    if (!opeatorAndZero(eq2)) {
      if (ans >= 0) {
        // We add the equals and the answer 
        returnString = eq2 + "=" + ans;
        // If the string is 8 charcters long (which means the random length loop worked, we return the string)
        if (returnString.length() == 8) {
          return returnString;
        } 
      }
    }
    // Else we will go again
    return getEquation();
  }

  // In this method, we add any spaces where we have operators to make the equation solvabekl
  public static String formatInput(String lhs) {
    // We use the regex replaceAll for operators
    lhs = lhs.replaceAll("\\+", " \\+ ");
    lhs = lhs.replaceAll("-", " - ");
    lhs = lhs.replaceAll("\\*", " \\* ");
    lhs = lhs.replaceAll("/", " / ");
    return lhs;
  }

  // In this method we chedk if there any operators which are followed by a zero and return true if there is 
  public static boolean opeatorAndZero(String equation) {
    ArrayList<String> ops = new ArrayList<String>();
    ops.add("*");
    ops.add("/");
    ops.add("-");
    ops.add("+");
    String first = "" + equation.charAt(0);
    // We loop through the equation except the first and last indexes
    for (int index = 1; index < equation.length() - 1; index++) {
      // We get the previous character and the current one
      String previous = "" + equation.charAt(index - 1);
      String current = "" + equation.charAt(index);
      // If the previous one is an operator and the current one is zero, we return true
      if (ops.contains(previous) && current.equals("0")) {
        return true;
      }
    }
    // If we get here we know that there is never any scenario where we have an operator and a zero
    return false; 
  }

  // in this method we get the answer of an expression
  public static int getAnswer(String equation) {
    // first we convert the string to an array list 
    ArrayList<String> eq = new ArrayList<String>();
    Scanner parser = new Scanner(equation);
    while(parser.hasNext()) {
      String token = parser.next();
      eq.add(token);
    }

    // We then surround our evaluating code with a try catch in case the input is not formed properly
    try {
      // We now loop threough the array list to check for multiplaction and division
      for (int index = 0; index < eq.size(); index++) {
        // We get the current value
        String e = eq.get(index);
        // we check for multiplication
        if (e.equals("*")) {
          // We use double to get rid of integer math (5/3 should not be equal to 1) 
          // We get the previous index and the next one
          double num1 = Double.parseDouble(eq.get(index - 1));
          double num2 = Double.parseDouble(eq.get(index + 1));
          // We the multiply them
          double sum = num1 * num2;
          // We remove the index ahead and the curent one while setting the previous index to the sum
          eq.remove(index + 1);
          eq.remove(index);
          eq.set(index - 1, "" + sum);
          // We then subtract one from the index to make sure we loop through all elements
          index--;
        }
        // We then do the same processfor division as for mltiplication
        if (e.equals("/")) {
          double num1 = Double.parseDouble(eq.get(index - 1));
          double num2 = Double.parseDouble(eq.get(index + 1));
          double sum = num1 / num2;
          eq.remove(index + 1);
          eq.remove(index);
          eq.set(index - 1, "" + sum);
          index--;
        }
      }
      // We now loop through a second time for additiona nd subtractona s they are lower down in the order of operations
      for (int index = 0; index < eq.size(); index++) {
        String e = eq.get(index);
        // We do the same thing for addition as multiplication
        if (e.equals("+")) {
          double num1 = Double.parseDouble(eq.get(index - 1));
          double num2 = Double.parseDouble(eq.get(index + 1));
          double sum = num1 + num2;
          eq.remove(index + 1);
          eq.remove(index);
          eq.set(index - 1, "" + sum);
          index--;
        }
        // We do the same thing for addition as multiplication
        if (e.equals("-")) {
          double num1 = Double.parseDouble(eq.get(index - 1));
          double num2 = Double.parseDouble(eq.get(index + 1));
          double sum = num1 - num2;
          eq.remove(index + 1);
          eq.remove(index);
          eq.set(index - 1, "" + sum);
          index--;
        }
      }
    }
    // If w have any errors,w e know we have bad input and return -1, which signals that something went wrong
    catch (NumberFormatException e) {
      return -1;
    }
    // We then check if the answer is an integer and does no have a decimal answer
    // If it is not an int, we return -1 to signal something went wrong
    double d = Double.parseDouble(eq.get(0));
    if ((d % 1) != 0) {
      return -1;
    } 
    // Else we make the double and int and return it while closing the parser
    else {
      int x = (int) d;
      parser.close();
      return x;
    }   
  }    
}
