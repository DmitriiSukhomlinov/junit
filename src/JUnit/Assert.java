package JUnit;

public class Assert {
    private Assert() {}

    private static String EQUAL_DEFAULT_ERROR_MESSAGE = "Values \"%1$s\" and \"%2$s\" should be equal.";
    private static String NOT_EQUAL_DEFAULT_ERROR_MESSAGE = "Values \"%1$s\" and \"%2$s\" should not be equal.";
    private static String IS_TRUE_DEFAULT_ERROR_MESSAGE = "The condition should be true.";
    private static String IS_FALSE_DEFAULT_ERROR_MESSAGE = "The condition should be false.";

    public static <T> void equals(T actual, T expected) {
        equalsWithErrorMessage(actual, expected, String.format(EQUAL_DEFAULT_ERROR_MESSAGE, actual, expected));
    }

    public static <T> void equalsWithErrorMessage(T actual, T expected, String errorMessage) {
        checkCondition(actual.equals(expected), errorMessage);
    }

    public static <T> void notEquals(T actual, T expected) {
        notEqualsWithErrorMessage(actual, expected, String.format(NOT_EQUAL_DEFAULT_ERROR_MESSAGE, actual, expected));
    }

    public static <T> void notEqualsWithErrorMessage(T actual, T expected, String errorMessage) {
        checkCondition(!actual.equals(expected), errorMessage);
    }

    public static void isTrue(boolean condition) {
        isTrueWithErrorMessage(condition, IS_TRUE_DEFAULT_ERROR_MESSAGE);
    }

    public static void isTrueWithErrorMessage(boolean condition, String errorMessage) {
        checkCondition(condition, errorMessage);
    }

    public static void isFalse(boolean condition) {
        isFalseWithErrorMessage(condition, IS_FALSE_DEFAULT_ERROR_MESSAGE);
    }

    public static void isFalseWithErrorMessage(boolean condition, String errorMessage) {
        checkCondition(!condition, errorMessage);
    }

    private static void checkCondition(boolean condition, String errorMessage) {
        if (!condition) {
            throw new RuntimeException(errorMessage);
        }
    }
}
