import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int sizeY = SCREEN_HEIGHT/UNIT_SIZE;
    static final int sizeX = SCREEN_WIDTH/UNIT_SIZE;
    static final int DELAY = 75;
    int[][] board = new int[sizeX][sizeY];
    private Apple apple;
    private Frog frog;
    private Snake playerSnake;
    //private SnakeAI aiSnake;
    Timer timer;
    Random random;
    JButton backToMenu;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame() {
        apple = new Apple(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE, board);
        frog = new Frog(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE);
        playerSnake = new Snake(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE, board);
        //aiSnake = new SnakeAI(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE, apple, frog, playerSnake);

        apple.start();
        new Thread(frog).start();
        playerSnake.start();
        //aiSnake.start();

        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {

        if(playerSnake.getRunning()) {
            apple.draw(g);
            frog.draw(g);
            playerSnake.draw(g);

//            // Print the top border
//            System.out.print("  "); // Space for row labels
//            for (int j = 0; j < sizeY; j++) {
//                System.out.print("  " + j + " ");
//            }
//            System.out.println();
//
//            // Print each row
//            for (int i = 0; i < sizeX; i++) {
//                System.out.print(i + " "); // Row label
//                for (int j = 0; j < sizeY; j++) {
//                    System.out.print("| " + board[i][j] + " ");
//                }
//                System.out.println("|");
//
//                // Print the row separator
//                System.out.print("  "); // Space for row labels
//                for (int j = 0; j < sizeY; j++) {
//                    System.out.print("----");
//                }
//                System.out.println("-");
//            }
//            for (int i = 0; i < aiSnake.getBodyParts(); i++) {
//                if (i == 0) {
//                    g.setColor(Color.red);
//                    g.fillRect(aiSnake.getX(i), aiSnake.getY(i), UNIT_SIZE, UNIT_SIZE);
//                } else {
//                    g.setColor(new Color(255, 100, 0));
//                    g.fillRect(aiSnake.getX(i), aiSnake.getY(i), UNIT_SIZE, UNIT_SIZE);
//                }
//            }

            g.setColor(Color.red);
            g.setFont( new Font("Ink Free",Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+playerSnake.getScore(), (SCREEN_WIDTH - metrics.stringWidth("Score: "+playerSnake.getScore()))/2, g.getFont().getSize());

//            g.setColor(Color.BLUE);
//            g.setFont( new Font("Ink Free",Font.BOLD, 40));
//            FontMetrics metrics2 = getFontMetrics(g.getFont());
//            g.drawString("Score for AI: "+aiSnake.getScore(), (SCREEN_WIDTH - metrics2.stringWidth("Score for AI: "+aiSnake.getScore()))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }

    }

    public void gameOver(Graphics g) {
        //Score
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+playerSnake.getScore(), (SCREEN_WIDTH - metrics1.stringWidth("Score: "+playerSnake.getScore()))/2, g.getFont().getSize());
        //Game Over text
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        //Button to back to menu
        if (backToMenu == null) {
            // Button to back to menu
            backToMenu = new JButton("Back to menu");
            backToMenu.setBackground(Color.BLACK);
            backToMenu.setForeground(Color.WHITE);
            backToMenu.setFocusPainted(false);
            backToMenu.setFont(new Font("Tahoma", Font.BOLD, 16));
            backToMenu.setBounds((SCREEN_WIDTH - 150) / 2, SCREEN_HEIGHT / 2 + 100, 150, 50); // Position the button

            // Add action listeners
            backToMenu.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new GameMenu().setVisible(true);
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(GamePanel.this);
                    topFrame.dispose(); // Close the current game window
                }
            });

            this.setLayout(null); // Use absolute positioning for the button
            this.add(backToMenu);
            this.revalidate();
            this.repaint();

        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (playerSnake.isAlive()) {
            repaint();
        }
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(playerSnake.getDirection() != 'R' && playerSnake.getY(0) != playerSnake.getY(1)) {
                        playerSnake.setDirection('L');
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(playerSnake.getDirection()  != 'L' && playerSnake.getY(0) != playerSnake.getY(1)) {
                        playerSnake.setDirection('R');
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(playerSnake.getDirection()  != 'D' && playerSnake.getX(0) != playerSnake.getX(1)) {
                        playerSnake.setDirection('U');
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(playerSnake.getDirection()  != 'U' && playerSnake.getX(0) != playerSnake.getX(1)) {
                        playerSnake.setDirection('D');
                    }
                    break;
            }
        }
    }
}