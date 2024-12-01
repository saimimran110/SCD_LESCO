package lesco.bill.system.a1.pkg22l.pkg7906;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class BillingInfo {
    private String customerId;
    private int regularUnitsConsumed;
    private int peakUnitsConsumed;
    private double totalElectricityCost;
    private double salesTax;
    private double fixedCharges;
    private double totalBillingAmount;
    private String dueDate;
    private String billStatus;
    private String readingEntryDate;  // Added this field to store the reading date

    public BillingInfo() {
    }
 
    // Constructor to initialize a Billing object
    public BillingInfo(String customerId, int regularUnitsConsumed, int peakUnitsConsumed, double totalElectricityCost, double salesTax, double fixedCharges, double totalBillingAmount, String dueDate, String readingEntryDate) {
        this.customerId = customerId;
        this.regularUnitsConsumed = regularUnitsConsumed;
        this.peakUnitsConsumed = peakUnitsConsumed;
        this.totalElectricityCost = totalElectricityCost;
        this.salesTax = salesTax;
        this.fixedCharges = fixedCharges;
        this.totalBillingAmount = totalBillingAmount;
        this.dueDate = dueDate;
        this.readingEntryDate = readingEntryDate;
        this.billStatus = "Unpaid"; // Default status
    }

    // Getters and Setters
    public String getCustomerId() {
        return customerId;
    }

    public int getRegularUnitsConsumed() {
        return regularUnitsConsumed;
    }

    public int getPeakUnitsConsumed() {
        return peakUnitsConsumed;
    }

    public double getTotalElectricityCost() {
        return totalElectricityCost;
    }

    public double getSalesTax() {
        return salesTax;
    }

    public double getFixedCharges() {
        return fixedCharges;
    }

    public double getTotalBillingAmount() {
        return totalBillingAmount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public String getReadingEntryDate() {
        return readingEntryDate;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setRegularUnitsConsumed(int regularUnitsConsumed) {
        this.regularUnitsConsumed = regularUnitsConsumed;
    }

    public void setPeakUnitsConsumed(int peakUnitsConsumed) {
        this.peakUnitsConsumed = peakUnitsConsumed;
    }

    public void setTotalElectricityCost(double totalElectricityCost) {
        this.totalElectricityCost = totalElectricityCost;
    }

    public void setSalesTax(double salesTax) {
        this.salesTax = salesTax;
    }

    public void setFixedCharges(double fixedCharges) {
        this.fixedCharges = fixedCharges;
    }

    public void setTotalBillingAmount(double totalBillingAmount) {
        this.totalBillingAmount = totalBillingAmount;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setReadingEntryDate(String readingEntryDate) {
        this.readingEntryDate = readingEntryDate;
    }

    // Updated to include readingEntryDate
    @Override
    public String toString() {
        return customerId + "," + readingEntryDate + "," + regularUnitsConsumed + "," + peakUnitsConsumed + "," + totalElectricityCost + "," + salesTax + "," + fixedCharges + "," + totalBillingAmount + "," + dueDate + "," + billStatus + ",";
    }

    // Method to create BillingInfo object from a line in BillingInfo.txt
public static BillingInfo fromString(String data) {
    String[] fields = data.split(",");

    // Validate the number of fields (should be 10)
    if (fields.length != 10) {  
        throw new IllegalArgumentException("Invalid data format: " + data);
    }

    // Create and populate a BillingInfo object
    BillingInfo bill = new BillingInfo();
    bill.setCustomerId(fields[0]);  // Customer ID
    bill.setReadingEntryDate(fields[1]);  // Reading entry date
    bill.setRegularUnitsConsumed(Integer.parseInt(fields[2]));  // Regular units consumed
    bill.setPeakUnitsConsumed(Integer.parseInt(fields[3]));  // Peak units consumed
    bill.setTotalElectricityCost(Double.parseDouble(fields[4]));  // Total electricity cost
    bill.setSalesTax(Double.parseDouble(fields[5]));  // Sales tax
    bill.setFixedCharges(Double.parseDouble(fields[6]));  // Fixed charges
    bill.setTotalBillingAmount(Double.parseDouble(fields[7]));  // Total billing amount
    bill.setDueDate(fields[8]);  // Due date
    bill.setBillStatus(fields[9]);  // Bill status (Paid/Unpaid)

    return bill;
}



 public static boolean generateBill(String customerId) {
        String customerFile = "CustomerInfo.txt";
        String billingFile = "BillingInfo.txt";
        Map<String, String[]> tariffInfo = loadTariffTaxInfo(); // Load tariff info

        // Get customer data
        String customerData = getCustomerData(customerId, customerFile);
        if (customerData == null) {
            System.out.println("Customer ID not found.");
            return false;
        }

        String[] parts = customerData.split(",");
        String meterType = parts[6];
        String customerType = parts[5];
        int regularUnitsConsumed = Integer.parseInt(parts[8]);
        int peakUnitsConsumed = meterType.equals("Three Phase") ? Integer.parseInt(parts[9]) : 0;

        // Get tariff info
        String tariffKey = meterType + " " + customerType;
        String[] tariffData = tariffInfo.get(tariffKey);
        if (tariffData == null) {
            System.out.println("Tariff data not found.");
            return false;
        }

        double regularUnitPrice = Double.parseDouble(tariffData[1]);
        double peakHourUnitPrice = meterType.equals("Three Phase") ? Double.parseDouble(tariffData[2]) : 0;
        double salesTaxRate = Double.parseDouble(tariffData[3]);
        double fixedCharges = Double.parseDouble(tariffData[4]);

        // Calculate billing amounts
        double regularCost = regularUnitsConsumed * regularUnitPrice;
        double peakHourCost = peakUnitsConsumed * peakHourUnitPrice;
        double totalElectricityCost = regularCost + peakHourCost;
        double salesTax = totalElectricityCost * (salesTaxRate / 100);
        double totalBillingAmount = totalElectricityCost + salesTax + fixedCharges;

        // Create bill entry
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String readingEntryDate = dateFormat.format(new Date());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        String dueDate = dateFormat.format(cal.getTime());

        try (FileWriter writer = new FileWriter(billingFile, true)) {
            writer.write(customerId + "," + readingEntryDate + "," + regularUnitsConsumed + "," + peakUnitsConsumed + "," + totalElectricityCost + "," + salesTax + "," + fixedCharges + "," + totalBillingAmount + "," + dueDate + "," + "Unpaid,\n");
            System.out.println("Bill generated successfully for customer " + customerId);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    // Helper method to get customer data
    public static String getCustomerData(String customerId, String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(customerId)) {
                    return line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to load tariff tax info
    public static Map<String, String[]> loadTariffTaxInfo() {
        String fileName = "TerrifTaxInfo.txt";
        Map<String, String[]> tariffInfo = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int counter = 1;
            String key;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1) {
                    if (counter == 1 || counter == 3) {
                        key = parts[0] + " Domestic";
                    } else {
                        key = parts[0] + " Commercial";
                    }

                    // Add the full line parts (including empty values) to the map
                    tariffInfo.put(key, parts);
                    counter++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tariffInfo;
    }
}
