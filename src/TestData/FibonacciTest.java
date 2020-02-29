package TestData;

import Annotations.Test;

public class FibonacciTest {
    private MyMath instance;

    //@Before
    public void beforeEach() {
        instance = new MyMath();
    }

    @Test
    public void fibonacciTest() {
        instance = new MyMath();
        int x = instance.fibonacci(7);
        //Assert.assertEquals(x, 21);
        assert (x == 21);
    }

}
