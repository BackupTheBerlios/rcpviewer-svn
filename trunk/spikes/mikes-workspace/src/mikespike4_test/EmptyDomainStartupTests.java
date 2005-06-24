package mikespike4_test;

import mikespike4.GuiPlugin;
import mikespike4.rcp.ApplicationWorkbenchWindowAdvisor;
import mikespike4.views.classbar.ClassBarView;
import mikespike4_test.util.GuiReadUtil;

import org.eclipse.ui.PlatformUI;

import de.berlios.rcpviewer.domain.Domain;

/**
 * Thes are tests to try out the initialisation of the GUI with an empty
 * domain.
 * @author Mike
 *
 */
public class EmptyDomainStartupTests extends AbstractRcpTestCase {
	

	/**
	 * Test that the workbench opens.
	 */
	public void testWorkbenchOpened() {
		assertNotNull( GuiReadUtil.getActivePage() );
	}
	
	/**
	 * Test that resources can be read - use workbench title for this
	 */
	public void testWorkbenchTitle() {
		assertEquals(
				GuiPlugin.getResourceString(
						ApplicationWorkbenchWindowAdvisor.TITLE_KEY  ),
				PlatformUI.getWorkbench()
				          .getActiveWorkbenchWindow()
				          .getShell()
				          .getText() );
	}	
	
	/**
	 * Test that class bar instantiated
	 */
	public void testClassBarExists() {
		assertNotNull( GuiReadUtil.getViewPart( ClassBarView.ID ) );
	}	
	
	/**
	 * Test that the error case of no domain classes is reported to the user
	 */
	public void testEmptyDomainReported() {
		assertTrue( Domain.instance().classes().isEmpty() );
		assertEquals(
				GuiPlugin.getResourceString( ClassBarView.EMPTY_DOMAIN_MSG_KEY ),
				GuiReadUtil.getStatusLineErrorText() );
	}
}
