package test;

import lesco.bill.system.a1.pkg22l.pkg7906.BillingInfo;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BillingInfoTest {

    private Path originalCustomerFile;
    private Path originalBillingFile;
    private Path originalTariffFile;

    @BeforeEach
    void setup() throws IOException {
        // Save original files if they exist
        originalCustomerFile = Paths.get("CustomerInfo.txt");
        originalBillingFile = Paths.get("BillingInfo.txt");
        originalTariffFile = Paths.get("TerrifTaxInfo.txt");

        // Back up original files
        if (Files.exists(originalCustomerFile)) {
            Files.move(originalCustomerFile, originalCustomerFile.resolveSibling("CustomerInfo_backup.txt"));
        }
        if (Files.exists(originalBillingFile)) {
            Files.move(originalBillingFile, originalBillingFile.resolveSibling("BillingInfo_backup.txt"));
        }
        if (Files.exists(originalTariffFile)) {
            Files.move(originalTariffFile, originalTariffFile.resolveSibling("TerrifTaxInfo_backup.txt"));
        }

        // Create test files
        Files.write(originalCustomerFile, List.of(
                "8447,5440070637191,Saim Imran,adads,03332306481,Commercial,Three Phase,26-11-2024,10,5"
        ));
        Files.write(originalBillingFile, List.of(
                "8447,28/10/2024,56,0,840.0,168.0,250.0,1258.0,04/11/2024,Unpaid,"
        ));
        Files.write(originalTariffFile, List.of(
                "Three Phase,60,55,2,5",
                "Three Phase,43,90,31,100"
        ));
    }

    @AfterEach
    void cleanup() throws IOException {
        // Remove test files
        Files.deleteIfExists(originalCustomerFile);
        Files.deleteIfExists(originalBillingFile);
        Files.deleteIfExists(originalTariffFile);

        // Restore original files
        if (Files.exists(originalCustomerFile.resolveSibling("CustomerInfo_backup.txt"))) {
            Files.move(originalCustomerFile.resolveSibling("CustomerInfo_backup.txt"), originalCustomerFile);
        }
        if (Files.exists(originalBillingFile.resolveSibling("BillingInfo_backup.txt"))) {
            Files.move(originalBillingFile.resolveSibling("BillingInfo_backup.txt"), originalBillingFile);
        }
        if (Files.exists(originalTariffFile.resolveSibling("TerrifTaxInfo_backup.txt"))) {
            Files.move(originalTariffFile.resolveSibling("TerrifTaxInfo_backup.txt"), originalTariffFile);
        }
    }

    @Test
    void testFromStringValidData() {
        String data = "8447,28/10/2024,56,0,840.0,168.0,250.0,1258.0,04/11/2024,Unpaid,";
        BillingInfo bill = BillingInfo.fromString(data);

        assertEquals("8447", bill.getCustomerId());
        assertEquals(56, bill.getRegularUnitsConsumed());
        assertEquals(0, bill.getPeakUnitsConsumed());
        assertEquals(1258.0, bill.getTotalBillingAmount());
        assertEquals("Unpaid", bill.getBillStatus());
    }

    @Test
    void testFromStringInvalidData() {
        String invalidData = "invalid,data,missing,fields";
        assertThrows(IllegalArgumentException.class, () -> BillingInfo.fromString(invalidData));
    }

    @Test
    void testGenerateBillSuccess() throws IOException {
        // Simulate generating a bill
        boolean result = BillingInfo.generateBill("8447");

        assertTrue(result);

        // Verify the new bill entry was added to the billing file
        List<String> lines = Files.readAllLines(originalBillingFile);
        assertEquals(2, lines.size()); // Original entry + new entry
        assertTrue(lines.get(1).contains("8447"));
    }
}
