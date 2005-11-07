package org.essentialplatform.progmodel.louis.core.tests;

import org.eclipse.jface.resource.ImageDescriptor;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.core.fixture.progmodel.louis.ProspectiveSale;
import org.essentialplatform.core.tests.AbstractTestCase;
import org.essentialplatform.progmodel.louis.core.domain.LouisDomainClass;

/**
 * Tests for the use of the <tt>ImageUrlAt</tt> annotations.
 *
 * <p>
 * TODO: should fix the URL ref.
 * 
 * @author Dan Haywood
 */
public abstract class TestImageDescriptor extends AbstractTestCase {

	public TestImageDescriptor(IDomainBuilder domainBuilder) {
		super(domainBuilder);
	}


	private IDomainClass domainClass;
	
	public void testDomainClassWithImageUrlAt() {
		domainClass = lookupAny(ProspectiveSale.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		LouisDomainClass rcpViewerDC = 
			domainClass.getAdapter(LouisDomainClass.class);
		ImageDescriptor id = rcpViewerDC.imageDescriptor();
		assertNotNull(id);
	}

	/**
	 * currently failing: still returning an ImageDescriptor even if no builder installed ...
	 *
	 */
	public void incompletetestDomainClassWithImageUrlAtWithoutBuilderInstalled() {
		domainClass = lookupAny(ProspectiveSale.class);
		
		ImageDescriptor id = (ImageDescriptor)domainClass.getAdapter(ImageDescriptor.class);
		assertNull(id);
	}

	
}
