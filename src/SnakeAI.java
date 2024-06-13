import java.awt.*;

/**
 * Klasa reprezentująca węża sterowanego przez sztuczną inteligencję w grze Snake. Wąż AI porusza się po planszy, zbiera jabłka i żaby, oraz unika przeszkód.
 */
public class SnakeAI extends Thread {
    private int[] x, y;
    private int bodyParts;
    private char direction;
    private boolean running;
    private int boardWidth, boardHeight, unitSize;
    private int score;
    private Apple apple;
    private Frog frog;
    private Snake playerSnake;
    private int sizeX, sizeY;
    int[][] board;

    /**
     * Konstruktor klasy SnakeAI inicjalizujący węża AI na planszy.
     * 
     * @param boardWidth Szerokość planszy.
     * @param boardHeight Wysokość planszy.
     * @param unitSize Rozmiar jednostki planszy.
     * @param apple Obiekt klasy Apple reprezentujący jabłko na planszy.
     * @param frog Obiekt klasy Frog reprezentujący żabę na planszy.
     * @param playerSnake Obiekt klasy Snake reprezentujący węża gracza.
     * @param board Dwuwymiarowa tablica reprezentująca planszę.
     */
    public SnakeAI(int boardWidth, int boardHeight, int unitSize, Apple apple, Frog frog, Snake playerSnake, int[][] board) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.unitSize = unitSize;
        this.apple = apple;
        this.frog = frog;
        this.playerSnake = playerSnake;
        this.board = board;
        this.sizeX = boardWidth / unitSize;
        this.sizeY = boardHeight / unitSize;
        x = new int[sizeX * sizeY];
        y = new int[sizeX * sizeY];
        bodyParts = 6;
        score = 0;
        direction = 'R';
        running = true;
        initSnake();
    }

    /**
     * Rysuje węża AI na planszy.
     * 
     * @param g Obiekt Graphics do rysowania.
     */
    public void draw(Graphics g) {
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(Color.WHITE);
                g.fillRect(x[i] * unitSize, y[i] * unitSize, unitSize, unitSize);
            } else {
                g.setColor(new Color(10, 10, 150));
                g.fillRect(x[i] * unitSize, y[i] * unitSize, unitSize, unitSize);
            }
        }
    }

    /**
     * Inicjalizuje pozycję początkową węża AI.
     */
    private void initSnake() {
        for (int i = 0; i < bodyParts; i++) {
            x[i] = (bodyParts - i - 1);
            y[i] = 0;
            board[x[i]][y[i]] = -2;
        }
    }

    /**
     * Aktualizuje pozycję węża AI na planszy.
     */
    private void updateSnake() {
        for (int i = 0; i < bodyParts; i++) {
            board[x[i]][y[i]] = -2;
        }
    }

    /**
     * Porusza wężem AI w kierunku zgodnym z bieżącym kierunkiem.
     */
    private void move() {
        int endOfSnakeX = x[bodyParts - 1];
        int endOfSnakeY = y[bodyParts - 1];
        for (int i = bodyParts - 1; i > 0; i--) {
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
     * Zwraca liczbę części ciała węża AI.
     * 
     * @return Liczba części ciała węża AI.
     */
    public int getBodyParts() {
        return bodyParts;
    }

    /**
     * Zwraca wynik węża AI.
     * 
     * @return Wynik węża AI.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sprawdza, czy gra z wężem AI nadal trwa.
     * 
     * @return true, jeśli gra trwa, false w przeciwnym razie.
     */
    public boolean getRunning() {
        return running;
    }

    /**
     * Sprawdza, czy wąż AI zjadł jabłko.
     * 
     * @return true, jeśli wąż AI zjadł jabłko, false w przeciwnym razie.
     */
    private boolean checkApple() {
        if (board[x[0]][y[0]] == 1) {
            bodyParts++;
            score++;
            logApplePosition();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sprawdza, czy wąż AI zjadł żabę.
     * 
     * @return true, jeśli wąż AI zjadł żabę, false w przeciwnym razie.
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
     * Sprawdza kolizje węża AI z przeszkodami i ścianami planszy.
     */
    private void checkCollisions() {
        if (x[0] < 0 || x[0] >= sizeX || y[0] < 0 || y[0] >= sizeY) {
            running = false;
            return;
        }

        if (board[x[0]][y[0]] < 0) {
            running = false;
            return;
        }

        if (!running) {
            interrupt();
        }
    }

    /**
     * Decyduje o kierunku ruchu węża AI w oparciu o pozycję jabłka.
     */
    private void decideDirection() {
        if (apple == null) return;
        
        int appleX = apple.getX() / unitSize;
        int appleY = apple.getY() / unitSize;
        
        // Debugowanie pozycji jabłka
        System.out.println("Apple position in AI: (" + appleX + ", " + appleY + ")");
        
        int deltaX = appleX - x[0];
        int deltaY = appleY - y[0];
        
        char newDirection = direction;
        
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0 && direction != 'L') {
                newDirection = 'R';
            } else if (deltaX < 0 && direction != 'R') {
                newDirection = 'L';
            }
        } else {
            if (deltaY > 0 && direction != 'U') {
                newDirection = 'D';
            } else if (deltaY < 0 && direction != 'D') {
                newDirection = 'U';
            }
        }
        
        if (willHitWall(newDirection) || willHitSelf(newDirection)) {
            char safeDirection = findSafeDirection();
            if (safeDirection != direction) {
                newDirection = safeDirection;
            }
        }
        
        direction = newDirection;
    }

    /**
     * Sprawdza, czy wąż AI uderzy w ścianę, jeśli będzie kontynuował ruch w zadanym kierunku.
     * 
     * @param dir Kierunek ruchu do sprawdzenia.
     * @return true, jeśli wąż AI uderzy w ścianę, false w przeciwnym razie.
     */
    private boolean willHitWall(char dir) {
        int nextX = x[0];
        int nextY = y[0];

        switch (dir) {
            case 'U': nextY -= 1; break;
            case 'D': nextY += 1; break;
            case 'L': nextX -= 1; break;
            case 'R': nextX += 1; break;
        }

        return nextX < 0 || nextX >= sizeX || nextY < 0 || nextY >= sizeY;
    }

    /**
     * Sprawdza, czy wąż AI uderzy w siebie, jeśli będzie kontynuował ruch w zadanym kierunku.
     * 
     * @param dir Kierunek ruchu do sprawdzenia.
     * @return true, jeśli wąż AI uderzy w siebie, false w przeciwnym razie.
     */
    private boolean willHitSelf(char dir) {
        int nextX = x[0];
        int nextY = y[0];

        switch (dir) {
            case 'U': nextY -= 1; break;
            case 'D': nextY += 1; break;
            case 'L': nextX -= 1; break;
            case 'R': nextX += 1; break;
        }

        return board[nextX][nextY] < 0;
    }

    /**
     * Znajduje bezpieczny kierunek ruchu dla węża AI, unikający kolizji.
     * 
     * @return Bezpieczny kierunek ruchu.
     */
    private char findSafeDirection() {
        char[] directions = {'U', 'D', 'L', 'R'};
        for (char dir : directions) {
            if (!willHitWall(dir) && !willHitSelf(dir)) {
                return dir;
            }
        }
        return direction; 
    }

    /**
     * Loguje pozycję jabłka.
     */
    private void logApplePosition() {
        if (apple != null) {
            int appleX = apple.getX() / unitSize;
            int appleY = apple.getY() / unitSize;
        }
    }

    /**
     * Główna pętla działania węża AI, która decyduje o kierunku i porusza wężem co 150 ms.
     */
    @Override
    public void run() {
        while (apple == null) {
            try {
                Thread.sleep(10); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long lastActionTime100ms = System.currentTimeMillis();
        long currentTime;
        while (running) {
            currentTime = System.currentTimeMillis();

            if (currentTime - lastActionTime100ms >= 10) {
                decideDirection();
                move();
                lastActionTime100ms = currentTime;
            }
            try {
                Thread.sleep(150); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
