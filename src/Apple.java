import java.awt.*;
import java.util.Random;

/**
 * Klasa reprezentująca jabłko w grze. Jabłko jest generowane na planszy i zmienia swoje położenie po zebraniu przez węża.
 */
public class Apple extends Thread {
    private int x, y;
    private int boardWidth, boardHeight, unitSize;
    private int sizeX, sizeY;
    static final int DELAY = 75;
    int[][] board;

    /**
     * Konstruktor klasy Apple inicjalizujący pozycję jabłka na planszy.
     * 
     * @param boardWidth Szerokość planszy.
     * @param boardHeight Wysokość planszy.
     * @param unitSize Rozmiar jednostki planszy.
     * @param board Dwuwymiarowa tablica reprezentująca planszę.
     */
    public Apple(int boardWidth, int boardHeight, int unitSize, int[][] board) {
        this.unitSize = unitSize;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.sizeX = boardWidth / unitSize;
        this.sizeY = boardHeight / unitSize;
        this.board = board;
        generateNewPosition(boardWidth, boardHeight, unitSize, board);
        board[x][y] = 1;
    }

    /**
     * Generuje nową pozycję jabłka na planszy, upewniając się, że nie koliduje ono z przeszkodami ani wężem.
     * 
     * @param boardWidth Szerokość planszy.
     * @param boardHeight Wysokość planszy.
     * @param unitSize Rozmiar jednostki planszy.
     * @param board Dwuwymiarowa tablica reprezentująca planszę.
     */
    public void generateNewPosition(int boardWidth, int boardHeight, int unitSize, int[][] board) {
        Random random = new Random();
        while (true) {
            x = random.nextInt(boardWidth / unitSize);
            y = random.nextInt(boardHeight / unitSize);
            // Upewnij się, że jabłko nie pojawia się na przeszkodzie lub w wężu
            if (!CheckField(x, y)) break;
        }
        board[x][y] = 1;
        System.out.println("Apple generated at: (" + x + ", " + y + ")");
    }

    /**
     * Sprawdza, czy pole na planszy jest zajęte.
     * 
     * @param x Pozycja x na planszy.
     * @param y Pozycja y na planszy.
     * @return true, jeśli pole jest zajęte, false w przeciwnym razie.
     */
    public boolean CheckField(int x, int y) {
        return board[x][y] != 0;
    }

    /**
     * Zwraca pozycję x jabłka w pikselach.
     * 
     * @return Pozycja x jabłka.
     */
    public int getX() { 
        return x * unitSize; 
    }

    /**
     * Zwraca pozycję y jabłka w pikselach.
     * 
     * @return Pozycja y jabłka.
     */
    public int getY() { 
        return y * unitSize; 
    }

    /**
     * Ustawia nową pozycję jabłka i aktualizuje planszę.
     * 
     * @param x Nowa pozycja x jabłka.
     * @param y Nowa pozycja y jabłka.
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        board[x][y] = 1; // Aktualizowanie planszy
    }

    /**
     * Rysuje jabłko na planszy.
     * 
     * @param g Obiekt Graphics do rysowania.
     */
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(x * unitSize, y * unitSize, unitSize, unitSize);
    }

    /**
     * Sprawdza, czy jabłko zostało zebrane.
     * 
     * @param x Pozycja x na planszy.
     * @param y Pozycja y na planszy.
     * @return true, jeśli jabłko zostało zebrane, false w przeciwnym razie.
     */
    public boolean IsAppleCollected(int x, int y) {
        return board[x][y] != 1;
    }

    /**
     * Główna pętla działania jabłka, która sprawdza jego stan co 75 ms.
     */
    @Override
    public void run() {
        while (true) {
            if (IsAppleCollected(x, y)) {
                generateNewPosition(boardWidth, boardHeight, unitSize, board);
            }
            try {
                Thread.sleep(DELAY); // Jabłko sprawdza swój stan co 75ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
