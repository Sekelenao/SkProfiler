package io.github.sekelenao.skprofiler.log;

import io.github.sekelenao.skprofiler.system.EnvironmentProperties;
import io.github.sekelenao.skprofiler.util.Optionals;

import java.util.Objects;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CustomLogger {

    private static final ClassValue<CustomLogger> LOGGER_CACHE = new ClassValue<>() {

        @Override
        protected CustomLogger computeValue(Class<?> clazz) {
            var newLogger = Logger.getLogger(clazz.getName());
            newLogger.setUseParentHandlers(false);
            ConsoleHandler handler = new ConsoleHandler();
            handler.setFormatter(new CustomLoggingFormatter());
            newLogger.addHandler(handler);
            return new CustomLogger(newLogger);
        }

    };

    private final Logger logger;

    public CustomLogger(Logger logger) {
        Objects.requireNonNull(logger);
        this.logger = logger;
    }

    public static CustomLogger on(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return LOGGER_CACHE.get(clazz);
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

    public static void displayBannerAndStartingLogs(CustomLogger logger) {
        System.out.println("""
               _____ __   ____             _____ __
              / ___// /__/ __ \\_________  / __(_) /__  _____
              \\__ \\/ //_/ /_/ / ___/ __ \\/ /_/ / / _ \\/ ___/
             ___/ / ,< / ____/ /  / /_/ / __/ / /  __/ /
            /____/_/|_/_/   /_/   \\____/_/ /_/_/\\___/_/
            """);
        logger.info(
            "Agent is starting within the target application: {0}",
            Optionals.asStringOrMissingDescriptor(EnvironmentProperties.command())
        );
        logger.info(
            "Actually running with the following Java version: {0}",
            Optionals.asStringOrMissingDescriptor(EnvironmentProperties.javaVersion())
        );
    }

}
