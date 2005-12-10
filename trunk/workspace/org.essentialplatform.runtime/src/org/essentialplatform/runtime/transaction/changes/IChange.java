package org.essentialplatform.runtime.transaction.changes;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.IrreversibleTransactionException;

/**
 * An abstraction of some work performed in the domain.
 * 
 * <p>
 * Concrete examples include:
 * <ul>
 * <li> invoking an operation
 * <li> setting an attribute
 * <li> instantiating a domain object
 * <li> deleting a domain object
 * </ul>
 * 
 * <p>
 * <b>All implementations are expected to have value semantics.</b>
 */
public interface IChange {

	/**
	 * A default implementation of this interface that does nothing.
	 * 
	 * <p>
	 * That is, the null object pattern.
	 */
	public final static IChange NULL = NullChange.__instance;

	/**
	 * A default implementation of this interface that does nothing but that
	 * cannot be uncommitted.
	 */
	public final static IChange IRREVERSIBLE = IrreversibleChange.__instance;

	/**
	 * The transaction in which this change was performed.
	 * @return
	 */
	public ITransaction getTransaction();
	
	/**
	 * Execute this atom of work.
	 * 
	 * <p>
	 * The object returned is that required as per proceed(...); often null.
	 */
	public Object execute();

	/**
	 * Undo this atom of work.
	 * 
	 * <p>
	 * Every implementation must be able to be undone (backing out an in-memory
	 * pending change). However, it isn't the case that that every work item can
	 * be irreversible (whether a committed change can be backed out).
	 */
	public void undo();

	/**
	 * Whether this atom of work can irreversible (cannot be automatically
	 * undone once committed).
	 * 
	 * <p>
	 * This is a different concept from being undone (which is simply backing
	 * out a pending in-memory change).
	 * 
	 * @return
	 */
	public boolean isIrreversible();

	/**
	 * The pojo or pojos that are modified as a result of this work atom.
	 * 
	 * <p>
	 * When added to a {@link ITransaction}, these pojos are effectively
	 * enlisted into it.
	 * 
	 * @return
	 */
	public Set<ITransactable> getModifiedPojos();

	/**
	 * A human-readable description of this atom of work.
	 * 
	 * <p>
	 * Since work atoms may be exposed to the user, they should be able to
	 * provide some clue as to what they represent.
	 * 
	 * <p>
	 * For example:
	 * <ul>
	 * <li> a work atom that represents a field modification might have the name
	 * of the containing class and the field name
	 * <li> a work atom that represents an invokation of an operation might hold
	 * the name of that operation
	 * </ul>
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * Any additional information that may further help describe this work atom.
	 * 
	 * <p>
	 * For example:
	 * <ul>
	 * <li> a work atom that represents a field modification might have the pre
	 * and post values.
	 * <li> a work atom that represents an invokation of an operation might hold
	 * the values of the arguments.
	 * </ul>
	 * 
	 * @return
	 */
	public Object[] getExtendedInfo();

	/**
	 * Null object pattern applied to {@link IChange}s.
	 * 
	 * <p>
	 * This is <code>public</code> only because members of interfaces must be
	 * so.
	 */
	public static final class NullChange implements IChange {

		/**
		 * package level visibility for {@link IChange} to access
		 */
		final static NullChange __instance = new NullChange();

		private final static String __description = "Null";

		private final static Object[] __extendedInfo = new Object[] {};

		private NullChange() {
		}

		public ITransaction getTransaction() {
			return null;
		}

		/*
		 * Does nothing.
		 * 
		 * @see org.essentialplatform.transaction.IChange#execute()
		 */
		public final Object execute() {
			// does nothing
			return null;
		}

		/*
		 * Does nothing.
		 * 
		 * @see org.essentialplatform.transaction.IChange#undo()
		 */
		public final void undo() {
			// does nothing
		}

		/*
		 * Can reverse (since does nothing).
		 * 
		 * @see org.essentialplatform.transaction.IChange#isIrreversible()
		 */
		public boolean isIrreversible() {
			return false;
		}

		/*
		 * @see org.essentialplatform.transaction.IChange#getDescription()
		 */
		public String getDescription() {
			return __description;
		}

		/*
		 * @see org.essentialplatform.transaction.IChange#getExtendedInfo()
		 */
		public Object[] getExtendedInfo() {
			return __extendedInfo;
		}

		/*
		 * @see org.essentialplatform.transaction.IChange#getModifiedPojos()
		 */
		public Set<ITransactable> getModifiedPojos() {
			return Collections.EMPTY_SET;
		}

		/**
		 * Always returns 0.
		 * 
		 * <p>
		 * Well, it is the Null object.
		 */
		public int hashCode() {
			return 0;
		}

		/**
		 * equal if it is a {@link NullChange}
		 */
		@Override
		public boolean equals(Object other) {
			if (!getClass().equals(other.getClass())) {
				return false;
			}
			return true;
		}

		/*
		 * 
		 * @see org.essentialplatform.runtime.transaction.changes.IChange#doesNothing()
		 */
		public boolean doesNothing() {
			return true;
		}

		private IChange _parent;
		public IChange getParent() {
			return _parent;
		}
		public void setParent(final IChange parent) {
			this._parent = parent;
		}

	}

	/**
	 * Null object pattern applied to {@link WorkAtom}s.
	 * 
	 * <p>
	 * This is <code>public</code> only because members of interfaces must be
	 * so.
	 */
	public static final class IrreversibleChange implements IChange {

		/**
		 * package level visibility for access by ....
		 */
		private final static IrreversibleChange __instance = new IrreversibleChange();

		private final static String __description = "Irreversible";

		private final static Object[] __extendedInfo = new Object[] {};

		private IrreversibleChange() {
		}

		/*
		 * Does nothing.
		 * 
		 * @see org.essentialplatform.transaction.IChange#execute()
		 */
		public final Object execute() {
			// does nothing
			return null;
		}

		/*
		 * Does nothing.
		 * 
		 * @see org.essentialplatform.transaction.IChange#undo()
		 */
		public final void undo() {
			throw new IrreversibleTransactionException();
		}

		/**
		 * Rather non-intuitively, returns <code>false</code> so that this
		 * sort of change will be added to transactions.
		 */
		public boolean doesNothing() {
			return false;
		}

		/*
		 * Can reverse (since does nothing).
		 * 
		 * @see org.essentialplatform.transaction.IChange#isIrreversible()
		 */
		public boolean isIrreversible() {
			return true;
		}

		/*
		 * @see org.essentialplatform.transaction.IChange#getModifiedPojos()
		 */
		public Set<ITransactable> getModifiedPojos() {
			return Collections.EMPTY_SET;
		}

		/*
		 * @see org.essentialplatform.transaction.IChange#getDescription()
		 */
		public String getDescription() {
			return __description;
		}

		/*
		 * @see org.essentialplatform.transaction.IChange#getExtendedInfo()
		 */
		public Object[] getExtendedInfo() {
			return __extendedInfo;
		}

		/**
		 * Always returns 0.
		 * 
		 * <p>
		 * Well, it is the Null object.
		 */
		public int hashCode() {
			return 0;
		}

		public boolean equals(Object other) {
			if (!getClass().equals(other.getClass())) {
				return false;
			}
			return true;
		}

		/*
		 * @see org.essentialplatform.runtime.transaction.changes.IChange#getTransaction()
		 */
		public ITransaction getTransaction() {
			return null;
		}

		private IChange _parent;
		public IChange getParent() {
			return _parent;
		}
		public void setParent(final IChange parent) {
			this._parent = parent;
		}
	}

	/**
	 * Indicates that this change in effect does nothing (eg pre- and post-
	 * values are the same).
	 * 
	 * @return
	 */
	public boolean doesNothing();

	/**
	 * The parent change within which this change was performed.
	 * 
	 * <p>
	 * May be null if the change was initiated "directly" (ie by an explicit
	 * user gesture such as typing in a field in the UI).
	 * 
	 * @return
	 */
	public IChange getParent();

	/**
	 * Sets the owning parent of this change.
	 * @param set
	 */
	public void setParent(IChange change);

}
