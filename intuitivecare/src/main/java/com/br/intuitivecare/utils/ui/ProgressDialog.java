package com.br.intuitivecare.utils.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class ProgressDialog {

    private static final Map<String, JDialog> dialogMap = new HashMap<>();

    private ProgressDialog() {
        throw new UnsupportedOperationException("Evitando que a Classe seja instanciada");
    }

    public static void show(String message, String title) {
        SwingUtilities.invokeLater(() -> {
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressBar.setStringPainted(true);
            progressBar.setString(message);

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(progressBar, BorderLayout.CENTER);
            panel.setPreferredSize(new Dimension(300, 70));

            JOptionPane optionPane = new JOptionPane(panel, JOptionPane.INFORMATION_MESSAGE);
            optionPane.setOptions(new Object[0]);
            JDialog dialog = optionPane.createDialog(title);
            dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            dialogMap.put(title, dialog);
            dialog.setVisible(true);
        });
    }

    public static void close(String title) {
        SwingUtilities.invokeLater(() -> {
            JDialog dialog = dialogMap.remove(title);
            if (dialog != null) {
                dialog.dispose();
            }
        });
    }

}
