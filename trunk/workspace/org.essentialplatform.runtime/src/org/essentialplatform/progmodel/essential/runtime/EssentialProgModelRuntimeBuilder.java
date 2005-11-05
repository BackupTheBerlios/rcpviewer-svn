package org.essentialplatform.progmodel.essential.runtime;

import org.essentialplatform.core.domain.builders.AbstractCompositeDomainBuilder;
import org.essentialplatform.core.domain.builders.IDomainBuilder;


public final class EssentialProgModelRuntimeBuilder extends AbstractCompositeDomainBuilder {

	public EssentialProgModelRuntimeBuilder() {
		super(new IDomainBuilder[]{
				new EssentialProgModelStandardRuntimeBuilder(),
				new EssentialProgModelExtendedRuntimeBuilder(),
		});
	}
}
