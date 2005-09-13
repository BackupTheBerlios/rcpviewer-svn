/**
 * 
 */
package de.berlios.rcpviewer.gui.editors.parts;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * @author Mike
 *
 */
class CollectionTableLabelProvider extends LabelProvider implements
		ITableLabelProvider {
	
	private final  List<EAttribute> _attributes;
	private final ILabelProvider _delegate;
	
	CollectionTableLabelProvider( List<EAttribute> attributes ) {
		assert attributes != null;
		_attributes = attributes;
		_delegate = GuiPlugin.getDefault().getLabelProvider();
	}

	/**
	 * Always <code>null</code>
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		if ( element == null ) throw new IllegalArgumentException();
		if ( !(element instanceof IDomainObject ) ) throw new IllegalArgumentException();
		if ( columnIndex == 0 ) {
			return _delegate.getText( element );
		}
		else {
			return _delegate.getText(
					((IDomainObject<?>)element).getAttribute( 
							_attributes.get( --columnIndex ) ).get() );
		}
	}

}
