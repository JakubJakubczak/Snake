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

    public SnakeAI(int boardWidth, int boardHeight, int unitSize, Apple apple, Frog frog, Snake playerSnake) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.unitSize = unitSize;
        this.apple = apple;
        this.frog = frog;
        this.playerSnake = playerSnake;
        x = new int[boardWidth * boardHeight / (unitSize * unitSize)];
        y = new int[boardWidth * boardHeight / (unitSize * unitSize)];
        bodyParts = 6;
        score=0;
        direction = 'R';
        running = true;
        initSnake();
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

    private void initSnake() {
        for (int i = 0; i < bodyParts; i++) {
            x[i] = (bodyParts - i) * unitSize;
            y[i] = unitSize;
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

    private void checkApple() {
        if (x[0] == apple.getX() && y[0] == apple.getY()) {
            bodyParts++;
            score++;
            // ******************
            // zakomentowane, bo byl mocno zmieniany program, bez udziału snakeAI
            //apple.generateNewPosition(boardWidth, boardHeight, unitSize);
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

    private void decideDirection() {
        // Simple AI to move towards the apple or frog
        if (Math.abs(x[0] - apple.getX()) > Math.abs(y[0] - apple.getY())) {
            direction = (x[0] > apple.getX()) ? 'L' : 'R';
        } else {
            direction = (y[0] > apple.getY()) ? 'U' : 'D';
        }
    }

    @Override
    public void run() {
        while (running) {
            decideDirection();
            move();
            checkApple();
            checkFrog();
            checkCollisions();
            try {
                Thread.sleep(150); // Control AI snake speed
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }
}