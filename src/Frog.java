import java.awt.*;
import java.util.Random;

/**
 * Klasa reprezentująca żabę w grze. Żaba porusza się losowo po planszy i zmienia swoje położenie po zjedzeniu przez węża.
 */
public class Frog implements Runnable {
    private int x, y;
    private int boardWidth, boardHeight, unitSize;
    private int jump = 3;
    private int sizeX, sizeY;
    int[][] board;

    /**
     * Konstruktor klasy Frog inicjalizujący pozycję żaby na planszy.
     * 
     * @param boardWidth Szerokość planszy.
     * @param boardHeight Wysokość planszy.
     * @param unitSize Rozmiar jednostki planszy.
     * @param board Dwuwymiarowa tablica reprezentująca planszę.
     */
    public Frog(int boardWidth, int boardHeight, int unitSize, int[][] board) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.unitSize = unitSize;
        this.sizeX = boardWidth / unitSize;
        this.sizeY = boardHeight / unitSize;
        this.board = board;
        generateNewPosition();
        board[x][y] = 2;
    }

    /**
     * Generuje nową pozycję żaby na planszy, upewniając się, że nie koliduje ona z przeszkodami ani wężem.
     */
    public void generateNewPosition() {
        Random random = new Random();
        while (true) {
            x = random.nextInt(boardWidth / unitSize);
            y = random.nextInt(boardHeight / unitSize);
            // Upewnij się, że żaba nie pojawia się na przeszkodzie lub w wężu
            if (!CheckField(x, y)) break;
        }
        board[x][y] = 2;
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
     * Przesuwa żabę w losowym kierunku.
     */
    public void move() {
        Random random = new Random();
        int x_start = x;
        int y_start = y;
        int direction = random.nextInt(4);
        while (true) {
            switch (direction) {
                case 0:
                    y = (y + jump) % (sizeY - 1);
                    break; // Ruch w dół
                case 1:
                    if (y - jump >= 0) y = y - jump;
                    else y = (sizeY - 1) + (y - jump);
                    break; // Ruch w górę
                case 2:
                    if (x - jump >= 0) x = x - jump;
                    else x = (sizeX - 1) + (x - jump);
                    break; // Ruch w lewo
                case 3:
                    x = (x + jump) % (sizeX - 1);
                    break; // Ruch w prawo
            }
            if (!CheckField(x, y)) break;
        }
        board[x][y] = 2;
        board[x_start][y_start] = 0;
    }

    /**
     * Zwraca pozycję x żaby w jednostkach planszy.
     * 
     * @return Pozycja x żaby.
     */
    public int getX() { 
        return x; 
    }

    /**
     * Zwraca pozycję y żaby w jednostkach planszy.
     * 
     * @return Pozycja y żaby.
     */
    public int getY() { 
        return y; 
    }

    /**
     * Rysuje żabę na planszy.
     * 
     * @param g Obiekt Graphics do rysowania.
     */
    public void draw(Graphics g) {
        g.setColor(new Color(60, 150, 45));
        g.fillRect(x * unitSize, y * unitSize, unitSize, unitSize);
    }

    /**
     * Sprawdza, czy żaba została zjedzona.
     * 
     * @param x Pozycja x na planszy.
     * @param y Pozycja y na planszy.
     * @return true, jeśli żaba została zjedzona, false w przeciwnym razie.
     */
    public boolean IsFrogEaten(int x, int y) {
        return board[x][y] != 2;
    }

    /**
     * Główna pętla działania żaby, która sprawdza jej stan i porusza się co 1000 ms.
     */
    @Override
    public void run() {
        long lastActionTime1000ms = System.currentTimeMillis();
        long currentTime;
        while (true) {
            currentTime = System.currentTimeMillis();
            if (IsFrogEaten(x, y)) {
                generateNewPosition();
            }

            if (currentTime - lastActionTime1000ms >= 1000) {
                move();
                lastActionTime1000ms = currentTime;
            }
            try {
                Thread.sleep(75); // Żaba sprawdza swój stan co 75 ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
