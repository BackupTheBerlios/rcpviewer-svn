package mikespike3.model;

import mikespike3.EasyBeanExample;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;

import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.runtime.AbstractDomainBootstrap;
import de.berlios.rcpviewer.domain.runtime.IDomainBootstrap;

// REVIEW_CHANGE for ted: was EasyBeanDomain.  Instead, now bootstraps our domain.  That's because compile-time domains will always be implicitly registered (we just infer them from Java AST).  It's only in the runtime that we need to do any sort of explicit registration.
public class EasyBeanDomainBootstrap 
extends AbstractDomainBootstrap
{

	public EasyBeanDomainBootstrap()
	throws CoreException
	{
		super(EasyBeanExample.PLUGIN_ID);
	}
	
	@Override
	public void registerClasses() throws CoreException {
		registerClass(EasyBean.class);
	}

}
