package GUI;

import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartingPage extends JFrame {

    // Constructor
    public StartingPage() {
        init();  // Initialize the UI components
        setVisible(true);  // Make sure the frame is visible after initialization
    }

    // Initialize the components
    public void init() {
        setTitle("Lesco Billing System");
        setSize(1000, 700);  // Updated frame size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the window

        JPanel panel = new JPanel();
        panel.setLayout(null);  // Use absolute positioning
        panel.setBackground(Color.WHITE);  // Set background to white

        // Add Logo
        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon("logo.jpg");  // Load image from Logo.jpg
        logoLabel.setIcon(logoIcon);
        logoLabel.setVisible(true);
        logoLabel.setBounds(30, 30, 70, 70);  // Increased size and repositioned
        panel.add(logoLabel);

        // Title Label
        JLabel titleLabel = new JLabel("Lesco Billing System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));  // Increased font size
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 0, 1000, 100);  // Centered and widened
        panel.add(titleLabel);

        // Button: Select Role
        JButton selectRoleButton = new JButton("Select Role");
        selectRoleButton.setFont(new Font("Arial", Font.PLAIN, 18));  // Larger font
        selectRoleButton.setBounds(400, 250, 200, 50);  
        selectRoleButton.setBackground(Color.BLACK);  // Set button background to black
        selectRoleButton.setForeground(Color.YELLOW);  // Set button text to yellow
        selectRoleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: Add functionality to handle Select Role button click
                System.out.println("Select Role button pressed");
                dispose();
                new RoleSelection();
            }
        });
        panel.add(selectRoleButton);

        // Button: Add Employee
        JButton addEmployeeButton = new JButton("Add Employee");
        addEmployeeButton.setFont(new Font("Arial", Font.PLAIN, 18));  // Larger font
        addEmployeeButton.setBounds(400, 320, 200, 50);  // Repositioned and resized
        addEmployeeButton.setBackground(Color.BLACK);  // Set button background to black
        addEmployeeButton.setForeground(Color.YELLOW);  // Set button text to yellow
        addEmployeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: Add functionality to handle Add Employee button click
                System.out.println("Add Employee button pressed");
                dispose();
               new AddEmployeeGUI();
            }
        });
        panel.add(addEmployeeButton);

        // Button: Exit
        JButton exitButton = new JButton("EXIT");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 18));  // Larger font
        exitButton.setBounds(400, 390, 200, 50);  // Repositioned and resized
        exitButton.setBackground(Color.BLACK);  // Set button background to black
        exitButton.setForeground(Color.YELLOW);  // Set button text to yellow
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: Add functionality to handle EXIT button click
                System.exit(0);  // Exit the application
            }
        });
        panel.add(exitButton);

        // Add the panel to the frame
        add(panel);
    }

  
}
