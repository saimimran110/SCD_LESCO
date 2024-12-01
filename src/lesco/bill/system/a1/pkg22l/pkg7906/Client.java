package lesco.bill.system.a1.pkg22l.pkg7906;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the server.");

            while (true) {
                System.out.println("Enter command:");
                String command = scanner.nextLine(); // Read user input

                out.writeObject(command); // Send request to server
                String response = (String) in.readObject(); // Receive response from server
                System.out.println("Server response: " + response);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

