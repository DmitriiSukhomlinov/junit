package JUnit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class Analyzer {
    private ArrayList<String> testPassed = new ArrayList<>();
    private ArrayList<String> undeclaredClassMessages = new ArrayList<String>();
    private HashMap<String, String> testFailed = new HashMap<>();

    public ArrayList<String> getTestPassed() {
        return testPassed;
    }

    public ArrayList<String> getUndeclaredClassMessages() {
        return undeclaredClassMessages;
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
            undeclaredClassMessages.add(_class.getName());
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

    private void processBeforeOrAfter(Object instance, Method[] methods, AnnotationType type) {
        for (Method method :getMethodsOfAnnotationType(methods, type)) {
             try {
                 method.invoke(instance);
             } catch (Exception e) {
                 return;
             }
        }
    }

    private void runTests(Object instance, Method[] methods) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                try { //before
                    processBeforeOrAfter(instance, methods, AnnotationType.Before);
                } catch (Exception e) {
                    testFailed.put(method.getName(), e.getMessage());
                    continue;
                }

                boolean exceptionWasThrown = false;
                try {
                    method.invoke(instance);
                } catch (Throwable e) {
                    exceptionWasThrown = true;
                    if (method.getAnnotation(Test.class).expected().equals(e.getCause().getClass())) {
                        try {//after
                            processBeforeOrAfter(instance, methods, AnnotationType.After);
                            testPassed.add(method.getName());
                        } catch (Exception e2) {
                            testFailed.put(method.getName(), e2.getCause().getMessage());
                            continue;
                        }
                    } else {
                        testFailed.put(method.getName(), e.getCause().getMessage());
                    }
                }
                if (!exceptionWasThrown) {
                    try {//after
                        processBeforeOrAfter(instance, methods, AnnotationType.After);
                    } catch (Exception e) {
                        testFailed.put(method.getName(), e.getMessage());
                        continue;
                    }
                    testPassed.add(method.getName());
                }
            }
        }
    }

}
