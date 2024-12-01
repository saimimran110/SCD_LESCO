package lesco.bill.system.a1.pkg22l.pkg7906;


import java.io.*;
import java.net.*;

public class ClientSocket
{
    private static final String SERVER_ADDRESS = "localhost"; // Server's address
    private static final int PORT = 12345; // Port number
    private static ClientSocket instance; // Singleton instance
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    // Private constructor for Singleton
    private ClientSocket() throws IOException {
        try {
            this.socket = new Socket(SERVER_ADDRESS, PORT); // Connect to the server
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Failed to connect to the server: " + e.getMessage());
            throw e; // Rethrow exception to notify callers
        }
    }

    // Singleton pattern
    public static synchronized ClientSocket getInstance() throws IOException {
        if (instance == null) {
            instance = new ClientSocket();
        }
        return instance;
    }

    // Send request to server and receive response
    public String sendRequest(String request) throws IOException, ClassNotFoundException {
        try {
            out.writeObject(request); // Send the request
            return (String) in.readObject(); // Receive the response
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error during communication: " + e.getMessage());
            throw e; // Propagate exception
        }
    }

    // Close the connection
    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
    }
}
