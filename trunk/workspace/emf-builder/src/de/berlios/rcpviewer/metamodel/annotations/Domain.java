package de.berlios.rcpviewer.metamodel.annotations;
import java.lang.annotation.*;

/**
 * Persistable and visualisable domain object.
 * 
 * Consumed by AspectJ, hence only source retention policy.
 */
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Target(ElementType.TYPE)
public @interface Domain { }
