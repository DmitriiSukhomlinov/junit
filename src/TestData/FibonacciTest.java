package TestData;

import JUnit.Before;
import JUnit.Test;
import JUnit.After;

public class FibonacciTest {
    private MyMath instance;

    @Before
    public void beforeEach() {
        instance = new MyMath();
    }

    @Before
    public void beforeEach2() {
        assert (0 == 0);
    }

    @Test
    public void fibonacciTest() {
        instance = new MyMath();
        int x = instance.fibonacci(7);
        //Assert.assertEquals(x, 21);
        assert (x == 21);
    }

    @After
    public void afterEach() {
        assert (0 == 0);
    }

    @After
    public void afterEach2() {
        assert (0 == 0);
    }

}
