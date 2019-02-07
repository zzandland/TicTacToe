public class Player {
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

  int getScore() { return score; }

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
