/* This is the BoardGui class which will be where the user puts in their guesses
We have a matrix of JTextFields for the user to enter their characters and buttons for them to submit their answer
We change the color of the JTextField depepning on how close they got
At the end of the game, we tell them whether they won or lost and ask them if they want to play again
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.font.*;
public class BoardGui implements ActionListener {

// we set up our needed fields for the gui
// We add the rows of JTextFields for our guesses
private JTextField[] first = new JTextField[8];
private JTextField[] second = new JTextField[8];
private JTextField[] third = new JTextField[8];
private JTextField[] fourth = new JTextField[8];
private JTextField[] fifth = new JTextField[8];
private JTextField[] sixth = new JTextField[8];
private JTextField[][] matrix = new JTextField[6][8];

// We add our frame for the matrix
private JFrame frame;
// We add our equation field
private Equation e;
// We add out guess buttons 
private JButton firstGuess;
private JButton secondGuess;
private JButton  thirdGuess;
private JButton  fourthGuess;
private JButton  fifthGuess;
private JButton  sixthGuess;
// We add our output frame
private JFrame frame2;


public BoardGui(Equation e) {
  // We set the equation
  this.e = e;
  // We set up the frame for the matrix of JTextFields
  frame = new JFrame();
  frame.setTitle("Nerdle");
  frame.setSize(800, 600);
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  // We set up or output frame
  frame2 = new JFrame();
  frame2.setSize(200, 100);
  frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  // We create JTextFields in our matrix
  for (int row = 0; row < 6; row++) {
    for (int col = 0; col < 8; col++) {
      matrix[row][col] = new JTextField();
      matrix[row][col].setHorizontalAlignment(JTextField.CENTER);
      matrix[row][col].setFont(Font.decode("Oxygen-BOLD-25"));
      matrix[row][col].setBorder(BorderFactory.createBevelBorder(0));
      // Second to sixth rows uneditable
      if (row > 0) {
        matrix[row][col].setEditable(false);
      }
    }
  }

  // We now set up our JButton
  firstGuess = new JButton();
  secondGuess = new JButton();
  thirdGuess = new JButton();
  fourthGuess = new JButton();
  fifthGuess = new JButton();
  sixthGuess = new JButton();
  firstGuess.setText("Enter first guess");
  firstGuess.addActionListener(this);
  secondGuess.setText("Enter second guess");
  secondGuess.addActionListener(this);
  thirdGuess.setText("Enter third guess");
  thirdGuess.addActionListener(this);
  fourthGuess.setText("Enter fourth guess");
  fourthGuess.addActionListener(this);
  fifthGuess.setText("Enter fifth guess");
  fifthGuess.addActionListener(this);
  sixthGuess.setText("Enter sixth guess");
  sixthGuess.addActionListener(this);

  // We set our row arrays from our matrix 
  first = matrix[0];
  second = matrix[1]; 
  third = matrix[2];
  fourth = matrix[3];
  fifth = matrix[4];
  sixth = matrix[5];

  // We set the frame layout
  frame.setLayout(new GridLayout(6,9));

  // We now add the JTextFields and use if statements to add our buttons at the right time
  for (int row = 0; row < 6; row++) {
    for (int col = 0; col < 8; col++) {
      frame.add(matrix[row][col]);
    }
    if (row == 0) {
      frame.add(firstGuess);
    }
    else if (row == 1) {
      frame.add(secondGuess);
    }
    else if (row == 2) {
      frame.add(thirdGuess);
    }
    else if (row == 3) {
      frame.add(fourthGuess);
    }
    else if (row == 4) {
      frame.add(fifthGuess);
    } else {
      frame.add(sixthGuess);
    }
  }
  // We set our frame's visiblity to true
  frame.setVisible(true);
}

  // In this event we handle any button clicks
  public void actionPerformed(ActionEvent event) {
    // We get the actual command
    String s = event.getActionCommand();
    // We call handlechange with the current row and the next row for the first 5 buttons as our parameters
    switch(s) {
      case "Enter first guess":
        handleChange(first, second);
        break;
      case "Enter second guess":
        handleChange(second, third);
        break;
      case "Enter third guess":
        handleChange(third, fourth);
        break;
      case "Enter fourth guess":
        handleChange(fourth, fifth);
        break;
      case "Enter fifth guess":
        handleChange(fifth, sixth);
        break;
      case "Enter sixth guess":
        // Since the 6th guess is a special case, we call a new method, handleLastGuess()
        handleLastGuess(sixth);
        break;
      // We should never get to our default so we have it here as a precaution
      default:
        System.out.println("woops");
      }
  }

  // This is a getter for main to use the popup frame
  public JFrame getSecondFrame() {
    return frame2;
  }

  // We handle out Last Guess in this method
  public void handleLastGuess(JTextField[] sixth) {
    // we check if the guess is correct
    boolean allGreen = checkAllGreen(sixth);
    // if it is we handle the win and if not, we handle the loss
    if (allGreen) {
      handleWin();
    } else {
      handleLoss();
    }
  }

  // We use clearRow to clear the curren row of a guess if it
  // is improperly formatted
  public void clearRow(JTextField[] current) {
    // We do this by resetting the row to be editable and setting the text to be blank
    for (int index = 0; index < current.length; index++) {
      current[index].setEditable(true);
      current[index].setText("");
    }
    // We then use our output frame to display the error
    JOptionPane.showMessageDialog(frame2, "Invalid input, try again.");
  }

  // This method handles any change that happens for the first 5 guesses
  public void handleChange(JTextField[] current, JTextField[] next) {
    // We change the color based on the guess
    String s = changeColor(current);
    // If the user enters in something wrong, we clear the current row and exit the method
    if (s.equals("error")) {
      clearRow(current);
      return;
    }
    // If there is no error
    else {
      // We check if the guess is corect
      boolean allGreen = checkAllGreen(current);
      // If it is, we handle the win
      if (allGreen) {
        handleWin();
      } 
      // If it isn't a win, we set the next row to be editable
      else {
        for (int index = 0; index < next.length; index++) {
          next[index].setEditable(true);
        }
      }
    }
  }

  // In this method we check if the guess is correct
  public boolean checkAllGreen(JTextField[] current) {
    // We get the guess and check if our equation object tells us that the guess is correct by verifying that the answer is all green
    String guess = getGuess(current);
    String colors = e.display(guess);
    return colors.equals("GGGGGGGG");
  }

  // In this method we make the entire matrix uneditable
  public void disableMatrix() {
    for (int row = 0; row < matrix.length; row ++) {
      for (int col = 0; col < matrix[row].length; col++) {
        if (matrix[row][col].isEditable()) {
          matrix[row][col].setEditable(false);
        }
      }
    }
  }


  // In this method we handle a loss
  public void handleLoss() {
    // we first disable the matrix
    disableMatrix();
    // We use our output frame to inform the ueser they lost and to tell them what the correct answer was
    JOptionPane.showMessageDialog(frame2, "Sorry you lost.\nThe correct answer was " + e.getExpression());
    // We then ask if they want to play again using the output frame
    int input = JOptionPane.showConfirmDialog(frame2, "Do you want to play again y/n");
    // If the answer is yes
    if (input == 0) {
      // We get rid of the current frame 
      frame.dispose();
      // We rerun the game and inform runGame that this is not the user's first time playing
      Main.runGame(false);
    }
  }

  // In this method we handle a loss
  public void handleWin() {
    // We disable the matrix and tell the user they won
    disableMatrix();
    JOptionPane.showMessageDialog(frame2, "Congratulations you won!");
    // We then ask if they want to play again using the output frame
    int input = JOptionPane.showConfirmDialog(frame2, "Do you want to play again y/n");
    // If the answer is yes
    if (input == 0) {
      // We get rid of the current frame 
      frame.dispose();
      // We rerun the game and inform runGame that this is not the user's first time playing
      Main.runGame(false);
    }
  }

  // We check if the user's guess is valid in this method
  public boolean validInput(JTextField[] current) {
    // We initialize guess
    String guess = "";
    // We set valid to true as our default flag
    boolean valid = true;
    // We add to our guess from the current row
    for (int index = 0; index < current.length; index++) {
      // If one text field has a non-1 length we know it is false
      if (current[index].getText().length() != 1) {
        return false;
      }  else {
        guess += current[index].getText();
      }
    }
    // We initalize our lhs
    String lhs = "";
      try {  
        // We get our lhs and than add spaces around operators
        lhs = guess.substring(0, guess.indexOf("="));
        lhs = Main.formatInput(lhs);
        // We then get our answer to the expression from Main
        int ans = Main.getAnswer(lhs);
        // We then get the rhs of the guess 
        int rhs = Integer.parseInt(guess.substring(guess.indexOf("=") + 1));
        // If the length is corrent, the answer is positive, there are no leading zeroes and the rhs is equal to the actual answer, valid remains true. Otherwise it becomes false
        valid = (guess.length() == 8 && ans >= 0 && !Main.opeatorAndZero(guess) && rhs == ans);
      } 
      // if we get any exception we know the user messed up and return false
      catch (Exception e) {
        return false;
      }
    // We then return valid
    return valid;
  }

  // We chamge the color of the row in this method
  public String changeColor(JTextField[] current) {
    // if we don;t have a valid guess, we know there is an error and return error to signal that we need to let the user know of their error
    boolean valid = validInput(current);
    if (!valid) {
      return "error";
    }
    // We get our equation from the row
    String equation = getGuess(current);
    // We get the colors we need to display
    String colors = e.display(equation);
    for (int index = 0; index < colors.length(); index++) {
      String c = "" + colors.charAt(index);
      // if the color is green, we change the color to green and prevent the text field from being edited by the user
      if (c.equals("G")) {
        current[index].setEditable(false);
        current[index].setBackground(Color.GREEN);  
      } 
      // if the color is yellow, we change the color to yellow and prevent the text field from being edited by the user
      else if (c.equals("Y")) {
        current[index].setEditable(false);
        current[index].setBackground(Color.YELLOW);
      } 
      // else if the value is "B", we change the color to gray and prevent the text field from being edited
      else {
        current[index].setEditable(false);
        current[index].setBackground(Color.GRAY);
      }
    }
    // We return an empty string because there are no errors
    return "";
  }

  // In this method we get our guess from the current row
  public String getGuess(JTextField[] current) {
    // We initialize guess
    String guess = "";
    // We then loop through the row and get our guess
    for (int index = 0; index < current.length; index++) {
        guess += current[index].getText();
    }
    return guess;
  }
}