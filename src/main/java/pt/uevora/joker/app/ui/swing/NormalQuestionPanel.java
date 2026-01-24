package pt.uevora.joker.app.ui.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Set;
import java.util.function.IntConsumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class NormalQuestionPanel extends JPanel {
    private final JTextArea questionArea;
    private final JButton[] optionButtons;
    private final boolean[] optionEnabled;
    private final JButton useJokerButton;
    private final JButton skipJokerButton;
    private IntConsumer optionListener;
    private Runnable useJokerListener;
    private Runnable skipJokerListener;
    private boolean jokerEnabled;

    public NormalQuestionPanel() {
        super(new BorderLayout(8, 8));
        questionArea = new JTextArea(4, 40);
        questionArea.setEditable(false);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);

        optionButtons = new JButton[4];
        optionEnabled = new boolean[4];
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 8, 8));
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

        useJokerButton = new JButton("Use Joker");
        useJokerButton.addActionListener(event -> {
            if (useJokerListener != null) {
                useJokerListener.run();
            }
        });
        skipJokerButton = new JButton("Answer");
        skipJokerButton.addActionListener(event -> {
            if (skipJokerListener != null) {
                skipJokerListener.run();
            }
        });

        JPanel jokerPanel = new JPanel();
        jokerPanel.add(new JLabel("Joker:"));
        jokerPanel.add(useJokerButton);
        jokerPanel.add(skipJokerButton);

        add(new JScrollPane(questionArea), BorderLayout.NORTH);
        add(optionsPanel, BorderLayout.CENTER);
        add(jokerPanel, BorderLayout.SOUTH);

        setJokerDecisionMode(false);
        setJokerEnabled(true);
    }

    public void setQuestion(String text) {
        questionArea.setText(text);
    }

    public void setOptions(List<String> options, Set<Integer> remaining) {
        for (int i = 0; i < optionButtons.length; i++) {
            String label = (char) ('A' + i) + ". " + options.get(i);
            optionButtons[i].setText(label);
            optionEnabled[i] = remaining.contains(i);
            optionButtons[i].setEnabled(optionEnabled[i]);
        }
    }

    public void setOptionListener(IntConsumer listener) {
        this.optionListener = listener;
    }

    public void setUseJokerListener(Runnable listener) {
        this.useJokerListener = listener;
    }

    public void setSkipJokerListener(Runnable listener) {
        this.skipJokerListener = listener;
    }

    public void setJokerEnabled(boolean enabled) {
        jokerEnabled = enabled;
        useJokerButton.setEnabled(enabled);
    }

    public void setJokerDecisionMode(boolean enabled) {
        useJokerButton.setEnabled(enabled && jokerEnabled);
        skipJokerButton.setEnabled(enabled);
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setEnabled(!enabled && optionEnabled[i]);
        }
    }
}
