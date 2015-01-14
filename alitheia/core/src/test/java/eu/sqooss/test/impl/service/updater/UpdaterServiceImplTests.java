package eu.sqooss.test.impl.service.updater;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import eu.sqooss.core.AlitheiaCore;
import eu.sqooss.impl.service.tds.DataAccessorFactory;
import eu.sqooss.impl.service.updater.UpdaterServiceImpl;
import eu.sqooss.service.db.StoredProject;
import eu.sqooss.service.scheduler.Job;
import eu.sqooss.service.scheduler.Job.State;
import eu.sqooss.service.updater.UpdaterService.UpdaterStage;
import eu.sqooss.test.accessors.TestBTSDataAccessor;
import eu.sqooss.test.accessors.TestMailAccessor;
import eu.sqooss.test.accessors.TestSCMDataAccessor;
import eu.sqooss.test.service.fds.CoreActivator;

public class UpdaterServiceImplTests {
	
	private static AlitheiaCore core;
	private static UpdaterServiceImpl updater;
	
	@BeforeClass
    public static void setUp() throws MalformedURLException {
    	core = new CoreActivator().getAlitheiaCore();
    	updater = (UpdaterServiceImpl) core.getUpdater();
    	
    	DataAccessorFactory.addImplementation("scm", TestSCMDataAccessor.class);
		DataAccessorFactory.addImplementation("bts", TestBTSDataAccessor.class);
		DataAccessorFactory.addImplementation("mail", TestMailAccessor.class);
    }

	@Test
    public void testRegisterUnregisterUpdaterService() {
		
		updater.registerUpdaterService(MetadataUpdaterTest.class);
		updater.registerUpdaterService(MetadataUpdaterFailingTest.class);
		updater.unregisterUpdaterService(MetadataUpdaterTest.class);
		updater.unregisterUpdaterService(MetadataUpdaterFailingTest.class);
	}
	
	@Test
	public void testUpdate() {
		
		core.getTDSService().addAccessor(23, "test", "bts://google.nl", "mail://test@test.nl", "scm://google.nl");
		StoredProject p = Mockito.mock(StoredProject.class);
		Mockito.when(p.getId()).thenReturn((long) 23);
		assertFalse(updater.update(null));
		assertTrue(updater.update(p, UpdaterStage.INFERENCE));
		assertFalse(updater.update(p, "test"));
		assertTrue(updater.update(p));
	}
	
	@Test
	public void testIsUpdateRunning() {
		StoredProject p = Mockito.mock(StoredProject.class);
		assertFalse(updater.isUpdateRunning(p, null));
	}
}
