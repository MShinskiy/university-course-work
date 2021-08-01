using System;

namespace Server
{

    class Trader
    {
        public Trader(int traderId, int stockAmount)
        {
            TraderId = traderId;
            StockAmount = stockAmount;
        }

        public int TraderId {get; set;}
        public int StockAmount {get; set;}
    }
}