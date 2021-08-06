import java.util.ArrayList;

public class GameState {
    final int[][] board;
    private int[] spacePos = {0, 0};
    static final int[][] INITIAL_BOARD = {{8, 7, 6}, {5, 4, 3}, {2, 1, 0}}; //Holding initial board
    static  final int[][] FINAL_BOARD = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};  //Holding final board (goal board)

    //constructor... holds a state of a board at the moment
    //finds where is 0 and holds its coordinates
    public GameState(int[][] board) {
        this.board = board;
        for(int x = 0; x < 3; x++){
            for(int y = 0; y < 3; y++) {
                if (board[x][y] == 0) {
                   this.spacePos[0] = x;
                   this.spacePos[1] = y;
                   break;
                }
            }
        }
    }

    //returns a clone of a board at the current GameState with same board config.
    public GameState clone() {
        int[][] clonedBoard = new int[3][3];
        for(int i = 0; i < 3; i++) {
            clonedBoard[i] = new int[3];
            System.arraycopy(this.board[i], 0, clonedBoard[i], 0, 3);
        }

        return new GameState(clonedBoard);
    }

    //Maybe never used---------------------
    public int[] getSpacePos() {
        return spacePos;
    }

    //returns board config as a string
    public String toString() {
        String s = "";
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                s += " " + this.board[x][y];
            }
            s += "\n";
        }
        return s;
    }

    //returns true iff the current board config == goal board config
    public boolean isGoal() {
        for (int x = 0; x < 3; x++) {
            for(int y = 0; y < 3; y++){
                if(this.board[x][y] != FINAL_BOARD[x][y]) {
                    return false;
                }
            }
        }
        return true;
    }

    //returns true iff the gs supplied has same board config as current
    public boolean sameBoard(GameState gs) {
        for (int x = 0; x < 3; x++) {
            for(int y = 0; y < 3; y++){
              if(this.board[x][y] != gs.board[x][y]) {
                  return false;
              }
            }
        }
        return true;
    }

    public int countCost(GameState gs) {
        int count = 0;
        for(int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++){
                if(gs.board[x][y] != FINAL_BOARD[x][y]) {
                    count++;
                }
            }
        }
        return count;
    }

    //*returns a list of all GameStates
    // that can be reached in a a single move
    // from current GameState
    public ArrayList<GameState> possibleMoves() {
        ArrayList<GameState> moves = new ArrayList<>();
        for(int x = 0; x < 3; x++) {
            for(int y = 0; y < 3; y++){
                int[] start = {x, y};
                if(start != this.spacePos) {
                    int diffX = Math.abs(this.spacePos[0] - start[0]);
                    int diffY = Math.abs(this.spacePos[1] - start[1]);
                    if((diffX == 1 && diffY == 0) || (diffX == 0 && diffY == 1)) {
                        GameState newState = this.clone();
                        newState.board[ this.spacePos[0] ][ this.spacePos[1] ] =
                                this.board[ start[0] ][ start[1] ];
                        newState.board[ start[0] ][ start[1] ] = 0;
                        newState.spacePos = start;
                        moves.add(newState);
                    }
                }
            }
        }
        return moves;
    }

}
