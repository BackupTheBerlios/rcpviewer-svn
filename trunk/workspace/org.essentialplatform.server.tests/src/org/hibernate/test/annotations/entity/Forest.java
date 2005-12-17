//$Id: Forest.java,v 1.4 2005/12/06 17:42:48 epbernard Exp $
package org.hibernate.test.annotations.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.LobType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.PolymorphismType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.Index;

/**
 * Use hibernate specific annotations
 * @author Emmanuel Bernard
 */
@Entity
@BatchSize(size=5)
@org.hibernate.annotations.Entity(
		selectBeforeUpdate = true,
		dynamicInsert = true, dynamicUpdate = true,
		optimisticLock = OptimisticLockType.ALL,
		polymorphism = PolymorphismType.EXPLICIT)
@Where(clause="1=1")
@FilterDef(name="minLength", parameters={ @ParamDef( name="minLength", type="integer" ) } )
@Filters( {
	@Filter(name="betweenLength"),
	@Filter(name="minLength", condition=":minLength <= length")
} )
@org.hibernate.annotations.Table(name="Forest", indexes = { @Index(name="idx", columnNames = { "name", "length" } ) } )
public class Forest {
	private Integer id;
	private String name;
	private long length;
	private String longDescription;
	private String smallText;
	private String bigText;
	private Country country;

	@Type(type="text")
	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	@Id(generate = GeneratorType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Type(type="caster")
	public String getSmallText() {
		return smallText;
	}

	@Type(type="caster", parameters = { @Parameter( name="cast", value = "upper" ) } )
	public String getBigText() {
		return bigText;
	}

	public void setSmallText(String smallText) {
		this.smallText = smallText;
	}

	public void setBigText(String bigText) {
		this.bigText = bigText;
	}

	@Lob(type=LobType.BLOB)
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
}
