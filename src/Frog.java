import java.awt.*;
import java.util.Random;


public class Frog implements Runnable {
    private int x, y;
    private int boardWidth, boardHeight, unitSize;
    private int jump = 3;
    private int sizeX, sizeY;

    public Frog(int boardWidth, int boardHeight, int unitSize) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.unitSize = unitSize;
        this.sizeX = boardWidth / unitSize;
        this.sizeY = boardHeight / unitSize;
        generateNewPosition();
    }

    public void generateNewPosition() {
        Random random = new Random();
        x = random.nextInt(boardWidth / unitSize);
        y = random.nextInt(boardHeight / unitSize);
    }

    public void CheckCollison(){

    }
    public void move() {
        // Example movement logic, can be enhanced for more complex behavior
        Random random = new Random();
        int direction = random.nextInt(4);
        switch (direction) {
            case 0: y = (y+jump)%(sizeY-1); break;
            case 1: if(y-jump >= 0)  y=y-jump;
                    else y = (sizeY -1) + (y-jump); break; // Move down
            case 2: if(x-jump >= 0)  x=x-jump;
                    else x = (sizeX -1) + (x-jump); break; // Move left
            case 3: x = (x+jump)%(sizeX-1); break; // Move right
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void draw(Graphics g) {
        g.setColor(new Color(60, 150, 45));
        g.fillRect(x*unitSize, y*unitSize, unitSize, unitSize);
    }

    @Override
    public void run() {
        while (true) {
            move();
            try {
                Thread.sleep(1000); // Frog moves every second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}