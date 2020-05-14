package JUnit;

class TestThread extends Thread {
    final private TestRunner runner;

    public TestThread(TestRunner runner) {
        super();
        this.runner = runner;
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
