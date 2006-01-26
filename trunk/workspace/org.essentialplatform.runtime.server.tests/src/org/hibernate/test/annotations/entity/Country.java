//$Id: Country.java,v 1.2 2005/06/27 17:02:08 epbernard Exp $
package org.hibernate.test.annotations.entity;

import java.io.Serializable;

/**
 * Serializable object to be serialized in DB as is
 * @author Emmanuel Bernard
 */
public class Country implements Serializable {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
