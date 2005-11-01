package org.essentialplatform.progmodel.standard;
import java.lang.annotation.*;

/**
 * The lower bound for a collection.
 * 
 * <p>
 * Although attributes and (single) references can be marked as 
 * <code>@Optional</code> or not, for collections the presumption is that
 * empty collections are allowed.  To specify that a collection must have 
 * some content, use instead this annotation.
 * 
 * <p>
 * Implementation note: corresponds to <i>LowerBound</i> in the underlying EMF
 * meta model.
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
