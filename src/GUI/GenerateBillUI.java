package GUI;

import Controller.EmployeeController;
import lesco.bill.system.a1.pkg22l.pkg7906.ClientSocket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GenerateBillUI extends JFrame {
    EmployeeController ec;

    public GenerateBillUI() {
        ec = new EmployeeController();
        setTitle("Generate Bill");
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

        // Generate Bill Button
        JButton generateBillButton = new JButton("Generate Bill");
        generateBillButton.setFont(new Font("Arial", Font.BOLD, 16));  // Larger font for the button
        generateBillButton.setBackground(Color.BLACK);
        generateBillButton.setForeground(Color.YELLOW);
        generateBillButton.setPreferredSize(new Dimension(200, 40));  // Larger button size
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(generateBillButton, gbc);

        // Action listener for the button
        generateBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerId = customerIdField.getText().trim();

                if (!customerId.isEmpty()) {
                    // Run the bill generation in the background using SwingWorker
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            // Show the loading screen
                            loading loadingScreen = new loading("Generating Bill...");

                            try {
                                // Send the request to the server
                                String request = String.format("Employee,GENERATE_BILL,%s", customerId);
                                ClientSocket client = ClientSocket.getInstance();
                                String response = client.sendRequest(request);

                                // Simulate loading for UI purposes (adjust delay if necessary)
                                Thread.sleep(1500);

                                // Process the server response
                                SwingUtilities.invokeLater(() -> {
                                    if ("Bill generated successfully.".equalsIgnoreCase(response)) {
                                        JOptionPane.showMessageDialog(
                                                GenerateBillUI.this,
                                                "Bill generated successfully for ID: " + customerId,
                                                "Success",
                                                JOptionPane.INFORMATION_MESSAGE
                                        );
                                    } else {
                                        JOptionPane.showMessageDialog(
                                                GenerateBillUI.this,
                                                response,
                                                "Error",
                                                JOptionPane.ERROR_MESSAGE
                                        );
                                    }
                                });
                            } finally {
                                // Ensure the loading screen is closed
                                loadingScreen.dispose();
                            }
                            return null;
                        }
                    };

                    worker.execute();  // Start the background task
                } else {
                    JOptionPane.showMessageDialog(
                            GenerateBillUI.this,
                            "Please enter a valid Customer ID.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });


        // Add main panel to the frame
        add(mainPanel);

        setVisible(true);
    }
}
