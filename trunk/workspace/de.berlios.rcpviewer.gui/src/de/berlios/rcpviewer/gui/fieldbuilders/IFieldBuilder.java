package de.berlios.rcpviewer.gui.fieldbuilders;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.swt.widgets.Composite;

/**
 * Type for building individual <code>IField></code> within a gui.
 * <br>Implementations must be stateless as they are repeatedly used.
 * @author Mike
 */
public interface IFieldBuilder {
	
	public static final String EXTENSION_POINT_ID
		= "de.berlios.rcpviewer.gui.fieldbuilder"; //$NON-NLS-1$
	
	/**
	 * Whether this field builder is applicable for the passed element.
	 * @param attribute
	 * @return
	 */
	public boolean isApplicable( ETypedElement element );

	/**
	 * Create the <code>IField</code> within the supplied parent composite.
	 * <br>Whether or not editing is allowed is ascertained from the element.
	 * @param parent - the composite to be used, not <code>null</code>
	 * @param element - the element to display, , not <code>null</code>
	 * @param listener - a callback for the field to notify that it has been
	 * edited, , can be <code>null</code>
	 * @param int[] - column widths - the parent composite does not have any
	 * layout set so any number of columns can be used - this is a <b>hint</b> 
	 * to indicate column widths that would neatest fit with the overall gui 
	 * container - can be ignored 
	 * @return field
	 */
	public IField createField( Composite parent, 
							   ETypedElement element,
							   IFieldListener listener, 
							   int[] columnWidths );
	
	
	/**
	 * Gui representation of an individaul attribute field.
	 * <br>These should be lightweight but will have to hold gui-specific state.
	 * <br>A lable is also expected.
	 */
	public interface IField {
		
		/**
		 * Sets the focus.
		 */
		public void setFocus();
		
		/**
		 * Sets the gui to display the passed value.
		 * <br>This value will match the attribute passed in <code>createGui()</code>
		 * @param obj
		 */
		public void setGuiValue( Object obj );
		
		/**
		 * Return the value displayed by the gui.
		 * <br>This value should match the attribute passed in <code>createGui()</code>
		 * @return
		 */
		public Object getGuiValue();
	}
	
	/**
	 * Implementations want to know whenever the field is modified via the gui.
	 * @author Mike
	 */
	public interface IFieldListener {
		
		public void fieldModified( IField field );
		
		public void fieldFocusLost ( IField field );
	}

	
}
