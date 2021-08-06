public class Exercise1 {
    public interface Dequeue<T> {
        public void addleft(T o);

        public void addright(T o);

        public void removeleft();

        public void removeright();

        public T left();

        public T right();

        public boolean isempty();

        public boolean isfull();
    }
}

class DequeueException extends RuntimeException {
    DequeueException(String s) {
        super(s);
    }
}

class ArrayDequeue<T> implements Exercise1.Dequeue<T> {

    private T[] arr;
    private int frontPos, backPos;

    public ArrayDequeue() {
        System.out.println("Deque array created");
        arr = (T[]) new Object[5];
        backPos = 0;
        frontPos = -1;
    }

    public void addleft(T o) {
        if (!isfull()) {
            if (frontPos == -1) {
                frontPos = 0;
                backPos = 0;
            } else if (frontPos == 0) frontPos = arr.length - 1;
            else frontPos--;
            arr[frontPos] = o;
            System.out.println("Adding to LEFT: " + o);
        } else throw new DequeueException("addleft(): Array is full");
    }

    public void addright(T o) {
        if (!isfull()) {
            if (frontPos == -1) {
                frontPos = 0;
                backPos = 0;
            } else if (backPos == arr.length - 1)
                backPos = 0;
            else
                backPos++;
            arr[backPos] = o;
            System.out.println("Adding to RIGHT: " + o);
        } else throw new DequeueException("addright(): Array is full");
    }

    public void removeleft() {
        if (!isempty()) {
            System.out.println("Removing from LEFT: " + left());
            if (frontPos == backPos) {
                frontPos = -1;
                backPos = -1;
            } else if (frontPos == arr.length - 1)
                frontPos = 0;
            else
                frontPos++;
        } else throw new DequeueException("removeleft(): Array is empty");
    }

    public void removeright() {
        if (!isempty()) {
            System.out.println("Removing from RIGHT: " + right());
            if (frontPos == backPos) {
                frontPos = -1;
                backPos = -1;
            } else if (backPos == 0)
                backPos = arr.length - 1;
            else
                backPos--;
        } else throw new DequeueException("removeright(): Array is empty");
    }

    public T left() {
        if(!isempty())  return arr[frontPos];
        else throw new DequeueException("left(): Array is empty");
    }

    public T right() {
        if(!isempty()) return arr[backPos];
        else throw new DequeueException("right(): Array is empty");
    }

    public boolean isempty() {
        return frontPos == -1;
    }

    public boolean isfull() {
        return (frontPos == 0 && backPos == arr.length - 1) || frontPos == backPos + 1;
    }

    public String toString() {
        if (isempty()) return "<>";
        StringBuilder sb = new StringBuilder();
        sb.append('<');
        int pos = frontPos;
        while (pos != backPos) {
            sb.append(arr[pos]);
            sb.append(',');
            pos = (pos + 1) % arr.length;
        }
        sb.append(arr[backPos]);
        sb.append('>');
        return (sb.toString());
    }
}



