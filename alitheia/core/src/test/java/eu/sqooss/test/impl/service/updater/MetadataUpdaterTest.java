package eu.sqooss.test.impl.service.updater;

import eu.sqooss.service.db.StoredProject;
import eu.sqooss.service.logging.Logger;
import eu.sqooss.service.updater.MetadataUpdater;
import eu.sqooss.service.updater.Updater;
import eu.sqooss.service.updater.UpdaterService.UpdaterStage;

@Updater(descr = "Test updater", 
mnem = "MLTHREAD",
stage = UpdaterStage.INFERENCE)
public class MetadataUpdaterTest implements MetadataUpdater {

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
