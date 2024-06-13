import java.awt.*;

/**
 * Klasa reprezentująca węża w grze Snake. Wąż porusza się po planszy, zbiera jabłka i żaby, oraz unika przeszkód.
 */
public class Snake extends Thread {
    private int[] x, y;
    private int bodyParts;
    private char direction;
    private boolean running;
    private int boardWidth, boardHeight, unitSize;
    private int score;
    private int sizeX, sizeY;
    int[][] board;

    /**
     * Konstruktor klasy Snake inicjalizujący węża na planszy.
     * 
     * @param boardWidth Szerokość planszy.
     * @param boardHeight Wysokość planszy.
     * @param unitSize Rozmiar jednostki planszy.
     * @param board Dwuwymiarowa tablica reprezentująca planszę.
     */
    public Snake(int boardWidth, int boardHeight, int unitSize, int[][] board) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.unitSize = unitSize;
        this.board = board;
        this.sizeX = boardWidth / unitSize;
        this.sizeY = boardHeight / unitSize;
        x = new int[sizeX * sizeY];
        y = new int[sizeX * sizeY];
        bodyParts = 6;
        direction = 'R';
        running = true;
        score = 0;
        initSnake();
    }

    /**
     * Rysuje węża na planszy.
     * 
     * @param g Obiekt Graphics do rysowania.
     */
    public void draw(Graphics g) {
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(Color.GREEN);
                g.fillRect(x[i] * unitSize, y[i] * unitSize, unitSize, unitSize);
            } else {
                g.setColor(new Color(30, 190, 0));
                g.fillRect(x[i] * unitSize, y[i] * unitSize, unitSize, unitSize);
            }
        }
    }

    /**
     * Inicjalizuje pozycję początkową węża.
     */
    private void initSnake() {
        for (int i = 0; i < bodyParts; i++) {
            x[i] = (bodyParts - i);
            y[i] = 0;
            board[x[i]][y[i]] = -1;
        }
    }

    /**
     * Aktualizuje pozycję węża na planszy.
     */
    private void updateSnake() {
        for (int i = 0; i < bodyParts; i++) {
            board[x[i]][y[i]] = -1;
        }
    }

    /**
     * Porusza wężem w kierunku zgodnym z bieżącym kierunkiem.
     */
    private void move() {
        int endOfSnakeX = x[bodyParts - 1];
        int endOfSnakeY = y[bodyParts - 1];
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U': y[0] = y[0] - 1; break;
            case 'D': y[0] = y[0] + 1; break;
            case 'L': x[0] = x[0] - 1; break;
            case 'R': x[0] = x[0] + 1; break;
        }
        checkCollisions();
        if (!running) {
            return;
        }
        if (checkApple() || checkFrog()) {
            updateSnake();
        } else {
            board[endOfSnakeX][endOfSnakeY] = 0;
            updateSnake();
        }
    }

    /**
     * Zwraca liczbę części ciała węża.
     * 
     * @return Liczba części ciała węża.
     */
    public int getBodyParts() {
        return bodyParts;
    }

    /**
     * Zwraca wynik gracza.
     * 
     * @return Wynik gracza.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sprawdza, czy gra nadal trwa.
     * 
     * @return true, jeśli gra trwa, false w przeciwnym razie.
     */
    public boolean getRunning() {
        return running;
    }

    /**
     * Zwraca pozycję x części ciała węża.
     * 
     * @param i Indeks części ciała.
     * @return Pozycja x części ciała.
     */
    public int getX(int i) {
        if (i >= 0 && i < bodyParts) {
            return x[i];
        } else {
            throw new IndexOutOfBoundsException("Invalid index: " + i);
        }
    }

    /**
     * Zwraca pozycję y części ciała węża.
     * 
     * @param i Indeks części ciała.
     * @return Pozycja y części ciała.
     */
    public int getY(int i) {
        if (i >= 0 && i < bodyParts) {
            return y[i];
        } else {
            throw new IndexOutOfBoundsException("Invalid index: " + i);
        }
    }

    /**
     * Sprawdza, czy wąż zjadł jabłko.
     * 
     * @return true, jeśli wąż zjadł jabłko, false w przeciwnym razie.
     */
    private boolean checkApple() {
        if (board[x[0]][y[0]] == 1) {
            bodyParts++;
            score++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sprawdza, czy wąż zjadł żabę.
     * 
     * @return true, jeśli wąż zjadł żabę, false w przeciwnym razie.
     */
    private boolean checkFrog() {
        if (board[x[0]][y[0]] == 2) {
            bodyParts++;
            score++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sprawdza kolizje węża z przeszkodami i ścianami planszy.
     */
    private void checkCollisions() {
        if (x[0] < 0 || x[0] > (sizeX - 1) || y[0] < 0 || y[0] > (sizeY - 1)) {
            running = false;
            return;
        }

        if (board[x[0]][y[0]] < 0) { // liczba ujemna oznacza przeszkodę
            running = false;
            return;
        }

        if (!running) {
            interrupt();
        }
    }

    /**
     * Ustawia nowy kierunek ruchu węża.
     * 
     * @param newDirection Nowy kierunek ruchu ('U' - góra, 'D' - dół, 'L' - lewo, 'R' - prawo).
     */
    public void setDirection(char newDirection) {
        direction = newDirection;
    }

    /**
     * Zwraca bieżący kierunek ruchu węża.
     * 
     * @return Bieżący kierunek ruchu.
     */
    public char getDirection() {
        return direction;
    }

    /**
     * Główna pętla działania węża, która porusza go co 150 ms.
     */
    @Override
    public void run() {
        while (running) {
            move();
            try {
                Thread.sleep(150); // Kontroluje prędkość węża
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }
}
