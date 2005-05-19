package de.berlios.rcpviewer.progmodel.rcpviewer;

import org.eclipse.jface.resource.ImageDescriptor;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.standard.DomainClass;

/**
 * Tests for the use of the <tt>ImageUrlAt</tt> annotations.
 *
 * <p>
 * TODO: should fix the URL ref.
 * 
 * @author Dan Haywood
 */
public class TestImageDescriptor extends AbstractTestCase {

	@InDomain
    @ImageUrlAt("http://www.eclipse.org/artwork/builtoneclipse/images/bui_eclipse_pos_logo_fc_sm.jpg")
	public static class ProspectiveSale {
	}

	private Domain domain;
	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
		domain = Domain.instance();
	}

	protected void tearDown() throws Exception {
		domain.reset();
		domain = null;
		super.tearDown();
	}
	
	public void testDomainClassWithImageUrlAt() {
		domain.addExtension(new RcpViewerExtension());
		domainClass = domain.lookup(ProspectiveSale.class);
		domain.done();
		ImageDescriptor id = (ImageDescriptor)domainClass.getAdapter(ImageDescriptor.class);
		assertNotNull(id);
	}

	/**
	 * currently failing: still returning an ImageDescriptor even if no extension installed ...
	 *
	 */
	public void incompletetestDomainClassWithImageUrlAtWithoutExtensionInstalled() {
		domainClass = new DomainClass<ProspectiveSale>(ProspectiveSale.class);
		ImageDescriptor id = (ImageDescriptor)domainClass.getAdapter(ImageDescriptor.class);
		assertNull(id);
	}

	
}
