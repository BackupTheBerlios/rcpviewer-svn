package org.essentialplatform.louis.celleditors;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;

import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.domain.Domain;

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
			if ( boolean.class == clazz ) {
				editor = new CheckboxCellEditor();
			}
			else {
				return new PrimitiveCellEditor( clazz );
			}
		}
		else if ( Boolean.class == clazz ) {
			editor = new CheckboxCellEditor();
		}
		else if ( Byte.class == clazz ) {
			editor = new PrimitiveCellEditor( clazz );
		}
		else if ( Character.class == clazz ) {
			editor = new PrimitiveCellEditor( clazz );
		}
		else if ( Short.class == clazz ) {
			editor = new PrimitiveCellEditor( clazz );
		}	
		else if ( Integer.class == clazz ) {
			editor = new PrimitiveCellEditor( clazz );
		}
		else if ( Long.class == clazz ) {
			editor = new PrimitiveCellEditor( clazz );
		}	
		else if ( Float.class == clazz ) {
			editor = new PrimitiveCellEditor( clazz );
		}
		else if ( Double.class == clazz ) {
			editor = new PrimitiveCellEditor( clazz );
		}
		else if ( String.class == clazz ) {
			editor = new TextCellEditor();
		}
		else {
			IDomainClass domainClass = Domain.instance().lookupNoRegister( clazz );
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
