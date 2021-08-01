using System.Collections.Generic;

namespace Server
{
class Market
{
    private readonly SortedList<int, Trader> traders = new SortedList<int, Trader>();
    private static int traderId = 1000;

    //Create trader on the server and save it to the SortedList
    public int CreateTrader(int amount)
    {
        lock(traders)
        {
            traderId++;
            Trader trader = new Trader(traderId, amount);
            traders.Add(traderId, trader);
            return traderId;
        }
    }

    //Delete the trader from the server
    public void DropTrader(int traderId)
    {
        lock(traders)
        {
            traders.Remove(traderId);
        }
    }

    //Get a list of traders
    public List<int> GetListOfTraders()
    {
        lock(traders){
            List<int> list = new List<int>();
            foreach(Trader trader in traders.Values)
                list.Add(trader.TraderId);

            return list;
        }
    }

    //Get id of a trader that has stock
    public int getTraderIdWithStock()
    {
        lock (traders)
        {
            foreach(Trader trader in traders.Values)
            {
                if (trader.StockAmount == 1)
                    return trader.TraderId;
            }
            return 0;
        }
    }
    
    //Getters
    public Trader GetTrader(int id){ 
        lock(traders){
            return traders[id];
        }
    }
    public Trader GetFirstTrader(){ 
        lock(traders){
            return traders.Values[0];
        }
    }
    public Trader GetLastTrader(){ 
        lock(traders)
        {
            return traders.Values[traders.Count-1]; 
        }
    }

    //Method for transferring the stock
    public void Transfer(int from, int to) 
    {
        lock(traders)
        {
            if(GetTrader(from).StockAmount < 1)
                throw new TransferException("You do not own any stock :(");
            if(!GetListOfTraders().Contains(to))
                throw new TransferException("The trader specified is not on the server or  does not exist :(");
            GetTrader(from).StockAmount = 0;
            GetTrader(to).StockAmount = 1;
        }
    }
}
}