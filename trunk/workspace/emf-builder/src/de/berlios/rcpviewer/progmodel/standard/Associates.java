package de.berlios.rcpviewer.progmodel.standard;
import java.lang.annotation.*;

/**
 * The type (as a Java class) that a 1:m reference associates.
 * 
 * <p>
 * 1:M references between classes are typically specified with List<T>, Set<T> 
 * or Map<T>.  However, the generic type T is erased during compilation so that
 * by the time the metamodel is built (reflectively, at runtime), the type is
 * no longer available.
 * 
 * <p>
 * This annotation therefore allows the type to be preserved into the runtime 
 * such that the information needed to build the metamodel is present.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.METHOD)
public @interface Associates {
	Class<?> value();
}
