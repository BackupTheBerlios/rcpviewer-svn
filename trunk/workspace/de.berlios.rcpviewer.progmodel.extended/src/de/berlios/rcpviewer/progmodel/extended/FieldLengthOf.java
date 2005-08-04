package de.berlios.rcpviewer.progmodel.extended;
import java.lang.annotation.*;

/**
 * Indicates the desired field length of a string attribute within the user
 * interface.
 * 
 * <p>
 * The field length is different from the maximum length 
 * (<code>@MaxLength</code> annotation) because the latter represents the 
 * number of characters that can be stored for an attribute whereas the former 
 * is the number of characters that will be shown.  Often they will be the same 
 * (in which case the field length annotation can be omitted).  If the field 
 * length is larger than the max length then it will be set to the maximum
 * length.  If the value is zero or negative then it will be ignored.
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
 * If the <code>Mask</code> annotation is present then this annotation if
 * present is ignored and instead the length of the mask's string is used.
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
public @interface FieldLengthOf {
	int value();
}
