package mikespike3.model;

import mikespike3.EasyBeanExample;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;

import de.berlios.rcpviewer.domain.Domain;

public class EasyBeanDomain 
extends Domain
{

	public EasyBeanDomain()
	throws CoreException
	{
		super(EasyBeanExample.PLUGIN_ID);

		// register classes
		if (lookup(EasyBean.class) == null) {
			String msg= "Failed to register class in domain:"+EasyBean.class.getName();
			Status status= new Status(Status.ERROR, EasyBeanExample.PLUGIN_ID, 0, msg, null);
			throw new CoreException(status);
		}
	}

}
