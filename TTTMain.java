/** 
 * TTTMain.java
 * @Author: Sam Clarke
 * @Date: 3-11-2024
 * @Class: CS&145
 * @Assignment: Assignment #3
 * @Purpose: Create an interactive Tic Tac Toe game
 */

import java.util.*;
import java.io.*;

public class TTTMain
{
    public static void main(String[] args)
    {
        //OBJECTS AND VARIABLES
        Scanner input = new Scanner(System.in);
        Scanner inputInt = new Scanner(System.in);
        TTTManager currentGame = null;
        int[] results = new int[3];
        boolean hasQuit = false;

        intro();

        while (!hasQuit)
        {
            switch(menu(input))
            {
                case 'p': //Play one game with the user
                    currentGame = playGame(input, inputInt, results);
                    break;
                case 's': //Save the previous game to a file
                    if (currentGame != null)
                    {
                        saveGame(input, currentGame);
                    }
                    else
                    {
                        System.out.println("No game to save, please play a game first!");
                    }
                    break;
                case 'r': //Show the overall results
                    showResults(results);
                    break;
                case 'q': //Quit the program
                    hasQuit = true;
                    break;
            }  
        }
    }

    /**
     * Saves the previous game to a File.
     * 
     * @param input Scanner object for gathering file name
     * @param game Previous game object
     */
    public static void saveGame(Scanner input, TTTManager game)
    {
        try
        {
            System.out.print("\nWhat is the name of the file you would like to save your game to? ");
            PrintStream output = new PrintStream(new File(input.nextLine()));
            game.printBoard(output);
            output.println(winnerName(game.winner()) + " won the game!");
            System.out.println("Game has been sucessfully saved!");
        } 
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    /**
     * Prints out the overall results to the console.
     * 
     * @param results Array that stores the overall results data
     */
    public static void showResults(int[] results)
    {
        System.out.println("\nYour Wins: " + results[0]);
        System.out.println("Computer Wins: " + results[1]);
        System.out.println("Cat (tie) Wins: " + results[2]);
    }

    /**
     * Plays one game of Tic Tac Toe with the user
     * 
     * @param input Scanner object for gathering user string input
     * @param inputInt Scanner object for gathering user int inputs
     * @param results Array taht stores the overall results data
     * @return The game object
     */
    public static TTTManager playGame(Scanner input, Scanner inputInt, int[] results)
    {
        //GATHER USER INPUT DATA
        System.out.print("\nWhat board size do you want to play with? Enter 3 for (3x3), 4 for (4x4), etc: ");
        int boardSize = inputInt.nextInt();
        System.out.print("Would you like to play with X's or O's? ");
        char playerLetter = input.nextLine().toUpperCase().charAt(0);
        while (playerLetter != 'X' && playerLetter != 'O')
        {
            System.out.print("Invalid letter. Please choose either X or O: ");
            playerLetter = input.nextLine().toUpperCase().charAt(0);
        }

        //CREATE GAME OBJECT
        TTTManager game = new TTTManager(boardSize, playerLetter);
        game.printBoard(System.out);

        while (game.winner() < 0) //KEEP TAKING TURNS UNTIL WINNER
        {
            game.oneRound(input);
        }
        results[game.winner()]++; //UPDATE RESULTS
        System.out.println(winnerName(game.winner()) + " won the game!");

        return game;
    }

    /**
     * Gets the name of the winner.
     * 
     * @param winner Integer value that represents specific winner (0 = user, 1 = computer, 2 = tie)
     * @return A string of the winner's name
     */
    public static String winnerName(int winner)
    {
        if (winner == 0) {
            return "You";
        } else if (winner == 1) {
            return "The Computer";
        } else if (winner == 2) {
            return "The Cat";
        }
        return null;
    }

    /**
     * Prints an intro to the game.
     */
    public static void intro()
    {
        System.out.printf("%s%n%s%n%s%n",
        "Welcome to Sam's Tic Tac Toe game!",
        "This program will allow you play Tic Tac Toe games against the computer.", 
        "One thing to note: the Tic Tac Toe board's (0,0) origin is in the bottom left of the board.");
    }

    /**
     * Prints the menu to the game.
     * 
     * @param input Scanner object for gathering user input
     * @return The user's character input
     */
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