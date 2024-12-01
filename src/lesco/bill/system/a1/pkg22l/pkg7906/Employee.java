package lesco.bill.system.a1.pkg22l.pkg7906;

import java.io.*;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static lesco.bill.system.a1.pkg22l.pkg7906.TariffTaxManager.updateTariffTaxInfo;

public class Employee {

    // Class variables (attributes)
    private String name;
    private String username;
    private String password;
    private static String EMPLOYEE_FILE = "EmployeeData.txt";
public static final String ANSI_RESET = "\u001B[0m";        // Reset color
    public static final String ANSI_GREEN = "\u001B[32m";       // Green color
    public static final String ANSI_RED = "\u001B[31m";         // Red color 
    // Constructor to initialize an Employee object
    public Employee(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public String getFileName() {
        return EMPLOYEE_FILE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Method to add a new employee to the file
    public static boolean addEmployeeInFile( String username, String password) {
    try {
        if (isUsernameUnique(EMPLOYEE_FILE, username)) {
            FileWriter writer = new FileWriter(EMPLOYEE_FILE, true);
            writer.write(username + "," + password + "\n"); // Add new employee data
            writer.close();
            System.out.println("Employee added successfully!");
            return true;
        } else {
            System.out.println("Username already exists.");
            return false;
        }
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
}



    // Employee login method
    public static boolean employeeLogin(String username, String password) {

        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper method to check if username is unique
    public static boolean isUsernameUnique(String fileName, String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username)) {
                    return false; // Username already exists
                }
            }
        } catch (FileNotFoundException e) {
            return true; // File doesn't exist, assume first entry
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    // Method to update bill status to 'Paid' after paymer (Done by Employee) and reseting the meter for customer
   public static boolean UpdateTheBillStatusAfterBilling(String customerId) {
        String billingFile = "BillingInfo.txt";
        StringBuilder updatedContent = new StringBuilder();
        boolean billFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(billingFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(customerId)) {
                    
                    parts[9] = "Paid";
                    billFound = true;
                    System.out.println("Bill status updated to 'Paid' for customer ID: " + customerId);
                }
                updatedContent.append(String.join(",", parts)).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return billFound;
        }

        // If the bill was found, write the updated content back to the file
        if (billFound) {
            try (FileWriter writer = new FileWriter(billingFile)) {
                writer.write(updatedContent.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Reset the customer's units after billing
            updateUnitsAfterBilling(customerId, "CustomerInfo.txt");
            System.out.println("Units!");
            return true;

        } else {
            System.out.println("Bill not found for customer ID: " + customerId);
        }
        return false;
    }

    public static void updateUnitsAfterBilling(String customerId, String customerFile) {
        File file = new File(customerFile);
        StringBuilder updatedContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(customerId)) {
                    // Reset regular and peak units to 0
                    parts[8] = "0";
                    if (parts[6].equals("Three Phase")) {
                        parts[9] = "0"; // Reset peak hours for 3-phase meters
                    }
                }
                updatedContent.append(String.join(",", parts)).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Write back the updated content to file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(updatedContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Method to update employee's password
    public static boolean updatePassword(String username,String CurrentPassword, String newPassword) {
        File file = new File(EMPLOYEE_FILE);
        StringBuilder updatedContent = new StringBuilder();
        boolean employeeFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)&&parts[1].equals(CurrentPassword)) {
                    // Update the password
                    parts[1] = newPassword;
                    employeeFound = true;
                }
                updatedContent.append(String.join(",", parts)).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // If the employee was found and updated, rewrite the file with updated content
        if (employeeFound) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(updatedContent.toString());
                System.out.println("Password updated successfully for username: " + username);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Username not found: " + username);
        }
        return false;
    }

    public static void UpdateTerrifInfo() {
        updateTariffTaxInfo();
    }

  public static ArrayList<BillingInfo> viewPaidAndUnpaidBills(String customerId) {
        String billingFile = "BillingInfo.txt";
        boolean billFound = false;
        DecimalFormat decimalFormat = new DecimalFormat("#.##"); // Format to 2 decimal places
        ArrayList<BillingInfo> billsList = new ArrayList<>(); // Create an ArrayList to store BillingInfo objects

        try (BufferedReader reader = new BufferedReader(new FileReader(billingFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 10 && parts[0].equals(customerId)) {
                    // Extract the bill details for the given customer ID
                    String customerID = parts[0];
                    String readingEntryDate = parts[1];
                    int regularUnitsConsumed = Integer.parseInt(parts[2]);
                    int peakUnitsConsumed = Integer.parseInt(parts[3]);
                    double totalElectricityCost = Double.parseDouble(parts[4]);
                    double salesTax = Double.parseDouble(parts[5]);
                    double fixedCharges = Double.parseDouble(parts[6]);
                    double totalBillingAmount = Double.parseDouble(parts[7]);
                    String dueDate = parts[8];
                    String billStatus = parts[9].trim();

                   BillingInfo bill = new BillingInfo(
    customerID, regularUnitsConsumed, peakUnitsConsumed, totalElectricityCost,
    salesTax, fixedCharges, totalBillingAmount, dueDate, readingEntryDate // Include readingEntryDate here
);
                    bill.setBillStatus(billStatus);

                    // Add the bill to the list
                    billsList.add(bill);

                    // Print the bill details
                    System.out.println("******************************************************");
                    System.out.println("Bill Details for Customer ID " + customerId + ":");
                    System.out.println("Customer ID: " + customerID);
                    System.out.println("Reading Entry Date: " + readingEntryDate);
                    System.out.println("Regular Units Consumed: " + regularUnitsConsumed);
                    System.out.println("Peak Units Consumed: " + peakUnitsConsumed);
                    System.out.println("Total Electricity Cost: " + decimalFormat.format(totalElectricityCost) + " Rs");
                    System.out.println("Sales Tax: " + decimalFormat.format(salesTax) + " Rs");
                    System.out.println("Fixed Charges: " + decimalFormat.format(fixedCharges) + " Rs");
                    System.out.println("Total Billing Amount: " + decimalFormat.format(totalBillingAmount) + " Rs");
                    System.out.println("Due Date: " + dueDate);

                    // Print Bill Status with color
                    if (billStatus.equalsIgnoreCase("Paid")) {
                        System.out.println("Bill Status: Paid");
                    } else {
                        System.out.println("Bill Status: Unpaid");
                    }

                    System.out.println("******************************************************");

                    billFound = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!billFound) {
            System.out.println("No bills found for Customer ID " + customerId);
        }

        return billsList; // Return the list of bills
    }

    public static boolean isExpiringSoon(Date expiryDate, Date currentDate, Date expiryThreshold) {
        return expiryDate.after(currentDate) && expiryDate.before(expiryThreshold);
    }

   public static ArrayList<Customer> viewExpiringCNICs() {
       ArrayList <Customer> list= new ArrayList<Customer>();
        String nadraFile = "NADRADB.txt";
        String customerFile = "CustomerInfo.txt";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 30); // Add 30 days to current date
        Date expiryThreshold = calendar.getTime(); // The date 30 days from now

        boolean expiringCnicFound = false; // Flag to check if any expiring CNICs are found

        // Map to hold CNIC and corresponding Customer Information
        Map<String, String[]> cnicToCustomerInfoMap = new HashMap<>();

        // Read customer file and populate the map
        try (BufferedReader customerReader = new BufferedReader(new FileReader(customerFile))) {
            String line;
            while ((line = customerReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String customerId = parts[0]; // Assuming Customer ID is in the 1st column
                    String cnic = parts[1]; // Assuming CNIC is in the 2nd column

                    // Store the entire array (customer info) in the map with CNIC as key
                    cnicToCustomerInfoMap.put(cnic, parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Check expiry dates from NADRA DB and print customer details for expiring CNICs
        try (BufferedReader nadraReader = new BufferedReader(new FileReader(nadraFile))) {
            String line;
            while ((line = nadraReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String cnic = parts[0];
                    String expiryDateStr = parts[2]; // CNIC expiry date is in 3rd column

                    Date expiryDate = dateFormat.parse(expiryDateStr); // Parse expiry date

                    // Calculate the number of days remaining until expiry
                    long differenceInMilliSeconds = expiryDate.getTime() - currentDate.getTime();
                    long differenceInDays = differenceInMilliSeconds / (1000 * 60 * 60 * 24);

                    if (isExpiringSoon(expiryDate, currentDate, expiryThreshold)) {
                        expiringCnicFound = true;

                        // Find customer info based on CNIC
                        String[] customerInfo = cnicToCustomerInfoMap.get(cnic);
                        if (customerInfo != null) {
                            // Display the CNIC details
                            System.out.println("*************************************");
                            System.out.println("CNIC: " + cnic + " is expiring in " + differenceInDays + " days.");
                            System.out.println("Customer Information:");
                           
                            System.out.println("Customer ID: " + customerInfo[0]);
                            System.out.println("CNIC: " + customerInfo[1]);
                            System.out.println("Name: " + (customerInfo.length > 2 ? customerInfo[2] : "N/A"));
                            System.out.println("Address: " + (customerInfo.length > 3 ? customerInfo[3] : "N/A"));
                            System.out.println("Phone Number: " + (customerInfo.length > 4 ? customerInfo[4] : "N/A"));
                            System.out.println("*************************************");
                             Customer c= new Customer(cnic, customerInfo[2], customerInfo[3], customerInfo[4], " ", " ", " ");
                             c.setID(customerInfo[0]);
                            list.add(c);
                        } else {
                            System.out.println("Customer with CNIC: " + cnic + " not found in customer database.");
                        }
                       
                    }
                }
            }

            // If no expiring CNICs were found
            if (!expiringCnicFound) {
                System.out.println("No CNICs expiring in the next 30 days.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
   }
   
   public static boolean GenerateElectricityBillForCustomer(String CustomerId)
   {
       return BillingInfo.generateBill(CustomerId);
   }
}

   


