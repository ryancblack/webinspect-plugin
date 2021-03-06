package me.automationdomination.plugins.webinspect.service.ssc;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;



/**
 * Created with IntelliJ IDEA. User: bspruth Date: 8/17/14 Time: 10:18 PM To
 * change this template use File | Settings | File Templates.
 */
public class SscServiceImpl implements SscService {
	
	private static final int PROJECT_ID_INDEX = 0;
	private static final int PROJECT_NAME_INDEX = 1;
	private static final int PROJECT_VERSION_INDEX = 2;
	
	private static Logger logger = Logger.getLogger(SscServiceImpl.class.getName());
	
	private final SscServer sscServer;

	public SscServiceImpl(final SscServer sscServer) {
		super();
		this.sscServer = sscServer;
	}

	@Override
	public Map<Integer, String> retrieveProjects() {
		logger.fine("retrieving projects");
		
		final HashMap<Integer, String> projects = new HashMap<Integer, String>();
		
		// retrieve the projects data from the server
		final String projectsString = sscServer.retrieveProjects();

		// tokenize the projects output
		final StringTokenizer projectsStringTokenizer = new StringTokenizer(projectsString, "\n");
		
		while (projectsStringTokenizer.hasMoreTokens()) {
			final String line = projectsStringTokenizer.nextToken();
			
			final String[] values = line.split(",");
			
			// we assume a length of three means we have a line with real
			// projects listed
			if (values.length == 3) {
				try {
					final Integer projectID = Integer.parseInt(values[PROJECT_ID_INDEX]);
					final String projectName = values[PROJECT_NAME_INDEX];
					final String projectVersion = values[PROJECT_VERSION_INDEX];
					final String fullProjectName = projectName + " (" + projectVersion + ")";
					
					projects.put(projectID, fullProjectName);
				} catch (final NumberFormatException e) {
					logger.warning("skipping line <" + line + "> with unexpected project id value");
				}
			} else {
				logger.warning("skipping line <" + line + "> with unexpected number of tokens");
			}
		}
		
		return projects;
	}

    @Override
    public String uploadFpr(String fprScanFile, String projectVersionID) {
        return null;
    }
}
