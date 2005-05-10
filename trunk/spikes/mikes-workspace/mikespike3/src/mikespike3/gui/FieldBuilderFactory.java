package mikespike3.gui;

import java.lang.reflect.Method;

public class FieldBuilderFactory {

	/**
	 * Selects and generates IFieldBuilder appropriate for passed method on the
	 * passed instance
	 * @param instance
	 * @return
	 */
	public IFieldBuilder create( Method method, Object instance ) {
		return new DefaultFieldBuilder();
	}

}
