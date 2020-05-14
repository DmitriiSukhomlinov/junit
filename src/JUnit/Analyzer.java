package JUnit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

class Analyzer {
    private ArrayList<String> testPassed = new ArrayList<>();
    private HashMap<String, String> testFailed = new HashMap<>();

    private static final String UNEXPECTED_EXCEPTION_ERROR_MESSAGE = "Incorrect exception, expected: \"%1$s\", current: \"%2$s\".";
    private static final String EXCEPTION_WASNT_THROWN_BUT_SHOULD_BE = "The following exception wasn't thrown: \"%1$s\", but should be.";

    public ArrayList<String> getTestPassed() {
        return testPassed;
    }

    public HashMap<String, String> getTestFailed() {
        return testFailed;
    }

    public void analyze(Class<?> clazz) {
        Object instance = null;
        try {
            instance = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            assert(false);
            return;
        }

        runTests(instance, clazz.getMethods());
    }

    private ArrayList<Method> getMethodsOfAnnotationType(Method[] methods, Class classType) {
        ArrayList<Method> result = new ArrayList<Method>();
        //Class classType = Before.class;
        //if (type == AnnotationType.After) {
        //    classType = After.class;
        //}

        for (Method method : methods) {
            if (method.isAnnotationPresent(classType)) {
                result.add(method);
            }
        }

        return result;
    }

    private void processBeforeOrAfter(Object instance, ArrayList<Method> methods) throws Throwable {
        for (Method method : methods) {
             try {
                method.invoke(instance);
             } catch (Exception e) {
                throw e.getCause();
             }
        }

    }

    private void runTests(Object instance, Method[] methods) {
        ArrayList<Method> methodsBefore = getMethodsOfAnnotationType(methods, Before.class);
        ArrayList<Method> methodsAfter = getMethodsOfAnnotationType(methods, After.class);

        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                try { //before
                    processBeforeOrAfter(instance, methodsBefore);
                } catch (Throwable e) {
                    testFailed.put(method.getName(), e.getMessage());
                    continue;
                }

                try {
                    method.invoke(instance);
                    var expected = method.getAnnotation(Test.class).expected();
                    boolean exceptionWasntThrown = expected.equals(Null.class);
                    if (!exceptionWasntThrown) {
                        testFailed.put(method.getName(),
                                String.format(EXCEPTION_WASNT_THROWN_BUT_SHOULD_BE, expected));
                    }
                    try {//after
                        processBeforeOrAfter(instance, methodsAfter);
                    } catch (Throwable e) {
                        testFailed.put(method.getName(), e.getMessage());
                        continue;
                    }
                    testPassed.add(method.getName());
                } catch (Throwable e) {
                    var expected = method.getAnnotation(Test.class).expected();
                    var current = e.getCause().getClass();
                    boolean expectedExceptionWasThrown = expected.equals(current);
                    if (!expectedExceptionWasThrown) {
                        if (expected.equals(Null.class)) {
                            testFailed.put(method.getName(), e.getCause().getMessage());
                        } else {
                            testFailed.put(method.getName(),
                                    String.format(UNEXPECTED_EXCEPTION_ERROR_MESSAGE, expected, current));
                        }
                    }
                    try {//after
                        processBeforeOrAfter(instance, methodsAfter);
                    } catch (Throwable e2) {
                        if (expectedExceptionWasThrown) {
                            testFailed.put(method.getName(), e2.getMessage());
                        }
                        continue;
                    }
                    testPassed.add(method.getName());



                }
            }
        }
    }

}
