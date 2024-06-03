import java.awt.*;

public class Snake extends Thread {
    private int[] x, y;
    private int bodyParts;
    private char direction;
    private boolean running;
    private int boardWidth, boardHeight, unitSize;
    private int score;
    private int sizeX, sizeY;
    int[][] board;

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

    public void draw(Graphics g) {
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(Color.GREEN);
                g.fillRect(x[i]*unitSize, y[i]*unitSize, unitSize, unitSize);
            } else {
                g.setColor(new Color(30, 190, 0));
                g.fillRect(x[i]*unitSize, y[i]*unitSize, unitSize, unitSize);
            }
        }
    }
    private void initSnake() {
        for (int i = 0; i < bodyParts; i++) {
            x[i] = (bodyParts - i);
            y[i] = 0;
            board[x[i]][y[i]] = -1;
        }
    }

    private void updateSnake() {
        for (int i = 0; i < bodyParts; i++) {
            board[x[i]][y[i]] = -1;
        }
    }

    private void move() {
        int endOfSnakeX = x[bodyParts-1];
        int endOfSnakeY = y[bodyParts-1];
        System.out.println(x[0] + " " + y[0]);
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
        if(!running){
            return;
        }
        if(checkApple() || checkFrog()){
            updateSnake();
        }
        else{
            board[endOfSnakeX][endOfSnakeY] = 0;
            updateSnake();
        };
    }

    public int getBodyParts() {
        return bodyParts;
    }
    public int getScore(){
        return score;
    }
    public boolean getRunning(){
        return running;
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
    private boolean checkApple() {
        if (board[x[0]][y[0]] == 1) {
            bodyParts++;
            score++;
            return true;
        }
        else return false;
    }

    private boolean checkFrog() {
        if (board[x[0]][y[0]] == 2) {
            bodyParts++;
            score++;
            return true;
        }
        else return false;
    }

    private void checkCollisions() {
//        for (int i = bodyParts; i > 0; i--) {
//            if (x[0] == x[i] && y[0] == y[i]) {
//                running = false;
//            }
//        }
//
//        // head of snake collides with -2(snakeAI) or static obstacle(-3)
//        if (board[x[0]][y[0]] == -2 || board[x[0]][y[0]] == -3) { // negative number is obstacle
//            running = false;
//        }
        if (x[0] < 0 || x[0] > (sizeX -1) || y[0] < 0 || y[0] > (sizeY -1)) {
            running = false;
            return;
        }

        if (board[x[0]][y[0]] < 0) { // negative number is obstacle
            running = false;
            return;
        }


        if (!running) {
            interrupt();
        }
    }

    public void setDirection(char newDirection) {
            direction = newDirection;
        }
    public char getDirection() {
        return direction;
    }

    @Override
    public void run() {
        while (running) {
            move();
            try {
                Thread.sleep(150); // Control snake speed
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }
}
