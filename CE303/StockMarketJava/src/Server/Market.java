package Server;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Market {

    private static final TreeMap<Integer, Trader> traders = new TreeMap<>();
    private int traderId = 1000;

    //Create trader on the server and save it to TreeMap
    public int createTrader(int amount){
        synchronized (traders) {
            traderId++;
            Trader trader = new Trader(traderId, amount);
            traders.put(traderId, trader);
            return traderId;
        }
    }

    //Delete trader form server
    public void dropTrader(int traderId){
        synchronized (traders) {
            traders.remove(traderId);
        }
    }

    //Get a list of traders
    public List<Integer> getListOfTraders(){
        synchronized (traders) {
            List<Integer> list = new ArrayList<>();
            for (Trader trader : traders.values())
                list.add(trader.getTraderId());
            return list;
        }
    }

    //Get id of a trader that has stock
    public int getTraderIdWithStock(){
        synchronized (traders){
            for(Trader trader : traders.values()) {
                if (trader.getStockAmount() == 1)
                    return trader.getTraderId();
            }
            return 0;
        }
    }

    //Getters
    public Trader getFirstTrader(){
        synchronized (traders) {
            return traders.get(traders.firstKey());
        }
    }

    public Trader getLastTrader(){
        synchronized (traders) {
            return traders.get(traders.lastKey());
        }
    }

    public Trader getTrader(int id){
        synchronized (traders) {
            return traders.get(id);
        }
    }

    //Method for transferring the stock
    public void transfer(int from, int to) throws TransferException, InterruptedException {
        synchronized (traders){
            if(getTrader(from).getStockAmount() < 1)
                throw new TransferException("You do not own any stock :(");
            if(!getListOfTraders().contains(to))
                throw new TransferException("The trader specified is not on the server or does not exist :(");
            getTrader(from).setStockAmount(0);
            getTrader(to).setStockAmount(1);
        }
    }
}
