import java.util.*;
import java.net.*;
import java.io.*;

public class Client {
    String name;
    int port;
    private Socket sock = null;

    public Client(String name, int port){
        this.name = name;
        this.port = port;
        try{
            sock = new Socket(name, port);
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public void handshake(){
        try{
            PrintWriter pw = new PrintWriter(sock.getOutputStream());
            pw.println("Trying to connect");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public void disconnect(){
        try{
            sock.close();
        }catch(IOException e){
            System.out.print(e);
        }
    }
    public String request(String num){
        String reply = null;
        try{
            PrintWriter request = new PrintWriter(sock.getOutputStream());
            request.print(num);
            request.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            reply = in.readLine();
            request.close();
            in.close();
            sock.close();
        }catch(IOException e){
            System.out.print(e);
        }
        return reply;
    }

    public Socket getSocket(){
        return sock;
    }
}