//$Id: PropertyDefaultMappingsTest.java,v 1.1 2005/05/12 13:33:32 epbernard Exp $
package org.hibernate.test.annotations.entity;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.test.annotations.TestCase;

/**
 * @author Emmanuel Bernard
 */
public class PropertyDefaultMappingsTest extends TestCase {
	public PropertyDefaultMappingsTest(String x) {
		super(x);
	}

	public void testSerializableObject() throws Exception {
		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		Country c = new Country();
		c.setName("France");
		Address a = new Address();
		a.setCity("Paris");
		a.setCountry(c);
		s.persist(a);
		tx.commit();
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		Address reloadedAddress = (Address) s.get( Address.class, a.getId() );
		assertNotNull(reloadedAddress);
		assertNotNull( reloadedAddress.getCountry() );
		assertEquals( a.getCountry().getName(), reloadedAddress.getCountry().getName() );
		tx.rollback();
		s.close();
	}

	protected Class[] getMappings() {
		return new Class[] {
			Address.class
		};
	}
}
