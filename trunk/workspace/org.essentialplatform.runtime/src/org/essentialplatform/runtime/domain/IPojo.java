package org.essentialplatform.runtime.domain;

import org.essentialplatform.runtime.transaction.ITransactable;



public interface IPojo extends ITransactable {

	// TODO: <T>
	IDomainObject getDomainObject();
}
