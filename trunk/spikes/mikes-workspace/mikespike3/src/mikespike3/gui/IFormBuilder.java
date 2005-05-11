package mikespike3.gui;

import org.eclipse.swt.widgets.Composite;

/**
 * Type for building editor gui structures.
 * @author Mike
 */
public interface IFormBuilder {

	public boolean isApplicable( Class clazz, Object value );

	public void createGui( Composite parent, Object instance );
}
