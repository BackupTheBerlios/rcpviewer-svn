//$Id: CommunityBid.java,v 1.1 2005/08/25 21:47:39 epbernard Exp $
package org.hibernate.test.annotations.entity;

import javax.persistence.Entity;
import javax.persistence.AccessType;

/**
 * @author Emmanuel Bernard
 */
@Entity(access= AccessType.FIELD)
public class CommunityBid extends Bid {
	public Starred communityNote;
}
