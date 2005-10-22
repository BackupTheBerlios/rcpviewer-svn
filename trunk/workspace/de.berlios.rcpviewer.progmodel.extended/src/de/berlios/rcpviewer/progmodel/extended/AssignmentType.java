package de.berlios.rcpviewer.progmodel.extended;

/**
 * The way in which the id is assigned.
 */
public enum AssignmentType {
	/**
	 * The identifier is assigned by the application.
	 * 
	 * <p>
	 * All attributes that make up the identifier must be assigned by
	 * application logic before the object is persisted.
	 */
	APPLICATION,
	/**
	 * The identifier is assigned by the objectstore.
	 * 
	 * <p>
	 * The attribute making up the identifier is assigned by the object
	 * store.
	 * 
	 * <p>
	 * For most object stores, there will be an additional constraint
	 * that the identifier must be simple (consist of only a single
	 * attribute) and be an integral number.
	 */
	OBJECT_STORE,
	/**
	 * The identifier is assigned according dependent upon the context in which
	 * it has been used.
	 * 
	 * <p>
	 * The rules are as follows:
	 * <ul>
	 * <li>If the identifier is composite (that is, there are multiple attributes
	 *     annotated with <tt>Id</tt>), then the assignment is 
	 *     application-defined.
	 * <li>If the identifier is simple (that is, there is only one attribute 
	 *     annotated with <tt>Id</tt>), but the attribute is not an integer number,
	 *     then again the assignment is application-defined.
	 * <li>If the identifier is simple and is for an integer number, then the
	 *     assigned is defaulted to be <i>by the object store</i>.
	 * </ul>
	 */
	CONTEXT,
}
