package org.essentialplatform.server.tests.hibernate;

import junit.framework.TestCase;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

public class TestHibernateClassLoading extends TestCase /*org.hibernate.test.annotations.TestCase*/ {

	public void testLoadDriver() throws ClassNotFoundException {
		AnnotationConfiguration cfg = new AnnotationConfiguration();
	}

//	@Override
//	protected Class[] getMappings() {
//		return new Class[]{};
//	}



}
