package GUI;

import Controller.CustomerController;
import lesco.bill.system.a1.pkg22l.pkg7906.BillingInfo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ViewCurrentBillUI extends JFrame {
    private CustomerController customerController;
    private String customerId;
    private JTable billTable;
    private DefaultTableModel tableModel;
    private ArrayList<BillingInfo> billList;
    private JTextField searchField;

    public ViewCurrentBillUI() {
        // Ask for the customer ID using a dialog
        customerId = JOptionPane.showInputDialog(this, "Enter Your Customer ID:", "Customer ID Input", JOptionPane.PLAIN_MESSAGE);
        
        if (customerId == null || customerId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Customer ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        customerController = new CustomerController();  // Create an instance of the CustomerController

        setTitle("LESCO BILLING SYSTEM - View Current Bill");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header
        JLabel headerLabel = new JLabel("Current Bill for Customer ID: " + customerId);
        headerLabel.setForeground(Color.BLACK);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table Model and JTable
        String[] columnNames = {
                "Customer ID", "Reading Entry Date", "Regular Units", "Peak Units",
                "Electricity Cost", "Sales Tax", "Fixed Charges", "Total Amount", "Due Date", "Bill Status"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Prevent cell editing
            }
        };

        billTable = new JTable(tableModel);
        billTable.getTableHeader().setBackground(Color.BLACK);
        billTable.getTableHeader().setForeground(Color.YELLOW);
        billTable.setBackground(Color.WHITE);
        billTable.setForeground(Color.BLACK);
        billTable.setFont(new Font("Arial", Font.PLAIN, 14));
        billTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(billTable);

        // Fetch bills from the controller
        billList = customerController.VIEW_BILL(customerId);

        // Add bills to the table
        populateTable(billList);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.YELLOW);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> dispose());  // Close the current window and go back

        // Add components to the main panel
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        // Move Search Panel to the bottom and make it smaller
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));  // Align components horizontally
        searchPanel.setBackground(Color.BLACK);
        searchPanel.setPreferredSize(new Dimension(800, 50));  // Make it smaller

        // Adjust Search Label
        JLabel searchLabel = new JLabel("Search: ");
        searchLabel.setForeground(Color.YELLOW);
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        searchPanel.add(searchLabel);

        // Adjust Search Field
        searchField = new JTextField(15);  // Reduce the width of the text field
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchPanel.add(searchField);

        // Adjust Search Button
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(Color.YELLOW);
        searchButton.setForeground(Color.BLACK);
        searchButton.setFocusPainted(false);
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setPreferredSize(new Dimension(100, 30));  // Adjust button size
        searchButton.addActionListener(e -> searchBill());
        searchPanel.add(searchButton);

        mainPanel.add(searchPanel, BorderLayout.SOUTH);  // Add search panel to the bottom

        add(mainPanel);
        setVisible(true);
    }

    // Populate the table with the bill list
    private void populateTable(ArrayList<BillingInfo> bills) {
        tableModel.setRowCount(0);  // Clear existing rows

        for (BillingInfo bill : bills) {
            Object[] row = {
                    bill.getCustomerId(),
                    bill.getReadingEntryDate(),
                    bill.getRegularUnitsConsumed(),
                    bill.getPeakUnitsConsumed(),
                    bill.getTotalElectricityCost(),
                    bill.getSalesTax(),
                    bill.getFixedCharges(),
                    bill.getTotalBillingAmount(),
                    bill.getDueDate(),
                    bill.getBillStatus()
            };
            tableModel.addRow(row);
        }
    }

    // Search for a bill in the table based on substring match in any field
    private void searchBill() {
        String searchText = searchField.getText().trim().toLowerCase();  // Get search text and convert to lowercase
        
     
        tableModel.setRowCount(0);  

       
        for (BillingInfo bill : billList) {
            
            boolean matchFound = bill.getCustomerId().toLowerCase().contains(searchText) ||
                                 bill.getReadingEntryDate().toLowerCase().contains(searchText) ||
                                 String.valueOf(bill.getRegularUnitsConsumed()).contains(searchText) ||
                                 String.valueOf(bill.getPeakUnitsConsumed()).contains(searchText) ||
                                 String.valueOf(bill.getTotalElectricityCost()).contains(searchText) ||
                                 String.valueOf(bill.getSalesTax()).contains(searchText) ||
                                 String.valueOf(bill.getFixedCharges()).contains(searchText) ||
                                 String.valueOf(bill.getTotalBillingAmount()).contains(searchText) ||
                                 bill.getDueDate().toLowerCase().contains(searchText) ||
                                 bill.getBillStatus().toLowerCase().contains(searchText);

            // If any field matches the search text, add the row to the table
            if (matchFound) {
                Object[] row = {
                        bill.getCustomerId(),
                        bill.getReadingEntryDate(),
                        bill.getRegularUnitsConsumed(),
                        bill.getPeakUnitsConsumed(),
                        bill.getTotalElectricityCost(),
                        bill.getSalesTax(),
                        bill.getFixedCharges(),
                        bill.getTotalBillingAmount(),
                        bill.getDueDate(),
                        bill.getBillStatus()
                };
                tableModel.addRow(row);
            }
        }
    }

  
}
