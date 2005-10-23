package de.berlios.rcpviewer.progmodel.extended;
import java.lang.annotation.*;

import de.berlios.rcpviewer.progmodel.standard.Immutable;

import de.berlios.rcpviewer.progmodel.extended.Lifecycle;
import de.berlios.rcpviewer.progmodel.extended.Lookup;

/**
 * Indicates that the annotated operation should be called when the domain 
 * object is either initially saved or subsequently updated.
 * 
 * <p>
 * Every regular domain object has a save operation, either implicitly or 
 * explicitly.  If the domain programmer has written a <code>save()</code> 
 * operation or annotated a no-arg operation with the {@link SaveOperation} 
 * then the operation is explicitly specified.  If no such operation exists 
 * then the platform still provides a save operation.
 * 
 * <p>
 * Whether or not the programmer has written a save operation, the platform 
 * will save the object using:
 * <pre>
 *     getAppContainer().save(this);
 * </pre>
 * <p>
 * In other words, there is no need for the save operation to include the
 * above code (although doing so is not an error).
 * 
 * <p>
 * It should be noted that a save may not succeed because there may be
 * entity integrity constraints on either the primary key or (more likely) on
 * any business keys (also known as alternate keys).  In such a case the 
 * operation will fail.  A domain programmer may wish to use a 
 * <code>savePre()</code> method to guard against this, however this isn't 
 * mandatory and there would still be a race condition.
 * 
 * <p>
 * By convention the platform will look for a method named <code>save()</code>
 * and will invoke this when an object is being persisted for the first time or
 * subsequently saved.  The purpose of this annotation allows another 
 * public no-arg method to be nominated as the save operation.
 * 
 * <p>
 * Whether or not a save operation has been explicitly provided, all 
 * declarative and programmatic prerequisites for each of the domain object's 
 * attribute (eg {@link MinLengthOf}, {@link Mask}, not {@link Optional}, or the
 * <code>...Pre()</code> methods) will be enforced.  Very often this will be 
 * all that is required and explicit no save operation will be needed.  But as a
 * counter example: a save operation (with corresponding <code>savePre()</code>
 * method) would be required to perform inter-attribute checks, such as 
 * checking that <code>startDate &lt; endDate</code>.
 * 
 * <p> 
 * For objects that are components of a larger containing object (eg the Name
 * of a Customer) it is not appropriate for them to expose a save operation.
 * Instead these objects should be persisted through their containing object
 * (Customer).  So here the {@link #unusableReason()} attribute of this 
 * annotation can be used to indicate a reason why the <code>File>Save</code>
 * operation should be unusable for such an object.  Even though this may have 
 * been done, the object can still supply some prerequisites using a 
 * <code>savePre()</code> method.  However, these prerequisites will be applied
 * when the containing object is saved (persistence by reachability).
 * 
 * <p>
 * Since the save operation is standard to all domain objects, there is no
 * need to use an {@link RelativeOrder} annotation with it; the UI will position the 
 * save operation available anyway (eg as <i>File>Save</i>. 
 *    
 * <p>
 * This annotation is ignored if applied to any non-public method or to a 
 * method associated with defining an attribute or reference.  If used on a 
 * class that has been annotated as {@link Lookup} or {@link TransientOnly}
 * then it will also be ignored.
 * 
 * <p>
 * The annotation is ignored for any type annotated as {@link Lookup}, 
 * {@link Immutable} or for a domain class that is not persistable 
 * ({@link Lifecycle}'s <code>saveable</code> attribute).  For these types 
 * the <i>File>Save</i> will be disabled in the UI. 
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 *
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD})
public @interface SaveOperation {
	/**
	 * A non-empty reason indicates that the save operation should <i>not</i>
	 * be enabled for this object.
	 * 
	 * <p>
	 * Any prerequisites for the save operation will still be enforced when 
	 * this object is implicitly saved (through persistence by reacahbility).
	 *  
	 * @return
	 */
	String unusableReason() default "";
	
	class Factory {
		private Factory() {}
		public static SaveOperation create(final String unusableReason) {
			return new SaveOperation(){
				public String unusableReason() { return unusableReason; }
				public Class<? extends Annotation> annotationType() { return SaveOperation.class; }
			};
		}
	}

}
