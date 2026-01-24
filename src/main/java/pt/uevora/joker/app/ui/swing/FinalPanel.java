package pt.uevora.joker.app.ui.swing;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FinalPanel extends JPanel {
    private final JLabel prizeLabel;
    private final JLabel jokersLabel;
    private final JButton exitButton;

    public FinalPanel(Runnable onExit) {
        super(new BorderLayout(8, 8));
        prizeLabel = new JLabel("Final prize: €0", JLabel.CENTER);
        jokersLabel = new JLabel("Final jokers: 0", JLabel.CENTER);
        exitButton = new JButton("Exit");
        exitButton.addActionListener(event -> {
            if (onExit != null) {
                onExit.run();
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(prizeLabel, BorderLayout.NORTH);
        centerPanel.add(jokersLabel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        add(exitButton, BorderLayout.SOUTH);
    }

    public void setSummary(int prize, int jokers) {
        prizeLabel.setText("Final prize: €" + prize);
        jokersLabel.setText("Final jokers: " + jokers);
    }
}
