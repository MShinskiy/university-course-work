package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    private final Socket socket;
    private Market market;

    public ClientHandler(Socket socket, Market market){
        this.socket = socket;
        this.market = market;
    }

    //Method lists all the traders on the server
    private void displayTraders(List<Integer> idList){
        String border = "+------+-------+------+";

        System.out.println("Number of traders on the market: " + idList.size());
        //Header
        System.out.println(border);
        System.out.println("|№     |ID     |Stocks|");
        System.out.println(border);
        //Body
        for(int i = 1; i < idList.size() + 1; i++){
            Trader tr = market.getTrader(idList.get(i-1));
            String no = String.format("%5d", i);
            String id = String.format("%7d", tr.getTraderId());
            String stock = String.format("%6d", tr.getStockAmount());
            System.out.println("|" + no + ".|" + id + "|" + stock + "|");
            System.out.println(border);
        }
        System.out.println();
    }

    @Override
    public void run() {
        int traderId = 0;
        try(
                Scanner scanner = new Scanner(socket.getInputStream());
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            try {
                writer.println("SUCCESS"); //Inform trader that connection established

                //if a new trader is first on the server, grant him with a stock
                if( market.getListOfTraders().size() == 0)
                    traderId = market.createTrader(1);
                else
                    traderId = market.createTrader(0);
                System.out.println("New connection; trader ID " + traderId);

                writer.println(traderId); //Send ID that a trader has been assigned to

                displayTraders(market.getListOfTraders());

                while (true) {
                    String line = scanner.nextLine();
                    String[] substrings = line.split(" ");
                    switch (substrings[0].toLowerCase()){
                        case "give":
                            int beneficiary = Integer.parseInt(substrings[1]);
                            try {
                                market.transfer(traderId, beneficiary);
                                if(market.getTrader(beneficiary) == null) {
                                    market.getTrader(traderId).setStockAmount(1);
                                    throw new TransferException("Trader disconnected while transfer operation");
                                }

                                displayTraders(market.getListOfTraders());
                                System.out.println(traderId + " -----> " + beneficiary);
                                writer.println("SUCCESS");
                            } catch (TransferException te){
                                System.out.println(traderId + " --X--> " + beneficiary);
                                writer.println(te.getMessage());
                            }
                            break;
                        case "update":
                            List<Integer> listOfTraders = market.getListOfTraders();
                            writer.println(listOfTraders.size());
                            for( Integer id : listOfTraders)
                                writer.println(id);
                            break;
                        case "stock_amount":
                            writer.println(market.getTrader(traderId).getStockAmount());
                            break;
                        case "id_stock":
                            int id = market.getTraderIdWithStock();
                            if(id != 0)
                                writer.println(id);
                            else
                                writer.println("Error occurred while retrieving traders with stock");
                            break;
                        default:
                            throw new Exception("Unknown command: " + substrings[0]);
                    }
                }
            } catch (Exception e){
                writer.println("Error occurred: " + e.getMessage());
            }
        } catch (Exception ignored){
        } finally {
            System.out.println("Trader " + traderId + " disconnected.");

            /*
            if the trader that leaves has a stock and there are other traders left on the market
                then if the first trader in the list is not the trader that leaves now
                    grant that trader with a stock
                else grant last trader in the list with a stock
             */
            if(market.getTrader(traderId).getStockAmount() > 0 && market.getListOfTraders().size() > 1) {
                Trader beneficiary;
                if (!market.getTrader(traderId).equals(market.getFirstTrader())) {
                    beneficiary = market.getFirstTrader();
                }else{
                    beneficiary = market.getLastTrader();
                }
                beneficiary.setStockAmount(1);
                System.out.println("Trader with id: " + beneficiary.getTraderId() + " has been granted a stock");
            }

            //exclude the trader from the list and print the rest
            market.dropTrader(traderId);
            displayTraders( market.getListOfTraders());
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}