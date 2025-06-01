import javax.swing.*;

public class Images {
    public static ImageIcon getImageIcon(String path) {
        return new ImageIcon(Images.class.getResource(path));
    }
}
