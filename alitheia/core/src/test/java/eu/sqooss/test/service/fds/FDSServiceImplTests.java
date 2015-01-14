package eu.sqooss.test.service.fds;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.velocity.texen.util.FileUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import eu.sqooss.core.AlitheiaCore;
import eu.sqooss.impl.service.fds.FDSServiceImpl;
import eu.sqooss.impl.service.tds.DataAccessorFactory;
import eu.sqooss.service.db.ProjectFile;
import eu.sqooss.service.db.ProjectFileState;
import eu.sqooss.service.db.ProjectVersion;
import eu.sqooss.service.db.StoredProject;
import eu.sqooss.service.fds.CheckoutException;
import eu.sqooss.service.fds.OnDiskCheckout;
import eu.sqooss.service.fds.Timeline;
import eu.sqooss.service.logging.Logger;
import eu.sqooss.service.tds.InvalidAccessorException;
import eu.sqooss.service.tds.ProjectAccessor;
import eu.sqooss.service.tds.Revision;
import eu.sqooss.service.tds.SCMAccessor;
import eu.sqooss.service.tds.TDSService;
import eu.sqooss.test.accessors.TestSCMDataAccessor;

public class FDSServiceImplTests {

	static FDSServiceImpl impl;

	@Mock
	static BundleContext bc;
	static Bundle b; 
	static Logger l;
	ProjectFile pf;
	ProjectFileState pfs;
	StoredProject sp;
	ProjectVersion pv;
	TDSService tds;
	ProjectAccessor pa;
	Revision r;
	SCMAccessor sa;

	CoreActivator act;

	@Before
	public void setUp() throws MalformedURLException, InvalidAccessorException{

		// Start the core
		act = new CoreActivator();
		b = act.getBundle();
		bc = act.getBundleContext();
		l = Mockito.mock(Logger.class);

		impl = new FDSServiceImpl();
		impl.setInitParams(bc, l);

		// Set all the required items for mocking
		pf = Mockito.mock(ProjectFile.class);
		pfs = Mockito.mock(ProjectFileState.class);
		sp = Mockito.mock(StoredProject.class);
		pv = Mockito.mock(ProjectVersion.class);
		tds = Mockito.mock(TDSService.class);
		pa = Mockito.mock(ProjectAccessor.class);
		r = Mockito.mock(Revision.class);
		sa = Mockito.mock(SCMAccessor.class);
		
		Mockito.when(pf.getState()).thenReturn(pfs);
		Mockito.when(pfs.toString()).thenReturn("ADDED");
		Mockito.when(pf.getProjectVersion()).thenReturn(pv);
		Mockito.when(pv.getRevisionId()).thenReturn("1337");
		Mockito.when(pv.getProject()).thenReturn(sp);
		Mockito.when(sp.getId()).thenReturn(0l);
		Mockito.when(tds.getAccessor(0)).thenReturn(pa);
		Mockito.when(pa.getSCMAccessor()).thenReturn(sa);
		Mockito.when(sa.newRevision("1337" )).thenReturn(r);
	}

	@After
	public void TearDown(){
		impl = null;
	}

	@Test
	public void TestmplNotNull(){
		assertNotNull(impl);
	}

	@Test
	public void TeststartUpSuccessful(){ 
		assertTrue(impl.startUp());
		// and shut down again
		impl.shutDown();
	}
	
	@Test
	public void ShutDownClean(){
		Mockito.when(bc.getProperty("eu.sqooss.fds.cleanupOnExit")).thenReturn("true");
		
		impl.shutDown();
	}

	@Test
	public void TestRun(){
		impl.run();
	}

	@Test
	public void TestTimeLineImplSucces(){
		StoredProject c = new StoredProject("testProject");
		assertNotNull(c);

		Timeline testTimeLine = impl.getTimeline(c);
		assertNotNull(testTimeLine);
	}

	@Test
	public void TestGetFileFailure(){
		ProjectFile pf = Mockito.mock(ProjectFile.class);
		ProjectFileState pfs = Mockito.mock(ProjectFileState.class);
		Mockito.when(pf.getState()).thenReturn(pfs);
		Mockito.when(pfs.toString()).thenReturn("DELETED");
		pf.setState(pfs);

		assertNull(impl.getFile(pf));
	}


	@Test
	public void TestUpdateCheckoutCNull() throws CheckoutException{
		ProjectVersion pv = new ProjectVersion();

		boolean result = impl.updateCheckout(null, pv);
		assertFalse(result);
	}

	@Test (expected=NullPointerException.class)
	public void TestReleaseCheckoutFailure(){
		OnDiskCheckout odc = Mockito.mock(OnDiskCheckout.class);
		impl.releaseCheckout(odc);
	}

	@Test
	public void TestReleaseCheckoutSuccess() throws FileNotFoundException, CheckoutException{
		OnDiskCheckout odc = Mockito.mock(OnDiskCheckout.class);
		String path = FileUtil.mkdir("test");
		File dir = new File(path);
		Mockito.when(odc.getRoot()).thenReturn(dir);

		assertEquals(dir, odc.getRoot());

		impl.releaseCheckout(odc);
		assertFalse(dir.exists());
	}
	
	// Test that invoke test functions in the fdsserviceimpl class

	@Test (expected=CheckoutException.class)
	public void TestGetInMemoryCheckoutNoAccessor() throws CheckoutException, InvalidAccessorException{

		// add data accessor for http scheme
		DataAccessorFactory.addImplementation("http", TestSCMDataAccessor.class);
		AlitheiaCore.getInstance().getTDSService().addAccessor(pv.getProject().getId(), "test", "test", "test", "http://google.nl");

		Mockito.when(tds.projectExists(0)).thenReturn(true);
		Mockito.when(tds.accessorExists(0)).thenReturn(false);

		impl.setTDS(tds);	

		assertNull(impl.getInMemoryCheckout(pv));
	}

	@Test (expected=CheckoutException.class)
	public void TestGetInMemoryCheckoutProjectNotExisting() throws CheckoutException, InvalidAccessorException{

		// add data accessor for http scheme
		DataAccessorFactory.addImplementation("http", TestSCMDataAccessor.class);
		AlitheiaCore.getInstance().getTDSService().addAccessor(pv.getProject().getId(), "test", "test", "test", "http://google.nl");

		Mockito.when(tds.projectExists(0)).thenReturn(false);

		impl.setTDS(tds);	

		assertNull(impl.getInMemoryCheckout(pv));
	}
	
	@Test
	public void TestGetInMemoryCheckoutProjectSuccess() throws CheckoutException, InvalidAccessorException{

		// add data accessor for http scheme
		DataAccessorFactory.addImplementation("http", TestSCMDataAccessor.class);
		AlitheiaCore.getInstance().getTDSService().addAccessor(pv.getProject().getId(), "test", "test", "test", "http://google.nl");

		ProjectAccessor a = Mockito.mock(ProjectAccessor.class);
		SCMAccessor s = Mockito.mock(SCMAccessor.class);
		
		Mockito.when(tds.projectExists(0)).thenReturn(true);
		Mockito.when(tds.accessorExists(0)).thenReturn(true);
		Mockito.when(tds.getAccessor(0)).thenReturn(a);
		Mockito.when(a.getSCMAccessor()).thenReturn(s);

		impl.setTDS(tds);	

		assertNotNull(impl.getInMemoryCheckout(pv));
	}
	
	@Test
	public void TestGetCheckoutSuccess() throws CheckoutException{
		DataAccessorFactory.addImplementation("http", TestSCMDataAccessor.class);
		AlitheiaCore.getInstance().getTDSService().addAccessor(pv.getProject().getId(), "test", "test", "test", "http://google.nl");

		Mockito.when(tds.projectExists(0)).thenReturn(true);
		Mockito.when(tds.accessorExists(0)).thenReturn(true);
		Mockito.when(pv.getProject().getName()).thenReturn("testProject");
		
		impl.setTDS(tds);
		
		assertNotNull(impl.getCheckout(pv, ""));
	}
	
	@Test
	public void TestUpdateCheckoutFalse() throws CheckoutException {
		OnDiskCheckout c = Mockito.mock(OnDiskCheckout.class);
		
		ConcurrentHashMap<OnDiskCheckout, Integer> map = new ConcurrentHashMap<OnDiskCheckout, Integer>();
		map.put(c, 30);
		
		impl.setCheckoutHandles(map);
		
		assertFalse(impl.updateCheckout(c, pv));
	}

	@Test
	public void TestGetFileNull() throws InvalidAccessorException {
		// add data accessor for http scheme
		DataAccessorFactory.addImplementation("http", TestSCMDataAccessor.class);
		AlitheiaCore.getInstance().getTDSService().addAccessor(pv.getProject().getId(), "test", "test", "test", "http://google.nl");

		impl.setTDS(tds);

		assertNull(impl.getFile(pf));
	}
	
	@Test
	public void TestCreateNewRevision() throws CheckoutException{
		DataAccessorFactory.addImplementation("http", TestSCMDataAccessor.class);
		AlitheiaCore.getInstance().getTDSService().addAccessor(pv.getProject().getId(), "test", "test", "test", "http://google.nl");

		Mockito.when(tds.projectExists(0)).thenReturn(false);

		impl.setTDS(tds);	
		
		assertNotNull(impl.createNewRevision(pv));
	}
	
	@Test
	public void TestGetFileContentsSuccess() {
		DataAccessorFactory.addImplementation("http", TestSCMDataAccessor.class);
		AlitheiaCore.getInstance().getTDSService().addAccessor(pv.getProject().getId(), "test", "test", "test", "http://google.nl");

		impl.setTDS(tds);
		
		Mockito.when(pf.getFileName()).thenReturn("test.txt");
		
		assertNotNull(impl.getFileContents(pf));
	}

}
