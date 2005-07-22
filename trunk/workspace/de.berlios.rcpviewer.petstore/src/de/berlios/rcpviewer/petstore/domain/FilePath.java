package de.berlios.rcpviewer.petstore.domain;

import java.io.File;

import de.berlios.rcpviewer.progmodel.extended.IStringParser;
import de.berlios.rcpviewer.progmodel.extended.MaxLengthOf;
import de.berlios.rcpviewer.progmodel.standard.Value;

/**
 * Custom value object that represents a relative or absolute file or directory.
 * @author Dan Haywood
 *
 * <p>
 * Using a custom value object offers more scope for customized UI.  For
 * example, the UI could provide a "Browse" button alongside any attributes.
 *
 * <p>
 * <i>
 * Programming Model notes: the annotations are effectively inherited by
 * any attribute of this value type.
 * </i>
 * 
 * <p>
 * <i>
 * Programming Model notes:
 * <ul>
 * <li> ... IStringParser ...
 * </ul>
 * </i>
 */
@MaxLengthOf(1024)
@Value
public final class FilePath implements IStringParser {

	/**
	 * Create a filepath object.
	 * 
	 * <p>
	 * Should be followed by a call to the <code>init()</code> method.
	 *  
	 * <p>
	 * <i>
	 * Programming Model notes: No-arg constructor is required by the 
	 * framework.
	 * </i>
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ... init ...
     * </ul>
     * </i>
	 *
	 */
	public FilePath() {
	}
	
	/**
	 * Configure this file path.
	 * 
	 * <p>
	 * Typically called within a <code>someOperationArgs()</code> method to
	 * specify the behaviour of a parameter, for example:
	 * <pre>
	 * public void uploadFile(final FilePath filePath) { 
	 *     ... 
	 * }
	 * public void uploadFileArgs(final FilePath[] filePath) {
	 *     <b>filePath[0].init("jpg", "gif", "png");</b>
	 * }
	 * </pre>
	 *
	 */
	public void init(String... filterList) {
		if (filterList == null || filterList.length == 0) {
			_directoryOnly = true;
			_filterList = new String[]{};
			return;
		}
		_directoryOnly = false;
		_filterList = filterList; 
	}
	
	
	/**
	 * Whether the file path should show only directories, not files.
	 * 
	 * @return
	 */
	public boolean isDirectoryOnly() {
		return _directoryOnly;
	}
	private boolean _directoryOnly;
	

	/**
	 * For file paths representing files rather than directories, provides
	 * an optional filtering of file extensions.
	 * 
	 * @return
	 */
	public String[] getFilterList() {
		return _filterList;
	}
	private String[] _filterList;

	
	/**
	 * Path to a valid file or directory, or <code>null</code> otherwise.
	 * 
	 * <p>
	 * There is no mutator directly.
	 * 
	 * @return
	 */
	public String getPath() {
		return _path;
	}
	private String _path;
	
	/**
	 * Implementation of {@link IStringParser} interface, checks that the
	 * input represents a readable file (with suffix matching the filter list) 
	 * or directory. 
     * 
	 * @param string
	 * @return
	 */
	public boolean isValidString(final String input) {
		File f = new File(input);
		if (!f.exists()) {
			return false;
		}
		if (!f.canRead()) {
			return false;
		}
		if (!isDirectoryOnly()) {
			if (f.isDirectory()) {
				return false;
			}
			boolean matchesFilter = false;
			for(String filter: _filterList) {
				if (f.getName().endsWith("." + filter)) {
					matchesFilter = true;
					break;
				}
			}
			if (!matchesFilter) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Implementation of {@link IStringParser} interface.
	 * 
	 * @param string
	 * @throws IllegalArgumentException iff <code>isValidString</code> would return <code>false</code>
	 */
	public void parseString(final String input) {
		if(!isValidString(input)) {
			throw new IllegalArgumentException("Invalid input ('" + input + "')");
		}
		_path = input;
	}
}
