package org.essentialplatform.runtime.progmodel.standard;

import org.essentialplatform.core.domain.builders.AbstractCompositeDomainBuilder;
import org.essentialplatform.core.domain.builders.IDomainBuilder;


public final class EssentialProgModelDomainBuilder extends AbstractCompositeDomainBuilder {

	public EssentialProgModelDomainBuilder() {
		super(new IDomainBuilder[]{
				new EssentialProgModelStandardSemanticsDomainBuilder(),
				new EssentialProgModelExtendedSemanticsDomainBuilder(),
		});
	}
}
