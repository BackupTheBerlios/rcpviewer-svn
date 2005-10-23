package de.berlios.rcpviewer.progmodel.extended;
import java.lang.annotation.*;

/**
 * Indicates an attribute or reference, or a parameter to an operation, is 
 * optional in the UI.
 *
 * <p>
 * Often capturing certain domain state is at the discretion of the business
 * user.  For example, an application might allow a description for an email 
 * address for a customer to be captured ("Work", "Home" etc) but won't
 * <i>insist</i> on it.
 * 
 * <p>
 * Marking an attribute or reference as being optional means that a domain 
 * object can be created or subsequently saved without the user having to 
 * specify any value for that member.  Note that marking a collection as
 * optional will be ignored - an empty collection is allowed even if
 * <code>@Optional</code> has not been specified.  To insist on at least one
 * element in a collection, use instead the <code>LowerBoundOf</code> 
 * annotation.
 * 
 * <p>
 * The annotation can also be used on a parameter for an operation.  Again,
 * generally a parameter is required and if a non-optional parameter has not
 * been provided then the operation cannot be invoked.  This annotation 
 * indicates that a value of null (or 0 for integers, or false for booleans)
 * is acceptable. 
 * 
 * <p>
 * (An alternative design would have been to have had a <code>@Mandatory</code>
 * annotation.  We felt it would lead to fewer (or more readily detected)
 * errors in end applications by presuming attributes and references mandatory
 * unless otherwise stated).
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface Optional {
	class Factory {
		private Factory() {}
		public static Optional create() {
			return new Optional(){
				public Class<? extends Annotation> annotationType() { return Optional.class; }
			};
		}
	}
}
