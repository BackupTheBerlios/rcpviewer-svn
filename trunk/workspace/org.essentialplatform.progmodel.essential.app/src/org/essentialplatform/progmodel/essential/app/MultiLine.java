package org.essentialplatform.progmodel.essential.app;
import java.lang.annotation.*;

/**
 * Indicates the number of lines over which to display this (string) attribute.
 * 
 * <p>
 * The annotation can be either to:
 * <ul>
 * <li>the getter of a string attribute, or
 * <li>a string parameter of an operation, or
 * <li>a value type that implements {@link IStringParser}.
 * </ul>
 * <p>
 * If the annotation is applied to a non-string attribute then it has no 
 * effect.  Applying the annotation any other method (eg a method representing 
 * a domain operation) also has no effect.
 * <p>
 * If the annotation is applied to a string parameter of an operation then it
 * determines the field length of that parameter.  This typically will be
 * the same as an attribute on some class upon that the operation affects.  
 * Again the annotation will be ignored if applied to a non-string parameter 
 * or to a string parameter of a method that does not represent a domain 
 * operation.  
 * <p>
 * If the annotation is applied to a value type (with a {@link Value}
 * annotation) that implements {@link IStringParser}, then any attribute or
 * operation parameter of that value type will inherit the field length.  The 
 * annotation will be ignored if it is applied to any other type. 
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
public @interface MultiLine {
	int value();
	class Factory {
		private Factory() {}
		public static MultiLine create(final int value) {
			return new MultiLine() { 
				public int value() { return value; }
				public Class<? extends Annotation> annotationType() { return MultiLine.class; }
			};
		}
	}
}
