//$Id: ZipCode.java,v 1.1 2005/06/27 17:02:08 epbernard Exp $
package org.hibernate.test.annotations.entity;

import javax.persistence.Entity;
import javax.persistence.AccessType;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Emmanuel Bernard
 */
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Entity(access = AccessType.FIELD)
@org.hibernate.annotations.Entity(mutable = false)
public class ZipCode {
	@Id
	public String code;
}
