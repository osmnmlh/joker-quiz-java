package pt.uevora.joker.app;

import javafx.application.Application;
import pt.uevora.joker.app.ui.fx.JokerFxApp;

public class GuiMain {
    public static void main(String[] args) {
        if (!isJavaFxAvailable()) {
            System.out.println("JavaFX not available in this runtime. GUI cannot start. Use CLI.");
            System.exit(1);
            return;
        }

        try {
            Application.launch(JokerFxApp.class, args);
        } catch (RuntimeException | Error e) {
            System.out.println("JavaFX not available in this runtime. GUI cannot start. Use CLI.");
            System.exit(1);
        }
    }

    private static boolean isJavaFxAvailable() {
        try {
            Class.forName("javafx.application.Application", false, GuiMain.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            return false;
        }
    }
}
