package GUI;

import Controller.CustomerController;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BillPredictionUI extends JFrame {
    private JRadioButton singlePhaseRadio, threePhaseRadio;
    private JTextField idField, regularUnitField, peakUnitField;
    private JButton predictButton, cancelButton;
    private JTextArea resultArea;
    private JLabel peakUnitLabel;
    private CustomerController cc;

    // Color scheme
    private final Color YELLOW = new Color(255, 255, 0);
    private final Color BLACK = Color.BLACK;
    private final Color WHITE = Color.WHITE;
    private final Color DARK_GRAY = new Color(64, 64, 64);

    public BillPredictionUI() {
        cc = new CustomerController();
        setTitle("LESCO Bill Prediction");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BLACK);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(YELLOW);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("LESCO Bill Prediction");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(BLACK);
        headerPanel.add(titleLabel);

        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BLACK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Customer ID
        addLabelAndField(mainPanel, gbc, "Customer ID:", idField = new JTextField(15));

        // Meter Type
        addLabelAndRadioButtons(mainPanel, gbc, "Meter Type:",
                singlePhaseRadio = new JRadioButton("Single Phase"),
                threePhaseRadio = new JRadioButton("Three Phase"));

        // Regular Units
        addLabelAndField(mainPanel, gbc, "Regular Units:", regularUnitField = new JTextField(15));

        // Peak Units
        peakUnitLabel = new JLabel("Peak Units:");
        styleLabel(peakUnitLabel);
        peakUnitField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(peakUnitLabel, gbc);
        gbc.gridx = 1;
        styleTextField(peakUnitField);
        mainPanel.add(peakUnitField, gbc);

        // Result Area
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JLabel resultLabel = new JLabel("Prediction Result:");
        styleLabel(resultLabel);
        mainPanel.add(resultLabel, gbc);

        gbc.gridy++;
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        resultArea.setBackground(DARK_GRAY);
        resultArea.setForeground(WHITE);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(YELLOW),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, gbc);

        // Action Listeners
        singlePhaseRadio.addActionListener(e -> updateFieldsForMeterType());
        threePhaseRadio.addActionListener(e -> updateFieldsForMeterType());

        // Initial update of the form based on default selection
        singlePhaseRadio.setSelected(true);
        updateFieldsForMeterType();

        return mainPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(BLACK);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        predictButton = new JButton("Predict Bill");
        cancelButton = new JButton("Cancel");
        styleButton(predictButton);
        styleButton(cancelButton);
        
        predictButton.addActionListener(e -> predictBill());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(predictButton);
        buttonPanel.add(cancelButton);

        return buttonPanel;
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel label = new JLabel(labelText);
        styleLabel(label);
        panel.add(label, gbc);

        gbc.gridx = 1;
        styleTextField(field);
        panel.add(field, gbc);
    }

    private void addLabelAndRadioButtons(JPanel panel, GridBagConstraints gbc, String labelText, JRadioButton radio1, JRadioButton radio2) {
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel label = new JLabel(labelText);
        styleLabel(label);
        panel.add(label, gbc);

        gbc.gridx = 1;
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        radioPanel.setBackground(BLACK);
        styleRadioButton(radio1);
        styleRadioButton(radio2);

        ButtonGroup group = new ButtonGroup();
        group.add(radio1);
        group.add(radio2);

        radioPanel.add(radio1);
        radioPanel.add(radio2);
        panel.add(radioPanel, gbc);
    }

    private void styleLabel(JLabel label) {
        label.setForeground(YELLOW);
        label.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private void styleTextField(JTextField field) {
        field.setBackground(WHITE);
        field.setForeground(BLACK);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(YELLOW),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    private void styleRadioButton(JRadioButton radio) {
        radio.setBackground(BLACK);
        radio.setForeground(YELLOW);
        radio.setFont(new Font("Arial", Font.PLAIN, 14));
        radio.setFocusPainted(false);
    }

    private void styleButton(JButton button) {
        button.setBackground(YELLOW);
        button.setForeground(BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BLACK),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)));
    }

    private void updateFieldsForMeterType() {
        peakUnitField.setEnabled(threePhaseRadio.isSelected());
        peakUnitField.setText("");
    }

    private void predictBill() {
        String id = idField.getText();
        String regularUnitStr = regularUnitField.getText();
        String peakUnitStr = peakUnitField.isEnabled() ? peakUnitField.getText() : "";

        if (id.isEmpty() || regularUnitStr.isEmpty() || (threePhaseRadio.isSelected() && peakUnitStr.isEmpty())) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int regularUnits = Integer.parseInt(regularUnitStr);
            int peakUnits = threePhaseRadio.isSelected() ? Integer.parseInt(peakUnitStr) : 0;

            ArrayList<String> predictionResult = cc.PRIDICT_BILL(id, regularUnits, peakUnits);

            resultArea.setText("");
            for (String line : predictionResult) {
                if (!line.equals("false") && !line.equals("true")) {
                    resultArea.append(line + "\n");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for units.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}