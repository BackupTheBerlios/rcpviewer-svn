package org.essentialplatform.progmodel.standard;

import org.essentialplatform.domain.AbstractCompositeDomainBuilder;
import org.essentialplatform.domain.IDomainBuilder;


public final class EssentialProgModelDomainBuilder extends AbstractCompositeDomainBuilder {

	public EssentialProgModelDomainBuilder() {
		super(new IDomainBuilder[]{
				new EssentialProgModelStandardSemanticsDomainBuilder(),
				new EssentialProgModelExtendedSemanticsDomainBuilder(),
		});
	}
}
