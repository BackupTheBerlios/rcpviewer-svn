package org.essentialplatform.progmodel.essential.app;
import java.lang.annotation.*;

/**
 * If the viewing mechanism provides generic capabilities for managing the
 * lifecycle of domain objects, then the domain object class definition can
 * be annotated with this annotation.
 * 
 * <p>
 * The defaults for the attributes of this annotation are all true; simply
 * omitting the annotation altogether implies all are false.
 *  
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.TYPE)
public @interface Lifecycle {
	/**
	 * Whether the type can be searched for using some generic search
	 * capability.  Default is true.
	 * 
	 * <p>
	 * If searchable, the UI may render this as enabling from Search menu.
	 * 
	 * @return
	 */
	boolean searchable() default true;
	/**
	 * Whether the type can be explicitly instantiated.  Default is true.
	 * 
	 * <p>
	 * If instantiatable, the UI may render this as enabling 'File>New', for
	 * example.
	 * 
	 * @return
	 */
	boolean instantiable() default true;
	/**
	 * Whether the type can be explicitly saved.  Default is true.
	 * 
	 * <p>
	 * The UI may render this as disabling the 'File>Save', for example.
	 * 
	 * <p>
	 * Note: this does not mean that the object can never be persisted, only
	 * that it isn't surfaced through a generic mechanism of the UI.
	 * 
	 * @return
	 */
	boolean saveable() default true;
	
	class Factory {
		private Factory() {}
		public static Lifecycle create(final boolean searchable, final boolean instantiable, final boolean saveable) {
			return new Lifecycle() {
				public boolean searchable() { return searchable; }
				public boolean instantiable() { return instantiable; }
				public boolean saveable() { return saveable; }
				public Class<? extends Annotation> annotationType() { return Lifecycle.class; }
			};
		}
	}

}
