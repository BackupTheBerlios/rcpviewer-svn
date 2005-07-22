package de.berlios.rcpviewer.progmodel.extended;
import java.lang.annotation.*;

/**
 * 
 * Indicates the maximum length of a string attribute.
 * 
 * <p>
 * The maximum length indicates the number of characters that will be stored
 * for a given attribute.  The annotation can be either to:
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
 * determines the maximum length of that parameter.  This typically will be
 * the same as an attribute on some class upon that the operation affects.  
 * Again the annotation will be ignored if applied to a non-string parameter 
 * or to a string parameter of a method that does not represent a domain operation.  
 * <p>
 * If the annotation is applied to a value type (with a <code>@Value</code>
 * annotation) that implements IStringParser, then any attribute of that value
 * type will inherit the maximum length.  The annotation will be ignored if it 
 * is applied to any other type. 
 * 
 * <p>
 * If the <code>Mask</code> annotation is present then this annotation if
 * present is ignored and instead the length of the mask's string is used.
 * 
 * <p>
 * Values of 0 or a negative value are ignored. 
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 *
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
public @interface MaxLengthOf {
	int value();
}
