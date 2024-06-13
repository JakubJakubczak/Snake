import java.awt.*;
import java.util.Random;

/**
 * Klasa reprezentująca przeszkodę w grze. Przeszkoda jest generowana na planszy i składa się z kilku jednostek.
 */
public class Obstacle {
    private int boardWidth, boardHeight, unitSize;
    private int sizeX, sizeY;
    private int size;
    private int[] x, y;

    /**
     * Konstruktor klasy Obstacle inicjalizujący pozycję przeszkody na planszy.
     * 
     * @param boardWidth Szerokość planszy.
     * @param boardHeight Wysokość planszy.
     * @param unitSize Rozmiar jednostki planszy.
     * @param board Dwuwymiarowa tablica reprezentująca planszę.
     */
    public Obstacle(int boardWidth, int boardHeight, int unitSize, int[][] board) {
        this.unitSize = unitSize;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.sizeX = boardWidth / unitSize;
        this.sizeY = boardHeight / unitSize;
        generateObstacle(board);
    }

    /**
     * Generuje nową przeszkodę na planszy.
     * 
     * @param board Dwuwymiarowa tablica reprezentująca planszę.
     */
    public void generateObstacle(int[][] board) {
        Random random = new Random();
        int max = 11;
        int min = 4;
        int direction;
        int start_pointX, start_pointY;

        size = random.nextInt((max - min) + 1) + min;
        this.size = size;
        x = new int[size];
        y = new int[size];
        direction = random.nextInt(2); // 0 - góra/dół, 1 - lewo/prawo

        if (direction == 0) {
            start_pointX = random.nextInt(sizeX);
            start_pointY = random.nextInt(sizeY - size);
        } else {
            start_pointX = random.nextInt(sizeX - size);
            start_pointY = random.nextInt(sizeY);
        }

        x[0] = start_pointX;
        y[0] = start_pointY;

        if (direction == 0) {
            for (int i = 1; i < size; i++) {
                x[i] = start_pointX;
                y[i] = start_pointY + i;
            }
        }
        if (direction == 1) {
            for (int i = 1; i < size; i++) {
                x[i] = start_pointX + i;
                y[i] = start_pointY;
            }
        }

        // Inicjalizacja globalnej tablicy planszy
        for (int i = 0; i < size; i++) {
            board[x[i]][y[i]] = -3; // -3 oznacza przeszkodę
        }
    }

    /**
     * Rysuje przeszkodę na planszy.
     * 
     * @param g Obiekt Graphics do rysowania.
     */
    public void draw(Graphics g) {
        for (int i = 0; i < size; i++) {
            g.setColor(new Color(150, 75, 0));
            g.fillRect(x[i] * unitSize, y[i] * unitSize, unitSize, unitSize);
        }
    }
}
