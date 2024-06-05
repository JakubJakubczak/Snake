import java.awt.*;
import java.util.Random;

public class Apple extends Thread {
    private int x, y;
    private int boardWidth, boardHeight, unitSize;
    private int sizeX, sizeY;
    static final int DELAY = 75;
    int[][] board;

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

    public boolean CheckField(int x, int y) {
        return board[x][y] != 0;
    }

    public int getX() { 
        return x * unitSize; 
    }

    public int getY() { 
        return y * unitSize; 
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        board[x][y] = 1; // Aktualizowanie planszy
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(x * unitSize, y * unitSize, unitSize, unitSize);
    }

    public boolean IsAppleCollected(int x, int y) {
        return board[x][y] != 1;
    }

    @Override
    public void run() {
        while (true) {
            if (IsAppleCollected(x, y)) {
                generateNewPosition(boardWidth, boardHeight, unitSize, board);
            }
            try {
                Thread.sleep(DELAY); // Apple checks its status every 75ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
