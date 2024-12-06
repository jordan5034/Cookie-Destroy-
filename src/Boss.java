import java.awt.*;
import javax.swing.*;

public class Boss {
    private int x, y;
    private final int width = 80;
    private final int height = 50;
    private final int speed = 2;
    private boolean movingRight = true;
    private Image bossImage;

    public Boss() {
        x = 0;
        y = 50; // Spawn at the top of the screen
        bossImage = new ImageIcon(getClass().getResource("boss.png")).getImage();
    }

    public void move() {
        if (movingRight) {
            x += speed;
            if (x + width >= 400) { // Assuming screen width is 400
                movingRight = false;
            }
        } else {
            x -= speed;
            if (x <= 0) {
                movingRight = true;
            }
        }
    }

    public void draw(Graphics2D g) {
        if (bossImage != null) {
            g.drawImage(bossImage, x, y, width, height, null);
        } else {
            g.setColor(Color.MAGENTA);
            g.fillRect(x, y, width, height);
        }
    }

    public boolean isHit(int basketX, int basketY, int basketWidth, int basketHeight) {
        return basketY < y + height && basketY + basketHeight > y &&
                basketX < x + width && basketX + basketWidth > x;
    }
}
