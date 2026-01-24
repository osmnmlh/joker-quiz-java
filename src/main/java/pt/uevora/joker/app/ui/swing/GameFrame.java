package pt.uevora.joker.app.ui.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class GameFrame extends JFrame {
    private final JLabel roundLabel;
    private final JLabel moneyLabel;
    private final JLabel jokersLabel;
    private final JTextArea messageArea;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final NormalQuestionPanel normalPanel;
    private final BonusPanel bonusPanel;
    private final FinalPanel finalPanel;
    private Runnable onClose;

    public GameFrame() {
        super("Jogo do Joker — GUI (Swing)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        roundLabel = new JLabel("Round: -");
        moneyLabel = new JLabel("Money: €0");
        jokersLabel = new JLabel("Jokers: 0");

        JPanel statusPanel = new JPanel();
        statusPanel.add(roundLabel);
        statusPanel.add(new JLabel("|", SwingConstants.CENTER));
        statusPanel.add(moneyLabel);
        statusPanel.add(new JLabel("|", SwingConstants.CENTER));
        statusPanel.add(jokersLabel);

        messageArea = new JTextArea(5, 40);
        messageArea.setEditable(false);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        normalPanel = new NormalQuestionPanel();
        bonusPanel = new BonusPanel();
        finalPanel = new FinalPanel(this::dispose);
        cardPanel.add(normalPanel, "normal");
        cardPanel.add(bonusPanel, "bonus");
        cardPanel.add(finalPanel, "final");

        Container root = getContentPane();
        root.setLayout(new BorderLayout(8, 8));
        root.add(statusPanel, BorderLayout.NORTH);
        root.add(cardPanel, BorderLayout.CENTER);
        root.add(new JScrollPane(messageArea), BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                if (onClose != null) {
                    onClose.run();
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    public void updateStatus(int roundNumber, int money, int jokers) {
        SwingUtilities.invokeLater(() -> {
            roundLabel.setText("Round: " + roundNumber);
            moneyLabel.setText("Money: €" + money);
            jokersLabel.setText("Jokers: " + jokers);
        });
    }

    public void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            messageArea.append(message + System.lineSeparator());
            messageArea.setCaretPosition(messageArea.getDocument().getLength());
        });
    }

    public void showNormalCard() {
        cardLayout.show(cardPanel, "normal");
    }

    public void showBonusCard() {
        cardLayout.show(cardPanel, "bonus");
    }

    public void showFinalCard() {
        cardLayout.show(cardPanel, "final");
    }

    public NormalQuestionPanel getNormalPanel() {
        return normalPanel;
    }

    public BonusPanel getBonusPanel() {
        return bonusPanel;
    }

    public FinalPanel getFinalPanel() {
        return finalPanel;
    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }

    public void setFinalActions(Runnable onExit, Runnable onBack) {
        finalPanel.setOnExit(onExit);
        finalPanel.setOnBack(onBack);
    }
}
