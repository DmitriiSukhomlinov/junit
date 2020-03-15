package TestData;

import JUnit.After;
import JUnit.Assert;
import JUnit.Before;
import JUnit.Test;

public class FactorialTest {
    private MyMath instance;

    @Before
    public void beforeEach() {
        instance = new MyMath();
    }

    @Test
    public void factorialTest() {
        //instance = new MyMath();
        int x = instance.factorial(10);
        //Assert.assertEquals(x, 3628800);
        assert(x == 3628800);
    }

    @Test(expected = Exception.class)
    public void testFactorialExeption() throws Exception {
        //instance = new MyMath();
        instance.factorialExeption(-42);
    }

    @After
    public void afterEach() {
        Assert.isFalse(true);
    }
}
