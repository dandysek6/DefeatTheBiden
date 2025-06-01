import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Random;

public class GUI implements ActionListener {
    public int count = 0;
    public int multiplier = 1;
    public int health = 20;
    public int golds = 0;
    public int goldValue;
    private double critChance = 0.0;
    public int bossesDefeated = 0;
    public int currentStage = 1;
    public static final int BASE_HEALTH_START = 20;
    private static final int BASE_HEALTH_MULTIPLIER = 2;
    public int baseHealth = BASE_HEALTH_START;
    private int newPrice1 = 100;
    private int newPrice2 = 500;
    private boolean acEnableDisable = false;
    private AutoClicker autoClickerInstance = new AutoClicker(this);

    private JFrame frame;
    public JLabel label, hplabel;
    private JPanel panel, mainPanel, shopPanel, healthbar, statsPanel, supportHPpanel, redPanel, closeRight, shopLayout, critPan, m20Pan, m100Pan, washingtonPane, bossPanel;
    private JPanel spHalf, spHalf2, spHalf3, spHalf4, spHalf5;
    public JButton button, autoclicker, resetButton, gold, close, criticalStrike, firstMultiplier, secondMultiplier;

    public GUI() {
        frame = new JFrame();

        //HP label
        hplabel = new JLabel("Health: " + health + " (Stage " + currentStage + ")" + "  Bosses left: " + (10 - (bossesDefeated % 10)));
        hplabel.setForeground(Color.BLACK);
        hplabel.setHorizontalAlignment(SwingConstants.CENTER);
        hplabel.setBounds(0, 0, 502, 50);
        loadProgress();

        // BOSS
        button = new JButton(Images.getImageIcon("biden-blast.png"));
        button.setPressedIcon(Images.getImageIcon("biden-red.png"));
        button.addActionListener(this);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        bossPanel = new JPanel(new GridBagLayout());
        bossPanel.add(button, new GridBagConstraints());
        bossPanel.setOpaque(false);

        washingtonPane = new JPanel();
        washingtonPane.setLayout(new BorderLayout());
        washingtonPane.setOpaque(false);
        washingtonPane.add(bossPanel, BorderLayout.CENTER);

        // RESET BUTTON
        resetButton = new JButton("Reset progress");
        resetButton.setBackground(new Color(115, 147, 179));
        resetButton.setForeground(Color.BLACK);
        resetButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetButton.setFocusPainted(false);
        resetButton.setOpaque(true);
        resetButton.addActionListener(e -> {
            int odpoved = JOptionPane.showConfirmDialog(
                    null,
                    "Do you really want to reset the progress?",
                    "Reset progress",
                    JOptionPane.YES_NO_OPTION
            );
            if (odpoved == JOptionPane.YES_OPTION) {
                resetProgress();
                JOptionPane.showMessageDialog(
                        null,
                        "Progress reset successfully.",
                        "Complete",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        // AUTOCLICKER
        autoclicker = new JButton("Autoclicker: Deactivated");
        autoclicker.setEnabled(true);
        autoclicker.addActionListener(e -> toggleAutoclicker());
        autoclicker.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dedicnostButtonu(resetButton, autoclicker);

        // DAMAGE DEALT
        label = new JLabel("Current Damage:  " + multiplier);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.BLACK);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setOpaque(true);
        label.setBackground(new Color(115, 147, 179));

        //MONEY
        gold = new JButton("Gold: " + golds);
        gold.addActionListener(e -> openShop());
        gold.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dedicnostButtonu(resetButton, gold);

        JLayeredPane layeredpane = new JLayeredPane();
        layeredpane.setPreferredSize(new Dimension(50, 300));
        layeredpane.setBounds(0, 0, 502, 50);

        //red HP
        redPanel = new JPanel();
        redPanel.setBackground(new Color(255, 127, 127, 255));
        redPanel.setBounds(0, 0, 502, 50);

        layeredpane.add(redPanel, Integer.valueOf(0));
        layeredpane.add(hplabel, Integer.valueOf(1));

        //HEALTH BAR
        healthbar = new JPanel();
        healthbar.setLayout(null);
        healthbar.add(layeredpane);
        healthbar.setBackground(Color.white);
        healthbar.setPreferredSize(new Dimension(0, 50));
        healthbar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        healthbar.setBackground(Color.white);

        //PANEL BELOW BOSS
        supportHPpanel = new JPanel();
        supportHPpanel.setLayout(new BorderLayout());
        supportHPpanel.add(healthbar, BorderLayout.NORTH);
        supportHPpanel.setOpaque(false);
        supportHPpanel.setPreferredSize(new Dimension(0, 250));

        //STATS
        statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(4, 1, 0, 20));
        statsPanel.setOpaque(false);
        statsPanel.add(label);
        statsPanel.add(autoclicker);
        statsPanel.add(resetButton);
        statsPanel.add(gold);

        spHalf2 = new JPanel();
        spHalf2.setOpaque(false);

        spHalf = new JPanel();
        spHalf.setOpaque(false);
        spHalf.setLayout(new GridLayout(2, 1));
        spHalf.add(statsPanel);
        spHalf.add(spHalf2);

        //PANEL WITH BOSS
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(washingtonPane, BorderLayout.CENTER);
        panel.add(supportHPpanel, BorderLayout.SOUTH);
        panel.setBackground(new Color(80, 200, 120));
        panel.setOpaque(false);

        //KONTENT PANEL
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 2));
        mainPanel.add(panel);
        mainPanel.add(spHalf);
        mainPanel.setVisible(true);

        criticalStrike = new JButton("<html><div style='text-align: center;'>Crit chance<br>???gold</div></html>");
        criticalStrike.setBackground(new Color(115, 147, 179));
        criticalStrike.setForeground(Color.WHITE);
        criticalStrike.setBorderPainted(false);
        criticalStrike.setCursor(new Cursor(Cursor.HAND_CURSOR));
        criticalStrike.setFocusPainted(false);
        criticalStrike.setEnabled(false);
        criticalStrike.setBorder(BorderFactory.createLineBorder(new Color(80, 200, 120), 10));
        criticalStrike.setOpaque(true);

        firstMultiplier = new JButton("<html><div style='text-align: center;'>Damage x3<br>" + newPrice1 + "</div></html>");
        firstMultiplier.addActionListener(e -> buyfirstMultiplier());
        zdeditBoosty(criticalStrike, firstMultiplier);
        firstMultiplier.setEnabled(false);
        firstMultiplier.setOpaque(true);

        secondMultiplier = new JButton("<html><div style='text-align: center;'>Damage x4<br>"+ newPrice2 +"</div></html>");
        secondMultiplier.addActionListener(e -> buysecondMultiplier());
        zdeditBoosty(criticalStrike, secondMultiplier);
        secondMultiplier.setEnabled(false);
        secondMultiplier.setOpaque(true);

        close = new JButton("Close");
        close.setBackground(new Color(255, 127, 127));
        close.setForeground(Color.BLACK);
        close.setBorderPainted(false);
        close.setCursor(new Cursor(Cursor.HAND_CURSOR));
        close.addActionListener(e -> setClose());
        close.setFocusPainted(false);
        close.setOpaque(true);

        JLabel shopLabel = new JLabel("Shop");
        shopLabel.setForeground(Color.BLACK);
        shopLabel.setBorder(new EmptyBorder(0, 10, 0, 0));

        //closeRIGHT
        closeRight = new JPanel();
        closeRight.setLayout(new BorderLayout());
        closeRight.add(close, BorderLayout.EAST);
        closeRight.add(shopLabel, BorderLayout.WEST);
        closeRight.setBackground(Color.WHITE);

        critPan = new JPanel();
        critPan.setLayout(new BorderLayout());
        critPan.setBorder(new EmptyBorder(100, 10, 100, 10));
        critPan.add(criticalStrike, BorderLayout.CENTER);
        critPan.setOpaque(false);

        m20Pan = new JPanel();
        m20Pan.setLayout(new BorderLayout());
        m20Pan.setBorder(new EmptyBorder(100, 10, 100, 10));
        m20Pan.add(firstMultiplier, BorderLayout.CENTER);
        m20Pan.setOpaque(false);

        m100Pan = new JPanel();
        m100Pan.setLayout(new BorderLayout());
        m100Pan.setBorder(new EmptyBorder(100, 10, 100, 10));
        m100Pan.add(secondMultiplier, BorderLayout.CENTER);
        m100Pan.setOpaque(false);

        ImageIcon iconShop = Images.getImageIcon("shop-wall.jpg");
        VykreslitObrazek pozadiShop = new VykreslitObrazek(iconShop.getImage());

        //SHOP CONTENT LAYOUT
        pozadiShop.setLayout(new GridLayout(1, 3));
        pozadiShop.add(critPan);
        pozadiShop.add(m20Pan);
        pozadiShop.add(m100Pan);

        //SHOP
        shopPanel = new JPanel();
        shopPanel.setLayout(new BorderLayout());
        shopPanel.setBackground(Color.WHITE);
        shopPanel.setVisible(false);
        shopPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        shopPanel.setBounds(300, 100, 490, 500);
        shopPanel.add(closeRight, BorderLayout.NORTH);
        shopPanel.add(pozadiShop, BorderLayout.CENTER);

        ImageIcon icon = Images.getImageIcon("capitol2.jpg");
        VykreslitObrazek pozadi = new VykreslitObrazek(icon.getImage());

        //RAMECEK
        pozadi.setLayout(new BorderLayout());
        pozadi.add(mainPanel, BorderLayout.CENTER);
        pozadi.setBorder(new EmptyBorder(10, 10, 50, 10));
        pozadi.setBounds(0, 0, 1024, 768);
        mainPanel.setOpaque(false);

        JLayeredPane shopPane = new JLayeredPane();
        shopPane.setPreferredSize(new Dimension(1024, 768));
        shopPane.add(pozadi, Integer.valueOf(0));
        shopPane.add(shopPanel, Integer.valueOf(1));
        shopPane.setLayout(null);

        frame.setContentPane(shopPane);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Defeat the biden");
        frame.setVisible(true);
        frame.setBounds(0, 0, 1024, 768);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(Images.getImageIcon("biden-blast.png").getImage());
        checkMultiplierButton();
    }

    public int getGold() {
        Random rand = new Random();
        int baseGold = rand.nextInt(10) + 10; // 10-19

        double stageMultiplier = Math.pow(1.2, currentStage - 1); //1,2 umocneno na currentstage - 1
        goldValue = (int) (baseGold * stageMultiplier);
        return goldValue;
    }

    private void toggleAutoclicker() {
        if (autoClickerInstance.isRunning()) {
            autoClickerInstance.stop();
            autoclicker.setText("Autoclicker: Deactivated");
        } else {
            autoClickerInstance = new AutoClicker(this);
            autoClickerInstance.start();
            autoclicker.setText("Autoclicker: Activated");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        health -= multiplier;
        label.setText("Current damage: " + multiplier);
        hplabel.setText("Health: " + health + " (Stage " + currentStage + ")" + "  Bosses left: " + (10 - (bossesDefeated % 10)));

        healthBar();
        if (e.getSource() == autoclicker) {
            toggleAutoclicker();
        }

        if (health <= 0) {
            bossesDefeated++;
            golds += getGold();
            gold.setText("Gold: " + golds);

            if (bossesDefeated % 10 == 0) {
                currentStage++;
                baseHealth *= BASE_HEALTH_MULTIPLIER;
                health = baseHealth;
                hplabel.setText("Health: " + health + " (Stage " + currentStage + ")" + "  Bosses left: " + (10 - (bossesDefeated % 10)));
                healthBar();
            }
            checkMultiplierButton();
            health = baseHealth;
            hplabel.setText("Health: " + health + " (Stage " + currentStage + ")" + "  Bosses left: " + (10 - (bossesDefeated % 10)));
        }
        saveProgress();
    }

    private void buyfirstMultiplier() {
        if (golds >= newPrice1) {
            golds -= newPrice1;
            multiplier *= 3;
            newPrice1 *= 2;
            gold.setText("Gold: " + golds);
            firstMultiplier.setText("<html><div style='text-align: center;'>Damage x3<br>" + newPrice1 + "</div></html>");
            saveProgress();
            checkMultiplierButton();
        }
    }

    private void buysecondMultiplier() {
        if (golds >= newPrice2) {
            golds -= newPrice2;
            multiplier *= 4;
            newPrice2 *= 2;
            gold.setText("Gold: " + golds);
            secondMultiplier.setText("<html><div style='text-align: center;'>Damage x4<br>"+ newPrice2 +"</div></html>");
            saveProgress();
            checkMultiplierButton();
        }
    }

    public void healthBar() {
        int maxWidth = 502;
        int newWidth = (int) ((health / (double) baseHealth) * maxWidth);
        if (newWidth <= 0) newWidth = maxWidth;
        redPanel.setBounds(0, 0, newWidth, 50);
    }

    private void openShop() {
        shopPanel.setVisible(true);
        resetButton.setEnabled(false);
        gold.setEnabled(false);
    }

    private void setClose() {
        shopPanel.setVisible(false);
        resetButton.setEnabled(true);
        gold.setEnabled(true);
    }

    private void resetProgress() {
        multiplier = 1;
        golds = 0;
        bossesDefeated = 0;
        currentStage = 1;
        baseHealth = BASE_HEALTH_START;
        health = baseHealth;
        newPrice1 = 100;
        newPrice2 = 500;

        label.setText("Current damage: " + multiplier);
        gold.setText("Gold: " + golds);
        hplabel.setText("Health: " + health + " (Stage " + currentStage + ")" + "  Bosses left: " + (10 - (bossesDefeated % 10)));
        firstMultiplier.setText("<html><div style='text-align: center;'>Damage x3<br>" + newPrice1 + "</div></html>");
        secondMultiplier.setText("<html><div style='text-align: center;'>Damage x3<br>" + newPrice2 + "</div></html>");
        healthBar();

        firstMultiplier.setEnabled(false);
        secondMultiplier.setEnabled(false);
        checkMultiplierButton();
        saveProgress();
    }

    public void checkMultiplierButton() {
        if (firstMultiplier != null && secondMultiplier != null) {
            firstMultiplier.setEnabled(golds >= newPrice1);
            secondMultiplier.setEnabled(golds >= newPrice2);
        }
    }

    public void saveProgress() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("progress.txt"))) {
            writer.write("Multiplier: " + multiplier + "\n");
            writer.write("Golds: " + golds + "\n");
            writer.write("BossesDefeated: " + bossesDefeated + "\n");
            writer.write("CurrentStage: " + currentStage + "\n");
            writer.write("BaseHealth: " + baseHealth + "\n");
            writer.write("NewPrice1: " + newPrice1 + "\n");
            writer.write("NewPrice2: " + newPrice2 + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadProgress() {
        try (BufferedReader reader = new BufferedReader(new FileReader("progress.txt"))) {
            multiplier = Integer.parseInt(reader.readLine().split(": ")[1]);
            golds = Integer.parseInt(reader.readLine().split(": ")[1]);
            bossesDefeated = Integer.parseInt(reader.readLine().split(": ")[1]);
            currentStage = Integer.parseInt(reader.readLine().split(": ")[1]);
            baseHealth = Integer.parseInt(reader.readLine().split(": ")[1]);
            health = baseHealth;
            newPrice1 = Integer.parseInt(reader.readLine().split(": ")[1]);
            newPrice2 = Integer.parseInt(reader.readLine().split(": ")[1]);

            hplabel.setText("Health: " + health + " (Stage " + currentStage + ")" +
                    "  Bosses left: " + (10 - (bossesDefeated % 10)));

            checkMultiplierButton();
        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException ex) {
            baseHealth = 20;
            currentStage = 1;
            multiplier = 1;
            golds = 0;
            bossesDefeated = 0;
            health = baseHealth;
            newPrice1 = 100;
            newPrice2 = 500;

            hplabel.setText("Health: " + health + " (Stage " + currentStage + ")" +
                    "  Bosses left: 10");
            checkMultiplierButton();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }

    private static void dedicnostButtonu(JButton zdroj, JButton cil) {
        cil.setForeground(zdroj.getForeground());
        cil.setBackground(zdroj.getBackground());
        cil.setBorder(zdroj.getBorder());
        cil.setFocusPainted(false);
        cil.setOpaque(true);
    }

    private static void zdeditBoosty(JButton zdroj, JButton cil) {
        cil.setBackground(zdroj.getBackground());
        cil.setForeground(zdroj.getForeground());
        cil.setBorderPainted(false);
        cil.setFocusPainted(false);
        cil.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cil.setBorder(zdroj.getBorder());
        cil.setSize(zdroj.getSize());
    }
}