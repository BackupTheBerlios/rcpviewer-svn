package mikespike4.views.classbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mikespike4.GuiPlugin;
import mikespike4.util.ImageUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;

/**
 * Outlook-style bar for each class in Domain.
 * @author Mike
 */
public class ClassBarView extends ViewPart {

	public static final String ID = ClassBarView.class.getName();
	public static final String EMPTY_DOMAIN_MSG_KEY = "ClassBarView.EmptyDomain";

	private List<Button> buttons = new ArrayList<Button>();
	
	@Override
	public void createPartControl( final Composite parent) {
		if ( parent == null ) throw new IllegalArgumentException();

		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 10;
		parent.setLayout( layout );
		
		final Collection<IDomainClass<?>> classes = Domain.instance().classes();
		if ( classes.isEmpty() ) {
			// error message if no classes
			getViewSite().getActionBars().getStatusLineManager().setErrorMessage(
					ImageUtil.resize(
							Display.getCurrent().getSystemImage( SWT.ICON_ERROR ),
							new Point( 16, 16 ) ),
					GuiPlugin.getResourceString( EMPTY_DOMAIN_MSG_KEY ) );
		}
		else {
			for( IDomainClass<?> clazz : classes ) {
				Button button = new Button( parent, SWT.FLAT );
				button.setText( clazz.getName() );
				buttons.add( button );
			}
		}
	}

	@Override
	public void setFocus() {
		// does nowt
	}


	/**
	 * Test method 
	 * @return
	 */
	public List<Button> testGetButtons(){
		return buttons;
	}

}
