package pt.uevora.joker.app;

import java.awt.GraphicsEnvironment;

import pt.uevora.joker.app.ui.swing.JokerSwingApp;

public class GuiMain {
    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("GUI cannot start in headless environment. Use CLI.");
            System.exit(1);
            return;
        }

        JokerSwingApp.launch();
    }
}
