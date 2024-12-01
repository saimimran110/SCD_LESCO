package GUI;

import Controller.EmployeeController;
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
                String customerId = customerIdField.getText();

                if (!customerId.isEmpty()) {
                    // Run the bill generation in the background using SwingWorker
                    SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                        @Override
                        protected Boolean doInBackground() throws Exception {
                            // Show the loading screen
                            loading loadingScreen = new loading("Generating Bill...");

                            try {
                                // Simulate loading (you can adjust this delay or remove it)
                                Thread.sleep(3000); // 3-second simulated delay

                                // Call the method to generate the bill
                                return ec.GENERATE_BILL(customerId);
                            } finally {
                                // Ensure the loading screen is closed
                                loadingScreen.dispose();
                            }
                        }

                        @Override
                        protected void done() {
                            try {
                                // Get the result from doInBackground
                                boolean isBillGenerated = get();

                                if (isBillGenerated) {
                                    JOptionPane.showMessageDialog(GenerateBillUI.this, "Bill generated successfully for ID: " + customerId);
                                } else {
                                    JOptionPane.showMessageDialog(GenerateBillUI.this, "Please enter a valid Customer ID");
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(GenerateBillUI.this, "An error occurred while generating the bill.");
                            }
                        }
                    };

                    worker.execute();  // Start the background task
                } else {
                    JOptionPane.showMessageDialog(GenerateBillUI.this, "Please enter a valid Customer ID.");
                }
            }
        });

        // Add main panel to the frame
        add(mainPanel);

        setVisible(true);
    }
}
