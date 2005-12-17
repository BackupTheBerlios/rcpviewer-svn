//$Id: Length.java,v 1.1 2005/05/12 13:33:32 epbernard Exp $
package org.hibernate.test.annotations.entity;

/**
 * @author Emmanuel Bernard
 */
public interface Length<Type> {
	Type getLength();
}
