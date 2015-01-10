package eu.sqooss.impl.service.fds.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import eu.sqooss.core.AlitheiaCore;
import eu.sqooss.impl.service.fds.RevisionManager;
import eu.sqooss.service.db.ProjectFile;
import eu.sqooss.service.db.ProjectFileState;
import eu.sqooss.service.db.ProjectVersion;
import eu.sqooss.service.db.StoredProject;
import eu.sqooss.service.logging.Logger;
import eu.sqooss.service.tds.InvalidAccessorException;
import eu.sqooss.service.tds.ProjectAccessor;
import eu.sqooss.service.tds.Revision;
import eu.sqooss.service.tds.SCMAccessor;
import eu.sqooss.service.tds.TDSService;

public class RevisionManagerTests {
	
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
    }
	
	@Test
	public void TestProjectVersionToRevisionFailure(){
		RevisionManager rm = new RevisionManager(l);
		ProjectVersion pv = Mockito.mock(ProjectVersion.class);
		TDSService tds = Mockito.mock(TDSService.class);
		StoredProject sp = Mockito.mock(StoredProject.class);

		Mockito.when(pv.getProject()).thenReturn(sp);
		Mockito.when(sp.getId()).thenReturn(0l);
		Mockito.when(tds.accessorExists(pv.getProject().getId())).thenReturn(false);
		
		assertNull(rm.projectVersionToRevision(pv));
	}
	
	@Test
	public void TestProjectVersionToRevisionSuccess(){
		AlitheiaCore core = act.getAlitheiaCore();
		RevisionManager rm = new RevisionManager(l);
		ProjectVersion pv = Mockito.mock(ProjectVersion.class);
		TDSService tds = Mockito.mock(TDSService.class);
		StoredProject sp = Mockito.mock(StoredProject.class);
		Revision r = Mockito.mock(Revision.class);
		SCMAccessor scm = Mockito.mock(SCMAccessor.class);

		Mockito.when(AlitheiaCore.getInstance()).thenReturn(core);
		Mockito.when(core.getTDSService()).thenReturn(tds);
		Mockito.when(pv.getProject()).thenReturn(sp);
		Mockito.when(sp.getId()).thenReturn(0l);
		Mockito.when(tds.accessorExists(0l)).thenReturn(true);
		Mockito.when(scm.newRevision(pv.getRevisionId())).thenReturn(r);
		
		assertEquals(r, rm.projectVersionToRevision(pv));
	}
	
	@Test
	public void TestProjectFileRevisionPathTypeDeleted(){
		ProjectFile pf = Mockito.mock(ProjectFile.class);
		TDSService tds = Mockito.mock(TDSService.class);
		ProjectFileState pfs = Mockito.mock(ProjectFileState.class);
		Mockito.when(pf.getState()).thenReturn(pfs);
		Mockito.when(pfs.toString()).thenReturn("DELETED");
		pf.setState(pfs);
		RevisionManager rm = new RevisionManager(l);
		
		assertNull(rm.projectFileRevision(pf, tds));
	}
	
	@Test
	public void TestProjectFileRevisionSuccess() throws InvalidAccessorException{
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

}
