using System;
using System.IO;
using System.Net.Sockets;

namespace Client
{
    class Client : IDisposable
    {
        const int port = 9999;

        private readonly StreamReader reader;
        private readonly StreamWriter writer;

        private readonly int id;

        //Start client
        public Client()
        {
            TcpClient tcpClient = new TcpClient("localhost", port);
            NetworkStream stream = tcpClient.GetStream();
            reader = new StreamReader(stream);
            writer = new StreamWriter(stream);
            writer.AutoFlush = true;

            String line = reader.ReadLine();
            if (line.Trim().ToLower() != "success")
                throw new Exception(line);

            this.id = int.Parse(reader.ReadLine());
            Id = id;
        }

        public int Id{get;}

        public void Give(int to)
        {
            writer.WriteLine($"GIVE {to}");
            String line = reader.ReadLine();
            if (line.Trim().ToLower() != "success")
                throw new Exception(line);
        }

        public int[] GetTraders()
        {
            writer.WriteLine("UPDATE");

            String line = reader.ReadLine();
            int listSize = int.Parse(line);

            int[] idList = new int[listSize];
            for(int i = 0; i < listSize; i++)
            {
                line = reader.ReadLine();
                idList[i] = int.Parse(line);
            }
            return idList;
        }

        public int GetStockAmount()
        {
            writer.WriteLine("STOCK_AMOUNT");
            return int.Parse(reader.ReadLine());
        }

        public int GetIdStock()
        {
            writer.WriteLine("ID_STOCK");
            return int.Parse(reader.ReadLine());
        }

        public void Dispose()
        {
            reader.Close();
            writer.Close();
        }
    }
}