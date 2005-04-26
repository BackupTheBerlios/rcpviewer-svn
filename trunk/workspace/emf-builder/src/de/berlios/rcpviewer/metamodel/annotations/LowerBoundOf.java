package de.berlios.rcpviewer.metamodel.annotations;
import java.lang.annotation.*;

/**
 * The lower bound for this attribute (corresponds to LowerBound, that is,
 * minimum cardinality, in meta model).
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.METHOD)
public @interface LowerBoundOf {
	int value();
}
