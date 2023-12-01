package tests.testng;

import com.google.common.annotations.VisibleForTesting;
import org.testng.annotations.*;

public class TestNGHooks {
    /**
     * BeforeSuite
     * BeforeTest
     * BeforeClass
     * BeforeMethod
     */

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("Before Suite!");
    }

    @BeforeTest
    public void beforeTest() {
        System.out.println("\tBefore Test!");
    }

    @BeforeClass
    public void beforeClass() {
        System.out.println("\t\tBefore Class!");
    }

    @BeforeMethod
    public void beforeMethod() {
        System.out.println("\t\t\tBefore Method!");
    }

    @Test
    public void test1() {
        System.out.println("\t\t\t\tTest method 01!");
    }

    @Test
    public void test2() {
        System.out.println("\t\t\t\tTest method 02!");
    }
}
