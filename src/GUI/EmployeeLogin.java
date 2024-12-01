package GUI;

import Controller.EmployeeController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeLogin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private EmployeeController employeeController;

    public EmployeeLogin() {
        employeeController = new EmployeeController();

        setTitle("LESCO BILLING SYSTEM - Employee Login");
        setSize(1000, 700); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

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
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RoleSelection(); 
            }
        });
        headerPanel.add(backButton);

        // Add "Identify Yourself" Label to the Header Panel
       

        JLabel headerLabel = new JLabel("LESCO BILLING SYSTEM");
        headerLabel.setForeground(Color.YELLOW);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headerLabel.setBounds(300, 50, 500, 50);
        headerPanel.add(headerLabel);

        JLabel titleLabel = new JLabel("Employee Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBounds(400, 120, 300, 50);
        headerPanel.add(titleLabel);
        
         JLabel identifyLabel = new JLabel("Identify Yourself");
        identifyLabel.setForeground(Color.BLACK);
        identifyLabel.setFont(new Font("Arial", Font.BOLD, 40));
        identifyLabel.setBounds(350, 150, 500, 50);
        add(identifyLabel);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        // Username Field
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridy++;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 20));
        usernameField.setPreferredSize(new Dimension(300, 50));
        formPanel.add(usernameField, gbc);

        // Password Field
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordField.setPreferredSize(new Dimension(300, 50));
        formPanel.add(passwordField, gbc);

        // Login Button
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

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                boolean loginSuccessful = employeeController.LOGIN_EMPLOYEE(username, password);

                if (loginSuccessful) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    dispose();
                    new EmployeeDashboardGUI(username);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}