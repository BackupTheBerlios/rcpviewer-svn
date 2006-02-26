package org.essentialplatform.core.springsupport;

import org.essentialplatform.core.IBundlePeer;
import org.osgi.framework.Bundle;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;

public abstract class AbstractBundleFactoryBean implements FactoryBean {

	public AbstractBundleFactoryBean() {}
	
	public AbstractBundleFactoryBean(IBundlePeer bundlePeer) {
		this();
		setBundlePeer(bundlePeer);
	}

	public AbstractBundleFactoryBean(Bundle bundle) {
		this();
		setBundle(bundle);
	}

	private IBundlePeer _bundlePeer;
	public void setBundlePeer(IBundlePeer bundlePeer) {
		_bundlePeer = bundlePeer;
	}


	private Bundle _bundle;
	public void setBundle(Bundle bundle) {
		_bundle = bundle;
	}


	/*
	 * Will return <tt>null</tt> if hasn't been configured.
	 * 
	 * <p>
	 * This can happen because we use 2 passes to build the context.
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		if (_bundle != null) {
			return _bundle;
		}
		if (_bundlePeer != null) {
			return _bundlePeer.getBundle();
		}
		return null;
	}

	public boolean isSingleton() {
		return true;
	}

	public final Class getObjectType() {
		return Bundle.class;
	}

	/**
	 * Type-safe downcast of {@link AbstractFactoryBean#getObject()}.
	 * 
	 * <p>
	 * Useful if manipulating programmatically, eg in tests.  Unlike the
	 * {@link AbstractFactoryBean#getObject()} call, though, if there are problems
	 * it will return <tt>null</tt> rather than throw an exception.
	 * 
	 * @return the bundle, or <tt>null</tt> if not found.
	 */
	public final Bundle getBundle() {
		try {
			return (Bundle)getObject();
		} catch (Exception ex) {
			return null;
		}
	}


}
