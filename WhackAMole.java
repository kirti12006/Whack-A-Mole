import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class WhackAMole extends JFrame implements ActionListener {

    RoundButton[] buttons = new RoundButton[9];
    JLabel scoreLabel, timeLabel;
    JButton startButton;

    int score = 0;
    int moleIndex = -1;
    int timeLeft = 30;

    Random random = new Random();
    Timer moleTimer, gameTimer;

    public WhackAMole() {
        setTitle("Whack A Mole");
        setSize(450, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top panel
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        scoreLabel = new JLabel("Score: 0", JLabel.CENTER);
        timeLabel = new JLabel("Time: 30", JLabel.CENTER);

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        topPanel.add(scoreLabel);
        topPanel.add(timeLabel);
        add(topPanel, BorderLayout.NORTH);

        // Game grid
        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 18, 18));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        gridPanel.setBackground(new Color(60, 140, 60)); // grass
        add(gridPanel, BorderLayout.CENTER);

        for (int i = 0; i < 9; i++) {
            buttons[i] = new RoundButton();
            buttons[i].setBackground(new Color(200, 230, 255)); // empty hole
            buttons[i].setFont(new Font("Arial", Font.BOLD, 32));
            buttons[i].addActionListener(this);
            buttons[i].setEnabled(false);
            gridPanel.add(buttons[i]);
        }

        // Start button
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.addActionListener(e -> startGame());
        add(startButton, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    void startGame() {
        score = 0;
        timeLeft = 30;
        scoreLabel.setText("Score: 0");
        timeLabel.setText("Time: 30");
        startButton.setEnabled(false);

        for (RoundButton btn : buttons) {
            btn.setText("");
            btn.setBackground(new Color(200, 230, 255));
            btn.setEnabled(true);
        }

        moleTimer = new Timer(800, e -> showMole());
        gameTimer = new Timer(1000, e -> updateTime());

        moleTimer.start();
        gameTimer.start();
    }

    void showMole() {
        // Clear old mole
        if (moleIndex != -1) {
            buttons[moleIndex].setText("");
            buttons[moleIndex].setBackground(new Color(200, 230, 255));
        }

        moleIndex = random.nextInt(9);

        // Show new mole (BIG CIRCLE)
        buttons[moleIndex].setText("●");
        buttons[moleIndex].setForeground(Color.WHITE);
        buttons[moleIndex].setBackground(new Color(139, 69, 19)); // brown mole
    }

    void updateTime() {
        timeLeft--;
        timeLabel.setText("Time: " + timeLeft);

        if (timeLeft == 0) {
            endGame();
        }
    }

    void endGame() {
        moleTimer.stop();
        gameTimer.stop();

        for (RoundButton btn : buttons) {
            btn.setText("");
            btn.setEnabled(false);
        }

        startButton.setEnabled(true);

        JOptionPane.showMessageDialog(
                this,
                "Game Over!\nYour Score: " + score,
                "Whack A Mole",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        RoundButton clicked = (RoundButton) e.getSource();

        if (clicked.getText().equals("●")) {
            score++;
            scoreLabel.setText("Score: " + score);

            clicked.setText("");
            clicked.setBackground(new Color(200, 230, 255));
        }
    }

    public static void main(String[] args) {
        new WhackAMole();
    }
}

/* -------- Round Button (Hole Shape) -------- */

class RoundButton extends JButton {

    public RoundButton() {
        setFocusPainted(false);
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillOval(0, 0, getWidth(), getHeight());

        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
    }

    @Override
    public boolean contains(int x, int y) {
        int r = getWidth() / 2;
        return Math.pow(x - r, 2) + Math.pow(y - r, 2) <= Math.pow(r, 2);
    }
}
