class BST {
    /**
     * Added methods:
     * nonleaves()
     * --count()
     * depth()
     * --maxDepth()
     * range()
     * --inrange()
     * display()
     *
     * Added variables:
     * int count
     *
     * Every method has a support method with a recurrent relation, so it calls it self
     *
     * I also change the structure of the BST class so it is more readable for me.
     * I HAVEN'T change anything inside of it, but only indentation an stuff like this.
    */
    public BTNode<Integer> root;
    public int count = 0; //Supporting global variable for nonleaves method

    public BST() {
        root = null;
    }

    public boolean find(Integer i) {
        BTNode<Integer> n = root;
        boolean found = false;

        while (n != null && !found) {
            int comp = i.compareTo(n.data);
            if (comp == 0) found = true;
            else if (comp < 0) n = n.left;
            else n = n.right;
        }
        return found;
    }

    public boolean insert(Integer i) {
        BTNode<Integer> parent = root, child = root;
        boolean goneLeft = false;

        while (child != null && i.compareTo(child.data) != 0) {
            parent = child;
            if (i.compareTo(child.data) < 0) {
                child = child.left;
                goneLeft = true;
            } else {
                child = child.right;
                goneLeft = false;
            }
        }

        if (child != null) return false;  // number already present
        else {
            BTNode<Integer> leaf = new BTNode<Integer>(i);
            if (parent == null) root = leaf;// tree was empty
            else if (goneLeft) parent.left = leaf;
            else parent.right = leaf;
            return true;
        }
    }
    //nonleaves method and support method//---------------------------------------|
    public int nonleaves() {
        return count(root);
    }

    public int count(BTNode n) {
        if (n == null || (n.left == null && n.right == null)) return 0;
        return 1 + count(n.left) + count(n.right);
    }
    //----------------------------------------------------------------------------|
    //depth method and support method//-------------------------------------------|
    public int depth() {
        BTNode<Integer> n = root;
        if (n == null) return 0;
        else return maxDepth(n);
    }

    public int maxDepth(BTNode n) {
        if (n == null) return 0;
        return 1 + Math.max(maxDepth(n.left), maxDepth(n.right));
    }
    //----------------------------------------------------------------------------|
    //range method and support method//-------------------------------------------|
    public int range(int min, int max) {
        if (min > max) throw new IllegalArgumentException("min > max");
        count = 0;
        inrange(root, min, max);
        return count;
    }

    public void inrange(BTNode n, int min, int max) {
        if (n != null) {
            inrange(n.left, min, max);
            if ((int) n.data > min && (int) n.data <= max) count++;
            inrange(n.right, min, max);
        }
    }
    //----------------------------------------------------------------------------|
    //display method to print the BST//-------------------------------------------|
    public void display(BTNode root) {
        if (root != null) {
            display(root.left);
            System.out.print(" " + root.data);
            display(root.right);
        }
    }
    //----------------------------------------------------------------------------|
}

class BTNode<T> {
    T data;
    BTNode<T> left, right;

    BTNode(T o) {
        data = o;
        left = right = null;
    }
}

class Test {
    public static void main(String[] args) {
        BST a = new BST();
        a.insert(50);
        a.insert(45);
        a.insert(25);
        a.insert(260);
        a.insert(546);
        a.insert(650);
        a.insert(60);
        a.insert(65);
        a.display(a.root);
        System.out.println();
        System.out.println("depth: " + a.depth());
        System.out.println("non leaves: " + a.nonleaves());
        System.out.println("range: 40 -> 565: " + a.range(40, 565));
        System.out.println("range: 0 -> 500: " + a.range(0, 500));
        System.out.println("range: 500 -> 0: " + a.range(500, 0));
    }
}

