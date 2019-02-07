import java.io.*;

public class Game {
  static private boolean completed = false;

  static private void showBoard(char[][] board) {
    for (char[] row : board) {
      buildRow(row.length);
      buildRow(row);
    }
    buildRow(board[0].length);
  }

  static private void buildRow(int len) {
    StringBuilder rowBuild = new StringBuilder();
    for (int i = 0; i < len; i++) {
      rowBuild.append("----");
    }
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
    System.out.println("Type in the vertical index of the coordinate and press return");
    int y = Integer.parseInt(br.readLine());
    System.out.println("Type in the horizontal index of the coordinate and press return");
    int x = Integer.parseInt(br.readLine());
    return player.placePiece(x, y, board.getBoard());
  }

  static private boolean checkHorizontal(Player player, char[][] gameBoard) {
    char piece = player.getPiece();
    for (char[] row : gameBoard) {
      boolean gameWon = true;
      for (char grid: row) {
        if (grid != piece) gameWon = false;
      }
      if (gameWon) return true;
    }
    return false;
  }

  static private boolean checkVertical(Player player, char[][] gameBoard) {
    char piece = player.getPiece();
    for (int i = 0; i < gameBoard[0].length; i++) {
      boolean gameWon = true;
      for (int j = 0; j < gameBoard.length; j++) {
        if (gameBoard[j][i] != piece) gameWon = false;
      }
      if (gameWon) return true;
    }
    return false;
  }

  static private boolean checkDiagonal(Player player, char[][] gameBoard) {
    char piece = player.getPiece();
    return (gameBoard[0][0] == piece && gameBoard[1][1] == piece && gameBoard[2][2] == piece)
      || (gameBoard[0][2] == piece && gameBoard[1][1] == piece && gameBoard[2][0] == piece);
  }

  static private boolean checkWinning(Player player, Board board) {
    char[][] gameBoard = board.getBoard();
    return checkHorizontal(player, gameBoard)
      || checkVertical(player, gameBoard)
      || checkDiagonal(player, gameBoard);
  }

  static private String namePlayer(BufferedReader br, int player) throws IOException {
    System.out.println(String.format("Type in Player %d's name and press return: ", player));
    String name = br.readLine();
    return name;
  }

  static private void gameOver(Player player) {
    System.out.println(String.format("%s won the Game!", player.getName()));
    player.wonAGame();
  }

  static private void initGame() throws IOException {
    InputStreamReader isr = new InputStreamReader(System.in);
    BufferedReader br = new BufferedReader(isr);

    System.out.println("=======================");
    System.out.println("   TIC    TAC    TOE   ");
    System.out.println("=======================");

    Board board = new Board(3);
    Player player1 = new Player(namePlayer(br, 1), 'X', true);
    Player player2 = new Player(namePlayer(br, 2), 'O', false);

    while (!completed) {
      boolean placed = false;
      
      if (player1.getTurn()) placed = placeNext(br, player1, board);
      else placed = placeNext(br, player2, board);

      if (placed) {
        if (checkWinning(player1, board)) {
          gameOver(player1);
          completed = true;
        } else if (checkWinning(player2, board)) {
          gameOver(player2);
          completed = true;
        } else {
          player1.toggleTurn();
          player2.toggleTurn();
        }
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

class Board {
  private char[][] board;

  public Board(int size) {
    board = new char[size][size];
  }

  char[][] getBoard() { return board; }
}

class Player {
  private String name;
  private char piece;
  private boolean turn;
  private int score = 0;

  public Player(String name, char piece, boolean turn) {
    this.name = name;
    this.piece = piece;
    this.turn = turn;
  }

  String getName() { return name; }

  char getPiece() { return piece; }

  boolean getTurn() { return turn; }

  boolean placePiece(int x, int y, char[][] board) {
    if (board[y][x] == '\u0000') {
      board[y][x] = piece;
      return true;
    } else {
      System.out.println("You cannot place there. Try again");
      return false;
    }
  }

  void toggleTurn() { turn = !turn; }

  void wonAGame() { score++; }
}
