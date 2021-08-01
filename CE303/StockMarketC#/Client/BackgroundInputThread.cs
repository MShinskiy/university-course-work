using System;
using System.Threading;

namespace Client 
{
    class BackgroundInputThread
    {
        private readonly Client client;
        private readonly int id;

        public BackgroundInputThread(Client client)
        {
            this.client = client;
            this.id = client.Id;
        }

        public void Run()
        {
            try
            {
                //Run loop to keep Thread alive
                while(true)
                {
                    string line = Console.ReadLine().Trim().ToLower();
                    switch(line)
                    {
                        case "quit":
                            client.Dispose();
                            Environment.Exit(0);
                            break;
                        case "id":
                            Console.WriteLine($"Your ID : {id}");
                            break;
                        case "rules":
                            Program.PrintRules("wait");
                            break;
                        case "update":
                            Program.PrintTraders(client, "wait");
                            break;
                        case "continue":
                            if(client.GetStockAmount() == 1)
                                return;
                            else
                                Console.WriteLine("You cannot yet proceed. Please wait...");
                            break;
                        default:
                            Console.WriteLine("Unknown command");
                            break;
                    }
                }
            }
            catch(ThreadInterruptedException)
            {
                return;
            }
        }
    }
}