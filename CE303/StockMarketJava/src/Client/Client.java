package Client;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements AutoCloseable {

    private final int port = 9999;

    private final Scanner reader;
    private final PrintWriter writer;
    private final int id;

    //Start client
    public Client() throws Exception {
        Socket socket = new Socket("localhost", port);
        reader = new Scanner(socket.getInputStream());

        writer = new PrintWriter(socket.getOutputStream(), true);

        String line = reader.nextLine();
        if (line.trim().compareToIgnoreCase("success") != 0)
            throw new Exception(line);

        this.id = Integer.parseInt(reader.nextLine());
    }


    public void give(int to) throws Exception {
        writer.println("GIVE " + to);
        String line = reader.nextLine();
        if (line.trim().compareToIgnoreCase("success") != 0)
            throw new Exception(line);
    }

    //Getters
    public int getId(){return id;}

    public int[] getTraders(){
        writer.println("UPDATE");

        String line = reader.nextLine();
        int listSize = Integer.parseInt(line);

        int[] idList = new int[listSize];
        for(int i = 0; i < listSize; i++){
            line = reader.nextLine();
            idList[i] = Integer.parseInt(line);
        }
        return idList;
    }

    public int getStockAmount(){
        writer.println("STOCK_AMOUNT");
        return Integer.parseInt(reader.nextLine());
    }

    public int getIdStock(){
        writer.println("ID_STOCK");
        return Integer.parseInt(reader.nextLine());
    }

    @Override
    public void close() {
        reader.close();
        writer.close();
    }
}
