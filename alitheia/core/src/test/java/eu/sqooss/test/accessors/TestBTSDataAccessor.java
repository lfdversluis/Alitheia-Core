package eu.sqooss.test.accessors;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.sqooss.service.tds.AccessorException;
import eu.sqooss.service.tds.BTSAccessor;
import eu.sqooss.service.tds.BTSEntry;

public class TestBTSDataAccessor implements BTSAccessor {

	@Override
	public List<URI> getSupportedURLSchemes() {
		return new ArrayList<URI>();
	}

	@Override
	public void init(URI dataURL, String projectName) throws AccessorException {
		
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public BTSEntry getBug(String bugID) {
		return null;
	}

	@Override
	public List<String> getAllBugs() {
		return null;
	}

	@Override
	public List<String> getBugsNewerThan(Date d) {
		return null;
	}

}
