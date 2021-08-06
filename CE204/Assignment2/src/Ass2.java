public class Ass2 {
    public static void main(String[] args) {
        //Print welcome message
        System.out.println("Reg number 1804336");
        System.out.println("Welcome to Max's expression evaluation program. Please type an expression: ");
        Parser p = new Parser();
        String in;
        do {
            ExpTree tree = p.parseLine();
            //fill idMap with values to be assumed
            tree.fillId();

            //print in post order
            try {
                tree.postDisplay(tree);
                System.out.println("Post-order" + tree.postOrder);
                System.out.println("In-order:" + tree);
            } catch (ParseException e) {
                System.out.println(e);
            }

            //print calculation result
            try {
                System.out.println("Result: " + tree.calc(tree));
            } catch (ArithmeticException e) {
                System.out.println(e);
            } finally {
                System.out.println("Another expression (y/n)? ");
                in = p.getLine();
            }
        } while (in.equals("y"));
    }
}
