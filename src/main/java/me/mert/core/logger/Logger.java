package me.mert.core.logger;

public class Logger {
    private Logger() {}

    public static void log(LogLevel logLevel, String msg) {
        System.out.printf("[%s] %s%n", logLevel, msg);
    }

    public static void debug(String msg) {
        log(LogLevel.DEBUG, msg);
    }
    public static void info(String msg) {
        log(LogLevel.INFO, msg);
    }
    public static void warning(String msg) {
        log(LogLevel.WARNING, msg);
    }
    public static void error(String msg) {
        log(LogLevel.ERROR, msg);
    }
}
