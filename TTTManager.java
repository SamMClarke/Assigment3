/** 
 * TTTManager.java
 * @Author: Sam Clarke
 * @Date: 3-11-2024
 * @Class: CS&145
 * @Assignment: Assignment #3
 * @Purpose: Create an interactive Tic Tac Toe game
 */

import java.util.*;
import java.io.*;

public class TTTManager
{  
    //CLASS OBJECTS, VARIABLES, AND CONSTANTS
    private static final char EMPTY_CHAR = '\u0000';

    private char[][] gameBoard;
    private int[][] winCombinations;
    private int[] playerArray;
    private int[] computerArray;

    private int boardSize;
    private char playerLetter;
    private char computerLetter; 

    /**
     * Constructs a game object.
     * 
     * @param boardSize Width (and height) of the game board
     * @param playerLetter The letter (X or O) the player will be controlling
     */
    public TTTManager(int boardSize, char playerLetter)
    {
        //CREATE GAME ARRAYS
        gameBoard = new char[boardSize][boardSize];
        winCombinations = new int[boardSize*2+2][boardSize*boardSize];
        playerArray = new int[boardSize*boardSize];
        computerArray = new int[boardSize*boardSize];

        this.boardSize = boardSize;
        this.playerLetter = playerLetter;
        if (playerLetter == 'X')
        {
            computerLetter = 'O';
        }
        else
        {
            computerLetter = 'X';
        }
        
        generateWinCombinations();
    }

    /**
     * Generates all the possible win combinations for the game board.
     */
    private void generateWinCombinations()
    {
        //GENERATE HORIZONTAL COMBINATIONS:
        //Example 3x3:
        //111000000
        //000111000
        //000000111
        for (int i = 0; i < boardSize; i++)
        {
            int[] horzCombination = new int[boardSize*boardSize];
            for (int j = 0; j < boardSize; j++)
            {
                horzCombination[i * boardSize + j] = 1;
            }
            winCombinations[i] = horzCombination;
        }

        //GENERATE VERTICAL COMBINATIONS
        //Example 3x3:
        //100100100
        //010010010
        //001001001
        for (int i = 0; i < boardSize; i++)
        {
            int[] vertCombination = new int[boardSize*boardSize];
            for (int j = 0; j < boardSize; j++)
            {
                vertCombination[i + j * boardSize] = 1;
            }
            winCombinations[i + boardSize] = vertCombination;
        }

        //GNEREATE DIAGONAL COMBINATIONS
        //Example 3x3:
        //100010001
        //001010100
        int[] rightDiagonal = new int[boardSize*boardSize];
        int[] leftDiagonal = new int[boardSize*boardSize];
        for (int i = 0; i < boardSize; i++)
        {
            rightDiagonal[i * (boardSize + 1)] = 1;
            leftDiagonal[(i + 1) * (boardSize - 1)] = 1;
        }
        winCombinations[boardSize*2] = rightDiagonal;
        winCombinations[boardSize*2+1] = leftDiagonal;
    }

    /**
     * Prints the current game board.
     * 
     * @param output The place to print the game board to
     */
    public void printBoard(PrintStream output)
    {
        for (int i = 0; i < boardSize; i++)
        {
            output.println(String.join("", Collections.nCopies(boardSize, "+---")) + "+"); //Java 8 String.repeat workaround
            for (int j = 0; j < boardSize; j++)
            {
                if (gameBoard[i][j] == EMPTY_CHAR) //EMPTY CELL
                {
                    output.print("|   ");
                }
                else //FILLED CELL
                {
                    output.print("| " + gameBoard[i][j] + " ");
                }
            }
            output.println("|");
        }
        output.println(String.join("", Collections.nCopies(boardSize, "+---")) + "+"); //Java 8 String.repeat workaround
    }

    /**
     * Completes one round (one X played and one O played) of the game.
     * 
     * @param input Scanner object for gathering user coordinates
     */
    public void oneRound(Scanner input)
    {
        System.out.print("Where would you like to play?\nEnter coordinates in the form x,y: ");
        String[] coords = input.nextLine().split("[/|.|,| |]"); //SPLIT COORIDATES INTO X,Y COMPONENTS
        int xCoord = Integer.parseInt(coords[0]);
        int yCoord = boardSize-1-Integer.parseInt(coords[1]);
        //WHILE INVALID COORDINATES, ASK AGAIN
        while (xCoord < 0 || xCoord > boardSize-1 || yCoord < 0 || yCoord > boardSize-1 || gameBoard[yCoord][xCoord] != EMPTY_CHAR)
        {
            System.out.print("The coordinates (" + coords[0] + "," + coords[1] + ") are either out of range or already filled. Please try again: ");
            coords = input.nextLine().split("[/|.|,| |]");
            xCoord = Integer.parseInt(coords[0]);
            yCoord = boardSize-1-Integer.parseInt(coords[1]);
        }

        //UPDATE PLAYER ARRAY AND GAME BOARD
        playerArray[(yCoord)*boardSize+xCoord] = 1;
        gameBoard[yCoord][xCoord] = playerLetter;
        printBoard(System.out);

        if (!checkWinner(playerArray)) //IF THE PLAYER HASN'T WON YET
        {
            //COMPUTER'S TURN
            System.out.println("\nMy turn...\n");
            computerMove();
            printBoard(System.out);
        }
    }

    /**
     * Places computer letter in a cell using three rules:
     * -If computer is one move away from winning -> win
     * -If the user is one move away from winning -> block
     * -Else play a letter in a random cell.
     */
    private void computerMove()
    {
        //CHECK FOR COMPUTER WINS
        for (int i = 0; i < gameBoard.length; i++)
        {
            for (int j = 0; j < gameBoard[i].length; j++)
            {
                if (gameBoard[i][j] == EMPTY_CHAR)
                {
                    computerArray[(i*boardSize)+j] = 1;
                    if (checkWinner(computerArray))
                    {
                        gameBoard[i][j] = computerLetter;
                        return;
                    }
                    else
                    {
                        //UNDO MOVE IF IT DOES NOT LEAD TO A WIN
                        computerArray[(i*boardSize)+j] = 0;
                    }
                }
            }
        }

        //CHECK FOR USER BLOCKS
        for (int i = 0; i < gameBoard.length; i++)
        {
            for (int j = 0; j < gameBoard[i].length; j++)
            {
                if (gameBoard[i][j] == EMPTY_CHAR)
                {
                    playerArray[(i*boardSize)+j] = 1;
                    if (checkWinner(playerArray))
                    {
                        playerArray[(i*boardSize)+j] = 0;
                        computerArray[(i*boardSize)+j] = 1;
                        gameBoard[i][j] = computerLetter;
                        return;
                    }
                    else
                    {
                        //UNDO MOVE IF IT DOES NOT LEAD TO A BLOCK
                        playerArray[(i*boardSize)+j] = 0;
                    }
                }
            }
        }

        //PLACE LETTER IN RANDOM CELL
        Random r = new Random();
        int row;
        int col;
        do
        {
            row = boardSize-1-r.nextInt(boardSize);
            col = r.nextInt(boardSize);
        } while (gameBoard[row][col] != EMPTY_CHAR);

        computerArray[(row)*boardSize+col] = 1;
        gameBoard[row][col] = computerLetter;
    }

    /**
     * Checks if the given player has won the game.
     * 
     * @param array The array of the player to check for
     * @return A booleon of whether or not they won
     */
    private boolean checkWinner(int[] array)
    {
        boolean hasWon = false;
        for (int[] combination : winCombinations)
        {
            hasWon = true;
            for (int i = 0; i < combination.length; i++)
            {
                if (combination[i] == 1 && array[i] != 1)
                {
                    hasWon = false;
                    break;
                }
            }
            if (hasWon)
            {
                return hasWon;
            }
        }
        return hasWon;
    }

    /**
     * Checks if the board is full and therefore a tie.
     * 
     * @return A boolean of whether or not the board is full
     */
    private boolean fullBoard()
    {
        for (int i = 0; i < gameBoard.length; i++)
        {
            for (int j = 0; j < gameBoard[i].length; j++)
            {
                if (gameBoard[i][j] == EMPTY_CHAR)
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets the winner as an int value (to be used in the results array later).
     * 
     * @return The int value of the winner
     */
    public int winner()
    {
        if (checkWinner(playerArray)) //PLAYER WINS
        {
            return 0;
        }
        else if (checkWinner(computerArray)) //COMPUTER WINS
        {
            return 1;
        }
        else if (fullBoard()) //TIE
        {
            return 2;
        }
        return -1; //NO WINNER
    }
}