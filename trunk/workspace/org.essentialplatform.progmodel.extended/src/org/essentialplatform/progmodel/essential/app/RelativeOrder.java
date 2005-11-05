package org.essentialplatform.progmodel.essential.app;
import java.lang.annotation.*;

/**
 * The value of the annotation determines the order relative to other
 * attributes (when applied to a method representing an attribute) or 
 * operations (when applied to a method representing an operation).  
 * 
 * <p>
 * For an attribute, the annotation should be specified on the accessor
 * (getter), or the mutator (setter) if there is no getter.  If specified
 * on both, then only the accessor's is used.
 * 
 * <p>
 * Specifying this annotation on things other than an attribute or 
 * operation has no effect.  Also specifying this annotation with the
 * {@link SaveOperation} also has no effect (the save operation is common to
 * all domain objects and the UI will always represent in the same way as
 * <i>File>Save</i>. 
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.METHOD)
public @interface RelativeOrder {
	int value();
	class Factory {
		private Factory() {}
		public static RelativeOrder create(final int value) {
			return new RelativeOrder(){
				public int value() { return value; }
				public Class<? extends Annotation> annotationType() { return RelativeOrder.class; }
			};
		}
	}
}
