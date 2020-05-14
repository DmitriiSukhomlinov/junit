package JUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TestRunner {
    private ArrayList<String> arguments;
    private ConcurrentLinkedQueue<Class<?>> testClasses = new ConcurrentLinkedQueue<Class<?>>();
    private ArrayList<String> testPassed = new ArrayList<>();
    private HashMap<String, String> testFailed = new HashMap<>();
    private HashMap<String, String> classFailed = new HashMap<>();
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


        for (int i = 0; i < arguments.size(); i++) {
            if (i == 0) {
                threadsNumber = Integer.parseInt(arguments.get(i));
                if (threadsNumber < 1) {
                    throw new Exception("Threads number should be more than 1.");
                }
            } else {
                testClasses.add(Class.forName(arguments.get(i)));
            }
        }

        for (int i = 0; i < threadsNumber; i++ ) {
            new TestThread(this).start();
        }

        while (testingInProcess()) {
            Thread.sleep(10);
        }

        printOutput();
    }

    public Class<?> getTestObject() {
        return testClasses.poll();
    }

    public synchronized void sendInfo(Analyzer analyzer) {
        testPassed.addAll(analyzer.getTestPassed());
        testFailed.putAll(analyzer.getTestFailed());
        classFailed.putAll(analyzer.getClassFailed());
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

        System.out.println();
        if (!testFailed.isEmpty()) {
            System.out.println("The following test are failed with errors:");
            for (String key : testFailed.keySet()) {
                System.out.println(key + ": " + testFailed.get(key));
            }
        }

        System.out.println();
        if (!classFailed.isEmpty()) {
            System.out.println("The instance of the following classes wasn't created because of the exception:");
            for (String key : classFailed.keySet()) {
                System.out.println(key + ": " + classFailed.get(key));
            }
        }
    }
}
