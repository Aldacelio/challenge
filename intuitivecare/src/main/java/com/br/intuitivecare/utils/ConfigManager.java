package com.br.intuitivecare.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Arquivo config.properties não encontrado!");
            }
            props.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Erro ao carregar configurações", ex);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}