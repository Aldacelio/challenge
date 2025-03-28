package com.br.intuitivecare.utils;

import javax.swing.JOptionPane;

public class Dialog {

    public static boolean confirm(String message) {
        int response = JOptionPane.showConfirmDialog(null,
                formatMessage(message),
                "Confirmação",
                JOptionPane.YES_NO_OPTION);
        return response == JOptionPane.YES_OPTION;
    }

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null,
                formatMessage(message),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(null,
                formatMessage(message),
                title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static String formatMessage(String message) {
        return "<html><body style='width: 300px;'>" + message + "</body></html>";
    }

}
