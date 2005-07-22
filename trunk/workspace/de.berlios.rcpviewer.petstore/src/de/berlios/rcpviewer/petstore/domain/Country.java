package de.berlios.rcpviewer.petstore.domain;

import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.extended.Lookup;
import de.berlios.rcpviewer.progmodel.extended.Mask;
import de.berlios.rcpviewer.progmodel.extended.Order;

/**
 * Country, as defined by ISO3166.
 * 
 * <p>
 * <i>
 * Programming Model notes: annotated with <code>@Lookup</code> meaning that
 * it is implicitly immutable, and should be rendered appropriately whenever
 * referenced (eg drop-down box).  Since the object is immutable, there are 
 * no mutators for any of the attributes.  
 * </i>
 *
 * @link http://www.iso.org/iso/en/prods-services/iso3166ma/index.html
 * 
 * <p>
 * <i>
 * Programming Model notes:
 * <ul>
 * <li> ...
 * </ul>
 * </i>
 * @author Dan Haywood
 *
 */
@Lookup
@InDomain
public class Country {

	/**
	 * No-arg constructor for persistence layer.
	 *
	 */
	public Country() {
	}

	
	/**
	 * Two character abbreviation for this country.
	 * 
	 * <p>
	 * <i>
	 * Programming Model notes: 
	 * <ul> 
	 * <li> max length and min length inferred from the presence of the 
	 *      <code>@Mask</code> annotation (in this case, 2 characters). 
	 * <li> mandatory (since no <code>Optional</code>tag.
	 * </ul> 
	 * </i>
	 * 
	 * @return
	 */
	@Order(1)
	@Mask("AA")
	@DescribedAs("Two letter code for a country, eg US, GB or DE")
	public String getIsoA2Code() {
		return isoA2Code;
	}
	/**
	 * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> Immutable since (a) <code>private</code> visibility and (b) type
     *      is annotated as a <code>@Lookup</code>
     * <li> Provided for the persistence layer.
     * </ul>
     * </i>
	 *  
	 * @param isoA2Code
	 */
	private void setIsoA2Code(final String isoA2Code) {
		this.isoA2Code = isoA2Code;
	}
	private String isoA2Code;

	

	/**
	 * Two character abbreviation for this country.
	 * 
	 * <p>
	 * <i>
	 * Programming Model notes: 
	 * <ul> max length and min length inferred from the presence of the 
	 *      <code>@Mask</code> annotation (in this case, 2 characters). 
	 * <ul> mandatory (since no <code>Optional</code>tag. 
	 * </i>
	 * 
	 * @return
	 */
	@Order(2)
	@Mask("AAA")
	@DescribedAs("Three letter code for a country, eg USA, GBR or DEU")
	public String getIsoA3Code() {
		return _isoA3Code;
	}
	/**
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
	 * 
	 * @param isoA2Code
	 */
	private void setIsoA3Code(String isoA3Code) {
		this._isoA3Code = isoA3Code;
	}
	private String _isoA3Code;

	
	
	@Order(3)
	@Mask("000")
	@DescribedAs("Three digit Identifies a physical territory")
	public String getIsoNumber() {
		return isoNumber;
	}
	/**
	 * <p>
	 * <i>
	 * Programming Model notes: used only programmatically; declared as 
	 * <code>@Lookup</code> and so immutable.
	 * </i>
	 *  
	 * @param isoA2Code
	 */
	public void setIsoNumber(String isoNumber) {
		this.isoNumber = isoNumber;
	}
	private String isoNumber;
	
	

	/**
	 * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
	 */
	@Order(4)
	@DescribedAs("Name of country or (sometimes) geographically separate territories")
	public String getName() {
		return _name;
	}
	/**
	 * <p>
	 * <i>
	 * Programming Model notes: used only programmatically; declared as 
	 * <code>@Lookup</code> and so immutable.
	 * </i>
	 */
	public void setName(final String name) {
		_name = name;
	}
	private String _name;

	

	/**
	 * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
	 */
	@Order(5)
	@DescribedAs("Whether this country is on ISO's withdrawn list")
	public boolean is_withdrawn() {
		return _withdrawn;
	}
	private boolean _withdrawn;
}
