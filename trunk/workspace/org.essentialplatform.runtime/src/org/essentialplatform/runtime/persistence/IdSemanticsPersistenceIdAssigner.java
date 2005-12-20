package org.essentialplatform.runtime.persistence;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.progmodel.essential.app.AssignmentType;
import org.essentialplatform.runtime.domain.IDomainObject;

import org.essentialplatform.runtime.persistence.IPersistenceIdAssigner;

/**
 * Delegates to either a {@link CompositeIdPersistenceIdAssigner} or the 
 * supplied {@link IPersistenceIdAssigner} dependent upon the semantics of the 
 * <tt>@Id</tt> annotation of the supplied {@link IDomainClass}.
 * 
 * @author Dan Haywood
 *
 */
public final class IdSemanticsPersistenceIdAssigner extends AbstractPersistenceIdAssigner {

	private final IPersistenceIdAssigner _persistenceIdAssigner;
	private final IDomainClass _domainClass;

	public IdSemanticsPersistenceIdAssigner(IDomainClass domainClass, IPersistenceIdAssigner delegatePersistenceIdAssigner) {
		super(null);
		
		_domainClass = domainClass;
		IPersistenceIdAssigner compositePersistenceIdAssigner = new CompositeIdPersistenceIdAssigner(domainClass);

		// figure out which IPersistenceIdAssigner to delegate to. 
		AssignmentType idAssignmentType = _domainClass.getIdAssignmentType();
		
		if (idAssignmentType == AssignmentType.APPLICATION) {
			_persistenceIdAssigner = compositePersistenceIdAssigner;
		} else if (idAssignmentType == AssignmentType.OBJECT_STORE) {
			_persistenceIdAssigner = delegatePersistenceIdAssigner;
		} else if (idAssignmentType == AssignmentType.CONTEXT) {
			if (domainClass.isSimpleId()) {
				_persistenceIdAssigner = delegatePersistenceIdAssigner;
			} else {
				_persistenceIdAssigner = compositePersistenceIdAssigner;
			}
		} else {
			// never happen
			_persistenceIdAssigner = null;
		}
	}

	@Override
	protected <T> PersistenceId doGeneratePersistenceIdFor(IDomainObject<T> domainObject) {
		assert _domainClass == domainObject.getDomainClass();
		return _persistenceIdAssigner.assignPersistenceIdFor(domainObject);
	}

}
