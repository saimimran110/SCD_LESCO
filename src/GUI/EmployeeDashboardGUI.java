package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeDashboardGUI extends JFrame {
    private String employeeName;

    public EmployeeDashboardGUI(String En) {
        employeeName = En;
        setTitle("LESCO BILLING SYSTEM - Employee Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.BLACK);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));

        ImageIcon icon = new ImageIcon(getClass().getResource("Logout.jpg"));
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);
        JButton logoutButton = new JButton(scaledIcon);

        logoutButton.setForeground(Color.YELLOW);
        logoutButton.setBackground(Color.BLACK);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(EmployeeDashboardGUI.this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);  // Terminate the program
                }
            }
        });
        headerPanel.add(logoutButton, BorderLayout.WEST);

        JLabel welcomeLabel = new JLabel("Welcome " + employeeName);
        welcomeLabel.setForeground(Color.YELLOW);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 20, 20));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] tasks = {
            "Update Bill Status", "Update Password", "Generate Bill",
            "View Customers with Expiring CNICs", "View Paid and Unpaid Bills",
            "Add Customer", "Update Tariff Taxes", "View Customer Details"
        };

        for (String task : tasks) {
            JButton button = new JButton(task);
            button.setBackground(Color.BLACK);
            button.setForeground(Color.YELLOW);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setFocusPainted(false);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleButtonClick(task);
                }
            });
            buttonPanel.add(button);
        }

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private void handleButtonClick(String task) {
        switch (task) {
            case "Update Bill Status":
                new UpdateBillStatusUI();
                break;
            case "Update Password":
                new UpdatePasswordUI();
                break;
            case "Generate Bill":
                new GenerateBillUI();
                break;
            case "View Customers with Expiring CNICs":
                new ExpiringCNICGUI();
                break;
            case "View Paid and Unpaid Bills":
                new PaidUnpaidBillsGUI();
                break;
            case "Add Customer":
                new AddCustomerGUI();
                break;
            case "Update Tariff Taxes":
                new TariffTaxManagerUI();
                break;
            case "View Customer Details":
               CustomerWindow customerWindow = new CustomerWindow();
            customerWindow.setVisible(true); 
                break;
        }
    }
}
