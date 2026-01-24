package pt.uevora.joker.app.ui.swing;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FinalPanel extends JPanel {
    private final JLabel prizeLabel;
    private final JLabel jokersLabel;
    private final JLabel reasonLabel;
    private final JButton exitButton;
    private final JButton backButton;

    public FinalPanel(Runnable onExit) {
        super(new BorderLayout(8, 8));
        prizeLabel = new JLabel("Final prize: €0", JLabel.CENTER);
        jokersLabel = new JLabel("Final jokers: 0", JLabel.CENTER);
        reasonLabel = new JLabel("", JLabel.CENTER);
        exitButton = new JButton("Exit");
        exitButton.addActionListener(event -> {
            if (onExit != null) {
                onExit.run();
            }
        });
        backButton = new JButton("Back to Menu");

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(prizeLabel, BorderLayout.NORTH);
        centerPanel.add(jokersLabel, BorderLayout.CENTER);
        centerPanel.add(reasonLabel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        buttonPanel.add(exitButton);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setSummary(int prize, int jokers, String reason) {
        prizeLabel.setText("Final prize: €" + prize);
        jokersLabel.setText("Final jokers: " + jokers);
        reasonLabel.setText(reason == null ? "" : reason);
    }

    public void setOnBack(Runnable onBack) {
        for (java.awt.event.ActionListener listener : backButton.getActionListeners()) {
            backButton.removeActionListener(listener);
        }
        backButton.addActionListener(event -> {
            if (onBack != null) {
                onBack.run();
            }
        });
    }

    public void setOnExit(Runnable onExit) {
        for (java.awt.event.ActionListener listener : exitButton.getActionListeners()) {
            exitButton.removeActionListener(listener);
        }
        exitButton.addActionListener(event -> {
            if (onExit != null) {
                onExit.run();
            }
        });
    }
}
