package lesco.bill.system.a1.pkg22l.pkg7906;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 12345;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(10); // Handle up to 10 clients simultaneously

    public static void main(String[] args) {
        System.out.println("Server is running...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                threadPool.execute(new ClientHandler(clientSocket)); // Assign client to a new thread
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            while (true) {
                String request = (String) in.readObject(); // Receive client request
                System.out.println("Request from client: " + request);

                String response = handleRequest(request); // Handle the request
                out.writeObject(response); // Send response back to the client
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private synchronized String handleRequest(String request) {
        String[] parts = request.split(",");
        String command = parts[1];

        try {
            switch (command) {
                case "LOGIN":
                    return handleLogin(parts); // Modified to support employee login
                case "GENERATE_BILL":
                    return handleGenerateBill(parts);
                case "UPDATE_TARIFF":
                    return handleUpdateTariff();
                case "VIEW_CNIC_EXPIRIES":
                    return handleViewCnicExpiries();
                default:
                    return "Invalid command.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error handling request: " + e.getMessage();
        }
    }

    private String handleLogin(String[] parts) {
        // Added proper implementation for employee login handling
        if (parts.length < 4) {
            return "Invalid LOGIN command. Expected format: LOGIN,<userType>,<username>,<password>";
        }

        String userType = parts[0]; // Fixed to properly identify the userType from the request
        String username = parts[2];
        String password = parts[3];

        if ("Employee".equalsIgnoreCase(userType)) {
            boolean loginSuccess = Employee.employeeLogin(username, password);
            return loginSuccess ? "Login successful." : "Invalid username or password.";
        }

        // Customer login can be implemented here if needed in the future
        return "Unsupported user type for login: " + userType;
    }

    private String handleGenerateBill(String[] parts) {
        String customerId = parts[1];
        boolean success = BillingInfo.generateBill(customerId);
        return success ? "Bill generated successfully." : "Error generating bill.";
    }

    private String handleUpdateTariff() {
        TariffTaxManager.updateTariffTaxInfo();
        return "Tariff updated successfully.";
    }

    private String handleViewCnicExpiries() {
        ArrayList<Customer> expiringCnicList = Employee.viewExpiringCNICs();
        if (expiringCnicList.isEmpty()) {
            return "No expiring CNICs found.";
        }

        StringBuilder response = new StringBuilder();
        for (Customer customer : expiringCnicList) {
            response.append("Customer ID: ").append(customer.getCustomerId()).append(", CNIC: ").append(customer.getCnic()).append("\n");
        }
        return response.toString();
    }
}
