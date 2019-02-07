public class Board {
  private char[][] board;

  public Board(int size) {
    board = new char[size][size];
  }

  char[][] getBoard() { return board; }
}

