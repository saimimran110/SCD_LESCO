package GUI;

import Controller.EmployeeController;
import lesco.bill.system.a1.pkg22l.pkg7906.ClientSocket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class UpdateBillStatusUI extends JFrame {
    EmployeeController ec;

    public UpdateBillStatusUI() {
        ec = new EmployeeController();
        setTitle("Update Bill Status");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel setup
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Customer ID label and text field
        JLabel customerIdLabel = new JLabel("Customer ID:");
        customerIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));  // Make label text larger
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(customerIdLabel, gbc);

        JTextField customerIdField = new JTextField(15);
        customerIdField.setFont(new Font("Arial", Font.PLAIN, 16));  // Larger font for the text field
        customerIdField.setPreferredSize(new Dimension(200, 30));  // Larger text field
        gbc.gridx = 1;
        mainPanel.add(customerIdField, gbc);

        // Update Button
        JButton updateButton = new JButton("Update Bill Status");
        updateButton.setFont(new Font("Arial", Font.BOLD, 16));  // Larger font for the button
        updateButton.setBackground(Color.BLACK);
        updateButton.setForeground(Color.YELLOW);
        updateButton.setPreferredSize(new Dimension(200, 40));  // Larger button size
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(updateButton, gbc);

        // Action listener for the button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerId = customerIdField.getText().trim();

                if (customerId.isEmpty()) {
                    JOptionPane.showMessageDialog(UpdateBillStatusUI.this, "Please enter a valid Customer ID.");
                    return;
                }

                try {
                    // Send the update request to the server
                    String request = "Employee,UPDATE_BILL_STATUS," + customerId;
                    ClientSocket client = ClientSocket.getInstance();
                    String response = client.sendRequest(request);

                    // Process the server response
                    if (response.startsWith("Success")) {
                        JOptionPane.showMessageDialog(UpdateBillStatusUI.this, response);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(UpdateBillStatusUI.this, response, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(UpdateBillStatusUI.this, "Error connecting to the server. Please try again.", "Connection Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });


        // Add main panel to the frame
        add(mainPanel);

        setVisible(true);
    }
}
