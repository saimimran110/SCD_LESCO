package lesco.bill.system.a1.pkg22l.pkg7906;


import GUI.StartingPage;


public class LESCOBILLSYSTEMA122l7906 {

    public static void main(String[] args) {
        
   
        new StartingPage();
   
//        Scanner scanner = new Scanner(System.in);
//
//        while (true) {
//            // Main menu
//            System.out.println("********** Main Menu **********");
//            System.out.println("1. Add Employee");
//            System.out.println("2. Select Role");
//            System.out.println("3. Exit");
//            System.out.print("Select an option: ");
//            int option = scanner.nextInt();
//            scanner.nextLine(); // Consume newline
//
//            switch (option) {
//                case 1:
//                    // Add Employee
//                    System.out.print("Enter username: ");
//                    String username = scanner.nextLine();
//                    System.out.print("Enter password: ");
//                    String password = scanner.nextLine();
//                    boolean added = Employee.addEmployeeInFile(username, password);
//                    if (added) {
//                        System.out.println("Employee added successfully!");
//                    } else {
//                        System.out.println("Failed to add employee. Username may already exist.");
//                    }
//                    break;
//
//                case 2:
//                    // Select Role and Login
//                    System.out.println("Select your role:");
//                    System.out.println("1. Employee");
//                    System.out.println("2. Customer");
//                    int role = scanner.nextInt();
//                    scanner.nextLine(); // Consume newline
//
//                    switch (role) {
//                        case 1:
//                            // Employee login
//                            System.out.print("Enter username: ");
//                            String empUsername = scanner.nextLine();
//                            System.out.print("Enter password: ");
//                            String empPassword = scanner.nextLine();
//
//                            if (Employee.employeeLogin(empUsername, empPassword)) {
//                                System.out.println("Login Successful!");
//                                handleEmployeeTasks(scanner);
//                            } else {
//                                System.out.println("Login Failed! Incorrect username or password.");
//                            }
//                            break;
//
//                        case 2:
//                            // Customer login
//                            System.out.print("Enter Customer ID: ");
//                            String custId = scanner.nextLine();
//                            System.out.print("Enter CNIC: ");
//                            String cnic = scanner.nextLine();
//
//                            if (Customer.loginForCustomer(custId, cnic)) {
//                                System.out.println("Login Successful!");
//                                handleCustomerTasks(scanner);
//                            } else {
//                                System.out.println("Login Failed! Incorrect Customer ID or CNIC.");
//                            }
//                            break;
//
//                        default:
//                            System.out.println("Invalid role selection.");
//                            break;
//                    }
//                    break;
//
//                case 3:
//                    System.out.println("Exiting...");
//                    scanner.close();
//                    System.exit(0);
//                    break;
//
//                default:
//                    System.out.println("Invalid option. Please try again.");
//                    break;
//            }
//        }
//    }
//
// private static void handleEmployeeTasks(Scanner scanner) {
//    while (true) {
//        System.out.println("********** Employee Tasks **********");
//        System.out.println("1. Update Bill Status");
//        System.out.println("2. Update Password");
//        System.out.println("3. Generate Bill");
//        System.out.println("4. View Customers with Expiring CNICs");
//        System.out.println("5. View Paid and Unpaid Bills");
//        System.out.println("6. Add Customer");
//        System.out.println("7.Update Terrif Taxes");
//        System.out.println("8. Exit");
//        System.out.print("Select an option: ");
//        int taskOption = scanner.nextInt();   
//        scanner.nextLine(); // Consume newline
//
//        switch (taskOption) {
//            case 1:
//                // Update Bill Status
//                System.out.print("Enter Customer ID: ");
//                String customerId = scanner.nextLine();
               // Employee.UpdateTheBillStatusAfterBilling("9040");
//                System.out.println("Bill status updated.");
//                break;
//
//            case 2:
//                // Update Password
//                System.out.print("Enter username: ");
//                String username = scanner.nextLine();
//                System.out.print("Enter current password: ");
//                String currentPassword = scanner.nextLine();
//                System.out.print("Enter new password: ");
//                String newPassword = scanner.nextLine();
//                if (Employee.updatePassword(username, currentPassword, newPassword)) {
//                    System.out.println("Password updated successfully.");
//                } else {
//                    System.out.println("Wrong Credentials.");
//                }
//                break;
//
//            case 3:
//                // Generate Bill
//                System.out.print("Enter Customer ID: ");
//                String billCustomerId = scanner.nextLine();
               // Employee.GenerateElectricityBillForCustomer("9040");
//                System.out.println("Bill generated.");
//                break;
//
//            case 4:
//                // View Expiring CNICs
//                Employee.viewExpiringCNICs();
//                break;
//
//            case 5:
//                // View Paid and Unpaid Bills
//                System.out.print("Enter Customer ID: ");
//                String billCustomerIdForView = scanner.nextLine();
//                Employee.viewPaidAndUnpaidBills(billCustomerIdForView);
//                break;
//
//            case 6:
//                // Add Customer
//                System.out.print("Enter CNIC: ");
//                String cnic = scanner.nextLine();
//                System.out.print("Enter Name: ");
//                String name = scanner.nextLine();
//                System.out.print("Enter Address: ");
//                String address = scanner.nextLine();
//                System.out.print("Enter Phone Number: ");
//                String phoneNum = scanner.nextLine();
//                
//                System.out.println("Select Customer Type:");
//                System.out.println("1. Commercial");
//                System.out.println("2. Domestic");
//                int customerTypeOption = scanner.nextInt();
//                scanner.nextLine(); // Consume newline
//                String customerType = (customerTypeOption == 1) ? "Commercial" : "Domestic";
//                
//                System.out.println("Select Meter Type:");
//                System.out.println("1. Single Phase");
//                System.out.println("2. Three Phase");
//                int meterTypeOption = scanner.nextInt();
//                scanner.nextLine(); // Consume newline
//                String meterType = (meterTypeOption == 1) ? "Single Phase" : "Three Phase";
//                
//                // Get today's date in dd-MM-yyyy format
//                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//                String connectionDate = sdf.format(new Date());
//                
//                Customer.addCustomer(cnic, name, address, phoneNum, customerType, meterType, connectionDate);
//                break;
//
//            case 7:
//                Employee.UpdateTerrifInfo();
//            case 8:
//                return;
//            default:
//                System.out.println("Invalid option. Please try again.");
//                break;
//        }
//    }
//}
//
//
//    private static void handleCustomerTasks(Scanner scanner) {
//        while (true) {
//            System.out.println("********** Customer Tasks **********");
//            System.out.println("1. View Current Bill");
//            System.out.println("2. Update Units Consumed");
//            System.out.println("3. Update CNIC Expiry Date");
//            System.out.println("4. Predict Your Bill");
//            System.out.println("5. Exit");
//            System.out.print("Select an option: ");
//            int taskOption = scanner.nextInt();
//            scanner.nextLine(); // Consume newline
//
//            switch (taskOption) {
//                case 1:
//                    // View Current Bill
//                    System.out.print("Enter Customer ID: ");
//                    String customerId = scanner.nextLine();
                   // Customer.viewBills("9040");
//                    break;
//
//                case 2:
//                    // Update Units Consumed
//                    System.out.print("Enter Customer ID: ");
//                    customerId = scanner.nextLine();
//                    System.out.print("Enter Regular Units Consumed: ");
//                    int regularUnits = scanner.nextInt();
//                    System.out.print("Enter Peak Hours Units Consumed (enter 0 if not applicable): ");
//                    int peakUnits = scanner.nextInt();
//                    scanner.nextLine(); // Consume newline
//                    boolean unitsUpdated = Customer.updateUnitsConsumed(customerId, regularUnits, peakUnits);
//                    if (unitsUpdated) {
//                        System.out.println("Units consumed updated.");
//                    } else {
//                        System.out.println("Failed to update units consumed.");
//                    }
//                    break;
//
//                case 3:
//                    // Update CNIC Expiry Date
//                    boolean updated = Customer.updateCNICExpiryDate();
//                    if (updated) {
//                        System.out.println("CNIC expiry date updated successfully.");
//                    } else {
//                        System.out.println("Failed to update CNIC expiry date.");
//                    }
//                    break;
//                case 4:
//                    Customer.predictBill();
//                case 5:
//                    return;
//
//                default:
//                    System.out.println("Invalid option. Please try again.");
//                    break;
//            }
//        }
    }
}
