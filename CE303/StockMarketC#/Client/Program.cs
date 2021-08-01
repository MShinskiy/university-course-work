using System;
using System.Threading;

namespace Client
{
    class Program
    {
        static void Main(string[] args)
        {
            try
            {
                using(Client client = new Client())
                {
                    //Start up message for user
                    Console.WriteLine("Logged in successfully.");
                    Console.WriteLine($"Your unique ID: {client.Id}");
                    while(true)
                    {
                        //Wait if have no stock
                        if(client.GetStockAmount() == 0){
                            //Create a thread to read input while this thread waits
                            BackgroundInputThread bgit = new BackgroundInputThread(client);
                            Thread inputThread = new Thread(bgit.Run);
                            inputThread.Start(); 

                            PrintRules("wait");
                            PrintTraders(client, "wait");

                            //Get updates from the server every 500ms
                            do{
                                if(client.GetStockAmount() != 0)
                                {
                                    //Wait until client obtains a stock and client enters "continue", so thread dies 
                                    Console.WriteLine("Stock obtained.");
                                    Console.WriteLine("Enter \"continue\" to proceed.");
                                    inputThread.Join();
                                    break;
                                }
                                Thread.Sleep(500);
                            }
                            while(true);
                        }

                        PrintTraders(client, "active");
                        PrintRules("active");

                        while(client.GetStockAmount() == 1)
                        {
                            String id = Console.ReadLine();
                            //Try perform transfer, otherwise check for other commands
                            try
                            {
                                client.Give(int.Parse(id));
                            }
                            catch(FormatException)
                            {
                                switch(id.ToLower())
                                {
                                    case "update":
                                        PrintTraders(client, "active");
                                        break;
                                    case "quit":
                                        client.Dispose();
                                        Environment.Exit(0);
                                        break;
                                    case "id":
                                        Console.WriteLine($"Your ID is: {client.Id}");
                                        break;
                                    case "rules":
                                        PrintRules("active");
                                        break;
                                    default:
                                        Console.WriteLine("Unknown command or ID has to be a number.");
                                        break;
                                }
                            }
                            catch(Exception e)
                            {
                                Console.WriteLine($"{e.Message} \n");
                            }
                        }
                    }
                }
            } 
            catch(Exception e)
            {
                Console.WriteLine(e.Message);
            }

        }
        
        //Print available commands
        public static void PrintRules(String flag)
        {
            String line = "";
            if(flag.Equals("wait"))
                line = "|Please wait...                                     |";
            else if(flag.Equals("active"))
                line = "|Enter valid ID of the trader to transfer the stock;|";

            Console.WriteLine("\n+---------------------------------------------------+");
            Console.WriteLine(line);
            Console.WriteLine("|Enter \"update\" to update a list of traders;        |");
            Console.WriteLine("|Enter \"id\" to print your ID;                       |");
            Console.WriteLine("|Enter \"rules\" to print available commands;         |");
            Console.WriteLine("|Enter \"quit\" to leave the Market.                  |");
            Console.WriteLine("+---------------------------------------------------+");
        }

        //Print available traders
        public static void PrintTraders(Client client, String flag)
        {
            var tradersList = client.GetTraders();

            int traderWithStock = 0;
            if(flag.Equals("wait"))
                traderWithStock = client.GetIdStock();

            //Once the stock is obtained, print the list of traders
            if(tradersList.Length <= 1)
            {
                if(flag.Equals("wait"))
                    Console.WriteLine("You are the only trader on the market");
                else if(flag.Equals("active"))
                {
                    Console.WriteLine("No available traders at the moment.");
                    Console.WriteLine("However, you may give a stock to yourself.");
                }
            }
            else
            {
                Console.WriteLine("\nList of available traders:");
                int count = 0;

                String border = "";
                String header = "";
                if(flag.Equals("wait"))
                {
                    border = "+------+-------+-----+";
                    header = "|№     |ID     |Stock|";
                }
                else if(flag.Equals("active"))
                {
                    border = "+------+-------+";
                    header = "|№     |ID     |";
                }

                //Header
                Console.WriteLine(border);
                Console.WriteLine(header);
                Console.WriteLine(border);
                //Body
                foreach(int id in tradersList)
                {
                    //Skip the client and print OTHER traders
                    if(id == client.Id)
                        continue;

                    int stk = 0;
                    if(flag.Equals("wait") && id == traderWithStock)
                    {
                        stk = 1;
                    }

                    String line = "";
                    if(flag.Equals("wait"))
                    {
                        line = $"|{++count + ".", 6}|{id, 7}|{stk, 5}|";
                    }
                    else if(flag.Equals("active"))
                    {
                        line = $"|{++count + ".",6}|{id,7}|";
                    }
                    Console.WriteLine(line);
                    Console.WriteLine(border);
                }
            }
        }
    }
}
