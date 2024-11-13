import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;

public class Server {
    private int port; //create varaibles 
    private ServerSocket serverSocket;
    private ArrayList<Socket> clientSockets;
    private ArrayList<LocalDateTime> connectedTimes;
    
    public Server(int port){ //server constructor and initialization of varaibles for every instance
        this.port = port;
        clientSockets = new ArrayList<Socket>();
        connectedTimes = new ArrayList<LocalDateTime>();
        try{
            serverSocket = new ServerSocket(port); //tries to create new server socket for connections
        }catch(IOException e){ //exception
            System.out.println(e);
        }
    }
    
    private class ClientHandler extends Thread{ //client handler used in serve
        private Socket clientSocket;
    
        public ClientHandler(Socket socket){ //constructor
            this.clientSocket = socket;
        }
        
        public void run(){
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //set up new input stream
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); //setup new output streat
                String handshake = in.readLine(); //read message
                if ("12345".equals(handshake) != true){ //check if code is correct if not
                    out.println("couldn't handshake"); //fail
                    clientSocket.close(); //close
                    return;
                }
                String request = null; 
                while ((request = in.readLine()) != null) { //as long as the request isnt null
                    String response = processRequest(request); //call on processRequest
                    out.println(response); //print response
                }
                
            }catch(IOException e){ //error handling
                System.out.println(e);
            }
        }
    }
    
    public void serve(int numClients){ //accepting a specific number of clients
        try {
            for (int i = 0; i < numClients; i++){  
                Socket clientSocket = serverSocket.accept(); //accept connection
                clientSockets.add(clientSocket); //add to list of connections
                connectedTimes.add(LocalDateTime.now()); //add time connected
                (new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public void disconnect(){ //disconnects all clients
        try {
            for (int i = 0; i < clientSockets.size(); i++){ //loop through
                Socket clientSocket = clientSockets.get(i); //obtain clientSocket
                clientSocket.close(); //close clientSocket
            }
            serverSocket.close(); //close entire server socket
        }catch(IOException e){
            System.out.println(e); //error handling
        }
    }
    
    public ArrayList<LocalDateTime> getConnectedTimes(){ //returns arrayList of connectedTimess
        return connectedTimes;
    }
    
    public String processRequest(String num){ //processes request
        Scanner scanner = new Scanner(num); //creates a new scanner
        if (scanner.hasNextInt() == true){
            int n = scanner.nextInt(); //add int to n
            int factorCount = countFactors(n); //find factor
            String result = "The number " + n + " has " + factorCount + " factors"; //return result
            return result;
        }else{
            return "There was an exception on the server";
        }
    }
    
    public int countFactors(int num){ //counts the factors of a given number
        int count = 0;
        for (int i = 1; i <= num; i++) {
            if (num % i == 0) {
                count++;
            }
        }
        return count;
    }
}
