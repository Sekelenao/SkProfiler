package io.github.sekelenao.skprofiler.log;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.Objects;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomLoggingFormatter extends Formatter {

    private static final String BOLD = "\u001B[1m";

    private static final String RED = "\u001B[31m";

    private static final String GREEN = "\u001B[32m";

    private static final String YELLOW = "\u001B[33m";

    public static final String CYAN = "\u001B[36m";

    public static final String PURPLE = "\u001B[35m";

    public static final String GRAY = "\u001B[37m";

    private static final String RESET = "\u001B[0m";

    private static String formatLogLevel(String level){
        return BOLD + switch (level){
            case "SEVERE"-> RED;
            case "WARNING" -> YELLOW;
            case "INFO" -> GREEN;
            default -> PURPLE;
        } + level + RESET;
    }

    private static String formatMessage(String message, Object... parameters){
        if(Objects.nonNull(parameters) && parameters.length > 0){
            return MessageFormat.format(message, parameters);
        }
        return message;
    }

    private static String formatInstant(Instant instant){
        return GRAY + instant.atZone(java.time.ZoneId.systemDefault())
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")) + RESET;
    }

    private static String formatLoggerName(String loggerName){
        return GRAY + "[" + loggerName.substring(loggerName.lastIndexOf('.') + 1) + "]" + RESET;
    }

    private static String formatAppName(){
        return BOLD + CYAN + "SkProfiler" + RESET;
    }

    @Override
    public String format(LogRecord logRecord) {
        return String.format(
                "%s %s %s %s %s%n",
                formatInstant(logRecord.getInstant()),
                formatAppName(),
                formatLogLevel(logRecord.getLevel().toString()),
                formatLoggerName(logRecord.getLoggerName()),
                formatMessage(logRecord.getMessage(), logRecord.getParameters())
        );
    }

}