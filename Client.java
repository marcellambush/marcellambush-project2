import java.io.*;
import java.net.*;

public class Client{
    private String name; //client variables 
    private int port;
    private Socket socket;

    public Client(String name, int port){ //constructor + initialize varaibles for each instance
        this.name = name;
        this.port = port;
        try{
            this.socket = new Socket(name, port); //create socket connection
        }catch(Exception e){ //error handling
            System.out.println(e);
        }
    }

    public void handshake(){ 
        try{
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true); //create pw to send info
            pw.println("12345"); //send passcode
        }catch(Exception e){ //error handling
            System.out.println(e);
        }
    }

    public String request(String num){ //send request to server
        String reply = null;
        try{
            PrintWriter request = new PrintWriter(socket.getOutputStream(), true); //create pw to send info
            request.println(num);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //input to read
            reply = in.readLine(); //read response
            in.close(); //close input stream
        }catch(IOException e){ //error handling
            System.out.print(e);
        }
        return reply; //return response
    }

    public void disconnect(){ //disconnect from server
        try{
            socket.close(); //close socket
        }catch(IOException e){ //error handling
            System.out.print(e);
        }
    }

    public Socket getSocket(){ //return socket if requested
        return socket;
    }
}