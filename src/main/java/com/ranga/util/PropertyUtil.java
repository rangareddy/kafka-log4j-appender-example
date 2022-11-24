package com.ranga.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

    private static File getConfigFile() {
        return getConfigFile(null);
    }

    private static File getConfigFile(String file) {
        if (file == null || file.length() == 0) {
            file = "src/main/resources/log4j.properties";
        }
        return new File(file);
    }

    public static Properties getProperties() {
        return getProperties(getConfigFile());
    }

    public static Properties getProperties(String file) {
        return getProperties(getConfigFile(file));
    }

    public static Properties getProperties(String[] args) {
        return getProperties(args, null);
    }

    public static Properties getProperties(String[] args, String file) {
        if (args.length > 0 || file != null) {
            return getProperties(getConfigFile(args.length > 0 ? args[0] : file));
        } else {
            return getProperties(getConfigFile());
        }
    }

    private static Properties getProperties(File file) {
        if (!file.exists()) {
            throw new RuntimeException(file + " does not exist");
        }
        Properties prop = new Properties();
        try (InputStream stream = new FileInputStream(file)) {
            prop.load(stream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return prop;
    }
}
