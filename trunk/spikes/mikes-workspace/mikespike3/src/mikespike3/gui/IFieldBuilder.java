package mikespike3.gui;

import org.eclipse.swt.widgets.Composite;

/**
 * Type for building individual fields within a gui.
 * @author Mike
 */
public interface IFieldBuilder {
	
	public boolean isApplicable( Class clazz, Object value );

	public void createGui( Composite parent, Object value );
}
