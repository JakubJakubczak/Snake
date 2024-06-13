import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Random;

/**
 * Panel gry, który obsługuje rysowanie i logikę gry węża.
 */
public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int sizeY = SCREEN_HEIGHT / UNIT_SIZE;
    static final int sizeX = SCREEN_WIDTH / UNIT_SIZE;
    static final int DELAY = 50;
    int[][] board = new int[sizeX][sizeY];
    private Apple apple;
    private Frog frog;
    private Snake playerSnake;
    private Obstacle obstacle;
    private SnakeAI aiSnake;
    javax.swing.Timer timer;
    Random random;
    JButton backToMenu;
    private static final String HIGHSCORE_FILE = "highscores.txt";
    private boolean scoreSaved = false;

    /**
     * Konstruktor inicjujący panel gry.
     */
    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    /**
     * Rozpoczyna grę, inicjalizując wszystkie obiekty gry.
     */
    public void startGame() {
        apple = new Apple(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE, board);
        frog = new Frog(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE, board);
        playerSnake = new Snake(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE, board);
        obstacle = new Obstacle(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE, board);
        aiSnake = new SnakeAI(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE, apple, frog, playerSnake, board);

        apple.start();
        new Thread(frog).start();
        playerSnake.start();
        aiSnake.start();

        timer = new javax.swing.Timer(DELAY, this);
        timer.start();
    }

    /**
     * Metoda rysująca komponenty gry.
     * @param g Obiekt Graphics do rysowania.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * Rysuje wszystkie elementy gry.
     * @param g Obiekt Graphics do rysowania.
     */
    public void draw(Graphics g) {
        if (playerSnake.getRunning()) {
            obstacle.draw(g);
            apple.draw(g);
            frog.draw(g);
            playerSnake.draw(g);
            aiSnake.draw(g);

            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + playerSnake.getScore(), (SCREEN_WIDTH - metrics.stringWidth("Score: " + playerSnake.getScore())) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    /**
     * Metoda wywoływana po zakończeniu gry. Rysuje ekran końcowy gry i zapisuje wynik.
     * @param g Obiekt Graphics do rysowania.
     */
    public void gameOver(Graphics g) {
        // Zapisz wynik, jeśli nie został jeszcze zapisany
        if (!scoreSaved) {
            saveScore(playerSnake.getScore());
            scoreSaved = true;
        }

        // Wynik
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + playerSnake.getScore(), (SCREEN_WIDTH - metrics1.stringWidth("Score: " + playerSnake.getScore())) / 2, g.getFont().getSize());

        // Tekst końcowy gry
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        // Przycisk powrotu do menu
        if (backToMenu == null) {
            backToMenu = new JButton("Back to menu");
            backToMenu.setBackground(Color.BLACK);
            backToMenu.setForeground(Color.WHITE);
            backToMenu.setFocusPainted(false);
            backToMenu.setFont(new Font("Tahoma", Font.BOLD, 16));
            backToMenu.setBounds((SCREEN_WIDTH - 150) / 2, SCREEN_HEIGHT / 2 + 100, 150, 50); // Pozycjonowanie przycisku

            // Dodaj nasłuchiwacze akcji
            backToMenu.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new GameMenu().setVisible(true);
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(GamePanel.this);
                    topFrame.dispose(); // Zamknij bieżące okno gry
                }
            });

            this.setLayout(null); // Użyj absolutnego pozycjonowania dla przycisku
            this.add(backToMenu);
            this.revalidate();
            this.repaint();
        }
    }

    /**
     * Zapisuje wynik do pliku.
     * @param score Wynik do zapisania.
     */
    private void saveScore(int score) {
        try {
            File file = new File(HIGHSCORE_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(Integer.toString(score));
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obsługuje zdarzenia akcji.
     * @param e Zdarzenie akcji.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (playerSnake.isAlive()) {
            repaint();
        }
    }

    /**
     * Klasa nasłuchująca zdarzenia klawiatury.
     */
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (playerSnake.getDirection() != 'R' && playerSnake.getY(0) != playerSnake.getY(1)) {
                        playerSnake.setDirection('L');
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (playerSnake.getDirection() != 'L' && playerSnake.getY(0) != playerSnake.getY(1)) {
                        playerSnake.setDirection('R');
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (playerSnake.getDirection() != 'D' && playerSnake.getX(0) != playerSnake.getX(1)) {
                        playerSnake.setDirection('U');
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (playerSnake.getDirection() != 'U' && playerSnake.getX(0) != playerSnake.getX(1)) {
                        playerSnake.setDirection('D');
                    }
                    break;
            }
        }
    }
}
