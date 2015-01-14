package eu.sqooss.test.service.fds;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Hashtable;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;

import eu.sqooss.core.AlitheiaCore;

public class CoreActivator {
	
	@Mock
	static BundleContext bc;
	static Bundle b; 
	static AlitheiaCore core;
	
	public CoreActivator() throws MalformedURLException{
		 b = Mockito.mock(Bundle.class);
		 Mockito.when(b.getResource("hibernate.cfg.xml")).thenReturn(new File("src/main/resources/hibernate.cfg.xml").toURI().toURL());
		 Hashtable<String, String> table = new Hashtable<String, String>();
		 table.put(org.osgi.framework.Constants.BUNDLE_NAME, "test");
		 table.put(org.osgi.framework.Constants.BUNDLE_VERSION, "1.42");
		 table.put(org.osgi.framework.Constants.BUNDLE_DESCRIPTION, "test description");
		 table.put(org.osgi.framework.Constants.BUNDLE_CONTACTADDRESS, "Piet Tester");
		 Mockito.when(b.getHeaders()).thenReturn(table);
		 bc = Mockito.mock(BundleContext.class);
		 Mockito.when(bc.getProperty("eu.sqooss.db")).thenReturn("mysql");
		 Mockito.when(bc.getProperty("eu.sqooss.db.host")).thenReturn("localhost");
		 Mockito.when(bc.getProperty("eu.sqooss.db.schema")).thenReturn("alitheia");
		 Mockito.when(bc.getProperty("eu.sqooss.db.user")).thenReturn("alitheia");
		 Mockito.when(bc.getProperty("eu.sqooss.db.passwd")).thenReturn("alitheia");
		 Mockito.when(bc.getProperty("eu.sqooss.db.conpool")).thenReturn("c3p0");
		 Mockito.when(bc.getBundle()).thenReturn(b);
		 
		 ServiceReference mockRef = Mockito.mock(ServiceReference.class);
		 Mockito.when(bc.getServiceReference(AlitheiaCore.class.getName())).thenReturn(mockRef);
		 
		 // mock HTTP server
		 ServiceReference sr = Mockito.mock(ServiceReference.class);
		 HttpService hs = Mockito.mock(HttpService.class);
		 Mockito.when(bc.getServiceReference(HttpService.class.getName())).thenReturn(sr);
		 Mockito.when(bc.getService(sr)).thenReturn(hs);
		core = new AlitheiaCore(bc);
		
		Mockito.when(bc.getService(mockRef)).thenReturn(core);
	}
	
	public Bundle getBundle(){
		return b;
	}
	
	public BundleContext getBundleContext(){
		return bc;
	}
	
	public AlitheiaCore getAlitheiaCore(){
		return core;
	}

}
