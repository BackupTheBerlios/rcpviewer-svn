package org.essentialplatform.progmodel.extended;
import java.lang.annotation.*;

/**
 * A regular expression to apply to string attributes.
 * 
 * <p>
 * The intent is similar to {@link Mask}, however with all the power that 
 * regular expression afford.  However, unlike {@link Mask}, the minimum length 
 * and maximum length are <i>not</i> inferred from the length of the regex 
 * pattern.
 * 
 * <p>
 * The annotation can be either to:
 * <ul>
 * <li>the getter of a mutable string attribute, or
 * <li>a string parameter of an operation, or
 * <li>a value type that implements {@link IStringParser}.
 * </ul>
 * <p>
 * If the annotation is applied to a string attribute then it is applied when
 * the attribute is modified.  If there is no setter, then the annotation is 
 * ignored.  Applying the annotation to a non-string attribute has no effect 
 * and applying the annotation any other method (eg a method representing a 
 * domain operation) also has no effect.
 * <p>
 * If the annotation is applied to a string parameter of an operation then it
 * determines what can be entered into that parameter.  Again it will be ignored
 * if applied to a non-string parameter or to a string parameter of a method
 * that does not represent a domain operation.  
 * <p>
 * If the annotation is applied to a value type (with a <code>@Value</code>
 * annotation) that implements {@link IStringParser}, then any attribute or 
 * operation parameter of that value type will inherit the regex.  The 
 * annotation will be ignored if it is applied to any other type. 
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
public @interface Regex {
	String value();
	class Factory {
		private Factory() {}
		public static Regex create(final String value) {
			return new Regex(){
				public String value() { return value; }
				public Class<? extends Annotation> annotationType() { return Regex.class; }
			};
		}
	}
}
