package de.berlios.rcpviewer.progmodel.extended;
import java.lang.annotation.*;

import de.berlios.rcpviewer.progmodel.extended.AssignmentType;

/**
 * 
 * Indicates that the attribute is either wholly or part of the persisted
 * identifier for this object.
 * 
 * <p>
 * Each object when persisted has an identifier which is made up of one or
 * several of its attributes.  For a RDBMS-based object store, these correspond
 * to the primary key.
 * 
 * <p>
 * If a single attribute makes up the identifier, then the <tt>value()</tt> 
 * property is not required.  However, if multiple attributes make up the 
 * identifier, then <tt>value()</tt> <i>is</i> required, this representing the 
 * relative significance of the attribute within the identifier.  The values 
 * of those attributes annotated with the <tt>@Id</tt> attribute should be 
 * contiguous. 
 * 
 * <p>
 * The <tt>assignedBy</tt> property is used to influence how the values of the
 * attributes annotated with the <tt>@Id</tt> annotation are populated.  The
 * {@link AssignmentType} enumerates the options: either assigned by the 
 * application, assigned by the object store, or assigned according to context
 * (ultimately still application or object store).  
 * 
 * <p>
 * Typically the <tt>assignedBy</tt> property is not required to be specified
 * since its default is to assign according to context.  The semantics of this
 * should meet the majority of cases.  Specifically:
 * <ul>
 * <li>if the identifier is composite (consists of multiple attributes) then
 *     again the identifier is presumed to be assigned by application. 
 * <li>if the identifier is simple (consists of only one attribute) and the
 *     type of the attribute is not integral number, then the identifier is
 *     presumed to be assigned by application. 
 * <li>if the identifier is simple and the type of the attribute is an 
 *     integral number, then the identifier is presumed to be assigned by 
 *     the object store. 
 * </ul>
 * Therefore, the only time when the <tt>assignedBy</tt> need be explicitly
 * specified is for an integral identifier which is assigned by the application.
 * 
 * <p>
 * The annotation should be specified on the accessor (getter), or the mutator 
 * (setter) if there is no getter.  If specified on both, then only the 
 * accessor's is used.
 * 
 * <p>
 * Specifying this annotation on things other than an attribute or 
 * operation has no effect.
 * 
 * <p>
 * Values of 0 or a negative value are ignored. 
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 *
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD})
public @interface Id {
	int value() default 1;
	AssignmentType assignedBy() default AssignmentType.CONTEXT;
	class Factory {
		private Factory() {}
		public static Id create(final int value, final AssignmentType assignedBy) {
			return new Id() {
				public int value() {
					return value;
				}
				public AssignmentType assignedBy() {
					return assignedBy;
				}
				public Class<? extends Annotation> annotationType() {
					return Id.class;
				}
			};
		}
	}
}
