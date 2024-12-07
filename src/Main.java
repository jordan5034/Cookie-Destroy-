
package games.CookieDestroy.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Main extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GameScreen gameScreen;

    public Main() {
        setTitle("Cookie Destroyer");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        initMenuPanel();
        initInstructionsPanel();
        initGamePanel();

        add(mainPanel);
        setVisible(true);
    }

    private void initMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JLabel title = new JLabel("Cookie Destroyer", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JButton startButton = new JButton("Start Game");
        JButton instructionsButton = new JButton("Instructions");
        JButton exitButton = new JButton("Exit");

        startButton.addActionListener(e -> cardLayout.show(mainPanel, "Game"));
        instructionsButton.addActionListener(e -> cardLayout.show(mainPanel, "Instructions"));
        exitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(title);
        menuPanel.add(startButton);
        menuPanel.add(instructionsButton);
        menuPanel.add(exitButton);

        mainPanel.add(menuPanel, "Menu");
    }

    private void initInstructionsPanel() {
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new BorderLayout());

        JTextArea instructionsText = new JTextArea(
                "Instructions:\n\n" +
                        "- Move the spaceship using your mouse.\n" +
                        "- Catch apples to score points.\n" +
                        "- Avoid missing apples or it's game over!\n" +
                        "- Press 'R' to restart after game over.\n\n" +
                        "Good luck!"
        );
        instructionsText.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionsText.setEditable(false);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        instructionsPanel.add(instructionsText, BorderLayout.CENTER);
        instructionsPanel.add(backButton, BorderLayout.SOUTH);

        mainPanel.add(instructionsPanel, "Instructions");
    }

    private void initGamePanel() {
        gameScreen = new GameScreen();
        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.add(gameScreen, BorderLayout.CENTER);

        mainPanel.add(gamePanel, "Game");
    }

    public static void main(String[] args) {
        new Main();
    }
}
