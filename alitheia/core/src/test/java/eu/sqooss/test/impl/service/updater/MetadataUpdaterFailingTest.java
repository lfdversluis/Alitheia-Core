package eu.sqooss.test.impl.service.updater;

import eu.sqooss.service.db.StoredProject;
import eu.sqooss.service.logging.Logger;
import eu.sqooss.service.updater.MetadataUpdater;

public class MetadataUpdaterFailingTest implements MetadataUpdater {

	@Override
	public void setUpdateParams(StoredProject sp, Logger l) {
		
	}

	@Override
	public void update() throws Exception {
		
	}

	@Override
	public int progress() {
		return 0;
	}

}
