import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameScreen extends JPanel implements ActionListener, MouseMotionListener, KeyListener {
    private Timer timer;
    private Basket basket;
    private ArrayList<Apple> apples;
    private ArrayList<Cookie> cookies;
    private Boss boss = null; // Boss instance
    private int score = 0;
    private boolean gameOver = false;
    private Image background;
    private int currentLevel = 1; // Start at level 1
    private int objectsPerLevel = 3; // Number of objects added per level


    public GameScreen() {
        basket = new Basket();
        apples = new ArrayList<>();
        cookies = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            apples.add(new Apple());
            cookies.add(new Cookie());
        }

        background = new ImageIcon(getClass().getResource("background.png")).getImage();

        timer = new Timer(50, this);
        timer.start();

        addMouseMotionListener(this);
        addKeyListener(this);

        setFocusable(true);
        requestFocusInWindow();
        setLayout(null);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (background != null) {
            g2d.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        } else {
            g2d.setColor(Color.CYAN);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        basket.draw(g2d);
        for (Apple apple : apples) {
            apple.draw(g2d);
        }
        for (Cookie cookie : cookies) {
            cookie.draw(g2d);
        }

        // Draw boss if spawned
        if (boss != null) {
            boss.draw(g2d);
        }

        g2d.setColor(Color.RED);
        g2d.drawString("Score: " + score, 10, 20);

        if (gameOver) {
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            g2d.drawString("Game Over!", 100, 200);
            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            g2d.drawString("Press 'R' to Restart", 110, 240);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            timer.stop();
            return;
        }

        // Check for level progression
        if (score >= currentLevel * 100) { // Example: 100 points per level
            currentLevel++;
            spawnObjectsRecursively(1, apples, cookies); // Add one more level's objects
            objectsPerLevel++; // Optional: Increase the number of objects added per level
        }

        // Existing logic for updating apples, cookies, and boss
        for (Apple apple : apples) {
            apple.fall();
            if (basket.catchApple(apple)) {
                score += 10;
                apple.resetPosition();
            }
            if (apple.getY() > getHeight()) {
                gameOver = true;
            }
        }

        for (Cookie cookie : cookies) {
            cookie.fall();
            if (basket.catchCookie(cookie)) {
                score += 5;
                cookie.resetPosition();
            }
            if (cookie.getY() > getHeight()) {
                gameOver = true;
            }
        }

        if (boss != null) {
            boss.move();
            if (boss.isHit(basket.getX(), basket.getY(), basket.getWidth(), basket.getHeight())) {
                gameOver = true;
            }
        }

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        basket.move(e.getX(), e.getY());
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver && e.getKeyCode() == KeyEvent.VK_R) {
            restartGame();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    private void restartGame() {
        basket = new Basket();
        apples.clear();
        cookies.clear();
        boss = null;
        currentLevel = 1; // Reset to level 1
        objectsPerLevel = 3; // Reset objects per level
        spawnObjectsRecursively(currentLevel, apples, cookies); // Spawn initial objects
        score = 0;
        gameOver = false;
        timer.start();
        repaint();
    }


    // Inner classes for Basket, Apple, Cookie
    class Basket {
        private int x = 180;
        private int y = 350;
        private int width = 50;
        private int height = 30;
        private Image spaceship;

        public Basket() {
            spaceship = new ImageIcon(getClass().getResource("spaceship.png")).getImage();
        }

        public void move(int mouseX, int mouseY) {
            // Adjust the basket position relative to its size
            x = mouseX - width / 2;
            y = mouseY - height / 2;

            // Ensure the basket stays within the screen bounds
            x = Math.max(0, Math.min(x, GameScreen.this.getWidth() - width));
            y = Math.max(0, Math.min(y, GameScreen.this.getHeight() - height));
        }


        public void draw(Graphics2D g) {
            if (spaceship != null) {
                g.drawImage(spaceship, x, y, width, height, null);
            } else {
                g.setColor(Color.BLUE);
                g.fillRect(x, y, width, height);
            }
        }

        public boolean catchApple(Apple apple) {
            return apple.getY() + apple.getSize() > y && apple.getY() < y + height &&
                    apple.getX() + apple.getSize() > x && apple.getX() < x + width;
        }

        public boolean catchCookie(Cookie cookie) {
            return cookie.getY() + cookie.getSize() > y && cookie.getY() < y + height &&
                    cookie.getX() + cookie.getSize() > x && cookie.getX() < x + width;
        }

        // Getters for Boss collision
        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    class Apple {
        private int x, y;
        private final int size = 20;
        private final int speed = 3;
        private Image appleImage;

        public Apple() {
            appleImage = new ImageIcon(getClass().getResource("Apple.png")).getImage();
            resetPosition();
        }

        public void resetPosition() {
            x = (int) (Math.random() * getWidth());
            y = 0;
        }

        public void fall() {
            y += speed + currentLevel;
        }

        public void draw(Graphics2D g) {
            if (appleImage != null) {
                g.drawImage(appleImage, x, y, size, size, null);
            } else {
                g.setColor(Color.RED);
                g.fillRect(x, y, size, size);
            }
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getSize() {
            return size;
        }
    }

    class Cookie {
        private int x, y;
        private final int size = 20;
        private final int speed = 2;
        private Image cookieImage;

        public Cookie() {
            resetPosition();
            cookieImage = new ImageIcon(getClass().getResource("cookie.png")).getImage();
        }

        public void resetPosition() {
            x = (int) (Math.random() * getWidth());
            y = 0;
        }

        public void fall() {
            y += speed;
        }

        public void draw(Graphics2D g) {
            if (cookieImage != null) {
                g.drawImage(cookieImage, x, y, size, size, null);
            } else {
                g.setColor(Color.ORANGE);
                g.fillRect(x, y, size, size);
            }
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getSize() {
            return size;
        }
    }

    private void spawnObjectsRecursively(int level, ArrayList<Apple> apples, ArrayList<Cookie> cookies) {
        if (level <= 0) {
            return; // Base case: Stop recursion when no levels are left
        }

        // Spawn objects for the current level
        for (int i = 0; i < objectsPerLevel; i++) {
            apples.add(new Apple());
            cookies.add(new Cookie());
        }

        // Recursive call for the next level
        spawnObjectsRecursively(level - 1, apples, cookies);
    }

}
