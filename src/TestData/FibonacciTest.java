package TestData;

import JUnit.Assert;
import JUnit.Before;
import JUnit.Test;
import JUnit.After;
//import JUnit.Assert;

public class FibonacciTest {
    private MyMath instance;
    private int x;

    @Before
    public void beforeEach() {
        instance = new MyMath();
        x = instance.fibonacci(7);
        //Assert.isFalse(true);
    }

//    @Before
//    public void beforeEach2() {
//        Assert.equals(0, 0);
//    }

    @Test
    public void fibonacciTest() {
        Assert.equals(x, 20);
    }

    @Test
    public void fibonacciTest2() {
        Assert.equalsWithErrorMessage(x, 21, "Error fibonacciTest2");
    }

    @Test
    public void fibonacciTest3() {
        Assert.notEquals(x, 20);
    }

    @Test
    public void fibonacciTest4() {
        Assert.notEqualsWithErrorMessage(x, 20, "Error fibonacciTest4");
    }

    @Test
    public void fibonacciTest5() {
        Assert.isTrue(x == 21);
    }

    @Test
    public void fibonacciTest6() {
        Assert.isTrueWithErrorMessage(x == 21, "Error fibonacciTest6");
    }

    @Test
    public void fibonacciTest7() {
        Assert.isFalse(x == 20);
    }

    @Test
    public void fibonacciTest8() {
        Assert.isFalseWithErrorMessage(x == 20, "Error fibonacciTest8");
    }

    @After
    public void afterEach() {
        //assert (1 == 0);
        int i = 0;
        Assert.isTrue(true);
    }

//    @After
//    public void afterEach2() {
//        Assert.isFalse(false);
//    }

}
