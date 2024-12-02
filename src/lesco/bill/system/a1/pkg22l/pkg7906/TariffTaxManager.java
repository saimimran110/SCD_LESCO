package lesco.bill.system.a1.pkg22l.pkg7906;

import java.io.*;
import java.util.Scanner;

public class TariffTaxManager {
    private static final String TARIF_TAX_FILE = "TerrifTaxInfo.txt";

    // Method to ensure exactly 4 rows in the file
    public static void ensureFourRows() {
        File file = new File(TARIF_TAX_FILE);
        if (!file.exists()) {
            System.out.println("File does not exist. Please create the file first.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            long lineCount = reader.lines().count();
            if (lineCount != 4) {
                System.out.println("File does not contain exactly 4 rows. Correcting...");
                resetFile();  // Create file with default values if necessary
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to reset file to default values (4 rows)
    private static void resetFile() {
        String[] defaultContent = {
                "Single Phase,5,,17,150",
                "Single Phase,15,,20,250",
                "Three Phase,8,12,17,150",
                "Three Phase,18,25,20,250"
        };
        try (FileWriter writer = new FileWriter(TARIF_TAX_FILE)) {
            for (String line : defaultContent) {
                writer.write(line + "\n");
            }
            System.out.println("Default TariffTaxInfo file reset.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to update tariff/tax information
    // Method to update tariff/tax information
    public static void updateTariffTaxInfo() {
        Scanner scanner = new Scanner(System.in);

        ensureFourRows();

        // Prompt user for input
        int meterTypeOption = 0;
        while (meterTypeOption != 1 && meterTypeOption != 2) {
            System.out.println("Select Meter Type:");
            System.out.println("1. Single Phase");
            System.out.println("2. Three Phase");
            if (scanner.hasNextInt()) {
                meterTypeOption = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } else {
                System.out.println("Invalid input. Please enter 1 or 2.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        String meterType = meterTypeOption == 1 ? "Single Phase" : "Three Phase";

        // Select customer type
        int customerTypeOption = 0;
        while (customerTypeOption != 1 && customerTypeOption != 2) {
            System.out.println("Select Customer Type:");
            System.out.println("1. Domestic");
            System.out.println("2. Commercial");
            if (scanner.hasNextInt()) {
                customerTypeOption = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } else {
                System.out.println("Invalid input. Please enter 1 or 2.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        String customerType = customerTypeOption == 1 ? "Domestic" : "Commercial";

        // Determine row index
        int row = getRowIndex(meterType, customerType);

        // Collect new values
        System.out.println("Enter new Regular Unit Price:");
        String regularUnitPrice = scanner.nextLine();

        String peakHourUnitPrice = "";
        if (meterType.equals("Single Phase")) {
            System.out.println("Peak hour unit price is not applicable for Single Phase meters. Setting it to empty.");
        } else {
            System.out.println("Enter new Peak Hour Unit Price:");
            peakHourUnitPrice = scanner.nextLine();
        }

        System.out.println("Enter new Tax Percentage:");
        String taxPercentage = scanner.nextLine();

        System.out.println("Enter new Fixed Charges:");
        String fixedCharges = scanner.nextLine();

        String newEntry = String.format("%s,%s,%s,%s,%s",
                meterType, regularUnitPrice, peakHourUnitPrice, taxPercentage, fixedCharges);

        // Update the file with new entry
        updateFileRow(row, newEntry);


    }

    // Determine row index based on meter and customer type
    public static int getRowIndex(String meterType, String customerType) {
        if (meterType.equals("Single Phase")) {
            return customerType.equals("Domestic") ? 1 : 2;
        } else {
            return customerType.equals("Domestic") ? 3 : 4;
        }
    }

    // Method to update a specific row in the file
    public static void updateFileRow(int row, String newEntry) {
        File file = new File(TARIF_TAX_FILE);
        StringBuilder updatedContent = new StringBuilder();
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int currentRow = 1;
            while ((line = reader.readLine()) != null) {
                if (currentRow == row) {
                    updatedContent.append(newEntry).append("\n");
                    updated = true;
                } else {
                    updatedContent.append(line).append("\n");
                }
                currentRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (updated) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(updatedContent.toString());
                System.out.println("Tariff/Tax information updated for row " + row);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Row " + row + " not found.");
        }
    }

    public static String getRowContent(int row) {
        File file = new File(TARIF_TAX_FILE);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int currentRow = 1;
            while ((line = reader.readLine()) != null) {
                if (currentRow == row) {
                    return line;  // Return the content of the specified row
                }
                currentRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";  // Return an empty string if the row is not found
    }

}