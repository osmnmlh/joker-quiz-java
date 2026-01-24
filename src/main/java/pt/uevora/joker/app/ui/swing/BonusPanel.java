package pt.uevora.joker.app.ui.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.function.IntConsumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class BonusPanel extends JPanel {
    private final JTextArea questionArea;
    private final JButton[] optionButtons;
    private final JLabel countdownLabel;
    private final JLabel correctLabel;
    private Timer timer;
    private Runnable timeoutListener;
    private IntConsumer optionListener;

    public BonusPanel() {
        super(new BorderLayout(8, 8));
        questionArea = new JTextArea(3, 40);
        questionArea.setEditable(false);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);

        optionButtons = new JButton[2];
        JPanel optionsPanel = new JPanel(new GridLayout(1, 2, 8, 8));
        for (int i = 0; i < optionButtons.length; i++) {
            int index = i;
            JButton button = new JButton();
            button.addActionListener(event -> {
                if (optionListener != null) {
                    optionListener.accept(index);
                }
            });
            optionButtons[i] = button;
            optionsPanel.add(button);
        }

        countdownLabel = new JLabel("Time left: 60s");
        correctLabel = new JLabel("Correct: 0");
        JPanel infoPanel = new JPanel();
        infoPanel.add(countdownLabel);
        infoPanel.add(correctLabel);

        add(new JScrollPane(questionArea), BorderLayout.NORTH);
        add(optionsPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
    }

    public void setQuestion(String text, List<String> options) {
        questionArea.setText(text);
        optionButtons[0].setText("A. " + options.get(0));
        optionButtons[1].setText("B. " + options.get(1));
        setOptionsEnabled(true);
    }

    public void setOptionListener(IntConsumer listener) {
        this.optionListener = listener;
    }

    public void setTimeoutListener(Runnable listener) {
        this.timeoutListener = listener;
    }

    public void startTimer(long remainingMs) {
        stopTimer();
        long endTime = System.currentTimeMillis() + remainingMs;
        timer = new Timer(1000, event -> {
            long leftMs = endTime - System.currentTimeMillis();
            if (leftMs <= 0) {
                countdownLabel.setText("Time left: 0s");
                stopTimer();
                setOptionsEnabled(false);
                if (timeoutListener != null) {
                    timeoutListener.run();
                }
                return;
            }
            long seconds = (leftMs + 999) / 1000;
            countdownLabel.setText("Time left: " + seconds + "s");
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    public void stopTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    public void reset() {
        questionArea.setText("");
        countdownLabel.setText("Time left: 60s");
        setCorrectCount(0);
        setOptionsEnabled(false);
    }

    public void setCorrectCount(int correct) {
        correctLabel.setText("Correct: " + correct);
    }

    public int getCorrectCount() {
        String text = correctLabel.getText();
        int idx = text.lastIndexOf(' ');
        if (idx == -1) {
            return 0;
        }
        try {
            return Integer.parseInt(text.substring(idx + 1));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void setOptionsEnabled(boolean enabled) {
        for (JButton button : optionButtons) {
            button.setEnabled(enabled);
        }
    }
}
