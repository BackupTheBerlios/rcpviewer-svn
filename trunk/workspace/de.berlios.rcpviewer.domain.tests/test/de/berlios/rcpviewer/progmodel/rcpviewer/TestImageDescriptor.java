package de.berlios.rcpviewer.progmodel.rcpviewer;

import org.eclipse.jface.resource.ImageDescriptor;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;

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


	private IDomainClass<?> domainClass;
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
		
		RcpViewerDomainClass rcpViewerDC = 
			domainClass.getAdapter(RcpViewerDomainClass.class);
		ImageDescriptor id = rcpViewerDC.imageDescriptor();
		assertNotNull(id);
	}

	/**
	 * currently failing: still returning an ImageDescriptor even if no builder installed ...
	 *
	 */
	public void incompletetestDomainClassWithImageUrlAtWithoutBuilderInstalled() {
		domainClass = lookupAny(ProspectiveSale.class);
		RuntimeDomain.instance().done();
		
		ImageDescriptor id = (ImageDescriptor)domainClass.getAdapter(ImageDescriptor.class);
		assertNull(id);
	}

	
}
