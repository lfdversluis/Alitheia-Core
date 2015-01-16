package eu.sqooss.test.accessors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mockito.Mockito;

import eu.sqooss.service.tds.AccessorException;
import eu.sqooss.service.tds.AnnotatedLine;
import eu.sqooss.service.tds.CommitLog;
import eu.sqooss.service.tds.Diff;
import eu.sqooss.service.tds.InvalidProjectRevisionException;
import eu.sqooss.service.tds.InvalidRepositoryException;
import eu.sqooss.service.tds.Revision;
import eu.sqooss.service.tds.SCMAccessor;
import eu.sqooss.service.tds.SCMNode;
import eu.sqooss.service.tds.SCMNodeType;

public class TestSCMDataAccessor implements SCMAccessor {

	@Override
	public List<URI> getSupportedURLSchemes() {
		List<URI> a = new ArrayList<URI>();
		try {
			a.add(new URI("scm"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return a;
	}

	@Override
	public void init(URI dataURL, String projectName) throws AccessorException {
		
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Revision newRevision(Date d) {
		return Mockito.mock(Revision.class);
	}

	@Override
	public Revision newRevision(String uniqueId) {
		return Mockito.mock(Revision.class);
	}

	@Override
	public Revision getHeadRevision() throws InvalidRepositoryException {
		return null;
	}

	@Override
	public Revision getFirstRevision() throws InvalidRepositoryException {
		return null;
	}

	@Override
	public Revision getNextRevision(Revision r)
			throws InvalidProjectRevisionException {
		return null;
	}

	@Override
	public Revision getPreviousRevision(Revision r)
			throws InvalidProjectRevisionException {
		return null;
	}

	@Override
	public boolean isValidRevision(Revision r) {
		return false;
	}

	@Override
	public void getCheckout(String repoPath, Revision revision, File localPath)
			throws InvalidProjectRevisionException, InvalidRepositoryException,
			FileNotFoundException {
		
	}

	@Override
	public void updateCheckout(String repoPath, Revision src, Revision dst,
			File localPath) throws InvalidProjectRevisionException,
			InvalidRepositoryException, FileNotFoundException {
		
	}

	@Override
	public void getFile(String repoPath, Revision revision, File localPath)
			throws InvalidProjectRevisionException, InvalidRepositoryException,
			FileNotFoundException {
		
	}

	@Override
	public void getFile(String repoPath, Revision revision, OutputStream stream)
			throws InvalidProjectRevisionException, InvalidRepositoryException,
			FileNotFoundException {
		
	}

	@Override
	public CommitLog getCommitLog(String repoPath, Revision r1, Revision r2)
			throws InvalidProjectRevisionException, InvalidRepositoryException {
		return null;
	}

	@Override
	public Diff getDiff(String repoPath, Revision r1, Revision r2)
			throws InvalidProjectRevisionException, InvalidRepositoryException,
			FileNotFoundException {
		return null;
	}

	@Override
	public SCMNodeType getNodeType(String repoPath, Revision r)
			throws InvalidRepositoryException {
		return null;
	}

	@Override
	public List<SCMNode> listDirectory(SCMNode dir)
			throws InvalidRepositoryException, InvalidProjectRevisionException {
		return null;
	}

	@Override
	public SCMNode getNode(String path, Revision r)
			throws InvalidRepositoryException, InvalidProjectRevisionException {
		return null;
	}

	@Override
	public List<AnnotatedLine> getNodeAnnotations(SCMNode s) {
		return null;
	}

}
