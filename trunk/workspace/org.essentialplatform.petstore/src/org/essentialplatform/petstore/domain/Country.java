package org.essentialplatform.petstore.domain;

import org.essentialplatform.progmodel.standard.DescribedAs;
import org.essentialplatform.progmodel.standard.InDomain;
import org.essentialplatform.progmodel.extended.IPrerequisites;
import org.essentialplatform.progmodel.extended.Lookup;
import org.essentialplatform.progmodel.extended.Mask;
import org.essentialplatform.progmodel.extended.RelativeOrder;
import org.essentialplatform.progmodel.extended.MinLengthOf;
import org.essentialplatform.progmodel.extended.MaxLengthOf;
import org.essentialplatform.progmodel.extended.Prerequisites;
import org.essentialplatform.progmodel.extended.SaveOperation;

/**
 * Country, as defined by ISO3166.
 * 
 * <p>
 * <i>
 * Programming Model notes:   
 * </i>
 *
 * <p>
 * TODOs
 * <ul>
 * <li>TODO: Hibernate mapping to ???
 * </ul>
 * <p>
 * 
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
 * @link http://www.iso.org/iso/en/prods-services/iso3166ma/index.html
 * 
 * @author Dan Haywood
 *
 */
@Lookup
@InDomain
public class Country {

	/**
	 * Constructor.
	 * 
     * <p>
     * <i>
     * See overview for discussion on programming model conventions and 
     * annotations.
     * </i>
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
     * <li> See overview for discussion on programming model conventions
     *      and annotations.
	 * </ul> 
	 * </i>
	 * 
	 * @return
	 */
	@RelativeOrder(1)
	@MaxLengthOf(2)
	@DescribedAs("Two letter code for a country, eg US, GB or DE")
	public String getIsoA2Code() {
		return isoA2Code;
	}
	private void setIsoA2Code(final String isoA2Code) {
		this.isoA2Code = isoA2Code;
	}
	private String isoA2Code;

	

	/**
	 * Three character abbreviation for this country.
	 * 
	 * <p>
	 * <i>
	 * Programming Model notes: 
	 * </i>
	 * 
	 * @return
	 */
	@RelativeOrder(2)
	@MaxLengthOf(2)
	@DescribedAs("Three letter code for a country, eg USA, GBR or DEU")
	public String getIsoA3Code() {
		return _isoA3Code;
	}
	private void setIsoA3Code(String isoA3Code) {
		this._isoA3Code = isoA3Code;
	}
	private String _isoA3Code;

	
	
	/**
	 * Three digit number for this country.
	 * 
	 * <p>
	 * <i>
	 * Programming Model notes:
	 * <ul>
     * <li> See overview for discussion on programming model conventions
     *      and annotations.
	 * </ul>
	 * </i>
	 * 
	 * @return
	 */
	@RelativeOrder(3)
	@Mask("000")
	@DescribedAs("Three digit Identifies a physical territory")
	public String getIsoNumber() {
		return isoNumber;
	}
	private void setIsoNumber(String isoNumber) {
		this.isoNumber = isoNumber;
	}
	private String isoNumber;
	
	

	/**
	 * Name of country or (sometimes) geographically separate territories.
	 * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> See overview for discussion on programming model conventions
     *      and annotations.
     * </ul>
     * </i>
	 */
	@RelativeOrder(4)
	@MaxLengthOf(50)
	@DescribedAs("Name of country or (sometimes) geographically separate territories.")
	public String getName() {
		return _name;
	}
	private void setName(final String name) {
		_name = name;
	}
	private String _name;

	

	/**
	 * Whether this country is on ISO's withdrawn list.
	 * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> See overview for discussion on programming model conventions
     *      and annotations.
     * </ul>
     * </i>
	 */
	@RelativeOrder(5)
	@DescribedAs("Whether this country is on ISO's withdrawn list.")
	public boolean isWithdrawn() {
		return _withdrawn;
	}
	private void setWithdrawn(final boolean withdrawn) {
		_withdrawn = withdrawn;
	}
	private boolean _withdrawn;
	


	
}
