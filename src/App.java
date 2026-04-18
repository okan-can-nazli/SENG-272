import javax.swing.*;
import view.MainFrame;

// main entry point, run this to start the app
// compile: find src -name "*.java" | xargs javac -d bin
// run: java -cp bin App
public class App {
    public static void main(String[] args) {
        // try to use the OS's native look, falls back to default if it fails
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}