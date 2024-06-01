import java.util.Random;

public class Apple {
    private int x, y;

    public Apple(int boardWidth, int boardHeight, int unitSize) {
        generateNewPosition(boardWidth, boardHeight, unitSize);
    }

    public void generateNewPosition(int boardWidth, int boardHeight, int unitSize) {
        Random random = new Random();
        x = random.nextInt(boardWidth / unitSize) * unitSize;
        y = random.nextInt(boardHeight / unitSize) * unitSize;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}