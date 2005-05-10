package mikespike3.gui;

public class FormBuilderFactory {

	/**
	 * Selects and generates IFormBuilder appropriate for passed instance.
	 * @param instance
	 * @return
	 */
	public IFormBuilder create( Object instance ) {
		return new DefaultFormBuilder();
	}

}
