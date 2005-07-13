package de.berlios.rcpviewer.session;

import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.SessionManager;

public abstract aspect PojoAspect {
	
	/**
	 * All pojos that are have an {@link de.berlios.rcpviewer.progmodel.standard.InDomain} 
	 * annotation should implement {@link de.berlios.rcpviewer.session.IPojo}. 
	 */
	declare parents: (@InDomain *) implements IPojo;

	/**
	 * Introduce implementation of {@link IPojo} to @InDomain pojos.
	 * 
	 * JAVA_5_FIXME
	 */
	public IDomainObject IPojo.getDomainObject() {
		InDomain inDomain = this.getClass().getAnnotation(InDomain.class);
		String domainName = inDomain.value();
		RuntimeDomain domain = RuntimeDomain.instance(domainName);
		SessionManager sessionManager = SessionManager.instance(); 
		for(ISession session: sessionManager.getAllSessions()) {
			if (domain != session.getDomain()) {
				continue;
			}
			if (!session.hasDomainObjectFor(this)) {
				continue;
			}
			return session.getDomainObjectFor(this, this.getClass());
		}
		return null;
	}

	/**
	 * used for percflow: an aspect is instantiated each time an operation
	 * is invoked on a pojo
	 * 
	 * <p>
	 * protected for sub-aspects
	 */
	protected pointcut invokeOperationOnPojo(IPojo pojo): 
		invokePublicMethodOnPojo(pojo) &&
		!invokeAccessorOnPojo(Object) &&
		!invokeMutatorOnPojo(Object, Object) &&
		!within(PojoAspect);
	
	/**
	 * protected for sub-aspects
	 */
	protected pointcut invokePublicMethodOnPojo(IPojo pojo): 
		execution(public * IPojo+.*(..)) && this(pojo);
	
	/**
	 * protected for sub-aspects
	 */
	protected pointcut invokeAccessorOnPojo(IPojo pojo): 
		execution(public !void IPojo+.get*()) && this(pojo);
	
	/**
	 * protected for sub-aspects
	 */
	protected pointcut invokeMutatorOnPojo(IPojo pojo, Object postValue): 
		execution(public void IPojo+.set*(*)) && this(pojo) && args(postValue);
	
	/**
	 * Capture an attribute being changed on some pojo.
	 * 
	 * <p>
	 * The pojo being changed could either be the one on which the operation
	 * has been invoked, or could be some other pojo entirely.
	 * 
	 * <p>
	 * protected for sub-aspects
	 * 
	 */
	protected pointcut modifyAttributeOnPojo(IPojo pojo, Object postValue) :
		args(postValue) && this(pojo) && set(* IPojo+.*);

	
	/**
	 * Capture a pojo being instantiated.
	 */
	public pointcut instantiatePojo(IPojo pojo): 
		execution(IPojo+.new(..)) && this(pojo);

	
}
