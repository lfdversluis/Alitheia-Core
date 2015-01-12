package eu.sqooss.impl.service.fds.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import org.apache.velocity.texen.util.FileUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import eu.sqooss.impl.service.fds.FDSServiceImpl;
import eu.sqooss.impl.service.fds.RevisionManager;
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

public class FDSServiceImplTests {
	
	static FDSServiceImpl impl;
	
	@Mock
	static BundleContext bc;
	static Bundle b; 
	static Logger l;
	
	CoreActivator act;
	
	@Before
    public void setUp() throws MalformedURLException{
		
		// Start the core
		act = new CoreActivator();
		b = act.getBundle();
		bc = act.getBundleContext();
		l = Mockito.mock(Logger.class);
		
        impl = new FDSServiceImpl();
        impl.setInitParams(bc, l);
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
	public void TestGetFileSuccess() throws InvalidAccessorException {
		ProjectFile pf = Mockito.mock(ProjectFile.class);
		ProjectFileState pfs = Mockito.mock(ProjectFileState.class);
		Revision r = Mockito.mock(Revision.class);
		TDSService tds = Mockito.mock(TDSService.class);
		ProjectAccessor pa = Mockito.mock(ProjectAccessor.class);
		StoredProject sp = Mockito.mock(StoredProject.class);
		ProjectVersion pv = Mockito.mock(ProjectVersion.class);
		SCMAccessor sa = Mockito.mock(SCMAccessor.class);
		
		Mockito.when(pf.getState()).thenReturn(pfs);
		Mockito.when(pfs.toString()).thenReturn("ADDED");
		Mockito.when(pf.getProjectVersion()).thenReturn(pv);
		Mockito.when(pv.getRevisionId()).thenReturn("1337");
		Mockito.when(pv.getProject()).thenReturn(sp);
		Mockito.when(sp.getId()).thenReturn(0l);
		Mockito.when(tds.getAccessor(0)).thenReturn(pa);
		Mockito.when(pa.getSCMAccessor()).thenReturn(sa);
		Mockito.when(sa.newRevision("1337" )).thenReturn(r);
		
		RevisionManager rm = new RevisionManager(l);
		
		assertEquals(r, rm.projectFileRevision(pf, tds));
	}
	
	@Test
	public void TestUpdateCheckoutCNull() throws CheckoutException{
		ProjectVersion pv = new ProjectVersion();
		
		boolean result = impl.updateCheckout(null, pv);
		assertFalse(result);
	}
	
	@Test(expected=NullPointerException.class)
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

}
