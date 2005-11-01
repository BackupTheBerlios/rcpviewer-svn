package org.essentialplatform.progmodel.rcpviewer;

import org.eclipse.jface.resource.ImageDescriptor;

import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.IDeploymentSpecifics;
import org.essentialplatform.domain.Domain;
import org.essentialplatform.domain.IDomainBuilder;
import org.essentialplatform.domain.IDomainClass;

/**
 * Tests for the use of the <tt>ImageUrlAt</tt> annotations.
 *
 * <p>
 * TODO: should fix the URL ref.
 * 
 * @author Dan Haywood
 */
public abstract class TestImageDescriptor extends AbstractTestCase {

	public TestImageDescriptor(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainAnalyzer) {
		super(domainSpecifics, domainAnalyzer);
	}


	private IDomainClass domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
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
		Domain.instance().done();
		
		ImageDescriptor id = (ImageDescriptor)domainClass.getAdapter(ImageDescriptor.class);
		assertNull(id);
	}

	
}
