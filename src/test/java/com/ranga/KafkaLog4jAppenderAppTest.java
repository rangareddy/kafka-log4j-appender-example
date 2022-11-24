package com.ranga;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple KafkaLog4jAppenderApp.
 */
public class KafkaLog4jAppenderAppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public KafkaLog4jAppenderAppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(KafkaLog4jAppenderAppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }
}
