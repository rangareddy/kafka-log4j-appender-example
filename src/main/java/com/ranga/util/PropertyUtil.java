package com.ranga.util;

import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
    public static Properties getProperties() {
        Properties prop = new Properties();
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("log4j.properties")) {
            prop.load(stream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return prop;
    }
}
