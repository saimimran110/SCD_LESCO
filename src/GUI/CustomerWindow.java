package GUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import lesco.bill.system.a1.pkg22l.pkg7906.ClientSocket;
import lesco.bill.system.a1.pkg22l.pkg7906.Customer;

public class CustomerWindow extends JFrame {
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton exitButton;
    private JTextField searchField;

    public static final String CUSTOMER_FILE = "CustomerInfo.txt";
    private static ArrayList<Customer> customers = new ArrayList<>();

    public CustomerWindow() {
        setTitle("Customer Information");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);  // Black background for the whole window

        // Load customers from file
        loadCustomersFromFile();

        // Create table model
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Customer ID");
        tableModel.addColumn("CNIC");
        tableModel.addColumn("Name");
        tableModel.addColumn("Address");
        tableModel.addColumn("Phone");
        tableModel.addColumn("Customer Type");
        tableModel.addColumn("Meter Type");
        tableModel.addColumn("Connection Date");
        tableModel.addColumn("Regular Units");
        tableModel.addColumn("Peak Units");

        // Populate table with customer data
        for (Customer customer : customers) {
            tableModel.addRow(new Object[] {
                    customer.getCustomerId(),
                    customer.getCnic(),
                    customer.getName(),
                    customer.getAddress(),
                    customer.getPhoneNum(),
                    customer.getCustomerType(),
                    customer.getMeterType(),
                    customer.getConnectionDate(),
                    customer.getRegularUnits(),
                    customer.getPeakUnits()
            });
        }

        // Create JTable
        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.setBackground(Color.WHITE);  // White background for the table
        customerTable.setForeground(Color.BLACK);  // Black text for readability
        customerTable.setFont(new Font("Arial", Font.PLAIN, 14));
        customerTable.setRowHeight(30);
        customerTable.setSelectionBackground(Color.YELLOW);  // Yellow selection color
        customerTable.setSelectionForeground(Color.BLACK);

        scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);  // Black background for the button panel
        updateButton = new JButton("Update");
        updateButton.setBackground(Color.YELLOW);  // Yellow background for the button
        updateButton.setForeground(Color.BLACK);  // Black text on the button
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton = new JButton("Delete");
        deleteButton.setBackground(Color.RED);  // Red background for delete button
        deleteButton.setForeground(Color.WHITE);  // White text for delete button
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton = new JButton("Exit");  // Exit button
        exitButton.setBackground(Color.GRAY);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add search field without a button
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.BLACK);
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        add(searchPanel, BorderLayout.NORTH);

        // Set button actions
        // Update Button Action Listener
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = customerTable.getSelectedRow();
                if (selectedRow != -1) {
                    String customerId = (String) customerTable.getValueAt(selectedRow, 0); // Get Customer ID
                    String currentName = (String) customerTable.getValueAt(selectedRow, 2); // Current Name
                    String currentAddress = (String) customerTable.getValueAt(selectedRow, 3); // Current Address

                    String newName = JOptionPane.showInputDialog("Enter new name:", currentName);
                    String newAddress = JOptionPane.showInputDialog("Enter new address:", currentAddress);

                    if (newName != null && newAddress != null) {
                        try {
                            // Send update request to the server
                            ClientSocket client = ClientSocket.getInstance();
                            String request = String.format("Customer,UPDATE_CUSTOMER,%s,%s,%s", customerId, newName, newAddress);
                            String response = client.sendRequest(request);

                            if (response.equalsIgnoreCase("Customer updated successfully.")) {
                                JOptionPane.showMessageDialog(null, response, "Success", JOptionPane.INFORMATION_MESSAGE);
                                loadCustomersFromServer(); // Reload the table data
                            } else {
                                JOptionPane.showMessageDialog(null, response, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error connecting to the server.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a customer to update.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

// Delete Button Action Listener
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = customerTable.getSelectedRow();
                if (selectedRow != -1) {
                    String customerId = (String) customerTable.getValueAt(selectedRow, 0); // Get Customer ID

                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this customer?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            // Send delete request to the server
                            ClientSocket client = ClientSocket.getInstance();
                            String request = "Customer,DELETE_CUSTOMER," + customerId;
                            String response = client.sendRequest(request);

                            if (response.equalsIgnoreCase("Customer deleted successfully.")) {
                                JOptionPane.showMessageDialog(null, response, "Success", JOptionPane.INFORMATION_MESSAGE);
                                loadCustomersFromServer(); // Reload the table data
                            } else {
                                JOptionPane.showMessageDialog(null, response, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error connecting to the server.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a customer to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

// Exit Button Action Listener
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the window
            }
        });

        // Search functionality for customers
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchCustomer(searchField.getText().toLowerCase());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchCustomer(searchField.getText().toLowerCase());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchCustomer(searchField.getText().toLowerCase());
            }
        });
    }

    // Load customers from CustomerInfo.txt file
    private void loadCustomersFromFile() {
        customers.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 9) {
                    int regularUnits = Integer.parseInt(parts[8]);  // Regular units
                    int peakUnits = (parts.length > 9 && !parts[9].isEmpty()) ? Integer.parseInt(parts[9]) : 0;  // Peak units if available

                    Customer customer = new Customer();
                    customer.setID(parts[0]);  // Set the ID from the file
                    customer.setCnic(parts[1]);
                    customer.setName(parts[2]);
                    customer.setAddress(parts[3]);
                    customer.setPhoneNum(parts[4]);
                    customer.setCustomerType(parts[5]);
                    customer.setMeterType(parts[6]);
                    customer.setConnectionDate(parts[7]);
                    customer.setRegularUnits(regularUnits);
                    customer.setPeakUnits(peakUnits);

                    customers.add(customer);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Search functionality for customers by any field
    private void searchCustomer(String searchText) {
        DefaultTableModel searchModel = new DefaultTableModel();
        searchModel.addColumn("Customer ID");
        searchModel.addColumn("CNIC");
        searchModel.addColumn("Name");
        searchModel.addColumn("Address");
        searchModel.addColumn("Phone");
        searchModel.addColumn("Customer Type");
        searchModel.addColumn("Meter Type");
        searchModel.addColumn("Connection Date");
        searchModel.addColumn("Regular Units");
        searchModel.addColumn("Peak Units");

        // If the search text is empty, display all customers
        if (searchText.isEmpty()) {
            for (Customer customer : customers) {
                searchModel.addRow(new Object[] {
                        customer.getCustomerId(),
                        customer.getCnic(),
                        customer.getName(),
                        customer.getAddress(),
                        customer.getPhoneNum(),
                        customer.getCustomerType(),
                        customer.getMeterType(),
                        customer.getConnectionDate(),
                        customer.getRegularUnits(),
                        customer.getPeakUnits()
                });
            }
            customerTable.setModel(searchModel);
            return;
        }

        // Search in all fields
        for (Customer customer : customers) {
            boolean matchFound = customer.getCustomerId().toLowerCase().contains(searchText) ||
                    customer.getCnic().toLowerCase().contains(searchText) ||
                    customer.getName().toLowerCase().contains(searchText) ||
                    customer.getAddress().toLowerCase().contains(searchText) ||
                    customer.getPhoneNum().toLowerCase().contains(searchText) ||
                    customer.getCustomerType().toLowerCase().contains(searchText) ||
                    customer.getMeterType().toLowerCase().contains(searchText) ||
                    customer.getConnectionDate().toLowerCase().contains(searchText) ||
                    String.valueOf(customer.getRegularUnits()).contains(searchText) ||
                    String.valueOf(customer.getPeakUnits()).contains(searchText);

            if (matchFound) {
                searchModel.addRow(new Object[] {
                        customer.getCustomerId(),
                        customer.getCnic(),
                        customer.getName(),
                        customer.getAddress(),
                        customer.getPhoneNum(),
                        customer.getCustomerType(),
                        customer.getMeterType(),
                        customer.getConnectionDate(),
                        customer.getRegularUnits(),
                        customer.getPeakUnits()
                });
            }
        }

        // Update table with search results
        customerTable.setModel(searchModel);
    }
    private void loadCustomersFromServer() {
        try {
            ClientSocket client = ClientSocket.getInstance();
            String response = client.sendRequest("Customer,FETCH_CUSTOMERS");
            tableModel.setRowCount(0);

            if (!response.startsWith("Error")) {
                String[] rows = response.split(";");
                for (String row : rows) {
                    tableModel.addRow(row.split(","));
                }
            } else {
                JOptionPane.showMessageDialog(this, response, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Update customer method
    private void updateCustomer(int selectedRow) {
        // Get Customer ID from the selected row
        String customerId = (String) customerTable.getValueAt(selectedRow, 0);

        // Find the customer object in the ArrayList
        Customer customer = customers.stream().filter(c -> c.getCustomerId().equals(customerId)).findFirst().orElse(null);

        if (customer != null) {
            // Prompt for each field
            String newCnic = JOptionPane.showInputDialog(this, "Enter new CNIC:", customer.getCnic());
            String newName = JOptionPane.showInputDialog(this, "Enter new name:", customer.getName());
            String newAddress = JOptionPane.showInputDialog(this, "Enter new address:", customer.getAddress());
            String newPhoneNum = JOptionPane.showInputDialog(this, "Enter new phone number:", customer.getPhoneNum());
            String newCustomerType = JOptionPane.showInputDialog(this, "Enter new customer type (e.g., Domestic, Commercial):", customer.getCustomerType());
            String newMeterType = JOptionPane.showInputDialog(this, "Enter new meter type (e.g., Single Phase, Three Phase):", customer.getMeterType());
            String newConnectionDate = JOptionPane.showInputDialog(this, "Enter new connection date (DD-MM-YYYY):", customer.getConnectionDate());
            int newRegularUnits = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter new regular units:", customer.getRegularUnits()));
            int newPeakUnits = 0;

            // Only prompt for peak units if the meter type is Three Phase
            if (newMeterType != null && newMeterType.equalsIgnoreCase("Three Phase")) {
                newPeakUnits = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter new peak units:", customer.getPeakUnits()));
            }

            // Update customer object with new values
            customer.setCnic(newCnic);
            customer.setName(newName);
            customer.setAddress(newAddress);
            customer.setPhoneNum(newPhoneNum);
            customer.setCustomerType(newCustomerType);
            customer.setMeterType(newMeterType);
            customer.setConnectionDate(newConnectionDate);
            customer.setRegularUnits(newRegularUnits);
            customer.setPeakUnits(newPeakUnits);

            // Update the table
            tableModel.setValueAt(newCnic, selectedRow, 1);
            tableModel.setValueAt(newName, selectedRow, 2);
            tableModel.setValueAt(newAddress, selectedRow, 3);
            tableModel.setValueAt(newPhoneNum, selectedRow, 4);
            tableModel.setValueAt(newCustomerType, selectedRow, 5);
            tableModel.setValueAt(newMeterType, selectedRow, 6);
            tableModel.setValueAt(newConnectionDate, selectedRow, 7);
            tableModel.setValueAt(newRegularUnits, selectedRow, 8);
            tableModel.setValueAt(newPeakUnits, selectedRow, 9);

            // Save changes to the file
            saveCustomersToFile();

            JOptionPane.showMessageDialog(this, "Customer updated successfully!", "Update", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Customer not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Delete customer method
    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow != -1) {
            String customerId = (String) customerTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    ClientSocket client = ClientSocket.getInstance();
                    String response = client.sendRequest("Customer,DELETE_CUSTOMER," + customerId);

                    if (response.equalsIgnoreCase("Customer deleted successfully.")) {
                        JOptionPane.showMessageDialog(this, response, "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadCustomersFromServer();
                    } else {
                        JOptionPane.showMessageDialog(this, response, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error connecting to the server.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Save updated customers to CustomerInfo.txt
    private void saveCustomersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_FILE))) {
            for (Customer customer : customers) {
                writer.write(customer.getCustomerId() + "," +
                        customer.getCnic() + "," +
                        customer.getName() + "," +
                        customer.getAddress() + "," +
                        customer.getPhoneNum() + "," +
                        customer.getCustomerType() + "," +
                        customer.getMeterType() + "," +
                        customer.getConnectionDate() + "," +
                        customer.getRegularUnits() + "," +
                        customer.getPeakUnits());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}