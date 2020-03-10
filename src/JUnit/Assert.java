package JUnit;

public class Assert {
    private Assert() {}

    private static String equalsDefaultErrorMessage = "Values \"%1$s\" and \"%2$s\" should be equal.";
    private static String notEqualsDefaultErrorMessage = "Values \"%1$s\" and \"%2$s\" should not be equal.";
    private static String isTrueDefaultErrorMessage = "The condition should be true.";
    private static String isFalseDefaultErrorMessage = "The condition should be false.";

    public static <T> void equals(T actual, T expected) {
        equalsWithErrorMessage(actual, expected, String.format(equalsDefaultErrorMessage, actual, expected));
    }

    public static <T> void equalsWithErrorMessage(T actual, T expected, String errorMessage) {
        checkCondition(actual.equals(expected), errorMessage);
    }

    public static <T> void notEquals(T actual, T expected) {
        notEqualsWithErrorMessage(actual, expected, String.format(notEqualsDefaultErrorMessage, actual, expected));
    }

    public static <T> void notEqualsWithErrorMessage(T actual, T expected, String errorMessage) {
        checkCondition(!actual.equals(expected), errorMessage);
    }

    public static void isTrue(boolean condition) {
        isTrueWithErrorMessage(condition, isTrueDefaultErrorMessage);
    }

    public static void isTrueWithErrorMessage(boolean condition, String errorMessage) {
        checkCondition(condition, errorMessage);
    }

    public static void isFalse(boolean condition) {
        isFalseWithErrorMessage(condition, isFalseDefaultErrorMessage);
    }

    public static void isFalseWithErrorMessage(boolean condition, String errorMessage) {
        checkCondition(!condition, errorMessage);
    }

    private static void checkCondition(boolean condition, String errorMessage) {
        if (!condition) {
            throw new Error(errorMessage);
        }
    }
}
