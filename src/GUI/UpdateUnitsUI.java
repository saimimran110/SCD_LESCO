package GUI;

import Controller.CustomerController;
import lesco.bill.system.a1.pkg22l.pkg7906.ClientSocket;
import lesco.bill.system.a1.pkg22l.pkg7906.Customer;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class UpdateUnitsUI extends JFrame {
    private JTextField customerIdField, regularUnitsField, peakUnitsField;
    private JButton proceedButton, updateButton, cancelButton;
    private JLabel statusLabel;
    private String customerMeterType;
    private CustomerController cc;

    // Color scheme
    private final Color YELLOW = new Color(255, 255, 0);
    private final Color BLACK = Color.BLACK;
    private final Color WHITE = Color.WHITE;

    public UpdateUnitsUI() {
        cc = new CustomerController();
        setTitle("Update Units Consumed");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BLACK);

        // Top panel for customer ID and proceed button
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Main panel for unit inputs
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        // Bottom panel for buttons
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(BLACK);

        JLabel customerIdLabel = new JLabel("Customer ID:");
        styleLabel(customerIdLabel);
        customerIdField = new JTextField(15);
        styleTextField(customerIdField);
        proceedButton = new JButton("Proceed");
        styleButton(proceedButton);

        panel.add(customerIdLabel);
        panel.add(customerIdField);
        panel.add(proceedButton);

        proceedButton.addActionListener(e -> fetchCustomerInfo());

        return panel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        regularUnitsField = new JTextField(10);
        peakUnitsField = new JTextField(10);
        styleTextField(regularUnitsField);
        styleTextField(peakUnitsField);
        regularUnitsField.setEnabled(false);
        peakUnitsField.setEnabled(false);

        JLabel regularUnitsLabel = new JLabel("Regular Units Consumed:");
        JLabel peakUnitsLabel = new JLabel("Peak Hours Units Consumed:");
        styleLabel(regularUnitsLabel);
        styleLabel(peakUnitsLabel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(regularUnitsLabel, gbc);

        gbc.gridx = 1;
        panel.add(regularUnitsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(peakUnitsLabel, gbc);

        gbc.gridx = 1;
        panel.add(peakUnitsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        statusLabel = new JLabel("Enter Customer ID and click Proceed");
        styleLabel(statusLabel);
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(statusLabel, gbc);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(BLACK);

        updateButton = new JButton("Update");
        cancelButton = new JButton("Cancel");
        styleButton(updateButton);
        styleButton(cancelButton);

        updateButton.addActionListener(e -> updateUnits());
        cancelButton.addActionListener(e -> dispose());

        panel.add(updateButton);
        panel.add(cancelButton);

        return panel;
    }

    private void styleTextField(JTextField field) {
        field.setBackground(WHITE);
        field.setForeground(BLACK);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(YELLOW, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    private void styleButton(JButton button) {
        button.setBackground(YELLOW);
        button.setForeground(BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    private void styleLabel(JLabel label) {
        label.setForeground(YELLOW);
        label.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private void fetchCustomerInfo() {
        String customerId = customerIdField.getText().trim();

        if (customerId.isEmpty()) {
            showMessage("Please enter a Customer ID.", true);
            return;
        }

        try {
            String request = String.format("Customer,FETCH_INFO,%s", customerId);
            ClientSocket client = ClientSocket.getInstance();
            String response = client.sendRequest(request);

            if (response.startsWith("Error")) {
                showMessage(response, true);
            } else {
                customerMeterType = response;
                regularUnitsField.setEnabled(true);
                peakUnitsField.setEnabled("Three Phase".equalsIgnoreCase(customerMeterType));
                showMessage("Customer found. Meter type: " + customerMeterType, false);
            }
        } catch (Exception e) {
            showMessage("Error connecting to the server. Please try again.", true);
            e.printStackTrace();
        }
    }

    private void updateUnits() {
        if (customerMeterType == null) {
            showMessage("Please fetch customer info first.", true);
            return;
        }

        String customerId = customerIdField.getText().trim();
        String regularUnits = regularUnitsField.getText().trim();
        String peakUnits = peakUnitsField.getText().trim();

        if (customerId.isEmpty() || regularUnits.isEmpty() || (peakUnitsField.isEnabled() && peakUnits.isEmpty())) {
            showMessage("Please fill in all required fields.", true);
            return;
        }

        try {
            int regularUnitsConsumed = Integer.parseInt(regularUnits);
            int peakUnitsConsumed = peakUnitsField.isEnabled() ? Integer.parseInt(peakUnits) : 0;

            String request = String.format("Customer,UPDATE_UNITS,%s,%d,%d", customerId, regularUnitsConsumed, peakUnitsConsumed);
            ClientSocket client = ClientSocket.getInstance();
            String response = client.sendRequest(request);

            if (response.equalsIgnoreCase("Success")) {
                showMessage("Units consumed updated successfully.", false);
            } else {
                showMessage(response, true);
            }
        } catch (NumberFormatException e) {
            showMessage("Please enter valid numbers for units.", true);
        } catch (Exception e) {
            showMessage("Error connecting to the server. Please try again.", true);
            e.printStackTrace();
        }
    }

    private void showMessage(String message, boolean isError)
    {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? Color.RED : Color.GREEN);
    }
  
}