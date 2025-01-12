package io.github.sekelenao.skprofiler.log;

import io.github.sekelenao.skprofiler.system.EnvironmentProperties;

import java.util.Objects;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CustomLogger {

    private final Logger logger;

    public CustomLogger(Logger logger) {
        Objects.requireNonNull(logger);
        this.logger = logger;
    }

    public static CustomLogger on(Class<?> clazz){
        Objects.requireNonNull(clazz);
        var logger = Logger.getLogger(clazz.getName());
        logger.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new CustomLoggingFormatter());
        logger.addHandler(handler);
        return new CustomLogger(logger);
    }

    public void info(String message, Object... parameters) {
        Objects.requireNonNull(message);
        Objects.requireNonNull(parameters);
        logger.log(Level.INFO, message, parameters);
    }

    public void warning(String message, Object... parameters) {
        Objects.requireNonNull(message);
        Objects.requireNonNull(parameters);
        logger.log(Level.WARNING, message, parameters);
    }

    public void severe(String message, Object... parameters) {
        Objects.requireNonNull(message);
        Objects.requireNonNull(parameters);
        logger.log(Level.SEVERE, message, parameters);
    }

    public static void displayBannerAndStartingLogs(CustomLogger logger){
        System.out.println("""
           _____ __   ____             _____ __
          / ___// /__/ __ \\_________  / __(_) /__  _____
          \\__ \\/ //_/ /_/ / ___/ __ \\/ /_/ / / _ \\/ ___/
         ___/ / ,< / ____/ /  / /_/ / __/ / /  __/ /
        /____/_/|_/_/   /_/   \\____/_/ /_/_/\\___/_/
        """);
        logger.info("Agent is starting within the target application: {0}", EnvironmentProperties.command().orElse("Unknown"));
        logger.info("Actually running with the following Java version: {0}", EnvironmentProperties.javaVersion());
    }

}
