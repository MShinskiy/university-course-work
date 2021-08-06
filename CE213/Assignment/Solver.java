
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import static java.lang.Integer.MAX_VALUE;

public class Solver {
    Node rootNode;                                      //Initial node
    ArrayList<Node> unexpanded = new ArrayList<Node>(); // Holds unexpanded node list
    ArrayList<Node> expanded = new ArrayList<Node>();   // Holds expanded node list
    Integer count;



    //Constructor that sets up instance of class with a node corresponding to the initial state as the root node.
    public Solver(int[][] initialBoard) {
        GameState initialState = new GameState(initialBoard);
        rootNode = new Node(initialState);
    }

    public void solve(PrintWriter output) {
        unexpanded.add(rootNode); //initial node to start from
        int c = 1;//DELETE
        //While loop in which  logic works
        while( unexpanded.size() > 0 ){ //Loop until there is an unexpanded node
                                        // (in case we are given until finds solution)
            int tempScore = MAX_VALUE;  //to compare score (h)
            int tempTotal = MAX_VALUE;  //to compare total (f)
            int index = 0;              //store index of the node

            for(Node n : unexpanded) {  //for-each loop(Node selection):Most important
                if(n.getTotal() <= tempTotal) {              //selects node with smallest total (f)
                    tempTotal = n.getTotal();
                    //index = unexpanded.indexOf(n);          //store index of selected node
                    if (n.getScore() <= tempScore) {          //if a node with same total (f) but smaller score(h),
                        tempScore = n.getScore();             //then its selected
                        index = unexpanded.indexOf(n);        //Store index of the node that is of 1. smallest total score and 2. smallest score (cost)
                    }
                }
            }

            Node n = unexpanded.get(index);                 //retrieve selected node
            expanded.add(n);                                //expand selected node
            //print expanded nodes//DELETE
            output.println(
                            "N: "      + c++ +
                            " Score: " + n.getScore() +
                            " Level: " + n.getLevel() +
                            ", 0 at: {x: " + n.state.getSpacePos()[0] + ", y: " + n.state.getSpacePos()[1] + "}"
            );
            output.println(n.state.toString());

            int newLevel = n.getLevel() + 1;                //increment level (g) by 1
            unexpanded.remove(n);                           //remove from unexpanded list, because was expanded
            //finish if a goal state found
            if(n.state.isGoal()) {
                reportSolution(n, output);
                return;
            } else {
                //apply possible moves to nodes from moveList
                ArrayList<GameState> moveList = n.state.possibleMoves();

                for(GameState gs : moveList){
                    if((Node.findNodeWithState(unexpanded, gs) == null) &&
                            (Node.findNodeWithState(expanded, gs) == null)) {
                        //and store newly generated node in the unexpanded list
                        int newScore = n.state.countCost(n.state);          //calculate cost of new node
                        Node newNode = new Node(gs, n, newScore, newLevel); //create node
                        unexpanded.add(newNode);                            //add node
                    }
                }
            }
        }
        output.println("No solution found");
    }

    //Prints all the steps in solution. Recursive function
    public void printSolution(Node n, PrintWriter output) {
        if(n.parent != null) {
            printSolution(n.parent, output);
        }
        if (count < 10) {
            output.println("   " + count++);
        } else {
            output.println("  " + count++);
        }
        output.println(n.state);
    }

    //Prints solution and statistics
    public void reportSolution(Node n, PrintWriter output) {

        output.println("Solution found!");
        count = 1;
        printSolution(n, output);
        output.println((n.getLevel()+1) + " moves");
        output.println("Nodes expanded: " + this.expanded.size());
        output.println("Nodes unexpanded: " + this.unexpanded.size());
        output.println();
    }

    public static void main(String[] args) throws Exception{
        long sTime = System.currentTimeMillis();
        Solver problem = new Solver (GameState.INITIAL_BOARD);
        File outFile = new File("output.txt");
        PrintWriter output = new PrintWriter(outFile);
        problem.solve(output);
        long eTime = System.currentTimeMillis();
        output.println("Time taken: " + ((eTime - sTime)/1000)/60 + "m " + ((eTime - sTime)/1000)%60 + "s");
        output.close();
    }

}
