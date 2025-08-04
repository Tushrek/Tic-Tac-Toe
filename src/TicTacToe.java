import java.util.Random;
import java.util.Scanner;

/**
 * A command-line based Tic-Tac-Toe game with two modes:
 * 1. Player vs. Player (PVP)
 * 2. Player vs. Environment (PVE), where the player competes against a simple AI.
 */
public class TicTacToe {

    private char[][] board;
    private char currentPlayer;
    private static final int BOARD_SIZE = 3;
    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';
    private static final char EMPTY_CELL = '-';

    /**
     * Constructor to initialize the game.
     */
    public TicTacToe() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        currentPlayer = PLAYER_X; // Player X starts the game
        initializeBoard();
    }

    /**
     * Initializes the board with empty cells.
     */
    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY_CELL;
            }
        }
    }

    /**
     * Prints the current state of the game board to the console.
     */
    private void printBoard() {
        System.out.println("-------------");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print("| ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println();
            System.out.println("-------------");
        }
    }

    /**
     * Checks if the game board is full.
     * @return true if the board is full, false otherwise.
     */
    private boolean isBoardFull() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == EMPTY_CELL) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the current player has won the game.
     * @return true if the current player has won, false otherwise.
     */
    private boolean checkWin() {
        // Check rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) {
                return true;
            }
        }
        // Check columns
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (board[0][j] == currentPlayer && board[1][j] == currentPlayer && board[2][j] == currentPlayer) {
                return true;
            }
        }
        // Check diagonals
        if ((board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) ||
                (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer)) {
            return true;
        }
        return false;
    }

    /**
     * Switches the current player.
     */
    private void switchPlayer() {
        currentPlayer = (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
    }

    /**
     * Places a mark at the specified row and col.
     * @param row The row to place the mark.
     * @param col The column to place the mark.
     * @return true if the move was successful, false otherwise.
     */
    private boolean placeMark(int row, int col) {
        if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE && board[row][col] == EMPTY_CELL) {
            board[row][col] = currentPlayer;
            return true;
        }
        return false;
    }

    /**
     * Starts the Player vs. Player game mode.
     */
    public void startPVP() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printBoard();
            System.out.println("Player " + currentPlayer + ", enter your move (row[1-3] col[1-3]):");
            int row = scanner.nextInt() - 1;
            int col = scanner.nextInt() - 1;

            if (placeMark(row, col)) {
                if (checkWin()) {
                    printBoard();
                    System.out.println("Player " + currentPlayer + " wins!");
                    break;
                } else if (isBoardFull()) {
                    printBoard();
                    System.out.println("It's a draw!");
                    break;
                }
                switchPlayer();
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }
        scanner.close();
    }

    /**
     * Starts the Player vs. Environment (AI) game mode.
     */
    public void startPVE() {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        System.out.println("You are Player X. The AI is Player O.");

        while (true) {
            printBoard();
            if (currentPlayer == PLAYER_X) {
                System.out.println("Player " + currentPlayer + ", enter your move (row[1-3] col[1-3]):");
                int row = scanner.nextInt() - 1;
                int col = scanner.nextInt() - 1;

                if (placeMark(row, col)) {
                    if (checkWin()) {
                        printBoard();
                        System.out.println("Player " + currentPlayer + " wins!");
                        break;
                    } else if (isBoardFull()) {
                        printBoard();
                        System.out.println("It's a draw!");
                        break;
                    }
                    switchPlayer();
                } else {
                    System.out.println("Invalid move. Try again.");
                }
            } else { // AI's turn
                System.out.println("AI's turn (Player O)");
                int row, col;
                do {
                    row = random.nextInt(BOARD_SIZE);
                    col = random.nextInt(BOARD_SIZE);
                } while (!placeMark(row, col));

                System.out.println("AI placed O at (" + (row + 1) + ", " + (col + 1) + ")");

                if (checkWin()) {
                    printBoard();
                    System.out.println("Player " + currentPlayer + " (AI) wins!");
                    break;
                } else if (isBoardFull()) {
                    printBoard();
                    System.out.println("It's a draw!");
                    break;
                }
                switchPlayer();
            }
        }
        scanner.close();
    }

    /**
     * The main method to run the game.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Tic-Tac-Toe!");
        System.out.println("Choose a game mode:");
        System.out.println("1. Player vs Player (PVP)");
        System.out.println("2. Player vs Environment (PVE)");
        int choice = scanner.nextInt();

        TicTacToe game = new TicTacToe();

        switch (choice) {
            case 1:
                game.startPVP();
                break;
            case 2:
                game.startPVE();
                break;
            default:
                System.out.println("Invalid choice. Exiting.");
                break;
        }
        scanner.close();
    }
}
