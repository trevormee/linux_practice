/*
	 PackageSetTest.java
	 @author Trevor Mee
 */

package mainProject;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.io.IOException;

public class PackageSetTest {

    private PackageSet packageSet;

    @Before
    public void setUp() throws IOException {
        packageSet = new PackageSet("packages.txt", 4);
        packageSet.LoadFile();
    }

    @After
    public void tearDown() {
        packageSet = null;
    }

    @Test
    public void testGetSize() {
        assertEquals(4, PackageSet.getSize());
    }

    @Test
    public void testGetFileName() {
        assertEquals("packages.txt", packageSet.getFileName());
    }

    @Test
    public void testGetPackageName() {
        assertEquals("Food", PackageSet.getPackageName(0));
        assertEquals("Beverage", PackageSet.getPackageName(1));
        assertEquals("WiFi", PackageSet.getPackageName(2));
        assertEquals("Spa & Treatment", PackageSet.getPackageName(3));
    }

    @Test
    public void testGetPackagePrice() {
        assertEquals(5.99, PackageSet.getPackagePrice(0), 0.01);
        assertEquals(3.99, PackageSet.getPackagePrice(1), 0.01);
        assertEquals(2.99, PackageSet.getPackagePrice(2), 0.01);
        assertEquals(19.99, PackageSet.getPackagePrice(3), 0.01);
    }

    @Test
    public void testGetPackageDescription() {
        assertEquals("Hungry? Enjoy all the snacks you need and a meal planned by one of our train chefs upon your request!", PackageSet.getPackageDescription(0));
        assertEquals("Parched? Select this package and you can have as many drinks as you like!", PackageSet.getPackageDescription(1));
        assertEquals("Need to send an email or enjoy streaming your favorite show? This package will help you take care of everything for your internet needs!", packageSet.getPackageDescription(2));
        assertEquals("You asked, we delivered! Enjoy an hour long spa session in our most relaxing train car!", PackageSet.getPackageDescription(3));
    }
}
