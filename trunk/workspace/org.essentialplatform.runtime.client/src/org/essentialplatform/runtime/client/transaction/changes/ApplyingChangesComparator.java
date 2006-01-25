package org.essentialplatform.runtime.client.transaction.changes;

import java.util.Comparator;


/**
 * Sorts a list of changes such that they may be applied to the persistent
 * object store.
 * 
 * <p>
 * The algorithm is as follows:
 * <ul>
 * <li> {@link IChange#NULL} changes
 * <li> {@link IChange#IRREVERSIBLE} changes
 * <li> {@link InstantiationChange}s
 * <li> {@link IModificationChange}s
 *      <ul>
 *      <li>{@link AttributeChange}s
 *      <li>{@link OneToOneReferenceChange}s
 *      <li>{@link AddToCollectionChange}s
 *      <li>{@link RemoveFromCollectionChange}s
 *      </ul>
 * <li> {@link DeletionChange}s
 * </ul>
 * If null is passed in, will be placed last.
 * 
 * @author Dan Haywood
 */
public class ApplyingChangesComparator implements Comparator<IChange> {

	public int compare(IChange o1, IChange o2) {
		if (o1 == null && o2 == null) return 0;
		if (o1 == null) return +1;
		if (o2 == null) return -1;
		if (o1 == IChange.NULL && o2 == IChange.NULL) return 0;
		if (o1 == IChange.NULL) return -1;
		if (o2 == IChange.NULL) return +1;
		if (o1 == IChange.IRREVERSIBLE && o2 == IChange.IRREVERSIBLE) return 0;
		if (o1 == IChange.IRREVERSIBLE) return -1;
		if (o2 == IChange.IRREVERSIBLE) return +1;
		if (o1 instanceof InstantiationChange && o2 instanceof InstantiationChange) return 0;
		if (o1 instanceof InstantiationChange) return -1;
		if (o2 instanceof InstantiationChange) return +1;
		if (o1 instanceof AttributeChange && o2 instanceof AttributeChange) return 0;
		if (o1 instanceof AttributeChange) return -1;
		if (o2 instanceof AttributeChange) return +1;
		if (o1 instanceof OneToOneReferenceChange && o2 instanceof OneToOneReferenceChange) return 0;
		if (o1 instanceof OneToOneReferenceChange) return -1;
		if (o2 instanceof OneToOneReferenceChange) return +1;
		if (o1 instanceof AddToCollectionChange && o2 instanceof AddToCollectionChange) return 0;
		if (o1 instanceof AddToCollectionChange) return -1;
		if (o2 instanceof AddToCollectionChange) return +1;
		if (o1 instanceof RemoveFromCollectionChange && o2 instanceof RemoveFromCollectionChange) return 0;
		if (o1 instanceof RemoveFromCollectionChange) return -1;
		if (o2 instanceof RemoveFromCollectionChange) return +1;
		if (o1 instanceof DeletionChange && o2 instanceof DeletionChange) return 0;
		if (o1 instanceof DeletionChange) return -1;
		if (o1 instanceof DeletionChange) return +1;
		return -1;
	}

}
