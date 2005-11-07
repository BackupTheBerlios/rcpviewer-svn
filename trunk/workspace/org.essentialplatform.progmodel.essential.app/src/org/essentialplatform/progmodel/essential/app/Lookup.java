package org.essentialplatform.progmodel.essential.app;
import java.lang.annotation.*;

/**
 * Indicates that a type is a lookup so should be rendered appropriately
 * (eg as a drop-down).
 *
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.TYPE})
public @interface Lookup {
	class Factory {
		private Factory() {}
		public static Lookup create() {
			return new Lookup(){
				public Class<? extends Annotation> annotationType() { return Lookup.class; }
			};
		}
	}

}
