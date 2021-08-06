import java.util.TreeMap;

public class ExpTree {
    String postOrder = "";
    String inOrder = "";
    public int kind;
    public Object value;
    public ExpTree lChild, rChild;
    public TreeMap<String, Integer> idMap = new TreeMap<>();
    public static final int
            numNode = 0,
            idNode = 1,
            opNode = 2,
            letNode = 3,
            andNode = 4,
            eqNode = 5;

    //Constructor
    public ExpTree(int knd, Object val, ExpTree l, ExpTree r) {
        kind = knd;
        value = val;
        lChild = l;
        rChild = r;
    }

    //fills the idMap with the values to be assumed
    public void fillId() {
        idMap.put("A", 25);
        idMap.put("B", 24);
        idMap.put("C", 23);
        idMap.put("D", 22);
        idMap.put("E", 21);
        idMap.put("F", 20);
        idMap.put("G", 19);
        idMap.put("H", 18);
        idMap.put("I", 17);
        idMap.put("J", 16);
        idMap.put("K", 15);
        idMap.put("L", 14);
        idMap.put("M", 13);
        idMap.put("N", 12);
        idMap.put("O", 11);
        idMap.put("P", 10);
        idMap.put("Q", 9);
        idMap.put("R", 8);
        idMap.put("S", 7);
        idMap.put("T", 6);
        idMap.put("U", 5);
        idMap.put("V", 4);
        idMap.put("W", 3);
        idMap.put("X", 2);
        idMap.put("Y", 1);
        idMap.put("Z", 0);
    }

    //Calculates the value of an expression
    public int calc(ExpTree tree) {
        if (tree.kind == numNode) return (int) tree.value;
            //Get the value from the idMap dedicated to this id
        else if (tree.kind == idNode) return idMap.get(tree.value);

        else {
            int left = calc(tree.lChild), right = calc(tree.rChild);

            //if/elseif statement for each operand
            //performs the operation
            if (tree.value.equals('+')) return left + right;
            else if (tree.value.equals('-')) return left - right;
            else if (tree.value.equals('*')) return left * right;

            else if (tree.value.equals('/'))
                if (right != 0) return left / right;
                else throw new ArithmeticException("Divisor is 0");

            else if (tree.value.equals('^'))
                if (right > 0) return (int) Math.pow(left, right);
                else throw new ArithmeticException("The power is -ve");

            else if (tree.value.equals('%'))
                if (right != 0) return left % right;
                else throw new ArithmeticException("Divisor is 0");

            else return 0;
        }
    }

    //Modifies global string to be a post order traversal output
    public void postDisplay(ExpTree tree) {
        if (tree != null) {
            postDisplay(tree.lChild);
            postDisplay(tree.rChild);
            postOrder += " " + tree.value;
        }
    }

    //Modifies default printing to user's specifications
    public String toString() {
        return toString(this);
    }

    //Helper method to print the string in in-order with parenthesis
    public String toString(ExpTree tree) {
        if (tree == null) return "";
        if (tree.kind == opNode) {
            /*
            Parentheses are needed around a + or - node when it is a child of a *, /, % or ^ node
             */
            if ((tree.value.equals('*') || tree.value.equals('/') || tree.value.equals('%') || tree.value.equals('^'))
                    &&
                    (tree.lChild.value.equals('-') || tree.lChild.value.equals('+')))
                return '(' + toString(tree.lChild) + tree.value.toString() + toString(tree.rChild) + ')';
            /*
            and also when it is the right child of another + or -node
            */
            else if ((tree.value.equals('-') || tree.value.equals('+'))
                    &&
                    (tree.rChild.value.equals('-') || tree.rChild.value.equals('+')))
                return '(' + toString(tree.lChild) + tree.value.toString() + toString(tree.rChild) + ')';
            /*
            Parentheses are needed around a *, /  or % node when it is a child of a ^ node
            */
            else if ((tree.value.equals('^'))
                    &&
                    (tree.lChild.value.equals('*') || tree.lChild.value.equals('/') || tree.lChild.value.equals('%')))
                return '(' + toString(tree.lChild) + tree.value.toString() + toString(tree.rChild) + ')';
            /*
            and also when it is the right child of another *, /  or % node
             */
            else if (((tree.value.equals('*') || tree.value.equals('/') || tree.value.equals('%'))
                    &&
                    (tree.rChild.value.equals('*') || tree.rChild.value.equals('/') || tree.rChild.value.equals('%'))))
                return '(' + toString(tree.lChild) + tree.value.toString() + toString(tree.rChild) + ')';
            /*
             Parentheses are needed around a ^ node only when it is the left child of another ^ node
            */
            else if (tree.value.equals('^')
                    &&
                    tree.lChild.value.equals('^'))
                return '(' + toString(tree.lChild) + tree.value.toString() + toString(tree.rChild) + ')';

        }
        //Parentheses are not needed
        return toString(tree.lChild) + tree.value.toString() + toString(tree.rChild);
    }
}
