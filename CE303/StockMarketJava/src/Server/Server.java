package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final static int port = 9999;

    private static final Market market = new Market();

    private static void run(){
        ServerSocket serverSocket;
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("Waiting for incoming connections...");
            while(true){
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket, market)).start();
            }
        } catch ( IOException ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        run();
    }
}
