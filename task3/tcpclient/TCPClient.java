package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {

    public static Integer BUFFERSIZE = 1024;
    public static Integer timeout;
    public static Integer limit;
    public static boolean shutdown;


    public TCPClient(boolean shutdown, Integer timeout, Integer limit) {

        this.timeout = timeout;
        this.shutdown = shutdown;
        this.limit = limit;

        if (limit != null){                         //om limit inte är null så sätter vi limit till den buffersize som vi anget. 
            BUFFERSIZE = limit;
        }
            else {
            limit = 1024;
        }
    }
    
    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        Socket clientSocket = new Socket(hostname, port);        //skapar en clientsocket som vi binder till hostname och port  
                 
       // clientSocket.getOutputStream().write(toServerBytes);            //Skriver toserverbytes till serven.
        ByteArrayOutputStream out = new ByteArrayOutputStream();                    //Lagra dynamiskt.                                                  
        byte[] fromServerBuffer = new byte[BUFFERSIZE];   //varje omgång tas det emot 1024 bytes 
        //clientSocket.setSoTimeout(timeout);
        try{
            if(timeout != null) {clientSocket.setSoTimeout(this.timeout);}
            clientSocket.getOutputStream().write(toServerBytes);

             if(this.shutdown == true){           //denna gör och vi tar aldrig emot. 
                clientSocket.shutdownOutput();
            }  
            if (limit != null){
                int fromServerLength = clientSocket.getInputStream().read(fromServerBuffer); 
                out.write(fromServerBuffer, 0, fromServerLength);
            }  else {                                                                    
                while (true){
                    int fromServerLength = clientSocket.getInputStream().read(fromServerBuffer);            //Här läser den och tar emot 1024 bytes
                    if (fromServerLength == -1)
                    break;
                    out.write(fromServerBuffer, 0, fromServerLength);                           //HÄr skriver dne ut dessa 1024 bytes till dynamiska listan
                }
            }
            clientSocket.close();  
            } catch(SocketTimeoutException e){
            return out.toByteArray();
            }                                         
        return out.toByteArray();               //returnerar listan.
    }
}
