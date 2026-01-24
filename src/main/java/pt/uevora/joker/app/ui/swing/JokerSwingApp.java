package pt.uevora.joker.app.ui.swing;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public final class JokerSwingApp {
    private JokerSwingApp() {
    }

    public static void launch() {
        SwingUtilities.invokeLater(JokerSwingApp::createAndShow);
    }

    private static void createAndShow() {
        JFrame frame = new JFrame("Jogo do Joker — GUI (Swing)");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(0, 8));
        content.add(new JLabel("GUI Mode (Work in Progress)", SwingConstants.CENTER), BorderLayout.NORTH);
        content.add(new JLabel("Swing OK", SwingConstants.CENTER), BorderLayout.CENTER);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(event -> frame.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exitButton);
        content.add(buttonPanel, BorderLayout.SOUTH);

        Container root = frame.getContentPane();
        root.add(content, BorderLayout.CENTER);

        frame.setSize(420, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
