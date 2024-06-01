public class Snake extends Thread {
    private int[] x, y;
    private int bodyParts;
    private char direction;
    private boolean running;
    private int boardWidth, boardHeight, unitSize;
    private int score;
    private Apple apple;
    private Frog frog;

    public Snake(int boardWidth, int boardHeight, int unitSize, Apple apple, Frog frog) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.unitSize = unitSize;
        this.apple = apple;
        this.frog = frog;
        x = new int[boardWidth * boardHeight / (unitSize * unitSize)];
        y = new int[boardWidth * boardHeight / (unitSize * unitSize)];
        bodyParts = 6;
        direction = 'R';
        running = true;
        score = 0;
        initSnake();
    }

    private void initSnake() {
        for (int i = 0; i < bodyParts; i++) {
            x[i] = (bodyParts - i) * unitSize;
            y[i] = 0;
        }
    }

    private void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U': y[0] = y[0] - unitSize; break;
            case 'D': y[0] = y[0] + unitSize; break;
            case 'L': x[0] = x[0] - unitSize; break;
            case 'R': x[0] = x[0] + unitSize; break;
        }
    }

    public int getBodyParts() {
        return bodyParts;
    }
    public int getScore(){
        return score;
    }
    public int getX(int i) {
        if (i >= 0 && i < bodyParts) {
            return x[i];
        } else {
            throw new IndexOutOfBoundsException("Invalid index: " + i);
        }
    }

    public int getY(int i) {
        if (i >= 0 && i < bodyParts) {
            return y[i];
        } else {
            throw new IndexOutOfBoundsException("Invalid index: " + i);
        }
    }
    private void checkApple() {
        if (x[0] == apple.getX() && y[0] == apple.getY()) {
            bodyParts++;
            score++;
            apple.generateNewPosition(boardWidth, boardHeight, unitSize);
        }
    }

    private void checkFrog() {
        if (x[0] == frog.getX() && y[0] == frog.getY()) {
            bodyParts++;
            score++;
            frog.generateNewPosition();
        }
    }

    private void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        if (x[0] < 0 || x[0] >= boardWidth || y[0] < 0 || y[0] >= boardHeight) {
            running = false;
        }

        if (!running) {
            interrupt();
        }
    }

    public void changeDirection(char newDirection) {
        if ((newDirection == 'L' && direction != 'R') ||
                (newDirection == 'R' && direction != 'L') ||
                (newDirection == 'U' && direction != 'D') ||
                (newDirection == 'D' && direction != 'U')) {
            direction = newDirection;
        }
    }

    @Override
    public void run() {
        while (running) {
            move();
            checkApple();
            checkFrog();
            checkCollisions();
            try {
                Thread.sleep(100); // Control snake speed
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }
}
