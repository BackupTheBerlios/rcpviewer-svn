package org.essentialplatform.runtime.shared.domain.handle;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.progmodel.essential.app.AssignmentType;

import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;

/**
 * Delegates to either a {@link CompositeIdHandleAssigner} or the 
 * supplied {@link IHandleAssigner} dependent upon the semantics of the 
 * <tt>@Id</tt> annotation of the supplied {@link IDomainClass}.
 * 
 * @author Dan Haywood
 *
 */
public final class IdSemanticsHandleAssigner extends AbstractHandleAssigner {

	private final IHandleAssigner _handleAssigner;
	private final IDomainClass _domainClass;

	public IdSemanticsHandleAssigner(IDomainClass domainClass, IHandleAssigner delegateHandleAssigner) {
		super(null);
		
		_domainClass = domainClass;
		IHandleAssigner compositeHandleAssigner = new CompositeIdHandleAssigner(domainClass);

		// figure out which IHandleAssigner to delegate to. 
		AssignmentType idAssignmentType = _domainClass.getIdAssignmentType();
		
		if (idAssignmentType == AssignmentType.APPLICATION) {
			_handleAssigner = compositeHandleAssigner;
		} else if (idAssignmentType == AssignmentType.OBJECT_STORE) {
			_handleAssigner = delegateHandleAssigner;
		} else if (idAssignmentType == AssignmentType.CONTEXT) {
			if (domainClass.isSimpleId()) {
				_handleAssigner = delegateHandleAssigner;
			} else {
				_handleAssigner = compositeHandleAssigner;
			}
		} else {
			// never happen
			_handleAssigner = null;
		}
	}

	@Override
	protected <T> Handle doGenerateHandleFor(IDomainObject<T> domainObject) {
		assert _domainClass == domainObject.getDomainClass();
		return _handleAssigner.assignHandleFor(domainObject);
	}

}
