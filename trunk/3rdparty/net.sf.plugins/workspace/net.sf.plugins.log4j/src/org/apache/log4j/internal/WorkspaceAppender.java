package org.apache.log4j.internal;

import org.apache.log4j.RollingFileAppender;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;

/**
 * This appender writes log messaged into a file named .log4j which is
 * located in the current application's workspace directory.
 *
 * @author ted stockwell
 */
public class WorkspaceAppender extends RollingFileAppender {

	public WorkspaceAppender() {
		IPath logPath = Platform.getLogFileLocation();
		logPath = logPath.removeLastSegments(1);
		logPath = logPath.append(".log4j");
		logPath = logPath.makeAbsolute();
		String sPath = logPath.toString();
		setFile(sPath);
	}

}
