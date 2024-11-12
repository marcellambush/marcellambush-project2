/*import java.util.*;
public class Server{
    int num;
    public Server(int num){
        this.num = num;
    }
    public void serve(int num){

    }
    public void disconnect(){

    }
    public ArrayList getLocalTimes(){

    }
}*/
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;

public class Server {
    private int port;              // Port to listen on
    private ServerSocket serverSocket;  // Server socket for accepting connections
    private List<Socket> clientSockets; // List to track connected clients
    private List<LocalDateTime> connectedTimes; // To track when clients connect

    public Server(int port) {
        this.port = port;
        clientSockets = new ArrayList<>();
        connectedTimes = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(port); // Open server socket on the specified port
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    // Method to serve a single client or multiple clients
    public void serve(int numClients) {
        try {
            for (int i = 0; i < numClients; i++) {
                Socket clientSocket = serverSocket.accept(); // Wait for a client to connect
                clientSockets.add(clientSocket); // Add the client socket to the list

                // Record the time when this client connected
                connectedTimes.add(LocalDateTime.now());

                // Start a new thread to handle this client
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Error while accepting client connection: " + e.getMessage());
        }
    }

    // Clean up the server and disconnect all clients
    public void disconnect() {
        try {
            for (Socket clientSocket : clientSockets) {
                clientSocket.close(); // Close all client sockets
            }
            serverSocket.close(); // Close the server socket
            System.out.println("Server disconnected.");
        } catch (IOException e) {
            System.err.println("Error during disconnection: " + e.getMessage());
        }
    }

    // Method to retrieve the times when clients connected (for test purposes)
    public List<LocalDateTime> getConnectedTimes() {
        return connectedTimes;
    }

    // A private inner class to handle communication with clients in a separate thread
    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                // Set up input/output streams for client communication
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Read the request from the client (e.g., number to process)
                String input = in.readLine();
                System.out.println("Received from client: " + input);

                // Example: Process the client's request (e.g., finding the number of factors)
                String response = processRequest(input);
                
                // Send the response back to the client
                out.println(response);
            } catch (IOException e) {
                System.err.println("Error while communicating with client: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();  // Close the connection to the client
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }

        // Helper method to process a client's request (e.g., calculating factors)
        private String processRequest(String num) {
            try {
                int n = Integer.parseInt(num);
                int factorCount = countFactors(n);
                return "The number " + num + " has " + factorCount + " factors";
            } catch (NumberFormatException e) {
                return "There was an exception on the server";
            }
        }

        // Helper method to count the factors of a number
        private int countFactors(int num) {
            int count = 0;
            for (int i = 1; i <= num; i++) {
                if (num % i == 0) {
                    count++;
                }
            }
            return count;
        }
    }
}
