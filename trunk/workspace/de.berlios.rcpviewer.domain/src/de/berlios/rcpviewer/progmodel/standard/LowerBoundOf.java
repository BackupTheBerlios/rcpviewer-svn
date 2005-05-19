package de.berlios.rcpviewer.progmodel.standard;
import java.lang.annotation.*;

/**
 * The lower bound for this attribute (corresponds to LowerBound, that is,
 * minimum cardinality, in meta model).
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.METHOD)
public @interface LowerBoundOf {
	int value();
}
