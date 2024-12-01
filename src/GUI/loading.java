package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class loading extends JFrame {
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private Timer timer;
    private int progress = 0;

    // Constructor that accepts a message and starts the loading process immediately
    public loading(String message) {
        init(message);  // Initialize the loading screen
        setVisible(true);  // Make the frame visible immediately
        timer.start();  // Start the loading process immediately
    }

    // Initialization method
    private void init(String message) {
        setTitle("Loading...");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

        // Status Label to display the message passed to the constructor
        statusLabel = new JLabel("Loading " + message + "...");
        statusLabel.setForeground(Color.YELLOW);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalGlue());
        panel.add(statusLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Customizing the JProgressBar
        progressBar = new JProgressBar(0, 100) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE); // Set percentage text color here
                String percentage = getValue() + "%";
                FontMetrics fm = g.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(percentage)) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g.drawString(percentage, x, y);
            }
        };
        progressBar.setStringPainted(false); // Disable default percentage text
        progressBar.setForeground(Color.YELLOW);
        progressBar.setBackground(Color.BLACK);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(progressBar);
        panel.add(Box.createVerticalGlue());

        add(panel);

        // Timer logic to update the progress bar
        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 4;
                progressBar.setValue(progress);
                if (progress == 100) {
                    timer.stop();
                    statusLabel.setText(message + " Loading Complete!");
                }
            }
        });
    }
}
