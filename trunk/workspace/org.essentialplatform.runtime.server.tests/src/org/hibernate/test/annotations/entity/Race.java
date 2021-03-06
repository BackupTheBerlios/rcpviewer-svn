//$Id: Race.java,v 1.1 2005/05/12 13:33:32 epbernard Exp $
package org.hibernate.test.annotations.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Emmanuel Bernard
 */
@Entity
public class Race implements Length<Long> {
	private Long length;
	private Integer id;
    @Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}
}
