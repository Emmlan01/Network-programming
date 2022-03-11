package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {

    

    public TCPClient() {
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        
        byte [] fromServerBuffer = new byte[1024];
        Socket clientSocket = new Socket(hostname, port); 
        clientSocket.getOutputStream().write(toServerBytes); 
        ByteArrayOutputStream bytesToServer = new ByteArrayOutputStream();
        
        while(true){
        int byteReturned = clientSocket.getInputStream().read(fromServerBuffer, 0, fromServerBuffer.length);    //returnerar en input stream för clientsocket. Där den läser data Reads up to len bytes of data from the input stream into an array of bytes.
        if(byteReturned == -1)
        break;
        bytesToServer.write(fromServerBuffer, 0, byteReturned);
        }
        clientSocket.close();
        return bytesToServer.toByteArray();
    }
}
