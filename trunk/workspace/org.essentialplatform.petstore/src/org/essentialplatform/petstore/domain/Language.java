package org.essentialplatform.petstore.domain;

import org.essentialplatform.progmodel.essential.app.DescribedAs;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Lookup;
import org.essentialplatform.progmodel.essential.app.Mask;
import org.essentialplatform.progmodel.essential.app.MaxLengthOf;
import org.essentialplatform.progmodel.essential.app.RelativeOrder;

/**
 * Language, as defined by ISO639-1.
 * 
 * <p>
 * From ISO's own definition: ISO 639-1 provides a code consisting of language 
 * code elements comprising two-letter language identifiers for the 
 * representation of names of languages. The language identifiers according to 
 * this part of ISO 639 were devised originally for use in terminology, 
 * lexicography and linguistics, but may be adopted for any application 
 * requiring the expression of language in two-letter coded form, especially 
 * in computerized systems. The alpha-2 code was devised for practical use for 
 * most of the major languages of the world that are not only most frequently 
 * represented in the total body of the world's literature, but which also 
 * comprise a considerable volume of specialized languages and terminologies. 
 * Additional language identifiers are created when it becomes apparent that a 
 * significant body of documentation written in specialized languages and 
 * terminologies exists. Languages designed exclusively for machine use, such 
 * as computer-programming languages, are not included in this code.
 * 
 * <p>
 * <i>
 * Programming Model notes:
 * <ul>
 * <li> the {@link Lookup} annotation indicates that this object is
 *      implicitly immutable and moreover should be rendered appropriately 
 *      whenever referenced (eg as a drop-down box).  It also means that the
 *      save operation is implicitly disabled.
 * <li> Any mutators should have <code>private</code> visibility, used by the 
 *      platform when recreating instances from the persistence layer.
 * </ul>
 * </i>
 *
 * <p>
 * TODOs
 * <ul>
 * <li>TODO: Hibernate mapping to ???
 * </ul>
 * 
 * @link http://www.w3.org/WAI/ER/IG/ert/iso639.htm
 * @author Dan Haywood
 *
 */
@Lookup
@InDomain
public class Language {

	/**
	 * 
	 *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
	 */
	public Language() {
	}

	
	/**
	 * Although not every language has a two letter code, we have restricted
	 * the list of languages to be only those that do.
	 * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
	 */
	@RelativeOrder(1)
	@Mask("AA")
	@DescribedAs("Two letter code for the language.")
	public String getIsoA2Code() {
		return _isoA2Code;
	}
	public void setIsoA2Code(String isoA2Code) {
		this._isoA2Code = isoA2Code;
	}
	private String _isoA2Code;
	

	/**
	 * 
	 *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
	 */
	@RelativeOrder(2)
	@Mask("AAA")
	@DescribedAs("Three letter code for the language.")
	public String getIsoA3Code() {
		return _isoA3Code;
	}
	public void setIsoA3Code(String isoA3Code) {
		this._isoA3Code = isoA3Code;
	}
	private String _isoA3Code;
	

	/**
	 * 
	 *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
	 */
	@RelativeOrder(3)
	@MaxLengthOf(30)
	@DescribedAs("Name of the language.")
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}
	private String _name;
}
