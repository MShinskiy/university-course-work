using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Threading;

namespace Server{
class Program{

    private const int port = 9999;

    private static readonly Market market = new Market();
    public static void Main(string[] args)
    {
        RunServer();
    }

    private static void RunServer()
    {
        TcpListener listener = new TcpListener(IPAddress.Loopback, port);
        listener.Start();
        Console.WriteLine("Waiting for incoming connections...");
        while(true)
        {
            TcpClient tcpClient = listener.AcceptTcpClient();
            new Thread(HandleIncomingConnection).Start(tcpClient);
        }
    }

    //Lists all the traders on the server
    private static void DisplayTraders(int[] idList)
    {
        String border = "+-----+-------+------+";
        
        Console.WriteLine($"Number of traders on the market: {idList.Length}");
        //Header
        Console.WriteLine(border);
        Console.WriteLine("|№    |ID     |Stocks|");
        Console.WriteLine(border);
        //Body
        for(int i = 1; i < idList.Length + 1; i++)
        {
            Trader tr = market.GetTrader(idList[i-1]);
            Console.WriteLine($"|{i + ".", 5}|{tr.TraderId, 7}|{tr.StockAmount, 6}|");
            Console.WriteLine(border);
        }
        Console.WriteLine();
    }

    private static void HandleIncomingConnection(object param)
    {
        TcpClient tcpClient = (TcpClient) param;
        using (Stream stream = tcpClient.GetStream())
        {
            StreamWriter writer = new StreamWriter(stream);
            StreamReader reader = new StreamReader(stream);
            writer.AutoFlush = true;
            int traderId = 0;
            try
            {
                writer.WriteLine("SUCCESS");

                //if a new trader is first on the server, grant him with a stock
                if(market.GetListOfTraders().Count == 0)
                    traderId = market.CreateTrader(1);
                else 
                    traderId = market.CreateTrader(0);

                Console.WriteLine($"New connection; trader ID: {traderId}");

                writer.WriteLine(traderId);//send ID that a trader has been assignet to

                DisplayTraders(market.GetListOfTraders().ToArray());

                while(true)
                {
                    string line = reader.ReadLine();
                    string[] substrings = line.Split(' ');
                    switch(substrings[0].ToLower())
                    {
                        case "give":
                            int beneficiary = int.Parse(substrings[1]);
                            try
                            {
                                market.Transfer(traderId, beneficiary);
                                if(market.GetTrader(beneficiary) == null)
                                {
                                    market.GetTrader(traderId).StockAmount = 1;
                                    throw new TransferException("Trader disconnected while transfer operation");
                                }

                                DisplayTraders(market.GetListOfTraders().ToArray());
                                Console.WriteLine($"{traderId} -----> {beneficiary}");
                                writer.WriteLine("SUCCESS");
                            } 
                            catch(TransferException te)
                            {
                                Console.WriteLine($"{traderId} --X--> {beneficiary}");
                                writer.WriteLine(te.Message);
                            }
                            break;
                        case "update":
                            int[] listOfTraders = market.GetListOfTraders().ToArray();
                            writer.WriteLine(listOfTraders.Length);
                            foreach( int trId in listOfTraders)
                                writer.WriteLine(trId);
                            break;
                        case "stock_amount":
                            writer.WriteLine(market.GetTrader(traderId).StockAmount);
                            break;
                        case "id_stock":
                            int id = market.getTraderIdWithStock();
                            if(id != 0)
                                writer.WriteLine(id);
                            else 
                                writer.WriteLine("Error occurred while retrieving traders with stock");
                            break;
                        default:
                            throw new Exception($"Unknown command: {substrings[0]}");
                    }
                }
            }
            catch (Exception e)
            {
                try
                {
                    writer.WriteLine("ERROR " + e.Message);
                }
                catch
                {
                    Console.WriteLine("Failed to send error message.");
                }
            }
            finally
            {
                Console.WriteLine($"Customer {traderId} disconnected.");
                /*
                if the trader that leaves has a stock and there are other traders left on the market
                    then if the first trader in the list is not the trader that leaves now
                        grant that trader with a stock
                    else grant last trader in the list with a stock
                */
                if(market.GetTrader(traderId).StockAmount > 0 && market.GetListOfTraders().Count > 1)
                {
                    Trader beneficiary;
                    if(!market.GetTrader(traderId).Equals(market.GetFirstTrader()))
                        beneficiary = market.GetFirstTrader();
                    else
                        beneficiary = market.GetLastTrader();
                    beneficiary.StockAmount = 1;
                    Console.WriteLine($"Trader with id: {beneficiary.TraderId} has been granted a stock");
                }
                
                //Exclude the trader from the list and print the rest
                market.DropTrader(traderId);
                DisplayTraders(market.GetListOfTraders().ToArray());
                tcpClient.Close();   
            }
        }
    }
}
}