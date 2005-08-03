package de.berlios.rcpviewer.progmodel.extended;
import java.lang.annotation.*;

import de.berlios.rcpviewer.progmodel.standard.Immutable;

import de.berlios.rcpviewer.progmodel.extended.SaveOperation;
import de.berlios.rcpviewer.progmodel.extended.Lifecycle;
import de.berlios.rcpviewer.progmodel.extended.Lookup;
import de.berlios.rcpviewer.progmodel.extended.ImmutableOncePersisted;;

/**
 * Indicates that the annotated operation should be called when the domain 
 * object is deleted.
 * 
 * <p>
 * The delete operation isn't completely analogous to the save operation 
 * (annotated by {@link SaveOperation}).  Every regular domain object has 
 * a save operation, even if the domain programmer hasn't written a 
 * <code>save()</code> operation or annotated an operation with 
 * {@link SaveOperation}.  Indeed, the domain programmer must use 
 * {@link SaveOperation#unusableReason()} to mark the implicitly provided
 * save operation as unusable if none should be provided.
 * 
 * <p>
 * In contrast, it does not make sense to say that every domain object can be
 * deleted.  Therefore the platform will only make a delete operation available
 * in the UI if there is a <code>delete()</code> operation or some operation
 * annotated with this annotation.
 *
 * <p>
 * If the programmer <i>has</i> provided a delete operation though, then the 
 * platform will implicitly perform the following:
 * <pre>
 *     getAppContainer().delete(this);
 * </pre>
 * <p>
 * In other words, there is no need for the delete operation to include the
 * above code (though doing so is not an error).
 * 
 * <p>
 * It should be noted that a delete may not succeed because there may be
 * referential integrity constraints that prevent a referenced object from 
 * being deleted.  In such a case the operation will fail.  A domain programmer
 * may wish to use a <code>deletePre()</code> method to guard against this, 
 * however this isn't mandatory and there would still be a race condition.
 * 
 * <p>
 * Since the delete operation is a standard operation, there is no need to use 
 * an {@link Order} annotation with it; the UI will position the delete 
 * operation available anyway (eg as <i>File>Delete</i>. 
 *   
 * <p>
 * Prerequisites for the delete operation can be specified in the usual way
 * using a <code>...Pre()</code> method.
 *  
 * <p>
 * The annotation is ignored for any type annotated as {@link Lookup}, 
 * {@link Immutable}, {@link ImmutableOncePersisted} or for a domain class that 
 * is not persistable ({@link Lifecycle}'s <code>saveable</code> attribute).
 *  
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 *
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD})
public @interface DeleteOperation {}
