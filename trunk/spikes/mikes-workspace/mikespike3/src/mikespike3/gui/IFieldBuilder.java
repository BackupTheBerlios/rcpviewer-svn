package mikespike3.gui;

import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Composite;

/**
 * Type for building individual fields within a gui.
 * @author Mike
 */
public interface IFieldBuilder {

	public void createGui( Composite parent, Method method, Object instance );
}
