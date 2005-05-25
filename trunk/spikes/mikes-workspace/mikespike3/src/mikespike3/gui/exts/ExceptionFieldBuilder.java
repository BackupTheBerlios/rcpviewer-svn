package mikespike3.gui.exts;

import mikespike3.gui.DefaultFieldBuilder;
import mikespike3.gui.IFieldBuilder;

import org.eclipse.swt.widgets.Composite;

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
