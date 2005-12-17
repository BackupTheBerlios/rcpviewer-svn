//$Id: Ransom.java,v 1.1 2005/05/16 17:43:08 epbernard Exp $
package org.hibernate.test.annotations.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratorType;
import javax.persistence.Column;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Columns;

/**
 * @author Emmanuel Bernard
 */
@Entity
public class Ransom {
	private Integer id;
	private String kidnapperName;
	private MonetaryAmount amount;
	private Date date;

	@Id(generate=GeneratorType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKidnapperName() {
		return kidnapperName;
	}

	public void setKidnapperName(String kidnapperName) {
		this.kidnapperName = kidnapperName;
	}

	@Type(type="org.hibernate.test.annotations.entity.MonetaryAmountUserType")
	@Columns(columns = {
			@Column(name="r_amount"),
			@Column(name="r_currency")
			})
	public MonetaryAmount getAmount() {
		return amount;
	}

	public void setAmount(MonetaryAmount amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
