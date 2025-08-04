# Tic-Tac-Toe Game

This is a classic Tic-Tac-Toe game built in Java that runs in the command line. You can play against another person (PVP) or against a simple computer AI (PVE).

## How to Compile and Run

1.  **Prerequisites:** Make sure you have a Java Development Kit (JDK) installed on your system.
2.  **Save the Code:** Save the game code as `TicTacToe.java`.
3.  **Open a Terminal:** Navigate to the directory where you saved the file.
4.  **Compile the Code:** Run the following command to compile the game:
    ```sh
    javac TicTacToe.java
    ```
5.  **Run the Game:** Once it's compiled, run the game with this command:
    ```sh
    java TicTacToe
    ```

## How to Play

1.  **Choose a Game Mode:** When the game starts, you will be prompted to choose a mode:
    ```
    Welcome to Tic-Tac-Toe!
    Choose a game mode:
    1. Player vs Player (PVP)
    2. Player vs Environment (PVE)
    ```
    Enter `1` for a two-player game or `2` to play against the computer.

2.  **Making a Move:**
    * The game board is a 3x3 grid.
    * When it's your turn, the game will prompt you to enter your move:
        ```
        Player X, enter your move (row[1-3] col[1-3]):
        ```
    * You need to enter two numbers: the **row number** followed by the **column number**, separated by a space.
    * The rows and columns are numbered 1 to 3.

    **Example:** To place your mark in the center square, you would type `2 2` and press Enter.

    ```
    -------------
    | - | - | - |
    -------------
    | - | - | - |
    -------------
    | - | - | - |
    -------------
    Player X, enter your move (row[1-3] col[1-3]): 2 2
    ```

3.  **Winning the Game:**
    The first player to get three of their marks in a row (horizontally, vertically, or diagonally) wins the game. If the entire board is filled and no one has won, the game is a draw.
