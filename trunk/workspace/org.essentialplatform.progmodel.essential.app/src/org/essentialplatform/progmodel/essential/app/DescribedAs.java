package org.essentialplatform.progmodel.essential.app;
import java.lang.annotation.*;

/**
 * The description of a domain class, attribute, operation or parameter of an
 * operation.
 * 
 * <p>
 * How a viewing mechanism might use this is up to it; one common usage is
 * tool tip text.
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
public @interface DescribedAs {
	String value();
}
