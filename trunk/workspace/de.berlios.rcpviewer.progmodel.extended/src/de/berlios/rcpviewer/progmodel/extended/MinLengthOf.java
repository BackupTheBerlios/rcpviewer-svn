package de.berlios.rcpviewer.progmodel.extended;
import java.lang.annotation.*;

/**
 * Indicates the minimum length of a string attribute.
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
 * determines the minimum length of that parameter.  The annotation will be 
 * ignored if applied to a non-string parameter or to a string parameter of a 
 * method that does not represent a domain operation.  
 * <p>
 * If the annotation is applied to a value type (with a <code>@Value</code>
 * annotation) that implements IStringParser, then any attribute of that value
 * type will inherit the minimum length.  The annotation will be ignored if it 
 * is applied to any other type. 
 * 
 * <p>
 * If the <code>Mask</code> annotation is present then this annotation if
 * present is ignored and instead the length of the mask's string is used.
 * 
 * <p>
 * Although this annotation can be used to make a string attribute mandatory
 * (by specifying a minimum length of 1, say), it is sufficient simply to
 * omit the <code>@Optional</code> tag - this has the same effect and moreover
 * applies to non-string attributes.   Nevertheless, a value of 0 is valid
 * and can be used to indicate that the attribute is optional.  A negative
 * value will simply be ignored.
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
public @interface MinLengthOf {
	int value();
}
