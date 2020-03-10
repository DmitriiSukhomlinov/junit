package JUnit;

public class TestThread extends java.lang.Thread {
    private TestRunner runner = null;

    public TestThread(TestRunner _runner) {
        super();
        runner = _runner;
    }

    @Override
    public void run() {
        Analyzer analyzer = new Analyzer();
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
