package de.berlios.rcpviewer.progmodel.standard;

import de.berlios.rcpviewer.domain.AbstractCompositeDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainBuilder;


public final class EssentialProgModelDomainBuilder extends AbstractCompositeDomainBuilder {

	public EssentialProgModelDomainBuilder() {
		super(new IDomainBuilder[]{
				new EssentialProgModelStandardSemanticsDomainBuilder(),
				new EssentialProgModelExtendedSemanticsDomainBuilder(),
		});
	}
}
