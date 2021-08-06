import java.util.ArrayList;

public class Node {
    GameState state;
    Node parent;

    //f(heuristic function) = h(number of missplaced puzzles) + g(depth)
    private int total;  //F = H+G
    private int score;  //H - score
    private int level;  //G - score(depth)


    //General... node constructor
    public Node (GameState state, Node parent, int score, int level) {
        this.state = state;
        this.parent = parent;
        this.score = score;
        this.level = level;

    }

    //Initial... node constructor
    public Node(GameState state) {
        this(state, null, 8, 0);
    }

    public int getTotal() {
        return getScore() + getLevel();
    }

    public int getScore(){
        return score;
    }

    public int getLevel(){
        return level;
    }

    public String toString() {
        return "Node: " + state + "";
    }

    /*
        Provided a List with nodes and current GameState,
        method will search for node from List that has the GameState
        as the one specified
        returns first one found
        or null if not found at all
    */
    public static Node findNodeWithState(ArrayList<Node> nodeArrayList, GameState gs) {
        for (Node node : nodeArrayList) {
            if(gs.sameBoard(node.state)){
                return node;
            }
        }
        return null;
    }

}
