package de.berlios.rcpviewer.domain;
import java.lang.annotation.*;

/**
 * To specify the class that a collection (List, Map etc) associates.
 *
 * <p>
 * This annotation is needed because of Java 5.0's type erasure (we don't
 * know the compile-time type of the object held in collections).
 *   
 * <p>
 * Runtime retention policy since read reflectively.
 * 
 * TODO: not yet in use; the idea is though that this annotation will be
 * used as part of working out links in the metamodel.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface References {
	Class value();
}
