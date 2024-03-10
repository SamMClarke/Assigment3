import java.util.*;

public class TTTManager
{  
    private static final char EMPTY_CHAR = '\u0000';
    private char[][] gameBoard;
    private int[][] winCombinations;
    private int[] playerArray;
    private int[] computerArray;

    private int boardSize;
    private char playerLetter;

    public TTTManager(int boardSize, char playerLetter)
    {
        gameBoard = new char[boardSize][boardSize];
        winCombinations = new int[boardSize*2+2][boardSize*boardSize];
        playerArray = new int[boardSize*boardSize];
        computerArray = new int[boardSize*boardSize];
        this.boardSize = boardSize;
        this.playerLetter = playerLetter;

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

    public void printBoard()
    {
        for (int i = 0; i < boardSize; i++)
        {
            System.out.println(String.join("", Collections.nCopies(boardSize, "+---")) + "+");
            for (int j = 0; j < boardSize; j++)
            {
                if (gameBoard[i][j] == EMPTY_CHAR)
                {
                    System.out.print("|   ");
                }
                else
                {
                    System.out.print("| " + gameBoard[i][j] + " ");
                }
            }
            System.out.println("|");
        }
        System.out.println(String.join("", Collections.nCopies(boardSize, "+---")) + "+");
    }

    public void oneRound(Scanner input)
    {
        System.out.print("Where would you like to play?\nEnter coordinates in the form x,y: ");
        String[] coords = input.nextLine().split(",");
        int xCoord = Integer.parseInt(coords[0]);
        int yCoord = Integer.parseInt(coords[1]);
        while (xCoord < 0 || xCoord > boardSize-1 || yCoord < 0 || yCoord > boardSize-1 || gameBoard[boardSize-1-yCoord][xCoord] != EMPTY_CHAR)
        {
            System.out.print("The coordinates (" + coords[0] + "," + coords[1] + ") are either out of range or already filled. Please try again: ");
            coords = input.nextLine().split(",");
            xCoord = Integer.parseInt(coords[0]);
            yCoord = Integer.parseInt(coords[1]);
        }

        playerArray[(boardSize-1-yCoord)*boardSize+xCoord] = 1;
        gameBoard[boardSize-1-yCoord][xCoord] = playerLetter;
        printBoard();
    }

    private boolean fullBoard()
    {
        for (int i = 0; i < gameBoard.length; i++)
        {
            for (int j = 0; j < gameBoard[0].length; j++)
            {
                if (gameBoard[i][j] == EMPTY_CHAR)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public String winner()
    {
        boolean playerWin;
        boolean computerWin;

        if (fullBoard())
        {
            return "The Cat";
        }

        for (int[] combination : winCombinations)
        {
            playerWin = true;
            computerWin = true;
            for (int i = 0; i < combination.length; i++)
            {
                if (combination[i] == 1 && playerArray[i] != 1)
                {
                    playerWin = false;
                }
                if (combination[i] == 1 && computerArray[i] != 1)
                {
                    computerWin = false;
                }
                if (!playerWin && !computerWin)
                {
                    break;
                }
            }
            if (playerWin)
            {
                return "You";
            }
            else if (computerWin)
            {
                return "The Computer";
            }
        }
        return null;
    }
}