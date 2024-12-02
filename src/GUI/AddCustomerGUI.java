package GUI;

import lesco.bill.system.a1.pkg22l.pkg7906.ClientSocket;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCustomerGUI extends JFrame {
    private JTextField cnicField, nameField, addressField, phoneNumField;
    private JRadioButton commercialRadio, domesticRadio, singlePhaseRadio, threePhaseRadio;
    private JButton submitButton, cancelButton;

    public AddCustomerGUI() {
        setTitle("Add New Customer");
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

        // CNIC
        addLabelAndField(mainPanel, gbc, "CNIC:", cnicField = new JTextField(20));

        // Name
        addLabelAndField(mainPanel, gbc, "Name:", nameField = new JTextField(20));

        // Address
        addLabelAndField(mainPanel, gbc, "Address:", addressField = new JTextField(20));

        // Phone Number
        addLabelAndField(mainPanel, gbc, "Phone Number:", phoneNumField = new JTextField(20));

        // Customer Type
        JPanel customerTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        customerTypePanel.setBackground(Color.BLACK);
        commercialRadio = new JRadioButton("Commercial");
        domesticRadio = new JRadioButton("Domestic");
        styleRadioButton(commercialRadio);
        styleRadioButton(domesticRadio);
        ButtonGroup customerTypeGroup = new ButtonGroup();
        customerTypeGroup.add(commercialRadio);
        customerTypeGroup.add(domesticRadio);
        customerTypePanel.add(commercialRadio);
        customerTypePanel.add(domesticRadio);
        addLabelAndComponent(mainPanel, gbc, "Customer Type:", customerTypePanel);

        // Meter Type
        JPanel meterTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        meterTypePanel.setBackground(Color.BLACK);
        singlePhaseRadio = new JRadioButton("Single Phase");
        threePhaseRadio = new JRadioButton("Three Phase");
        styleRadioButton(singlePhaseRadio);
        styleRadioButton(threePhaseRadio);
        ButtonGroup meterTypeGroup = new ButtonGroup();
        meterTypeGroup.add(singlePhaseRadio);
        meterTypeGroup.add(threePhaseRadio);
        meterTypePanel.add(singlePhaseRadio);
        meterTypePanel.add(threePhaseRadio);
        addLabelAndComponent(mainPanel, gbc, "Meter Type:", meterTypePanel);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.BLACK);
        submitButton = new JButton("Add");
        cancelButton = new JButton("Cancel");
        styleButton(submitButton);
        styleButton(cancelButton);
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);

        submitButton.addActionListener(e -> submitCustomer());
        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void submitCustomer() {
        String cnic = cnicField.getText();
        String name = nameField.getText();
        String address = addressField.getText();
        String phoneNum = phoneNumField.getText();
        String customerType = commercialRadio.isSelected() ? "Commercial" : "Domestic";
        String meterType = singlePhaseRadio.isSelected() ? "Single Phase" : "Three Phase";

        if (cnic.isEmpty() || name.isEmpty() || address.isEmpty() || phoneNum.isEmpty() ||
                (!commercialRadio.isSelected() && !domesticRadio.isSelected()) ||
                (!singlePhaseRadio.isSelected() && !threePhaseRadio.isSelected())) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Show the loading screen
        loading loadingScreen = new loading("Adding Customer");

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(1500);  // Simulate delay
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String connectionDate = sdf.format(new Date());

                String request = String.format("Customer,CUSTOMER_ADD,%s,%s,%s,%s,%s,%s,%s",
                        cnic, name, address, phoneNum, customerType, meterType, connectionDate);

                ClientSocket client = ClientSocket.getInstance();
                String response = client.sendRequest(request);

                loadingScreen.dispose(); // Close loading screen

                if ("Customer added successfully.".equals(response)) {
                    JOptionPane.showMessageDialog(AddCustomerGUI.this, "Customer added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(AddCustomerGUI.this, response, "Error", JOptionPane.ERROR_MESSAGE);
                }

                return null;
            }
        };
        worker.execute();
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
        panel.add(component, gbc);
    }

    private void styleTextField(JTextField field) {
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void styleRadioButton(JRadioButton radio) {
        radio.setBackground(Color.BLACK);
        radio.setForeground(Color.YELLOW);
        radio.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void styleButton(JButton button) {
        button.setBackground(Color.YELLOW);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }
}
