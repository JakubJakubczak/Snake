import java.util.Random;


public class Frog implements Runnable {
    private int x, y;
    private int boardWidth, boardHeight, unitSize;

    public Frog(int boardWidth, int boardHeight, int unitSize) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.unitSize = unitSize;
        generateNewPosition();
    }

    public void generateNewPosition() {
        Random random = new Random();
        x = random.nextInt(boardWidth / unitSize) * unitSize;
        y = random.nextInt(boardHeight / unitSize) * unitSize;
    }

    public void move() {
        // Example movement logic, can be enhanced for more complex behavior
        Random random = new Random();
        int direction = random.nextInt(4);
        switch (direction) {
            case 0: y = (y - unitSize + boardHeight) % boardHeight; break; // Move up
            case 1: y = (y + unitSize) % boardHeight; break; // Move down
            case 2: x = (x - unitSize + boardWidth) % boardWidth; break; // Move left
            case 3: x = (x + unitSize) % boardWidth; break; // Move right
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public void run() {
        while (true) {
            move();
            try {
                Thread.sleep(500); // Frog moves every half second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}