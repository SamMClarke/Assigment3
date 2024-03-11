import java.util.*;
import java.io.*;

public class TTTManager
{  
    private static final char EMPTY_CHAR = '\u0000';
    private char[][] gameBoard;
    private int[][] winCombinations;
    private int[] playerArray;
    private int[] computerArray;

    private int boardSize;
    private char playerLetter;
    private char computerLetter; 

    public TTTManager(int boardSize, char playerLetter)
    {
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

    private void generateWinCombinations()
    {
        for (int i = 0; i < boardSize; i++)
        {
            int[] horzCombination = new int[boardSize*boardSize];
            for (int j = 0; j < boardSize; j++)
            {
                horzCombination[i * boardSize + j] = 1;
            }
            winCombinations[i] = horzCombination;
        }

        for (int i = 0; i < boardSize; i++)
        {
            int[] vertCombination = new int[boardSize*boardSize];
            for (int j = 0; j < boardSize; j++)
            {
                vertCombination[i + j * boardSize] = 1;
            }
            winCombinations[i + boardSize] = vertCombination;
        }

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

    public void printBoard(PrintStream output)
    {
        for (int i = 0; i < boardSize; i++)
        {
            output.println(String.join("", Collections.nCopies(boardSize, "+---")) + "+");
            for (int j = 0; j < boardSize; j++)
            {
                if (gameBoard[i][j] == EMPTY_CHAR)
                {
                    output.print("|   ");
                }
                else
                {
                    output.print("| " + gameBoard[i][j] + " ");
                }
            }
            output.println("|");
        }
        output.println(String.join("", Collections.nCopies(boardSize, "+---")) + "+");
    }

    public void oneRound(Scanner input)
    {
        System.out.print("Where would you like to play?\nEnter coordinates in the form x,y: ");
        String[] coords = input.nextLine().split(",");
        int xCoord = Integer.parseInt(coords[0]);
        int yCoord = boardSize-1-Integer.parseInt(coords[1]);
        while (xCoord < 0 || xCoord > boardSize-1 || yCoord < 0 || yCoord > boardSize-1 || gameBoard[yCoord][xCoord] != EMPTY_CHAR)
        {
            System.out.print("The coordinates (" + coords[0] + "," + coords[1] + ") are either out of range or already filled. Please try again: ");
            coords = input.nextLine().split(",");
            xCoord = Integer.parseInt(coords[0]);
            yCoord = boardSize-1-Integer.parseInt(coords[1]);
        }

        playerArray[(yCoord)*boardSize+xCoord] = 1;
        gameBoard[yCoord][xCoord] = playerLetter;
        printBoard(System.out);
        if (!checkWinner(playerArray))
        {
            System.out.println("\nMy turn...\n");
            computerMove();
            printBoard(System.out);
        }
    }

    private void computerMove()
    {
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
                        computerArray[(i*boardSize)+j] = 0;
                    }
                }
            }
        }

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
                        playerArray[(i*boardSize)+j] = 0;
                    }
                }
            }
        }

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

    public int winner()
    {
        if (checkWinner(playerArray))
        {
            return 0;
        }
        else if (checkWinner(computerArray))
        {
            return 1;
        }
        else if (fullBoard())
        {
            return 2;
        }
        return -1;
    }
}