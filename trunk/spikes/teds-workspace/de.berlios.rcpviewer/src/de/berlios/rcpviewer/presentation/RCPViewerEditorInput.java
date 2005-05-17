package de.berlios.rcpviewer.presentation;

import net.sf.rcpplugins.utils.EclipseUtils;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * A simple IEditorInput wrapper for a given object.
 * 
 * @author ted stockwell
 */
public class RCPViewerEditorInput
implements IEditorInput
{

    Object _object;
	IRCPViewerLabelProvider _labelProvider;
	ImageDescriptor _imageDescriptor;
	

    public RCPViewerEditorInput(Object object)
    {
        _object = object;
		_labelProvider= (IRCPViewerLabelProvider)EclipseUtils.getAdapter(object, ILabelProvider.class);
		if (_labelProvider != null) {
			_imageDescriptor= new ImageDescriptor() {
				public ImageData getImageData() {
					Image image= _labelProvider.getImage(_object);
					if (image == null)
						return null;
					return image.getImageData();
				}			
			};
		}
    }
    
    public boolean exists()
    {
		// TODO
        return true;
    }

    public ImageDescriptor getImageDescriptor()
    {
        return _imageDescriptor;
    }

    public String getName()
    {
		if (_labelProvider != null)
			return _labelProvider.getText(_object);
        return _object.toString();
    }

    public IPersistableElement getPersistable()
    {
		// TODO
        return null;
    }

    public String getToolTipText()
    {
		if (_labelProvider != null)
			return _labelProvider.getToolTipText(_object);
        return _object.toString();
    }

    public Object getAdapter(Class adapterClass)
    {
		return EclipseUtils.getAdapter(this, adapterClass);
    }
}
