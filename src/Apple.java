import java.awt.*;
import java.util.Random;

public class Apple extends Thread{
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

    public void generateNewPosition(int boardWidth, int boardHeight, int unitSize,int[][] board) {
        Random random = new Random();
        while(true) {
            x = random.nextInt(boardWidth / unitSize);
            y = random.nextInt(boardHeight / unitSize);
            // **********8
            // dodać, żeby jabłko się nie respiło na przeszkodzie lub w którymś wężu - to w check collision
            if(!CheckField(x,y)) break;
        }
        board[x][y] = 1;
    }
    public boolean CheckField(int x, int y) {
        if(board[x][y] == 0)
            return false;
        else
            return true;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(x*unitSize, y*unitSize, unitSize, unitSize);
    }

    public boolean IsAppleCollected(int x, int y){
        if(board[x][y] == 1) return false;
        else return true;
    }

    @Override
    public void run() {
        while (true) {
            if(IsAppleCollected(x,y))
                generateNewPosition(boardWidth, boardHeight, unitSize, board);
            try {
                Thread.sleep(DELAY); // Frog moves every second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        }
    }
