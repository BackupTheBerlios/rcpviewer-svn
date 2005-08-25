/**
 * 
 */
package de.berlios.rcpviewer.gui.jobs;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import de.berlios.rcpviewer.gui.util.ImageUtil;

/**
 * Reports a message - currently this simply places it on the status line.
 * @author Mike
 */
public class ReportJob extends AbstractUserJob {
	
	/* constants */
	
	public static final boolean INFO = false;
	public static final boolean ERROR = true;
	
	private static final Point STATUS_BAR_IMAGE_SIZE = new Point ( 16, 16 );
	
	
	/* static fields & methods */
	
	private static Image __defaultImage = null;
	private static Image __defaultErrorImage = null;
	private static IStatusLineManager __mgr = null;
	
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
	
	private static IStatusLineManager getStatusLine() {
		assert __mgr != null;
		return __mgr;
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
		
		if ( args == null ) {
			_msg = msg;
		}
		else {
			 ByteArrayOutputStream out = new ByteArrayOutputStream( msg.length() );
			 PrintStream ps = null;
			 try {
				 ps = new PrintStream( out );
				 ps.printf( msg, args );
				 ps.close();
				 ps = null;
				 _msg = new String( out.toByteArray() );
			 }
			 finally {
				 if ( ps != null ) ps.close();
			 }
		}
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
		// display
		if ( _isError ) {
			getStatusLine().setErrorMessage( image, _msg );
		}
		else {
			getStatusLine().setMessage( image, _msg );
		}
		return Status.OK_STATUS;
	}

}
