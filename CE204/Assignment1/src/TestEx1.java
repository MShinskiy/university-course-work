public class TestEx1 {
    /**
     * This is a test class for Exercise1
     * Here I have 7 try-catch blocks
     * 1st, check the behavior when ask for an item from left in an empty array. (Exception expected)
     * 2nd, check the behavior when ask for an item from right in an empty array. (Exception expected)
     * 3rd, check the behavior when removing from an empty array. (Exception expected)
     * 4th, fill an array.
     * 5th, overfill an array (Exception expected), as I don't transfer a full array to a new array.
     * 6th, check removeleft() method behavior.
     * 7th, check removeright() method behavior.
     * After each try-catch block there is print into console line to print a current array content,
     * and a comment with expected output.
     *
     * The test class assumes that the size of an array is 5.
     * An array
    */
    public static void main(String[] args) {
        ArrayDequeue ad = new ArrayDequeue();
        try{
            //ask for item from left in empty array
            ad.left();
        } catch (DequeueException de) {
            //has to throw an exception because array is empty
            System.out.println(de);
        }
        System.out.println(ad); //<>

        try{
            //ask for item from right in empty array
            ad.right();
        } catch (DequeueException de) {
            //has to throw an exception because array is empty
            System.out.println(de);
        }
        System.out.println(ad); //<>

        try{
            //remove from empty array
            ad.removeleft();
        } catch (DequeueException de) {
            //has to throw an exception because removing from an empty array
            System.out.println(de);
        }
        System.out.println(ad); //<>

        try{
            //fill an array
            ad.addleft(1);  //<1>
            ad.addright("two"); //<1,two>
            ad.addright(1); //<1,two,1>
            ad.addleft(0);  //<0,1,two,1>
            ad.addright(0); //<0,1,two,1,0>

        } catch (DequeueException de) {
            //shouldn't cause any exceptions
            System.out.println(de);
        }
        System.out.println("Output: " + ad); //<0,1,two,1,0>

        try{
            //overfill an array
            ad.addleft(0);
        } catch (DequeueException de) {
            //has to throw an exception because adding to a full array
            System.out.println(de);
        }
        System.out.println("Output: " + ad); //<0,1,two,1,0>

        try{
            //remove from left
            ad.removeleft();
        } catch (DequeueException de) {
            //shouldn't cause any exceptions
            System.out.println(de);
        }
        System.out.println("Output: " + ad); //<1,two,1,0>

        try{
            //remove from right
            ad.removeright();
        } catch (DequeueException de) {
            //shouldn't cause any exceptions
            System.out.println(de);
        }
        System.out.println("Output: " + ad); //<1,two,1>
    }
}
