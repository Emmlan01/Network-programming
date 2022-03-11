import java.net.*;
import java.io.*;
import tcpclient.TCPClient;

public class HTTPAsk {
    public static void main( String[] args) throws IOException {
        int BUFFERSIZE = 1024;
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));

        while(true){
                boolean shutdown = false;               
                Integer timeout = null;			  
                Integer limit = null;			     
                String hostname = null;			       
                int portnumber = 0;			         
                Boolean ask = false;                   
                Boolean get = false;                    
                Boolean http = false;                   

                Socket clientSocket = serverSocket.accept();
                OutputStream out = clientSocket.getOutputStream();
                byte[] fromServerBuffer = new byte[BUFFERSIZE];
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                int fromServerLength;
                do{
                    fromServerLength = clientSocket.getInputStream().read(fromServerBuffer, 0, BUFFERSIZE);
                    output.write(fromServerBuffer, 0, fromServerLength);
                } while(fromServerLength == BUFFERSIZE);

                String s = output.toString();
                byte[] inputBytes = new byte[0];

                String[] params = s.split("[? &=/]");
                for (int i = 0; i < params.length; i++) {
                    if(params[i].equals("hostname")){
                        hostname = params[i + 1];
                    }
                    else if(params[i].equals("string")){
                        inputBytes = params[i+1].getBytes("UTF-8");
                    }
                    else if(params[i].equals("shutdown")){
                        shutdown = Boolean.parseBoolean(params[i+1]);
                    }
                    else if(params[i].equals("limit")){
                        limit = Integer.parseInt(params[i+1]);
                    }
                    else if(params[i].equals("timeout")){
                        timeout = Integer.parseInt(params[i+1]);
                    }
                    else if(params[i].equals("port")){
                        portnumber = Integer.parseInt(params[i+1]);
                    }
                    else if(params[i].equals("ask")){
                        ask = true;
                    }
                    else if(params[i].equals("GET")){
                        get = true;
                    }
                    else if(params[i].equals("HTTP")){
                        http = true;
                    }
                }
            try{
                if(ask){
                    if(hostname != null && portnumber != 0 && http && get){
                        TCPClient tcpClient = new tcpclient.TCPClient(shutdown, timeout, limit);

                        byte[] fromClient = tcpClient.askServer(hostname, portnumber, inputBytes);
                        out.write("HTTP/1.1 200 OK\r\n".getBytes());
                        out.write("Content-Type: text/plain\r\n".getBytes());
                        out.write("\r\n".getBytes());
                        out.write(new String(fromClient, 0, fromClient.length, "UTF-8").getBytes());
                    } else {
                        out.write("HTTP/1.1 400 Bad Request\r\n".getBytes());
                        out.write("Content-Type: text/html\r\n".getBytes());
                        out.write("\r\n".getBytes());
                        out.write("<h1>400 Bad Request</h1>".getBytes());
                    }
                } else {
                    out.write("HTTP/1.1 404 Not Found\r\n".getBytes());
                    out.write("Content-Type: text/html\r\n".getBytes());
                    out.write("\r\n".getBytes());
                    out.write("<h1>404 Not Found</h1>".getBytes());
                }
            } catch(UnknownHostException e){
                out.write("HTTP/1.1 400 Bad Request\r\n".getBytes());
                out.write("Content-Type: text/html\r\n".getBytes());
                out.write("\r\n".getBytes());
                out.write("<h1>400 Bad Request</h1>".getBytes());
            }
            out.flush();
            out.close();
        }
    }                        
}