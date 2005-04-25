package de.berlios.rcpviewer.metamodel;

import de.berlios.rcpviewer.metamodel.link.LinkSemanticsType;

/**
 * Represents the semantics of a link for both participants.
 * 
 * TODO: not yet in use, but part of the supporting structure for interpreting 
 * pojos.
 */
public abstract class LinkSemantics {

	private final LinkSemanticsType linkSemanticsType;
	private boolean aggregated;
	private boolean optional;
	
	/**
	 * Creates LinkSemantics of specified type, defaulting to be regular
	 * reference (not aggregated) and optional.
	 * 
	 * <p>
	 * Use {@link #makeAggregated()) and {@link #makeMandatory()} if necessary
	 * to override these defaults.
	 * 
	 * @param linkSemanticsType
	 */
	protected LinkSemantics(final LinkSemanticsType linkSemanticsType) {
		this.linkSemanticsType = linkSemanticsType;
		this.aggregated = false;
		this.optional = true;
	}
	
	/**
	 * Indicates whether the object that is referenced by the link that has this
	 * semantic (in the direction implied by the link's references <i>to</i>
	 * this semantic) is aggregated (rather than a regular reference).
	 * 
	 * <p>
	 * In other words, for a {@link #Link} from x to y, then the 
	 * {@link LinkSemantics} with role {@link LinkSemantics#getSemanticsOfYFromX()} 
	 * that has its {@link #isAggregated()} set to <code>true</code> means that y 
	 * is aggregated by x.
	 * 
	 * <p>
	 * The converse relationship is composition, so another way of saying the
	 * above statement is that x is the composite (parent) and y is a 
	 * aggregated component child.
	 * 
	 * <p>
	 * On the other hand, if a {@link #Link} from x and y has a 
	 * {@link LinkSemantics} in the role {@link LinkSemantics#getSemanticsOfXFromY()}
	 * with {@link #isAggregated()} set, then it means that y is the composite 
	 * and x is the component.
	 *
	 * <p>
	 * It is not valid for both {@link LinkSemantics} associated with a
	 * {@link Link} to be set as aggregated (but this is not enforced by 
	 * this class).
	 */
	public boolean isAggregated() {
		return aggregated;
	}
	/**
	 * @post {@link #isAggregated()} is true. 
	 */
	public final void makeAggregated() {
		this.aggregated = true;
	}
	
	/**
	 * @post #isAggregate() is false.
	 */
	public final void makeReference() {
		this.aggregated = false;
	}

	
	/**
	 * Indicates whether the object that is referenced by the link that has this
	 * semantic (in the direction implied by the link's references <i>to</i>
	 * this semantic) is optional (rather than mandatory).
	 * 
	 * <p>
	 * In other words, for a {@link #Link} from x to y, then the 
	 * {@link LinkSemantics} with role {@link LinkSemantics#getSemanticsOfYFromX()} 
	 * that has its {@link #isOptional()} set to <code>true</code> means that y 
	 * is optional for x (there may be no y referenced by x).
	 * 
	 * <p>
	 * On the other hand, if a {@link #Link} from x and y has a 
	 * {@link LinkSemantics} in the role {@link LinkSemantics#getSemanticsOfXFromY()}
	 * with {@link #isOptional()} set, then it means that x is optional for y 
	 * (there may be no x referenced by y).
	 *
	 * <p>
	 * It is not valid for a link to have optional semantics in one direction
	 * but to have aggregated semantics in the other.  For example, it would be
	 * invalid to have a link from x to y with semantics of aggregated (y is a
	 * component of x), but with that link's semantics from y to x to be
	 * optional (there could be no x for y).  However, this class does not
	 * enforce this rule. 
	 */
	public final boolean isOptional() {
		return this.optional;
	}
	/**
	 * @post #isOptional() is false
	 */
	public final void makeMandatory() {
		this.optional = false;
	}
	
	/**
	 * @post #isOptional() is true and #isComposite() is false
	 */
	public final void makeOptional() {
		this.optional = true;
		this.aggregated = false;
	}

	/**
	 * opposite of {@link #isOptional()}
	 */
	public final boolean isMandatory() {
		return !isOptional();
	}
	

	public final LinkSemanticsType getType() {
		return linkSemanticsType;
	}
}
