package org.essentialplatform.progmodel.extended;
import java.lang.annotation.*;

/**
 * The name of a domain class, attribute, operation or parameter of an
 * operation.
 * 
 * <p>
 * For domain classes, attributes and operations the name can be inferred from
 * its name in code.  However, sometimes the desired name might not have been 
 * used, perhaps because it is a reserved word in the programming language.  If
 * this annotation is present, then it is used in preference to the inferred
 * name.
 * 
 * <p>
 * For parameters of an operation the name cannot be picked up reflectively, 
 * and so this annotation allows a meaningful name to be specified (rather than
 * just "int0", "string1" and so forth).
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
public @interface Named {
	String value();
	class Factory {
		private Factory() {}
		public static Named create(final String value) {
			return new Named(){
				public String value() { return value; }
				public Class<? extends Annotation> annotationType() { return Named.class; }
			};
		}
	}
}
