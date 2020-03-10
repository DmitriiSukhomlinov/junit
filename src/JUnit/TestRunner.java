package JUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TestRunner {
    private ArrayList<String> arguments;
    private ConcurrentLinkedQueue<Class<?>> testClasses = new ConcurrentLinkedQueue<Class<?>>();
    private ArrayList<String> testPassed = new ArrayList<>();
    private ArrayList<String> undeclaredClasses = new ArrayList<String>();
    private HashMap<String, String> testFailed = new HashMap<>();
    int threadsNumber = 0;
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

        while (testingInProcess()) {
            TestThread.sleep(10);
        }

        printOutput();
    }

    public Class<?> getTestObject() {
        return testClasses.poll();
    }

    public synchronized void sendInfo(Analyzer analyzer) {
        testPassed.addAll(analyzer.getTestPassed());
        undeclaredClasses.addAll(analyzer.getUndeclaredClassMessages());
        testFailed.putAll(analyzer.getTestFailed());
    }

    public synchronized void incSemaphore() {
        semaphore++;
    }

    private boolean testingInProcess() {
        return semaphore != threadsNumber;
    }

    private void printOutput() {
        if(!testPassed.isEmpty()) {
            System.out.println("The following tests are passed:");
            for (String str : testPassed) {
                System.out.println(str);
            }
        }

        if (!undeclaredClasses.isEmpty()) {
            System.out.println("The following classes are undeclared:");
            for (String str : undeclaredClasses) {
                System.out.println(str);
            }
        }

        if (!testFailed.isEmpty()) {
            System.out.println("The following test are failed with errors:");
            for (String key : testFailed.keySet()) {
                System.out.println(key + ": " + testFailed.get(key));
            }
        }
    }
}
