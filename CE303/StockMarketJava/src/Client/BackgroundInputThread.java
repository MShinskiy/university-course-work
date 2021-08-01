package Client;

import java.util.Scanner;

public class BackgroundInputThread implements Runnable {

    private final Client client;
    private final int id;

    public BackgroundInputThread(Client client){
        this.client = client;
        this.id = client.getId();
    }

    @Override
    public void run() {
        Scanner read = new Scanner(System.in);
        //Run loop to keep Thread alive
        while (true) {
            //Inner loop to check if there is the line to read input
            while (read.hasNextLine()) {
                String line = read.nextLine().trim().toLowerCase();
                switch (line) {
                    case "quit":
                        client.close();
                        System.exit(0);
                        break;
                    case "id":
                        System.out.println("Your ID: " + id);
                        break;
                    case "rules":
                        ClientProgram.printRules("wait");
                        break;
                    case "update":
                        ClientProgram.printTraders(client, "wait");
                        break;
                    case "continue":
                        if(client.getStockAmount() == 1)
                            return;
                        else
                            System.out.println("You cannot yet proceed. Please wait...");
                        break;
                    default:
                        System.out.println("Unknown command");
                }
                if(Thread.currentThread().isInterrupted())
                    return;
            }
            if(Thread.currentThread().isInterrupted())
                return;
        }
    }
}