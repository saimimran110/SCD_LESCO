package Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import lesco.bill.system.a1.pkg22l.pkg7906.BillingInfo;
import lesco.bill.system.a1.pkg22l.pkg7906.Customer;

import static lesco.bill.system.a1.pkg22l.pkg7906.Customer.loginForCustomer;
import static lesco.bill.system.a1.pkg22l.pkg7906.Customer.addCustomer;
import static lesco.bill.system.a1.pkg22l.pkg7906.Customer.getCustomerName;
import static lesco.bill.system.a1.pkg22l.pkg7906.Customer.viewBills;
import static lesco.bill.system.a1.pkg22l.pkg7906.Customer.updateUnitsConsumed;
import static lesco.bill.system.a1.pkg22l.pkg7906.Customer.updateCNICExpiryDate;
import static lesco.bill.system.a1.pkg22l.pkg7906.Customer.predictBill;
import java.io.*;


public class CustomerController {

    public static boolean CUSTOMER_LOGIN(String id, String cnic) {
        return loginForCustomer(id, cnic);
    }

    public static int ADD_CUSTOMER(String cnic, String name, String address, String phoneNum, String customerType, String meterType, String connectionDate) {
        return addCustomer(cnic, name, address, phoneNum, customerType, meterType, connectionDate);
    }

    public static ArrayList<BillingInfo> VIEW_BILL(String customerId) {
        ArrayList<BillingInfo> list = viewBills(customerId);
        return list;
    }

    public static boolean UPDATE_UNIT(String ci, int ru, int pu) {
        return updateUnitsConsumed(ci, ru, pu);
    }

    public static String GIVE_NAME_OF_CUSTOMER(String id) {

        return getCustomerName(id);
    }

    public static boolean UPDATE_CNIC_EXPIRY_DATE(String cnic, String customerId, int day, int month, int year) {
        return updateCNICExpiryDate(cnic, customerId, day, month, year);
    }

    public static ArrayList<String> PRIDICT_BILL(String id, int ru, int pu) {

        ArrayList<String> list = predictBill(id, ru, pu);

        return list;

    }
    public String fetchAllCustomers() {
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(Customer.CUSTOMER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append(";");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Unable to fetch customers.";
        }
        return response.toString().replaceAll(";$", ""); // Remove trailing semicolon
    }

    // Update a customer
    public String updateCustomer(String customerId, String newName, String newAddress) {
        try (BufferedReader reader = new BufferedReader(new FileReader(Customer.CUSTOMER_FILE))) {
            StringBuilder updatedContent = new StringBuilder();
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(customerId)) {
                    data[2] = newName;
                    data[3] = newAddress;
                    found = true;
                }
                updatedContent.append(String.join(",", data)).append("\n");
            }
            if (found) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(Customer.CUSTOMER_FILE))) {
                    writer.write(updatedContent.toString());
                    return "Customer updated successfully.";
                }
            }
            return "Error: Customer not found.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Unable to update customer.";
        }
    }

    // Delete a customer
    public String deleteCustomer(String customerId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(Customer.CUSTOMER_FILE))) {
            StringBuilder updatedContent = new StringBuilder();
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (!data[0].equals(customerId)) {
                    updatedContent.append(line).append("\n");
                } else {
                    found = true;
                }
            }
            if (found) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(Customer.CUSTOMER_FILE))) {
                    writer.write(updatedContent.toString());
                    return "Customer deleted successfully.";
                }
            }
            return "Error: Customer not found.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Unable to delete customer.";
        }
    }

}
