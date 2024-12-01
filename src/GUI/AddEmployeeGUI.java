package GUI;

import Controller.EmployeeController;  // Import the controller
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddEmployeeGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton addButton;
    private EmployeeController employeeController; // Reference to the controller

    public AddEmployeeGUI() {
        employeeController = new EmployeeController(); // Initialize the controller

        setTitle("LESCO BILLING SYSTEM");
        setSize(1000, 700); // Window size updated to 1000x800
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Creating the header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.BLACK);
        headerPanel.setPreferredSize(new Dimension(1000, 100)); // Adjusted size for the header
        headerPanel.setLayout(null); // Using null layout to position the back button freely

        // Back Button
        JButton backButton = new JButton("←");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setBounds(10, 10, 60, 60); // Positioned at top left
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.YELLOW);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close current window
                new StartingPage(); // Assuming you have a StartingPage class to go back to
            }
        });
        headerPanel.add(backButton);

        // Header Label
        JLabel headerLabel = new JLabel("LESCO BILLING SYSTEM");
        headerLabel.setForeground(Color.YELLOW);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 30)); // Increased font size for larger window
        headerLabel.setBounds(300, 20, 500, 50); // Positioned in the center
        headerPanel.add(headerLabel);

        // Creating the form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE); // Set form background to white
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Increased padding for a larger window
        gbc.anchor = GridBagConstraints.CENTER;

        // Title label
        JLabel titleLabel = new JLabel("Add Your Employee");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Increased font size for the title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // Username label and text field
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridy++;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Increased font size for labels
        formPanel.add(usernameLabel, gbc);

        gbc.gridx++;
        gbc.anchor = GridBagConstraints.LINE_START;
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 18)); // Adjusted font size for input fields
        usernameField.setPreferredSize(new Dimension(250, 40)); // Increased height of the input field
        formPanel.add(usernameField, gbc);

        // Password label and password field
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Adjusted font size for password label
        formPanel.add(passwordLabel, gbc);

        gbc.gridx++;
        gbc.anchor = GridBagConstraints.LINE_START;
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18)); // Adjusted font size for password field
        passwordField.setPreferredSize(new Dimension(250, 40)); // Increased height of the input field
        formPanel.add(passwordField, gbc);

        // Add button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addButton = new JButton("Add");
        addButton.setBackground(Color.BLACK);
        addButton.setForeground(Color.YELLOW);
        addButton.setPreferredSize(new Dimension(150, 50)); // Increased button size
        addButton.setFont(new Font("Arial", Font.BOLD, 20)); // Adjusted font size for the button
        formPanel.add(addButton, gbc);

        // Adding action listener to the Add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the text from the username and password fields
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // Show the loading screen
                        loading loadingScreen = new loading("Employee");

                        // Simulate the loading screen (wait for it to complete)
                        Thread.sleep(3000); // 3-second simulated delay

                        // Once loading is done, dispose of the loading screen
                        loadingScreen.dispose();
                        return null;
                    }

                    @Override
                    protected void done() {
                        // This code runs on the Event Dispatch Thread after doInBackground() is done
                        // Call the EmployeeController to add the employee
                        if (employeeController.ADD_EMPLOYEE(username, password)) {
                            JOptionPane.showMessageDialog(null, "Employee added Successfully!!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Username already exists");
                        }

                        dispose(); // Close the current window
                        new StartingPage(); // Open the starting page
                    }
                };

                worker.execute(); // Start the worker (background task)
            }
        });

        // Setting up the layout
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}
