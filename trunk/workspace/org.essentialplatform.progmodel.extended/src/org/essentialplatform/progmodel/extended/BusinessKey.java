package org.essentialplatform.progmodel.extended;
import java.lang.annotation.*;

/**
 * 
 * Indicates that the attribute is an alternate key or a part of an
 * business key (sometimes called <i>alternate</i> key)
 * 
 * <p>
 * Although by convention domain classes should have a surrogate unique 
 * identifier, there will also be at least one way (and perhaps several ways)
 * for an end user to uniquely identify an object instance.
 * 
 * <p>
 * This annotation is a way of capturing these alternate ways, both for the
 * purpose of persistence (eg generation of <code>unique</code> constraints) 
 * but also potentially to provide in the UI standardised ways of searching 
 * for objects.
 * 
 * <p>
 * For an attribute that represents a business key in its entirety (eg a
 * Social Security Number or Driving License Number), the format is:
 * <code>@BusinessKey("ssn")</code> (where <i>ssn</i> strictly speaking is the 
 * formal name of the business key being described).
 * 
 * <p>
 * For an attribute that represents a component of a business key (eg first name
 * and last name for an Actors' Guild), the format is:
 * <code>@BusinessKey("name.1")</code> or <code>@BusinessKey("name.2")</code>. 
 * Here <i>name</i> is again the formal name of the business key being 
 * described, while the numeric suffix indicates the position in the key, most
 * significant first.
 * 
 * <p>
 * This annotation may only be applied to the getter of a read- or read-write
 * attribute, or to the setter of a write-only attribute. 
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 *
 * <p>
 * TODO: there is currently no support for an attribute to be in multiple
 * business keys (would need to have an annotation that holds an array of such
 * because Java does not allow multiple annotations of the same type of a code
 * feature).
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD})
public @interface BusinessKey {
	String name();
	int pos() default 1;
	class Factory {
		private Factory() {}
		public static BusinessKey create(final String name, final int pos) {
			return new BusinessKey(){
				public String name() { return name; }
				public int pos() { return pos; }
				public Class<? extends Annotation> annotationType() { return BusinessKey.class; }
			};
		}
	}


}
