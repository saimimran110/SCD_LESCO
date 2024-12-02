package GUI;

import lesco.bill.system.a1.pkg22l.pkg7906.ClientSocket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AddEmployeeGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton addButton;

    public AddEmployeeGUI() {
        setTitle("LESCO BILLING SYSTEM");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Creating the header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.BLACK);
        headerPanel.setPreferredSize(new Dimension(1000, 100));
        headerPanel.setLayout(null);

        JButton backButton = new JButton("←");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setBounds(10, 10, 60, 60);
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.YELLOW);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        backButton.addActionListener(e -> {
            dispose();
            new StartingPage(); // Assuming you have a StartingPage class
        });
        headerPanel.add(backButton);

        JLabel headerLabel = new JLabel("LESCO BILLING SYSTEM");
        headerLabel.setForeground(Color.YELLOW);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headerLabel.setBounds(300, 20, 500, 50);
        headerPanel.add(headerLabel);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        // Title
        JLabel titleLabel = new JLabel("Add Your Employee");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridy++;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        formPanel.add(usernameLabel, gbc);

        gbc.gridx++;
        gbc.anchor = GridBagConstraints.LINE_START;
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameField.setPreferredSize(new Dimension(250, 40));
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        formPanel.add(passwordLabel, gbc);

        gbc.gridx++;
        gbc.anchor = GridBagConstraints.LINE_START;
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField.setPreferredSize(new Dimension(250, 40));
        formPanel.add(passwordField, gbc);

        // Add Button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addButton = new JButton("Add");
        addButton.setBackground(Color.BLACK);
        addButton.setForeground(Color.YELLOW);
        addButton.setPreferredSize(new Dimension(150, 50));
        addButton.setFont(new Font("Arial", Font.BOLD, 20));
        formPanel.add(addButton, gbc);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                SwingWorker<Void, Void> worker = new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // Show the loading screen
                        loading loadingScreen = new loading("Adding Employee");
                        Thread.sleep(3000); // Simulate a delay (e.g., processing time)
                        loadingScreen.dispose();
                        return null;
                    }

                    @Override
                    protected void done() {
                        // Call the server to add the employee
                        try {
                            String request = "Employee,ADD_EMPLOYEE," + username + "," + password;
                            ClientSocket client = ClientSocket.getInstance();
                            String response = client.sendRequest(request);

                            if ("Employee added successfully.".equals(response)) {
                                JOptionPane.showMessageDialog(null, "Employee added successfully!");
                                dispose();
                                new StartingPage(); // Navigate to StartingPage
                            } else {
                                JOptionPane.showMessageDialog(null, response, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (IOException | ClassNotFoundException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error connecting to the server. Please try again.", "Connection Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };

                worker.execute();
            }
        });

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}
