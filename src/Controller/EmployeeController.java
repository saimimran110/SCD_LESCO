package Controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import lesco.bill.system.a1.pkg22l.pkg7906.BillingInfo;
import lesco.bill.system.a1.pkg22l.pkg7906.Customer;
import lesco.bill.system.a1.pkg22l.pkg7906.Employee;
import static lesco.bill.system.a1.pkg22l.pkg7906.Employee.GenerateElectricityBillForCustomer;
import static lesco.bill.system.a1.pkg22l.pkg7906.Employee.addEmployeeInFile;
import static lesco.bill.system.a1.pkg22l.pkg7906.Employee.employeeLogin;
import static lesco.bill.system.a1.pkg22l.pkg7906.Employee.updatePassword;
import static lesco.bill.system.a1.pkg22l.pkg7906.Employee.UpdateTheBillStatusAfterBilling;
import static lesco.bill.system.a1.pkg22l.pkg7906.Employee.*;

public class EmployeeController {
    private static final String BILLING_FILE_PATH = "BillingInfo.txt";

    public boolean ADD_EMPLOYEE(String u, String p) {
        return addEmployeeInFile(u, p);
    }

    public boolean LOGIN_EMPLOYEE(String u, String p) {
        return employeeLogin(u, p);
    }

    public boolean UPDATE_PASSWORD(String u, String p, String np) {
        return updatePassword(u, p, np);
    }

    public boolean UPDATE_BILLING_STATUS(String id) {
        return UpdateTheBillStatusAfterBilling(id);
    }

   public boolean GENERATE_BILL(String id) {
    return GenerateElectricityBillForCustomer(id);
}

    public ArrayList<Customer> VIEW_EXPIRING_CNIC() {
        return viewExpiringCNICs();
    }

    public ArrayList<BillingInfo> VIEW_PAID_UNPAID_BILLS(String customerId) {
        return Employee.viewPaidAndUnpaidBills(customerId);
    }

    // Method to remove the latest bill from the BillingInfo.txt file for a specific customer
   public boolean RemoveBill(String readingDate) {
        String FILE = "BillingInfo.txt";
        StringBuilder newFileContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;

            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Check if the reading date matches the line
                if (parts.length > 1 && parts[1].equals(readingDate)) {
                    // Skip this line if the reading date matches
                    System.out.println("Removed bill with reading date: " + readingDate);
                } else {
                    // Append the line to the new content
                    newFileContent.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // After reading, rewrite the updated content back to the original file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            writer.write(newFileContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    public static ArrayList<BillingInfo> getAllBills() {
        ArrayList<BillingInfo> bills = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BILLING_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                bills.add(BillingInfo.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("Error reading the billing file: " + e.getMessage());
        }
        return bills;
    }
}
