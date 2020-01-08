package org.gradle.api.logging;

public class Logging {

    public static Logger getLogger(String name) {
        return new Logger() {
            @Override
            public void info(String msg) {
            }
        };
    }

}
