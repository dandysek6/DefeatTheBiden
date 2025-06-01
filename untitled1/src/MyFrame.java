import javax.swing.*;
import java.awt.*;

class VykreslitObrazek extends JPanel {
    private Image background;

    public VykreslitObrazek(Image background) {
        this.background = background;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}
