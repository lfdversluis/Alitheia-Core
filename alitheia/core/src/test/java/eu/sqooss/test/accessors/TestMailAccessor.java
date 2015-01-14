package eu.sqooss.test.accessors;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;

import eu.sqooss.service.tds.AccessorException;
import eu.sqooss.service.tds.MailAccessor;

public class TestMailAccessor implements MailAccessor {

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
	public String getRawMessage(String listname, String msgFileName)
			throws IllegalArgumentException, FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MimeMessage getMimeMessage(String listname, String msgFileName)
			throws IllegalArgumentException, FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getMessages(String listname)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getNewMessages(String listname)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getMessages(String listName, Date d1, Date d2)
			throws IllegalArgumentException, FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean markMessageAsSeen(String listname, String msgFileName)
			throws IllegalArgumentException, FileNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getMailingLists() {
		// TODO Auto-generated method stub
		return null;
	}

}
