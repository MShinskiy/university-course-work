package Client;

import java.util.Scanner;

public class ClientProgram {

    public static void main(String[] args) {
        try{
            Scanner in = new Scanner(System.in);

            try(Client client = new Client()){
                //Start up message for user
                System.out.println("Logged in successfully.");
                System.out.println("Your unique ID: " + client.getId());

                while(true){
                    //Wait if have no stock
                    if(client.getStockAmount() == 0){
                        //Create a thread to read input while this thread waits
                        Runnable input = new BackgroundInputThread(client);
                        Thread inputThread = new Thread(input);
                        inputThread.start();

                        printRules("wait");
                        printTraders(client, "wait");

                        //Get updates from the server every 500ms
                        do{
                            if(client.getStockAmount() != 0){
                                //Wait until client obtains a stock and client enters "continue", so thread dies
                                System.out.println("Stock obtained.");
                                System.out.println("Enter \"continue\" to proceed.");
                                inputThread.join();
                                break;
                            }
                            Thread.sleep(500);
                        } while(true);
                    }

                    printTraders(client, "active");
                    printRules("active");

                    while(client.getStockAmount() == 1) {
                        String id = in.nextLine();
                        //Try perform transfer, otherwise check for other commands
                        try {
                            client.give(Integer.parseInt(id));
                        } catch (NumberFormatException nfe) {
                            switch (id.toLowerCase()) {
                                case "update":
                                    printTraders(client, "active");
                                    break;
                                case "quit":
                                    client.close();
                                    System.exit(0);
                                    break;
                                case "id":
                                    System.out.println("Your ID is: " + client.getId());
                                    break;
                                case "rules":
                                    printRules("active");
                                    break;
                                default:
                                    System.out.println("Unknown command or ID has to be a number.");
                                    break;
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage() + "\n");
                        }
                    }
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    //Print available commands
    public static void printRules(String flag){
        String line = "";
        if(flag.equals("wait"))
            line = "|Please wait...                                     |";
        else if(flag.equals("active"))
            line = "|Enter valid ID of the trader to transfer the stock;|";
        System.out.println("\n+---------------------------------------------------+");
        System.out.println(line);
        System.out.println("|Enter \"update\" to update a list of traders;        |");
        System.out.println("|Enter \"id\" to print your ID;                       |");
        System.out.println("|Enter \"rules\" to print available commands;         |");
        System.out.println("|Enter \"quit\" to leave the Market.                  |");
        System.out.println("+---------------------------------------------------+");
    }

    //Print traders on the server
    //Prints little different depending on flag
    public static void printTraders(Client client, String flag){
        var tradersList = client.getTraders();

        int traderWithStock = 0;
        if(flag.equals("wait"))
            traderWithStock = client.getIdStock();

        //Once the stock is obtained, print the list of traders
        if(tradersList.length <= 1) {
            //Different messages depending on the stage
            if(flag.equals("wait"))
                System.out.println("You are the only trader on the market");
            else if(flag.equals("active")) {
                System.out.println("No available traders at the moment.");
                System.out.println("However, you may give a stock to yourself.");
            }
        } else {
            System.out.println("\nList of traders:");
            int count = 0;

            String border = "";
            String header = "";
            if(flag.equals("wait")) {
                border = "+------+-------+-----+";
                header = "|№     |ID     |Stock|";
            }else if(flag.equals("active")) {
                border = "+------+-------+";
                header = "|№     |ID     |";
            }

            //Header
            System.out.println(border);
            System.out.println(header);
            System.out.println(border);
            //Body
            for (int id : tradersList) {
                //Skip the client and print OTHER traders
                if (id == client.getId())
                    continue;

                int stk = 0;
                if(flag.equals("wait") && id == traderWithStock) {
                    //if this trader has stock, set stock int to 1
                    stk = 1;
                }

                //Build a table with columns
                String no  = String.format("%5d", ++count);
                String idS = String.format("%7d", id);

                String line = "";
                if(flag.equals("wait")){
                    String stkS = String.format("%5d", stk);
                    line = "|" + no + ".|" + idS + "|" + stkS + "|";
                } else if(flag.equals("active"))
                    line = "|" + no + ".|" + idS + "|";

                System.out.println(line);
                System.out.println(border);
            }
        }
    }

}
