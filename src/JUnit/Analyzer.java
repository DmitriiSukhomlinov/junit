package JUnit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class Analyzer {
    private ArrayList<String> testPassed = new ArrayList<>();
    private HashMap<String, String> testFailed = new HashMap<>();

    private String UNEXPECTED_EXCEPTION_ERROR_MESSAGE = "Incorrect exception, expected: \"%1$s\", current: \"%2$s\".";

    public ArrayList<String> getTestPassed() {
        return testPassed;
    }

    public HashMap<String, String> getTestFailed() {
        return testFailed;
    }

    private enum AnnotationType {
        Before,
        After
    }

    public void analyze(Class<?> _class) {
        Object instance = null;
        try {
            instance = _class.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return;
        }

        runTests(instance, _class.getMethods());
    }

    private ArrayList<Method> getMethodsOfAnnotationType(Method[] methods, AnnotationType type) {
        ArrayList<Method> result = new ArrayList<Method>();
        Class classType = Before.class;
        if (type.equals(AnnotationType.After)) {
            classType = After.class;
        }

        for (Method method : methods) {
            if (method.isAnnotationPresent(classType)) {
                result.add(method);
            }
        }

        return result;
    }

    private void processBeforeOrAfter(Object instance, Method[] methods, AnnotationType type) throws Throwable {
        for (Method method :getMethodsOfAnnotationType(methods, type)) {
             try {
                method.invoke(instance);
             } catch (Exception e) {
                throw e.getCause();
             }
        }

    }

    private void runTests(Object instance, Method[] methods) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                try { //before
                    processBeforeOrAfter(instance, methods, AnnotationType.Before);
                } catch (Throwable e) {
                    testFailed.put(method.getName(), e.getMessage());
                    continue;
                }

                boolean exceptionWasThrown = false;
                try {
                    method.invoke(instance);
                } catch (Throwable e) {
                    exceptionWasThrown = true;
                    var expected = method.getAnnotation(Test.class).expected();
                    var current = e.getCause().getClass();
                    if (expected.equals(current)) {
                        try {//after
                            processBeforeOrAfter(instance, methods, AnnotationType.After);
                        } catch (Throwable e2) {
                            testFailed.put(method.getName(), e2.getMessage());
                            continue;
                        }
                        testPassed.add(method.getName());
                    } else {
                        if (expected.equals(Null.class)) {
                            testFailed.put(method.getName(), e.getCause().getMessage());
                        } else {
                            testFailed.put(method.getName(),
                                    String.format(UNEXPECTED_EXCEPTION_ERROR_MESSAGE, expected, current));
                        }
                    }
                }
                if (!exceptionWasThrown) {
                    try {//after
                        processBeforeOrAfter(instance, methods, AnnotationType.After);
                    } catch (Throwable e) {
                        testFailed.put(method.getName(), e.getMessage());
                        continue;
                    }
                    testPassed.add(method.getName());
                }
            }
        }
    }

}
