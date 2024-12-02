package GUI;

import Controller.EmployeeController;
import lesco.bill.system.a1.pkg22l.pkg7906.ClientSocket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdatePasswordUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private EmployeeController ec;
   
    public UpdatePasswordUI() {
        ec = new EmployeeController();
        setTitle("Update Password");
        setSize(600, 400); // Increased window size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setLocationRelativeTo(null); // Center the window
        setResizable(false);

        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 15, 15, 15); // Increased padding

        // Username label and text field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Increased font size
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20); // Increased text field size
        usernameField.setPreferredSize(new Dimension(200, 30)); // Increased dimension
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        // Current Password label and text field
        JLabel currentPasswordLabel = new JLabel("Current Password:");
        currentPasswordLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Increased font size
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(currentPasswordLabel, gbc);

        currentPasswordField = new JPasswordField(20); // Increased text field size
        currentPasswordField.setPreferredSize(new Dimension(200, 30)); // Increased dimension
        gbc.gridx = 1;
        mainPanel.add(currentPasswordField, gbc);

        // New Password label and text field
        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Increased font size
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(newPasswordLabel, gbc);

        newPasswordField = new JPasswordField(20); // Increased text field size
        newPasswordField.setPreferredSize(new Dimension(200, 30)); // Increased dimension
        gbc.gridx = 1;
        mainPanel.add(newPasswordField, gbc);

        // Submit button
        JButton submitButton = new JButton("Update Password");
        submitButton.setFont(new Font("Arial", Font.BOLD, 18)); // Increased button font
        submitButton.setBackground(Color.BLACK);
        submitButton.setForeground(Color.YELLOW);
        submitButton.setPreferredSize(new Dimension(200, 40)); // Increased button size
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(submitButton, gbc);

        // Add action listener for button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUpdatePassword();
            }
        });

        // Add main panel to the frame
        add(mainPanel);
        setVisible(true);
    }

    // Function to handle password update
    private void handleUpdatePassword() {
        String username = usernameField.getText();
        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());

        // Basic validation to check if fields are empty
        if (username.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Prepare the request string for the server
            String request = String.format("Employee,UPDATE_PASSWORD,%s,%s,%s", username, currentPassword, newPassword);

            ClientSocket client = ClientSocket.getInstance();
            String response = client.sendRequest(request);

            // Process the server's response
            if (response.equalsIgnoreCase("Password updated successfully.")) {
                showPasswordChangedPopup(username, currentPassword, newPassword);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, response, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void showPasswordChangedPopup(String username, String newPassword, String newPassword1)
    {
    JDialog popup = new JDialog(this, "Password Updated", true);
    popup.setSize(400, 200);
    popup.setLocationRelativeTo(this);

    JPanel popupPanel = new JPanel(new BorderLayout());
    popupPanel.setBackground(Color.WHITE);

    JLabel messageLabel = new JLabel("<html>Username: <b>" + username + "</b><br>New Password: <b>" + newPassword1 + "</b></html>", SwingConstants.CENTER);
    messageLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    popupPanel.add(messageLabel, BorderLayout.CENTER);

    JButton closeButton = new JButton("Close");
    closeButton.setBackground(Color.BLACK);
    closeButton.setForeground(Color.YELLOW);
    closeButton.setPreferredSize(new Dimension(80, 30));  // Set a smaller preferred size for the button
    popupPanel.add(closeButton, BorderLayout.SOUTH);

    // Close the pop-up when clicking the button
    closeButton.addActionListener(e -> popup.dispose());

    popup.add(popupPanel);

    // Timer to automatically close the pop-up after 3 seconds
    Timer timer = new Timer(3000, e -> popup.dispose());
    timer.setRepeats(false); // Only run once
    timer.start();

    popup.setVisible(true);
}

 }
