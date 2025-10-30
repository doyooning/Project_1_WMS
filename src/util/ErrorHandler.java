package util;

public class ErrorHandler {

    public static void displayAndLog(String userMessage, Throwable throwable) {
        // User-facing message
        System.out.println(userMessage);

        // Developer-facing log
        if (throwable != null) {
            System.err.println("[ERROR] " + throwable.getClass().getName() + ": " + throwable.getMessage());
            throwable.printStackTrace(System.err);
        }
    }
}