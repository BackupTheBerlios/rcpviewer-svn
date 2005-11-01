package org.essentialplatform.progmodel.standard;
import java.lang.annotation.*;

/** 
 * Classes that represent EMF datatypes (datatype of attribute not otherwise
 * part of the metamodel) should be annotated with this annotation.
 *
 * <p>
 * Consumed reflectively, so runtime retention policy.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.TYPE)
public @interface Value {
	String value() default ProgModelConstants.DEFAULT_DOMAIN_NAME;
}
