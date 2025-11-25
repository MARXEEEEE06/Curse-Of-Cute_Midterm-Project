import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Simple Pokémon-like battle demo in Java Swing.
 * - Uses your uploaded assets located under /mnt/data/*.png
 * - Animates idle frames for player (Felis) and enemy (Browney)
 * - Simple turn: Player "Attack" -> Enemy takes damage -> Enemy retaliates
 *
 * To compile and run:
 *   javac Combat.java
 *   java Combat
 *
 * Make sure the image files exist at the exact paths used below.
 */
public class Combat extends JPanel implements ActionListener {
    // Asset paths (these point to the uploaded files in the container)
    private static final String BG_FOLDER = 
    "res/Entities/Combat/background for battle/";

    private static final String[] BG_PATHS = {
        BG_FOLDER + "BACKGROUND1.png",
        BG_FOLDER + "BACKGROUND2.png",
        BG_FOLDER + "BACKGROUND3.png",
        BG_FOLDER + "BACKGROUND4.png",
        BG_FOLDER + "BACKGROUND5.png",
        BG_FOLDER + "BACKGROUND6.png",
        BG_FOLDER + "BACKGROUND7.png",
        BG_FOLDER + "BACKGROUND8.png"
    };

    private static final String FELIS_FOLDER = 
    "res/Entities/felis idle/";

    private static final String[] FELIS_FRAMES = {
        FELIS_FOLDER + "Felis_idle1.png",
        FELIS_FOLDER + "Felis_idle2.png",
        FELIS_FOLDER + "Felis_idle3.png",
        FELIS_FOLDER + "Felis_idle4.png"
    };


    private static final String BROWNEY_FOLDER = 
    "res/Entities/Combat/brown idle/";

    private static final String[] BROWNEY_FRAMES = {
        BROWNEY_FOLDER + "Browney_idle1.png",
        BROWNEY_FOLDER + "Browney_idle2.png",
        BROWNEY_FOLDER + "Browney_idle3.png",
        BROWNEY_FOLDER + "Browney_idle4.png"
    };

    private BufferedImage background;
    private BufferedImage[] felisFrames;
    private BufferedImage[] browneyFrames;
    
    // Skill 1 animation
    private BufferedImage[] skill1Frames; // array holding all frames of Skill 1
    private int skill1Index = 0;          // current frame
    private boolean skill1Active = false; // whether animation is playing
    private Timer skill1Timer;    
    private BufferedImage[] skill2Frames; // array holding all frames of Skill 1
    private int skill2Index = 0;          // current frame
    private boolean skill2Active = false; // whether animation is playing
    private Timer skill2Timer;         // cycles through frames
    private BufferedImage[] skill3Frames; // array holding all frames of Skill 1
    private int skill3Index = 0;          // current frame
    private boolean skill3Active = false; // whether animation is playing
    private Timer skill3Timer; 

    // Browney Skill 1
    private BufferedImage[] browneySkill1Frames;
    private int browneySkill1Index = 0;
    private boolean browneySkill1Active = false;
    private Timer browneySkill1Timer;


    private int felisIndex = 0;
    private int browneyIndex = 0;
    private int whatFrame = 0;

    // Simple stats
    private int playerHp = 100;
    private int enemyHp = 100;
    private boolean enemyDefeated = false;


    // UI
    private Timer animationTimer;
    private Timer enemyActionTimer;
    private JButton skill1Btn;
    private JButton skill2Btn;
    private JButton skill3Btn;
    private JLabel statusLabel;
    private JComboBox<String> bgSelect;
    
    // GamePanel reference for integration
    private GamePanel gamePanel;
    private boolean combatActive = false;

    public Combat(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setPreferredSize(new Dimension(800, 600));
        setLayout(null);

        loadAssets(0); // load default background

        // Animation timer (sprite frame updates)
        animationTimer = new Timer(150, e -> {
            felisIndex = (felisIndex + 1) % felisFrames.length;
            browneyIndex = (browneyIndex + 1) % browneyFrames.length;
            repaint();
        });
        // Don't start animation timer here - only start when combat begins
        // animationTimer.start();

        int panelWidth = 800;
        int btnWidth = 100;
        int btnHeight = 30;
        int rightMargin = 60;  // distance from right edge
        int startY = 500;

        

        // Skill buttons
        skill1Btn = new JButton("Skill 1");
        skill1Btn.setBounds(panelWidth - btnWidth - rightMargin - 280, startY, btnWidth, btnHeight);
        skill1Btn.setBackground(new Color(0, 0, 70));
        skill1Btn.setForeground(Color.YELLOW);
        skill1Btn.setFont(new Font("Bradley Hand ITC", Font.BOLD, 18));
        skill1Btn.setFocusPainted(false);
        skill1Btn.setVisible(false);
        skill1Btn.addActionListener(e -> {
        if (!skill1Active) {
            skill1Active = true;   // mark animation as playing
            skill1Index = 0;       // start from first frame
            if (skill1Timer != null) skill1Timer.start();   // start cycling frames
            playerSkill(1, 15, 5); // call damage logic
        }
        });
        add(skill1Btn);

        skill2Btn = new JButton("Skill 2");
        skill2Btn.setBounds(panelWidth - btnWidth - rightMargin - 150, startY, btnWidth, btnHeight);
        skill2Btn.setBackground(new Color(0, 0, 70));
        skill2Btn.setForeground(Color.YELLOW);
        skill2Btn.setFont(new Font("Bradley Hand ITC", Font.BOLD, 18));
        skill2Btn.setFocusPainted(false);
        skill2Btn.setVisible(false);
        skill2Btn.addActionListener(e -> {
        if (!skill2Active) {
            skill2Active = true;   // mark animation as playing
            skill2Index = 0;       // start from first frame
            if (skill2Timer != null) skill2Timer.start();   // start cycling frames
            playerSkill(2, 8, 5); // call damage logic
        }
        });
        add(skill2Btn);


        skill3Btn = new JButton("Skill 3");
        skill3Btn.setBounds(panelWidth - btnWidth - rightMargin - 20, startY, btnWidth, btnHeight);
        skill3Btn.setBackground(new Color(0, 0, 70));
        skill3Btn.setForeground(Color.YELLOW);
        skill3Btn.setFont(new Font("Bradley Hand ITC", Font.BOLD, 18));
        skill3Btn.setFocusPainted(false);
        skill3Btn.setVisible(false);
        skill3Btn.addActionListener(e -> {
        if (!skill3Active) {
            skill3Active = true;   // mark animation as playing
            skill3Index = 0;       // start from first frame
            if (skill3Timer != null) skill3Timer.start();   // start cycling frames
            playerSkill(3, 22, 5); // call damage logic
        }
        });
        add(skill3Btn);

        // Status label
        statusLabel = new JLabel("Choose your action...");
        statusLabel.setForeground(Color.YELLOW);
        statusLabel.setFont(new Font("Bradley Hand ITC", Font.BOLD, 16));
        statusLabel.setVisible(false);
        add(statusLabel);

        // Background selector
        bgSelect = new JComboBox<>();
        for (int i = 0; i < BG_PATHS.length; i++) bgSelect.addItem("BACKGROUND" + (i+1));
        bgSelect.setBounds(20, 20, 140, 30);
        bgSelect.addActionListener(e -> loadAssets(bgSelect.getSelectedIndex()));
        bgSelect.setVisible(false);
        add(bgSelect);

        // Enemy action timer placeholder
        enemyActionTimer = new Timer(800, null);
    }

    // Unified skill method
    private void playerSkill(int skillNum, int baseDamage, int randomRange) {
        if (enemyHp <= 0 || playerHp <= 0) return;

        int dmg = baseDamage + (int)(Math.random() * randomRange);
        enemyHp = Math.max(0, enemyHp - dmg);
        statusLabel.setText("Skill " + skillNum + " hits the enemy for " + dmg + " damage!");
        repaint();

        if (enemyHp == 0) {
        enemyHp = 0;
        enemyDefeated = true;          // mark enemy defeated
        statusLabel.setText("Enemy defeated!");
        skill1Btn.setEnabled(false);   // disable skill buttons
        skill2Btn.setEnabled(false);
        skill3Btn.setEnabled(false);
        repaint();
        return;
    }
        // Disable skill buttons during enemy attack
        skill1Btn.setEnabled(false);
        skill2Btn.setEnabled(false);
        skill3Btn.setEnabled(false);

        // Enemy retaliation
        enemyActionTimer.stop();
        enemyActionTimer = new Timer(700, ae -> {
            enemyAttack();
            skill1Btn.setEnabled(true);
            skill2Btn.setEnabled(true);
            skill3Btn.setEnabled(true);
        });
        enemyActionTimer.setRepeats(false);
        enemyActionTimer.start();
    }

    public void enemyAttack() {

    // Trigger Browney skill animation
    browneySkill1Active = true;
    browneySkill1Index = 0;
    if (browneySkill1Timer != null) browneySkill1Timer.start();

    int preDamage = 5;
    int randomNum = 5;

    // Now apply enemy damage normally
    int damage = preDamage + (int)(Math.random() * randomNum); // whatever your attack is
    playerHp -= damage;
    if (playerHp < 0) playerHp = 0;

    statusLabel.setText("Browney used Skill 1! -" + damage + " HP");

}


    private void loadAssets(int bgIndex) {
        try {
            background = ImageIO.read(new File(BG_PATHS[bgIndex]));
        } catch (IOException ex) {
            background = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = background.createGraphics();
            g.setColor(new Color(100, 200, 255));
            g.fillRect(0, 0, 800, 600);
            g.setColor(new Color(100, 200, 100));
            g.fillRect(0, 300, 800, 300);
            g.dispose();
        }

        skill1Frames = new BufferedImage[8]; // 8 PNGs for skill 1
        for (int i = 0; i < 8; i++) {
            try {
                String path = "res/Entities/Combat/felis skill 1/S1A" + (i+1) + ".png";
                skill1Frames[i] = ImageIO.read(new File(path));
            } catch (IOException ex) {
                ex.printStackTrace();
                skill1Frames[i] = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB); // fallback
            }
        }

        // Timer to cycle through frames
        skill1Timer = new Timer(100, e -> { // 100ms per frame
            skill1Index++;
            if (skill1Index >= skill1Frames.length) {
                skill1Index = 0;
                skill1Active = false; // stop animation when last frame is reached
                skill1Timer.stop();
                enemyAttack();
            }
            repaint(); // redraw panel with updated frame
        });

        skill2Frames = new BufferedImage[8]; // 8 PNGs for skill 2
        for (int i = 0; i < 8; i++) {
            try {
                String path = "res/Entities/Combat/felis skill 2/S2A" + (i+1) + ".png";
                skill2Frames[i] = ImageIO.read(new File(path));
            } catch (IOException ex) {
                ex.printStackTrace();
                skill2Frames[i] = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB); // fallback
            }
        }

        // Timer to cycle through frames
        skill2Timer = new Timer(100, e -> { // 100ms per frame
            skill2Index++;
            if (skill2Index >= skill2Frames.length) {
                skill2Index = 0;
                skill2Active = false; // stop animation when last frame is reached
                skill2Timer.stop();
                enemyAttack();
            }
            repaint(); // redraw panel with updated frame
        });

        skill3Frames = new BufferedImage[12]; // 8 PNGs for skill 2
        for (int i = 0; i < 12; i++) {
            try {
                String path = "res/Entities/Combat/felis skill 3/S3A" + (i+1) + ".png";
                skill3Frames[i] = ImageIO.read(new File(path));
            } catch (IOException ex) {
                ex.printStackTrace();
                skill3Frames[i] = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB); // fallback
            }
        }

        // Timer to cycle through frames
        skill3Timer = new Timer(100, e -> { // 100ms per frame
            skill3Index++;
            if (skill3Index >= skill3Frames.length) {
                skill3Index = 0;
                skill3Active = false; // stop animation when last frame is reached
                skill3Timer.stop();
                enemyAttack();
            }

            repaint(); // redraw panel with updated frame
        });

        
        // Load Browney Skill 1 (10 frames)
        browneySkill1Frames = new BufferedImage[10]; // 8 PNGs for skill 2
        for (int i = 0; i < 10; i++) {
            try {
                String path = "res/Entities/Combat/brown skill 1/Browney_skill" + (i+1) + ".png";
                browneySkill1Frames[i] = ImageIO.read(new File(path));
            } catch (IOException ex) {
                ex.printStackTrace();
                browneySkill1Frames[i] = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB); // fallback
            }
        }
        browneySkill1Timer = new Timer(80, e -> {
        browneySkill1Index++;

        if (browneySkill1Index >= browneySkill1Frames.length) {
            browneySkill1Timer.stop();
            browneySkill1Active = false;
            browneySkill1Index = 0;
        }

        repaint();
        });


        
        // Load Felis frames
        felisFrames = new BufferedImage[FELIS_FRAMES.length];
        for (int i = 0; i < FELIS_FRAMES.length; i++) {
            try {
                felisFrames[i] = ImageIO.read(new File(FELIS_FRAMES[i]));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // Load Browney frames
        browneyFrames = new BufferedImage[BROWNEY_FRAMES.length];
        for (int i = 0; i < BROWNEY_FRAMES.length; i++) {
            try {
                browneyFrames[i] = ImageIO.read(new File(BROWNEY_FRAMES[i]));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        repaint();
        
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Combat rendering is now handled by the draw() method
        // which is called from GamePanel.paintComponent()
    }

    private void drawHpBar(Graphics2D g, String name, int hp, int x, int y) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, 200, 20);
        g.setColor(Color.GREEN);
        int w = (int)(200 * (hp / 100.0));
        g.fillRect(x, y, w, 20);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, 200, 20);
        g.drawString(name + " HP: " + hp + "/100", x - 8, y - 12);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // not used
    }

    // ============ GAMEPANEL INTEGRATION METHODS ============
    
    /**
     * Returns whether combat is currently active
     */
    public boolean isActive() {
        return combatActive;
    }

    /**
     * Starts combat and initializes the battle
     */
    public void startCombat() {
        combatActive = true;
        playerHp = 100;
        enemyHp = 100;
        enemyDefeated = false;
        whatFrame = 0;
        statusLabel.setText("Battle Started!");
        statusLabel.setVisible(true);
        skill1Btn.setVisible(true);
        skill1Btn.setEnabled(true);
        skill2Btn.setVisible(true);
        skill2Btn.setEnabled(true);
        skill3Btn.setVisible(true);
        skill3Btn.setEnabled(true);
        bgSelect.setVisible(true);
        // Start animation timer when combat begins
        if (animationTimer != null && !animationTimer.isRunning()) {
            animationTimer.start();
        }
        repaint();
    }

    /**
     * Ends combat and returns to gameplay
     */
    public void endCombat() {
        combatActive = false;
        statusLabel.setVisible(false);
        skill1Btn.setVisible(false);
        skill2Btn.setVisible(false);
        skill3Btn.setVisible(false);
        bgSelect.setVisible(false);
        // Stop animation timer when combat ends
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        if (gamePanel != null) {
            gamePanel.repaint();
        }
    }

    /**
     * Draw the combat UI on the provided Graphics context
     */
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Draw background scaled to panel
        if (background != null) {
            g2.drawImage(background, 0, 0, 800, 600, null);
        }

        // Draw the block behind status text
        g2.setColor(new Color(0, 0, 70, 200)); // semi-transparent dark blue
        int labelX = 300;
        int labelY = 400;
        int labelWidth = 480;
        int labelHeight = 80;
        g2.fillRoundRect(labelX, labelY, labelWidth, labelHeight, 10, 10);

        // Draw dynamic status text
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Bradley Hand ITC", Font.BOLD, 24));
        String text = statusLabel.getText();
        g2.drawString(text, labelX + 10, labelY + 28);

        // Draw brown oval platform
        int platformWidth = 280;
        int platformHeight = 70;
        int platformX = 60;
        int platformY = 480;
        g2.setColor(new Color(135, 103, 51));
        g2.fillOval(platformX, platformY, platformWidth, platformHeight);

        // Draw player idle ONLY if no skill is active
        if (!skill1Active && !skill2Active && !skill3Active) {
            if (felisFrames != null && felisFrames.length > 0) {
                BufferedImage pImg = felisFrames[felisIndex];
                int pX = 20;
                int pY = 600 - pImg.getHeight() - 20;
                g2.drawImage(pImg, pX, pY, null);
            }
        }

        // Draw skill animations on top if active
        if (skill1Active && skill1Frames != null) {
            BufferedImage frame = skill1Frames[skill1Index];
            g2.drawImage(frame, 0, 0, 800, 600, null);
        } 

        if (skill2Active && skill2Frames != null) {
            BufferedImage frame = skill2Frames[skill2Index];
            double scale1 = 0.28;
            double scale2 = 0.40;
            int w = (int)(800 * scale1);
            int h = (int)(600 * scale2);
            g2.drawImage(frame, 130, 300, w, h, null);
        }

        if (skill3Active && skill3Frames != null) {
            BufferedImage frame = skill3Frames[skill3Index];
            g2.drawImage(frame, 0, 0, 800, 600, null);
        }

        if (browneySkill1Active && browneySkill1Frames != null) {
            BufferedImage bf = browneySkill1Frames[browneySkill1Index];
            g2.drawImage(bf, 0, 0, 800, 600, null);
        }

        // Draw basic HP bars
        drawHpBar(g2, "Player", playerHp, 110, 570);
        drawHpBar(g2, "Enemy", enemyHp, 490, 310);

        // Draw enemy (Browney) in upper-right corner
        if (!enemyDefeated && browneyFrames != null && browneyFrames.length > 0) {
            BufferedImage eImg = browneyFrames[browneyIndex];
            int eW = eImg.getWidth();
            int eH = eImg.getHeight();
            int eX = 800 - eW - 3;
            int eY = 1;
            g2.drawImage(eImg, eX, eY, null);
        }

        // Draw skill buttons
        drawButton(g2, skill1Btn);
        drawButton(g2, skill2Btn);
        drawButton(g2, skill3Btn);
    }

    /**
     * Draw a button on the graphics context
     */
    private void drawButton(Graphics2D g2, JButton btn) {
        Rectangle bounds = btn.getBounds();
        
        // Draw button background
        g2.setColor(btn.getBackground());
        g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        
        // Draw button border
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        
        // Draw button text
        g2.setColor(btn.getForeground());
        g2.setFont(btn.getFont());
        FontMetrics fm = g2.getFontMetrics();
        String text = btn.getText();
        int textX = bounds.x + (bounds.width - fm.stringWidth(text)) / 2;
        int textY = bounds.y + ((bounds.height - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(text, textX, textY);
    }

    /**
     * Handle mouse clicks in the combat UI
     */
    public void onMouseClicked(int x, int y) {
        // Check if any buttons were clicked
        if (skill1Btn.getBounds().contains(x, y)) {
            skill1Btn.doClick();
        } else if (skill2Btn.getBounds().contains(x, y)) {
            skill2Btn.doClick();
        } else if (skill3Btn.getBounds().contains(x, y)) {
            skill3Btn.doClick();
        }
    }

    /**
     * Update cursor based on hover state
     */
    public void updateCursorOnHover(int x, int y) {
        // Change cursor to hand if hovering over a button
        if (skill1Btn.getBounds().contains(x, y) ||
            skill2Btn.getBounds().contains(x, y) ||
            skill3Btn.getBounds().contains(x, y)) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Handle skill button press from keyboard (1, 2, 3)
     */
    public void onSkillPressed(int skillNumber) {
        if (!combatActive) return;
        
        switch (skillNumber) {
            case 1:
                skill1Btn.doClick();
                break;
            case 2:
                skill2Btn.doClick();
                break;
            case 3:
                skill3Btn.doClick();
                break;
            default:
                break;
        }
    }
}
