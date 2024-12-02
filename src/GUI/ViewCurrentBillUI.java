package GUI;

import lesco.bill.system.a1.pkg22l.pkg7906.BillingInfo;
import lesco.bill.system.a1.pkg22l.pkg7906.ClientSocket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ViewCurrentBillUI extends JFrame {
    private JTable billTable;
    private DefaultTableModel tableModel;
    private ArrayList<BillingInfo> billList = new ArrayList<>();
    private JTextField searchField;

    public ViewCurrentBillUI() {
        // Ask for the customer ID using a dialog
        String customerId = JOptionPane.showInputDialog(this, "Enter Your Customer ID:", "Customer ID Input", JOptionPane.PLAIN_MESSAGE);

        if (customerId == null || customerId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Customer ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setTitle("LESCO BILLING SYSTEM - View Current Bill");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

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
                return false;
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

        // Fetch bills from the server
        fetchBillsFromServer(customerId);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.YELLOW);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> dispose());

        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.BLACK);
        searchPanel.setPreferredSize(new Dimension(800, 50));

        JLabel searchLabel = new JLabel("Search: ");
        searchLabel.setForeground(Color.YELLOW);
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        searchPanel.add(searchLabel);

        searchField = new JTextField(15);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(Color.YELLOW);
        searchButton.setForeground(Color.BLACK);
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchButton.addActionListener(e -> searchBill());
        searchPanel.add(searchButton);

        mainPanel.add(searchPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void fetchBillsFromServer(String customerId) {
        try {
            String request = "Customer,VIEW_BILLS," + customerId;
            ClientSocket client = ClientSocket.getInstance();
            String response = client.sendRequest(request);

            if (response.startsWith("Error")) {
                JOptionPane.showMessageDialog(this, response, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Parse response and populate the bill list
            String[] billData = response.split(";");
            for (String billString : billData) {
                billList.add(BillingInfo.fromString(billString));
            }

            // Populate the table
            populateTable(billList);
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error connecting to the server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void populateTable(ArrayList<BillingInfo> bills) {
        tableModel.setRowCount(0);

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

    private void searchBill() {
        String searchText = searchField.getText().trim().toLowerCase();
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
