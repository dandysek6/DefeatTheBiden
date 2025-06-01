import java.util.Timer;
import java.util.TimerTask;

public class AutoClicker {
    private final GUI gui;
    private final Timer timer;
    private boolean running;

    public AutoClicker(GUI gui) {
        this.gui = gui;
        this.timer = new Timer();
        this.running = false;
    }

    public void start() {
        if (running) return;
        running = true;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                gui.health -= gui.multiplier;
                gui.label.setText("Current damage: " + gui.multiplier);
                gui.hplabel.setText("Health: " + gui.health + " (Stage " + gui.currentStage + ")" +
                        "  Bosses left: " + (10 - (gui.bossesDefeated % 10)));

                gui.healthBar();

                if (gui.health <= 0) {
                    gui.bossesDefeated++;
                    gui.golds += gui.getGold();
                    gui.gold.setText("Gold: " + gui.golds);

                    if (gui.bossesDefeated % 10 == 0) {
                        gui.currentStage++;
                        gui.baseHealth *= 2;
                        gui.health = gui.baseHealth;
                        gui.hplabel.setText("Health: " + gui.health + " (Stage " + gui.currentStage + ")" +
                                "  Bosses left: " + (10 - (gui.bossesDefeated % 10)));
                    }
                    gui.checkMultiplierButton();
                    gui.health = gui.baseHealth;
                    gui.hplabel.setText("Health: " + gui.health + " (Stage " + gui.currentStage + ")" +
                            "  Bosses left: " + (10 - (gui.bossesDefeated % 10)));
                }
                gui.saveProgress();
            }
        }, 0, 125);
    }

    public void stop() {
        running = false;
        timer.cancel();
        timer.purge();
    }

    public boolean isRunning() {
        return running;
    }
}