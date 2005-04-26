package de.berlios.rcpviewer.progmodel.standard;
import java.lang.annotation.*;

/** 
 * Classes that represent EMF datatypes (datatype of attribute not otherwise
 * part of the metamodel) should be annotated with this annotation.
 *
 * <p>
 * Consumed by AspectJ, hence only source retention policy.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Target(ElementType.TYPE)
public @interface Value { }
