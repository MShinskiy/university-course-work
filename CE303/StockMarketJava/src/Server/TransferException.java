package Server;

public class TransferException extends Exception {
    //Creating custom exception to catch it in the transfer function od the code
    TransferException(String message){
        super(message);
    }
}
