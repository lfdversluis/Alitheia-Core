package eu.sqooss.test.service.abstractmetric;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;

import eu.sqooss.core.AlitheiaCore;
import eu.sqooss.impl.service.metricactivator.MetricMismatchException;
import eu.sqooss.service.abstractmetric.AbstractMetric;
import eu.sqooss.service.db.DAObject;
import eu.sqooss.service.db.Metric;
import eu.sqooss.service.db.ProjectVersionMeasurement;

public class AbstractMetricTest {
	
	@Mock
	static BundleContext bc;
	static Bundle b;
	static AbstractMetric tm;
    	
    @BeforeClass
    public static void setUp() throws MalformedURLException {
    	
    	b = Mockito.mock(Bundle.class);
    	Mockito.when(b.getResource("hibernate.cfg.xml")).thenReturn(new File("src/main/resources/hibernate.cfg.xml").toURI().toURL());
    	Hashtable<String, String> table = new Hashtable<String, String>();
    	table.put(org.osgi.framework.Constants.BUNDLE_NAME, "test");
    	table.put(org.osgi.framework.Constants.BUNDLE_VERSION, "1.42");
    	table.put(org.osgi.framework.Constants.BUNDLE_DESCRIPTION, "test description");
    	Mockito.when(b.getHeaders()).thenReturn(table);
    	bc = Mockito.mock(BundleContext.class);
    	Mockito.when(bc.getProperty("eu.sqooss.db")).thenReturn("mysql");
    	Mockito.when(bc.getProperty("eu.sqooss.db.host")).thenReturn("localhost");
    	Mockito.when(bc.getProperty("eu.sqooss.db.schema")).thenReturn("alitheia");
    	Mockito.when(bc.getProperty("eu.sqooss.db.user")).thenReturn("alitheia");
    	Mockito.when(bc.getProperty("eu.sqooss.db.passwd")).thenReturn("alitheia");
    	Mockito.when(bc.getProperty("eu.sqooss.db.conpool")).thenReturn("c3p0");
    	Mockito.when(bc.getBundle()).thenReturn(b);
    	
    	// mock HTTP server
    	ServiceReference sr = Mockito.mock(ServiceReference.class);
    	HttpService hs = Mockito.mock(HttpService.class);
    	Mockito.when(bc.getServiceReference(HttpService.class.getName())).thenReturn(sr);
    	Mockito.when(bc.getService(sr)).thenReturn(hs);
    	
    	new AlitheiaCore(bc);
    	tm = new TestMetric(bc);
    }

    @Test
    public void testInstallRemove() throws MetricMismatchException, ClassNotFoundException {

    	// start a database session
    	AlitheiaCore.getInstance().getDBService().startDBSession();
    	
    	// create a measurement
    	DAObject measurement = new ProjectVersionMeasurement();
    	
    	// install the Test Metric
    	assertTrue(tm.install());
    	assertFalse(tm.install());
    	AlitheiaCore.getInstance().getDBService().commitDBSession();
    	
    	assertEquals(tm.getName(), "test");
    	assertEquals(tm.getVersion(), "1.42");
    	assertEquals(tm.getDescription(), "test description");
    	
    	// remove the Test Metric
    	AlitheiaCore.getInstance().getDBService().startDBSession();
    	assertTrue(tm.remove());
    	AlitheiaCore.getInstance().getDBService().commitDBSession();
    	assertTrue(tm.cleanup(null));
    }
    
    @Test
    public void testGetUniqueKey() {
    	assertEquals(tm.getUniqueKey(), "EF27AD95F3F2F5E104B5874FBA54D61B");
    }
    
    @Test
    public void testGetConfigurationSchema() {
    	// we have no configuration assigned with this plugin
    	assertEquals(tm.getConfigurationSchema().size(), 0);
    }
    
    @Test
    public void testGetDependencies() {
    	assertEquals(tm.getDependencies().size(), 0);
    }
    
    @Test
    public void testGetActivationTypes() {
    	Set<Class<? extends DAObject>> activations = tm.getActivationTypes();
    	assertEquals(activations.size(), 1);
    }
}