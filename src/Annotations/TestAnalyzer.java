package Annotations;

import java.lang.reflect.Method;

public class TestAnalyzer {
    private int testPassed = 0;
    private int testFailed = 0;

    public void analyze(Class<?> _class) throws Exception {
        Object instance = _class.getDeclaredConstructor().newInstance();
        for (Method method : _class.getMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                Test test = method.getAnnotation(Test.class);
                boolean exceptionWasThrown = false;
                try {
                    method.invoke(instance);
                } catch (Exception e) {
                    exceptionWasThrown = true;
                    if (test.expected().equals(e.getCause().getClass())) {
                       testPassed++;
                    } else {
                        testFailed++;
                    }
                }
                if (!exceptionWasThrown) {
                    testPassed++;
                }
            }
        }
    }
}
