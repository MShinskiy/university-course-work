namespace Server
{
//Creating custom exception to catch it in the transfer function od the code
public class TransferException : System.Exception
{
    public TransferException() { }
    public TransferException(string message) : base(message) { }
    public TransferException(string message, System.Exception inner) : base(message, inner) { }
    protected TransferException(
        System.Runtime.Serialization.SerializationInfo info,
        System.Runtime.Serialization.StreamingContext context) : base(info, context) { }
}
}