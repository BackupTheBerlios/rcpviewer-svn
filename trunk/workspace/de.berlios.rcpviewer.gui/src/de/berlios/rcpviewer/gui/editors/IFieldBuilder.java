package de.berlios.rcpviewer.gui.editors;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.widgets.Composite;

/**
 * Type for building individual <code>IField></code> within a gui.
 * <br>Implementations must be stateless as they are repeatedly used.
 * @author Mike
 */
public interface IFieldBuilder {
	
	public static final String EXTENSION_POINT_ID
		= "de.berlios.rcpviewer.gui.fieldbuilder";
	
	/**
	 * Whether this field builder is applicable for the passed attribute.
	 * @param attribute
	 * @return
	 */
	public boolean isApplicable( EAttribute attribute );

	/**
	 * Create the <code>IField</code> within the supplied parent composite.
	 * <br>The third arg is a callback for the field to notify that it has been
	 * edited.  This may be null if editabel = <code>false</code>.
	 * @param parent
	 * @param clazz
	 * @param attribute
	 * @return focus
	 */
	public IField createField( Composite parent, 
							   boolean editable,
							   IFieldListener listener );
	
	
	/**
	 * Gui representation of an individaul attribute field.
	 * <br>These should be lightweight but will have to hold gui-specific state.
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
	 * Implementatiosn want to know whever the field is modified via the gui.
	 * @author Mike
	 */
	public interface IFieldListener {
		
		public void fieldModified();
	}

	
}
