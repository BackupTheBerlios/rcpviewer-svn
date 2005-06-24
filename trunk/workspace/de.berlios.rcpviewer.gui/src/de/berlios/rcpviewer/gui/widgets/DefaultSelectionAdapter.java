
package de.berlios.rcpviewer.gui.widgets;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * Convenience class that pipes all default selection events to normal
 * selection events.
 */
public class DefaultSelectionAdapter extends SelectionAdapter {

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionAdapter#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public final void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}
}
