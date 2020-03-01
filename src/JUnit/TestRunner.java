package JUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.lang.Thread.sleep;

public class TestRunner {
    private ArrayList<String> arguments;
    private ConcurrentLinkedQueue<Class<?>> testClasses = new ConcurrentLinkedQueue<Class<?>>();
    private ArrayList<String> testPassed = new ArrayList<>();
    private HashMap<String, String> testFailed = new HashMap<>();
    volatile int semaphore = 0;

    public TestRunner(String[] args) {
        arguments = new ArrayList<String>(args.length);
        for (String arg : args ) {
            arguments.add(arg);
        }
    }

    public void start() throws Exception {
        if (arguments.size() < 2) {
            throw new Exception("Too few arguments.");
        }

        int threadsNumber = 0;
        for (String arg : arguments) {
            if ((arguments.indexOf(arg)) == 0) {
                threadsNumber = Integer.parseInt(arg);
                if (threadsNumber < 1) {
                    throw new Exception("Threads number should be more then 1.");
                }
            } else {
                testClasses.add(Class.forName(arg));
            }
        }

        for (int i = 0; i < threadsNumber; i++ ) {
            new TestThread(this).start();
        }

        while (semaphore != threadsNumber) {
            sleep(10);
        }

        System.out.println("The following tests are passed:");
        for (String str: testPassed) {
            System.out.println(str);
        }
        System.out.println("The following test are failed with errors:");
        for (String key : testFailed.keySet()) {
            System.out.println(key + ": " + testFailed.get(key));
        }
    }

    public synchronized Class<?> getTestObject() {
        Class<?> result = null;
        if (!testClasses.isEmpty()) {
            result = testClasses.poll();
        }
        return result;
    }

    public synchronized void sendInfo(TestAnalyzer analyzer) {
        testPassed.addAll(analyzer.getTestPassed());
        testFailed.putAll(analyzer.getTestFailed());
    }

    public synchronized void incSemaphore() {
        semaphore++;
    }

}
