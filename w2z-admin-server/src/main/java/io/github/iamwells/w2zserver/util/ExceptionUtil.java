package io.github.iamwells.w2zserver.util;

public class ExceptionUtil {

    public static String deepCauseMessage(Throwable throwable) {
        String message = throwable.getMessage();
        if (message != null) {
            return message;
        }
        StringBuilder builder = new StringBuilder();
        Throwable cause = throwable;
        while (cause != cause.getCause() && cause.getCause() != null) {
            builder.append(cause.getClass().getName())
                    .append(" -> ");
            cause = cause.getCause();
        }
        builder.append(cause.getClass().getName())
                .append("：")
                .append(cause.getMessage());
        return builder.toString();
    }
}
