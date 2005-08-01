package de.berlios.rcpviewer.progmodel.extended;
import java.lang.annotation.*;

/**
 * Indicates an attribute, reference or collection is invisible in the UI.
 *
 * <p>
 * Very often there are attributes (and sometimes references or collections)
 * that need to be persisted but that should never be seen in the UI.  In these
 * cases this annotation can be applied to effectively suppress the attribute
 * or so forth.
 * 
 * <p>
 * An alternative is to explicitly write a <code>...Pre()</code> method that
 * returns an {@link de.berlios.rcpviewer.progmodel.extended.IPrerequisites} 
 * whose visibility is constrained, for example:
 * <pre>
 * public int getInternalId() { 
 *     return _internalId;
 * }
 * public IPrerequisites getInternalIdPre() {
 *     return Prerequisites.invisible();
 * }
 * </pre>
 * <p>
 * This is identical to applying this annotation, but clearly the declarative
 * approach of applying this annotation is far easier.  One place where the 
 * programmatic approach <i>is</i> required however is when the visibility of
 * the member may vary, eg:
 * <pre>
 * public int getInternalId() { 
 *     return _internalId;
 * }
 * public IPrerequisites getInternalIdPre() {
 *     return Prerequisites.require(
 *     			notTuesday(), 
 *     			"Can't see this attribute on a Tuesday!", 
 *     			Constraint.INVISIBLE);
 * }
 * </pre>
 * 
 * <p>
 * One case where this annotation has no effect is if it is combined with the
 * <code>@SaveOperation</code>.  Since users are used to seeing File>Save, 
 * we don't want it to suddenly disappear.  The annotation will just be 
 * ignored if <code>@SaveOperation</code> is also present.  To disable access,
 * use <code>savePre()</code> method.
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD})
public @interface Invisible {
}
