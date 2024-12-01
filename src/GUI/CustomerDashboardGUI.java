package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerDashboardGUI extends JFrame {
    private String customerName;

    public CustomerDashboardGUI(String customerName) {
        this.customerName = customerName;
        setTitle("LESCO BILLING SYSTEM - Customer Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.RED);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));

        ImageIcon icon = new ImageIcon(getClass().getResource("Logout.jpg"));
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);
        JButton logoutButton = new JButton(scaledIcon);

        logoutButton.setForeground(Color.YELLOW);
        logoutButton.setBackground(Color.RED);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(CustomerDashboardGUI.this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);  // Terminate the program
                }
            }
        });
        headerPanel.add(logoutButton, BorderLayout.WEST);

        JLabel welcomeLabel = new JLabel("Welcome " + customerName);
        welcomeLabel.setForeground(Color.YELLOW);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] tasks = {
            "View Current Bill", "Update Units Consumed", 
            "Update CNIC Expiry Date", "Predict Your Bill"
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
            case "View Current Bill":
               new ViewCurrentBillUI(); // Opens a new window to view the current bill
                break;
            case "Update Units Consumed":
                 new UpdateUnitsUI(); // Allows the customer to update their unit consumption
                break;
            case "Update CNIC Expiry Date":
                 new UpdateCNICExpiryUI(); // UI to update CNIC expiry date
                break;
            case "Predict Your Bill":
              new BillPredictionUI(); // Opens a UI to predict bill based on unit consumption
                break;
        }
    }

}
