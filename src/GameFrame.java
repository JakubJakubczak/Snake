import javax.swing.JFrame;

/**
 * Klasa reprezentująca główne okno gry Snake.
 */
public class GameFrame extends JFrame {

    /**
     * Konstruktor klasy GameFrame inicjalizujący główne okno gry.
     */
    GameFrame() {
        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
