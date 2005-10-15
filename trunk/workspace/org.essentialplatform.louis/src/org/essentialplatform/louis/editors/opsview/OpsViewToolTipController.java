/**
 * 
 */
package org.essentialplatform.louis.editors.opsview;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.essentialplatform.louis.LouisPlugin;


/**
 * Controls tooltip display
 * @author Mike
 */
class OpsViewToolTipController extends MouseTrackAdapter {
	
	private final Tree _tree;

	/**
	 * @param viewer
	 */
	OpsViewToolTipController( TreeViewer viewer ) {
		super();
		assert viewer != null;
		_tree = viewer.getTree();
		_tree.addMouseTrackListener( this );
		_tree.setToolTipText( "" ); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseTrackAdapter#mouseHover(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseHover(MouseEvent event) {
		assert event != null;
		TreeItem item = _tree.getItem( new Point( event.x, event.y ) );
		String tooltip;
		if ( item == null ) {
			tooltip = ""; //$NON-NLS-1$
		}
		else {
			Object obj = item.getData();
			if ( obj == null ) {
				tooltip = ""; //$NON-NLS-1$
			}
			else if ( obj instanceof OpsViewActionProxy ) {
				if ( ((OpsViewActionProxy)obj).isValid() ) {
					tooltip = LouisPlugin.getResourceString(
								"OpsViewToolTipController.ValidAction" );  //$NON-NLS-1$
					}
					else {
						tooltip = LouisPlugin.getResourceString(
								"OpsViewToolTipController.InvalidAction" ); //$NON-NLS-1$
					}
			}
			else if ( obj instanceof OpsViewParameterProxy ) {
				if ( ((OpsViewParameterProxy)obj).isValid() ) {
					tooltip = LouisPlugin.getResourceString(
							"OpsViewToolTipController.ValidParam" );  //$NON-NLS-1$
				}
				else {
					tooltip = LouisPlugin.getResourceString(
							"OpsViewToolTipController.InvalidParam" ); //$NON-NLS-1$
				}
			}
			else {
				tooltip = ""; //$NON-NLS-1$
			}
		}
		_tree.setToolTipText( tooltip ) ;
	}
}
