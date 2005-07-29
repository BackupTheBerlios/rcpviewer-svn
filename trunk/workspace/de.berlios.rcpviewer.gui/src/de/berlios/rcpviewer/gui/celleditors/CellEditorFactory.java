package de.berlios.rcpviewer.gui.celleditors;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.RuntimeDomain;

/**
 * Static factory methods
 * @author Mike
 */
public class CellEditorFactory {
	
	/**
	 * Returns <code>CellEditor</code> appropriate for the passed class.
	 * <br>Note that the returned <code>CellEditor</code> must be attached
	 * to the parent control via the <code>create()</code> method.
	 * @param clazz
	 * @return
	 * @see org.eclipse.jface.viewers.CellEditor
	 */
	public static CellEditor getCellEditor( Class<?> clazz ) {
		if ( clazz == null ) throw new IllegalArgumentException();
		CellEditor editor = null;
		if ( clazz.isPrimitive() ) {
			if ( int.class == clazz ) {
				editor = new IntegerCellEditor();
			}
		}
		else if ( String.class == clazz ) {
			editor = new TextCellEditor();
		}
		else if ( Boolean.class == clazz ) {
			editor = new CheckboxCellEditor();
		}
		else if ( Integer.class == clazz ) {
			editor = new IntegerCellEditor();
		}
		else {
			IDomainClass domainClass = RuntimeDomain.instance().lookupNoRegister( clazz );
			assert domainClass != null;
			editor = new DomainObjectCellEditor( domainClass );
		}
		assert editor != null;
		return editor;
	}
	
	

	// prevent instantiation
	private CellEditorFactory() {
		super();
	}

}
