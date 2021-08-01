package Server;

public class Trader {

    private int traderId;
    private int stockAmount;

    public Trader(int traderId, int stockAmount){
        this.traderId = traderId;
        this.stockAmount = stockAmount;
    }
    //Getters
    public int getTraderId(){
        return this.traderId;
    }

    public int getStockAmount(){
        return this.stockAmount;
    }

    //Setters
    public void setTraderId(int id){
        this.traderId = id;
    }

    public void setStockAmount(int amount){
        this.stockAmount = amount;
    }
}
