package org.essentialplatform.progmodel.essential.app;
import java.lang.annotation.*;

/**
 * Whether a reference is a containment reference (with the owning class being
 * the container).
 * 
 * <p>
 * Containment is also known as composition or aggregation by value.  If you
 * use UML, its the black diamond adornment.  We've used containment though
 * because (a) that is the term that EMF uses, and (b) "composition" can
 * get confused with the composite pattern and with components.
 * 
 * <p>
 * Typically if a containing object is deleted then all of its contained
 * objects should be deleted too.  If contained objects should not be deleted
 * by default, then set the {@link #cascade()} property to <code>false</code>. 
 * 
 * <p>
 * This annotation is read reflectively when the metamodel is built, therefore
 * runtime retention. 
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.METHOD)
public @interface ContainerOf {
	boolean cascade() default true;
}

