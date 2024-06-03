import java.awt.*;

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

    private void initSnake() {
        for (int i = 0; i < bodyParts; i++) {
            x[i] = (bodyParts - i - 1);
            y[i] = 0;
            board[x[i]][y[i]] = -2;
        }
    }

    private void updateSnake() {
        for (int i = 0; i < bodyParts; i++) {
            board[x[i]][y[i]] = -2;
        }
    }

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

    public int getBodyParts() {
        return bodyParts;
    }

    public int getScore() {
        return score;
    }

    public boolean getRunning() {
        return running;
    }

    private boolean checkApple() {
        if (board[x[0]][y[0]] == 1) {
            bodyParts++;
            score++;
            return true;
        } else {
            return false;
        }
    }

    private boolean checkFrog() {
        if (board[x[0]][y[0]] == 2) {
            bodyParts++;
            score++;
            return true;
        } else {
            return false;
        }
    }

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

    private void decideDirection() {
        if (apple == null) return;
    
        int appleX = apple.getX() / unitSize;
        int appleY = apple.getY() / unitSize;
    
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

    private char findSafeDirection() {
        char[] directions = {'U', 'D', 'L', 'R'};
        for (char dir : directions) {
            if (!willHitWall(dir) && !willHitSelf(dir)) {
                return dir;
            }
        }
        return direction; 
    }

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
        long currentTime = System.currentTimeMillis();
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
