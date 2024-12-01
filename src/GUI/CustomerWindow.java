package GUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = customerTable.getSelectedRow();
                if (selectedRow != -1) {
                    updateCustomer(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a customer to update.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = customerTable.getSelectedRow();
                if (selectedRow != -1) {
                    deleteCustomer(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a customer to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        exitButton.addActionListener(e -> dispose()); 

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

                    Customer customer = new Customer(
                        parts[1], 
                        parts[2],  
                        parts[3],  
                        parts[4],  
                        parts[5],  
                        parts[6],  
                        parts[7],  
                        regularUnits,  
                        peakUnits     
                    );

                    customers.add(customer);  // Add the customer to the list
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

    // Update customer method
    private void updateCustomer(int selectedRow) {
        String customerId = (String) customerTable.getValueAt(selectedRow, 0);  // Get Customer ID
        Customer customer = customers.stream().filter(c -> c.getCustomerId().equals(customerId)).findFirst().orElse(null);

        if (customer != null) {
            String newName = JOptionPane.showInputDialog("Enter new name:", customer.getName());
            String newAddress = JOptionPane.showInputDialog("Enter new address:", customer.getAddress());
            String newPhone = JOptionPane.showInputDialog("Enter new phone number:", customer.getPhoneNum());
            String newCustomerType = JOptionPane.showInputDialog("Enter new customer type:", customer.getCustomerType());
            String newMeterType = JOptionPane.showInputDialog("Enter new meter type:", customer.getMeterType());

            // Update customer object
            customer.setName(newName);
            customer.setAddress(newAddress);
            customer.setPhoneNum(newPhone);
            customer.setCustomerType(newCustomerType);
            customer.setMeterType(newMeterType);

            // Update the table
            tableModel.setValueAt(newName, selectedRow, 2);
            tableModel.setValueAt(newAddress, selectedRow, 3);
            tableModel.setValueAt(newPhone, selectedRow, 4);
            tableModel.setValueAt(newCustomerType, selectedRow, 5);
            tableModel.setValueAt(newMeterType, selectedRow, 6);

            // Save updated customers to file
            saveCustomersToFile();
        }
    }

    // Delete customer method
   private void deleteCustomer(int selectedRow) {
    // Ensure the selected row is valid
    if (selectedRow >= 0 && selectedRow < customerTable.getRowCount()) {
        String customerId = (String) customerTable.getValueAt(selectedRow, 0);  // Get Customer ID from table
        customers.removeIf(c -> c.getCustomerId().equals(customerId));  // Remove from ArrayList

        // Remove the selected row from the table model
        tableModel.removeRow(selectedRow);

        // Write updated customers back to the file
        saveCustomersToFile();
    } else {
        System.out.println("Invalid row selection");
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
