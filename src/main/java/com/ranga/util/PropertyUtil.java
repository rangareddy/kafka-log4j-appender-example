package com.ranga.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

    public static Properties getProperties() {
        File file = new File("src/main/resources/log4j.properties");
        return getProperties(file);
    }
    public static Properties getProperties(File file) {
        Properties prop = new Properties();
        try (InputStream stream = new FileInputStream(file)) {
            prop.load(stream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return prop;
    }
}
