package com.ranga;

import org.apache.log4j.Logger;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        System.out.println("Hello World!");
        logger.info("Hello World!");
    }
}
