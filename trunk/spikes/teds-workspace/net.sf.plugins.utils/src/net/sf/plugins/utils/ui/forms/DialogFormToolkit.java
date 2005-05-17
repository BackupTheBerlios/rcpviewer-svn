package net.sf.plugins.utils.ui.forms;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * A toolkit suitable for creating dialog controls.
 * 
 * @author ted stockwell
 */
public class DialogFormToolkit extends FormToolkit {

	public DialogFormToolkit(Display pDisplay) {
		super(pDisplay);
	}

	public DialogFormToolkit(FormColors pColors) {
		super(pColors);
	}

	public void adapt(Control pControl, boolean pTrackFocus, boolean pTrackKeyboard) {
		Color b= pControl.getBackground();
		Color f= pControl.getForeground();
		try {
			super.adapt(pControl, pTrackFocus, pTrackKeyboard);
		}
		finally {
			pControl.setBackground(b);
			pControl.setForeground(f);
		}
	}
	
	public void adapt(Composite pComposite) {
		Color b= pComposite.getBackground();
		Color f= pComposite.getForeground();
		try {
			super.adapt(pComposite);
		}
		finally {
			pComposite.setBackground(b);
			pComposite.setForeground(f);
		}
	}
}
