package org.essentialplatform.progmodel.essential.app;
import java.lang.annotation.*;

/**
 * The abbreviation of a domain class.
 * 
 * <p>
 * Used in handles.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.TYPE})
public @interface Abbreviated {
	String value();
	class Factory {
		private Factory() {}
		public static Abbreviated create(final String value) {
			return new Abbreviated(){
				public String value() { return value; }
				public Class<? extends Annotation> annotationType() { return Abbreviated.class; }
			};
		}
	}
}
