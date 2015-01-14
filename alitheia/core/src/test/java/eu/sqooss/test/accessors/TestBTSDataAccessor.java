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
		// TODO Auto-generated method stub
		return new ArrayList<URI>();
	}

	@Override
	public void init(URI dataURL, String projectName) throws AccessorException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BTSEntry getBug(String bugID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllBugs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getBugsNewerThan(Date d) {
		// TODO Auto-generated method stub
		return null;
	}

}
