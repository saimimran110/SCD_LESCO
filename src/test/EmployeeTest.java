package test;

import lesco.bill.system.a1.pkg22l.pkg7906.Employee;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest
{

    private Path tempEmployeeFile;
    private Path tempBillingFile;
    private Path tempCustomerFile;

    private Path employeeBackupFile;
    private Path billingBackupFile;
    private Path customerBackupFile;

    @BeforeEach
    void setup() throws IOException {
        // Define paths for temporary files
        tempEmployeeFile = Paths.get("EmployeeData.txt");
        tempBillingFile = Paths.get("BillingInfo.txt");
        tempCustomerFile = Paths.get("CustomerInfo.txt");

        // Backup original files if they exist
        employeeBackupFile = tempEmployeeFile.resolveSibling("EmployeeData_backup.txt");
        billingBackupFile = tempBillingFile.resolveSibling("BillingInfo_backup.txt");
        customerBackupFile = tempCustomerFile.resolveSibling("CustomerInfo_backup.txt");

        if (Files.exists(tempEmployeeFile)) {
            Files.move(tempEmployeeFile, employeeBackupFile);
        }
        if (Files.exists(tempBillingFile)) {
            Files.move(tempBillingFile, billingBackupFile);
        }
        if (Files.exists(tempCustomerFile)) {
            Files.move(tempCustomerFile, customerBackupFile);
        }

        // Write initial data to the temporary files
        Files.write(tempEmployeeFile, Arrays.asList(
                "Saim,12345",
                "Talha,password123",
                "Abdullah,securepass"
        ));
        Files.write(tempBillingFile, Arrays.asList(
                "1234,28/10/2024,50,10,1000.0,150.0,250.0,1400.0,05/11/2024,Unpaid,"
        ));
        Files.write(tempCustomerFile, Arrays.asList(
                "1234,5440070637191,Saim Imran,Lahore,03332306481,Commercial,Three Phase,26-11-2024,50,10"
        ));
    }

    @AfterEach
    void cleanup() throws IOException
    {
        // Delete temporary files
        Files.deleteIfExists(tempEmployeeFile);
        Files.deleteIfExists(tempBillingFile);
        Files.deleteIfExists(tempCustomerFile);

        // Restore original files from backups
        if (Files.exists(employeeBackupFile)) {
            Files.move(employeeBackupFile, tempEmployeeFile);
        }
        if (Files.exists(billingBackupFile)) {
            Files.move(billingBackupFile, tempBillingFile);
        }
        if (Files.exists(customerBackupFile)) {
            Files.move(customerBackupFile, tempCustomerFile);
        }
    }

    @Test
    void testAddEmployeeInFileSuccess() {
        boolean result = Employee.addEmployeeInFile("Talha", "newpassword123");
        assertFalse(result, "Yes Employee Already Exists");
    }

    @Test
    void testAddEmployeeInFileDuplicateUsername() {
        boolean result = Employee.addEmployeeInFile("Saim", "newpassword");
        assertFalse(result, "Duplicate username should not be allowed.");
    }

    @Test
    void testEmployeeLoginSuccess() {
        boolean result = Employee.employeeLogin("Saim", "12345");
        assertTrue(result, "Login should succeed for valid credentials.");
    }

    @Test
    void testEmployeeLoginFailure() {
        boolean result = Employee.employeeLogin("Talha", "wrongpassword");
        assertFalse(result, "Login should fail for invalid credentials.");
    }

    @Test
    void testUpdatePasswordSuccess() {
        boolean result = Employee.updatePassword("Abdullah", "securepass", "newsecurepassword");
        assertTrue(result, "Password should be updated successfully.");

        // Verify the password is updated in the file
        try {
            List<String> lines = Files.readAllLines(tempEmployeeFile);
            assertTrue(lines.stream().anyMatch(line -> line.contains("Abdullah,newsecurepassword")));
        } catch (IOException e) {
            fail("IOException occurred while verifying the file: " + e.getMessage());
        }
    }

    @Test
    void testUpdatePasswordFailure() {
        boolean result = Employee.updatePassword("Saim", "wrongpassword", "newsecurepassword");
        assertFalse(result, "Password update should fail for incorrect current password.");
    }

    @Test
    void testUpdateTheBillStatusAfterBillingSuccess() {
        // Define the expected line as a string
        String expectedLine = "1234,28/10/2024,50,10,1000.0,150.0,250.0,1400.0,05/11/2024,Paid";

        // Call the method to update the bill status
        boolean result = Employee.UpdateTheBillStatusAfterBilling("1234");
        assertTrue(result, "Bill status should be updated successfully.");

        // Verify the updated content of the billing file
        try {
            List<String> lines = Files.readAllLines(tempBillingFile);

            // Debug: Print all actual lines from the file
            System.out.println("Actual Billing File Content:");
            for (String line : lines) {
                System.out.println(line);
            }

            // Check if the expected line is present in the file
            boolean matchFound = lines.stream()
                    .anyMatch(line -> line.equals(expectedLine));

            assertTrue(matchFound, "Expected line not found in the billing file.");
        } catch (IOException e) {
            fail("IOException occurred while verifying the file: " + e.getMessage());
        }
    }
}
