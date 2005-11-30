/**
 * 
 */
package org.essentialplatform.louis.jobs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.util.ImageUtil;
import org.essentialplatform.louis.util.PlatformUtil;
import org.essentialplatform.louis.util.StringUtil;


/**
 * Reports a message - currently this simply places it on the status line.
 * @author Mike
 */
public class ReportJob extends AbstractUserJob {
	
	/* constants */
	
	public static final boolean INFO = false;
	public static final boolean ERROR = true;
	
	private static final Point STATUS_BAR_IMAGE_SIZE = new Point ( 16, 16 );
	private static final DateFormat TIME_FORMATTER
		= new SimpleDateFormat( "HH:mm:ss" ); //$NON-NLS-1$
	
	
	/* static fields & methods */
	
	private static Image __defaultImage = null;
	private static Image __defaultErrorImage = null;
	private static IStatusLineManager __mgr = null;
	private static MessageConsoleStream __infoConsole = null;
	private static MessageConsoleStream __errorConsole = null;
	
	/**
	 * Clunky // FIXME
	 * @param mgr
	 */
	public static void setStatusLineManager( IStatusLineManager mgr ) {
		if( mgr == null ) throw new IllegalArgumentException();
		__mgr = mgr;
	}
	
	private static Image getDefaultImage( boolean isError ) {
		if ( isError ) {
			if ( __defaultErrorImage == null ) {
				__defaultErrorImage = ImageUtil.resize(
						Display.getCurrent().getSystemImage( SWT.ICON_ERROR ),
						STATUS_BAR_IMAGE_SIZE );
			}
			return __defaultErrorImage;
		}
		else {
			if ( __defaultImage == null ) {
				__defaultImage = ImageUtil.resize(
						Display.getCurrent().getSystemImage( SWT.ICON_INFORMATION ),
						STATUS_BAR_IMAGE_SIZE );
			}
			return __defaultImage;
		}	
	}
	
	/* instance fields & methods */
	
	private final String _msg;
	private final Image _image;
	private final boolean _isError;

	/**
	 * The last arg is to allow <code>printf</code>-style formatting.  If the
	 * var arg is null then the message is simply the msg argument.  If the
	 * var arg is not null then a <code>printf</code> is carried out using
	 * the msg arg as the format.
	 * @param msg to display - cannot be <code>null</code>
	 * @param image - can be null
	 * @param isError
	 * @param varg args - args
	 * @see PrintStream#printf(java.lang.String, java.lang.Object[]);
	 */
	public ReportJob( String msg, Image image, boolean isError, Object...args ) {
		super( ReportJob.class.getName() );
		if ( msg == null ) throw new IllegalArgumentException();
		_image = null;
		_isError = isError;
		_msg = StringUtil.printf( msg, args );
	}
	
	/**
	 */
	public ReportJob( String msg, Image image, Throwable ex ) {
		super( ReportJob.class.getName() );
		if ( msg == null ) throw new IllegalArgumentException();
		_image = null;
		_isError = true;
		StringBuilder buf = new StringBuilder();
		StackTraceElement[] elements = ex.getStackTrace(); 
		for(int i=0; i<elements.length; i++ ) {
			 buf.append(elements[i]);
			 if (i != elements.length-1) {
				 buf.append("\n           ");
			 }
		}
		_msg = buf.toString();
	}
	
	/**
	 * @param msg to display - cannot be <code>null</code>
	 * @param isError
	 */
	public ReportJob( String msg, Image image, boolean isError ) {
		this( msg, image, isError, (Object[])null );
	}
	
	/**
	 * @param msg to display - cannot be <code>null</code>
	 * @param isError
	 */
	public ReportJob( String msg, boolean isError, Object...args ) {
		this( msg, null, isError, args );
	}
	
	/**
	 * @param msg to display - cannot be <code>null</code>
	 * @param isError
	 */
	public ReportJob( String msg, boolean isError ) {
		this( msg, null, isError, (Object[])null );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {	
		// ensure there is an image
		Image image;
		if( _image == null ) {
			image = getDefaultImage( _isError );
		}
		else {
			image = ImageUtil.resize( _image, STATUS_BAR_IMAGE_SIZE );
		}
		
		// display on status line
		if ( _isError ) {
			getStatusLine().setErrorMessage( image, _msg );
		}
		else {
			getStatusLine().setMessage( image, _msg );
		}
		
		// output to console if visible
		if ( PlatformUtil.getView(  IConsoleConstants.ID_CONSOLE_VIEW ) != null ) {
			StringBuffer sb = new StringBuffer();
			sb.append( TIME_FORMATTER.format( new Date() ) );
			sb.append( " : " ); //$NON-NLS-1$
			sb.append( _msg );
			if ( _isError ) {
				getErrorConsole().println( sb.toString() );
			}
			else {
				getInfoConsole().println( sb.toString() );
			}
		}
		
		return Status.OK_STATUS;
	}
	
	/* private utility methods */
	
	private IStatusLineManager getStatusLine() {
		assert __mgr != null;
		return __mgr;
	}
		
	private MessageConsoleStream getInfoConsole() {
		if ( __infoConsole == null ) {
			initiateConsoles();
		}
		assert __infoConsole != null;
		return __infoConsole;
	}
	
	private MessageConsoleStream getErrorConsole() {
		if ( __errorConsole == null ) {
			initiateConsoles();
		}
		assert __errorConsole != null;
		return __errorConsole;
	}
	
	private void initiateConsoles() {
		assert __infoConsole == null;
		assert __errorConsole == null;
		IConsoleManager mgr = ConsolePlugin.getDefault().getConsoleManager();
		MessageConsole historyConsole = new MessageConsole( 
				LouisPlugin.getResourceString("ReportJob.ConsoleHistory" ), //$NON-NLS-1$
				null ) ;
		mgr.addConsoles( new IConsole[]{ historyConsole } );
		mgr.showConsoleView( historyConsole );
		__infoConsole = historyConsole.newMessageStream();
		__infoConsole.setColor( 
				Display.getCurrent().getSystemColor( SWT.COLOR_BLUE ) );
		__errorConsole = historyConsole.newMessageStream();
		__errorConsole.setColor( 
				Display.getCurrent().getSystemColor( SWT.COLOR_RED ) );
	}

}
