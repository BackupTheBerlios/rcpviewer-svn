//$Id: Address.java,v 1.2 2005/07/26 04:57:09 epbernard Exp $
package org.hibernate.test.annotations.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Has a serializable class as a property
 *
 * @author Emmanuel Bernard
 */
@Entity
@Table(name="serial_address")
public class Address {
	private Integer id;
	private String city;

	@Id(generate=GeneratorType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

    private Country country;
}
