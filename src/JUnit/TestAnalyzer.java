package JUnit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestAnalyzer {
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
            undeclaredClassMessages.add(e.getMessage());
        }

        runTests(instance, _class.getMethods());

//        for (Method method :getMethodsOfAnnotationType(methods, AnnotationType.Before)) {
//            try {
//                method.invoke(instance);
//            } catch (Exception e) {
//                exceptionMessages.add(e.getMessage());
//            }
//        }








//        for (Method method : _class.getMethods()) {
//            if (method.isAnnotationPresent(Test.class)) {
//                Test test = method.getAnnotation(Test.class);
//                boolean exceptionWasThrown = false;
//                try {
//                    method.invoke(instance);
//                } catch (Exception e) {
//                    exceptionWasThrown = true;
//                    if (test.expected().equals(e.getCause().getClass())) {
//                       testPassed++;
//                    } else {
//                        exceptionMessages.add(e.getMessage());
//                    }
//                }
//                if (!exceptionWasThrown) {
//                    testPassed++;
//                }
//            }
//        }

        //processBeforeOrAfter(instance, methods, AnnotationType.Before);
//        for (Method method :getMethodsOfAnnotationType(methods, AnnotationType.After)) {
//            try {
//                method.invoke(instance);
//            } catch (Exception e) {
//                exceptionMessages.add(e.getMessage());
//            }
//        }
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

    private void processBeforeOrAfter(Object instance, Method[] methods, AnnotationType type) throws Exception{
        for (Method method :getMethodsOfAnnotationType(methods, type)) {
            method.invoke(instance);
//            try {
//
//            } catch (Exception e) {
//                //testFailed.put(method)
//                //exceptionMessages.add(e.getMessage());
//            }
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
                } catch (Exception e) {
                    exceptionWasThrown = true;
                    if (method.getAnnotation(Test.class).expected().equals(e.getCause().getClass())) {
                        try {//after
                            processBeforeOrAfter(instance, methods, AnnotationType.After);
                            testPassed.add(method.getName());
                        } catch (Exception e2) {
                            testFailed.put(method.getName(), e.getMessage());
                            continue;
                        }
                    } else {
                        testFailed.put(method.getName(), e.getMessage());
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
