package eu.sqooss.impl.service.fds;

import eu.sqooss.core.AlitheiaCore;
import eu.sqooss.service.db.ProjectFile;
import eu.sqooss.service.db.ProjectVersion;
import eu.sqooss.service.logging.Logger;
import eu.sqooss.service.tds.InvalidAccessorException;
import eu.sqooss.service.tds.PathChangeType;
import eu.sqooss.service.tds.Revision;
import eu.sqooss.service.tds.SCMAccessor;
import eu.sqooss.service.tds.TDSService;

/**
 * This class can be queried for functionality regarding Revisions.
 * While performing functions it will write to the console using a Logger object.
 */
public class RevisionManager {
	
	private Logger logger;
	
	/**
	 * The constructor of the RevisionManager class.
	 * @param logger The logger to write lines to the console, 
	 * allowing for debugging or monitoring.
	 */
	public RevisionManager(Logger logger){
		this.logger = logger;
	}
	
	/**
     * Convert between database and SCM revision representations
     * 
     * @param pv The ProjectVersion of the project
     * 
     * @return A new Revision of the project.
     */
    public Revision projectVersionToRevision(ProjectVersion pv) {
        TDSService tds = AlitheiaCore.getInstance().getTDSService();
        SCMAccessor scm = null;

        if (tds.accessorExists(pv.getProject().getId())) {
            scm = (SCMAccessor) tds.getAccessor(pv.getProject().getId());
        } else {
            return null;
        }

        return scm.newRevision(pv.getRevisionId());
    }
    
    /**
     * For a project file, return the SCM revision that it refers to.
     * 
     * @param pf The ProjectFile to look up.
     * @param tds The TDSService to query project data.
     * 
     * @return The SCM revision for the project or null if the project file is
     *         deleted or otherwise unavailable.
     */
    public Revision projectFileRevision(ProjectFile pf, TDSService tds) {
        // Make sure that the file exists in the specified project version
        String fileStatus = pf.getState().toString();
        if (PathChangeType.valueOf(fileStatus) == PathChangeType.DELETED) {
            return null;
        }

        String projectVersion = pf.getProjectVersion().getRevisionId();
        long projectId = pf.getProjectVersion().getProject().getId();
        try {
            return tds.getAccessor(projectId).getSCMAccessor().newRevision(
                    projectVersion);
        } catch (InvalidAccessorException e) {
            logger.error("Invalid SCM accessor for project "
                    + pf.getProjectVersion().getProject().getName() + " "
                    + e.getMessage());
            return null;
        }
    }

}
