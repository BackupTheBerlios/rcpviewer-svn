package de.berlios.rcpviewer.metamodel.annotations;
import java.lang.annotation.*;

/** 
 * Classes that represent EMF datatypes (datatype of attribute not otherwise
 * part of the metamodel) should be annotated with this annotation.
 * 
 * Consumed by AspectJ, hence only source retention policy.
 */
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Target(ElementType.TYPE)
public @interface Value { }
