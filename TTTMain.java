import java.util.*;

public class TTTMain
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        Scanner inputInt = new Scanner(System.in);

        boolean hasQuit = false;

        intro();

        while (!hasQuit)
        {
            switch(menu(input))
            {
                case 'p':
                    playGame(input, inputInt);
                    break;
                case 's':
                    break;
                case 'r':
                    break;
                case 'q':
                    hasQuit = true;
                    break;
            }  
        }
    }

    public static void playGame(Scanner input, Scanner inputInt)
    {
        System.out.print("\nWhat board size do you want to play with? Enter 3 for (3x3), 4 for (4x4), etc: ");
        int boardSize = inputInt.nextInt();
        System.out.print("Would you like to play with X's or O's? ");
        TTTManager game = new TTTManager(boardSize, input.nextLine().toUpperCase().charAt(0));

        game.printBoard();
        while (game.winner() == null)
        {
            game.oneRound(input);
        }

        System.out.println(game.winner() + " won the game!");
    }

    public static void intro()
    {
        System.out.printf("%s%n%s%n%s%n",
        "Welcome to Sam's Tic Tac Toe game!",
        "This program will allow you play Tic Tac Toe games against the computer.", 
        "One thing to note: the Tic Tac Toe board's (0,0) origin is in the bottom left of the board.");
    }

    public static char menu(Scanner input)
    {
        System.out.printf("%n%s%n%s%n%s%n%s%n%s%n",
        "Please choose an action:",
        "(p) Play one game of Tic Tac Toe",
        "(s) Save the previous game to a file",
        "(r) Show the overall results",
        "(q) Quit the program");
        return input.nextLine().toLowerCase().charAt(0);
    }
}