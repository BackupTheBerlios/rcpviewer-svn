package org.essentialplatform.core.deployment;

import org.essentialplatform.core.deployment.Binding;
import org.springframework.beans.factory.InitializingBean;

/**
 * Exists solely to install its provided Binding onto the ThreadLocal using
 * {@link Binding}.
 * 
 * <p>
 * Will be installed automatically when all properties set <i>unless</i> 
 * the {@link #setInstall(boolean)} is set to <tt>false</tt>. 
 * 
 * @author Dan Haywood
 */
public class BindingInstaller implements InitializingBean {


	private IBinding _binding;
	/**
	 * For dependency injection.
	 * 
	 * @param binding
	 */
	public void setBinding(IBinding binding) {
		_binding = binding;
	}
	
	/**
	 * Sets the {@link Binding} for the current thread.
	 */
	public void afterPropertiesSet() throws Exception {
		if (_install) {
			install();
		}
	}


	/**
	 * For programmatic invocation if bindings are not installed automatically
	 * (that is, if {@link #setInstall(boolean)} has been set to <tt>false</tt>.
	 *
	 */
	public void install() {
		Binding.setBinding(_binding);
	}
	

	private boolean _install = true;
	/**
	 * Whether to install the bindings.
	 * 
	 * <p>
	 * Optional; default is <tt>true</tt>.
	 * 
	 * @param install
	 */
	public void setInstall(boolean install) {
		_install = install;
	}

}
