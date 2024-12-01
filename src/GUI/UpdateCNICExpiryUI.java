package GUI;

import Controller.CustomerController;
import javax.swing.*;
import java.awt.*;

public class UpdateCNICExpiryUI extends JFrame {
    private JTextField cnicField, customerIdField, dayField, monthField, yearField;
    private JButton updateButton, cancelButton;
    private CustomerController controller;

    public UpdateCNICExpiryUI() {
        setTitle("Update CNIC Expiry Date");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);

        // Initialize controller
        controller = new CustomerController();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Customer ID
        addLabelAndField(mainPanel, gbc, "Customer ID:", customerIdField = new JTextField(20));

        // CNIC
        addLabelAndField(mainPanel, gbc, "CNIC:", cnicField = new JTextField(20));

        // Expiry Date
        addLabelAndField(mainPanel, gbc, "Day:", dayField = new JTextField(2));
        addLabelAndField(mainPanel, gbc, "Month:", monthField = new JTextField(2));
        addLabelAndField(mainPanel, gbc, "Year:", yearField = new JTextField(4));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.BLACK);
        updateButton = new JButton("Update Expiry");
        cancelButton = new JButton("Cancel");
        styleButton(updateButton);
        styleButton(cancelButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        gbc.gridy++;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);

        // Add action listeners
        updateButton.addActionListener(e -> updateCNICExpiryDate());
        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.YELLOW);
        panel.add(label, gbc);

        gbc.gridx = 1;
        styleTextField(field);
        panel.add(field, gbc);
    }

    private void styleTextField(JTextField field) {
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void styleButton(JButton button) {
        button.setBackground(Color.YELLOW);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }

    // This method connects with the controller
    private void updateCNICExpiryDate() {
        String customerId = customerIdField.getText().trim();
        String cnic = cnicField.getText().trim();

        // Validate inputs
        if (customerId.isEmpty() || cnic.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get expiry date
        int day, month, year;
        try {
            day = Integer.parseInt(dayField.getText().trim());
            month = Integer.parseInt(monthField.getText().trim());
            year = Integer.parseInt(yearField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for the expiry date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update expiry date through controller
        boolean success = controller.UPDATE_CNIC_EXPIRY_DATE(cnic, customerId, day, month, year);

        if (success) {
            JOptionPane.showMessageDialog(this, "CNIC Expiry Date updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update CNIC Expiry Date.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

  
}
