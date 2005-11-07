package org.essentialplatform.progmodel.essential.app;
import java.lang.annotation.*;

/**
 * Indicates an attribute, reference or collection can be modified only until
 * the domain object has been initially saved. 
 *
 * <p>
 * Very often there are few constraints on a domain object's state while it is 
 * being created, however its state must be rigidly controlled once it has
 * been created.  This annotation can be applied to all members representing
 * an object's state (attribute, reference or collection) indicating that the
 * member may not be modified once the object has been persisted.
 * 
 * <p>
 * Alternatively the annotation may be applied to an entire type in which case
 * it is inherited by all members.  (Members can opt out of this by specifying
 * the <tt>optout</tt> attribute of true)
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ImmutableOncePersisted {
	/**
	 * When the annotation has been applied to a type - meaning that all
	 * attributes are immutable once persisted - then this can be additionally
	 * applied to an attribute to indicates that the attribute has opted out
	 * of this semantic (is still mutable once persisted).
	 * 
	 * <p>
	 * If specified for an attribute when the type does not have the annotation
	 * applied, then has no meaning and is ignored. 
	 * 
	 * @return
	 */
	boolean optout() default false;
	class Factory {
		private Factory() {}
		public static ImmutableOncePersisted create(final boolean optout) {
			return new ImmutableOncePersisted() { 
				public boolean optout() { return optout; }
				public Class<? extends Annotation> annotationType() { return ImmutableOncePersisted.class; }
			};
		}
	}
}
