package JUnit;

public class TestThread extends Thread {
    //private Class<?> testClass = null;
    private TestRunner runner = null;

    public TestThread(TestRunner _runner) {
        super();
        runner = _runner;
    }

    @Override
    public void run() {
        TestAnalyzer analyzer = new TestAnalyzer();
        while (true) {
            Class<?> testObject = runner.getTestObject();
            if (testObject == null) {
                runner.incSemaphore();
                return;
            }
            analyzer.analyze(testObject);
            runner.sendInfo(analyzer);
        }
    }

}
