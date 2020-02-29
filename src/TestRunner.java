import java.lang.annotation.Annotation;
import TestData.FactorialTest;
import Annotations.TestAnalyzer;

public class TestRunner {
    private static int threadsNumber = 0;

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new Exception("Too few arguments.");
        }

        for (int i = 0; i < args.length; i++) {
            if (i == 0) {
                threadsNumber = Integer.parseInt(args[i]);
                if (threadsNumber < 1) {
                    throw new Exception("Threads number should be more then 1.");
                }
            } else {
                Class<?> testClass = Class.forName(args[i]);
                TestAnalyzer testAnalyzer = new TestAnalyzer();
                testAnalyzer.analyze(testClass);

//                for (var method : testClass.getMethods()) {
//                    //method.invoke(new TestData.FactorialTest());
//                    Annotation[] annotations = method.getAnnotations();
//                    if (annotations.length == 0) {
//                        continue;
//                    }
//
//                    for (var a: annotations) {
//                        System.out.println(a.toString());
//                    }
//                }
//                int q = 0;
            }
        }



    }
}
