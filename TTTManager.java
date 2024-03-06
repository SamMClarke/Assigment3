import java.util.*;

public class TTTManager
{  
    private char[][] gameBoard;

    public TTTManager(int boardSize)
    {
        gameBoard = new char[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++)
            {
                gameBoard[i][j] = ' ';
            }
        }
    }


    public void printBoard()
    {
        for (int i = 0; i < gameBoard.length; i++)
        {
            System.out.println(String.join("", Collections.nCopies(gameBoard.length, "+---")) + "+");
            for (int j = 0; j < gameBoard.length; j++)
            {
                System.out.print("| " + gameBoard[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.print(String.join("", Collections.nCopies(gameBoard.length, "+---")) + "+");
    }
}