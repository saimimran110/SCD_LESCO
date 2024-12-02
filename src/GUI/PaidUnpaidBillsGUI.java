package GUI;

import Controller.EmployeeController;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import lesco.bill.system.a1.pkg22l.pkg7906.BillingInfo;
import lesco.bill.system.a1.pkg22l.pkg7906.ClientSocket;

public class PaidUnpaidBillsGUI {

    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton removeButton;
    private JTextField searchField;
    private ArrayList<BillingInfo> bills; // To store the bills
    private ArrayList<BillingInfo> filteredBills; // To store the filtered search results
    private EmployeeController cc;

    public PaidUnpaidBillsGUI() {
        // Initialize the JFrame
        cc = new EmployeeController();
        frame = new JFrame("Paid and Unpaid Bills");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.BLACK);

        // Create a panel for the title
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);
        JLabel titleLabel = new JLabel("Paid and Unpaid Bills");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.YELLOW);
        titlePanel.add(titleLabel);

        // Create the table model with column names
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        tableModel.addColumn("Customer ID");
        tableModel.addColumn("Regular Units");
        tableModel.addColumn("Peak Units");
        tableModel.addColumn("Total Amount (Rs)");
        tableModel.addColumn("Bill Status");
        tableModel.addColumn("Due Date");
        tableModel.addColumn("Reading Date"); // Add column for Reading Date

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setSelectionBackground(Color.YELLOW);
        table.setSelectionForeground(Color.BLACK);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);

        // Render bill status color
        table.getColumnModel().getColumn(4).setCellRenderer(new BillStatusRenderer());

        // Customize the table header
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.BLACK);
        header.setForeground(Color.YELLOW);
        header.setFont(new Font("Arial", Font.BOLD, 16));

        // Add the table to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));

        // Create panel for buttons and search field
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);

        // Create a search field
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        buttonPanel.add(new JLabel("Search: "));
        buttonPanel.add(searchField);

        // Add a search button
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(Color.YELLOW);
        searchButton.setForeground(Color.BLACK);
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        searchButton.addActionListener(e -> searchBills());
        buttonPanel.add(searchButton);

        // Remove button
        removeButton = new JButton("Remove Latest Bill");
        removeButton.setBackground(Color.RED);
        removeButton.setForeground(Color.WHITE);
        removeButton.setFont(new Font("Arial", Font.BOLD, 16));
        removeButton.addActionListener(e -> removeLatestBill());
        buttonPanel.add(removeButton);

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.setBackground(Color.YELLOW);
        closeButton.setForeground(Color.BLACK);
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.addActionListener(e -> frame.dispose());
        buttonPanel.add(closeButton);

        // Add components to the frame
        frame.setLayout(new BorderLayout());
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Prompt for customer ID and load data
        promptForCustomerIdAndLoadData();

        // Make the frame visible
        frame.setVisible(true);
    }

    private void promptForCustomerIdAndLoadData() {
        String customerId = JOptionPane.showInputDialog(frame, "Enter Customer ID:", "Customer ID", JOptionPane.QUESTION_MESSAGE);
        if (customerId != null && !customerId.trim().isEmpty()) {
            loadBillData(customerId);
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid Customer ID. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            frame.dispose();
        }
    }

    private void loadBillData(String customerId) {
        try {
            // Send the request to the server
            String request = String.format("Employee,VIEW_PAID_UNPAID_BILLS,%s", customerId);
            ClientSocket client = ClientSocket.getInstance();
            String response = client.sendRequest(request);

            if (response.startsWith("Error") || response.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No bills found for the given Customer ID.", "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                bills = parseBills(response); // Parse the response into a list of BillingInfo
                filteredBills = new ArrayList<>(bills); // Initialize filteredBills with all bills

                // Sort bills by reading date (latest first)
                bills.sort((bill1, bill2) -> {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date readingDate1 = dateFormat.parse(bill1.getReadingEntryDate());
                        Date readingDate2 = dateFormat.parse(bill2.getReadingEntryDate());
                        return readingDate2.compareTo(readingDate1); // Latest date first
                    } catch (Exception e) {
                        return 0;
                    }
                });

                // Add the sorted bills to the table
                displayBills(filteredBills);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to connect to the server. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private ArrayList<BillingInfo> parseBills(String response)
    {
        ArrayList<BillingInfo> bills = new ArrayList<>();
        String[] billEntries = response.split(";"); // Assuming bills are separated by ";"
        for (String entry : billEntries) {
            bills.add(BillingInfo.fromString(entry)); // Assuming BillingInfo has a static fromString method
        }
        return bills;
    }


    private void displayBills(ArrayList<BillingInfo> billsToDisplay) {
        tableModel.setRowCount(0); // Clear the table before displaying new data
        for (BillingInfo bill : billsToDisplay) {
            tableModel.addRow(new Object[]{
                    bill.getCustomerId(),
                    bill.getRegularUnitsConsumed(),
                    bill.getPeakUnitsConsumed(),
                    bill.getTotalBillingAmount(),
                    bill.getBillStatus(),
                    bill.getDueDate(),
                    bill.getReadingEntryDate() // Include reading date
            });
        }
    }

    private void searchBills() {
        String searchText = searchField.getText().toLowerCase();
        filteredBills.clear();

        // Filter bills based on search text
        for (BillingInfo bill : bills) {
            if (String.valueOf(bill.getRegularUnitsConsumed()).contains(searchText)
    || String.valueOf(bill.getPeakUnitsConsumed()).contains(searchText)
    || String.valueOf(bill.getTotalBillingAmount()).contains(searchText)
    || bill.getBillStatus().toLowerCase().contains(searchText)
    || bill.getDueDate().toLowerCase().contains(searchText)
    || bill.getReadingEntryDate().toLowerCase().contains(searchText)) {
    filteredBills.add(bill); // Add matching bills to filteredBills
}

        }

        // Display filtered bills in the table
        displayBills(filteredBills);
    }

    private void removeLatestBill() {
        if (bills != null && !bills.isEmpty()) {
            // Get the latest bill based on the reading date
            BillingInfo latestBill = bills.get(0); // The first bill is the latest due to sorting

            try {
                // Send the request to the server to remove the bill
                String request = String.format("Employee,REMOVE_LATEST_BILL,%s", latestBill.getReadingEntryDate());
                ClientSocket client = ClientSocket.getInstance();
                String response = client.sendRequest(request);

                if ("Bill removed successfully.".equalsIgnoreCase(response)) {
                    // Remove the latest bill from the table and the bills list
                    bills.remove(0);
                    tableModel.removeRow(0);

                    JOptionPane.showMessageDialog(frame, response, "Information", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, response, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Failed to connect to the server. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "No bills available to remove.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Custom renderer to show bill status in different colors
    private class BillStatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value != null) {
                String status = value.toString().toLowerCase();
                if (status.equals("unpaid")) {
                    c.setBackground(Color.RED);
                    c.setForeground(Color.WHITE);
                } else if (status.equals("paid")) {
                    c.setBackground(Color.GREEN);
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }
            }

            return c;
        }
    }
}
