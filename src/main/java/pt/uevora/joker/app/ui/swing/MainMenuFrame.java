package pt.uevora.joker.app.ui.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import pt.uevora.joker.app.ui.GameRunner;
import pt.uevora.joker.io.QuestionBankBootstrap;

public class MainMenuFrame extends JFrame {
    private final JTextArea statusArea;
    private GameRunner runner;

    public MainMenuFrame() {
        super("Jogo do Joker — GUI (Swing)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        statusArea = new JTextArea(8, 40);
        statusArea.setEditable(false);

        JButton startButton = new JButton("Start Game (GUI mode)");
        JButton buildCacheButton = new JButton("Build Cache");
        JButton exitButton = new JButton("Exit");

        startButton.addActionListener(event -> startGame());
        buildCacheButton.addActionListener(event -> buildCache());
        exitButton.addActionListener(event -> closeAndShutdown());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(buildCacheButton);
        buttonPanel.add(exitButton);

        Container root = getContentPane();
        root.setLayout(new BorderLayout(8, 8));
        root.add(new JLabel("GUI Mode (Work in Progress)", JLabel.CENTER), BorderLayout.NORTH);
        root.add(new JScrollPane(statusArea), BorderLayout.CENTER);
        root.add(buttonPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                closeAndShutdown();
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    public void appendStatus(String message) {
        SwingUtilities.invokeLater(() -> {
            statusArea.append(message + System.lineSeparator());
            statusArea.setCaretPosition(statusArea.getDocument().getLength());
        });
    }

    private void startGame() {
        if (runner != null) {
            appendStatus("Game already started.");
            return;
        }
        appendStatus("Starting game engine (GUI mode)...");
        GameFrame gameFrame = new GameFrame();
        SwingGameIO io = new SwingGameIO(gameFrame);
        runner = new GameRunner(io);
        gameFrame.setFinalActions(() -> {
            if (runner != null) {
                runner.shutdown();
                runner = null;
            }
            SwingUtilities.invokeLater(gameFrame::dispose);
        }, () -> {
            if (runner != null) {
                runner.shutdown();
                runner = null;
            }
            SwingUtilities.invokeLater(() -> {
                gameFrame.dispose();
                setVisible(true);
            });
        });
        gameFrame.setOnClose(() -> {
            if (runner != null) {
                runner.shutdown();
                runner = null;
            }
            SwingUtilities.invokeLater(() -> setVisible(true));
        });
        SwingUtilities.invokeLater(() -> {
            setVisible(false);
            gameFrame.setVisible(true);
        });
        runner.start();
    }

    private void buildCache() {
        appendStatus("Building cache...");
        new Thread(() -> {
            try {
                QuestionBankBootstrap.carregarPerguntasNormais();
                QuestionBankBootstrap.carregarPerguntasBonus();
                appendStatus("Cache build complete.");
            } catch (IOException e) {
                appendStatus("Error: " + e.getMessage());
            }
        }, "cache-builder").start();
    }

    private void closeAndShutdown() {
        if (runner != null) {
            runner.shutdown();
            runner = null;
        }
        if (isDisplayable()) {
            dispose();
        }
    }
}
