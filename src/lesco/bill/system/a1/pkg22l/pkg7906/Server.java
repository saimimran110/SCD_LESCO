package lesco.bill.system.a1.pkg22l.pkg7906;

import Controller.CustomerController;
import Controller.EmployeeController;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 12345;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(10); // Handle up to 10 clients simultaneously



    public static void main(String[] args) {
        System.out.println("Server is running...");
        ///temp



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
        //Employee,LOGIN,Saim,12345
        try {
            switch (command) {
                case "EMPLOYEE_LOGIN":
                    return handleLogin(parts); // Modified to support employee login
                case "GENERATE_BILL":
                    return handleGenerateBill(parts);
                case "UPDATE_TARIFF":
                    return handleUpdateTariff();
                case "UPDATE_CNIC_EXPIRY":
                    return handleUpdateCnicExpiry(parts);
                case "VIEW_CNIC_EXPIRES":
                    return handleViewCnicExpiries(parts);
                case "ADD_EMPLOYEE": // New case for adding employees
                    return handleAddEmployee(parts);
                case "PREDICT_BILL":
                    return handlePredictBill(parts);
                case "CUSTOMER_LOGIN": // New case for customer login
                    return handleCustomerLogin(parts);
                case "CUSTOMER_ADD":
                    return handleAddCustomer(parts);
                case "FETCH_INFO":
                    return handleFetchInfo(parts);
                case "UPDATE_UNITS":
                    return handleUpdateUnits(parts);
                case "VIEW_BILLS":
                    return handleViewBills(parts);
                case "UPDATE_BILL_STATUS":
                    return handleUpdateBillStatus(parts);
                case "UPDATE_PASSWORD":
                    return handleUpdatePassword(parts);
                case "VIEW_CNIC_EXPIRIES":
                    return handleViewCnicExpiries(parts);
                case "VIEW_PAID_UNPAID_BILLS":
                    return handleViewPaidUnpaidBills(parts);
                case "REMOVE_LATEST_BILL":
                    return handleRemoveLatestBill(parts);
                case "UPDATE_TARIFF_TAX":
                    return handleUpdateTariffTax(parts);
//                case "GET_CURRENT_VALUES":
//                    return handleGetCurrentValues(parts);
                case "FETCH_CUSTOMERS":
                    return handleFetchCustomers();
                case "UPDATE_CUSTOMER":
                    return handleUpdateCustomer(parts);
                case "DELETE_CUSTOMER":
                    return handleDeleteCustomer(parts);


                default:
                    return "Invalid command.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error handling request: " + e.getMessage();
        }
    }
    private String handleFetchCustomers() {
        CustomerController customerController = new CustomerController();
        return customerController.fetchAllCustomers();
    }

    // Function to handle updating a customer
    private String handleUpdateCustomer(String[] parts) {
        if (parts.length < 5) {
            return "Error: Invalid UPDATE_CUSTOMER command format.";
        }

        String customerId = parts[2];
        String newName = parts[3];
        String newAddress = parts[4];

        CustomerController customerController = new CustomerController();
        return customerController.updateCustomer(customerId, newName, newAddress);
    }

    // Function to handle deleting a customer
    private String handleDeleteCustomer(String[] parts) {
        if (parts.length < 3) {
            return "Error: Invalid DELETE_CUSTOMER command format.";
        }

        String customerId = parts[2];
        CustomerController customerController = new CustomerController();
        return customerController.deleteCustomer(customerId);
    }

    private String handleUpdateTariffTax(String[] parts) {
        if (parts.length < 7) {
            return "Error: Invalid UPDATE_TARIFF_TAX command format.";
        }

        String meterType = parts[2];
        String customerType = parts[3];
        String regularUnitPrice = parts[4];
        String peakHourUnitPrice = parts[5];
        String taxPercentage = parts[6];
        String fixedCharges = parts[7];

        String newEntry = String.format("%s,%s,%s,%s,%s",
                meterType, regularUnitPrice, peakHourUnitPrice, taxPercentage, fixedCharges);

        int row = TariffTaxManager.getRowIndex(meterType, customerType);
        TariffTaxManager.updateFileRow(row, newEntry);

        return "Success: Tariff and tax updated.";
    }

    private String handleGetCurrentValues(String[] parts) {
        if (parts.length < 3) {
            return "Error: Invalid GET_CURRENT_VALUES command format.";
        }

        String meterType = parts[2];
        String customerType = parts[3];
        int row = TariffTaxManager.getRowIndex(meterType, customerType);

        return TariffTaxManager.getRowContent(row);
    }

    private String handleViewCnicExpiries(String[] parts) {
        // Fetch the list of customers with expiring CNICs from the EmployeeController

        ArrayList<Customer> expiringCnicList = EmployeeController.VIEW_EXPIRING_CNIC();

        // Check if the list is empty
        if (expiringCnicList == null || expiringCnicList.isEmpty()) {
            return "Error: No expiring CNICs found.";
        }

        // Serialize customer data into a string format for transmission
        StringBuilder response = new StringBuilder();
        for (Customer customer : expiringCnicList) {
            response.append(customer.getCustomerId()).append(",")
                    .append(customer.getName()).append(",")
                    .append(customer.getCnic()).append(",")
                    .append(customer.getAddress()).append(",")
                    .append(customer.getPhoneNum()).append(";");
        }

        // Return the serialized customer data
        return response.toString();
    }

    private String handleViewPaidUnpaidBills(String[] parts) {
        if (parts.length < 3) {
            return "Error: Invalid VIEW_PAID_UNPAID_BILLS command. Expected format: Employee,VIEW_PAID_UNPAID_BILLS,<customerId>";
        }

        String customerId = parts[2];
        ArrayList<BillingInfo> bills = EmployeeController.VIEW_PAID_UNPAID_BILLS(customerId); // Assuming this method exists in the backend

        if (bills.isEmpty()) {
            return "Error: No bills found for Customer ID: " + customerId;
        }

        // Convert the list of bills to a string
        StringBuilder response = new StringBuilder();
        for (BillingInfo bill : bills) {
            response.append(bill.toString()).append(";");
        }

        return response.toString();
    }

    private String handleRemoveLatestBill(String[] parts) {
        if (parts.length < 3) {
            return "Error: Invalid REMOVE_LATEST_BILL command. Expected format: Employee,REMOVE_LATEST_BILL,<readingDate>";
        }

        String readingDate = parts[2];
        boolean success = EmployeeController.RemoveBill(readingDate);

        if (success) {
            return "Bill removed successfully.";
        } else {
            return "Error: Failed to remove the bill.";
        }
    }

    private String handleUpdatePassword(String[] parts) {
        if (parts.length < 4) {
            return "Invalid UPDATE_PASSWORD command. Expected format: Employee,UPDATE_PASSWORD,<username>,<currentPassword>,<newPassword>";
        }


        String username = parts[2];
        String currentPassword = parts[3];
        String newPassword = parts[4];

        // Call the backend function
        boolean success = EmployeeController.UPDATE_PASSWORD(username, currentPassword, newPassword);

        if (success) {
            return "Password updated successfully.";
        } else {
            return "Failed to update password. Check the username and current password.";
        }
    }

    private String handleUpdateBillStatus(String[] parts) {
        if (parts.length < 3) {
            return "Invalid UPDATE_BILL_STATUS command. Expected format: Employee,UPDATE_BILL_STATUS,<customerId>";
        }


        String customerId = parts[2];
        boolean statusUpdated =EmployeeController.UPDATE_BILLING_STATUS(customerId);
       
        if (statusUpdated) {
            return "Success: Bill status updated to PAID for Customer ID: " + customerId;
        } else {
            return "Error: Unable to update bill status for Customer ID: " + customerId;
        }
    }

    private String handleViewBills(String[] parts) {

        if (parts.length < 3) {
            return "Invalid VIEW_BILL command. Expected format: Customer,VIE_BILL,<customerId>";
        }
        String customerId = parts[2];

        ArrayList<BillingInfo> bills =CustomerController.VIEW_BILL(customerId);

        if (bills.isEmpty()) {
            return "Error: No bills found for Customer ID " + customerId;
        }

        // Serialize bills into a string format
        StringBuilder response = new StringBuilder();
        for (BillingInfo bill : bills) {
            response.append(bill.toString()).append(";");
        }
        return response.toString();  // Each bill is separated by a semicolon

    }

    private String handleUpdateCnicExpiry(String[] parts) {
        if (parts.length < 7) {
            return "Invalid UPDATE_CNIC_EXPIRY command. Expected format: Customer,UPDATE_CNIC_EXPIRY,<customerId>,<cnic>,<day>,<month>,<year>";
        }

        String customerId = parts[2];
        String cnic = parts[3];
        int day, month, year;

        try {
            day = Integer.parseInt(parts[4]);
            month = Integer.parseInt(parts[5]);
            year = Integer.parseInt(parts[6]);
        } catch (NumberFormatException e) {
            return "Invalid expiry date format.";
        }

        // Call the backend function
        boolean success = CustomerController.UPDATE_CNIC_EXPIRY_DATE(customerId, cnic, day, month, year);


        if (success) {
            return "CNIC expiry updated successfully.";
        } else {
            return "Failed to update CNIC expiry.";
        }
    }

    private String handleCustomerLogin(String[] parts) {
        if (parts.length < 4) {
            return "Invalid CUSTOMER_LOGIN command. Expected format: Customer,CUSTOMER_LOGIN,<id>,<cnic>";
        }

        String customerId = parts[2];
        String cnic = parts[3];
        boolean loginSuccess = Customer.loginForCustomer(customerId, cnic);

        if (loginSuccess) {
            // Fetch customer name for success response (if needed)

            String customerName =CustomerController.GIVE_NAME_OF_CUSTOMER(customerId);
            return "Login successful," + customerName; // Include the customer name in response
        } else {
            return "Invalid Customer ID or CNIC.";
        }
    }

    private String handlePredictBill(String[] parts) {
        if (parts.length < 4) {
            return "Invalid PREDICT_BILL command. Expected format: PREDICT_BILL,<customerId>,<regularUnits>,<peakUnits>";
        }

        String customerId = parts[2];
        int regularUnits = Integer.parseInt(parts[3]);
        int peakUnits = Integer.parseInt(parts[4]);



        ArrayList<String> predictionResult =CustomerController.PRIDICT_BILL(customerId, regularUnits, peakUnits);
        if (predictionResult == null || predictionResult.isEmpty()) {
            return "Error predicting the bill. Please check the inputs.";
        }

        StringBuilder response = new StringBuilder();
        for (String line : predictionResult) {
            response.append(line).append("\n");
        }

        return response.toString().trim();
    }

    private String handleAddEmployee(String[] parts) {
        if (parts.length < 4) {
            return "Invalid ADD command. Expected format: Employee,ADD,<username>,<password>";
        }

        String username = parts[2];
        String password = parts[3];
        boolean success =EmployeeController.ADD_EMPLOYEE(username, password);



        return success ? "Employee added successfully." : "Username already exists.";
    }

    private String handleLogin(String[] parts) {
        // Added proper implementation for employee login handling
        if (parts.length < 4) {
            return "Invalid LOGIN command. Expected format: LOGIN,<userType>,<username>,<password>";
        }

        String userType = parts[0]; // Fixed to properly identify the userType from the request
        String username = parts[2];
        String password = parts[3];

        if ("Employee".equalsIgnoreCase(userType))
        {

            boolean loginSuccess = EmployeeController.LOGIN_EMPLOYEE(username, password);
            return loginSuccess ? "Login successful." : "Invalid username or password.";
        }


        // Customer login can be implemented here if needed in the future
        return "Unsupported user type for login: " + userType;
    }

    private String handleGenerateBill(String[] parts) {
        if (parts.length < 3) {
            return "Invalid GENERATE_BILL command. Expected format: Employee,GENERATE_BILL,<customerId>";
        }

        String customerId = parts[2];

        boolean success = EmployeeController.GENERATE_BILL(customerId);

        if (success) {
            return "Bill generated successfully.";
        } else {
            return "Failed to generate the bill. Invalid Customer ID.";
        }
    }

    private String handleUpdateTariff() {

        TariffTaxManager.updateTariffTaxInfo();
        return "Tariff updated successfully.";
    }

    private String handleAddCustomer(String[] parts) {
        if (parts.length < 8) {
            return "Invalid ADD_CUSTOMER command. Expected format: ADD_CUSTOMER,<cnic>,<name>,<address>,<phoneNum>,<customerType>,<meterType>,<connectionDate>";
        }

        String cnic = parts[2];
        String name = parts[3];
        String address = parts[4];
        String phoneNum = parts[5];
        String customerType = parts[6];
        String meterType = parts[7];
        String connectionDate = parts[8];


        int result =CustomerController.ADD_CUSTOMER(cnic, name, address, phoneNum, customerType, meterType, connectionDate);
        switch (result) {
            case 0:
                return "Customer added successfully.";
            case 1:
                return "CNIC not valid.";
            case 2:
                return "Maximum 3 meters allowed per CNIC.";
            case 3:
                return "Error while adding customer.";
            default:
                return "Unknown error.";
        }
    }

    private String handleFetchInfo(String[] parts) {
        if (parts.length < 2) {
            return "Invalid FETCH_INFO command. Expected format: Customer,FETCH_INFO,<customerId>";
        }
        String customerId = parts[2];

        // Use Customer class to get meter type
        try (BufferedReader reader = new BufferedReader(new FileReader(Customer.CUSTOMER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] customerData = line.split(",");
                if (customerData[0].equals(customerId)) {
                    return customerData[6]; // Return meter type (e.g., "Single Phase" or "Three Phase")
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading customer file.";
        }
        return "Error: Customer not found.";
    }

    private String handleUpdateUnits(String[] parts) {
        if (parts.length < 5) {
            return "Invalid UPDATE_UNITS command. Expected format: Customer,UPDATE_UNITS,<customerId>,<regularUnits>,<peakUnits>";
        }
        String customerId = parts[2];
        int regularUnits;
        int peakUnits;

        try {
            regularUnits = Integer.parseInt(parts[3]);
            peakUnits = Integer.parseInt(parts[4]);
        } catch (NumberFormatException e) {
            return "Error: Invalid unit numbers.";
        }

        boolean success = CustomerController.UPDATE_UNIT(customerId, regularUnits, peakUnits);
        return success ? "Success" : "Error: Could not update units.";
    }
}
