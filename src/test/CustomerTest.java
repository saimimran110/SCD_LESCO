package test;

import lesco.bill.system.a1.pkg22l.pkg7906.Customer;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    private Path tempCustomerFile;
    private Path tempNadraFile;

    @BeforeEach
    void setup() throws IOException {
        // Create temporary files for testing
        tempCustomerFile = Paths.get("CustomerInfo.txt");
        tempNadraFile = Paths.get("NADRADB.txt");

        // Write initial data to the temporary files
        Files.write(tempCustomerFile, List.of(
                "4166,5440070637191,Saim Imran,Some Address,03332306481,Commercial,Three Phase,26-11-2024,0,0"
        ));
        Files.write(tempNadraFile, List.of(
                "5440070637191,17/09/2005,20/10/2025"
        ));
    }

    @AfterEach
    void cleanup() throws IOException {
        // Clean up temporary files
        Files.deleteIfExists(tempCustomerFile);
        Files.deleteIfExists(tempNadraFile);
    }

    @Test
    void testAddCustomerSuccess() {
        int result = Customer.addCustomer(
                "5440070637191", "Saim", "Some Address", "03333333333",
                "Domestic", "Single Phase", "30-11-2024"
        );
        assertEquals(0, result); // Success code
    }

    @Test
    void testAddCustomerInvalidCNIC() {
        int result = Customer.addCustomer(
                "69696969669", "HANNNJII", "Some Address", "0332658449",
                "Domestic", "Single Phase", "30-11-2024"
        );
        assertEquals(1, result); // Error code for invalid CNIC
    }

    @Test
    void testAddCustomerMaxMetersReached() throws IOException {
        Files.write(tempCustomerFile, List.of(
                "4166,5440070637191,Saim Imran,Some Address,03332306481,Commercial,Three Phase,26-11-2024,0,0",
                "4167,5440070637191,Talha,Another Address,03331234567,Commercial,Single Phase,27-11-2024,0,0",
                "4168,5440070637191,Abdullah,Third Address,03337654321,Domestic,Single Phase,28-11-2024,0,0"
        ), StandardOpenOption.APPEND);

        int result = Customer.addCustomer(
                "5440070637191", "Exceeding Name", "Exceeding Address", "03339999999",
                "Domestic", "Single Phase", "30-11-2024"
        );
        assertEquals(2, result); // Error code for exceeding meters
    }

    @Test
    void testUpdateUnitsConsumedSuccess() {
        try {
            // Debugging: Print the file path and check if it exists
            System.out.println("Customer File Path: " + tempCustomerFile.toAbsolutePath());
            assertTrue(Files.exists(tempCustomerFile), "Customer file does not exist!");

            // Call the method
            boolean result = Customer.updateUnitsConsumed("4166", 100, 50);
            assertTrue(result);

            // Read all lines from the file to verify
            List<String> lines = Files.readAllLines(tempCustomerFile);
            assertTrue(lines.get(0).contains(",100,50")); // Regular and Peak units updated
        } catch (IOException e) {
            fail("IOException occurred while reading the file: " + e.getMessage());
        }
    }


    @Test
    void testUpdateUnitsConsumedCustomerNotFound() {
        boolean result = Customer.updateUnitsConsumed("9999", 100, 50);
        assertFalse(result); // Customer not found
    }

    @Test
    void testLoginForCustomerSuccess() {
        boolean result = Customer.loginForCustomer("4166", "5440070637191");
        assertTrue(result); // Login should succeed
    }

    @Test
    void testLoginForCustomerFailure() {
        boolean result = Customer.loginForCustomer("4166", "1234567890");
        assertFalse(result); // Invalid CNIC, login fails
    }
}
