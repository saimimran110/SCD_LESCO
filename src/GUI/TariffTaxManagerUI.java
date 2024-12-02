package GUI;

import lesco.bill.system.a1.pkg22l.pkg7906.ClientSocket;
import lesco.bill.system.a1.pkg22l.pkg7906.TariffTaxManager;
import javax.swing.*;
import java.awt.*;

public class TariffTaxManagerUI extends JFrame {
    private JComboBox<String> meterTypeCombo, customerTypeCombo;
    private JTextField regularUnitPriceField, peakHourUnitPriceField, taxPercentageField, fixedChargesField;
    private JButton updateButton, cancelButton;
    private JTextArea currentValuesArea;

    public TariffTaxManagerUI() {
        setTitle("Update Tariff Taxes");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Meter Type
        addLabelAndComponent(mainPanel, gbc, "Meter Type:", meterTypeCombo = new JComboBox<>(new String[]{"Single Phase", "Three Phase"}));

        // Customer Type
        addLabelAndComponent(mainPanel, gbc, "Customer Type:", customerTypeCombo = new JComboBox<>(new String[]{"Domestic", "Commercial"}));

        // Regular Unit Price
        addLabelAndField(mainPanel, gbc, "Regular Unit Price:", regularUnitPriceField = new JTextField(10));

        // Peak Hour Unit Price
        addLabelAndField(mainPanel, gbc, "Peak Hour Unit Price:", peakHourUnitPriceField = new JTextField(10));

        // Tax Percentage
        addLabelAndField(mainPanel, gbc, "Tax Percentage:", taxPercentageField = new JTextField(10));

        // Fixed Charges
        addLabelAndField(mainPanel, gbc, "Fixed Charges:", fixedChargesField = new JTextField(10));

        // Current Values
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JLabel currentValuesLabel = new JLabel("Current Values:");
        currentValuesLabel.setForeground(Color.YELLOW);
        mainPanel.add(currentValuesLabel, gbc);

        gbc.gridy++;
        currentValuesArea = new JTextArea(5, 20);
        currentValuesArea.setEditable(false);
        currentValuesArea.setBackground(Color.WHITE);
        currentValuesArea.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(currentValuesArea);
        mainPanel.add(scrollPane, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.BLACK);
        updateButton = new JButton("Update");
        cancelButton = new JButton("Cancel");
        styleButton(updateButton);
        styleButton(cancelButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        gbc.gridy++;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);

        // Add action listeners
        meterTypeCombo.addActionListener(e -> updateCurrentValues());
        customerTypeCombo.addActionListener(e -> updateCurrentValues());
        updateButton.addActionListener(e -> updateTariffTax());
        cancelButton.addActionListener(e -> dispose());

        // Initial update of current values
        updateCurrentValues();

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

    private void addLabelAndComponent(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.YELLOW);
        panel.add(label, gbc);

        gbc.gridx = 1;
        styleComponent(component);
        panel.add(component, gbc);
    }

    private void styleTextField(JTextField field) {
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void styleComponent(JComponent component) {
        component.setBackground(Color.WHITE);
        component.setForeground(Color.BLACK);
        component.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void styleButton(JButton button) {
        button.setBackground(Color.YELLOW);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }
    private void updateCurrentValues() {
        String meterType = (String) meterTypeCombo.getSelectedItem();
        String customerType = (String) customerTypeCombo.getSelectedItem();

        String request = String.format("TariffTaxManager,GET_CURRENT_VALUES,%s,%s", meterType, customerType);

        try {
            ClientSocket client = ClientSocket.getInstance();
            String response = client.sendRequest(request);

            if (response.startsWith("Error")) {
                JOptionPane.showMessageDialog(this, response, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                currentValuesArea.setText(response);
                String[] values = response.split(",");
                regularUnitPriceField.setText(values[1]);
                peakHourUnitPriceField.setText(values[2]);
                taxPercentageField.setText(values[3]);
                fixedChargesField.setText(values[4]);
                peakHourUnitPriceField.setEnabled(!meterType.equals("Single Phase"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error connecting to the server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void updateTariffTax() {
        String meterType = (String) meterTypeCombo.getSelectedItem();
        String customerType = (String) customerTypeCombo.getSelectedItem();
        String regularUnitPrice = regularUnitPriceField.getText();
        String peakHourUnitPrice = peakHourUnitPriceField.getText();
        String taxPercentage = taxPercentageField.getText();
        String fixedCharges = fixedChargesField.getText();

        if (regularUnitPrice.isEmpty() || taxPercentage.isEmpty() || fixedCharges.isEmpty() ||
                (!meterType.equals("Single Phase") && peakHourUnitPrice.isEmpty())) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String request = String.format("TariffTaxManager,UPDATE_TARIFF_TAX,%s,%s,%s,%s,%s,%s",
                meterType, customerType, regularUnitPrice, peakHourUnitPrice, taxPercentage, fixedCharges);

        try {
            ClientSocket client = ClientSocket.getInstance();
            String response = client.sendRequest(request);

            JOptionPane.showMessageDialog(this, response, "Update Status",
                    response.startsWith("Success") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);

            if (response.startsWith("Success")) {
                updateCurrentValues(); // Refresh after successful update
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error connecting to the server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}