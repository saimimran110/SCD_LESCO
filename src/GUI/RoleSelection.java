package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoleSelection extends JFrame {

    // Constructor
    public RoleSelection() {
        init();
        setVisible(true);
    }

    // Initialize the components
    private void init() {
        setTitle("Lesco Billing System - Role Selection");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        // Title Label
        JLabel titleLabel = new JLabel("LESCO BILLING SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 0, 1000, 80);
        panel.add(titleLabel);

        // Back Button
        JButton backButton = new JButton("←");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setBounds(10, 10, 60, 60);
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.YELLOW);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Back button pressed");
                dispose();
                new StartingPage();
            }
        });
        panel.add(backButton);

        // Subheading Label
        JLabel subLabel = new JLabel("LOGIN AS?");
        subLabel.setFont(new Font("Arial", Font.BOLD, 40));
        subLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subLabel.setBounds(350, 150, 300, 40);
        panel.add(subLabel);

        // Button: Employee
        JButton employeeButton = new JButton("Employee");
        employeeButton.setFont(new Font("Arial", Font.PLAIN, 18));
        employeeButton.setBackground(Color.BLACK);
        employeeButton.setForeground(Color.YELLOW);
        employeeButton.setBounds(400, 250, 200, 50);
        employeeButton.addActionListener(new ActionListener() {
            
            
            public void actionPerformed(ActionEvent e) {
                System.out.println("Employee button pressed");
                dispose();
                new EmployeeLogin();
                
            }
        });
        panel.add(employeeButton);

        // Button: Customer
        JButton customerButton = new JButton("Customer");
        customerButton.setFont(new Font("Arial", Font.PLAIN, 18));
        customerButton.setBackground(Color.BLACK);
        customerButton.setForeground(Color.YELLOW);
        customerButton.setBounds(400, 320, 200, 50);
        customerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Customer button pressed");
                new CustomerLogin();
            }
        });
        panel.add(customerButton);

        // Add the panel to the frame
        add(panel);
    }
}