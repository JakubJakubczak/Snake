import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Klasa reprezentująca menu główne gry Snake.
 */
public class GameMenu extends JFrame {

    private static final String HIGHSCORE_FILE = "highscores.txt";

    /**
     * Konstruktor klasy GameMenu, który ustawia interfejs graficzny menu.
     */
    public GameMenu() {
        // Ustawienia okna
        setTitle("Game Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Wycentrowanie okna

        // Tworzenie przycisków
        JButton startButton = createButton("Start", new Color(60, 179, 113));
        JButton closeButton = createButton("Close", new Color(255, 69, 0));
        JButton showResultsButton = createButton("Show Results", new Color(70, 130, 180));

        // Dodawanie nasłuchiwaczy akcji
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

        // Ustawienie układu
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Dodanie marginesu
        buttonPanel.add(startButton);
        buttonPanel.add(showResultsButton);
        buttonPanel.add(closeButton);

        add(buttonPanel);
    }

    /**
     * Tworzy przycisk z podanym tekstem i kolorem.
     * 
     * @param text Tekst wyświetlany na przycisku.
     * @param color Kolor tła przycisku.
     * @return Utworzony przycisk.
     */
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Tahoma", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    /**
     * Rozpoczyna grę.
     */
    private void startGame() {
        // Kod rozpoczynający grę
        System.out.println("Game started!");
        new GameFrame(); // Przykład uruchomienia gry
    }

    /**
     * Zamyka grę.
     */
    private void closeGame() {
        // Kod zamykający grę
        System.out.println("Game closed!");
        System.exit(0);
    }

    /**
     * Pokazuje wyniki.
     */
    private void showResults() {
        // Kod wyświetlający wyniki
        System.out.println("Showing results...");
        showHighScores();
    }

    /**
     * Wyświetla najwyższe wyniki w nowym oknie.
     */
    private void showHighScores() {
        JFrame frame = new JFrame("High Scores");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); // Wycentrowanie okna

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        try {
            File file = new File(HIGHSCORE_FILE);
            if (!file.exists()) {
                textArea.setText("No high scores yet!");
            } else {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                List<Integer> scores = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    scores.add(Integer.parseInt(line));
                }
                reader.close();
                Collections.sort(scores, Collections.reverseOrder());
                StringBuilder sb = new StringBuilder("Top 5 High Scores:\n");
                for (int i = 0; i < Math.min(5, scores.size()); i++) {
                    sb.append((i + 1) + ". " + scores.get(i) + "\n");
                }
                textArea.setText(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.add(scrollPane);
        frame.setVisible(true);
    }
}
