import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenu extends JFrame {

    public GameMenu() {
        // Set up the frame
        setTitle("Game Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create buttons
        JButton startButton = createButton("Start", new Color(60, 179, 113));
        JButton closeButton = createButton("Close", new Color(255, 69, 0));
        JButton showResultsButton = createButton("Show Results", new Color(70, 130, 180));

        // Add action listeners
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeGame();
            }
        });

        showResultsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showResults();
            }
        });

        // Set up the layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        buttonPanel.add(startButton);
        buttonPanel.add(showResultsButton);
        buttonPanel.add(closeButton);

        add(buttonPanel);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Tahoma", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    private void startGame() {
        // Code to start the game
        System.out.println("Game started!");
        // You can replace this with your game starting logic
        new GameFrame();// Example of starting your game
    }

    private void closeGame() {
        // Code to close the game
        System.out.println("Game closed!");
        System.exit(0);
    }

    private void showResults() {
        // Code to show results
        System.out.println("Showing results...");
        // You can replace this with your results showing logic
    }
}