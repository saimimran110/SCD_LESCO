package GUI;

import Controller.CustomerController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CustomerLogin extends JFrame {
    private JTextField customerIdField;
    private JTextField cnicField;
    private JButton loginButton;
    private CustomerController cc;

    public CustomerLogin() {
        cc = new CustomerController();
        setTitle("LESCO BILLING SYSTEM - Customer Login");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.BLACK);
        headerPanel.setPreferredSize(new Dimension(1000, 100));
        headerPanel.setLayout(null);

        // Back button
        JButton backButton = new JButton("←");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setBounds(10, 10, 60, 60);
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.YELLOW);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close window
                new RoleSelection(); // Navigate to role selection page
            }
        });
        headerPanel.add(backButton);

        // Header label
        JLabel headerLabel = new JLabel("LESCO BILLING SYSTEM");
        headerLabel.setForeground(Color.YELLOW);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headerLabel.setBounds(300, 20, 500, 50);
        headerPanel.add(headerLabel);

        // Title label
        JLabel titleLabel = new JLabel("Customer Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBounds(400, 120, 300, 50);
        headerPanel.add(titleLabel);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        // Customer ID label and text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel customerIdLabel = new JLabel("Customer ID:");
        customerIdLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        formPanel.add(customerIdLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        customerIdField = new JTextField();
        customerIdField.setFont(new Font("Arial", Font.PLAIN, 20));
        customerIdField.setPreferredSize(new Dimension(300, 50));
        formPanel.add(customerIdField, gbc);

        // CNIC label and text field
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel cnicLabel = new JLabel("CNIC:");
        cnicLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        formPanel.add(cnicLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        cnicField = new JTextField();
        cnicField.setFont(new Font("Arial", Font.PLAIN, 20));
        cnicField.setPreferredSize(new Dimension(300, 50));
        formPanel.add(cnicField, gbc);

        // Key listener to automatically insert dashes
        cnicField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                 char c = e.getKeyChar();
                 String text = cnicField.getText();

                // If the input is not a digit, ignore it
                if (!Character.isDigit(c)) {
                    e.consume();
                    return;
                }

                // If we have 13 digits, prevent adding more characters
                if (text.length()> 13) {
                    e.consume();
                    return;
                }

                
                if (text.length() == 5 || text.length() == 13) {
                    cnicField.setText(text + "-");
                }
            }
        });

        // Login button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.YELLOW);
        loginButton.setPreferredSize(new Dimension(200, 60));
        loginButton.setFont(new Font("Arial", Font.BOLD, 22));
        formPanel.add(loginButton, gbc);

        // ActionListener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = customerIdField.getText();
                String cnic = cnicField.getText().replaceAll("-", ""); // Remove dashes before using the input

                if (cc.CUSTOMER_LOGIN(id, cnic)) {
                    JOptionPane.showMessageDialog(null, "Customer Logged in Successfully!!\n");
                    dispose();
                    new CustomerDashboardGUI(cc.GIVE_NAME_OF_CUSTOMER(id));
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Id or CNIC");
                }

            }
        });

        // Set layout and add panels
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}
