import java.awt.*;
import java.util.Random;


public class Frog implements Runnable {
    private int x, y;
    private int boardWidth, boardHeight, unitSize;
    private int jump = 3;
    private int sizeX, sizeY;
    int[][] board;

    public Frog(int boardWidth, int boardHeight, int unitSize,int[][] board) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.unitSize = unitSize;
        this.sizeX = boardWidth / unitSize;
        this.sizeY = boardHeight / unitSize;
        this.board = board;
        generateNewPosition();
        board[x][y] = 2;
    }

    public void generateNewPosition() {
        Random random = new Random();
        while(true) { // tu mogą być błędy !!!!!!!!!!!
            x = random.nextInt(boardWidth / unitSize);
            y = random.nextInt(boardHeight / unitSize);
            // **********8
            // dodać, żeby jabłko się nie respiło na przeszkodzie lub w którymś wężu - to w check collision
            if(!CheckField(x,y)) break;
        }
        board[x][y] = 2;
    }

    public boolean CheckField(int x, int y) {
        if(board[x][y] == 0)
            return false;
        else
            return true;
    }
//    public void CheckCollison(){
//
//    }
    public void move() {
        // Example movement logic, can be enhanced for more complex behavior
        Random random = new Random();
        int x_start = x;
        int y_start = y;
        int direction = random.nextInt(4);
        while(true) {
            switch (direction) {
                case 0:
                    y = (y + jump) % (sizeY - 1);
                    break;
                case 1:
                    if (y - jump >= 0) y = y - jump;
                    else y = (sizeY - 1) + (y - jump);
                    break; // Move down
                case 2:
                    if (x - jump >= 0) x = x - jump;
                    else x = (sizeX - 1) + (x - jump);
                    break; // Move left
                case 3:
                    x = (x + jump) % (sizeX - 1);
                    break; // Move right
            }
            if(!CheckField(x,y)) break;
        }
        board[x][y] = 2;
        board[x_start][y_start] = 0;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void draw(Graphics g) {
        g.setColor(new Color(60, 150, 45));
        g.fillRect(x*unitSize, y*unitSize, unitSize, unitSize);
    }
    public boolean IsFrogEaten(int x, int y){
        if(board[x][y] == 2) return false;
        else return true;
    }

    @Override
    public void run() {
        long lastActionTime1000ms = System.currentTimeMillis();
        long currentTime = System.currentTimeMillis();
        while (true) {
            currentTime = System.currentTimeMillis();
            if(IsFrogEaten(x,y))
                generateNewPosition();

            if (currentTime - lastActionTime1000ms >= 1000) {
                move();
                lastActionTime1000ms = currentTime;
            }
            try {
                Thread.sleep(75); // Frog moves every second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}