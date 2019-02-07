import java.io.*;

public class Game {
  static private boolean completed = false;
  static private boolean replay = false;

  static private void showBoard(char[][] board) {
    for (char[] row : board) {
      buildRow(row.length);
      buildRow(row);
    }
    buildRow(board[0].length);
  }

  static private void buildRow(int len) {
    StringBuilder rowBuild = new StringBuilder();
    for (int i = 0; i < len; i++) { rowBuild.append("----"); }
    rowBuild.append("-");
    System.out.println(rowBuild.toString());
  }

  static private void buildRow(char[] row) {
    StringBuilder rowBuild = new StringBuilder();
    for (char grid : row) {
      if (grid == '\u0000') rowBuild.append("|   ");
      else rowBuild.append("| " + grid + " ");
    }
    rowBuild.append('|');
    System.out.println(rowBuild.toString());
  }

  static private boolean placeNext(BufferedReader br, Player player, Board board) throws IOException {
    showBoard(board.getBoard());
    System.out.println(String.format("It is %s's turn.", player.getName()));
    System.out.println("Type in the vertical index of the coordinate and press return: ");
    int y = Integer.parseInt(br.readLine());
    System.out.println("Type in the horizontal index of the coordinate and press return: ");
    int x = Integer.parseInt(br.readLine());
    return player.placePiece(x, y, board.getBoard());
  }

  static private boolean checkHorizontal(char piece, char[][] gameBoard) {
    for (char[] row : gameBoard) {
      boolean gameWon = true;
      for (char grid: row) { if (grid != piece) gameWon = false; }
      if (gameWon) return true;
    }
    return false;
  }

  static private boolean checkVertical(char piece, char[][] gameBoard) {
    for (int i = 0; i < gameBoard[0].length; i++) {
      boolean gameWon = true;
      for (int j = 0; j < gameBoard.length; j++) {
        if (gameBoard[j][i] != piece) gameWon = false;
      }
      if (gameWon) return true;
    }
    return false;
  }

  static private boolean checkDiagonal(char piece, char[][] gameBoard) {
    return checkMajorDiagonal(piece, gameBoard) || checkMinorDiagonal(piece, gameBoard);
  }

  static private boolean checkMajorDiagonal(char piece, char[][] gameBoard) {
    for (int i = 0; i < gameBoard.length; i++) { if (gameBoard[i][i] != piece) return false; }
    return true;
  }

  static private boolean checkMinorDiagonal(char piece, char[][] gameBoard) {
    for (int i = 0; i < gameBoard.length; i++) { if (gameBoard[i][gameBoard.length - 1- i] != piece) return false; }
    return true;
  }

  static private boolean checkWinning(Player player, Board board) {
    char[][] gameBoard = board.getBoard();
    char piece = player.getPiece();
    return checkHorizontal(piece, gameBoard)
      || checkVertical(piece, gameBoard)
      || checkDiagonal(piece, gameBoard);
  }

  static private String namePlayer(BufferedReader br, int player) throws IOException {
    System.out.println(String.format("Type in Player %d's name and press return: ", player));
    String name = br.readLine();
    return name;
  }

  static private int generateBoard(BufferedReader br) throws IOException {
    System.out.println("Type the size of the board to play and press return: ");
    return Integer.parseInt(br.readLine());
  }

  static private void gameOver(Player player, Board board) {
    showBoard(board.getBoard());
    System.out.println(String.format("%s won the Game!", player.getName()));
    player.wonAGame();
  }

  static private boolean playAgain(BufferedReader br, Board board, int size) throws IOException {
    System.out.println("Do you want to play again? (y/n)");
    String answer = br.readLine();
    System.out.println(answer);
    if (answer.equals("y")) return true;
    else if (answer.equals("n")) return false;
    else {
      System.out.println("Invalid input. Please type again (y/n)");
      return playAgain(br, board, size);
    }
  }

  static private void showScore(Player player1, Player player2) {
    System.out.println("Current Score: ");
    System.out.println(String.format("%s: %d", player1.getName(), player1.getScore()));
    System.out.println(String.format("%s: %d", player2.getName(), player2.getScore()));
  }

  static private void initGame() throws IOException {
    InputStreamReader isr = new InputStreamReader(System.in);
    BufferedReader br = new BufferedReader(isr);

    System.out.println("=======================");
    System.out.println("   TIC    TAC    TOE   ");
    System.out.println("=======================");

    int size = generateBoard(br);

    Board board = new Board(size);
    Player player1 = new Player(namePlayer(br, 1), 'X', true);
    Player player2 = new Player(namePlayer(br, 2), 'O', false);

    while (!completed) {
      boolean placed = false;
      
      if (player1.getTurn()) placed = placeNext(br, player1, board);
      else placed = placeNext(br, player2, board);

      if (placed) {
        if (checkWinning(player1, board)) {
          gameOver(player1, board);
          completed = true;
          replay = playAgain(br, board, size);
        } else if (checkWinning(player2, board)) {
          gameOver(player2, board);
          completed = true;
          replay = playAgain(br, board, size);
        } else {
          player1.toggleTurn();
          player2.toggleTurn();
        }
      }

      if (replay) {
        completed = false;
        replay = false;
        board = new Board(size);
        showScore(player1, player2);
      }
    }

    isr.close();
    br.close();
  }

  public static void main(String args[]) {
    try {
      initGame();
    } catch (IOException e) {
      e.printStackTrace();
    }
  } 
}
