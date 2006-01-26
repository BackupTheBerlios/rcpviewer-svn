//$Id: Tree.java,v 1.1 2005/05/12 13:33:32 epbernard Exp $
package org.hibernate.test.annotations.entity;

import org.hibernate.annotations.Proxy;

import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;

/**
 * Non lazy entity
 * @author Emmanuel Bernard
 */
@Entity
@Proxy(lazy=false)
public class Tree {
	private Integer id;
	private String name;
    @Id(generate=GeneratorType.AUTO)
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
}
