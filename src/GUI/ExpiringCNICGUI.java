package GUI;

import Controller.EmployeeController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;

import lesco.bill.system.a1.pkg22l.pkg7906.ClientSocket;
import lesco.bill.system.a1.pkg22l.pkg7906.Customer;

public class ExpiringCNICGUI {

    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> rowSorter;

    public ExpiringCNICGUI() {
        // Initialize the JFrame
        frame = new JFrame("Expiring CNICs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.BLACK);

        // Create a panel for the title
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);
        JLabel titleLabel = new JLabel("Expiring CNICs");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.YELLOW);
        titlePanel.add(titleLabel);

        // Create the search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.BLACK);
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        searchLabel.setForeground(Color.YELLOW);
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        // Create the table model with column names
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        tableModel.addColumn("Customer ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("CNIC");
        tableModel.addColumn("Address");
        tableModel.addColumn("Phone Number");

        // Create the table with the model
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setSelectionBackground(Color.YELLOW);
        table.setSelectionForeground(Color.BLACK);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);

        // Enable sorting and filtering
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

      
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                searchTable();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                searchTable();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                searchTable();
            }

            private void searchTable() {
                String searchText = searchField.getText().trim();
                if (searchText.length() == 0) {
                    rowSorter.setRowFilter(null); // No filter if search is empty
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText)); // Case-insensitive search
                }
            }
        });

        // Customize the table header
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.BLACK);
        header.setForeground(Color.YELLOW);
        header.setFont(new Font("Arial", Font.BOLD, 16));

        // Add the table to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));

        // Create a panel for the close button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        JButton closeButton = new JButton("Close");
        closeButton.setBackground(Color.YELLOW);
        closeButton.setForeground(Color.BLACK);
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.addActionListener(e -> frame.dispose());
        buttonPanel.add(closeButton);

        // Add components to the frame
        frame.setLayout(new BorderLayout());
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(searchPanel, BorderLayout.BEFORE_FIRST_LINE); // Add search panel above table
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Load the data into the table when the window is created
        loadExpiringCNICs();

        // Make the frame visible
        frame.setVisible(true);
    }


    public void loadExpiringCNICs() {
        try {

            String request = "Employee,VIEW_CNIC_EXPIRES";

            // Send the request to the server and receive the response
            ClientSocket client = ClientSocket.getInstance(); // Singleton for client communication
            String response = client.sendRequest(request);

            if (response.startsWith("Error")) {
                JOptionPane.showMessageDialog(frame, response, "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Parse the response and populate the table
            String[] customers = response.split(";");
            for (String customerData : customers) {
                if (!customerData.isEmpty()) {
                    String[] customerDetails = customerData.split(",");
                    tableModel.addRow(new Object[]{
                            customerDetails[0], // Customer ID
                            customerDetails[1], // Name
                            customerDetails[2], // CNIC
                            customerDetails[3], // Address
                            customerDetails[4]  // Phone Number
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error loading expiring CNICs: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}
