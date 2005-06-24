package de.berlios.rcpviewer.gui.editors.exts;

import org.eclipse.swt.widgets.Composite;

import de.berlios.rcpviewer.gui.editors.DefaultFieldBuilder;
import de.berlios.rcpviewer.gui.editors.IFieldBuilder;

public class ExceptionFieldBuilder implements IFieldBuilder {

	private final DefaultFieldBuilder delegate = new DefaultFieldBuilder();
	
	public boolean isApplicable(Class clazz, Object value) {
		return Exception.class.isAssignableFrom( clazz );
	}

	/**
	 * Actually just does what the default field builder does,.
	 */
	public void createGui(Composite parent, Object value ) {
		delegate.createGui( parent, value );
	}

}
