package lesco.bill.system.a1.pkg22l.pkg7906;

import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
public class Customer {
    // Class variables (attributes)
    private String customerId;
    private String cnic;
    private String name;
    private String address;
    private String phoneNum;
    private String customerType;
    private String meterType;
    private String connectionDate;
    private int regularUnits = 0;  // Default to 0
    private int peakUnits = 0;     // Default to 0
    
    public static final String CUSTOMER_FILE = "CustomerInfo.txt";
    public static final String ANSI_RESET = "\u001B[0m";        // Reset color
    public static final String ANSI_GREEN = "\u001B[32m";       // Green color
    public static final String ANSI_RED = "\u001B[31m"; 
    
    public Customer (){}
 public Customer(String cnic, String name, String address, String phoneNum, 
                    String customerType, String meterType, String connectionDate) {
        Random rand = new Random();
        this.customerId = String.format("%04d", rand.nextInt(10000)); // Generates a 4-digit random ID
        this.cnic = cnic;
        this.name = name;
        this.address = address;
        this.phoneNum = phoneNum;
        this.customerType = customerType;
        this.meterType = meterType;
        this.connectionDate = connectionDate;
        // Regular units and Peak units default to 0
        this.regularUnits = 0;
        this.peakUnits = 0;
    }
  public Customer(String cnic, String name, String address, String phoneNum, 
                    String customerType, String meterType, String connectionDate,int regUnit,int peakUnit) {
        Random rand = new Random();
        this.customerId = String.format("%04d", rand.nextInt(10000)); // Generates a 4-digit random ID
        this.cnic = cnic;
        this.name = name;
        this.address = address;
        this.phoneNum = phoneNum;
        this.customerType = customerType;
        this.meterType = meterType;
        this.connectionDate = connectionDate;
        // Regular units and Peak units default to 0
        this.regularUnits = regUnit;
        this.peakUnits = peakUnit;
    }

    // Getters and setters
    public String getCustomerId() {
        return customerId;
    }

    public void setID(String id) {
        customerId = id;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public String getConnectionDate() {
        return connectionDate;
    }

    public void setConnectionDate(String connectionDate) {
        this.connectionDate = connectionDate;
    }

    // Regular Units Getter and Setter
    public int getRegularUnits() {
        return regularUnits;
    }

    public void setRegularUnits(int regularUnits) {
        this.regularUnits = regularUnits;
    }

    // Peak Units Getter and Setter
    public int getPeakUnits() {
        return peakUnits;
    }

    public void setPeakUnits(int peakUnits) {
        this.peakUnits = peakUnits;
    }

    public String getFileName() {
        return CUSTOMER_FILE;
    }

    // Override toString() to include regular and peak units
    @Override
    public String toString() {
        return customerId + "," + cnic + "," + name + "," + address + "," + phoneNum + "," 
               + customerType + "," + meterType + "," + connectionDate + ","
               + regularUnits + "," + peakUnits;
    }

  
   public static int addCustomer(String cnic, String name, String address, String phoneNum, String customerType, String meterType, String connectionDate) {
        if (!isValidCNIC(cnic)) {
            System.out.println("CNIC NOT VALID!");
            return 1; // Error code 1: Invalid CNIC
        }
        if (countMetersForCnic(CUSTOMER_FILE, cnic) >= 3) {
            return 2; // Error code 2: Maximum meters per CNIC
        }

        Random rand = new Random();
        String customerId = String.format("%04d", rand.nextInt(10000)); // Generates 4-digit random ID
        System.out.println("Randomly generated ID for " + name + " is " + customerId);

        String regUnitConsumed = "0";
        String peakHrsUnit = meterType.equals("Three Phase") ? "0" : ""; // Peak hrs unit for 3-phase

        try {
            FileWriter writer = new FileWriter(CUSTOMER_FILE, true);
            writer.write(customerId + "," + cnic + "," + name + "," + address + "," + phoneNum + "," + customerType + "," + meterType + "," + connectionDate + "," + regUnitConsumed + "," + peakHrsUnit + "\n");
            writer.close();
            return 0; // Success code 0: Customer added successfully
        } catch (IOException e) {
            e.printStackTrace();
            return 3; // Error code 3: File writing error
        }
    }
    // Helper 
    public static boolean isValidCNIC(String cnic) {
        if (cnic.length() != 13) {
            System.out.println("sd");
            return false;
        }

        String NADRADB_FILE = "NADRADB.txt";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateStr = dateFormat.format(new Date());

        try (BufferedReader reader = new BufferedReader(new FileReader(NADRADB_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(cnic)) {
                    Date expiry = dateFormat.parse(parts[2]);
                    Date currentDate = dateFormat.parse(currentDateStr);
                    return !expiry.before(currentDate); 
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper 
    public static int countMetersForCnic(String fileName, String cnic) {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1 && parts[1].equals(cnic)) {
                    count++; // Increment count if CNIC matches
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

   
    //Customer will be himself update their units
    public static boolean updateUnitsConsumed( String customerId, int regularUnits, int peakHourUnits) {
        File file = new File(CUSTOMER_FILE);
        StringBuilder updatedContent = new StringBuilder();
        boolean customerFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(customerId)) {
                    parts[8] = String.valueOf(regularUnits); // Regular units consumed
                    if (parts[6].equals("Three Phase")) {
                        parts[9] = String.valueOf(peakHourUnits); // Peak hour units
                    }
                    customerFound = true;
                }
                updatedContent.append(String.join(",", parts)).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (customerFound) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(updatedContent.toString());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static ArrayList<BillingInfo> viewBills(String customerId) {
        ArrayList<BillingInfo>list= new ArrayList<>();
        
        String billingFile = "BillingInfo.txt";
        int paidBillCount = 0;
        int unpaidBillCount = 0;
        DecimalFormat decimalFormat = new DecimalFormat("#.##"); // Format to 2 decimal places

        StringBuilder paidBills = new StringBuilder();
        StringBuilder unpaidBills = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(billingFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 10 && parts[0].equals(customerId)) {
                    
                    
                      BillingInfo bill = new BillingInfo(
                        parts[0],  // Customer ID
                        Integer.parseInt(parts[2]), // Regular Units Consumed
                        Integer.parseInt(parts[3]), // Peak Units Consumed
                        Double.parseDouble(parts[4]), // Total Electricity Cost
                        Double.parseDouble(parts[5]), // Sales Tax
                        Double.parseDouble(parts[6]), // Fixed Charges
                        Double.parseDouble(parts[7]), // Total Billing Amount
                        parts[8], // Due Date
                        parts[1]  // Reading Entry Date
                    );
                    String billDetails = "Bill Details:\n" +
                            "Customer ID: " + parts[0] + "\n" +
                            "Reading Entry Date: " + parts[1] + "\n" +
                            "Regular Units Consumed: " + parts[2] + "\n" +
                            "Peak Units Consumed: " + parts[3] + "\n" +
                            "Total Electricity Cost: " + decimalFormat.format(Double.parseDouble(parts[4])) + " Rs\n" +
                            "Sales Tax: " + decimalFormat.format(Double.parseDouble(parts[5])) + " Rs\n" +
                            "Fixed Charges: " + decimalFormat.format(Double.parseDouble(parts[6])) + " Rs\n" +
                            "Total Billing Amount: " + decimalFormat.format(Double.parseDouble(parts[7])) + " Rs\n" +
                            "Due Date: " + parts[8] + "\n";

                    String billStatus = parts[9].trim();  
                    bill.setBillStatus(billStatus);
                    list.add(bill);
                    if (billStatus.equalsIgnoreCase("Paid")) {
                        
                        paidBills.append(billDetails)
                                .append("Bill Status: " + ANSI_GREEN + "Paid" + ANSI_RESET + "\n\n");
                        paidBillCount++;
                    } else {
                        unpaidBills.append(billDetails)
                                .append("Bill Status: " + ANSI_RED + "Unpaid" + ANSI_RESET + "\n\n");
                        unpaidBillCount++;
                    }
                  
                  
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Display the report
        if (paidBillCount > 0) {
            System.out.println("Paid Bills (" + paidBillCount + "):\n");
            System.out.println(paidBills.toString());
        } else {
            System.out.println("No paid bills found for Customer ID " + customerId);
        }

        if (unpaidBillCount > 0) {
            System.out.println("Unpaid Bills (" + unpaidBillCount + "):\n");
            System.out.println(unpaidBills.toString());
        } else {
            System.out.println("No unpaid bills found for Customer ID " + customerId);
        }
        return list;
    }
    
    
    
  
    public static boolean updateCNICExpiryDate(String cnic, String customerId, int day, int month, int year) {
        String nadraFile = "NADRADB.txt";
        StringBuilder fileContent = new StringBuilder();
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(nadraFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].equals(cnic)) {
                    // Assuming the CNIC belongs to the customer, update the expiry date
                    // Update the expiry date to the new date provided
                    parts[2] = String.format("%02d/%02d/%04d", day, month, year);
                    updated = true;
                }
                fileContent.append(String.join(",", parts)).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (updated) {
            try (FileWriter writer = new FileWriter(nadraFile)) {
                writer.write(fileContent.toString());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }//Helper
    private static boolean isCnicBelongsToCustomer(String cnic, String customerId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String fileCustomerId = parts[0];
                    String fileCnic = parts[1];
                    if (fileCustomerId.equals(customerId) && fileCnic.equals(cnic)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public static boolean loginForCustomer(String customerId, String cnic) {
    String customerFile = "CustomerInfo.txt";
    
    try (BufferedReader reader = new BufferedReader(new FileReader(customerFile))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 2) {
                String id = parts[0]; // Assuming Customer ID is in the 1st column
                String cnicFromFile = parts[1]; // Assuming CNIC is in the 2nd column

                // Check if provided CNIC and Customer ID match
                if (cnicFromFile.equals(cnic) && id.equals(customerId)) {
                    return true;
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return false;
}
 public static ArrayList<String> predictBill(String customerId, int regularUnitsConsumed, int peakUnitsConsumed) {
  boolean flag=false;
    ArrayList<String> list = new ArrayList<>();

    // Prompt for customer ID
    list.add("Customer ID: " + customerId);

    // Get customer data
    String customerData = getCustomerData(customerId, CUSTOMER_FILE);
    if (customerData == null) {
        System.out.println("Customer ID not found.");
        return null;  // Return null if no data is found
    }

    String[] parts = customerData.split(",");
    String meterType = parts[6];
    String customerType = parts[5];

    // Add regular units consumed
    list.add("Regular Units Consumed: " + regularUnitsConsumed);

    // Handle peak units for Three Phase connections
    if (meterType.equals("Three Phase")) {
        flag=true;
        list.add("Peak Units Consumed: " + peakUnitsConsumed);
    } else {
        peakUnitsConsumed = 0;  // Set peak units to 0 for Single Phase
        list.add("Peak Units Consumed: " + peakUnitsConsumed);
    }

    // Load tariff information
    Map<String, String[]> tariffInfo = loadTariffTaxInfo();
    String tariffKey = meterType + " " + customerType;
    String[] tariffData = tariffInfo.get(tariffKey);

    if (tariffData == null) {
        System.out.println("Tariff data not found.");
        return null;  // Return null if no tariff data is found
    }

    double regularUnitPrice = Double.parseDouble(tariffData[1]);
    double peakHourUnitPrice = meterType.equals("Three Phase") ? Double.parseDouble(tariffData[2]) : 0;
    double salesTaxRate = Double.parseDouble(tariffData[3]);
    double fixedCharges = Double.parseDouble(tariffData[4]);

    // Add tariff information to the list
    list.add("Sales Tax Rate: " + salesTaxRate + "%");
    list.add("Fixed Charges: " + fixedCharges + " Rs");

    
    double regularCost = regularUnitsConsumed * regularUnitPrice;
    double peakHourCost = peakUnitsConsumed * peakHourUnitPrice;
    double totalElectricityCost = regularCost + peakHourCost;
    double salesTax = totalElectricityCost * (salesTaxRate / 100);
    double totalBillingAmount = totalElectricityCost + salesTax + fixedCharges;

     DecimalFormat decimalFormat = new DecimalFormat("#.##");

    list.add("Total Electricity Cost: " + decimalFormat.format(totalElectricityCost) + " Rs");
    list.add("Sales Tax: " + decimalFormat.format(salesTax) + " Rs");
    list.add("Total Billing Amount: " + decimalFormat.format(totalBillingAmount) + " Rs");
    if(flag)
    {
        list.add("true");
    }
    else{
    list.add("false");}
    return list;  // Return the ArrayList containing all the details
}

    // Helper method to get customer data
    private static String getCustomerData(String customerId, String fileName) {
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
    private static Map<String, String[]> loadTariffTaxInfo() {
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
                    tariffInfo.put(key, parts);
                    counter++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tariffInfo;
    }

    public String getCnicExpiryDate() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getPhone() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
public static String getCustomerMeterType(String customerId) {
    File file = new File(CUSTOMER_FILE);
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts[0].equals(customerId)) {
                return parts[6];  // Assuming meter type is in the 6th position in the CSV file
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;  // Return null if customer not found
}

     public static String getCustomerName(String customerId) {
        File file = new File(CUSTOMER_FILE);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Assuming the customer ID is in the first column and name in the second
                if (parts[0].equals(customerId)) {
                    return parts[2];  // Assuming name is in the second column (index 1)
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;  // Return null if customer ID not found
    }
    
  
}
