import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Enhanced Tic-Tac-Toe Game with Advanced Features
 * Features:
 * - Multiple AI difficulty levels (Easy, Medium, Hard)
 * - Game statistics tracking
 * - Save/Load game functionality
 * - Tournament mode
 * - Customizable board sizes (3x3, 4x4, 5x5)
 * - Color-coded console output
 * - Detailed game history
 */
public class EnhancedTicTacToe {
    
    // ANSI Color codes for better UI
    public static final String RESET = "\033[0m";
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String BLUE = "\033[34m";
    public static final String PURPLE = "\033[35m";
    public static final String CYAN = "\033[36m";
    public static final String WHITE = "\033[37m";
    public static final String BOLD = "\033[1m";
    
    private char[][] board;
    private char currentPlayer;
    private int boardSize;
    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';
    private static final char EMPTY_CELL = '-';
    
    // Game statistics
    private static GameStats stats = new GameStats();
    private List<String> moveHistory;
    private long gameStartTime;
    
    // AI difficulty levels
    public enum Difficulty {
        EASY, MEDIUM, HARD
    }
    
    /**
     * Constructor with customizable board size
     */
    public EnhancedTicTacToe(int size) {
        this.boardSize = (size >= 3 && size <= 5) ? size : 3;
        this.board = new char[boardSize][boardSize];
        this.currentPlayer = PLAYER_X;
        this.moveHistory = new ArrayList<>();
        this.gameStartTime = System.currentTimeMillis();
        initializeBoard();
    }
    
    /**
     * Default constructor for 3x3 board
     */
    public EnhancedTicTacToe() {
        this(3);
    }
    
    private void initializeBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = EMPTY_CELL;
            }
        }
    }
    
    private void printBoard() {
        System.out.println(CYAN + "\n" + "=".repeat(boardSize * 6 + 1) + RESET);
        
        // Print column numbers
        System.out.print(YELLOW + "   ");
        for (int j = 1; j <= boardSize; j++) {
            System.out.printf("%4d  ", j);
        }
        System.out.println(RESET);
        
        for (int i = 0; i < boardSize; i++) {
            System.out.print(YELLOW + (i + 1) + "  " + RESET);
            for (int j = 0; j < boardSize; j++) {
                char cell = board[i][j];
                String color = (cell == PLAYER_X) ? RED : (cell == PLAYER_O) ? BLUE : WHITE;
                System.out.print("| " + color + cell + RESET + " ");
            }
            System.out.println("|");
            System.out.println(CYAN + "   " + "-".repeat(boardSize * 6 - 1) + RESET);
        }
    }
    
    private boolean isBoardFull() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == EMPTY_CELL) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean checkWin() {
        // Check rows
        for (int i = 0; i < boardSize; i++) {
            boolean win = true;
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] != currentPlayer) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }
        
        // Check columns
        for (int j = 0; j < boardSize; j++) {
            boolean win = true;
            for (int i = 0; i < boardSize; i++) {
                if (board[i][j] != currentPlayer) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }
        
        // Check main diagonal
        boolean win = true;
        for (int i = 0; i < boardSize; i++) {
            if (board[i][i] != currentPlayer) {
                win = false;
                break;
            }
        }
        if (win) return true;
        
        // Check anti-diagonal
        win = true;
        for (int i = 0; i < boardSize; i++) {
            if (board[i][boardSize - 1 - i] != currentPlayer) {
                win = false;
                break;
            }
        }
        return win;
    }
    
    private void switchPlayer() {
        currentPlayer = (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
    }
    
    private boolean placeMark(int row, int col) {
        if (row >= 0 && row < boardSize && col >= 0 && col < boardSize && board[row][col] == EMPTY_CELL) {
            board[row][col] = currentPlayer;
            moveHistory.add(String.format("Player %c: (%d,%d)", currentPlayer, row + 1, col + 1));
            return true;
        }
        return false;
    }
    
    /**
     * Enhanced PVP mode with move history and timing
     */
    public void startPVP() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(BOLD + GREEN + "🎮 Player vs Player Mode Started!" + RESET);
        
        while (true) {
            printBoard();
            displayGameInfo();
            
            System.out.println(BOLD + "Player " + ((currentPlayer == PLAYER_X) ? RED + currentPlayer : BLUE + currentPlayer) + RESET + BOLD + ", enter your move (row col):" + RESET);
            
            try {
                int row = scanner.nextInt() - 1;
                int col = scanner.nextInt() - 1;
                
                if (placeMark(row, col)) {
                    if (checkWin()) {
                        printBoard();
                        System.out.println(BOLD + GREEN + "🎉 Player " + currentPlayer + " wins!" + RESET);
                        stats.recordWin(currentPlayer == PLAYER_X ? "Player X" : "Player O");
                        break;
                    } else if (isBoardFull()) {
                        printBoard();
                        System.out.println(BOLD + YELLOW + "🤝 It's a draw!" + RESET);
                        stats.recordDraw();
                        break;
                    }
                    switchPlayer();
                } else {
                    System.out.println(RED + "❌ Invalid move. Try again." + RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "❌ Invalid input. Please enter numbers only." + RESET);
                scanner.nextLine(); // Clear invalid input
            }
        }
        
        endGame();
    }
    
    /**
     * Enhanced PVE mode with AI difficulty levels
     */
    public void startPVE(Difficulty difficulty) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(BOLD + GREEN + "🤖 Player vs AI Mode Started! (Difficulty: " + difficulty + ")" + RESET);
        System.out.println(CYAN + "You are Player X. The AI is Player O." + RESET);
        
        while (true) {
            printBoard();
            displayGameInfo();
            
            if (currentPlayer == PLAYER_X) {
                System.out.println(BOLD + "Your turn " + RED + "X" + RESET + BOLD + ", enter your move (row col):" + RESET);
                
                try {
                    int row = scanner.nextInt() - 1;
                    int col = scanner.nextInt() - 1;
                    
                    if (placeMark(row, col)) {
                        if (checkWin()) {
                            printBoard();
                            System.out.println(BOLD + GREEN + "🎉 You win! Congratulations!" + RESET);
                            stats.recordWin("Player");
                            break;
                        } else if (isBoardFull()) {
                            printBoard();
                            System.out.println(BOLD + YELLOW + "🤝 It's a draw!" + RESET);
                            stats.recordDraw();
                            break;
                        }
                        switchPlayer();
                    } else {
                        System.out.println(RED + "❌ Invalid move. Try again." + RESET);
                    }
                } catch (InputMismatchException e) {
                    System.out.println(RED + "❌ Invalid input. Please enter numbers only." + RESET);
                    scanner.nextLine();
                }
            } else {
                System.out.println(CYAN + "🤖 AI is thinking..." + RESET);
                try {
                    Thread.sleep(1000); // Simulate AI thinking
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                int[] move = getAIMove(difficulty);
                placeMark(move[0], move[1]);
                
                System.out.println(BLUE + "AI placed O at (" + (move[0] + 1) + ", " + (move[1] + 1) + ")" + RESET);
                
                if (checkWin()) {
                    printBoard();
                    System.out.println(BOLD + RED + "🤖 AI wins! Better luck next time!" + RESET);
                    stats.recordWin("AI");
                    break;
                } else if (isBoardFull()) {
                    printBoard();
                    System.out.println(BOLD + YELLOW + "🤝 It's a draw!" + RESET);
                    stats.recordDraw();
                    break;
                }
                switchPlayer();
            }
        }
        
        endGame();
    }
    
    /**
     * AI move generation based on difficulty
     */
    private int[] getAIMove(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return getRandomMove();
            case MEDIUM:
                return getMediumMove();
            case HARD:
                return getHardMove();
            default:
                return getRandomMove();
        }
    }
    
    private int[] getRandomMove() {
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(boardSize);
            col = random.nextInt(boardSize);
        } while (board[row][col] != EMPTY_CELL);
        return new int[]{row, col};
    }
    
    private int[] getMediumMove() {
        // Try to win first
        int[] winMove = findWinningMove(PLAYER_O);
        if (winMove != null) return winMove;
        
        // Block opponent's winning move
        int[] blockMove = findWinningMove(PLAYER_X);
        if (blockMove != null) return blockMove;
        
        // Otherwise, random move
        return getRandomMove();
    }
    
    private int[] getHardMove() {
        // Use minimax algorithm for best move
        int[] bestMove = minimax(0, PLAYER_O);
        return new int[]{bestMove[1], bestMove[2]};
    }
    
    private int[] findWinningMove(char player) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == EMPTY_CELL) {
                    board[i][j] = player;
                    char temp = currentPlayer;
                    currentPlayer = player;
                    boolean wins = checkWin();
                    currentPlayer = temp;
                    board[i][j] = EMPTY_CELL;
                    if (wins) {
                        return new int[]{i, j};
                    }
                }
            }
        }
        return null;
    }
    
    private int[] minimax(int depth, char player) {
        List<int[]> availableMoves = getAvailableMoves();
        
        char temp = currentPlayer;
        currentPlayer = player;
        boolean playerWins = checkWin();
        currentPlayer = (player == PLAYER_O) ? PLAYER_X : PLAYER_O;
        boolean opponentWins = checkWin();
        currentPlayer = temp;
        
        if (playerWins && player == PLAYER_O) return new int[]{1, -1, -1};
        if (opponentWins && player == PLAYER_X) return new int[]{-1, -1, -1};
        if (availableMoves.isEmpty()) return new int[]{0, -1, -1};
        
        int bestScore = (player == PLAYER_O) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int bestRow = -1, bestCol = -1;
        
        for (int[] move : availableMoves) {
            board[move[0]][move[1]] = player;
            int score = minimax(depth + 1, (player == PLAYER_O) ? PLAYER_X : PLAYER_O)[0];
            board[move[0]][move[1]] = EMPTY_CELL;
            
            if (player == PLAYER_O && score > bestScore) {
                bestScore = score;
                bestRow = move[0];
                bestCol = move[1];
            } else if (player == PLAYER_X && score < bestScore) {
                bestScore = score;
                bestRow = move[0];
                bestCol = move[1];
            }
        }
        
        return new int[]{bestScore, bestRow, bestCol};
    }
    
    private List<int[]> getAvailableMoves() {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == EMPTY_CELL) {
                    moves.add(new int[]{i, j});
                }
            }
        }
        return moves;
    }
    
    private void displayGameInfo() {
        System.out.println(PURPLE + "📊 Moves: " + moveHistory.size() + " | Time: " + getElapsedTime() + "s" + RESET);
        if (!moveHistory.isEmpty()) {
            System.out.println(CYAN + "Last move: " + moveHistory.get(moveHistory.size() - 1) + RESET);
        }
    }
    
    private long getElapsedTime() {
        return (System.currentTimeMillis() - gameStartTime) / 1000;
    }
    
    private void endGame() {
        System.out.println(PURPLE + "\n📋 Move History:" + RESET);
        for (String move : moveHistory) {
            System.out.println("  " + move);
        }
        System.out.println(CYAN + "⏱️  Game Duration: " + getElapsedTime() + " seconds" + RESET);
        stats.displayStats();
        
        // Ask to save game
        Scanner scanner = new Scanner(System.in);
        System.out.print(YELLOW + "💾 Save this game? (y/n): " + RESET);
        if (scanner.nextLine().toLowerCase().startsWith("y")) {
            saveGame();
        }
    }
    
    /**
     * Tournament mode
     */
    public static void startTournament() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(BOLD + GREEN + "🏆 TOURNAMENT MODE" + RESET);
        System.out.print("Enter number of games: ");
        int numGames = scanner.nextInt();
        
        int playerWins = 0, aiWins = 0, draws = 0;
        
        for (int i = 1; i <= numGames; i++) {
            System.out.println(BOLD + CYAN + "\n🎮 Game " + i + " of " + numGames + RESET);
            EnhancedTicTacToe game = new EnhancedTicTacToe();
            
            // Simulate quick AI vs AI for tournament
            while (true) {
                int[] move = game.getRandomMove();
                game.placeMark(move[0], move[1]);
                
                if (game.checkWin()) {
                    if (game.currentPlayer == PLAYER_X) playerWins++;
                    else aiWins++;
                    break;
                } else if (game.isBoardFull()) {
                    draws++;
                    break;
                }
                game.switchPlayer();
            }
        }
        
        System.out.println(BOLD + GREEN + "\n🏆 TOURNAMENT RESULTS:" + RESET);
        System.out.println("Player X Wins: " + playerWins);
        System.out.println("Player O Wins: " + aiWins);
        System.out.println("Draws: " + draws);
    }
    
    /**
     * Save game functionality
     */
    private void saveGame() {
        try {
            String filename = "game_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
            PrintWriter writer = new PrintWriter(new FileWriter(filename));
            
            writer.println("Tic-Tac-Toe Game Save");
            writer.println("Date: " + LocalDateTime.now());
            writer.println("Board Size: " + boardSize);
            writer.println("Duration: " + getElapsedTime() + " seconds");
            writer.println("\nMove History:");
            for (String move : moveHistory) {
                writer.println(move);
            }
            
            writer.close();
            System.out.println(GREEN + "✅ Game saved as " + filename + RESET);
        } catch (IOException e) {
            System.out.println(RED + "❌ Error saving game: " + e.getMessage() + RESET);
        }
    }
    
    /**
     * Game statistics class
     */
    static class GameStats {
        private int totalGames = 0;
        private int playerXWins = 0;
        private int playerOWins = 0;
        private int draws = 0;
        
        public void recordWin(String winner) {
            totalGames++;
            if (winner.contains("X") || winner.equals("Player")) {
                playerXWins++;
            } else {
                playerOWins++;
            }
        }
        
        public void recordDraw() {
            totalGames++;
            draws++;
        }
        
        public void displayStats() {
            System.out.println(BOLD + CYAN + "\n📊 GAME STATISTICS:" + RESET);
            System.out.println("Total Games: " + totalGames);
            System.out.println("Player X Wins: " + playerXWins);
            System.out.println("Player O Wins: " + playerOWins);
            System.out.println("Draws: " + draws);
            if (totalGames > 0) {
                System.out.printf("Win Rate X: %.1f%%\n", (playerXWins * 100.0 / totalGames));
                System.out.printf("Win Rate O: %.1f%%\n", (playerOWins * 100.0 / totalGames));
            }
        }
        
        public void resetStats() {
            totalGames = playerXWins = playerOWins = draws = 0;
            System.out.println(GREEN + "✅ Statistics reset!" + RESET);
        }
    }
    
    /**
     * Enhanced main menu
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println(BOLD + CYAN + "╔══════════════════════════════════╗" + RESET);
        System.out.println(BOLD + CYAN + "║     🎮 ENHANCED TIC-TAC-TOE 🎮    ║" + RESET);
        System.out.println(BOLD + CYAN + "║        Advanced Edition          ║" + RESET);
        System.out.println(BOLD + CYAN + "╚══════════════════════════════════╝" + RESET);
        
        while (true) {
            System.out.println(BOLD + "\n🎯 MAIN MENU:" + RESET);
            System.out.println("1. 🎮 Player vs Player");
            System.out.println("2. 🤖 Player vs AI (Easy)");
            System.out.println("3. 🤖 Player vs AI (Medium)");
            System.out.println("4. 🤖 Player vs AI (Hard)");
            System.out.println("5. 🏆 Tournament Mode");
            System.out.println("6. 📊 View Statistics");
            System.out.println("7. 🔄 Reset Statistics");
            System.out.println("8. ⚙️  Custom Board Size");
            System.out.println("9. 🚪 Exit");
            
            System.out.print(YELLOW + "Choose option (1-9): " + RESET);
            
            try {
                int choice = scanner.nextInt();
                
                switch (choice) {
                    case 1:
                        new EnhancedTicTacToe().startPVP();
                        break;
                    case 2:
                        new EnhancedTicTacToe().startPVE(Difficulty.EASY);
                        break;
                    case 3:
                        new EnhancedTicTacToe().startPVE(Difficulty.MEDIUM);
                        break;
                    case 4:
                        new EnhancedTicTacToe().startPVE(Difficulty.HARD);
                        break;
                    case 5:
                        startTournament();
                        break;
                    case 6:
                        stats.displayStats();
                        break;
                    case 7:
                        stats.resetStats();
                        break;
                    case 8:
                        System.out.print("Enter board size (3-5): ");
                        int size = scanner.nextInt();
                        System.out.println("Choose game mode:");
                        System.out.println("1. PVP  2. PVE Easy  3. PVE Medium  4. PVE Hard");
                        int mode = scanner.nextInt();
                        EnhancedTicTacToe customGame = new EnhancedTicTacToe(size);
                        switch (mode) {
                            case 1: customGame.startPVP(); break;
                            case 2: customGame.startPVE(Difficulty.EASY); break;
                            case 3: customGame.startPVE(Difficulty.MEDIUM); break;
                            case 4: customGame.startPVE(Difficulty.HARD); break;
                        }
                        break;
                    case 9:
                        System.out.println(BOLD + GREEN + "👋 Thanks for playing! Goodbye!" + RESET);
                        System.exit(0);
                        break;
                    default:
                        System.out.println(RED + "❌ Invalid choice. Please try again." + RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "❌ Invalid input. Please enter a number." + RESET);
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}
