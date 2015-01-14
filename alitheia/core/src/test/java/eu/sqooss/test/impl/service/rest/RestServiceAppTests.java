package eu.sqooss.test.impl.service.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.MalformedURLException;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import eu.sqooss.impl.service.fds.tests.CoreActivator;
import eu.sqooss.impl.service.rest.RestServiceApp;

public class RestServiceAppTests {
	
	@BeforeClass
    public static void setUp() throws MalformedURLException {
    	new CoreActivator();
    }

	@Test
    public void testGetClasses() {
		Set<Class<?>> classes = new RestServiceApp().getClasses();
		assertNotNull(classes);
		assertEquals(classes.size(), 2);
    }
	
	@Test
    public void testGetSingletons() {
		assertNull(new RestServiceApp().getSingletons());
    }
}
