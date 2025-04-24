import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {

    private final int TILE_SIZE = 20;
    private final int WIDTH = 600;
    private final int HEIGHT = 400;
    private final int NUM_TILES_X = WIDTH / TILE_SIZE;
    private final int NUM_TILES_Y = HEIGHT / TILE_SIZE;

    private LinkedList<Point> snake;
    private Point food;
    private boolean gameOver;
    private int direction; // 0: up, 1: right, 2: down, 3: left
    private Timer timer;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (!gameOver) {
                    if (key == KeyEvent.VK_UP && direction != 2) {
                        direction = 0;
                    } else if (key == KeyEvent.VK_RIGHT && direction != 3) {
                        direction = 1;
                    } else if (key == KeyEvent.VK_DOWN && direction != 0) {
                        direction = 2;
                    } else if (key == KeyEvent.VK_LEFT && direction != 1) {
                        direction = 3;
                    }
                } else {
                    if (key == KeyEvent.VK_R) {
                        restartGame();
                    }
                }
            }
        });

        timer = new Timer(100, this);
        restartGame();
    }

    private void restartGame() {
        snake = new LinkedList<>();
        snake.add(new Point(5, 5));
        direction = 1;
        spawnFood();
        gameOver = false;
        timer.start();
    }

    private void spawnFood() {
        Random random = new Random();
        food = new Point(random.nextInt(NUM_TILES_X), random.nextInt(NUM_TILES_Y));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            moveSnake();
            checkCollisions();
            repaint();
        }
    }

    private void moveSnake() {
        Point head = snake.getFirst();
        Point newHead = null;

        switch (direction) {
            case 0: // Up
                newHead = new Point(head.x, head.y - 1);
                break;
            case 1: // Right
                newHead = new Point(head.x + 1, head.y);
                break;
            case 2: // Down
                newHead = new Point(head.x, head.y + 1);
                break;
            case 3: // Left
                newHead = new Point(head.x - 1, head.y);
                break;
        }

        if (newHead.equals(food)) {
            snake.addFirst(newHead);
            spawnFood();
        } else {
            snake.addFirst(newHead);
            snake.removeLast();
        }
    }

    private void checkCollisions() {
        Point head = snake.getFirst();
        if (head.x < 0 || head.x >= NUM_TILES_X || head.y < 0 || head.y >= NUM_TILES_Y) {
            gameOver = true;
        }

        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
                break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawSnake(g);
        drawFood(g);

        if (gameOver) {
            drawGameOver(g);
        }
    }

    private void drawSnake(Graphics g) {
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }

    private void drawFood(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private void drawGameOver(Graphics g) {
        String message = "Game Over! Press 'R' to Restart.";
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics metrics = g.getFontMetrics();
        g.drawString(message, (WIDTH - metrics.stringWidth(message)) / 2, HEIGHT / 2);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}