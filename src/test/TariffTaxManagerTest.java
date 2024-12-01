package test;

import lesco.bill.system.a1.pkg22l.pkg7906.TariffTaxManager;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TariffTaxManagerTest
{

    private static final String TARIF_TAX_FILE = "TerrifTaxInfo.txt";
    private Path backupFile;

    @BeforeEach
    void setup() throws IOException {
        // Backup the original file if it exists
        Path originalFile = Paths.get(TARIF_TAX_FILE);
        backupFile = originalFile.resolveSibling("TerrifTaxInfo_backup.txt");

        if (Files.exists(originalFile)) {
            Files.move(originalFile, backupFile);
        }

        // Write initial test data
        Files.write(originalFile, Arrays.asList(
                "Single Phase,5,,17,150",
                "Single Phase,15,,20,250",
                "Three Phase,60,55,2,5",
                "Three Phase,43,90,31,100"
        ));
    }

    @AfterEach
    void cleanup() throws IOException {
        // Remove the test file
        Files.deleteIfExists(Paths.get(TARIF_TAX_FILE));

        // Restore the original file from backup
        if (Files.exists(backupFile)) {
            Files.move(backupFile, Paths.get(TARIF_TAX_FILE));
        }
    }

    @Test
    void testEnsureFourRowsWithValidFile() {
        // Verify the file initially has 4 rows
        TariffTaxManager.ensureFourRows();

        try {
            List<String> lines = Files.readAllLines(Paths.get(TARIF_TAX_FILE));
            assertEquals(4, lines.size(), "File should have exactly 4 rows.");
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    void testEnsureFourRowsWithInvalidFile() throws IOException {
        // Modify the file to have fewer rows
        Files.write(Paths.get(TARIF_TAX_FILE), Arrays.asList(
                "Single Phase,5,,17,150",
                "Single Phase,15,,20,250"
        ));

        TariffTaxManager.ensureFourRows();

        try {
            List<String> lines = Files.readAllLines(Paths.get(TARIF_TAX_FILE));
            assertEquals(4, lines.size(), "File should have been corrected to 4 rows.");
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    void testUpdateTariffTaxInfo() {
        String originalRowContent = TariffTaxManager.getRowContent(3);
        String newEntry = "Three Phase,10,20,30,400";

        // Update the 3rd row
        TariffTaxManager.updateFileRow(3, newEntry);

        String updatedRowContent = TariffTaxManager.getRowContent(3);

        assertNotEquals(originalRowContent, updatedRowContent, "Row content should be updated.");
        assertEquals(newEntry, updatedRowContent, "Updated row content should match the new entry.");
    }

    @Test
    void testUpdateFileRowInvalidRow() {
        String invalidEntry = "Invalid,Entry,,0,0";

        // Try updating an invalid row
        TariffTaxManager.updateFileRow(5, invalidEntry);

        try {
            List<String> lines = Files.readAllLines(Paths.get(TARIF_TAX_FILE));
            assertFalse(lines.stream().anyMatch(line -> line.equals(invalidEntry)), "Invalid entry should not be added.");
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    void testGetRowContent() {
        String expectedRowContent = "Three Phase,60,55,2,5";

        String rowContent = TariffTaxManager.getRowContent(3);

        assertEquals(expectedRowContent, rowContent, "Row content should match the expected content.");
    }

    @Test
    void testGetRowContentInvalidRow() {
        String rowContent = TariffTaxManager.getRowContent(5);

        assertEquals("", rowContent, "Invalid row should return an empty string.");
    }

    @Test
    void testResetFile()
    {
        // Simulate an empty file
        try {
            Files.write(Paths.get(TARIF_TAX_FILE), Collections.emptyList());
        } catch (IOException e) {
            fail("IOException occurred while setting up test: " + e.getMessage());
        }

        TariffTaxManager.ensureFourRows();

        try {
            List<String> lines = Files.readAllLines(Paths.get(TARIF_TAX_FILE));
            assertEquals(4, lines.size(), "File should have been reset to 4 rows.");
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
}
